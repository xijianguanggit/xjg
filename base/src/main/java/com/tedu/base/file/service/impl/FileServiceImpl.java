package com.tedu.base.file.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.impl.cmd.GetAttachmentCmd;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.qiniu.storage.model.DefaultPutRet;
import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.file.dao.FileMapper;
import com.tedu.base.file.model.FileModel;
import com.tedu.base.file.service.FileService;
import com.tedu.base.file.util.ImgUtil;
import com.tedu.base.file.util.UploadFile;
import com.tedu.base.file.util.io.FilePathUtil;
import com.tedu.base.file.util.io.file;
import com.tedu.base.file.util.operation.LocalOperation;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 文件操作service接口实现类
 * @author gaolu
 *
 */
@Service("fileService")
public class FileServiceImpl implements FileService {
	@Resource
	public FileMapper fileMapper;
	Date date = new Date();
	@Value("${file.upload.path}")
	private String DISH;
	

	public enum  mediaTypeEnum {
	    local,
	    OSS
	};
	public enum  accessTypeEnum {
	    upload,
	    markdown
	};
	public enum  OSS {
	    qiNiu
	};	
	
	/**
	 * 
	 * @Description: 本地上传
	 * @author: gaolu
	 * @date: 2017年8月15日 下午2:32:15  
	 * @param:文件工具类对象,文件UUID
	 */
	@Override
	public Map<String, Object> localUpload(UploadFile upfiles,String fileUUID,String source,String uploadUrl,String module){
		//返回map集合，包含报错信息和文件信息对象
		Map<String, Object> returnMap = new HashMap<String, Object>(); 
		//上传结果信息
		String hintMessage = "";
		Date date = new Date();
		String path = "";
		String fileNewName = "";
		
		if (StringUtils.isNotEmpty(uploadUrl) && uploadUrl.equalsIgnoreCase(OSS.qiNiu.toString())) {
			Map<String, String> result = qiNiuOSS(upfiles.getFileInputStream()[0], uploadUrl);
			//其他文件服务器接口
			path = result.get("path");
			fileNewName = result.get("name");
		}else{
			//本服务器上传
			path = upfiles.getParameter("path");
			if (StringUtils.isEmpty(path)) {
				FilePathUtil pathUtil = new FilePathUtil(date,DISH);
				if (accessTypeEnum.markdown.toString().equals(source)) {
					path = pathUtil.getMdPath(upfiles.filepostfix[0], "private", fileUUID,module);
				}else{
					if (StringUtils.isEmpty(source) ) {
						source = "public";
					}
					path = pathUtil.getUploadPath(upfiles.filepostfix[0], source, fileUUID,module);
				}
			}
			//本地上传
			LocalOperation localOperation = new LocalOperation();
			hintMessage = localOperation.fileUpload(upfiles,path);
			fileNewName = upfiles.getUploadedFileNameList().get(0);
		}
		
		FileModel fileModel = new FileModel();
		
		//表单由文件上传成功则插入数据
		if (StringUtils.isEmpty(hintMessage) && upfiles.getFileInputStream().length > 0) {
			//如果oss服务器上的文件执行查询是否重复，若重复则直接用之前的数据，否则插入新数据
			if (StringUtils.isNotEmpty(uploadUrl)) {
				FileModel fModel = fileMapper.getFileByUUID(fileNewName);
				if (fModel != null) {
					fileModel = fModel;
				}else{
					fileModel = getFileModel(fileNewName, uploadUrl, date, upfiles, source, path);
				}
			}else{
				fileModel = getFileModel(fileNewName, uploadUrl, date, upfiles, source, path);
			}
		}else {
			hintMessage = "请上传文件";
		}
		returnMap.put("hintMessage", hintMessage);
		returnMap.put("fileModel", fileModel);
		return returnMap;
	}
	
	/**
	 * 
	 * @Description: 封装fileModel数据并插入到数据库中
	 * @author: gaolu
	 * @date: 2018年5月25日 上午1:53:19  
	 * @param:      
	 * @return: FileModel
	 */
	public FileModel getFileModel(String fileNewName,String uploadUrl,Date date,UploadFile upfiles,String source,String path){
		FileModel fileModel = new FileModel();
		
		//文件对象fileModel赋值
		int i = fileNewName.indexOf(".");
		if (i<0) {
			fileModel.setFileUUID(fileNewName);	//上传后文件名称
		}else{
			fileModel.setFileUUID(fileNewName.substring(0,i));	//上传后文件名称
		}
		if ("public".equalsIgnoreCase(source)) {
			fileModel.setAccessType("public");     //暂且设置为公有
		}else{
			fileModel.setAccessType("private");
		}
		fileModel.setLength(String.valueOf(upfiles.allfilesize));	//文件大小
		fileModel.setTitle(upfiles.filename[0]);	//上传前文件名称
		fileModel.setCreateTime(DateUtils.getDateToStr("yyyy-MM-dd HH:mm:ss",date));	//上传日期
		fileModel.setSource(source);
		fileModel.setFileType(upfiles.filepostfix[0]);
		if(SessionUtils.getUserInfo()!=null){
			fileModel.setCreateBy(Integer.parseInt(SessionUtils.getUserInfo().getEmpId().toString()));	//上传人
		} else {
			fileModel.setCreateBy(1);
		}
		fileModel.setPath(path);
		if ("public".equals(fileModel.getAccessType())) {
			String url = "";
			if (StringUtils.isNotEmpty(uploadUrl)) {
				url = path;
			}else{
				int a = fileModel.getPath().indexOf("public");
				url = fileModel.getPath().substring(a+6, fileModel.getPath().length()).replace("\\", "/")
						+fileModel.getFileUUID()+"."+fileModel.getFileType();
			}
			fileModel.setUrl(url);
		}
		
		if (StringUtils.isNotEmpty(uploadUrl)) {
			fileModel.setMediaType(mediaTypeEnum.OSS.toString());		//上传方式
		}else{
			fileModel.setMediaType(mediaTypeEnum.local.toString());		//上传方式
		}
		fileMapper.insertFile(fileModel);
		
		return fileModel;
	}
	
	/**
	 * 
	 * @Description: 将封装好的fileModel中的数据插入到数据库中
	 * @author: lvzhenhui
	 * @date: 2018年6月11日 下午5:07:34  
	 * @param:      
	 * @return: FileModel
	 */
	@Override
	public FileModel saveFileModel(FileModel fileModel){
		

		if(SessionUtils.getUserInfo()!=null){
			fileModel.setCreateBy(Integer.parseInt(SessionUtils.getUserInfo().getEmpId().toString()));	//上传人
		} else {
			fileModel.setCreateBy(1);
		}
		fileModel.setCreateTime(DateUtils.getDateToStr("yyyy-MM-dd HH:mm:ss",date));
		

		if (StringUtils.isNotEmpty(fileModel.getUrl())) {
			fileModel.setMediaType(mediaTypeEnum.OSS.toString());		//上传方式
		}else{
			fileModel.setMediaType(mediaTypeEnum.local.toString());		//上传方式
		}
		
		
		try{
			fileMapper.insertFile(fileModel);
		}catch(Exception e){
			e.printStackTrace();
		}


		
		

		return fileModel;
	}
	
	
	/**
	 * 
	 * @Description: 七牛服务器上传
	 * @author: gaolu
	 * @date: 2018年5月3日 下午9:03:29  
	 * @param:      
	 * @return: Map<String,String>
	 */
	public Map<String, String> qiNiuOSS(InputStream inputStream,String uploadUrl){
        Map<String, String> result = new HashMap<>();
        DefaultPutRet putRet=ImgUtil.LoadImg(inputStream);
        if (putRet != null) {
			result.put("path",putRet.key);
			result.put("name", putRet.hash);
		}
        return result;
    }

	
	/**
	 * 
	 * @Description: 根据文件id查询文件信息
	 * @author: gaolu
	 * @date: 2017年8月17日 上午2:53:45  
	 * @param: @return      
	 * @return: FileModel
	 */
	@Override
	public FileModel getFileById(Integer id){
		return fileMapper.getFileById(id);
	}
	
	/**
	 * 
	 * @Description: 根据文件uuid查询文件信息
	 * @author: gaolu
	 * @date: 2018年5月25日 上午2:06:04  
	 * @param:      
	 * @return: FileModel
	 */
	@Override
	public FileModel getFileByUUID(String fileUUID){
		return fileMapper.getFileByUUID(fileUUID);
	}
	
	@Override
	public void insertFile(FileModel fileModel){
		fileMapper.insertFile(fileModel);
	}
	
	/**
	 * 
	 * @author hejiakuo
	 * @date 2017-11-27
	 * @param upfiles
	 * @param fileUUID
	 * @param source
	 * @param fileType
	 */
	@Override
	public FileModel localUpload(String fileName,String fileSize,String uuid,String fileType,String mode,FileModel fileModel,String path){
		try {
			Date date = new Date();
			
			if(fileModel == null){
				fileModel = new FileModel();
				fileModel.setMediaType(mediaTypeEnum.local.toString());
				if ("upload".equals(fileType)) {
					fileModel.setAccessType("public");
					fileModel.setFileType("md");
				}else if("markdownFlow".equals(fileType)){
					fileModel.setAccessType("private");
					fileModel.setRemark("FORM");
					fileModel.setSource("export");
					fileModel.setFileType("md");
				}else if("pdf".equals(fileType)){
					fileModel.setAccessType("private");
					fileModel.setSource("export");
					fileModel.setFileType("pdf");
				} else {
					fileModel.setAccessType("private");
					fileModel.setSource("markdown");
					fileModel.setFileType("md");
				}
				
				fileModel.setFileUUID(uuid); // uuid
				fileModel.setCreateTime(DateUtils.getDateToStr("yyyy-MM-dd HH:mm:ss", date));
				fileModel.setCreateBy(Integer.parseInt(SessionUtils.getUserInfo().getEmpId()+"")); 
			}else{
				if ("upload".equals(fileType)) {
					fileModel.setAccessType("public");
				}else if("markdownFlow".equals(fileType)){
					fileModel.setAccessType("private");
					fileModel.setRemark("FORM");
					fileModel.setSource("export");
				} else {
					fileModel.setAccessType("private");
				}
			}
			fileModel.setPath(path);
			fileModel.setLength(String.valueOf(fileSize));
			fileModel.setTitle(fileName); 
			
			
			if(mode.equals("edit")){
				fileMapper.updateFileById(fileModel);
			}else{
				fileMapper.insertFile(fileModel);
			}
			
		} catch (NumberFormatException e) {
			return null;
		}
		return fileModel; 
	}

	@Override
	public void updateFileById(FileModel fileModel) {
		 fileMapper.updateFileById(fileModel);
	}

	@Override
	public FileModel getFileByFileName(String fileName) {
		return fileMapper.getFileByFileName(fileName);
	}

	/**
	 * Transform 类获取dish文件通过service获取.
	 * @return
	 */
	@Override
	public String getFileDish() {
		return DISH;
	}
}
