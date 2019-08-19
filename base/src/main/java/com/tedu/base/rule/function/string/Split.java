package com.tedu.base.rule.function.string;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;

import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/5
 */
public class Split extends AbstractFunction {

    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {

        String s = arg1.getValue(env).toString();
        String regex = arg2.getValue(env).toString();
        env.put("s",s);
        env.put("regex",regex);

        if (arg3 != null) {
            //String limit = arg2.getValue(env).toString();
            //env.put("limit",limit);
            return new AviatorRuntimeJavaType(AviatorEvaluator.execute("string.split(s,regex,limit)", env));

        } else {

            return new AviatorRuntimeJavaType(AviatorEvaluator.execute("string.split(s,regex)", env));
        }
    }

    @Override
    public String getName() {
        return "Split";
    }
}
