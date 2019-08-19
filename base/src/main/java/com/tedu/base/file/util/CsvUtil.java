package com.tedu.base.file.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.log4j.Logger;

import com.tedu.base.file.util.io.file;


public class CsvUtil {
	private static final Logger log = Logger.getLogger(CsvUtil.class);

	private String path;
	
	public CsvUtil(String path){
		this.path = path;
	}
	/**
	 * 
	 * @Description: 生成csv文件并储存
	 * @author: gaolu
	 * @date: 2017年8月28日 上午2:01:19  
	 * @param:      
	 * @return: void
	 */
	public void exportCsv(String[] dataTitle,String[] dataValue,List<Map<String, Object>> list) throws IOException{

		int count = dataTitle.length;
	    
	    //response.setContentType("application/octet-stream");
	    //response.addHeader("Content-Disposition", "attachment; filename=" + "exportCSV.csv");
	    //BufferedInputStream bis = null;
	    //BufferedOutputStream out = null;
	    file csvFile = new file(path);
	    csvFile.createfile();
	    File filetmp = new File(path);
	    FileWriterWithEncoding fwwe;
	    try {
	    	fwwe = new FileWriterWithEncoding(filetmp,"UTF-8");
	    	BufferedWriter bw = new BufferedWriter(fwwe);
	    	bw.write("\ufeff");
	    	String title = "";
	    	for (int i = 0; i < count; i++) {
	    		if (i == (count-1)) {
					title += dataTitle[i] ;
				}else{
					title += dataTitle[i] + ",";
				}
			}
	    	bw.write(title);
			bw.write("\n");
	    	
	    	for(Map<String, Object> o : list){
	    		String data = "";
	    		for(int i = 0;i<count;i++){
	    			try {
	    				Object resultValue = o.get(dataValue[i]);
	    				if (i == (count-1)) {
	    					if (resultValue == null) {
								data += "\" \"";
							}else{
								data += "\""+String.valueOf(resultValue)+"\"";
							}
						}else{
							if(resultValue == null){
								data += "\"\" ,";
							}else{
								data += "\""+String.valueOf(resultValue)+"\",";
							}
						}
					} catch (Exception e) {
						if (i == (count-1)) {
							data += "";
						}else{
							data += ""+",";
						}
						log.error(e.getMessage(),e);
					}
	    		}
	    		bw.write(data);
	    		bw.write("\n");
	    	}
	    	bw.close();
	    	fwwe.close();
//	        bis = new BufferedInputStream(new FileInputStream(file));
//	        out = new BufferedOutputStream(response.getOutputStream());
//	        byte[] buff = new byte[2048];
//	        while (true) {
//	          int bytesRead;
//	          if (-1 == (bytesRead = bis.read(buff, 0, buff.length))){
//	              break;
//	          }
//	          out.write(buff, 0, bytesRead);
//	        }
//	        file.deleteOnExit();
	    }
	    catch (IOException e) {
	        throw e;
	    }
/*	    finally{
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
	    long endTime = System.currentTimeMillis();    //获取结束时间
	}
}
