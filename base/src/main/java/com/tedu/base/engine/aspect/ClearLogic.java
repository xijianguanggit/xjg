package com.tedu.base.engine.aspect;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.ObjectUtils;

import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.util.FormUtil;
import com.tedu.base.initial.model.xml.ui.Param;

/**
 * Out
 * @author wangdanfeng
 *
 */
public class ClearLogic extends AbstractLogic{
	@Override
	public FormModel prepareModel(FormEngineRequest requestObj) {
		FormModel formModel = genModelByPanel(requestObj,Param.P_OutputPannelId);
		return formModel;
	}

	@Override
	public void prepareResponse( FormEngineRequest requestObj,
		 FormEngineResponse res) {
	}
	
	/**
	 * clear 
	 * @param out  panel|control1,control2...
	 */
	public static String getEmptyControlsStatemet(String out){
		if(ObjectUtils.isEmpty(out) || out.indexOf('|')<=0) return "";//clear panel
		String[] arrOut = out.split("[|]");
		if(arrOut.length<2 || arrOut[1].isEmpty()) return "";//参数非法，或者没配组件，暂不抛异常
		
		String[] arrControls = arrOut[1].split(",");
		Map<String,Object> mapData = new HashMap<>();
		for(String c:arrControls){
			mapData.put(c, "");
		}
		return String.format("$('#%s').form('load',%s)", arrOut[0],FormUtil.toJsonString(mapData));
	}
}
