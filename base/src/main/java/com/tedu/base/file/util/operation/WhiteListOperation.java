package com.tedu.base.file.util.operation;

/**
 * 文件白名单操作类，包括：验证文件，对比返回文件大类（预览用：如jpg,png归为图片类）
 * @author gaolu
 *
 */
public class  WhiteListOperation  {
	//导出类型
	static private enum whiteList {
		jpg,png,bmp,jpeg,gif,doc,docx,dot,dotx,xls,xlsx,ppt,pptx,pps,ppsx,pdf,txt,cvs,wps,dps,et,rar,gz,zip,md,html
	};	
	
	static private enum fileBelong {
		IMAGE,WORD,MARKDOWN,PDF,VIDEO,HTML
	}
	
	/**
	 * 
	 * @Description: 将根据文件名后缀分类文件
	 * @author: gaolu
	 * @date: 2017年10月27日 下午4:53:39  
	 * @param:      
	 * @return: String
	 */
	static public String classify(String fileType){
		//文件名后缀分类文件
		if ("jpg".equalsIgnoreCase(fileType)||"png".equalsIgnoreCase(fileType)||"bmp".equalsIgnoreCase(fileType)||"jpeg".equalsIgnoreCase(fileType)||"gif".equalsIgnoreCase(fileType)) {
			return fileBelong.IMAGE.toString();
		}
		
		if ("md".equalsIgnoreCase(fileType)) {
			return  fileBelong.MARKDOWN.toString();
		}
		
		if ("doc".equalsIgnoreCase(fileType)||"docx".equalsIgnoreCase(fileType)) {
			return  fileBelong.WORD.toString();
		}
		
		if ("pdf".equalsIgnoreCase(fileType)) {
			return  fileBelong.PDF.toString();
		}
		
		if ("mp4".equalsIgnoreCase(fileType)) {
			return  fileBelong.VIDEO.toString();
		}
		
		if ("html".equalsIgnoreCase(fileType)) {
			return  fileBelong.HTML.toString();
		}
		return "";
	}
	

	
}
