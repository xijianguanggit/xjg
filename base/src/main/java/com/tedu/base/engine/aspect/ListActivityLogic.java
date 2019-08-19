package com.tedu.base.engine.aspect;

import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.util.FormConfiguration;
import com.tedu.base.initial.model.xml.ui.Param;
import com.tedu.base.initial.model.xml.ui.Procedure;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/11/7
 */
public class ListActivityLogic extends AbstractLogic{
    @Override
    public FormModel prepareModel(FormEngineRequest requestObj) {
        //构造formModel
        Procedure p = FormConfiguration.getProcedure(serverTokenData);
        Param uiName = p.getParam(Param.P_UI_NAME);
        FormModel formModel = new FormModel();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("uiName",uiName);
        formModel.setData(map);
        return formModel;

    }

    @Override
    public void prepareResponse(FormEngineRequest requestObj, FormEngineResponse res) {

    }
}
