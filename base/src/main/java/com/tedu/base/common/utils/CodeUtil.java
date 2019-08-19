package com.tedu.base.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author yangjixin
 * @Description: Code按时间生成规则当前时间毫秒后+5位随机数
 * @date 2018/1/3
 */
public class CodeUtil {

    public static String GenerateCode(){
        //毫秒后5位随机数
        SimpleDateFormat simpleDateFormat;

        simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        Date date = new Date();

        String str = simpleDateFormat.format(date);

        Random random = new Random();

        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数

        return str+rannum;// 当前时间  }
    }

}
