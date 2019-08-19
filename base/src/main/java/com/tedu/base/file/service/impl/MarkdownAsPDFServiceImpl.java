package com.tedu.base.file.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.markdownj.MarkdownProcessor;
import org.springframework.stereotype.Service;

import com.tedu.base.file.interceptor.HtmlToPdfInterceptor;
import com.tedu.base.file.service.MarkdownAsPDFService;
import com.tedu.base.initial.model.xml.ui.FormConstants;






@Service("markdownAsPDFService")
public class MarkdownAsPDFServiceImpl implements MarkdownAsPDFService{

	
	/**
	 * markdown转html
	 * @param inputUrl 	markdown输入流
	 * @param outUrl		html输出
	 * @return
	 * @throws IOException
	 */
	@Override
    public  String mdToHtml(String inputUrl,String outUrl) throws IOException{
        String html = "";
        BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(inputUrl),"UTF-8"));
        int len=-1;
        while((len=br.read())!=-1) {
			html+=String.valueOf((char)len);
		}
        MarkdownProcessor markdownProcessor = new MarkdownProcessor();
        html = markdownProcessor.markdown(html);
        String css="<head><meta charset='UTF-8'></head>";
        OutputStreamWriter osw=new OutputStreamWriter(new FileOutputStream(outUrl),"UTF-8");
        osw.write(css);
        osw.write(html);
        br.close();
        osw.close();
        return html;
    }
 
    /** 
     * html转pdf 
     * @param srcPath html 
     * @param destPath pdf保存路径 
     * @return 转换成功返回true 
     */  
    @Override
    public  boolean convert(String srcPath, String destPath){  
          
        StringBuilder cmd = new StringBuilder();  
        cmd.append(FormConstants.TOPDFTOOL);  
        cmd.append(" ");  
        cmd.append(srcPath);  
        cmd.append(" ");  
        cmd.append(destPath);  
          
        boolean result = true;  
        try{  
            Process proc = Runtime.getRuntime().exec(cmd.toString());  
            HtmlToPdfInterceptor error = new HtmlToPdfInterceptor(proc.getErrorStream());  
            HtmlToPdfInterceptor output = new HtmlToPdfInterceptor(proc.getInputStream());  
            error.start();  
            output.start();  
            proc.waitFor();  
        }catch(Exception e){  
            result = false;  
            e.printStackTrace();  
        }  
          
        return result;  
    }  

}
