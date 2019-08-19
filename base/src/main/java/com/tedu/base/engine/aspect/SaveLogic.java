package com.tedu.base.engine.aspect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;

import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.util.FormConfiguration;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.engine.util.FormUtil;
import com.tedu.base.initial.model.xml.ui.Control;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.initial.model.xml.ui.Panel;
import com.tedu.base.initial.model.xml.ui.Param;
import com.tedu.base.initial.model.xml.ui.Procedure;
/**
 * save如有指mode，以指定为准，否则从request获取
 * In:masterTablePanel,detailPanel1,detailPanel2...
 * @author tedu
 *
 */
public class SaveLogic extends AbstractLogic{
	@Override
	public FormModel prepareModel(FormEngineRequest requestObj) {
		Map<String,Object> data = requestObj.getData();
		
		FormModel formModel = genModelByPanel(requestObj,Param.P_InputPanelId);
		translateForm(formModel);
		formModel.setEditMode(getEditMode(requestObj));
	    //保存前的表达式预设值
		FormConfiguration.setControlDefault(formModel, data, data,FormConstants.METHOD_GETTARGET);
		Procedure procInput = FormConfiguration.getProcedure(serverTokenData);//temp
		Param paramVer = procInput.getParam(Param.P_Version);
		if(paramVer != null && !paramVer.getValue().isEmpty()){
			formModel.setOptimisticProperty(paramVer.getValue());//乐观锁对应属性
		}
		validate(formModel);
		FormLogger.logFlow(String.format("校验成功{%s}",serverTokenData.getUiName()), FormLogger.LOG_TYPE_INFO);
		//construct detail model
		String uiName = formModel.getUIName();
		Param paramInput = procInput.getParam(Param.P_InputPanelId);
		
		String panelNames = ObjectUtils.toString(paramInput.getValue());
		String[] arrPanelName = panelNames.split(",");
		int len = arrPanelName.length;//是否有子表
		List<FormModel> detailList = new ArrayList<>();

		for(int i=1;i<len;i++){
			String[] arrConf = arrPanelName[i].split("[.]");
			String currentPanelName = arrConf[0];
			String fkControlName = arrConf.length>1?arrConf[1]:"";//外键组件名,这里预置空值到子表数据中，保存事物中，将主表主键值照此设置到data
			Object detail = data.get(currentPanelName);//spring将Grid数据自动映射为List<LinkedHashMap>
			
			FormModel subModel = new FormModel(uiName,currentPanelName);
			subModel.setForeignControl(fkControlName);
			//目前只在一对多的细表数据处理时使用.如果具有普遍性时,挪到FormModel的setDetailData方法
			//未来统一为根据propertyList获取值校验、构建时，再去掉此处加工逻辑
			buildDetailData(formModel,subModel,detail);
			subModel.setDetailData(detail);//这里增加了对属性名转control名的数据处理，这样可以和普通保存逻辑保持一致。
			detailList.add(subModel);
		}
		formModel.setDetailList(detailList);		
		//end
		return formModel;
	}


	private boolean isGirdPanel(Panel panel){
		return panel!=null && (panel.getType().equals(Panel.TYPE_GRID) ||  panel.getType().equals(Panel.TYPE_HIDDEN_TABLE));
	}
	/**
	 * 多行数据时
	 * 通常来自datagrid。
	 * 目前model的校验、构造SQL都是用controlName。所以这里对panel是Grid的明细数据进行key转换
	 * 每行的编辑模式若已在行中指定,则以指定值为主,否则继承主表编辑模式
	 * @param subModel
	 */
	private void buildDetailData(FormModel masterModel,FormModel subModel,Object detail){
		String masterEditMode = masterModel.getEditMode();
		 Panel panel = subModel.getPanel();
		 if((isGirdPanel(panel))){
			 //对panel是Grid的明细数据进行key转换
			 @SuppressWarnings("unchecked")
			List<Map<String,Object>> list = (List<Map<String,Object>>)detail;
			 String currentRowEditMode;
			 
			 FormModel tmpDetailModel ;
			 for(Map<String,Object> rowData:list){
				if(rowData.containsKey(Param.P_EditMode)){
					currentRowEditMode = ObjectUtils.toString(rowData.get(Param.P_EditMode));
				}else{//否则继承master table的editMode
					currentRowEditMode = masterEditMode;
				}				 
				rowData.put(Param.P_EditMode, currentRowEditMode);//对于明细表,构造一个model对象,所以每行的editMode放入行数据属性中，入库前据此决定insert/update
				for(Control c:subModel.getControlListAll()){
					 if(rowData.get(c.getProperty())!=null){
						 rowData.put(c.getName(), rowData.get(c.getProperty()));
					 }
				}
				Map<String,Object> env = new HashMap<>();
				env.putAll(rowData);
			    //保存前的表达式预设值
				subModel.setEditMode(currentRowEditMode);
				FormConfiguration.setControlDefault(subModel, env, rowData,FormConstants.METHOD_GETTARGET);	
				//校验明细
				if(FormUtil.isValidRow(rowData)){
					tmpDetailModel = new FormModel(subModel.getUIName(),subModel.getPanelName(),rowData);
					validate(tmpDetailModel);
				}
			 }
		 }
	}

	@Override
	public void prepareResponse(FormEngineRequest requestObj, FormEngineResponse res) {
		// do nothing
	}
}
