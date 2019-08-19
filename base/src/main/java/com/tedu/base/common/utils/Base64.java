/**
 * 
 */
package com.tedu.base.common.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.bouncycastle.util.encoders.UrlBase64;

/**
 * base64编码解码
 * @author xijianguang
 */
public class Base64 {
	public final static String ENCODING = "UTF-8";

	/**
     * 编码url专用
     * @param data 需要编码的url
     *
     * @return 编码后的url
     */
	public static String encodedURL(String data) throws UnsupportedEncodingException {
		byte[] b = UrlBase64.encode(data.getBytes(ENCODING));
		return new String(b, ENCODING);
	}

	/**
     * 解码url专用
     * @param data 需要解码的url
     *
     * @return 解码后的url
     */
	public static String decodeURL(String data) throws UnsupportedEncodingException {
		byte[] b = UrlBase64.decode(data.getBytes(ENCODING));
		return new String(b, ENCODING);
	}

	/**
	 * 普通字段编码
	 * 
	 * @param bstr
	 * @return String
	 */
	public static String encode(String bstr) {
		return new sun.misc.BASE64Encoder().encode(bstr.getBytes());
	}

	/**
	 * 普通字段解码
	 * 
	 * @param str
	 * @return string
	 * @throws IOException 
	 */
	public static String decode(String str) throws IOException {
		byte[] bt = null;
		sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
		bt = decoder.decodeBuffer(str);

		return new String(bt, ENCODING);
	}
}
