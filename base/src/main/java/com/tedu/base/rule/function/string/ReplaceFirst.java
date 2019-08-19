package com.tedu.base.rule.function.string;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;

import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/5
 */
public class ReplaceFirst extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        String s = args[0].getValue(env).toString();
        String regex = args[1].getValue(env).toString();
        String replacement = args[2].getValue(env).toString();
        env.put("s",s);
        env.put("regex",regex);
        env.put("replacement",replacement);
        return new AviatorString(AviatorEvaluator.execute("string.replace_first(s,regex,replacement)", env).toString());
    }

    @Override
    public String getName() {
        return "ReplaceFirst";
    }
}
