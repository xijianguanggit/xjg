package com.tedu.base.rule.function.string;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/5
 */
public class Contain extends AbstractVariadicFunction {


    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        String s1 = args[0].getValue(env).toString();
        String s2 = args[1].getValue(env).toString();
        env.put("s1",s1);
        env.put("s2",s2);

        Boolean result = (Boolean) AviatorEvaluator.execute("string.contains(s1,s2)", env);
        if (result) {
            return AviatorBoolean.TRUE;
        } else {
            return AviatorBoolean.FALSE;
        }
    }

    @Override
    public String getName() {
        return "Contain";
    }
}
