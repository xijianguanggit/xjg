package com.tedu.base.file.dao;

import com.tedu.base.file.model.FileModel;

public interface FileMapper {
	
	/**
	 * 
	 * @Description: 新增
	 * @author: gaolu
	 * @date: 2017年8月15日 下午3:00:44  
	 * @param:       
	 * @return: void
	 */
	public void insertFile(FileModel fileModel);
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
	 * @author: gaolu
	 * @date: 2017年8月15日 下午3:01:02  
	 * @param:       
	 * @return: void
	 */
	public void updateFileById(FileModel fileModel);
	/**
	 * 
	 * @Description: 查询
	 * @author: gaolu
	 * @date: 2017年8月15日 下午3:01:21  
	 * @param:       
	 * @return: void
	 */
	public FileModel getFileById(Integer id);
	
	/**
	 * 
	 * @Description: 根据文件名称查询文件信息.
	 * @author: hejk
	 * @date: 2017年12月21日  
	 * @param: @param id
	 * @param: @return      
	 * @return: FileModel
	 */
	public FileModel getFileByFileName(String fileName);
	
	/**
	 * 
	 * @Description: 根据fileUUID获取文件信息
	 * @author: gaolu
	 * @date: 2018年5月25日 上午2:08:12  
	 * @param:      
	 * @return: FileModel
	 */
	public FileModel getFileByUUID(String fileUUID);

}
