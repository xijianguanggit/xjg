package com.tedu.base.engine.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.aviator.AviatorEvaluator;
import com.tedu.base.auth.login.model.ShareModel;
import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.utils.ContextUtils;
import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.model.ServerTokenData;
import com.tedu.base.initial.model.xml.ui.Control;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.initial.model.xml.ui.Param;
import com.tedu.base.rule.AviatorUtil;

/**
 * 
 * @author wangdanfeng
 *
 */
public class FormUtil {
	public static final Logger log = Logger.getLogger(FormUtil.class);
	private static final String KEY_REDIRECT = "redirect";
	private FormUtil(){}
	/**
	 * 组件默认值设置
	 * 表达式解析不了的（解析结果为null）,将属性值本身作为初始值
	 * @param formModel
	 */
	public static Map<String,Object> getInitialData(FormModel formModel){
		Map<String,Object> env = new HashMap<>();
		AviatorUtil.prepareEnv(env,formModel);
		
		Map<String,Object> map = new HashMap<>();	
		List<Control> cList = formModel.getControlListAll();
		for(Control c:cList){
			String initialExpression = ObjectUtils.toString(c.getInitial());
			if(!initialExpression.isEmpty()){
				Object val = AviatorEvaluator.execute(initialExpression,env,true);
				if(val == null) {
					throw new ServiceException(ErrorCode.ILLEGAL_FORM_CONFIGURATION,
							String.format("组件名的initial表达式[%s]解析结果为null {%s}", c.getName(),initialExpression));
				}
				map.put(c.getName(), val);
			}else{
				map.put(c.getName(), "");
			}
		}
		return map;
	}
	
	public static String toJsonString(Object obj){
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);
		try{
			return objectMapper.writeValueAsString(obj);  
		}catch(Exception e){
			return "{error:error}";
		}
	}
	
    public static Object toJsonObj(String json, Class pojoClass) throws Exception{
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		try{
			return objectMapper.readValue(json, pojoClass);  //objectMapper.convertValue(json, ShareModel.class)
		}catch(Exception e){
			return "{error:error}";
		}    	
    }  

	
	/**
	 * 给查询条件场景下的下拉组件增加默认
	 * @param listData
	 */
	public static void appendDefaultQueryItems(List<Map<String,Object>> listData,
			ServerTokenData serverTokenData){
		Param paramOptions = FormConfiguration.getProcedure(serverTokenData).getParam(Param.P_Options);
		if(paramOptions==null) return;
		String otherOptions = 
				ObjectUtils.toString(paramOptions.getValue());
		String[] arrOptions = otherOptions.split("[|]");
		Map<String,Object> mapItem;
		List<Map<String,Object>> lstOthers = new ArrayList<>();
		for(String op:arrOptions){
			mapItem = new HashMap<>();
			String[] options = op.split(",");
			if(options!=null && options.length>0){
				mapItem.put("value", options[0]); 
				mapItem.put("text", options.length>1?options[1]:"");
				lstOthers.add(mapItem);
			}
		}
		listData.addAll(0, lstOthers);
	}
	
	/**
	 * 将bean转成map对象，并遵循系统一致的规则:空值不转，日志格式化.
	 * @param bean
	 * @return
	 */
	public static Map<String, Object> getBeanMap(Object bean){
		return getBeanMap(bean,true);
	}

	public static Map<String, Object> getBeanMap(Object bean,boolean ignoreEmpty){
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		if(ignoreEmpty){
			objectMapper.setSerializationInclusion(Include.NON_EMPTY);
		}
		objectMapper.setDateFormat(new SimpleDateFormat(DateUtils.YYMMDD_HHMMSS_24));
	    @SuppressWarnings("unchecked")
		Map<String, Object> data = objectMapper.convertValue(bean, Map.class);
	    return data;
	}
	
    public static String capitalFirst(String name) {
        char[] cs=name.toLowerCase().toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }
    //转换树形数据结构
    public static List<Map<String, Object>> transformToTree(List<Map<String,Object>> list, String id, String pId){
    	List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();
    	Map<String, Object> dataMap = new HashMap<String, Object>();
    	Set<String> pidSet = new HashSet<>();
    	for(int i=0;i<list.size();i++){
    		if(StringUtils.isEmpty(list.get(i).get(pId))){
    			Map<String, Object> treeData = new HashMap<String, Object>();
    			for (Map.Entry<String, Object> entry : list.get(i).entrySet()) {  
    			    treeData.put(entry.getKey(), entry.getValue());
    			}  
    			treeList.add(treeData);
    		} else {
    			pidSet.add(list.get(i).get(pId).toString());
    		}
    	}
    	Iterator<String> it = pidSet.iterator(); 
    	while (it.hasNext()) {
    		String key = it.next();
    		List<Map<String, Object>> childList = new ArrayList<Map<String, Object>>();
    		for(int j=0;j<list.size();j++){
    			if(list.get(j).get(pId)!=null && key.equals(list.get(j).get(pId).toString())){
    				Map<String, Object> treeData = new HashMap<String, Object>();
        			for (Map.Entry<String, Object> entry : list.get(j).entrySet()) {  
        			    treeData.put(entry.getKey(), entry.getValue());
        			}  
    				childList.add(treeData);
    			}
    		}
    		dataMap.put(key, childList);
		}  
    	for(Map<String, Object> t:treeList){
    		getChildren(dataMap, t.get("id").toString(), t);
    	}
    	return treeList;
    }
    // 递归查找
    private static void getChildren(Map<String, Object> dataMap, String pid, Map<String, Object> treeData){
    	treeData.put("children", dataMap.get(pid));
    	if(dataMap.get(pid)!=null){
    		List<Map<String, Object>> childListmap = (List<Map<String, Object>>)dataMap.get(pid);
    		for(Map<String, Object> t:childListmap){
    			if(dataMap.get(t.get("id").toString())!=null){
    				getChildren(dataMap, t.get("id").toString(), t);
    			}
    		}
    	}
    }
    
    /**
     * 对业务异常的处理
     * 统一封装为JSON对象。校验类还需用指定模板解析msg详情
     * @param templateUtil
     * @param ex
     * @return
     */
    public static ModelAndView getExceptionView(TemplateUtil templateUtil,Exception ex){
		if(ex instanceof ServiceException){
			return getServiceExceptionView((ServiceException)ex);
		}
		if(ex instanceof ValidException){
			return getValidExceptionView(templateUtil,(ValidException)ex);
		}
		
		FormLogger.logFlow(String.format("未封装的异常,按未知错误返回 %s : %s ", ex.getClass().getSimpleName(), ex.getMessage()),
				FormLogger.LOG_TYPE_DEBUG);
		ex.printStackTrace();
		//其余代码错误引起的异常简单封装后返给客户端
		ServiceException e = new ServiceException(ErrorCode.UNKNOWN,ex.getMessage());
		return getServiceExceptionView((ServiceException)e);
    }
    
    /**
     * 构造校验异常时的response对象
     * 外层code,msg放标准信息, see@ErrorCode
     * 内层data放详细msg
     * @param templateUtil
     * @param ex
     * @return
     */
    private static ModelAndView getValidExceptionView(TemplateUtil templateUtil,ValidException ex){
		ModelAndView mv = new ModelAndView();
		MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
		mv.setView(jsonView);
		
		FormEngineResponse exResponse = new FormEngineResponse("");
		
		Map<String,Object> env = new HashMap<>();
		env.put("errors", ex.getErrors().getFieldErrors());
		String detailErrMsg = templateUtil.getContent(FormConstants.TEMPLATE_VALIDATE, env);
		exResponse.setCode(ex.getErrorCode().getCode());
		exResponse.setMsg(ex.getErrorCode().getMsg());
		Map<String,Object> rawData = ex.getData();
		if(!rawData.containsKey("msg")){
			rawData.put("msg", detailErrMsg);//放入明细msg作为后台详细提示用，如果异常抛出位置的data中无msg，则用exception明细信息作为msg
		}
		exResponse.setData(ex.getData());
		jsonView.setAttributesMap(FormUtil.getBeanMap(exResponse));
		FormLogger.logFlow(exResponse.getData().toString(), FormLogger.LOG_TYPE_DEBUG);
		return mv;
    }
    
    /**
     * 业务异常
     * @param templateUtil
     * @param ex
     * @return
     */
    public static ModelAndView getServiceExceptionView(ServiceException ex){
    	ErrorCode error = ex.getErrorCode();
		FormLogger.logFlow(ex.toString(),FormLogger.LOG_TYPE_ERROR);
		ModelAndView mv = new ModelAndView();
		MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
		mv.setView(jsonView);
		FormEngineResponse exResponse = new FormEngineResponse(ex);
		jsonView.setAttributesMap(FormUtil.getBeanMap(exResponse));
		return mv;
    }
    
	/**
	 * 获取JS权限控制语句
	 * @param sList
	 * @return
	 */
	public static String getStatements(List<String> sList){
		String statements = String.join(" ;\n ", sList);//所有控制可用性的语句
		statements += ";";
		return statements;
	}
	
	/**
	 * 排除相同值
	 * @param map1
	 * @param map2
	 */
	public static void shrink(Map<String,Object> map1,Map<String,Object> map2){
		if(map1==null || map2==null) return;
		List<String> keys = new ArrayList<>();
		for (Map.Entry<String, Object> entry : map1.entrySet()) {  
			if(ObjectUtils.toString(entry.getValue()).equals(ObjectUtils.toString(map2.get(entry.getKey())))){
				keys.add(entry.getKey());
			}
		}  	
		for(String key:keys){
			map1.remove(key);
			map2.remove(key);
		}
	}
	
	/**
	 * 身份验证成功返回前调用
	 * 解析暂存的分享链接，客户端界面绘制完毕后根据对象redirect描述提示并打开页面
	 * 分享链接为/open/uiName?token=xxxxx，解析token，构造为redirect对象
	 * @param request
	 * @param model
	 */
    public static void checkOutSavedRequest(HttpServletRequest request,Model model) {
    	ShareModel redirectModel = null;
    	if(WebUtils.getSavedRequest(request)!=null){
	    	SavedRequest req = WebUtils.getAndClearSavedRequest(request);
	    	String queryString = req.getQueryString();
	    	if(ContextUtils.isPublicRequest(req,request) && queryString!=null){
	    		redirectModel = ShareModel.fromQueryString(queryString);
	    		if(redirectModel!=null && !SessionUtils.isAccessibleUrl(redirectModel.getTargetUrl())){
	    			WebUtils.getAndClearSavedRequest(request);//remove saved request
	    			redirectModel.setMsg("打开外部链接失败:"+ErrorCode.ILLEGAL_TOKEN.getMsg());
	    			FormLogger.logFlow("外部链接无权访问ui{" + redirectModel.getUi() + "}", FormLogger.LOG_TYPE_INFO);
	    		}
	    		else {
	    			//外部系统通过token访问时
	    			redirectModel = new ShareModel("","frmOrderDetailList","");
	    		}
		    	String strRedirect = redirectModel==null?"{}":FormUtil.toJsonString(redirectModel);
		    	model.addAttribute(KEY_REDIRECT,strRedirect);
		    	FormLogger.logFlow("外部链接即将打开ui:" + strRedirect , FormLogger.LOG_TYPE_INFO);	    		
	    	}else{//其他，跳转到登录前请求地址
	    		String url = req.getRequestUrl();
	    		url = url.substring(request.getContextPath().length());
	    		model.addAttribute(KEY_REDIRECT,"url:" + url);
	    	}
    	}else{
    		model.addAttribute(KEY_REDIRECT,"{}");//客户端页面拿到空对象时不做处理
    	}
    }

    public static void shareLink(){
		ShareModel model;
		model = new ShareModel("工作项管理","frmIssueList",null);

		Map<String,Object> mapParam = new HashMap<>();
		mapParam.put("eq_issueId", 123);
		mapParam.put("lk_title", "测试标题传参");
		model.setTarget("pCondition",mapParam);
//		return  model.toEncryptString();
    }
    
    public static boolean isValidRow(Map<String,Object> rowData){
    	return !rowData.containsKey("delete");
    }
    
    public static String getUIUrl(String uiName){
    	return FormConstants.XML_UI_MAPPING + uiName;
    }
    
    /**
     * 客户端IP
     * @param request
     * @return
     */
    public static String getClientIP(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || FormConstants.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || FormConstants.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || FormConstants.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    } 
}
