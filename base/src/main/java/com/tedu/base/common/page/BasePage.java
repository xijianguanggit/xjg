package com.tedu.base.common.page;

import com.tedu.base.common.utils.SessionUtils;


/**
 * 分页类
 * @author xijianguang
 */

public class BasePage {
    private Integer page = null;//默认不分页
    private int rows=25 ;//每页显示的记录数，默认是10
    private int totalRecord;//总记录数
    private int totalPage;//总页数
    private String sort;
    private String order;
    private Long total;
    private Long sessionEmpId;//权限
    private Long sessionRoleId;//权限
	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getTotalRecord() {
       return totalRecord;
    }

    public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}


	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public void setTotalRecord(int totalRecord) {
       this.totalRecord = totalRecord;
       //在设置总页数的时候计算出对应的总页数，在下面的三目运算中加法拥有更高的优先级，所以最后可以不加括号。
       int totalPage = totalRecord%rows==0 ? totalRecord/rows : totalRecord/rows + 1;
       this.setTotalPage(totalPage);
    }

    public int getTotalPage() {
       return totalPage;
    }

    public void setTotalPage(int totalPage) {
       this.totalPage = totalPage;
    }

	public Long getSessionEmpId() {
		return sessionEmpId;
	}

	public void setSessionEmpId(Long sessionEmpId) {
		this.sessionEmpId = sessionEmpId;
	}

	public Long getSessionRoleId() {
		return sessionRoleId;
	}

	public void setSessionRoleId(Long sessionRoleId) {
		this.sessionRoleId = sessionRoleId;
	}

	/**
	 * TPMS系统仅支持单角色。和角色相关的数据级筛选时使用
	 * @return
	 */
	public Boolean getIsAdmin(){
		Long roleId = SessionUtils.getUserInfo().getRoleId();
		return (roleId!=null && roleId==1);
	}
	
	public Boolean getIsTester(){
		return (sessionRoleId!=null && sessionRoleId==4);
	}
	
	public Boolean getIsTestManager(){
		return (sessionRoleId!=null && sessionRoleId==5);
	}
	
	public Boolean getIsDev(){
		return (sessionRoleId!=null && sessionRoleId==2);
	}
	public Boolean getIsDevManager(){
		return (sessionRoleId!=null && sessionRoleId==3);
	}
	
	
}
