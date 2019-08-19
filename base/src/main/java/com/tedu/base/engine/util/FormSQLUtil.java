package com.tedu.base.engine.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.ibatis.jdbc.SQL;
import org.apache.log4j.Logger;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.model.TableModel;
import com.tedu.base.initial.model.xml.ui.Control;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.initial.model.xml.ui.ModuleObject;
import com.tedu.base.initial.model.xml.ui.Property;

public class FormSQLUtil {
	public static final Logger log = Logger.getLogger(FormSQLUtil.class);

	private FormSQLUtil(){}
	/**
	 * 获取参与SQL构造的组件列表，每个组件含属性对象描述
	 * move to FormConfiguration
	 * @param formModel
	 * @return
	 */
	public static List<Control> getControlList(FormModel formModel){
		String panelName = ObjectUtils.toString(formModel.getPanelName());//"frmEmpEdit"可能多个
		if(panelName.isEmpty()) {
			//这个表单没法用,未来放在controller validate时完成校验
			throw new ServiceException(ErrorCode.ILLEGAL_REQUEST_PARAMETER,
					String.format("[%s].model的panel不存在,无法获取control", 
							formModel.getUIName()));		
		}
		//panel和model配置对象
		ModuleObject modelObject = formModel.getModel();
		List<Control> cList = formModel.getControlList();//panel组件列表
		List<Property> pList = formModel.getPropertyList();
		//开始构造updateSql
		formModel.setTableName(modelObject.getTable());
		String pk = modelObject.getPrimary();//"emp_id"
		formModel.setPrimaryField(pk);//"emp_id"

		List<Control> targetList = new ArrayList<>();
	    for(Control c:cList){
	    	if(ObjectUtils.toString(c.getProperty()).isEmpty()){
	    		log.debug("未绑定属性的组件" + c.getName());
	    		continue;
	    	}
	    	for(Property p:pList){
	    		if(c.getProperty().equals(p.getName()) && !ObjectUtils.toString(p.getField()).isEmpty()){
	    			//匹配当前组件的属性,且有配置数据库字段
	    			c.setPropertyObj(p);
	    			targetList.add(c);
	    			break;
	    		}
	    	}
	    }
		return targetList;
	}


	/**
	 * 根据指定字段、指定数据构造INSERT PreparedStatement
	 * 数据通常为bean，或者通过FormUtil.getBeanMap(myBean);获得的map
	 * @param fields
	 * @return
	 */
	public static String getInsertSql(TableModel model,Map<String, Object> data){
		//以上信息应从配置对象中获取
		String sql = 
				new SQL() {{ 
					INSERT_INTO(model.getTableName());
						for(String field:model.getFields()){
							if(data.get(camelCaseProperty(field)) != null){
								VALUES(field,String.format("#{data.%s}",camelCaseProperty(field)));
							}
						}
			  }}.toString();
		  return sql;
	}
	
	
	/**
	 * 驼峰转换成model属性名
	 * @param table_field
	 * @return tableField
	 */
	public static String camelCaseProperty(String table_field){
		String[] atoms = table_field.toLowerCase().split("_");
		StringBuilder camel = new StringBuilder();
		for (String atom : atoms) {
			if(camel.length()==0){
				camel.append(atom);
			}else{
			    if (atom.length() > 0) {
			        camel.append(Character.toUpperCase(atom.charAt(0)));
			    }
			    if (atom.length() > 1) {
			        camel.append(atom.substring(1).toLowerCase());
			    }
			}
		}
		return camel.toString();
	}
	
	/**
	 * 获取一个唯一键（单字段或多字段）的检测SQL
	 * @param formModel	
	 * @param fields
	 * @param fieldsHtmlName
	 * @return
	 */
	public static String getUniqueSql(FormModel formModel,String[] fields,String[] fieldsHtmlName){
		int len = fields==null?0:fields.length;
		String sql = new SQL(){{
			SELECT(formModel.getPrimaryField());
			FROM(formModel.getTableName());
			for(int i=0;i<len;i++){
				WHERE(String.format(FormConstants.FIELD_VALUE_TEMPLATE, fields[i],fieldsHtmlName[i]));
			}
			if(formModel.isEdit()){//编辑模式下验证唯一键需要排除记录自身
				WHERE(String.format("%s <> %s ", formModel.getPrimaryField(),
						formModel.getPrimaryFieldValue()));
			}
		}}.toString();
		return sql;
	}
	
	
	/**
	 * 记录更新前后差异
	 * @param formModel
	 * @return
	 */
	public static String getUpdateLogSql(FormModel formModel){
		List<Control> cList = getControlList(formModel);
		//开始构造updateSql
		String pk = formModel.getPrimaryField();//"emp_id"
		//以上信息应从配置对象中获取
		String sql = 
			new SQL() {{ 
				UPDATE(formModel.getTableName());
				Property property ;
				for(Control control:cList){
					property = control.getPropertyObj();//组件绑定的属性对象
					if(formModel.getData().get(control.getHtmlName()) != null){//仅更新传值的字段
						SET(String.format(FormConstants.FIELD_VALUE_TEMPLATE, property.getField(),control.getHtmlName()));
					}
				}
				WHERE(String.format("%s=#{primaryFieldValue}", pk));
		  }}.toString();
		return sql;
	}
	
}
