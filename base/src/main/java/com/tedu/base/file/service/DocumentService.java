package com.tedu.base.file.service;

import com.tedu.base.file.model.DocumentModel;

/**
 * Document文档表 操作service接口
 * @author hejk
 *
 */
public interface DocumentService {
	/**
	 * 
	 * @Description: 新增或者修改文档表数据
	 * @author: hejk
	 * @date: 2017年11月30日 上午2:44:18  
	 * @param:      
	 * @return: void
	 */
	public void handleDocument(String fileId,String cataId,String fileName);
	
	/**
	 * 
	 * @Description: 更改
	 * @author: hejk
	 * @date: 2017年12月1日 下午3:01:02  
	 * @param:       
	 * @return: void
	 */
	public void updateDocById(DocumentModel dm);
	
	/**
	 * 
	 * @Description: 根据doc_id查询
	 * @author: hejk
	 * @date: 2017年8月15日 下午3:01:21  
	 * @param:       
	 * @return: void
	 */
	public DocumentModel getFileById(Integer id);
	
	/**
	 * 
	 * @Description: 根据cata_id,file_id查询 是否有此文档存在(修改)
	 * @author: hejk
	 * @date: 2017年11月30日 下午3:01:21  
	 * @param:       
	 * @return: void
	 */
	public DocumentModel getFileByFileId(Integer cataId,Integer fileId);
	
	/**
	 * 
	 * @Description: 根据,file_id查询 是否有此文档存在(修改)
	 * @author: hejk
	 * @date: 2017年11月30日 下午3:01:21  
	 * @param:       
	 * @return: void
	 */
	public DocumentModel getFileByFileId(Integer fileId);
	
	
	
}
