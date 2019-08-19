package com.tedu.base.common.utils;

/**
 * 常用输入正则判断
 * @author xijianguang
 * @modify yangjixin
 * @Date
 */
public class RegexUtils {

    public static final String REGEX_NUM = "^[0-9]*$";
    public static final String REGEX_LETTER = "[a-z0-9A-Z]";
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    public static final String REGEX_CHINESE = "[(\\u4e00-\\u9fa5)]{0,}$";
    public static final String REGEX_ID_CARD = "(^\\d{17}([0-9]|x|X){1}$)|(^\\d{15}$)";
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
    public static final String REGEX_IP_ADDRESS = "(^(?:(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))$)|(^([0-9A-Fa-f]{0,4}:){2,7}([0-9A-Fa-f]{1,4}$|((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4})$)";
    public static final String REGEX_MOBILE = "1[0-9]{10}";
    public static final String REGEX_XSS = "^((?!<|>|=|%).)*$";
    public static final String REGEX_PHONE = "^(0\\d{2,3}-?)?\\d{7,8}$";
    public static final String REGEX_POST_CODE = "[1-9]\\d{5}(?!\\d)";
    public static final String REGEX_MAC_ADDRESS = "([A-Fa-f0-9]{2}-){5}[A-Fa-f0-9]{2}";
    public static final String REGEX_CHAR_NUM = "^[A-Za-z0-9]+$";
    public static final String REGEX_PASSWORD_STRENGTH = "^[A-Za-z0-9]+$";

}
