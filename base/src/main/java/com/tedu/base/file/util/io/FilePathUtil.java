package com.tedu.base.file.util.io;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.file.model.FileModel;
public class FilePathUtil {
	private String SPT = File.separator;
	private final String EXPORT = "export";
	private final String PUBLIC = "public";
	private final String UPLOAD = "upload";
	private final String PRIVATE = "private";
	private final String MARKDOWN = "markdown";
	private final String FORM = "form";
	private final String DOC = "doc";
	private final String PDF = "pdf";
	private final String HTML = "html";
	private String DISH;
	private Date date ;
	private String dateFormat = "";  //日期格式化
	
	public FilePathUtil(Date date,String dish){
		this.date = date;
		this.DISH = dish;
		this.dateFormat =  DateUtils.getDateToStr("yyyyMMdd",date);
	}
	/**
	 * 
	 * @Description: 获取上传文件存储路径
	 * @author: gaolu
	 * @date: 2017年8月27日 下午11:28:07  
	 * @param:      
	 * @return: String
	 */
	public String getUploadPath(String filePostfix,String accessType,String fileUuid,String module){
		String path = "";
		if (StringUtils.isNotEmpty(module)) {
			module = module + SPT;
		} else {
			module = "";
		}
		if (PUBLIC.equalsIgnoreCase(accessType)) {
			path = DISH + PUBLIC + SPT + module + dateFormat + SPT + filePostfix + SPT + fileUuid.substring(0, 2) + SPT;
		} else if (PRIVATE.equalsIgnoreCase(accessType)) {
			path = DISH + PRIVATE + SPT + module + UPLOAD + SPT + dateFormat + SPT + filePostfix + SPT
					+ fileUuid.substring(0, 2) + SPT;
		}
		return path;
	}
	/**
	 * 
	 * @Description: 获取markdown文件存储路径
	 * @author: gaolu
	 * @date: 2017年8月27日 下午11:28:07  
	 * @param:      
	 * @return: String
	 */
	public String getMdPath(String filePostfix,String accessType,String fileUuid,String module){
		String path = "";
		if (StringUtils.isNotEmpty(module)) {
			path = DISH + PRIVATE + SPT + module + SPT + MARKDOWN + SPT + dateFormat.substring(0, 4) + SPT
					+ dateFormat.substring(4, 6) + SPT + dateFormat.substring(6, 8) + SPT;
		} else {
			path = DISH + PRIVATE + SPT + MARKDOWN + SPT + dateFormat.substring(0, 4) + SPT + dateFormat.substring(4, 6)
					+ SPT + dateFormat.substring(6, 8) + SPT;
		}
		if ("md".equals(filePostfix) || "MD".equals(filePostfix)) {
		} else {
			path += filePostfix + SPT;
		}
		return path;
	}
	/**
	 * 
	 * @Description: 获取html文件存储路径
	 * @author: zhangzhiming
	 * @date: 2018年6月27日 下午16:18:45
	 * @param:      
	 * @return: String
	 */
	public String getHtmlPath(){
		String path = DISH+PRIVATE+SPT+MARKDOWN+SPT+dateFormat.substring(0,4)+SPT+dateFormat.substring(4,6)+SPT+dateFormat.substring(6,8)+SPT+HTML+SPT;
		return path;
	}
	/**
	 * 
	 * @Description: 获取pdf文件存储路径
	 * @author: zhangzhiming
	 * @date: 2018年6月27日 下午16:25:03
	 * @param:      
	 * @return: String
	 */
	public String getPdfPath(){
		String path = DISH+PRIVATE+SPT+MARKDOWN+SPT+dateFormat.substring(0,4)+SPT+dateFormat.substring(4,6)+SPT+dateFormat.substring(6,8)+SPT+PDF+SPT;
		return path;
	}
	/**
	 * 
	 * @Description: 表单文件存储路径
	 * @author: gaolu
	 * @date: 2017年8月27日 下午11:28:07  
	 * @param:      
	 * @return: String
	 */
	public String getFormPath(){
		String path = DISH+PRIVATE+SPT+EXPORT+SPT+FORM+SPT;
		return path;
	}	
	
	/**
	 * 
	 * @Description: 获取数据导出存储路径
	 * @author: gaolu
	 * @date: 2017年8月27日 下午11:28:23  
	 * @param:      
	 * @return: String
	 */
	public String getExportPath(){
		String path = DISH+PRIVATE+SPT+EXPORT+SPT+dateFormat+SPT;
		return path;
	}
	
	/**
	 * 
	 * @Description: 获取下载路径
	 * @author: gaolu
	 * @date: 2017年8月27日 下午11:30:54  
	 * @param: 文件对象，生成方式（上传还是导出）
	 * @return: String
	 */
	public String getDownPath(FileModel file,String methodType,String module){
		String path = "";
		if (StringUtils.isNotEmpty(module)) {
			module = module + SPT;
		} else {
			module = "";
		}
		String createdate = file.getCreateTime().substring(0,10).replace("-", "");  //创建日期精确到天
		
		//公有文件，只有上传文件功能使用
		if (PUBLIC.equals(file.getAccessType())) {
			
			path = DISH+PUBLIC+SPT+module+createdate+SPT+file.getFileType()+SPT+file.getFileUUID().substring(0,2)+SPT+file.getFileUUID()+"."+file.getFileType(); //+".dat"
			return path.replace(SPT, SPT+SPT);
		}
		//私有文件
		if (PRIVATE.equals(file.getAccessType())) {
			
			//私有文件，有上传或者数据导出使用
			if (EXPORT.equals(methodType.toLowerCase())) {
				String exportData = file.getCreateTime().replace(" ", "").replace("-","").replace(":", "").replace(".0", "").substring(0, 8);
				//导出的备注都是空，但是转换pdf有备注，因为oppenOffice转换pdf不能将pdf文件后缀带上.dat
				if("打印PDF".equals(file.getRemark())){
					path = DISH+PRIVATE+SPT+module+EXPORT+SPT+exportData+SPT+file.getFileUUID()+"."+file.getFileType();
				}else if("FORM".equals(file.getRemark())){
					path = DISH+PRIVATE+SPT+module+EXPORT+SPT+FORM+SPT+file.getFileUUID()+"."+file.getFileType(); //+".dat"
				}else{
					path = DISH+PRIVATE+SPT+module+EXPORT+SPT+exportData+SPT+file.getFileUUID()+"."+file.getFileType(); //+".dat"
				}
				return path.replace(SPT, SPT+SPT);
			}

			//上传文件
			if (UPLOAD.equals(methodType.toLowerCase())) {
				
				path = DISH+PRIVATE+SPT+module+UPLOAD+SPT+createdate+SPT+file.getFileType()+SPT+file.getFileUUID().substring(0,2)+SPT+file.getFileUUID()+"."+file.getFileType(); //+".dat"
				return path.replace(SPT, SPT+SPT);
			}

			//markdown下载使用
			if (MARKDOWN.equals(methodType.toLowerCase())) {
				
				path = DISH+PRIVATE+SPT+module+MARKDOWN+SPT+createdate.substring(0, 4)+SPT+createdate.substring(4, 6)+SPT+createdate.substring(6, 8)+SPT;
				if ("md".equals(file.getFileType())||"MD".equals(file.getFileType())) {
					path += file.getFileUUID()+".md"; //.dat
				}else{
					path += file.getFileType()+SPT+file.getFileUUID()+"."+file.getFileType(); //+".dat"
				}
				return path.replace(SPT, SPT+SPT);
			}
			
			
			//带版本文件存放目录下载使用
			if (DOC.equals(methodType.toLowerCase())) {
				//代码文件存放路径

			}
		}
		return path;
	}
}
