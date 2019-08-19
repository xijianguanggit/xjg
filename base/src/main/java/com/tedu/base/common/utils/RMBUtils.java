package com.tedu.base.common.utils;

import java.math.BigDecimal;

/**
 * @author yangjixin
 * @date 2017/9/7
 */
public class RMBUtils {
    /**
     * 中文中简写的汉字金额 经常使用
     */
    private static final String[] RMB_NUMBERS = new String[]{"一", "二", "三",
            "四", "五", "六", "七", "八", "九", "两", "廿", "卅", "○"};
    /**
     * 中文中繁写的汉字金额 经常使用
     */
    private static final String[] BIG_RMB_NUMBERS = new String[]{"壹", "贰",
            "叁", "肆", "伍", "陆", "柒", "捌", "玖", "俩", "廿", "卅", "零"};// 大写的汉字
    /**
     * 与汉字相应的转化的数字
     */
    private static final Long[] TO_ARABIC_NUMBERS = new Long[]{1L, 2L, 3L, 4L,
            5L, 6L, 7L, 8L, 9L, 2L, 2L, 3L, 0L};// 转化为阿拉伯数字

    /**
     * 人民币单位关键词（即大写数字倍数） 简写 注意：一定要由大到小
     */
    private static final String[] RMB_UNIT = new String[]{"亿", "万", "千", "百",
            "十", "元", "角", "分",};// 中文中间隔的倍数
    /**
     * 人民币单位关键词 繁体写
     */
    private static final String[] BIG_RMB_UNIT = new String[]{"億", "萬", "仟",
            "佰", "拾", "圆", "角", "分"};

    /**
     * 与人民币单位关键词对应的基数
     */
    private static final BigDecimal[] TO_CARDINAL_NUMBERS = new BigDecimal[]{
            new BigDecimal(100000000L), new BigDecimal(10000L),
            new BigDecimal(1000L), new BigDecimal(100L), BigDecimal.TEN,
            BigDecimal.ONE, new BigDecimal("0.1"), new BigDecimal("0.01"),
            new BigDecimal("0.001")};// 转化为阿拉伯的倍数

    /**
     * 大写转化为小写的过程操作
     *
     * @param money
     *         大写金额
     *
     * @return
     */
    public static String covertToDigital(String money) {
        BigDecimal number = getDigitalNum(money);
        return number.toString();
    }

    /**
     * 辅助类，处理中文数字转换成阿拉伯数字，利用递归算法
     *
     * @param money
     *         人民币大写
     *
     * @return
     */
    private static BigDecimal getDigitalNum(String money) {
        BigDecimal result = BigDecimal.ZERO;
        if ((money == null || money.trim().length() <= 0)) {
            return result;
        }
        // 匹配大写金额的单位
        for (int i = 0; i < RMB_UNIT.length; i++) {
            // 查找字符中的简、繁单位
            int index = money.lastIndexOf(RMB_UNIT[i]) == -1 ? money
                    .lastIndexOf(BIG_RMB_UNIT[i]) : money
                    .lastIndexOf(RMB_UNIT[i]);
            if (index >= 0) {
                String pre_money = money.substring(0, index); // 截取当前单位的
                // 前面的中文字符串
                money = money.substring(index + 1); // 截取当前单位后面的字符串 ，进行下一次迭代比较
                if ((pre_money == null || pre_money.length() <= 0)
                        && TO_CARDINAL_NUMBERS[i].intValue() == 10) { // 处理拾开头的特殊字符  例如 拾、 十
                    result = result.add(TO_CARDINAL_NUMBERS[i]);
                } else { // 对当前单位截取的前面的字符 递归处理
                    result = result.add(getDigitalNum(pre_money).multiply(
                            TO_CARDINAL_NUMBERS[i]));
                }
            }
        }
        // 如果不带单位 直接阿拉伯数字匹配替换
        if (money != null && money.length() > 0) {
            result = result.add(getArabicNumByBig(money));
        }
        return result;
    }

    /**
     * 辅助类，中文数字 转化为对应的阿拉伯数字
     *
     * @param big
     *
     * @return
     */
    private static BigDecimal getArabicNumByBig(String big) {
        BigDecimal result = BigDecimal.ZERO;
        for (int j = 0; j < RMB_NUMBERS.length; j++) {
            big = big.replaceAll(RMB_NUMBERS[j],
                    TO_ARABIC_NUMBERS[j].toString()); // 中文小写替换
            big = big.replaceAll(BIG_RMB_NUMBERS[j],
                    TO_ARABIC_NUMBERS[j].toString());// 中文大写替换
        }
        try {
            result = new BigDecimal(big);
        } catch (Exception e) {
            result = BigDecimal.ZERO;
        }
        return result;
    }


}
