package com.tedu.base.rule.function.string;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;

import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/5
 */
public class Substring extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        String s = FunctionUtils.getStringValue(args[0], env);
        Number begin = FunctionUtils.getNumberValue(args[1], env);
        Number end = FunctionUtils.getNumberValue(args[2], env);
        env.put("s",s);
        env.put("begin",begin);
        env.put("end",end);

        return new AviatorString(AviatorEvaluator.execute("string.substring(s,begin,end)", env).toString());

    }

    @Override
    public String getName() {
        return "Substring";
    }
}
