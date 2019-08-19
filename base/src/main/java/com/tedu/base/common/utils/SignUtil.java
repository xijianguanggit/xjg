package com.tedu.base.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/**
*
* 获取签名工具
* @author UC
*
*/
public class SignUtil {
	public static final Logger log = Logger.getLogger(SignUtil.class);
	public static String getSHA256(String str) {
		MessageDigest messageDigest;
		String encodeStr = "";
	try {
		messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(str.getBytes("UTF-8"));
		encodeStr = byte2Hex(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(),e);
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(),e);
		}
		return encodeStr;
	}

	private static String byte2Hex(byte[] bytes) {
		StringBuffer stringBuffer = new StringBuffer();
		String temp = null;
		for (int i = 0; i < bytes.length; i++) {
			temp = Integer.toHexString(bytes[i] & 0xFF);
			if (temp.length() == 1) {
				// 1得到一位的进行补0操作
				stringBuffer.append("0");
			}
			stringBuffer.append(temp);
		}
		return stringBuffer.toString();
	}
	
	public static String getSign(UCRequest request) {
		if (request.getRequestid() == null || request.getClientid() == null 
				|| request.getClientver() == null|| request.getVersion() == null 					
				|| request.getTerminal() == null || request.getTime() ==  null
				|| request.getAppcode() == null) {
		return null;
		}	
		if (getKey() == null) {
			return null;
		}
	
		String sign = "requestid=" + request.getRequestid() + "&clientid=" + request.getClientid() 	+ "&clientver="+ request.getClientver() + "&version=" + request.getVersion() + 	"&terminal=" + 	request.getTerminal()+ "&time=" + request.getTime() + "&appcode=" + 	request.getAppcode() + "&key=" + getKey();
		return getSHA256(sign);
	}
		
	private static String getKey() {
		Map<String, String> map = new HashMap<>();//PropertiesReader.cacheProperties("uc-api");
		String key = "";//map.get("uc.appKey");
		return key;
	}
	/**
	 *  传入数据，构造好签名后的请求
	 * @param requestObj
	 */
	public static UCRequest getRequestObject(Map<String,Object> data) {
		UCRequest req = new UCRequest();
		req.setBody(data);
		String sign = SignUtil.getSign(req);
		req.setSign(sign);
		return req;
	}
	
	private static void genLogin(){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Map<String,Object> data = new HashMap<>();
		data.put("loginName", "test");
		data.put("password", "4C0E776FB7D4B44223DA1542953CE6B3");
		data.put("validateCode", "");
		System.out.println(SignUtil.getRequestObject(data));
		System.out.println(gson.toJson(SignUtil.getRequestObject(data)));
	}
	
	public static void genGetToken(){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Map<String,Object> data = new HashMap<>();
		data.put("userCode", "L5Ncej_xWjINsrwrLryZx4zixmM=");//redis里的userName对应的token
		
		System.out.println(gson.toJson(SignUtil.getRequestObject(data)));
	}
	
	public static void main(String[] args){
		//请求/open/login，获取userCode
//		genLogin();
		genGetToken();//用redis中userName对应的值当做userCode，生成gettoken的请求串用于测试
		
	}
}

