package com.tedu.base.engine.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.ObjectUtils;
import org.apache.ibatis.jdbc.SQL;
import org.apache.log4j.Logger;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.model.UniqueModel;
import com.tedu.base.initial.model.xml.ui.Control;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.initial.model.xml.ui.ModuleObject;
import com.tedu.base.initial.model.xml.ui.Property;

/**
 * 唯一性校验
 * 1、根据描述找到需要校验的唯一性字段组合的集合
 * 2、逐个校验，检查unique当前组的唯一性字段们是否全在当前panel，如果全在就执行唯一性校验，如果部分在,抛出配置错误,如果全部在,不校验
 * 在service层开始时执行。需注意新增和更新逻辑对唯一性校验的不同处理方式
 * 返回uniqueModel，用于描述唯一性校验需执行的SQL等
 * @author wangdanfeng
 *
 */
public class UniqueValidator{
	public static final Logger log = Logger.getLogger(UniqueValidator.class);
	private UniqueValidator(){}
	public static List<UniqueModel> validate(FormModel formModel){
		ModuleObject modelObject = formModel.getModel();
		//检查唯一性校验的基本条件是否具备
		if(modelObject==null){
			FormLogger.logFlow(String.format("未绑定Model,忽略唯一性校验 {%s}", formModel.getUIName()), FormLogger.LOG_TYPE_WARN);
			return Collections.emptyList();
		}
		String unique = ObjectUtils.toString(modelObject.getUnique()).trim();
		if(unique.isEmpty()){
			FormLogger.logFlow(String.format("未设置unique,忽略唯一性校验 {%s}", formModel.getUIName()), FormLogger.LOG_TYPE_WARN);
			return Collections.emptyList();
		}
		if(ObjectUtils.toString(modelObject.getPrimary()).trim().isEmpty()){
			FormLogger.logFlow(String.format("绑定的Model没有设置primary,忽略唯一性校验 {%s}", formModel.getUIName()), FormLogger.LOG_TYPE_WARN);
		}
		//开始唯一性校验
		formModel.setPrimaryField(modelObject.getPrimary());
		formModel.setTableName(modelObject.getTable());
		List<Control> cList = formModel.getControlList();//panel组件列表
		List<Property> pList = formModel.getPropertyList();//model属性列表
		
		String[] uniqueGroup = unique.split("[|]");
		//逐组校验唯一性数据条件是否完备
		List<UniqueModel> lstUnique = new ArrayList<>();
		for(String groupFields:uniqueGroup){
			String[] arrFields = groupFields.split(",");
			Arrays.sort(arrFields);
			if(Arrays.binarySearch(arrFields,modelObject.getPrimary())>=0){
				//主键不参与唯一性校验
				continue;
			}
			UniqueModel uniqueModel = checkConfigAndValidate(formModel,cList,pList,groupFields);
			lstUnique.add(uniqueModel);
		}
		return lstUnique;
	}
	
	/**
	 * 根据配置和当前提交的表单数据，验证唯一性值是否完备
	 * 若值不完备，无法进行数据唯一性校验
	 * @param formModel
	 * @param cList
	 * @param pList
	 * @param fields
	 */
	private static UniqueModel checkConfigAndValidate(FormModel formModel,List<Control> cList,
			List<Property> pList,String groupFields) {
		String[] fields = groupFields.split(",");
	    FormConfiguration.filterSqlField(cList, pList);//过滤掉不需要参与SQL拼接的字段
	    Map<String, Control> mapPropertyControl = new HashMap<>();
	    Map<String, Property> mapFieldProperty = new HashMap<>();
	    //以属性名为key的组件配置map
	    try{
		    mapPropertyControl = cList.stream().collect(
		            Collectors.toMap(Control::getProperty, p -> p));
	    }catch(Exception e){
	    	FormLogger.logConf("配置错误,[UI]|[Panel]中可能有多个Control绑定到同一Property"
	    			, String.format("[%s]|[%s]", formModel.getUIName(),formModel.getPanelName()));
	    }
	    try{
		    //当前model的field作为key，配置map
		   mapFieldProperty = pList.stream().collect(
		            Collectors.toMap(Property::getField, p -> p));
	    }catch(Exception e){
	    	FormLogger.logConf("配置错误,[UI]|[Panel]的model中可能有多个Property绑定到同一Field"
	    			, String.format("[%s]|[%s]", formModel.getUIName(),formModel.getPanelName()));
	    }
	    
	    int len = fields==null?0:fields.length;
	    int cntNotNullValue = 0;
	    String[] fieldsHtmlName = new String[len]; //字段对应的组件htm名
	    String[] fieldsTitle = new String[len];
	    for(int i=0;i<len;i++){
	    	Property p = mapFieldProperty.get(fields[i]);
	    	Control control = mapPropertyControl.get(p.getName());
	    	if(control==null){// &&formModel.getEditMode().equals(FormModel.Mode.Update.value)
	    		FormLogger.logFlow(String.format("忽略编辑模式下未提交唯一键值时的校验 {%s}", p.getName()), FormLogger.LOG_TYPE_WARN);
	    		cntNotNullValue++;
	    		continue;
	    	}
	    	fieldsHtmlName[i] = control.getHtmlName();
	    	fieldsTitle[i] = control.getTitle();
	    	Object val = formModel.getData(fieldsHtmlName[i]);
	    	if(p.getField().equals(formModel.getModel().getPrimary())
	    			){//当前必填键是主键且是自增类型时，可以忽略掉此项校验
	    		val = "-1";
	    		formModel.setData(fieldsHtmlName[i], ObjectUtils.toString(val));
	    	}
	    	if(val != null) {
	    		cntNotNullValue++;//发现主键约束中的字段对应的值时，若非空，记录
	    	}else{
				throw new ServiceException(ErrorCode.REQUIRED_FIELD_MISSING,
						String.format("[%s][%s]", 
								formModel.getUIName(),
								control.getName()));		
	    	}
	    }
	    if(cntNotNullValue != len){
			throw new ServiceException(ErrorCode.REQUIRED_FIELD_MISSING,
					String.format("[%s][%s]唯一性校验时", 
							formModel.getUIName(),
							groupFields));		    	
	    }
	    return genUniqueModel(formModel, fields, fieldsHtmlName,fieldsTitle);
	}
	
	private static UniqueModel genUniqueModel(
			FormModel formModel,String[] fields,
			String[] fieldsHtmlName,
			String[] fieldsTitle){
	    UniqueModel uniqueModel = new UniqueModel();
	    
		int len = fields==null?0:fields.length;
		String[] fieldsValue = new String[fields.length];
		String sql = new SQL(){{
			SELECT(formModel.getPrimaryField());
			FROM(formModel.getTableName());
			for(int i=0;i<len;i++){
				WHERE(String.format(FormConstants.FIELD_VALUE_TEMPLATE, fields[i],fieldsHtmlName[i]));
				fieldsValue[i] = ObjectUtils.toString(formModel.getData().get(fieldsHtmlName[i]));
			}
			if(formModel.isEdit()){//编辑模式下验证唯一键需要排除记录自身
				WHERE(String.format("%s <> #{primaryFieldValue} ", formModel.getPrimaryField(),
						formModel.getPrimaryFieldValue()));
			}
			if(len==1){
				WHERE(String.format("%s is not null ", fields[0],
						formModel.getPrimaryFieldValue()));
				WHERE(String.format("%s != '' ", fields[0],
						formModel.getPrimaryFieldValue()));				
			}
		}}.toString();
		
		uniqueModel.setSql(sql.concat("limit 1 "));//只返一条
	    
	    uniqueModel.setFieldsTitle(fieldsTitle);
	    uniqueModel.setFieldsValue(fieldsValue);
	    return uniqueModel;
	}
}
