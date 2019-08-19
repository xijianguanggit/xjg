package com.tedu.base.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.RandomAccessFile;

import org.apache.log4j.Logger;

public class FileUtil {
	private static final Logger log = Logger.getLogger(FileUtil.class);
	 /** 
	  * 创建文件 
	  * @param fileName 
	  * @return 
	  */  
	 public static boolean createFile(File fileName)throws Exception{  
	  boolean flag=false;  
	  try{  
	   if(!fileName.exists()){  
	    fileName.createNewFile();  
	    flag=true;  
	   }  
	  }catch(Exception e){  
		  log.error(e.getMessage());;  
	  }  
	  return true;  
	 }   
	   
	 /** 
	  * 读TXT文件内容 
	  * @param fileName 
	  * @return 
	  */  
	 public static String readTxtFile(File fileName)throws Exception{  
	  String result="";  
	  FileReader fileReader=null;  
	  BufferedReader bufferedReader=null;  
	  try{  
	   fileReader=new FileReader(fileName);  
	   bufferedReader=new BufferedReader(fileReader);  
	   try{  
	    String read="";  
	    while((read=bufferedReader.readLine())!=null){  
	     result=result+read+"\r\n";  
	    }  
	   }catch(Exception e){  
		   log.error(e.getMessage());
	   }  
	  }catch(Exception e){  
		  log.error(e.getMessage());
	  }finally{  
	   if(bufferedReader!=null){  
	    bufferedReader.close();  
	   }  
	   if(fileReader!=null){  
	    fileReader.close();  
	   }  
	  }  
	  System.out.println("读取出来的文件内容是："+"\r\n"+result);  
	  return result;  
	 }  
	   
	   
	 public static boolean writeTxtFile(String content,File  fileName)throws Exception{  
	  RandomAccessFile mm=null;  
	  boolean flag=false;  
	  FileOutputStream o=null;  
	  try {  
	   o = new FileOutputStream(fileName);  
	      o.write(content.getBytes("GBK"));  
	      o.close();  
	//   mm=new RandomAccessFile(fileName,"rw");  
	//   mm.writeBytes(content);  
	   flag=true;  
	  } catch (Exception e) {  
	   // TODO: handle exception  
		  log.error(e.getMessage()); 
	  }finally{  
	   if(mm!=null){  
	    mm.close();  
	   }  
	  }  
	  return flag;  
	 }  

	 public static boolean writeTxtFileUtf8(String content,File  fileName)throws Exception{  
		  RandomAccessFile mm=null;  
		  boolean flag=false;  
		  FileOutputStream o=null;  
		  try {  
		   o = new FileOutputStream(fileName);  
		      o.write(content.getBytes("utf-8"));  
		      o.close();  
		//   mm=new RandomAccessFile(fileName,"rw");  
		//   mm.writeBytes(content);  
		   flag=true;  
		  } catch (Exception e) {  
		   // TODO: handle exception  
			  log.error(e.getMessage()); 
		  }finally{  
		   if(mm!=null){  
		    mm.close();  
		   }  
		  }  
		  return flag;  
		 }  
	  
	 public static boolean deleteFile(File dirFile) {
		    // 如果dir对应的文件不存在，则退出
		    if (!dirFile.exists()) {
		        return false;
		    }

		    if (dirFile.isFile()) {
		        return dirFile.delete();
		    } else {

		        for (File file : dirFile.listFiles()) {
		            deleteFile(file);
		        }
		    }

		    return dirFile.delete();
		}

	    //复制方法
	    public static void copyDir(String src, String des) throws Exception {
	        //初始化文件复制
	        File file1=new File(src);
	        //把文件里面内容放进数组
	        File[] fs=file1.listFiles();
	        //初始化文件粘贴
	        File file2=new File(des);
	        //判断是否有这个文件有不管没有创建
	        if(!file2.exists()){
	            file2.mkdirs();
	        }
	        //遍历文件及文件夹
	        for (File f : fs) {
	            if(f.isFile()){
	                //文件
	                fileCopy(f.getPath(),des+File.separator+f.getName()); //调用文件拷贝的方法
	            }else if(f.isDirectory()){
	                //文件夹
	            	copyDir(f.getPath(),des+File.separator+f.getName());//继续调用复制方法      递归的地方,自己调用自己的方法,就可以复制文件夹的文件夹了
	            }
	        }
	        
	    }

	    /**
	     * 文件复制的具体方法
	     */
	    private static void fileCopy(String src, String des) throws Exception {
	        //io流固定格式
	        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
	        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(des));
	        int i = -1;//记录获取长度
	        byte[] bt = new byte[2014];//缓冲区
	        while ((i = bis.read(bt))!=-1) {
	            bos.write(bt, 0, i);
	        }
	        bis.close();
	        bos.close();
	        //关闭流
	    }
	public static void contentToTxt(String filePath, String content) {  
	        String str = new String(); //原有txt内容  
	        String s1 = new String();//内容更新  
	        try {  
	            File f = new File(filePath);  
	            if (f.exists()) {  
	                System.out.print("文件存在");  
	            } else {  
	                System.out.print("文件不存在");  
	                f.createNewFile();// 不存在则创建  
	            }  
	            BufferedReader input = new BufferedReader(new FileReader(f));  
	  
	            while ((str = input.readLine()) != null) {  
	                s1 += str + "\n";  
	            }  
	            input.close();  
	            s1 += content;  
	  
	            BufferedWriter output = new BufferedWriter(new FileWriter(f));  
	            output.write(s1);  
	            output.close();  
	        } catch (Exception e) {  
	        	log.error(e.getMessage());  
	        }  
	    }  
}
