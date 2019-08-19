package com.tedu.base.auth.login.controller;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.engine.util.FormLogger;  

@Controller
public class DownloadAction  
{  
    @RequestMapping("downloadList")    
    public String downloadList(Model Model)  {
    	List<String> list = new ArrayList<String>();
//    	Map<String, String> map = new HashMap<String, String>();
//    	map.put("name", "navicat.zip");
//    	map.put("url", "E:\\tool\\navicat.zip");
    	list.add("navicat.rar");
//    	map = new HashMap<String, String>();
//    	map.put("name", "eclipse.rar");
//    	map.put("url", "E:\\tool\\eclipse.rar");
    	list.add("eclipse.rar");
    	list.add("huanjingdajian.rar");
//    	map = new HashMap<String, String>();
//    	map.put("name", "mysql.rar");
//    	map.put("url", "E:\\tool\\mysql.rar");
    	list.add("mysql.rar");
    	list.add("SSH工具.rar");
    	Model.addAttribute("menuList", list);
    	return "downloadList";
    }  
  
    @RequestMapping("download")    
    public String downloadFile(@RequestParam("fileName") String fileName,
            HttpServletRequest request, HttpServletResponse response) {
        if (fileName != null) {
            String realPath = "E:\\tool\\"+fileName;
            File file = new File(realPath);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition",
                        "attachment;fileName=" + fileName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                } catch (Exception e) {
                	FormLogger.error("下载文件接口",String.format("读取文件失败，文件不存在或操作异常"), FormLogger.LOG_TYPE_ERROR);
                	throw new ServiceException(ErrorCode.FILE_EXCEPTION,"文件操作异常");
                } finally {
                	try {
                		if (bis != null) {
                            bis.close();
                        } 
                		if (fis != null) {
                            fis.close();
                		}
                    }catch (IOException e) {
                    	FormLogger.error("下载文件接口",String.format("关闭文件失败"), FormLogger.LOG_TYPE_ERROR);
                    	throw new ServiceException(ErrorCode.FILE_EXCEPTION,"文件操作异常");
                    }
                }
             }
          }
        return null;
    }  
}  
