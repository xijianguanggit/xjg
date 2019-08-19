package com.tedu.base.engine.service;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.stereotype.Service;

import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.dao.FormLogMapper;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.ChangeLog;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.model.ServerTokenData;
import com.tedu.base.engine.model.SimpleFormModel;
import com.tedu.base.engine.model.TableModel;
import com.tedu.base.engine.model.UserLog;
import com.tedu.base.engine.util.FormConfiguration;
import com.tedu.base.initial.model.xml.ui.Control;
import com.tedu.base.initial.model.xml.ui.Panel;
@Service("formLogService")
public class FormLogService {
	@Resource
	private FormLogMapper logMapper;	
	@Resource
	private FormMapper formMapper;	
	
	/**
	 * 常用于记录更新前后的数据改变
	 * @param formModel
	 * @return
	 */
	public Map<String,Object> getDataById(FormModel formModel) {
		String idValue = formModel.getPrimaryFieldValue();
		String tableName = formModel.getTableName();
		String idField = formModel.getPrimaryField();
		
		if(idValue == null || tableName==null || idField==null) return null;

		Map<String,Object> mapData = logMapper.selectById(tableName, idField, idValue);
		//防止用于表达式场景,逐字段设置一遍以属性为key的值
		
		return mapData;
	}
	
	/**
	 * 
	 * @param formModel
	 * @param changeLog
	 */
	public Object logDiffData(ChangeLog changeLog) {
	    changeLog.setCreateTime(new Date());
	    changeLog.setCreateBy(SessionUtils.getUserInfo().getEmpId());
		changeLog.shrink();//剔除相同
		Object pk = save(changeLog);
		return pk;
	}
	
	/**
	 * UserLog 记录到动作
	 * ChangeLog是增删改查类的动作明细。在对应逻辑执行前后记录
	 * 两种log靠ui构造时基于flow给每个serverTokenData生成的flowId关联
	 * @param serverTokenData
	 * crud logic才记录action
	 */
	public void logAction(ServerTokenData serverTokenData){
		if(serverTokenData == null) return;
		
		Date createTime = new Date();
		
		UserLog log = new UserLog();
		log.setUiName(serverTokenData.getUiName());
		log.setUiTitle(FormConfiguration.getUI(serverTokenData.getUiName()).getTitle());
		log.setAction(serverTokenData.getEvent());
		log.setUserId(SessionUtils.getUserInfo()==null?-1:SessionUtils.getUserInfo().getUserId());
		log.setEmpId(SessionUtils.getUserInfo()==null?-1:SessionUtils.getUserInfo().getEmpId());
		Control control = FormConfiguration.getFlowTriggerControl(serverTokenData.getUiName(), serverTokenData.getTrigger());
		if(control != null){
			Panel panel = FormConfiguration.getPanel(serverTokenData.getUiName(), control.getPanelName());		
			log.setPanelName(ObjectUtils.toString(control.getPanelName()));
			log.setPanelTitle(panel.getTitle());
			log.setControlName(serverTokenData.getControlName());
			log.setControlTitle(ObjectUtils.toString(control.getTitle()));
		}else{
			log.setControlName("");
			log.setControlTitle("");
			log.setPanelName("");
			log.setPanelTitle("");
		}
		log.setFlowId(serverTokenData.getFlowId());
		log.setSessionId(SessionUtils.getSession().getId().toString());
		log.setCreateTime(createTime);
		log.setCreateBy(SessionUtils.getUserInfo()==null?-1:SessionUtils.getUserInfo().getEmpId());
		save(log);
	}

	public Object save(UserLog userLog) {
		SimpleFormModel simpleModel = new SimpleFormModel(userLog);
		return formMapper.insert(simpleModel);
	}

	public Object save(TableModel tableModel) {
		SimpleFormModel simpleModel = new SimpleFormModel(tableModel);
		return formMapper.insert(simpleModel);
	}
}
