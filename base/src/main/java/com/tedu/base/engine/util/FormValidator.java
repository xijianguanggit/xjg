package com.tedu.base.engine.util;

import java.util.ArrayList;
import java.util.List;

import com.tedu.base.common.utils.ContextUtils;
import com.tedu.base.initial.model.xml.ui.Control;
import com.tedu.base.initial.model.xml.ui.Flow;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.initial.model.xml.ui.Panel;
import com.tedu.base.initial.model.xml.ui.Param;
import com.tedu.base.initial.model.xml.ui.Procedure;
import com.tedu.base.initial.model.xml.ui.UI;
import com.tedu.base.initial.model.xml.ui.XML;
/**
 * 校验配置中的属性值正确性，配置有效性
 * 
 * 不容易发现的：
 * 1、find组件的目标属性不应该是主键，否则会造成重复校验问题。
 * @author wangdanfeng
 * 
 * 2、flow中的procedure，若ifyes,ifno指向了当前之前的procedure名称，会造成死循环
 * 3、Popbox中的find逻辑或其他panel.control或panel配置，若引用的panel在ui中不存在，js报错问题。
 * 4、配置List逻辑的组件是非ComboBox类型的
 * 5、
 *
 */
public class FormValidator {
	private FormValidator(){}
	public static void validate(){
		String currentUI = "";
		XML xml = ContextUtils.getXML();
		List<UI> uiList =  xml.getUi_layer().getUiList();
		List<String> msgList =new ArrayList<>();
		try{
			for(UI ui:uiList){
				currentUI = ui.getName();
				msgList.addAll(isValidInOut(ui));
			}
			
			//检测flow中的procedure循环调用。
		
			for(UI ui:uiList){
				currentUI = ui.getName();
				List<Flow> listFlow = ui.getFlowList();
				for(Flow flow:listFlow){
					validateProcedure( ui,flow);
				}
				validEncode(ui);
			}
		}catch(Exception e){
			FormLogger.error("配置校验异常,可能会造成运行时刻功能错误，建议尽快修正","或在此处加断点确认详情",currentUI);
		}
	}
	
	private static void validateProcedure(UI ui,Flow flow){
		List<Procedure> list = flow.getProcedureList();
		int len = list==null?0:list.size();
		for(int i=0;i<len;i++){
			String yes = list.get(i).getIfyes();
			List<Procedure> subList = list.subList(0, i);
			for(Procedure p:subList){
				if(p.getName().equals(yes)){
					FormLogger.error(ui.getName() + "检测到死循环调用", "第"+i+"个", yes);
					break;
				}
			}			
		}
	}
	
	public static List<String> isValidInOut(UI ui){
		List<String> err = new ArrayList<>();
		List<Flow> flowList = ui.getFlowList();
		for(Flow flow:flowList){
			List<Procedure> listProc = flow.getProcedureList();
			if(listProc==null) continue;
			for(Procedure p:listProc){
				if(p.getLogic().equals(FormConstants.LOGIC_BRANCH)) continue;
				Param param = p.getParam(Param.P_InputPanelId);
				if(param!=null ){
					err.addAll(validPanelParam(ui,param));
				}
				
				param = p.getParam(Param.P_OutputPannelId);
				if(param!=null ){
					err.addAll(validPanelParam(ui,param));
				}				
			}
		}
		return err;
	}
	
	private static List<String> validPanelParam(UI ui,Param param){
		List<String> errorList = new ArrayList<>();
		String paramValue = param.getValue();
		if(paramValue.indexOf('.')>=0 ){
			errorList.addAll(validPanelControl(ui,paramValue));
		}else{
			Panel panel = ui.getUiPanel(param.getValue());
			if(panel != null){
				errorList.addAll(validPanelControl(ui,paramValue));
			}
		}
		return errorList;
	}
	/**
	 * 属性值为panel.control或panel时，需确保panel存在，否则易在前端引用panel时引起JS错误
	 * @param ui
	 * @param panelControl
	 * @return
	 */
	private static List<String> validPanelControl(UI ui,String panelControl){
		List<String> errorList = new ArrayList<>();
		String uiName = ui.getName();
		String[] arr = panelControl.split("[.]");
		Panel p = ui.getUiPanel(arr[0]);
		if(p == null || arr.length<2 || arr[1]==null){
			errorList.add(String.format("132.[%s]错误的[%s]:panel不存在",uiName,panelControl));
		}else{
			Control c = p.getControl(arr[1]);
			if(c == null){
				errorList.add(String.format("139.[%s]错误的[%s.%s]:control不存在",uiName,panelControl,panelControl));
			}
		}
		return errorList;
	} 
	
	private static List<String> validEncode(UI ui){
		String uiName = ui.getName();
		List<String> errorList = new ArrayList<String>();
		List<Flow> flowList = ui.getFlowList();
		for(Flow flow:flowList){
			List<Procedure> pList = flow.getProcedureList();
			if(pList==null) continue;
			for(Procedure p:pList){
				if(p.getLogic().equals(FormConstants.LOGIC_ENCODE_ID)){
					Param param = p.getParam(Param.P_InputPanelId);
					if(param == null) continue;
					String value = param.getValue();
//					pTable.id
					if(value.indexOf('.')>=0){
						String[] arr = value.split("[.]");
						Panel gridPanel = ui.getUiPanel(arr[0]);
						if(gridPanel !=null && gridPanel.getType().equals(Panel.TYPE_GRID)){//如果是表格encode，必须后面的值是属性，而不是组件
							String controlName = arr[1];
							List<Control> columnList = gridPanel.getControlList();
							boolean found = false;
							for(Control column:columnList){
								if(column.getProperty().equals(controlName)){
									found = true;
									break;
								}
							}
							if(!found){
								errorList.add(String.format("169.[%s]错误的[%s.%s]:control不存在",uiName,arr[0],arr[1]));
							}
							
						}
					}
				}
			}
		}
		return errorList;
	}

		
	
	
	public static List<String> isValidTrigger(String trigger,UI ui){
		List<String> errorList = new ArrayList<>();
		String uiName = ui.getName();
		if(trigger.indexOf("[.]")>=0 ){
			validPanelControl(ui,trigger);
		}else{
			Panel p = ui.getUiPanel(trigger);
			if(p == null){
				errorList.add(String.format("192.[%s]错误的trigger[%s]:panel[%s]不存在",uiName,trigger,trigger));
			}
		}
		return errorList;
	}
}
