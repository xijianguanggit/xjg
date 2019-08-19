package com.tedu.base.file.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.tedu.base.file.util.io.FilePathUtil;
import com.tedu.base.file.util.io.file;

public class HtmlUtil {
	Logger log = Logger.getLogger(this.getClass());
	private String path ;
	
	public HtmlUtil(String path){
		this.path = path;
	}
	/**
	 * 
	 * @Description: 导出html
	 * @author: gaolu
	 * @date: 2017年8月28日 上午5:05:25  
	 * @param:      
	 * @return: void
	 */
	public void exportHtml(String[] dataTitle,String[] dataValue,List<Map<String, Object>> list) throws IOException{
		int count = dataTitle.length;
	     
/*	    BufferedInputStream bis = null;
	    BufferedOutputStream out = null;*/
	    file HtmlFile = new file(path);
	    //创建文件
	    HtmlFile.createfile();
	    File file = new File(path);
	    OutputStreamWriter outStream = null;
//	    FileWriter fwwe;
	    String htmlText = "";
	    StringBuilder data = new StringBuilder();
	    StringBuilder title = new StringBuilder();
	    try {
//	    	fwwe = new FileWriter(file);
	    	outStream = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
	    	BufferedWriter bw = new BufferedWriter(outStream);
	    	title.append("<tr>");
	    	for (int i = 0; i < count; i++) {
	    		title.append("<td>"+dataTitle[i]+"</td>");
			}
	    	title.append("</tr>");
	    	for(Map<String, Object> o : list){
	    		data.append("<tr>");
	    		for(int i = 0;i<count;i++){
	    			try {
	    				Object resultValue = o.get(dataValue[i]);
	    				    data.append("<td>");
	    					if (resultValue == null) {
	    						data.append("</td>");
							}else {	
								data.append(String.valueOf(resultValue) + "</td>");
							}
					} catch (Exception e) {
							data.append("<td> </td>");
							log.error(e.getMessage(),e);
					}
	    		}
	    		data.append("</tr>");
	    	}
	    	htmlText += "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n";
	    	htmlText +="<table>"+title.toString()+data.toString()+"</table>";
	    	bw.write(htmlText);
	    	bw.close();
//	    	fwwe.close();
	    	outStream.close();
/*	        bis = new BufferedInputStream(new FileInputStream(file));
	        out = new BufferedOutputStream(response.getOutputStream());
	        byte[] buff = new byte[2048];
	        while (true) {
	          int bytesRead;
	          if (-1 == (bytesRead = bis.read(buff, 0, buff.length))){
	              break;
	          }
	          out.write(buff, 0, bytesRead);
	        }
	        file.deleteOnExit();*/
	    }
	    catch (IOException e) {
	        throw e;
	    }
	/*    finally{
	        try {
	            if(bis != null){
	                bis.close();
	            }
	            if(out != null){
	                out.flush();
	                out.close();
	            }
	        }
	        catch (IOException e) {
	            throw e;
	        }
	    }*/
	}
}
