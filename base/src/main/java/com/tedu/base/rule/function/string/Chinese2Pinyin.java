package com.tedu.base.rule.function.string;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import com.tedu.base.common.utils.PinYinUtil;
import com.tedu.base.common.utils.bean.PinYinType;

import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/5
 */
public class Chinese2Pinyin extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        String s = FunctionUtils.getStringValue(args[0], env);
        String type = FunctionUtils.getNumberValue(args[1], env).toString();
        if (type.equals("1")) {
            return new AviatorString(PinYinUtil.toPinyin(s, PinYinType.UPPER_CASE));
        } else if (type.equals("2")) {
            return new AviatorString(PinYinUtil.toPinyin(s, PinYinType.LOW_CASE));
        } else if (type.equals("3")) {
            return new AviatorString(PinYinUtil.toPinyin(s, PinYinType.FIRST_UPPER_CASE));

        }

        return null;
    }

    @Override
    public String getName() {
        return "Chinese2Pinyin";
    }
}
