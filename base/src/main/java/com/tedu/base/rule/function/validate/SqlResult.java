package com.tedu.base.rule.function.validate;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.task.SpringUtils;

import java.util.HashMap;
import java.util.Map;

public class SqlResult extends AbstractVariadicFunction {

    FormService formService  =  SpringUtils.getBean("formService");

    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {

        String sqlId = args[0].getValue(env).toString();
        Map<String,Object>  data  = (HashMap<String,Object>)env.get(FormConstants.AVIATOR_ENV_PARAM);
        CustomFormModel formModel = new CustomFormModel("","",data);
        formModel.setSqlId(sqlId);
         int i = (int) formService.saveCustom(formModel);

         if(i==-1){
             return AviatorBoolean.FALSE;
         }else{
             return AviatorBoolean.TRUE;
         }
    }

    @Override
    public String getName() {
        return "SqlResult";
    }
}
