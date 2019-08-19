package com.tedu.base.initial.model.xml.ui;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

@XStreamAlias("param") 
public class Param implements Serializable{
	@XStreamAsAttribute
	private String name;
	@XStreamAsAttribute
	private String value;
	//规范中的常用参数名
	public static final String P_Dict = "Dict";
	public static final String P_Foreign = "Foreign";
	public static final String P_TO = "To";
	public static final String P_InputPanelId = "In";
	public static final String P_OutputPannelId = "Out";
	public static final String P_Format = "Format";
	public static final String P_Plugin = "Plugin";//插件处理类
	public static final String P_DAO_Plugin = "DAOPlugin";//事物插件处理类
	public static final String P_SQLId = "Sql";
	public static final String P_EditMode = "Mode";
	public static final String P_EditMode_ADD = "Add";
	public static final String P_EditMode_EDIT = "Edit";
	public static final String P_Msg = "Msg";
	public static final String P_Columns = "Columns";
	public static final String P_Title = "Title";
	public static final String P_Multi = "Multi";//find多选
	public static final String P_Options = "Options";
	public static final String P_Version = "Ver";//乐观锁控制。property
	public static final String P_Alias = "Alias";//controlName,alias|controlName,alias|...
	public static final String P_Templet = "Templet";
	
	public static final String P_Constraints = "Constraints";
	public static final String P_Cascade = "Cascade";
	public static final String VALUE_YES = "Y";
	public static final String VALUE_NO = "N";



	//流程相关参数
	//流程图ID
	public static final String P_WORK_FLOW = "WorkFlow";
	//审核说明
	public static final String P_DESC = "Desc";

	public static final String P_UI_NAME="UiName";

	public static final String VIEW_URL = "ViewUrl";
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString(){
		return name==null?"":name + ":" + value==null?"":value;
	}
	
	/**
	 * 支持扩展为客户端表达式
	 * @param s
	 * @return
	 */
	public boolean isJSExpression(){
		return value!=null && value.trim().endsWith(";");
	}
}
