package com.tedu.base.file.service;

import com.tedu.base.file.model.MarkDownDocModel;

/**
 * markdown流程图 操作service接口
 * @author hejk
 *
 */
public interface MarkDownDocService {
	/**
	 * 
	 * @Description: 新增或者修改文档表数据
	 * @author: hejk
	 * @date: 2017年11月30日 上午2:44:18  
	 * @param:      
	 * @return: void
	 */
	public void insertMDownDoc(MarkDownDocModel markdown);
	
	/**
	 * 
	 * @Description: 更改
	 * @author: hejk
	 * @date: 2017年12月1日 下午3:01:02  
	 * @param:       
	 * @return: void
	 */
	public void del();
	
	
}
