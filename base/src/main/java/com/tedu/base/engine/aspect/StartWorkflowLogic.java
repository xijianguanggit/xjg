package com.tedu.base.engine.aspect;

import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.base.task.SpringUtils;

import java.util.HashMap;

/**
 * @author yangjixin
 * @Description: 启动工作流逻辑
 * @date 2017/11/7
 */

public class StartWorkflowLogic extends AbstractLogic {

    FormLogService formLogService = SpringUtils.getBean("formLogService");

    @Override
    public CustomFormModel prepareModel(FormEngineRequest requestObj) {
        //自定义formModel
        String panelAndControlName=requestObj.getData().get("panelName").toString();
        String panelName = panelAndControlName.substring(0,panelAndControlName.indexOf("."));
        FormModel formModel = new FormModel(serverTokenData.getUiName(),panelName,requestObj.getData());

        String primaryFieldValue = formModel.getData().get("modelId").toString();

        formModel.setPrimaryFieldValue(primaryFieldValue);

        HashMap<String, Object> newData = (HashMap<String, Object>) formLogService.getDataById(formModel);

        CustomFormModel customFormModel = new CustomFormModel(formModel.getUIName(), formModel.getPanelName(), newData);
        customFormModel.setPrimaryFieldValue(primaryFieldValue);
        return customFormModel;
    }

    @Override
    public void prepareResponse(FormEngineRequest requestObj, FormEngineResponse res) {

    }
}
