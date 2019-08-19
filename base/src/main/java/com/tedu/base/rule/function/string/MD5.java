package com.tedu.base.rule.function.string;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import com.tedu.base.common.utils.MD5Util;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/6
 */
public class MD5 extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        String s = FunctionUtils.getStringValue(args[0], env);
        String md5Str = "";
        try {
            md5Str = MD5Util.MD5Encode(s, "32").toUpperCase();
        } catch (NoSuchAlgorithmException e) {

        }
        return new AviatorString(md5Str);
    }

    @Override
    public String getName() {
        return "MD5";
    }
}
