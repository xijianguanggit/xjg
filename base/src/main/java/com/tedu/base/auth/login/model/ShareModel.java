package com.tedu.base.auth.login.model;


import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.tedu.base.common.utils.DESUtil;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.engine.util.FormUtil;
import com.tedu.base.initial.model.xml.ui.FormConstants;

/**
 * 描述分享链接对象
 * 目前都是按menuitem方式打开。以后需要支持对话框等打开方式时再启用类型参数
 * @author wangdanfeng
 *
 */
public class ShareModel implements Serializable{
	private String url;
	private String ui;
	private String uiType = "ui";
	private String name;
	private String id ;//详情页
	private String msg;
	public static final String KEY_FILTER = "filter";
	String targetName;
	Map<String,Object> param;
	private ShareModel(){}

	
	public ShareModel(String name,String ui,String id){
		this.name = name;
		this.ui = ui;
		this.id = id;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getUi() {
		return ui;
	}

	public void setUi(String ui) {
		this.ui = ui;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTargetName() {
		return targetName;
	}

	public Map<String, Object> getParam() {
		return param;
	}
	
	//resource url
	public String getTargetUrl(){
		return "/"+uiType+"/"+ui;
	}

	/**
	 * 传参描述
	 * @param targetName target panel name
	 * @param param  data
	 */
	public void setTarget(String targetName,Map<String, Object> param) {
		this.targetName = targetName;
		this.param = param;
	}
	
	/**
	 * 生成基于指定网址的对外链接url
	 * @param siteUrl
	 * @return
	 */
	public String toUrl(String siteUrl){
		String token = toEncryptString();
		return String.format("%s%s%s?token=%s"
				,StringUtils.removeEnd(siteUrl, "/")
				, FormConstants.XML_PUBLIC_MAPPING
				,ui
				,token);
	}

	public String toEncryptString(){
		Map<String,Object> map = FormUtil.getBeanMap(this);
		map.remove("targetUrl");
		String jsonString = FormUtil.toJsonString(map);
		try {
			return DESUtil.aesEncrypt(jsonString, FormConstants.SHARE_LINK_KEY);
		} catch (Exception e) {
			FormLogger.error("ShareModel.toEncryptString exception " + e.getMessage(),e);
		}
		return null;
	}
	
	public static ShareModel fromQueryString(String queryString){
		ShareModel model = null;
		String[] arrParam = queryString.split("=");//固定带一个参数
		if(arrParam.length==2) {
    		String strToken = arrParam[1];
    		try {
    			model = fromToken(strToken);
			} catch (Exception e) {
				FormLogger.logFlow("checkOutSavedRequest exception " + e.getMessage(),
						FormLogger.LOG_TYPE_ERROR);
			}
		}
		return model;
	}
	/**
	 * 从token还原sharemodel对象
	 * @param token
	 * @throws Exception 
	 */
	private static ShareModel fromToken(String strToken) throws Exception{
		ShareModel model = null;
		String params = DESUtil.aesDecrypt(strToken, FormConstants.SHARE_LINK_KEY);
		Map<String,Object> data = (Map<String,Object>)FormUtil.toJsonObj(params, Map.class);
		String name = ObjectUtils.toString(data.get("name"));
		String strUi = ObjectUtils.toString(data.get("ui"));
		String id = data.get("id")==null?null:ObjectUtils.toString(data.get("id"));//notrequired
		
		model = new ShareModel(name,strUi,id);
		if(data.containsKey("param")){
			model.setTarget(ObjectUtils.toString(data.get("targetName")), 
					(Map<String,Object>)data.get("param"));
		}
		return model;
	}


	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getUiType() {
		return uiType;
	}

	public void setUiType(String uiType) {
		if(uiType!=null && !uiType.trim().isEmpty()){
			this.uiType = uiType;
		}
	}
}
