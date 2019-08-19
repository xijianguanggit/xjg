package com.tedu.base.file.util.operation;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.file.util.UploadFile;
import com.tedu.base.file.util.io.file;

/**
 * 服务器本地文件操作
 * @author gaolu
 *
 */
public class LocalOperation {
	
	HttpServletResponse response;
	String hintMessage;

	/**
	 * 
	 * @Description: 普通构造方法
	 * @author: gaolu
	 * @date: 2017年8月17日 上午9:50:00  
	 * @param:
	 */
	public LocalOperation() {}
	
	/**
	 * 构造方法,文件下載需要
	 * @param 输入HttpServletResponse对象
	 */
	public LocalOperation(HttpServletResponse response) {
		this.response = response;
	}
	
	/**
	 * 
	 * @Description: 文件上传
	 * @author: gaolu
	 * @date: 2017年8月15日 下午2:04:55  
	 * @param:上传结果
	 * @return: void      
	 */
	public String fileUpload(UploadFile upfiles, String createFilePath) {
		//文件最大限制
		String maxSize = upfiles.getParameter("maxSize");
		//允许文件格式
		String allowFile = upfiles.getParameter("allowFile");
		//上传地址
		upfiles.setUploadRootList(createFilePath);
		//上传文件最大限定
		if (StringUtils.isNotEmpty(maxSize)) {
			upfiles.setFileSizeMax(Long.parseLong(maxSize)*1024);
		}
		//上传文件限定格式
		if(StringUtils.isNotEmpty(allowFile)){
			upfiles.setAllowFile(allowFile);
		}
		//创建文件目录
		//System.out.println("+++++++++++++"+createFilePath+"++++++++++++++++++");
		file f = new file(createFilePath);
		f.createlist();
		try {
			upfiles.upload();
			//上传的提示消息
			hintMessage = upfiles.getUploadHint();
		} catch (Exception e) {
			hintMessage = "文件上传本地失败";
			//e.printStackTrace();
			throw e;
		}
		return hintMessage;
	}
	/**
	 * 
	 * @Description: 文件下载
	 * @author: gaolu
	 * @date: 2017年8月15日 下午2:04:55  
	 * @param:下载路径，文件原始名称
	 * @return: void      
	 */
	  public void fileDownload(String fPath,boolean previewPdf,String fileName)
	  {
		String SPT = File.separator;
	    InputStream in = null;
	    ServletOutputStream out = null;
	    if (StringUtils.isEmpty(fileName)) {
	    	fileName = fPath.replace(".dat", "");
	    	fileName = fileName.substring(fileName.lastIndexOf(SPT)+1);
		}
	    
	    try
	    {
	      if (previewPdf) {
	    	  this.response.setContentType("application/pdf");
			}else{
				this.response.setContentType("application/x-msdownload");
				this.response.setHeader("Content-Disposition", "attachment;filename=" + 
						java.net.URLEncoder.encode(fileName, "UTF-8"));
			}
	      File file = new File(fPath);
	      in = new FileInputStream(file);
	      out = this.response.getOutputStream();
	      byte[] bytes = new byte[1024];
	      int c;
	      while ((c = in.read(bytes)) != -1)
	      {
	        //int c;
	        out.write(bytes, 0, c);
	      }
	    }
	    catch (Exception e) {
	      FormLogger.error("下载文件接口",String.format("读取文件{%s}失败，文件不存在或操作异常:%s",fPath,e.getMessage()), FormLogger.LOG_TYPE_ERROR);
	    }
	    finally
	    {
	      try
	      {
	        if (in != null) {
	          in.close();
	        }
	        if (out != null)
	          out.close();
	      }
	      catch (Exception e) {
	    	FormLogger.error("下载文件接口",String.format("文件{%s}流关闭失败:%s",fPath,e.getMessage()), FormLogger.LOG_TYPE_ERROR);
	      }
	    }
	  }

	/**
	 * 
	 * @Description: set,get方法
	 * @author: gaolu
	 * @date: 2017年8月17日 上午10:13:08  
	 * @param: @return      
	 * @return: String
	 */
	public String getHintMessage() {
		return hintMessage;
	}

	public void setHintMessage(String hintMessage) {
		this.hintMessage = hintMessage;
	}

	  
}
