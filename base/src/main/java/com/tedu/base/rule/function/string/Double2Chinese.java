package com.tedu.base.rule.function.string;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import com.tedu.base.common.utils.ChineseNumberUtil;

import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/5
 */
public class Double2Chinese extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        Double number = FunctionUtils.getNumberValue(args[0], env).doubleValue();

        return new AviatorString(ChineseNumberUtil.convertToCapitalCurrency(number));
    }

    @Override
    public String getName() {
        return "Double2Chinese";
    }
}
