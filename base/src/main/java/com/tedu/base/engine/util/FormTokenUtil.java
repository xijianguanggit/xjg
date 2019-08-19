package com.tedu.base.engine.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;

import com.googlecode.aviator.AviatorEvaluator;
import com.tedu.base.engine.model.ServerTokenData;
import com.tedu.base.engine.service.FormTokenService;
import com.tedu.base.initial.model.xml.ui.Control;
import com.tedu.base.initial.model.xml.ui.Flow;
/**
 * 表单引擎token机制相关工具类
 * @author wangdanfeng
 *
 */
public class FormTokenUtil {
	public static final Logger log = Logger.getLogger(FormTokenUtil.class);
	private static final String CLAUSE_CHANGE_STATUS = "$('#%s_%s').%s('%s')";
	//系统配置组件类型与easyui组件类型对照
	private static HashMap<String,String> formCommonControlMap;
	static{
		formCommonControlMap = new HashMap<>();
		formCommonControlMap.put("button", "linkbutton");
		formCommonControlMap.put("popupbox", "searchbox");
		formCommonControlMap.put("textbox", "textbox");
		formCommonControlMap.put("combobox", "combobox");
		formCommonControlMap.put("datebox", "datebox");
		formCommonControlMap.put("kindeditor", "kindeditor");
		formCommonControlMap.put("Ueditor", "Ueditor");
	}
	
	private FormTokenUtil(){}
	/**
	 * 目前只有表格列需要区分
	 * @param c
	 * @return
	 */
	private static final String CONTROL_GRID_BUTTON = "link";//temp
	public static boolean isCommonControl(Control c){
		return !c.getType().equalsIgnoreCase(CONTROL_GRID_BUTTON);
	}
	
	public static boolean isPanelTrigger(String trigger ){
		return trigger.indexOf('.')<0;
	}

	
	
	/**
	 * 对过期的token重新验证后续期
	 * 只验证自己所在flow是否可用
	 * 如果不可用，继续抛出IllegalTokenException
	 * @param tokenData
	 */
	public static Boolean isEnabledFlow(ServerTokenData tokenData,Map<String,Object> env){
		Flow flow = FormConfiguration.getUI(tokenData.getUiName()).getFlow(tokenData.getTrigger(), tokenData.getEvent());
		return isEnabledFlow(flow,env);//为操作列触发事件时使用.这里没有运行时刻的数据对象,
	}
	
	/**
	 * 给前端生成的控制组件状态语句
	 * @param formModel
	 * @param c
	 * @param enabled
	 * @return
	 */
	public static String genControlStatusStatement(Control c,boolean enabled){
		String jsControlClass = formCommonControlMap.get(c.getType().toLowerCase());
		if(jsControlClass == null){
			return "";
		}
		
		
		String status = enabled?"enable":"disable";
		return String.format(CLAUSE_CHANGE_STATUS, 
				c.getPanelName(),c.getName(),
				jsControlClass,status) ;//$('#%s_%s').show(); 
	}
	
	/**
	 * 
	 * 授权按钮所在界面若使用定制模板，需将按钮在模板页面初始时设置成不可见，在查询等刷新权限数据语句中，设置为显示。
	 * @param c
	 * @return
	 */
	public static String genControlStatus(Control c,boolean enabled){
		String jsControlClass = formCommonControlMap.get(c.getType().toLowerCase());
		if(jsControlClass != null && jsControlClass.equals("linkbutton") && enabled){
			return String.format("$('#%s_%s').show()", 
				c.getPanelName(),c.getName()) ;//$('#%s_%s').show() 
		}else {
			return "";
		}
	}

	/**
	 * flow的filter结果
	 * 未配置filter视为可用
	 * 
	 * 20171012 加入新特性:在权限中配置的component类资源中，若含有当前flowId的，则表达式=true and 
	 * @param flow
	 * @param env
	 * @return boolean
	 */
	public static Boolean isEnabledFlow(Flow flow,Map<String,Object> env){
		String currentFilter = ObjectUtils.toString(flow.getFilter());
		if(currentFilter.isEmpty()) return true;
		Boolean ret = null;
		try{
			ret = (Boolean)AviatorEvaluator.execute(currentFilter,env,true);//缓存
			FormLogger.logFlow(String.format("解析表达式{%s}成功,结果={%s}", currentFilter,ret), FormLogger.LOG_TYPE_DEBUG);
		}catch(Exception e){
			//界面权限相关的filter表达式等，可能要最后一个逻辑执行时才能解析成功，解析异常是常态。
			FormLogger.logFlow(String.format("解析表达式{%s}异常 {%s}", currentFilter,e.getMessage()), FormLogger.LOG_TYPE_DEBUG);
		}
		return ret;
	}
	
	public static void restoreToken(FormTokenService formTokenService,ServerTokenData serverTokenData){
		if(serverTokenData!=null && !ObjectUtils.toString(serverTokenData.getToken()).isEmpty()){
			formTokenService.seToken(serverTokenData);
		}
	}

	
	public static void test1(){
		System.out.println("test1");
		String param = "123";
		test2(param);
	}
	public static void test2(String param){
		System.out.println("test2"+param);
	}

}
