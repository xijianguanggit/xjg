package com.tedu.base.engine.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tedu.base.engine.model.ConstraintsModel;
import com.tedu.base.engine.model.FormModel;

/**
 * 检查外键关系,提示
 * Contraints属性值格式：table.field|errMsg;table.field|errMsg
 * 
 * 级联删除关系
 * Cascade属性值格式：table.field;table.field
 * @author wangdanfeng
 *
 */
public class ConstraintsValidator{
	public static final Logger log = Logger.getLogger(ConstraintsValidator.class);
	private ConstraintsValidator(){}
	public static void setDeleteConstraints(FormModel formModel,String constraints,String cascade){
		if(!constraints.isEmpty()){
			formModel.setContraints(ConstraintsValidator.getConstraintsModel(constraints));
		}
		if(!cascade.isEmpty()){
			formModel.setCascade(ConstraintsValidator.getCascadeModel(cascade));
		}
	}
	
	/**
	 * String contraints = "t_role_func.role_id|角色授权表中已有此角色的授权信息;t_user_role.role_id|用户角色表中已有此角色的授权信息"
	 * @param constraints
	 * @return
	 */
	private static List<ConstraintsModel> getConstraintsModel(String constraints) {
		String[] arrGroup = constraints.split(";");
		List<ConstraintsModel> listModel = new ArrayList<>(); 
		for(String group:arrGroup){
			String[] arr = group.split("[|]");
			String strTableField = arr[0];
			String[] tableField = strTableField.split("[.]");
			String table = tableField[0];
			String field = tableField[1];
			String msg = arr[1];
			
			ConstraintsModel cModel = new ConstraintsModel();
			cModel.setTable(table);
			cModel.setField(field);
			cModel.setMsg(msg);
			listModel.add(cModel);
		}

		return listModel;
	}
	
	private static List<ConstraintsModel> getCascadeModel(String cascade) {
//		cascade=t_role_func.role_id;t_user_role.role_id
		String[] arrGroup = cascade.split(";");
		List<ConstraintsModel> listModel = new ArrayList<>(); 
		for(String group:arrGroup){
			String[] tableField = group.split("[.]");
			String table = tableField[0];
			String field = tableField[1];
			
			ConstraintsModel cModel = new ConstraintsModel();
			cModel.setTable(table);
			cModel.setField(field);
			listModel.add(cModel);
		}
		return listModel;
	}
}
