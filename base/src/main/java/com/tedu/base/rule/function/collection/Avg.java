package com.tedu.base.rule.function.collection;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.List;
import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/6
 */
public class Avg extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        List list = (List )args[0].getValue(env);
        env.put("seq",list);
        Double sum = (Double) AviatorEvaluator.execute("Sum(seq)", env);
        return AviatorDouble.valueOf(sum / list.size());
    }

    @Override
    public String getName() {
        return "Avg";
    }
}
