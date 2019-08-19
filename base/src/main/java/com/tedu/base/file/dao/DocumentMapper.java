package com.tedu.base.file.dao;

import org.apache.ibatis.annotations.Param;

import com.tedu.base.file.model.DocumentModel;
/**
 * 文档表映射.
 * @author hejiakuo
 *
 */
public interface DocumentMapper {
	
	/**
	 * 
	 * @Description: 新增文档表.
	 * @author: hejk
	 * @date: 2017年11月30日 下午13:00:44  
	 * @param:       
	 * @return: void
	 */
	public void handleDocument(DocumentModel fileModel);
//	/**
//	 * 
//	 * @Description: 删除
//	 * @author: gaolu
//	 * @date: 2017年8月15日 下午3:00:55  
//	 * @param:       
//	 * @return: void
//	 */
//	public void deleteFile();
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
	public DocumentModel getFileByFileId(@Param("cataId")Integer cataId, @Param("fileId")Integer fileId);
	
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
