package com.tedu.base.rule.function;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import com.tedu.base.common.utils.MD5Util;
import com.tedu.base.common.utils.PasswordUtil;

import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/27
 */
public class Password extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        String salt  = args[0].getValue(env).toString();
        String password= args[1].getValue(env).toString();
        password = MD5Util.MD5Encode(password).toUpperCase();
        String saltPassword  = PasswordUtil.getPassword(PasswordUtil.ALGORITHM_NAME_STR,salt,password);
        return new AviatorString(saltPassword);
    }

    @Override
    public String getName() {
        return "Password";
    }
}
