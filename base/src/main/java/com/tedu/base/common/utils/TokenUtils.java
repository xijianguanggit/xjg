package com.tedu.base.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Random;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.springframework.util.Base64Utils;

import com.tedu.base.engine.util.FormLogger;

public class TokenUtils {
	// 日志记录器
	public final Logger log = Logger.getLogger(this.getClass());
	private static final ThreadLocal<String> token = new ThreadLocal<String>();
	public static void setToken(){
		token.set(generateToken());
	}
	public static String getToken(){
		return token.get();
	}
    public static String generateToken(){  
    	return UUID.randomUUID().toString().toLowerCase();

//        String token = System.currentTimeMillis()+new Random().nextInt()+"";    //随机的值  
//        try {  
//            MessageDigest md = MessageDigest.getInstance("md5");        //注意下面的处理方式  
//            byte[] md5 = md.digest(token.getBytes());
//            
//            return new sun.misc.BASE64Encoder().encode(md5); //base64编码    
//            
//        } catch (NoSuchAlgorithmException e) {  
//            throw new RuntimeException(e.getMessage());  
//        }  
    }
    
    /**
     * url safe
     * @param key
     * @param value
     * @return
     */
    public static String generateToken(String key, String value) {
        String result;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(value.getBytes());
            result = Base64Utils.encodeToUrlSafeString(rawHmac);
        } catch (Exception e) {
            FormLogger.error("Failed to calculate HMAC : " + e.getMessage());
            return genUUID();
        }
        return result;
    }
    
    public static void main(String[] args){
    	System.out.println(""+generateToken());
    	System.out.println(""+generateToken());
//    	System.out.println(""+generateToken("aaa",TokenUtils.genUUID()));
//    	System.out.println(""+generateToken("aaa",TokenUtils.genUUID()));
    }
    
    public static String genUUID(){
    	return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
