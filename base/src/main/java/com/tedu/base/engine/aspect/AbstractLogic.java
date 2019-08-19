package com.tedu.base.engine.aspect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import com.googlecode.aviator.AviatorEvaluator;
import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.model.FieldCondition;
import com.tedu.base.engine.model.ClientContext;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.model.ServerTokenData;
import com.tedu.base.engine.service.FormTokenService;
import com.tedu.base.engine.util.FormConfiguration;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.engine.util.PanelValidator;
import com.tedu.base.initial.model.xml.ui.Control;
import com.tedu.base.initial.model.xml.ui.Panel;
import com.tedu.base.initial.model.xml.ui.Param;
import com.tedu.base.initial.model.xml.ui.Procedure;
import com.tedu.base.initial.model.xml.ui.Property;
import com.tedu.base.rule.AviatorUtil;
/**
 * 在logic执行前，准备参数
 * 在logic执行后加工返回结果
 * @author wangdanfeng
 *
 */
public abstract class AbstractLogic implements Logic{
	protected FormTokenService formTokenService;
	protected ServerTokenData serverTokenData;
	//TODO 入参等常用参数在基类中获取,避免在逻辑前中后阶段多次使用时需要反复查找
	private Param inParam;
	private Param outParam;
	
	public FormModel doBefore(FormEngineRequest requestObj){
		FormModel formModel = prepareModel(requestObj);
		if(formModel != null){
			formModel.setServerTokenData(getServerTokenData());
		}
		return formModel;
	}

	/**
	 * logic对应的controller执行后调用
	 * 主要用于加工返回数据
	 */
	public void doAfter(FormEngineRequest requestObj,FormEngineResponse res){
		if(serverTokenData== null) return;
		//如果有输出panel，记录对应数据方便解析表达式时使用
		cacheOutModel(requestObj,res.getData());
		//见具体逻辑处理类
		prepareResponse(requestObj,res);
		//逻辑token,组件可用性语句等
		prepareResponseCommon(requestObj,res);
	}
	
	/**
	 * 在有out panel参数的逻辑执行完毕返回前，缓存其数据内容。
	 * 解析当前UI内的表达式用
	 * @param data
	 * @param uiId
	 */
	private void cacheOutModel(FormEngineRequest requestObj,Object data){
		String uiId = requestObj.getWid();
		Procedure p = serverTokenData.getProcedure();
    	Param paramOut = p.getParam(Param.P_OutputPannelId);
    	//含.的Out是panel.control，这里只处理panel.有输出的panel型数据时,记录数据到缓存,方便不同表达式引用
    	if(paramOut != null && paramOut.getValue().indexOf('.')<0){//
			Panel outPanel = FormConfiguration.getPanel(serverTokenData.getUiName(), paramOut.getValue());
			if(outPanel!=null
					&& outPanel.getType().equals(Panel.TYPE_GROUP)
					&& data instanceof Map){//只记录普通表单的数据。其他表格什么的都不记录。用于未来解析UI中的表达式.
				formTokenService.setUIModel(uiId, paramOut.getValue(), data);//panel数据记录在treeNode的model属性里
			}
    	}
	}
	
	/**
	 * 从请求中
	 * @param requestObj
	 * @return
	 */
	public String getEditMode(FormEngineRequest requestObj){
		Map<String,Object> requestData = requestObj.getData();
		String editMode;
		//若save逻辑配置了editMode，则以配置为准
		Procedure pInput = FormConfiguration.getProcedure(serverTokenData);
		Param paramMode = pInput.getParam(Param.P_EditMode);
		if(paramMode != null){//如果当期逻辑中有指定Mode,覆盖
			editMode = paramMode.getValue();
		}else{
			editMode = ObjectUtils.toString(requestData.get(Param.P_EditMode));//优先从请求中获取Mode
		}
		return editMode;
	}
	/**
	 * 逻辑统一执行的部分
	 * 某些逻辑(isXXable)
	 * 执行后在response中携带的token相关属性
	 * 组件可用性语句
	 * 
	 * @param serverTokenData
	 * @return
	 */
	private void prepareResponseCommon(FormEngineRequest requestObj, FormEngineResponse resObj){
		if(serverTokenData==null) return;
		//response 携带method
		resObj.setMethod(serverTokenData.getLogic());

		ClientContext clientToken;
		
		if(isAntiDup()){
			formTokenService.renewToken(serverTokenData);//renew
			clientToken = new ClientContext(serverTokenData); 
		}else{
			clientToken = new ClientContext(serverTokenData.getUiName(),null);
		}

		String uiId = requestObj.getWid();
		//准备解析权限表达式、返回值表达式等需要的环境变量
		Map<String,Object> env = new HashMap<>();
		//解析返回消息通常只要逻辑相关的panel属性值就够了
		AviatorUtil.prepareEnv(env,resObj,requestObj,serverTokenData);//请求+返回 model.property和model.panel.property不要并存，
		AviatorUtil.prepareEnv(env,formTokenService.getUIModel(uiId));//这个方法有严重问题(map嵌套)，用下面语句代替
		AviatorUtil.prepareSessionEnv(env);//
		
		if(isControlUpdatable()){
			clientToken.setFilterStatement(
					formTokenService.genControlStatusStatements(uiId, env));
		}
		resObj.setToken(clientToken);
		
		setReturnMsg(env,resObj);

	}

	/**
	 * 组件名值替换为属性名值
	 * 下拉组件中若用到固定查询条件类型的值:如"未设置"等,需解释为实际意义的值
	 * 这个准备去掉
	 * @param formModel
	 */
	protected void translateForm(FormModel formModel){
		if(formModel == null) return;//do nothing
		List<Control> cList = formModel.getControlList();
		List<Property> pList = formModel.getPropertyList();
		Map<String,Object> mapData = formModel.getData();
		String val;
		String cName;
		String pName;
		String pType;
		for(Control c:cList){
			cName = c.getName();
			val = ObjectUtils.toString(mapData.get(cName)).trim();
			mapData.put(cName, val);
			if(c.getType().equalsIgnoreCase(Control.TYPE_COMBOBOX)){
				pName = ObjectUtils.toString(c.getProperty());
				if(val.isEmpty() || pName.isEmpty()) continue;
				for(Property p:pList){
					if(p.getName().equals(pName)){
						pType = p.getType();
						if(pType.equalsIgnoreCase(Property.TYPE_STRING)){
							val = val.replace(FieldCondition.NOT_NULL_FIELD_VALUE, "");//编辑表单中的未设置和查询时的未设置含义不同
						}
						mapData.put(cName,val);//更新值
					}
				}
			}
		}
	}
	
	/**
	 * Query数据填充到Panel，需要将propertyName转为controlName
	 * @param formModel
	 */
	protected void translateProperty(List<Control> cList,Map<String,Object> mapData){
		String val;
		String cName;
		String pName;
		for(Control c:cList){
			cName = c.getName();
			pName = c.getProperty();
			if(pName.isEmpty()) continue;
			val = ObjectUtils.toString(mapData.get(pName)).trim();
			mapData.put(cName, val);
		}
	}
	
	
	/**
	 * logic方法model参数构造及设置，执行
	 * @param FormEngineRequest
	 * @return
	 */
	public abstract FormModel prepareModel(FormEngineRequest requestObj);
	public abstract void prepareResponse(FormEngineRequest requestObj,FormEngineResponse res);

	
	public ServerTokenData consumeToken(FormTokenService formTokenService,String token){
		if(!token.isEmpty()){
			return formTokenService.consumeToken(token);
		}
		return null;
	}

	private void setReturnMsg(Map<String,Object> env,FormEngineResponse res){
		if(serverTokenData==null) return;
		Param paramMsg = serverTokenData.getProcedure().getParam(Param.P_Msg);
        if(paramMsg==null) return;
        
    	String msgConf = paramMsg.getValue();
		FormLogger.logFlow(String.format("Logic{%s} 解析表达式 {%s}",serverTokenData.getLogic(),
				msgConf),FormLogger.LOG_TYPE_DEBUG);
    	String msg = null ;
    	try{
    		msg = (String)AviatorEvaluator.execute(msgConf,env,true);
    	}catch(Exception e){
    		FormLogger.logExpression(String.format("解释语句异常:[%s] 数据:[%s]", msgConf,env),e.getMessage());
    	}
    	if(msg == null){
    		msg = ObjectUtils.toString(msgConf);//返回不能解析的表达式
    		FormLogger.logFlow(String.format("UI{%s}Logic{%s}的Msg参数值{%s}解析结果为空,请检查",
    				serverTokenData.getUiName(),
    				serverTokenData.getLogic(),msgConf),FormLogger.LOG_TYPE_WARN );
    	}
    	res.setMsg(ObjectUtils.toString(msg));
	}

	/**
	 * 根据token对应的对象描述,准备好logic使用的Model对象
	 * 多表时，用于构造主表model
	 * @param FormEngineRequest
	 * @param type
	 * @return
	 */
	public FormModel genModelByPanel(FormEngineRequest requestObj,String inOut){
		Map<String,Object> data = requestObj.getData();
		Procedure pInput = serverTokenData.getProcedure();
    	Param paramInputPanel = pInput.getParam(inOut);//Param.P_InputPanelIdrequestObj
    	String panelName = ObjectUtils.toString(paramInputPanel.getValue());
    	String mainPanel = panelName.split(",")[0];
    	FormModel formModel = new FormModel(serverTokenData.getUiName(),mainPanel,data);
    	formModel.setUpdateMaster(StringUtils.isNotEmpty(mainPanel));
    	return formModel;
	}
	
	public CustomFormModel genCustomModelByPanel(FormEngineRequest requestObj,String type){
		Procedure pInput = serverTokenData.getProcedure();
    	Param paramInputPanel = pInput.getParam(type);//Param.P_InputPanelId
    	return new CustomFormModel(serverTokenData.getUiName(),paramInputPanel.getValue(),requestObj.getData());
	}
	
	/**
	 * 查询类logic
	 * @param request
	 * @param serverTokenData
	 * @param args
	 * @return
	 */
	public FormModel prepareOutputModel(FormEngineRequest requestObj){
    	Procedure p = serverTokenData.getProcedure();;
    	Param param = p.getParam(Param.P_OutputPannelId);
		FormModel formModel = new FormModel(serverTokenData.getUiName(),param.getValue());
		Map<String,Object> data = requestObj.getData();
		formModel.setEditMode(ObjectUtils.toString(data.get(Param.P_EditMode)));
	    return formModel;
	}
	
	public void validate(FormModel formModel){
		if(formModel == null) return;//do nothing
		BindingResult result = new MapBindingResult(formModel.getData(), "data");
		PanelValidator panelValidator = new PanelValidator();
		panelValidator.validate(formModel, result);
		if(result.hasErrors()){
		    throw new ValidException(ErrorCode.INVALIDATE_FORM_DATA,result);
		}
	}
	/**
	 * 检测异常见BindingResult的error
	 * 常见的非写数据用的panel校验必填项
	 * @param formModel
	 * @return
	 */
	public BindingResult validateRequired4Query(List<Control> cList,Map<String,Object> data){
		BindingResult result = new MapBindingResult(data, "data");
		PanelValidator.validateRequired(cList,data, result,false);
		return result;
	}
	
	public FormTokenService getFormTokenService() {
		return formTokenService;
	}

	public void setFormTokenService(FormTokenService formTokenService) {
		this.formTokenService = formTokenService;
	}
	
	public ServerTokenData getServerTokenData() {
		return serverTokenData;
	}

	public void setServerTokenData(ServerTokenData serverTokenData) {
		this.serverTokenData = serverTokenData;
	}

	public Param getInParam() {
		return inParam;
	}

	public void setInParam(Param inParam) {
		this.inParam = inParam;
	}

	public Param getOutParam() {
		return outParam;
	}

	public void setOutParam(Param outParam) {
		this.outParam = outParam;
	}

	/**
	 * 当前逻辑实例的防重复提交属性
	 * 通常为Save,SaveCustom,ExecJob,Delete这类改变数据的逻辑
	 */
	public boolean isAntiDup(){
		return formTokenService.isAntidupLogic(serverTokenData.getLogic());
	}
	
	/**
	 * 当前逻辑实例的更新组件状态属性
	 * 通常为QueryById,Query,Transform等界面数据发生变化的逻辑
	 */
	public boolean isControlUpdatable(){
		return formTokenService.isControlUpdateLogic(serverTokenData.getLogic());
	}
}
