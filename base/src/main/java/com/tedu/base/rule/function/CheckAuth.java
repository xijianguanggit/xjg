package com.tedu.base.rule.function;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;

import java.util.Arrays;
import java.util.Map;

/**
 * 方便权限测试
 *
 * @author wangdanfeng
 */
public class CheckAuth extends AbstractVariadicFunction {
    private static String[] resource = new String[5];

    public String getName() {
        return "CheckAuth";
    }

    static {
        resource[0] = "/form/frmEmpEdit/pnlGroupPop/querybyid?queryParam=QryEmp";
        resource[1] = "/form/frmEmpEdit/pnlGroupPop/save";
        resource[2] = "/form/frmEmpEdit/pnlGroupPop/querybyid?queryParam=QryEmp";
        resource[3] = "/form/frmEmpEdit/pnlGroupPop/querybyid?queryParam=QryEmp";
        resource[4] = "/form/frmEmpEdit/pnlGroupPop/querybyid?queryParam=QryEmp";
        resource[5] = "/form/frmEmpEdit/pnlGroupPop/querybyid?queryParam=QryEmp";
    }

    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        String value = args[0].getValue(env).toString();
        return new AviatorRuntimeJavaType(Arrays.asList(resource).contains(value));
    }
}
