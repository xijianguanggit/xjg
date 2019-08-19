package com.tedu.base.engine.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 每次重新生成基于当前UI的所有权限相关数据,包括
 * 1、上下文关系的所有合法请求的token
 * 2、所有flow对应 control的客户端disable,enable语句
 * @author wangdanfeng
 *
 */
public class CSTokenData implements Serializable{
	private List<ServerTokenData> tokenList;
	private Map<String,ServerTokenData> tokenMap;//存放全局token的map,服务端使用
	private Map<String,String> clientTokenMap;//客户端按ui为组存放，用解析模板时的functionId变量获取
	public List<ServerTokenData> getTokenList() {
		return tokenList;
	}
	public void setTokenList(List<ServerTokenData> tokenList) {
		this.tokenList = tokenList;
	}
	
	public Map<String, ServerTokenData> getTokenMap() {
		return tokenMap;
	}
	public void setTokenMap(Map<String, ServerTokenData> tokenMap) {
		this.tokenMap = tokenMap;
	}
	public Map<String, String> getClientTokenMap() {
		return clientTokenMap;
	}
	public void setClientTokenMap(Map<String, String> clientTokenMap) {
		this.clientTokenMap = clientTokenMap;
	}
	
	
}
