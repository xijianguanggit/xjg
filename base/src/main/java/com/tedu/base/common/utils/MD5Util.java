package com.tedu.base.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.apache.shiro.codec.CodecException;
import org.apache.shiro.crypto.UnknownAlgorithmException;

import com.tedu.base.engine.util.FormLogger;

/**
 * md5加密
 * @author xijianguang
 */
public class MD5Util {
	private static final Logger log = Logger.getLogger(MD5Util.class);
	/**
     * 输入流加密
     * @param input 输入流
     * @param 返回类型 type=10时输出十进制加密类型 返回类型 type=16时输出十六进制加密类型
     * @return 加密后字符串
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
     */
    public static String inputStreamMD5(InputStream input, String type) throws IOException, NoSuchAlgorithmException{
    	StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
		for (int n; (n = input.read(b)) != -1;) {
		     out.append(new String(b, 0, n));
		}
    	return MD5Encode(out.toString(), type);
    } 


	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };

	/**
     * 转换为十进制或者十六进制
     * @param b char数组（加密后）
     * @param 返回类型 type=10时输出十进制加密类型 返回类型 type=16时输出十六进制加密类型
     * @return 加密后字符串
     */
	public static String byteArrayToString(byte[] b, String type) {
		StringBuffer resultSb = new StringBuffer();
		if ("10".equals(type)) {
			for (int i = 0; i < b.length; i++) {
				resultSb.append(byteToNumString(b[i]));// 使用本函数则返回加密结果的10进制数字字串，即全数字形式
			}
		} else {
			for (int i = 0; i < b.length; i++) {
				resultSb.append(byteToHexString(b[i]));// 若使用本函数转换则可得到加密结果的16进制表示，即数字字母混合的形式
				// resultSb.append(byteToNumString(b[i]));//使用本函数则返回加密结果的10进制数字字串，即全数字形式
			}
		}

		return resultSb.toString();
	}
	/**
     * 转换为十进制
     * @param b char数组（加密后）
     * @return 转为十进制后的字符串
     */
	private static String byteToNumString(byte b) {

		int _b = b;
		if (_b < 0) {
			_b = 256 + _b;
		}

		return String.valueOf(_b);
	}
	/**
     * 转换为十六进制
     * @param b char数组（加密后）
     * @return 转为十六进制后的字符串
     */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}
	/**
     * 加密主方法
     * @param origin 需要加密的字段
     * @param  type 返回类型 type=10时输出十进制加密类型 返回类型 type=16时输出十六进制加密类型
     * @return 加密后字符串
	 * @throws NoSuchAlgorithmException 
     */
	public static String MD5Encode(String origin, String type) throws NoSuchAlgorithmException {
		String resultString = null;
		resultString = new String(origin);
		MessageDigest md = MessageDigest.getInstance("MD5");
		resultString = byteArrayToString(md.digest(resultString.getBytes()), type);
		return resultString;
	}

	/**
	 * 和客户端算法一致
	 * @param origin
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String MD5Encode(String origin) {
		try{
			return MD5Encode(origin,"32");
		}catch(Exception e){
			FormLogger.error("MD5Encode", "加密失败", e.getMessage());
			return "";
		}
	}

	public static String MD5Encode(String salt,String origin,int hashIterations){

		byte []  byteSource = origin.getBytes();
		byte []  saltSource = salt.getBytes();
		try {
			 return byteArrayToString(hash(byteSource,saltSource,hashIterations),"32");
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage());
		}

      return "";
	}

	private static byte [] hash(byte [] source, byte [] salt, int hashIterations) throws CodecException, UnknownAlgorithmException, NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		if (salt != null) {
			md.reset();
			md.update(salt);
		}
		byte[] hashed = md.digest(source);
		int iterations = hashIterations - 1; //already hashed once above
		//iterate remaining number:
		for (int i = 0; i < iterations; i++) {
			md.reset();
			hashed = md.digest(hashed);
		}
		return hashed;
	}
}
