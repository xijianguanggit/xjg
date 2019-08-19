package com.tedu.base.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/5
 */
public class ChineseNumberUtil {
    private static final String[] units = new String[]{
            "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿"
    };

    private static final String[] numbers = new String[]{
            "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"
    };

    private static final String[] littleUnits = new String[]{
            "分", "角",
    };

    public static String convertToCapitalCurrency(Double money) {
        BigDecimal b = new BigDecimal(money);
        Double m = b.setScale(2, RoundingMode.HALF_UP).doubleValue();
        StringBuffer sbf = new StringBuffer();
        StringBuffer sbf1 = new StringBuffer();

        int unit = 0;
        int littleUnit = 0;
        while (money.intValue() != 0) {
            sbf.insert(0, units[unit++]);
            int number = money.intValue() % 10;
            sbf.insert(0, numbers[number]);
            money /= 10;
        }

        String mStr = m.toString();
        int littleMoney = Integer.parseInt(mStr.substring(mStr.indexOf(".") + 1, mStr.length()));
        while (littleMoney != 0) {
            int number = littleMoney % 10;
            sbf1.insert(0, littleUnits[littleUnit++]);

            sbf1.insert(0, numbers[number]);
            littleMoney /= 10;

        }


        return sbf.append(sbf1).toString();
    }


}
