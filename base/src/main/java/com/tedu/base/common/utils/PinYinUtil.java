package com.tedu.base.common.utils;

import org.apache.log4j.Logger;

import com.tedu.base.common.utils.bean.PinYinType;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/4
 */
public class PinYinUtil {
	private static final Logger log = Logger.getLogger(PinYinUtil.class);

    /**
     * 讲汉字转换成拼音
     *
     * @param chinese
     *
     * @return
     */
    public static String toPinyin(String chinese, PinYinType pinYinType) {
        char[] cl_chars = chinese.trim().toCharArray();
        String hanyupinyin = "";
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        Boolean isFirstMode = false;
        switch (pinYinType.getValue()) {
            // 输出拼音全部大写
            case "UPPER_CASE":
                return getAllLetters(chinese, HanyuPinyinCaseType.UPPERCASE);
            // 输出拼音全部小写
            case "LOW_CASE":
                return getAllLetters(chinese, HanyuPinyinCaseType.LOWERCASE);
            case "FIRST_UPPER_CASE":
                return getFirstLettersUp(chinese);
            default:
                return hanyupinyin;
        }

    }

    public static String getAllLetters(String chinese, HanyuPinyinCaseType hanyuPinyinCaseType) {
        char[] cl_chars = chinese.trim().toCharArray();
        String hanyupinyin = "";
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(hanyuPinyinCaseType);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        try {
            for (int i = 0; i < cl_chars.length; i++) {
                if (String.valueOf(cl_chars[i]).matches("[\u4e00-\u9fa5]+")) {// 如果字符是中文,则将中文转为汉语拼音
                    hanyupinyin += PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0];
                } else {// 如果字符不是中文,则不转换
                    hanyupinyin += cl_chars[i];
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
        	log.error(e.getMessage());
        }
        return hanyupinyin;

    }

    public static String getFirstLettersUp(String chinese) {
        return getFirstLetters(chinese, HanyuPinyinCaseType.UPPERCASE);
    }

    public static String getFirstLettersLo(String chinese) {
        return getFirstLetters(chinese, HanyuPinyinCaseType.LOWERCASE);
    }

    public static String getFirstLetters(String chinese, HanyuPinyinCaseType caseType) {
        char[] cl_chars = chinese.trim().toCharArray();
        StringBuffer pinyin = new StringBuffer();
        String hanyupinyin = "";
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        // 输出拼音全部大写
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
        try {
            for (int i = 0; i < cl_chars.length; i++) {
                defaultFormat.setCaseType(caseType);
                String str = String.valueOf(cl_chars[i]);
                if (str.matches("[\u4e00-\u9fa5]+")) {// 如果字符是中文,则将中文转为汉语拼音,并取第一个字母
                    pinyin.append(PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0].substring(0, 1));
                    defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
                    String leftWord = PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0];
                    pinyin.append(leftWord.substring(1, leftWord.length()));


                } else if (str.matches("[0-9]+")) {// 如果字符是数字,取数字
                    pinyin.append(cl_chars[i]);
                } else if (str.matches("[a-zA-Z]+")) {// 如果字符是字母,取字母
                    pinyin.append(cl_chars[i]);
                } else {// 否则不转换
                    pinyin.append(cl_chars[i]);//如果是标点符号的话，带着
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            System.out.println("字符不能转成汉语拼音");
        }
        return pinyin.toString();
    }


    /**
     * 取第一个汉字的第一个字符
     *
     * @return String
     *
     * @throws
     * @Title: getFirstLetter
     * @Description: TODO
     */
    public static String getFirstLetter(String Chinese) {
        char[] cl_chars = Chinese.trim().toCharArray();
        String hanyupinyin = "";
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);// 输出拼音全部大写
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
        try {
            String str = String.valueOf(cl_chars[0]);
            if (str.matches(RegexUtils.REGEX_CHINESE)) {// 如果字符是中文,则将中文转为汉语拼音,并取第一个字母

                //TODO遍历数组拼成小写拼音
                hanyupinyin = PinyinHelper.toHanyuPinyinStringArray(
                        cl_chars[0], defaultFormat)[0].substring(0, 1);
                ;
            } else if (str.matches(RegexUtils.REGEX_NUM)) {// 如果字符是数字,取数字
                hanyupinyin += cl_chars[0];
            } else if (str.matches(RegexUtils.REGEX_LETTER)) {// 如果字符是字母,取字母
                hanyupinyin += cl_chars[0];
            } else {// 否则不转换

            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            System.out.println("字符不能转成汉语拼音");
        }
        return hanyupinyin;
    }


}
