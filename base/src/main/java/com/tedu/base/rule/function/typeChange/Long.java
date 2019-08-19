package com.tedu.base.rule.function.typeChange;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorNumber;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/5
 */
public class Long extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        env.put("v",args[0].getValue(env));
        return AviatorNumber.valueOf(AviatorEvaluator.execute("long(v)", env));
    }

    @Override
    public String getName() {
        return "Long";
    }
}
