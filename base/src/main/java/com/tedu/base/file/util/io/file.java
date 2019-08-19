package com.tedu.base.file.util.io;

import java.io.File;

import org.apache.ibatis.javassist.compiler.ast.NewExpr;

import com.thoughtworks.xstream.io.path.Path;

public class file {
	private String coding;
	private String path = "";
	//系统路径  默认windows
	private String fileSystem = "windows";

	/**
	 * 构造方法，默认windows操作系统
	 * @param 输入文件硬盘路径
	 */
	public file(String path) {
		this.path = path;
		String os = System.getProperty("os.name");  
		if(os.toLowerCase().startsWith("win")){  
			fileSystem = "windows";
		}else{
			fileSystem = "linux";
		}
	}

	/**
	 * 
	 * @Description: 根据传入的操作系统参数
	 * @author: gaolu
	 * @date: 2017年8月15日 下午1:07:35  
	 * @param 输入文件硬盘路径
	 * @param 输入操作系统类型     
	 * @return: void      
	 * @throws
	 */
	public file(String path,String fileSystem) {
		if(fileSystem.equals("windows")){
			path = path.replace("/", "\\");
		}else if(fileSystem.equals("linux")){
			path = path.replace("\\", "/");
		}
		this.path = path;
		this.fileSystem = fileSystem;
	}
	
	/**
	 * 
	 * @Description: 创建目录
	 * @author: gaolu
	 * @date: 2017年8月15日 下午1:10:35  
	 * @param:       
	 * @return: void      
	 * @throws
	 */
	public void createlist() {
		File f;
		f = new File(path);
		f.mkdirs();
	}
	
	
	/**
	 * 
	 * @Description: 创建文件
	 * @author: gaolu
	 * @date: 2017年8月15日 下午1:14:24  
	 * @param:       
	 * @return: void      
	 * @throws
	 */
	public void createfile() {
		try {
			File f;
			int index = path.length();
			if(path.contains("\\")){
				index = path.lastIndexOf("\\");
			}else {
				index = path.lastIndexOf("/");
			}
				
				String list = path.substring(0, index);
				
				f = new File(list);
			
			//如果目录不存在，先创建目录
			if (!f.exists()) {
				f.mkdirs();
			}
			//然后创建文件
			f = new File(path);
			f.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Description: 删除单个文件
	 * @author: gaolu
	 * @date: 2017年8月15日 下午1:23:31  
	 * @param:       
	 * @return: void      
	 * @throws
	 */
	public void deletefile() {
		File f = new File(path);
		f.delete();
	}
	
	/**
	 * 
	 * @Description: 删除多个文件
	 * @author: gaolu
	 * @date: 2017年8月15日 下午1:25:31  
	 * @param:       
	 * @return: void      
	 * @throws
	 */
	public void deletefiles() {
		File f = new File(path);
		String[] files = f.list();
		for (int a = 0; a < files.length; a++) {
			File file = new File(path + "\\" + files[a]);
			file.delete();
		}
	}
	
	
    public boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

	/**
	 * 
	 * @Description: set,get方法
	 * @author: gaolu
	 * @date: 2017年8月15日 下午1:09:27  
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	public String getCoding() {
		return coding;
	}

	public void setCoding(String coding) {
		this.coding = coding;
	}

	public String getFileSystem() {
		return fileSystem;
	}

	public void setFileSystem(String fileSystem) {
		this.fileSystem = fileSystem;
	}
	
}
