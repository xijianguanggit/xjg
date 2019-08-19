package com.tedu.base.engine.util;

import com.tedu.base.common.utils.RegexUtils;
import org.apache.commons.lang.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;


/**
 * 数据验证工具类
 *
 * @author wangdanfeng
 */
public class ValidatorUtil {

    public static boolean checkPasswordStrength(Object val) {

        return Pattern.matches(RegexUtils.REGEX_PASSWORD_STRENGTH, val.toString());

    }

    /**
     * 校验是否是数字和字母
     *
     * @param val
     *
     * @return
     */
    public static boolean isCharAndNum(Object val) {
        return Pattern.matches(RegexUtils.REGEX_CHAR_NUM, val.toString());

    }

    /**
     * 校验是否是数字和字母
     *
     * @param val
     *
     * @return
     */
    public static boolean isNum(Object val) {
        return Pattern.matches(RegexUtils.REGEX_NUM, val.toString());

    }


    /**
     * 校验MAC地址
     *
     * @param val
     *
     * @return
     */
    public static boolean isMacString(Object val) {
        return Pattern.matches(RegexUtils.REGEX_MAC_ADDRESS, val.toString());

    }

    /**
     * 校验IP地址
     *
     * @param val
     *
     * @return
     */
    public static boolean isIpAddress(Object val) {
        return Pattern.matches(RegexUtils.REGEX_IP_ADDRESS, val.toString());

    }

    /**
     * 校验邮编
     *
     * @param val
     *
     * @return
     */
    public static boolean isPostCode(Object val) {
        return Pattern.matches(RegexUtils.REGEX_POST_CODE, val.toString());

    }


    /**
     * 校验固定电话号码
     *
     * @param val
     *
     * @return
     */
    public static boolean isPhone(Object val) {
        return Pattern.matches(RegexUtils.REGEX_PHONE, val.toString());

    }

    /**
     * 校验URL
     *
     * @param val
     *
     * @return
     */
    public static boolean isUrl(Object val) {
        return Pattern.matches(RegexUtils.REGEX_URL, val.toString());
    }


    public static boolean isInt(Object val) {
        try {
            Integer.parseInt(val.toString());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public static boolean isFloat(Object val) {
        try {
            Float.valueOf(val.toString());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDouble(Object val) {
        try {
            Double.parseDouble(val.toString());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isLong(Object val) {
        try {
            Long.parseLong(val.toString());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDatetime(Object val) {
        try {
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(val);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isDate(Object val) {
        try {
            new SimpleDateFormat("yyyy-MM-dd").parse(ObjectUtils.toString(val));
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isTime(Object val) {
        try {
            new SimpleDateFormat("HH:mm:ss").format(val);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 检验是否是email
     *
     * @param email
     *
     * @return
     */
    public static boolean isEmail(String email) {

        //验证Email可以包含数字，字母，- ,有@,有域名
        return Pattern.matches(RegexUtils.REGEX_EMAIL, email);
    }

    public static boolean isUrl(String value) {
        return Pattern.matches(RegexUtils.REGEX_URL, value);
    }

    /**
     * 是否是移动电话
     *
     * @param value
     *
     * @return
     */
    public static boolean isMobile(String value) {
        return Pattern.matches(RegexUtils.REGEX_MOBILE, value);
    }
    /**
     * XSS校验
     *
     * @param value
     *
     * @return
     */
    public static boolean isXSS(String value) {
        return Pattern.matches(RegexUtils.REGEX_XSS, value.replaceAll("\\s*|\t|\r|\n", ""));
    }

    /**
     * 身份证验证
     *
     * @param value
     *
     * @return
     */
    public static boolean isIdCard(String value) {
        return Pattern.matches(RegexUtils.REGEX_ID_CARD, value);
    }

    public static boolean isChinese(String value) {
        return Pattern.matches(RegexUtils.REGEX_CHINESE, value);
    }
}
