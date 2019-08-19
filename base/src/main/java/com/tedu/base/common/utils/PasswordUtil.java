package com.tedu.base.common.utils;

import com.googlecode.aviator.AviatorEvaluator;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/27
 */
public class PasswordUtil {

    public static final String ALGORITHM_NAME_STR = "{md5}";

    public static String getPassword(String algorithmName ,String salt,String userInput)  {

        String md5HashStr = MD5Util.MD5Encode(salt,userInput, 6).toString();
        return algorithmName+md5HashStr+salt;

    }

    public static void main(String[] args) {
       PasswordUtil  p = new PasswordUtil();

       String salt = AviatorEvaluator.execute("Guid()").toString();
       String pwd = p.getPassword("{md5}",salt,"202cb962ac59075b964b07152d234b70");

      System.out.println("salt:"+salt);
        System.out.println("pwd:"+pwd);

       String shiroSalt =  new Md5Hash("202cb962ac59075b964b07152d234b70","6499875BAF404E5EB95B020F0A4B5F99",6).toString();

        System.out.println("shiroSalt:"+shiroSalt);

    }
}
