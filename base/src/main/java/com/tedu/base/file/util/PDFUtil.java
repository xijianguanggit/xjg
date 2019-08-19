package com.tedu.base.file.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tedu.base.file.util.io.file;



/**
 * PDFUtil
 * @author gaolu
 *
 */
public class PDFUtil {
	private static final Logger log = Logger.getLogger(PDFUtil.class);
	//itext document
	private Document document = new Document();
	private String path = "";
	
	/**
	 * 构造方法
	 * @param 输入PDF文件的HttpServletResponse对象
	 */
	public PDFUtil(String path){
		this.path = path;
	}
	
	
	/**
	 * 将List数据写入到OutputStream
	 * @param list
	 * @return
	 */
	public void writeListToResponse(List list,String[] dataTitle, String[] dataValue) throws Exception{
		PdfWriter pw = null;
		
		try{
			//创建文件
		    file pdfFile = new file(path);
		    pdfFile.createfile();
			pw = PdfWriter.getInstance(document, new FileOutputStream(path));
			this.writeList(list,dataTitle,dataValue);
		}catch(Exception e){
			throw e;
		}finally{
			//关闭io流
			try{
				if(pw!=null){
					pw.close();
				}
			}catch(Exception e){
				throw e;
			}
		}
	}

	
	
	/**
	 * 根据list，将数据循环写入pdf文件
	 * @param
	 * @return
	 */
	private void writeList(List<Map<Object, Object>> list,String[] dataTitle, String[] dataValue) throws Exception{
		if(list!=null&&list.size()>0){
			  
			document.open();  
			//中文font
			BaseFont chineseBaseFont = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);  
			Font chineseFont = new Font(chineseBaseFont);  

			//pdf表格对象
			PdfPTable table = new PdfPTable(dataTitle.length);  
			
			//创建表格头
			for (int i = 0,j = dataTitle.length; i < j; i++) {
				table.addCell(new Phrase(dataTitle[i],chineseFont));  
			}
			 
			for(int a=0;a<list.size();a++){
				
				for(int x = 0,y = dataValue.length;x < y;x++){
					//设置反射对象,并调用getter方法获取字段的值
					Object result = list.get(a).get(dataValue[x]);
					//创建列
					if(result == null){
						table.addCell(new Phrase("",chineseFont));  
					}else{
						table.addCell(new Phrase(String.valueOf(result),chineseFont));
					}
				}
				
			}

			document.add(table);  
			
			document.close();  
		}
	}

	public Integer office2PDF (String sourceFile, String destFile) throws Exception{
		//sourceFile = "D:\\files\\model\\testPDF.xls";
		//destFile = "D:\\files\\model\\testPDF1.pdf";
        try {  
            File inputFile = new File(sourceFile);  
            if (!inputFile.exists()) {  
                return -1;// 找不到源文件, 则返回-1  
            }  
  
            // 如果目标路径不存在, 则新建该路径  
            File outputFile = new File(destFile);  
            if (!outputFile.getParentFile().exists()) {  
                outputFile.getParentFile().mkdirs();  
            }  
  
            //OpenOffice_HOME = "C:\\Program Files (x86)\\OpenOffice 4\\";
            // 启动OpenOffice的服务  
/*            String command = OpenOffice_HOME  
                    + "program\\soffice.exe -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\"";  
            Process pro = Runtime.getRuntime().exec(command);  */
            // connect to an OpenOffice.org instance running on port 8100 
            OpenOfficeConnection connection = new SocketOpenOfficeConnection(  
                    "127.0.0.1", 8100);  
            connection.connect();  
  
            // convert  
            DocumentConverter converter = new OpenOfficeDocumentConverter(  
                    connection);  
            converter.convert(inputFile, outputFile);  
  
            // close the connection  
            connection.disconnect();  
            // 关闭OpenOffice服务的进程  
/*            pro.destroy(); */ 
 
            return 0;  
        } catch (Exception e) {  
        	log.error(e.getMessage(),e);
            return -1;  
        }  
	}
	
	public static void main(String[] args) {
		PDFUtil pdf = new PDFUtil("");
		try {
			pdf.office2PDF("", "");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}

}
