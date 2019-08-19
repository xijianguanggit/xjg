package com.tedu.base.file.util.operation;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.file.model.FileModel;
import com.tedu.base.file.service.impl.FileServiceImpl.mediaTypeEnum;
import com.tedu.base.file.util.CsvUtil;
import com.tedu.base.file.util.ExportExcelUtil;
import com.tedu.base.file.util.HtmlUtil;
import com.tedu.base.file.util.PDFUtil;
import com.tedu.base.file.util.io.FilePathUtil;
import com.tedu.base.file.util.io.file;

/**
 * 导出数据操作，功能包含生成导出文件，并生成相应文件信息返回
 * @author tedu
 *
 */
public class ExportOperation {
	private String SPT = File.separator; 
	//导出类型
	private enum  exportTypeEnum {
	    Excel,
	    Csv,
	    Html,
	    Pdf
	};	
	
	//文件后缀
	private enum  fileTypeEnum {
	    xls,
	    csv,
	    html,
	    pdf
	};	
	
	/**
	 * 
	 * @Description: 创建导出文件
	 * @author: gaolu
	 * @date: 2017年9月12日 下午11:23:45  
	 * @param:  导出文件头、导出文件头编号、导出文件类型、导出文件数据、expFileName 导出文件的文件名
	 * @return: FileModel
	 */
	public FileModel createFile(String[] dataTitle, String[] dataValue, String exportType, List<Map<String,Object>> list,String DISH,String expFileName){
		FileModel fileModel = null;
		
		
		//生成文件路径
		Date date = new Date();
		//创建uuid
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		//创建文件路径对象
	    FilePathUtil filePath = new FilePathUtil(date,DISH);
	    String exPath = filePath.getExportPath();
		file f = new file(exPath);
		f.createlist();
	    //文件路径
	    String path = "";
	    //文件名字中日期截取
	    String dateName = DateUtils.getDateToStr("yyyy-MM-dd hh:mm:ss",date).replace(" ", "").replace("-","").replace(":", "");
	    //文件类型名字
	    String typeName = "";
	    
	    if (StringUtils.isNotEmpty(exportType)) {
	    	try {
	    		if (exportTypeEnum.Excel.toString().equals(exportType)) {
	    			//创建生成文件路径，并生成excel
	    			typeName = fileTypeEnum.xls.toString();
	    			/*if(!"".equals(expFileName)){
	    				path = exPath + expFileName+"."+fileTypeEnum.xls.toString();  //+".dat"
	    			}else{*/
	    				path = exPath + uuid+"."+fileTypeEnum.xls.toString();  //+".dat"
	    			/*}*/
	    			ExportExcelUtil excelUtil = new ExportExcelUtil(path);
	    			excelUtil.buildExcelDocument(dataTitle, dataValue, list);
	    		
	    		}else if (exportTypeEnum.Csv.toString().equals(exportType)) {
	    			//创建生成文件路径，并生成csv
	    			typeName = fileTypeEnum.csv.toString();
	    			/*if(!"".equals(expFileName)){
	    				path = exPath + expFileName+"."+fileTypeEnum.csv.toString(); //+".dat"
	    			}else{*/
	    				path = exPath + uuid+"."+fileTypeEnum.csv.toString(); //+".dat"
	    			/*}*/
	    			
	    			CsvUtil export = new CsvUtil(path);
	    			export.exportCsv(dataTitle, dataValue, list);
	    			
	    		}else if (exportTypeEnum.Html.toString().equals(exportType)) {
	    			//创建生成文件路径，并生成html
	    			typeName = fileTypeEnum.html.toString();
	    			/*if(!"".equals(expFileName)){
	    				path = exPath + uuid+"."+fileTypeEnum.html.toString(); //+".dat"
	    			}else{*/
	    				path = exPath + uuid+"."+fileTypeEnum.html.toString(); //+".dat"
	    			/*}*/
	    			
	    			HtmlUtil htmlUtil = new HtmlUtil(path);
	    			htmlUtil.exportHtml(dataTitle, dataValue, list);
	    			
	    		}else if (exportTypeEnum.Pdf.toString().equals(exportType)) {
	    			//创建生成文件路径，并生成pdf
	    			typeName = fileTypeEnum.pdf.toString();
	    			/*if(!"".equals(expFileName)){
	    				path = exPath + expFileName+"."+fileTypeEnum.pdf.toString(); //+".dat"
	    			}else{*/
	    				path = exPath + uuid+"."+fileTypeEnum.pdf.toString(); //+".dat"
	    			/*}*/
	    			
	    			PDFUtil pdfUtil = new PDFUtil(path);	
	    			pdfUtil.writeListToResponse(list,dataTitle,dataValue);
	    			
	    		}
	    		
	    		//存入文件数据到表中
	    		fileModel = new FileModel();
				fileModel.setMediaType(mediaTypeEnum.local.toString());
				fileModel.setAccessType("private");     
				fileModel.setFileUUID(uuid);	
				fileModel.setLength("0");	
				/*if (fileTypeEnum.xls.toString().equals(typeName)) {
					fileModel.setTitle("exportExcel"+dateName);
				}else{
					fileModel.setTitle("export"+typeName.toUpperCase()+dateName);	
				}*/
				if (fileTypeEnum.xls.toString().equals(typeName)) {
					if(!"".equals(expFileName)){
						fileModel.setTitle(expFileName);
					}else{
						fileModel.setTitle("exportExcel"+dateName);
					}
					
				}else{
					if(!"".equals(expFileName)){
						fileModel.setTitle(expFileName);
					}else{
						fileModel.setTitle("export"+typeName.toUpperCase()+dateName);
					}
				}
				
				fileModel.setCreateTime(DateUtils.getDateToStr("yyyy-MM-dd HH:mm:ss",date));	
				fileModel.setFileType(typeName.toLowerCase());
				fileModel.setSource("export");
				fileModel.setPath(exPath);
				fileModel.setCreateBy(Integer.parseInt(SessionUtils.getUserInfo().getEmpId().toString()));	
			} catch (Exception e) {
				FormLogger.error(e.getMessage(), e);
			}
		}
		
		return fileModel;
	}
}
