package com.tedu.base.engine.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.ui.ModelMap;

import com.tedu.base.engine.util.FormConfiguration;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.initial.model.xml.ui.Control;
import com.tedu.base.initial.model.xml.ui.Flow;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.initial.model.xml.ui.ModuleObject;
import com.tedu.base.initial.model.xml.ui.Panel;
import com.tedu.base.initial.model.xml.ui.Param;
import com.tedu.base.initial.model.xml.ui.Property;
/**
 * 服务于表单引擎的view model
 * 属性值用map存储
 * 常用于表单引擎构造的表单的处理及增删改查
 * @author wangdanfeng
 *
 */
public class FormModel implements Serializable,BasicFormModel{
	private static final long serialVersionUID = -1158966438635067762L;
	//编辑模式
	public static final String MODE_NEW = "0";
	public static final String MODE_UPDATE = "1";	
	
	private String UIName;
	private String panelName;
	private boolean autoinc;
	private String optimisticProperty = "";//默认没有锁。来自save配置
	private boolean updateMaster = true;//用于判断是否需要save主表。配置中若In的第一个参数为空需要用一个标识体现，以避免在service层更新主表
	//目前支持单表
	private String tableName;//table name 
	private String primaryField;//table primary key name
	private Object primaryFieldValue;//table primary key value.//主键用.insert语句后需要将新增记录的主键值反射设置给此属性;udate时request中提供的主键值
	private String primaryControl = null;//primary control name
	private String foreignControl = null;//foreignKey control name
	private String editMode;//编辑模式：保存/新增
	private String sqlId;//编辑模式：保存/新增
	private Map<String,Object> data = new HashMap<String,Object>();
	private List<Control> controlList = null;//和增删改查相关的组件
	private List<Control> controlListAll = null;//配置
	private List<Property> propertyList = null;//和增删改查相关的属性
	private List<Property> propertyListAll = null;//配置
	protected String selectSql;
	protected String updateSql;
	protected String insertSql;
	private ServerTokenData serverTokenData;
	private List<FormModel> detailList;//子表
	private List<ConstraintsModel> contraints;
	private List<ConstraintsModel> cascade;
	public enum Mode{  
        Insert("Add"),
        Update("Edit"),
        Readonly("Readonly");
        public String value;  
        Mode(String value){  
        	this.value = value;  
        }  
    }  
	
	public FormModel(){
	}
	
	
	public FormModel(String uiName,String panelName){
		this.UIName = uiName;
		this.panelName = panelName;
		//表单处理时需对controlList和propertyList做筛选
		//复制
		controlList = new ArrayList<>();
		controlListAll = new ArrayList<>();
		Panel panel = getPanel();
		ModuleObject modelObj = getModel();

		if(panel != null){
			controlList.addAll(panel.getControlList());
			controlListAll.addAll(controlList);
		}
		if(modelObj != null){
			propertyList = new ArrayList<>();
			propertyListAll = new ArrayList<>();
			propertyList.addAll(getModel().getPropertyList());
			propertyListAll.addAll(propertyList);//准备一份全集备份
			//主键及表名
			setTableName(modelObj.getTable());
			setPrimaryField(modelObj.getPrimary());//主键字段已获取
			setPrimaryControl();//继续获取主键字段对应的组件   //TO改进构建model，减少前后依赖
			setPrimaryFieldValue(data.get(getPrimaryControl()));
			setAutoinc(modelObj.isAutoinc());//是否自增
			//有model时，将DATA转换成以propertyName命名。原有key可暂时不动
			if(!ObjectUtils.toString(modelObj.getTable()).isEmpty() && 
				!ObjectUtils.toString(modelObj.getPrimary()).isEmpty()){
				FormConfiguration.filterSqlField(controlList, propertyList);//每个control对象里都有property对象实例
			}
		}
		prepareDataByPropertyName();//必须
	}
	
	public FormModel(String uiName,String panelName,HttpServletRequest request){
		this(uiName,panelName);
		this.setFormDataByRequest(request);
	}
	
	public FormModel(String uiName,String panelName,Map<String,Object> data){
		this(uiName,panelName);
		this.setFormDataByMap(data);
	}
	
	
	public void prepareDataByPropertyName(){
		prepareDataByPropertyName(getData());
	}
	
	public void prepareDataByPropertyName(Map<String, Object> data){
		if(data==null) return;
		try{
		    for(Control c:controlList){
		    	setPropertyValue(data,c);
		    }
		}catch(Exception e){
			FormLogger.logConf("返回数据时根据属性名构建key值失败", e.getMessage());
		}
	}
	
	/**
	 * 循环control,将control对应的属性名值也构造出来，方便引用
	 * @param data
	 * @param c
	 */
	private void setPropertyValue(Map<String, Object> data,Control c){
		if(c.getPropertyObj()==null) return;
		String propertyName = c.getPropertyObj().getName();
		String propertyField = c.getPropertyObj().getField();
		String dataKey = "";
		
		if(data.containsKey(c.getName())){
			dataKey = c.getName();
		}else if(propertyField!=null && data.containsKey(propertyField)){
			dataKey = propertyField;
		}
		if(!data.containsKey(propertyName)){
			data.put(propertyName, data.get(dataKey));
		}
	}
	
	@SuppressWarnings("unchecked")
	public FormModel(ModelMap modelMap){
		setData((HashMap<String,Object>)(modelMap.clone()));
	}
	public FormModel(Map<String,Object> data){
		setData(data);
	}
	public void put(String key,String value){
		getData().put(key, value);
	}

	@Override
	public Map<String, Object> getData() {
		return data;
	}


	/**
	 * 定义方法用于获取表单中的数据
	 * @param ctlName
	 * @return
	 */
	public Object getData(String ctlName) {
		return data.get(ctlName);
	}
	
	private String getRealControlName(String ctlName){
		return getPanel().getName() + "_" + ctlName;
	}
	public Object setData(String ctlName,String val) {
		return data.put(getRealControlName(ctlName),val);
	}
	
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	public String getUIName() {
		return UIName;
	}

	public String getPanelName() {
		return panelName;
	}


	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPrimaryField() {
		return primaryField;
	}

	public void setPrimaryField(String primary) {
		this.primaryField = primary;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getEditMode() {
		return editMode;
	}

	public void setEditMode(String editMode) {
		this.editMode = editMode;
	}
	
	/**
	 * 是编辑模式
	 * 常用判断
	 * @return
	 */
	public boolean isEdit() {
		return editMode.equals(Mode.Update.value);
	}

	public boolean isAdd() {
		return editMode.equals(Mode.Insert.value);
	}

	public String getSaveTypeByEditMode(){
		return editMode.equals(Mode.Update.value)?"update":"insert";//save
	}
	
	/**
	 * 从内存中获取当前表单数据对象对应的配置
	 * @return
	 */
	public Panel getPanel(){
		return FormConfiguration.getPanel(getUIName(), getPanelName());
	}

	
	public List<Flow> getFlow(){
		return FormConfiguration.getFlow(getUIName());
	}
	
	public ModuleObject getModel(){
		if(getPanel() == null) return null;
		return FormConfiguration.getModel(getUIName(), getPanel().getObject());
	}
	public String getPrimaryFieldValue() {
		return primaryFieldValue==null?"":primaryFieldValue.toString();
	}
	public void setPrimaryFieldValue(Object primaryFieldValue) {
		this.primaryFieldValue = primaryFieldValue;
	}


	public List<Control> getControlList() {
		return controlList;
	}


	public void setControlList(List<Control> controlList) {
		this.controlList = controlList;
	}


	public List<Property> getPropertyList() {
		return propertyList;
	}

	public List<Property> getPropertyListAll() {
		return propertyListAll;
	}

	public void setPropertyList(List<Property> propertyList) {
		this.propertyList = propertyList;
	}
	
	public String getInsertSql(){
		//开始构造updateSql
		//以上信息应从配置对象中获取
		String sql = 
			new SQL() {{ 
				INSERT_INTO(getTableName());
				Property property ;
				for(Control control:controlList){
					property = control.getPropertyObj();//组件绑定的属性对象
					//仅更新传值的字段
					if(!isInsertField(control,property)) continue;
					if(property.getType().equals(Property.TYPE_STRING) || 
							!ObjectUtils.toString(getData(control.getHtmlName())).isEmpty()){
						VALUES(property.getField(),
								String.format(FormConstants.FIELD_INSERT_TEMPLATE, control.getHtmlName()));
					}
				}
		  }}.toString();
		  return sql;
	}
	
	
	/**
	 * 自增主键时，主键字段不参与insert sql构建
	 * @param formModel
	 * @param control
	 * @param property
	 * @return
	 */
	private boolean isInsertField(Control control,Property property){
		if(property==null){
			FormLogger.logFlow("control["+control.getName() + "]的属性找不到",FormLogger.LOG_TYPE_WARN);
			return false;
		}else{
			return getData(control.getHtmlName()) != null && !ObjectUtils.toString(property.getField()).isEmpty() && 
				(!isAutoinc() || (isAutoinc() && !property.getField().equals(getPrimaryField())));
		}
	}
	
	/**
	 * 根据formModel构造预编译的UPDATE SQL
	 * In中的组件决定SQL
	 * 字符类型或非空字段
	 * 其他设置为null
	 * 如果有乐观锁，在校验阶段，已确保乐观锁有对应值
	 * 乐观锁支持Long型，带乐观锁save逻辑，更新时对应字段值递增1,，且where条件中 and 字段=传入值
	 */
	public String getUpdateSql(){
		//开始构造updateSql
		//以上信息应从配置对象中获取
		return 
			new SQL() {{ 
				UPDATE(tableName);
				Property property ;
				for(Control control:controlList){
					property = control.getPropertyObj();//组件绑定的属性对象
					Object val = getData().get(control.getHtmlName());
					String fieldName = ObjectUtils.toString(property.getField());
					if(val == null || fieldName.isEmpty()) continue;//没绑定字段的忽略 
					//仅更新传值的字段
					if(ObjectUtils.toString(control.getProperty()).equals(getOptimisticProperty())){
						SET(String.format("%s=%s+1", fieldName,fieldName));
						WHERE(String.format("%s=#{data.%s}", fieldName,getOptimisticProperty()));	
					}else if(property.getType().equalsIgnoreCase(Property.TYPE_STRING) || 
							!ObjectUtils.toString(getData().get(control.getHtmlName())).isEmpty()){
						SET(String.format(FormConstants.FIELD_VALUE_TEMPLATE, fieldName,control.getHtmlName()));
					}else{//空字符串存入非string字段的含义=set null,有值时,不做处理
						SET(String.format(FormConstants.FIELD_NULL_TEMPLATE, fieldName));
					}
				}
				WHERE(String.format("%s=#{primaryFieldValue}", primaryField));
		  }}.toString();
	}
	
	public String getSelectSql() {
		return selectSql;
	}


	public void setSelectSql(String selectSql) {
		this.selectSql = selectSql;
	}
	/**
	 * delete by id
	 * @return
	 */
	public String getDeleteSql(){
		return String.format("delete from %s where %s='%s'"
				,getTableName()
				,getPrimaryField()
				,getPrimaryFieldValue());
	}

	/**
	 * changeLog可以从formModel产生
	 * @return
	 */
	public ChangeLog getChangeLog(){
		ChangeLog changeLog = new ChangeLog();
		changeLog.setEntityName(getTableName());
		if(isEdit()){
//			changeLog.setOldData(formLogService.getData(formModel));//先查库
		}
		return changeLog;
	}


	public String getSqlId() {
		return sqlId;
	}


	public void setSqlId(String sqlId) {
		this.sqlId = sqlId;
	}
	
	/**
	 * JSON POST
	 * @param data
	 */
	public void setFormDataByMap(Map<String,Object> data){
		setData(data);
		setEditMode(ObjectUtils.toString(data.get(Param.P_EditMode)));
		setPrimaryFieldValue(data.get(getPrimaryControl()));
		prepareDataByPropertyName();
	}
	
	private void setFormDataByRequest(HttpServletRequest request){
		HashMap<String,Object> data = new HashMap<String,Object>();
		data.putAll(request.getParameterMap());
		String currentKey ;
		for (Map.Entry<String, Object> entry : data.entrySet()) {  
			 currentKey = entry.getKey();
			 String[] arr = (String[])data.get(currentKey);
			 if(arr[0].isEmpty()){
				 data.put(currentKey,arr[0]);//未录入提交的数据项设置为null(新增/编辑是否需要不同对待)
			 }else{
				 data.put(currentKey, arr[0]);
			 }
		 }  
		 setEditMode(ObjectUtils.toString(data.get(Param.P_EditMode)));
		 if(getModel()==null) {
			 FormLogger.logConf("formModel.model is null", "");
			 return;
		 }
		 //TODO从请求参数里获取不够灵活，改为从后台配置取主键对应的control对应的参数值作为primaryFieldValue
//		 String primaryKeyValue = ObjectUtils.toString(request.getParameter("primary"));//从客户端decodeid获取并放入到post data
		 setPrimaryFieldValue(request.getParameter(getPrimaryControl()));
		 setData(data);
		 prepareDataByPropertyName();
	}

	public ServerTokenData getServerTokenData() {
		return serverTokenData;
	}


	public void setServerTokenData(ServerTokenData serverTokenData) {
		this.serverTokenData = serverTokenData;
	}


	public List<Control> getControlListAll() {
		return controlListAll;
	}


	public void setControlListAll(List<Control> controlListAll) {
		this.controlListAll = controlListAll;
	}

	public String getPrimaryControl() {
		return primaryControl;
	}

	public void setPrimaryControl() {
		List<Control> cList = getControlListAll();
		List<Property> pList= getPropertyListAll();
		Property property = null;
		Control control = null;
		for(Property p:pList){
			if(!primaryField.isEmpty() && ObjectUtils.toString(p.getField()).equalsIgnoreCase(primaryField)){
				property = p;
				break;
			}
		}
		if(property==null) {
			return;
		}
		for(Control c:cList){
			if(ObjectUtils.toString(c.getProperty()).equals(property.getName())){
				control = c;
				primaryControl = control.getName();//panel.controlName
				break;
			}
		}
	}


	public void setPrimaryControl(String primaryControl) {
		this.primaryControl = primaryControl;
	}


	public boolean isAutoinc() {
		return autoinc;
	}


	public void setAutoinc(boolean autoinc) {
		this.autoinc = autoinc;
	}


	public List<FormModel> getDetailList() {
		return detailList==null?Collections.emptyList():detailList;
	}


	public void setDetailList(List<FormModel> detailList) {
		this.detailList = detailList;
	}
	/**
	 * 通常放多记录描述List<Map>
	 * @return
	 */
	public Object getDetailData(){
		return data.get("detail");
	}
	/**
	 * @param detail
	 */
	public void setDetailData(Object detail){
		 data.put("detail",detail);
	}


	public String getForeignControl() {
		return foreignControl;
	}


	public void setForeignControl(String foreignControl) {
		this.foreignControl = foreignControl;
	}


	public List<ConstraintsModel> getContraints() {
		return contraints;
	}

	public List<ConstraintsModel> getCascade() {
		return cascade;
	}
	
	/**
	 * 外键关系检查表
	 * @param contraints
	 */
	public void setContraints(List<ConstraintsModel> contraints) {
		this.contraints = contraints;
		if(contraints != null){
			for(ConstraintsModel c:contraints){
				c.setValue(getPrimaryFieldValue());
			}
		}
	}
	/**
	 * 级联删除表
	 * @param cascade
	 */
	public void setCascade(List<ConstraintsModel> cascade) {
		this.cascade = cascade;
		if(cascade != null){
			for(ConstraintsModel c:cascade){
				c.setValue(getPrimaryFieldValue());
			}
		}
	}


	public String getOptimisticProperty() {
		return optimisticProperty;
	}


	public void setOptimisticProperty(String optimisticProperty) {
		this.optimisticProperty = optimisticProperty;
	}
	
	public boolean hasOptimisticLock(){
		return optimisticProperty!=null && !optimisticProperty.trim().isEmpty();
	}


	public boolean isUpdateMaster() {
		return updateMaster;
	}


	public void setUpdateMaster(boolean updateMaster) {
		this.updateMaster = updateMaster;
	}
	
	
}
