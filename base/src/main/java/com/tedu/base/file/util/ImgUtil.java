package com.tedu.base.file.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

/**
 * @author wangzhenyang
 * @modify
 * @Description: 图片处理
 *
 * @date 2018/4/18
 */
public class ImgUtil {
	Logger log = Logger.getLogger(this.getClass());
	public static final String QINIU_URL="http://p57opmqzo.bkt.clouddn.com/";
	public static final String ACCESSKEY = "9UodED1p_MDm4Dg8Zd3xRByYVr0w7xkwoc6f3ZNO";
	public static final String SRCRETKEY = "zo1XffZS8_N5YJF2WGI6o_7yXDCF67tShAmMH2Pr";
	public static final String BUCKET="pic1";	
	
	
	
	public static DefaultPutRet LoadImg(InputStream inputStream) {
		Configuration cfg = new Configuration(Zone.zone2());
		UploadManager uploadManager = new UploadManager(cfg);
		// 默认不指定key的情况下，以文件内容的hash值作为文件名
		String key = null;
		Auth auth = Auth.create(ACCESSKEY, SRCRETKEY);
		String upToken = auth.uploadToken(BUCKET);
		DefaultPutRet putRet = null;
		try {
			Response response = uploadManager.put(inputStream, key, upToken,null,null);
			// 解析上传成功的结果
			putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
			
		} catch (QiniuException ex) {
			Response r = ex.response;
			try {
				System.err.println(r.bodyString());
			} catch (QiniuException ex2) {
				// ignore
			}
		}
		putRet.key= QINIU_URL + putRet.key;
		return putRet;
	}
	@Test
	public void t1() throws FileNotFoundException{
		FileInputStream fis=new FileInputStream("D:"+File.separator+"ssaa_1524022517575.png");
		DefaultPutRet putRet=LoadImg(fis);
		log.info(putRet.key);
		log.info(putRet.hash);
		
	}
}
