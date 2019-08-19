package com.tedu.base.rule.function.collection;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/6
 */
public class Distinct extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        Object o = args[0].getValue(env);
        if (o instanceof String[]) {
            String[] distinctSeq = (String[]) o;
            Set<String> set = new HashSet<>();
            for (int i = 0; i < distinctSeq.length; i++) {
                set.add(distinctSeq[i]);
            }
            String[] arrayResult = (String[]) set.toArray(new String[set.size()]);
            return new AviatorRuntimeJavaType(arrayResult);
        }
        if (o instanceof Long[]) {
            Long[] distinctSeq = (Long[]) o;
            Set<Long> set = new HashSet<Long>();
            for (int i = 0; i < distinctSeq.length; i++) {
                set.add(distinctSeq[i]);
            }
            Long[] arrayResult = (Long[]) set.toArray(new Long[set.size()]);
            return new AviatorRuntimeJavaType(arrayResult);
        }

        if (o instanceof Double[]) {
            Double[] distinctSeq = (Double[]) o;
            Set<Double> set = new HashSet<Double>();
            for (int i = 0; i < distinctSeq.length; i++) {
                set.add(distinctSeq[i]);
            }
            Double[] arrayResult = (Double[]) set.toArray(new Double[set.size()]);
            return new AviatorRuntimeJavaType(arrayResult);
        }

        if (o instanceof List) {
            List distinctSeq = (List) o;
            String[] arrayResult = (String[]) distinctSeq.toArray(new String[distinctSeq.size()]);
            Set<String> set = new HashSet<String>();
            for (int i = 0; i < arrayResult.length; i++) {
                set.add(arrayResult[i]);
            }
            List list = (List) set;
            return new AviatorRuntimeJavaType(list);
        }
        return null;
    }

    @Override
    public String getName() {
        return "Distinct";
    }
}
