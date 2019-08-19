package com.tedu.base.engine.aspect;

import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;

/**
 * 逻辑插件
 * @author wangdanfeng
 *
 */
public interface ILogicPlugin {
	/**
	 * 在逻辑的action之前执行
	 * @param requestObj
	 * @param formModel
	 */
    public Object doBefore(FormEngineRequest requestObj,FormModel formModel);
    
    /**
     * 在逻辑的后置处理后执行
     * @param requestObj 请求对象 
     * @param formModel prepareModel结果
     * @param beforeResult doBefore方法的执行结果
     * @param responseObj 返回对象
     */
    public void doAfter(FormEngineRequest requestObj,FormModel formModel, Object beforeResult,FormEngineResponse responseObj);
}
