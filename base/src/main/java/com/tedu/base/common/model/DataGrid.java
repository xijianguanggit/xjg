package com.tedu.base.common.model;

import java.util.List;

/**
 * 后台向前台返回JSON，用于easyui的datagrid
 * 
 * @author 孙宇
 * 
 */
public class DataGrid implements java.io.Serializable {
	public static final String PROPERTY_PAGE = "page";
	public static final String PROPERTY_ROWS = "rows";
	public static final String PROPERTY_SORT = "sort";
	public static final String PROPERTY_ORDER = "order";
	
	
	public DataGrid(List<?> list){
		this.rows = list;
	}
	private int total;// 总记录数
	private List<?> rows;// 每行记录
	private List<?> footer;// 每行记录
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<?> getFooter() {
		return footer;
	}

	public void setFooter(List<?> footer) {
		this.footer = footer;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	
}
