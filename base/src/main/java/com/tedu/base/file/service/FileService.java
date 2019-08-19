package com.tedu.base.file.service;

import java.io.InputStream;
import java.util.Map;

import com.tedu.base.file.model.FileModel;
import com.tedu.base.file.util.UploadFile;

/**
 * 文件操作service接口，包括上传下载和查看
 * @author gaolu
 *
 */
public interface FileService {
	/**
	 * 
	 * @Description: 本地上传
	 * @author: gaolu
	 * @date: 2017年8月15日 下午2:32:15  
	 * @param: 文件工具类对象，文件uuid，文件分类类型
	 */
	public Map<String, Object> localUpload(UploadFile upfiles,String fileUUID,String source,String uploadUrl,String module);
	
	/**
	 * 
	 * @Description: 根据文件id查询文件信息
	 * @author: gaolu
	 * @date: 2017年8月17日 上午2:51:55  
	 * @param: @param id
	 * @param: @return      
	 * @return: FileModel
	 */
	public FileModel getFileById(Integer id);
	
	/**
	 * 
	 * @Description: 插入文件表数据
	 * @author: gaolu
	 * @date: 2017年8月28日 上午2:44:18  
	 * @param:      
	 * @return: void
	 */
	public void insertFile(FileModel fileModel);
	
	/**
	 * 
	 * @Description: 更新文件表数据
	 * @author: hejk
	 * @date: 2017年12月1日 上午2:44:18  
	 * @param:      
	 * @return: void
	 */
	public void updateFileById(FileModel fileModel);
	/**
	 * 
	 * @Description: markDown文件上传
	 * @author: hejk
	 * @date: 2017年11月27日 下午2:32:15  
	 * @param: 文件工具类对象，文件uuid，文件分类类型
	 */
	public FileModel localUpload(String fileName,String fileSize,String uuid,String fileType,String mode,FileModel fileModel,String path);
	
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
	 * @date: 2018年5月25日 上午2:07:10  
	 * @param:      
	 * @return: FileModel
	 */
	public FileModel getFileByUUID(String fileUUID);
	/**
	 * Transform 类获取dish文件通过service获取.
	 * @return
	 */
	public String getFileDish();	
	/**
	 * 
	 * @Description: 将封装好的fileModel中的数据插入到数据库中
	 * @author: lvzhenhui
	 * @date: 2018年6月11日 下午5:07:34  
	 * @param:      
	 * @return: FileModel
	 */	
	public FileModel saveFileModel(FileModel fileModel);
}
