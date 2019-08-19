package com.tedu.base.file.dao;

import com.tedu.base.file.model.DocumentModel;
import com.tedu.base.file.model.MarkDownDocModel;
/**
 * 流程图表映射.
 * @author hejiakuo
 *
 */
public interface MarkDownDocMapper {
	
	/**
	 * 
	 * @Description: 新增流程图表.
	 * @author: hejk
	 * @date: 2017年12月21日 下午13:00:44  
	 * @param:       
	 * @return: void
	 */
	public void insertMDownDoc(MarkDownDocModel fileModel);
	/**
	 * 
	 * @Description: 每次重新加载都要对此表进行清除.
	 * @author: hejk
	 * @date: 2017年12月21日 下午3:00:55  
	 * @param:       
	 * @return: void
	 */
	public void del();

}
