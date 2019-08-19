package com.tedu.base.rule;
/**
 * github地址https://github.com/killme2008/aviator

 * 使用手册 https://github.com/killme2008/aviator/wiki
 */

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.utils.CloneUtils;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.CustomFunctionLoader;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.model.ServerTokenData;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.initial.model.xml.ui.FormConstants;

public class AviatorUtil {
	private static final Map<String,Object> env = new HashMap<String,Object>();
	{
		CustomFunctionLoader.load();
	}

	public static void main(String[] args){
		CustomFunctionLoader.load();
		int[] a = new int[]{1,2,3};
		String[] expressions = new String[]{
				"StartWith(model.code,'01')",
				"'xxxxx'==self",
				"女",
				"F",
				"1",
//				"checkEmail(self)",
//				"checkMobile(self)",
//				"currentTime()",
//				"getSessionKey('as')",
//				"seq.every(a,a)",
//				"checkChinese(self)",
				"editMode in ('Add','Edit')",
				"TokenUtils.genUUID()",
				"",
				"",
				""
		};
//        System.out.println(AviatorEvaluator.execute("seq.every(a,seq.and(seq.ge(0), seq.lt(10)))", env));
		Map<String, Object> env = new HashMap<String, Object>();
        env.put("self", "xxxxx");
        env.put("editMode", "Add");
		
//        return (Boolean)AviatorEvaluator.execute(expression,env);
//		for(String exp:expressions){
//			if(exp.isEmpty()) continue;
//			Object result = AviatorEvaluator.execute(exp,env);
//			System.out.println(String.format("%s...........%s", exp, result));
//
//		}

		//Contains函数
//		env.put("s1", "addsdfdd");
//		env.put("s2", "a");
//		env.put("s", "sdfddd");
        
        env.put("code", "a");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("code", "013");
		env.put(FormConstants.AVIATOR_ENV_MODEL, model);
		System.out.println(AviatorEvaluator.execute("StartWith(code,'0102')",env));


		//System.out.println("Contain:+++"+AviatorEvaluator.execute("Contain(s1,s2)",env));

		//System.out.println("Length:+++"+AviatorEvaluator.execute("Length(s)",env));



//		env.put("editMode", "Add");
//		System.out.println("editMode=='Add' ......"+AviatorEvaluator.execute("editMode=='Add'",env));
//		System.out.println("editMode=='Add' ......"+AviatorEvaluator.execute("editMode!='AAA'",env));

	}

	public static void prepareSessionEnv(Map<String,Object> env){
		Map<String,Object> mapSession = new HashMap<String,Object>();
		mapSession.put(FormConstants.AVIATOR_ENV_SESSION_USERINFO, SessionUtils.getUserInfo());
		mapSession.put(FormConstants.AVIATOR_ENV_SESSION_EXTINFO, SessionUtils.getExtInfo());
		mapSession.put(FormConstants.AVIATOR_ENV_SESSION_UI_PARAMETER, 
				SessionUtils.getParameter()==null?new HashMap():SessionUtils.getParameter());
		
		env.put(FormConstants.AVIATOR_ENV_SESSION, mapSession);
	}
	
	/**
	 * 
	 * @Description: 由于panel和control是变的，所以在此要设置2个map为了在配置时直接通过"."就可以告诉后台具体验证某个control
	 * @author: gaolu
	 * @date: 2017年9月30日 下午3:53:21  
	 * @param:      
	 * @return: void
	 */
	public static void prepareControlEnv(Map<String,Object> env,FormModel formModel){
		Map<String,Object> formModelMap = new HashMap<String,Object>();
		formModelMap = formModel.getData();
		env.put("param", formModelMap);
	}
/*	public static void prepareControlEnv(Map<String,Object> env,FormModel formModel,String controlName,Object controlValue){
		Map<String,Object> formModelMap = new HashMap<String,Object>();
		Map<String,Object> controlMap = new HashMap<String,Object>();
		formModelMap.put(formModel.getPanelName(), controlMap);
		controlMap.put(controlName, controlValue);
		env.put("param", formModelMap);
	}*/

	public static void prepareFormEnv(Map<String,Object> env,FormModel formModel){
		if(formModel != null){
			env.put(FormConstants.AVIATOR_ENV_MODE, formModel.getEditMode());
			env.put(FormConstants.AVIATOR_ENV_MODE_ALIAS, formModel.getEditMode());
			env.put(FormConstants.AVIATOR_ENV_MODEL, formModel.getData());
		}
	}
	
	

    /**
     * 执行validate表达式用
     * 除基本环境变量session和model外，还需引入self
     * @param value
     *
     * @return
     */
    public static boolean validExpression(String expression, FormModel formModel,Object currentValue) {
        Map<String, Object> env = new HashMap<String, Object>();
        prepareEnv(env,formModel);
        env.put(FormConstants.AVIATOR_ENV_SELF, currentValue);
        return (Boolean) AviatorEvaluator.execute(expression, env);
    }
    
    /**
     * 执行validate分支表达式用
     * 由于panel和control是变的，所以在此要设置2个map为了在配置时直接通过"."就可以告诉后台具体验证某个control
     *
     * @return
     */
/*    public static boolean validExpression(String expression, FormModel formModel) {
    	
		Map<String,Object> formModelMap = new HashMap<String,Object>();
		Map<String,Object> controlMap = new HashMap<String,Object>();
		formModelMap.put(formModel.getPanelName(), controlMap);
		controlMap.put(controlName, controlValue);
		env.put("param", formModelMap);
    	
    	Map<String, Object> env = new HashMap<String, Object>();
    	prepareEnv(env,formModel);
        return (Boolean) AviatorEvaluator.execute(expression, env);
    }*/
    public static boolean validExpression(String expression, FormModel formModel) {
    	Map<String, Object> env = new HashMap<String, Object>();
    	prepareControlEnv(env,formModel);
        return (Boolean) AviatorEvaluator.execute(expression, env);
    }

    
	/**
	 * 常用场景
	 * 构造好的formModel和session作为解析表达式的环境变量	 
	 * 1、session
	 * 2、model
	 * 3、editMode
	 * @param env
	 * @param formModel
	 */
	public static void prepareEnv(Map<String,Object> env,FormModel formModel){
		prepareFormEnv(env,formModel);
		prepareSessionEnv(env);
	}
	
	public static void prepareEnv(Map<String,Object> env,HttpServletRequest request){
		prepareSessionEnv(env);
		env.put(FormConstants.AVIATOR_ENV_LOGIC, request.getParameterMap());
	}

	public static void prepareEnv(Map<String,Object> env,HttpServletRequest request,FormEngineResponse res){
		prepareEnv(env,request);
		env.put(FormConstants.AVIATOR_ENV_MODEL, res.getData());
	}
	
	public static void prepareEnv(Map<String,Object> env,Map<String,Object> logicData,FormEngineResponse res){
		try{
			env.put(FormConstants.AVIATOR_ENV_LOGIC, CloneUtils.clone(logicData));
			env.put(FormConstants.AVIATOR_ENV_MODEL, CloneUtils.clone(res.getData()));
		}catch(Exception e){
			 
		}
	}

	/**
	 * 增加editMode
	 * @param serverTokenData
	 * @param env
	 * @param logicData
	 * @param res
	 */
	public static void prepareEnv(Map<String,Object> env,FormEngineResponse res,FormEngineRequest requestObj,ServerTokenData serverTokenData){
		prepareEnv(env,requestObj.getData(),res);
		if(serverTokenData!=null){
			String editMode = SessionUtils.getWindowMode(requestObj.getWid());// 同ui可能来自不同路径,使用uiId保存对应的editMode
			env.put(FormConstants.AVIATOR_ENV_MODE, editMode);
			env.put(FormConstants.AVIATOR_ENV_MODE_ALIAS, editMode);
		}
	}
	/**
	 * model.panelName.property和model.property在putAll时会覆盖。使用以下方法合并
	 * @param env
	 * @param panelData
	 */
	public static void prepareEnv(Map<String,Object> env,Map<String,Object> panelsData){
		if(panelsData==null || env==null) return;
		if(env.containsKey(FormConstants.AVIATOR_ENV_MODEL)){
			Object modelData = env.get(FormConstants.AVIATOR_ENV_MODEL);
			if(modelData instanceof Map){
				panelsData.forEach((key, value) -> {
					try {
						((Map)modelData).put(key, CloneUtils.clone(value));
					} catch (Exception e) {
						FormLogger.warn("clone not supported");
					}
				});
			}
		}else{
			env.put(FormConstants.AVIATOR_ENV_MODEL,panelsData);
		}
	}
	


}
