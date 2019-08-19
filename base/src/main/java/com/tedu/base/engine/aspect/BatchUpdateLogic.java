package com.tedu.base.engine.aspect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.utils.CloneUtils;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.util.FormConfiguration;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.initial.model.xml.ui.Panel;
import com.tedu.base.initial.model.xml.ui.Param;
/**
 * save如有指mode，以指定为准，否则从request获取
 * 构造成一个子表、主表为空的一对多保存model.
 * @author tedu
 *
 */
public class BatchUpdateLogic extends AbstractLogic{
	@Override
	public FormModel prepareModel(FormEngineRequest requestObj) {
		Map<String,Object> data = requestObj.getData();
		
		// 根据token获取导入逻辑的配置：主要是In，之后和save相同
		FormModel formModel = genModelByPanel(requestObj,Param.P_InputPanelId);
		formModel.setUpdateMaster(false);
		
		String uiName = serverTokenData.getUiName();
		String panelName = serverTokenData.getProcedure().getParam(Param.P_InputPanelId).getValue();
		FormModel subModel = new FormModel(uiName,panelName);
		List<Map<String,Object>> detailData = new ArrayList<>();
		//一拆多,等同客户端Master2Detail到表格的方式
		try{
			detailData = getRowsByIds(formModel.getPrimaryFieldValue(),data,subModel.getPrimaryControl());
		}catch(Exception e){
			FormLogger.logFlow(String.format("{%s}构造Model时复制数据异常", serverTokenData.getLogic()),
					FormLogger.LOG_TYPE_INFO);
			throw new ServiceException(
					ErrorCode.ILLEGAL_REQUEST_PARAMETER,
					String.format("{%s}构造Model时复制数据异常 {%s}",serverTokenData.getLogic(),e.getMessage()));
		}
		buildDetailData(subModel,detailData);
		subModel.setDetailData(detailData);
		List<FormModel> detailModelList = new ArrayList<>();
		detailModelList.add(subModel);
		formModel.setDetailList(detailModelList);
		FormLogger.logFlow(String.format("校验成功{%s}",uiName), FormLogger.LOG_TYPE_INFO);
		return formModel;
	}

	/**
	 * 根据逗号隔开ids值一行多条数据构造多条
	 * @param ids
	 * @param pkControlName
	 * @throws CloneNotSupportedException 
	 */
	public List<Map<String,Object>> getRowsByIds(String ids,Map<String,Object> row,String pkControlName) throws CloneNotSupportedException{
		List<Map<String,Object>> detailData = new ArrayList<>();
		String[] arrIds = ids.split(",");
		for(String id:arrIds){
			Map<String,Object> currentRow = (Map<String,Object>)CloneUtils.clone(row);	
			currentRow.put(pkControlName, id);
			detailData.add(currentRow);
		}
		return detailData;
	}
	
	private void buildDetailData(FormModel subModel,List<Map<String,Object>> rows){
		 Panel panel = subModel.getPanel();
		 if(panel!=null && panel.getType().equals(Panel.TYPE_GRID) ||
				 serverTokenData.getLogic().equalsIgnoreCase(FormConstants.LOGIC_BATCHUPDATE)){
			 //对panel是Grid的明细数据进行key转换
			 @SuppressWarnings("unchecked")
			 String defaultEditMode = FormModel.Mode.Update.value;//批量更新固定Update模式
			 FormModel tmpDetailModel ;
			 for(Map<String,Object> rowData:rows){
				rowData.put(Param.P_EditMode, defaultEditMode);
				Map<String,Object> env = new HashMap<>();
				env.putAll(rowData);
			    //保存前的表达式预设值
				FormConfiguration.setControlDefault(subModel, env, rowData,FormConstants.METHOD_GETTARGET);	
				//当前明细记录构造成单表校验
				tmpDetailModel = new FormModel(subModel.getUIName(),subModel.getPanelName(),rowData);
				validate(tmpDetailModel);
			 }
		 }
	}
	
	@Override
	public void prepareResponse(FormEngineRequest requestObj, FormEngineResponse res) {
		// do nothing
	}
}
