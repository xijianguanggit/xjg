package com.tedu.base.file.util;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.tedu.base.file.util.io.file;


public class ExportExcelUtil {
	private static final Logger log = Logger.getLogger(ExportExcelUtil.class);
	private String path;
	
	public ExportExcelUtil(String path) {
		this.path = path;
	}
	
    public void buildExcelDocument(String[] dataTitle,String[] dataValue,List<Map<String, Object>> list)  
            throws Exception {
    	
		//创建文件
	    file excelFile = new file(path);
	    excelFile.createfile();
		
		// 第一步，创建一个webbook，对应一个Excel文件  
        Workbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        Sheet sheet = wb.createSheet("sheet1");  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        Row titleRow = sheet.createRow(0);  
        
        for (int i = 0; i < dataTitle.length; i++) {
        	 Cell cell = titleRow.createCell(i);
        	 cell.setCellValue(dataTitle[i]); 
		}
        for(int x = 0; x < list.size() ;x++){
        	Row dataRow = sheet.createRow(x + 1);  
        	for(int y = 0;y < dataValue.length ; y++){
        		Cell dataCell = dataRow.createCell(y);  
        		if (list.get(x).get(dataValue[y])!=null) {
        			dataCell.setCellValue(list.get(x).get(dataValue[y]).toString());
				}else{
					dataCell.setCellValue("");
				}
        		
        	}
        }
  
        // 第六步，将文件存到指定位置  
        try  
        {  
            FileOutputStream fout = new FileOutputStream(path);  
            wb.write(fout);  
            fout.close();  
        }  
        catch (Exception e)  
        {  
        	log.error(e.getMessage(),e);
        }  
    }  
	

}
