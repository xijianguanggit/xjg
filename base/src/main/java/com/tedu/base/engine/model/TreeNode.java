package com.tedu.base.engine.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 * ui节点描述及相关缓存
 * @author wangdanfeng
 *
 */
public class TreeNode implements Serializable{
	private String pid;//父UI
	private String id;//uiid
	private String name;//ui name
	/**
	 * @see FormConstants.UI_TYPE
	 */
	private String uiType;//类型
	/**
	 * 用于解释filter。ui相关的 in out 都按panelName前缀记录在ui对应的model里方便解析表达式
	 */
	private Map<String, Object> model = new HashMap<String, Object>();//数据
	private Map<String,Object> logic = new HashMap<String,Object>();//数据
	
	private Map<String, ServerTokenData> tokens;//tokens
	public TreeNode(){}
	public TreeNode(String id,String name,String uiType,String pid){
		this.pid = pid;
		this.id = id;
		this.name = name;
		this.uiType = uiType;
	}
	
	public boolean hasParent(){
		return pid!=null && !pid.isEmpty();
	}
	
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, ServerTokenData> getTokens() {
		return tokens;
	}
	
	public String getUiType() {
		return uiType==null?"":uiType;
	}
	
	public void setUiType(String uiType) {
		this.uiType = uiType;
	}
	
	
	public Map<String, Object> getModel() {
		return model;
	}
	public void setModel(Map<String, Object> model) {
		this.model = model;
	}
	/**
	 * 记录当前UI的token，用于未来清理。
	 * 记录时，将此UI下历史token保留查询等不需防重复的逻辑的token,其余移除
	 * @param tokens
	 */
	public void setTokens(Map<String, ServerTokenData> tokens,Map<String, 
			ServerTokenData> totalTokens) {
		if(this.tokens!=null && this.tokens.size()>0){
			this.tokens.putAll(tokens);
		}else{
			this.tokens = tokens;
		}
	}
	public Map<String, Object> getLogic() {
		return logic;
	}
	public void setLogic(Map<String, Object> logic) {
		this.logic = logic;
	}
	
}
