package com.tedu.base.rule.function.string;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorNumber;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

import static com.googlecode.aviator.AviatorEvaluator.execute;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/5
 */
public class Length extends AbstractFunction {
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        String s = FunctionUtils.getStringValue(arg1, env);
        env.put("s",s);
        return AviatorNumber.valueOf(execute("string.length(s)", env));

    }

    @Override
    public String getName() {
        return "Length";
    }
}
