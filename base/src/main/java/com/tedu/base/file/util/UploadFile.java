package com.tedu.base.file.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.file.util.io.BasicFile;
import com.tedu.base.file.util.io.NetPath;
import com.tedu.base.file.util.io.file;


public class UploadFile {
	private static final Logger log = Logger.getLogger(UploadFile.class);
	private DiskFileItemFactory dfif = new DiskFileItemFactory();

	private ServletFileUpload file = null;

	private ArrayList<FileItem> list = null;
	
	private HttpServletRequest request;

	//上传文件名
	public String[] filename = null;
	//上传文件后缀
	public String[] filepostfix = null;
    //判断当前文件是否允许上传
	private boolean allowupload = true;
	//存储所有提交的前台form字段
	private Hashtable<String, String> ht = new Hashtable();
	//程序员传入的上传文件路径
	private String[] uploadpath = null;
    //是否使用默认的文件名
	private boolean defaultfilename = false;
	//所有上传文件大小，单位为字节
	public long allfilesize = 0;
	//单个文件允许上传的文件大小
	private long allowfilesize = 0;
	//所有文件允许上传的文件大小
	private long allowallsize = 0;
	//允许上传的文件类型
	private String allowfile = "";
	//不允许上传的文件类型
	private String noallowfile = "";
	//上传文件根路径
	private String uploadRootList;
	//上传后获取的上传文件名称List
	private List<String> uploadedFileNameList = new ArrayList<String>();
	//上传文件提示信息
	private String uploadHint;
	//应用根硬盘路径
	private String serverPath;
	//文件二进制流
	private InputStream[] fileInputStream;
	//上传临时文件路径
	private final String uploadtemp = "uploadtemp"+File.separator+File.separator;
	//安全字段,为防止注入攻击，设置安全字段，如果是true说明处于安全状态，如果是false则不是安全状态;
	boolean safeWarn = true;
	//文件后缀白名单
	private String fileWhiteList = "jpg,png,bmp,jpeg,gif,doc,docx,dot,dotx,xls,xlsx,ppt,pptx,pps,ppsx,pdf,txt,cvs,wps,dps,et,rar,gz,zip,md,mp4,mp3,flv,swf,py,ipynb,html,wmv";
	//白名单中文件所对应头信息集合
	private static Map<String, String> fileType = new HashMap<String, String>();
/*	static {
		fileType.put("jpg", "FFD8FF");
		fileType.put("jpeg", "FFD8FF");
		fileType.put("png", "89504e");
		fileType.put("bmp", "424d22");
		fileType.put("bmp", "424d82");
		fileType.put("bmp", "424d8e");
		fileType.put("gif", "474946");
		fileType.put("docx", "504b03");
		fileType.put("pptx", "504b03");
		fileType.put("xlsx", "504b03");
		fileType.put("ppsx", "504b03");
		fileType.put("ls", "D0CF11");
		fileType.put("ppt", "D0CF11");
		fileType.put("dot", "D0CF11");
		fileType.put("doc", "D0CF11");
		fileType.put("pps", "D0CF11");
		fileType.put("wps", "D0CF11");
		fileType.put("dps", "D0CF11");
		fileType.put("et", "D0CF11");
		fileType.put("pdf", "255044");
		fileType.put("cvs", "5B7572");
		fileType.put("rar", "526172");
		fileType.put("gz", "1f8b08");
		fileType.put("zip", "504B03");
		fileType.put("txt", "746869");
		
	}*/
	
	
	/**
	 * 
	 * @Description: 输入HttpServletRequest构造上传文件类，可实现多文件上传
	 * @author: gaolu
	 * @date: 2017年8月15日 下午12:12:24  
	 * @param: @return      
	 * @return: String类型，返回路径的完整字符串     
	 * @throws
	 */
	public UploadFile(HttpServletRequest request, String serverPath) {
		this.request = request;
		//如果客户端没有传入应用根路径，就自动生成
		if(this.serverPath==null||this.serverPath.equals("")){
			this.serverPath = NetPath.getServerPath();
		}else{
			this.serverPath = serverPath;
		}
		//System.out.println("###########"+this.serverPath+uploadtemp);

		try {
			//创建上传临时目录
			file f = new file(this.serverPath+uploadtemp);
			f.createlist();

			//设置上传临时目录
			dfif.setSizeThreshold(1024 * 1024 * 1024 * 1024);
			dfif.setRepository(new File(this.serverPath+uploadtemp));
			
			//创建文件上传组件
			file = new ServletFileUpload(dfif);
			
			//获取文件或非文件对象
			list = (ArrayList<FileItem>)file.parseRequest(request);	

			//根据上传文件的数量初始化变量
			filename = new String[list.size()];
			filepostfix = new String[list.size()];
			fileInputStream = new InputStream[list.size()];
			int num = 0;
			
			//循环获取提交的对象（包括文件和非文件）
			for (FileItem fi : list) {
				//判读是否为普通字段
				if (fi.isFormField()) {
					if(ht.containsKey(fi.getFieldName())){
						//checkbox的情况，会存在多个相同名称的字段
						ht.put(fi.getFieldName(), ht.get(fi.getFieldName())+" "+fi.getString("utf-8"));
					}else{
						ht.put(fi.getFieldName(), fi.getString("utf-8"));
					}
				}else {
					//累加文件的大小
					allfilesize = allfilesize + fi.getSize();
					
					try{
						//获取文件二进制流
						fileInputStream[num] = fi.getInputStream();
						
						//存储客户端提交时的文件原名字
						ht.put(fi.getFieldName(), fi.getName());
						
						//获取提交文件的名称和后缀
						String filepathtemp = fi.getName();
						String filenametemp = filepathtemp.substring(
								filepathtemp.lastIndexOf("\\") + 1,
								filepathtemp.lastIndexOf("."));
						String filepostfixtemp = filepathtemp
								.substring(filepathtemp.lastIndexOf(".") + 1);
						if (filenametemp.contains("%00")) {
							safeWarn = false;
						}
						filename[num] = filenametemp;
						filepostfix[num] = filepostfixtemp;
						//安全检测，验证文件是否在白名单
						if (!fileWhiteList.toLowerCase().contains(filepostfixtemp.toLowerCase())) {
							safeWarn = false;
						}
/*						else {
							//校验文件后缀是否是真实文件类型
							if (!compareFileType(filepostfixtemp, fileInputStream[num])) {
								safeWarn = false;
							}
						}*/
					}catch(Exception e){
						filename[num] = "";
						filepostfix[num] = "";
					}finally{
						num++;
					}
				}
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(),e);
			this.uploadHint = "文件上传错误！";
		}
	}

    /**
     * 
     * @Description: 通过文件流获取头信息
     * @author: gaolu
     * @date: 2017年8月18日 下午2:51:54  
     * @param: @param inputStream
     * @param: @return      
     * @return: String
     */
    private String getFileHeader(InputStream  inputStream) {
    	String value = null;
    	if (inputStream!=null) {
    		try {
    			byte[] b = new byte[3];
    			inputStream.read(b, 0, b.length);
    			value = bytesToHexString(b);
    		} catch (Exception e) {
    		} finally {
    			if(null != inputStream) {
    				try {
    					inputStream.close();
    				} catch (IOException e) {}
    			}
    		}
		}
        return value;
    }
    /**
     * 
     * @Description: byte转String
     * @author: gaolu
     * @date: 2017年8月18日 下午2:51:06  
     * @param: @param src
     * @param: @return      
     * @return: String
     */
    private String bytesToHexString(byte[] src){
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        //System.out.println("#####真实文件类型的头信息######"+builder.toString()+"##############");
        return builder.toString();
    }
    
    /**
     * 
     * @Description: 文件后缀和头信息对比，是否匹配
     * @author: gaolu
     * @date: 2017年8月18日 下午4:35:44  
     * @param: @param filePostfix
     * @param: @param inputStream
     * @param: @return      
     * @return: boolean
     */
    private boolean compareFileType(String filePostfix, InputStream inputStream){
    	//默认不通过
    	boolean flag = false;
    	if (StringUtils.isNotEmpty(filePostfix)) {
    		String fileHead = fileType.get(filePostfix.toLowerCase());
    		//System.out.println("#####后缀名对应的头信息######"+fileHead+"##############");
    		if (fileHead.equals(getFileHeader(inputStream))) {
    			flag = true;
			}
		}
    	return flag;
    }
	/**
	 * 
	 * @Description: 设置允许单文件上传最大值
	 * @author: gaolu
	 * @date: 2017年8月15日 下午3:42:43  
	 * @param: @param size      
	 * @return: void
	 */
	public void setFileSizeMax(long size) {
		allowfilesize = size;
	}
	
	/**
	 * 
	 * @Description: 设置允许的文件类型
	 * @author: gaolu
	 * @date: 2017年8月15日 下午1:39:07  
	 * @param: String类型参数，输入为文件类型列表，多个用逗号隔开      
	 * @return: void      
	 */
	public void setAllowFile(String allowfile) {
		this.allowfile = allowfile;
	}

	/**
	 * 
	 * @Description: 判断文件类型是否许可
	 * @author: gaolu
	 * @date: 2017年8月15日 下午1:45:44  
	 * @param: @param filepostfix      
	 * @return: void      
	 */
	private void judgeAllowFile(String filepostfix) {

		String[] allow = allowfile.split(",");
		int a = 0;
		for (String allowtemp : allow) {
			//如果包含后缀结束循环
			if (allowtemp.toLowerCase().equals(filepostfix.toLowerCase())) {
				break;
			}
			else {
				a++;
			}
		}
		//不存在允许的上传文件类型
		if (allow.length == a) {
			allowupload = false;
			this.uploadHint = "不支持此上传文件类型！只允许上传"+this.allowfile+"，请重新上传";
		}
	}

	/**
	 * 
	 * @Description: 设置不允许的文件类型
	 * @author: gaolu
	 * @date: 2017年8月15日 下午1:48:41  
	 * @param: @param noallowfile      
	 * @return: void      
	 */
	public void setNoAllowFile(String noallowfile) {
		this.noallowfile = noallowfile;
	}

	/**
	 * 
	 * @Description: 判断文件类型是否不许可
	 * @author: gaolu
	 * @date: 2017年8月15日 下午1:49:41  
	 * @param: @param noallowfile      
	 * @return: void      
	 */
	private void judgeNoAllowFile(String filepostfix) {
		String[] noallow = noallowfile.split(",");
		int a = 0;
		for (String noallowtemp : noallow) {
			//出现不允许上传的文件类型，结束循环
			if (noallowtemp.toLowerCase().equals(filepostfix.toLowerCase())) {
				allowupload = false;
				this.uploadHint = "不支持此上传文件类型！不允许上传"+filepostfix;
				break;
			}
		}
	}

	/**
	 * 此方法用于调用者重写，调用者提供一个获取上传文件名的策略，文件名称不能包含后缀
	 * @param null
	 * @return String类型，返回一个上传文件名
	 */
	public String offerUploadFileNames(){
		return null;
	}
	/**
	 * 返回请求参数(包括文件和form字段)
	 * @param String类型参数，输入为要查找的key
	 * @return String类型，返回value值
	 */
	public String getParameter(String key) {

		return (String) ht.get(key);
	}
	
	/**
	 * 设置上传所有文件的根目录
	 * @param String类型参数，文件根目录
	 * 		      路径可传相对路径或者绝对路径（路径结尾必须加"/"或"\"）：1相对路径相对于应用的根目录；2绝对路径由用户提供,windows以盘符:\开始，linux以/开始
	 * @return void
	 */
	public void setUploadRootList(String rootList){
		this.uploadRootList = rootList;
	}
	
	/**
	 * 
	 * @Description: 文件上传
	 * @author: gaolu
	 * @date: 2017年8月15日 下午1:43:46  
	 * @param:       
	 * @return: void      
	 * @throws
	 */
	public void upload() {
		//安全验证
		if (safeWarn) {
			try {
				
				int pathnum = 0;
				
				//上传文件验证
				for(FileItem f:list) {
					if (!f.isFormField()) {
						if(!f.getName().equals("")){
							allowupload = true;
							
							//验证所有文件大小
							if (allowallsize != 0) {
								if (allfilesize > allowallsize) {
									allowupload = false;
									this.uploadHint = "所有文件总大小超过上线！不能超过"+(allowallsize/1024/1024)+"M";
								}
							}
							
							//验证单个文件大小
							if (allowfilesize != 0) {
								if (f.getSize() > allowfilesize) {
									allowupload = false;
									this.uploadHint = "单个文件大小超过上线！不能超过"+(allowfilesize/1024/1024)+"M";
								}
							}
							
							//验证允许文件类型
							if (!allowfile.equals("")) {
								judgeAllowFile(filepostfix[pathnum]);
							}
							
							//验证不允许文件类型
							if (!noallowfile.equals("")) {
								judgeNoAllowFile(filepostfix[pathnum]);
							}
							
							//一个文件不通过验证，则不允许所有文件上传
							if(!allowupload){
								break;
							}
						}
						pathnum++;
					}
				}
				
				//验证完成，将数量置为0，准备上传
				pathnum = 0;
				
				for(FileItem f:list) {
					if (!f.isFormField()) {
						if(!f.getName().equals("")){
							//如果验证不通过不上传文件
							if (allowupload) {
								
								//根据跟目录和文件名的生成规则上传文件
								if(this.uploadRootList!=null&&!this.uploadRootList.equals("")){
									//返回要保存文件名称
									String filename = this.offerUploadFileNames();
									if(filename!=null&&!filename.equals("")){
										String webpath = "";
										//判断上传根路径给出的是绝对路径还是相对路径
										if (!this.uploadRootList.contains(":")&&!this.uploadRootList.substring(0, 1).equals("/")) {
											webpath = this.serverPath;
										}
										//创建目录
										String listName = webpath + this.uploadRootList;
										file list = new file(listName);
										list.createlist();
										
										if (StringUtils.isNotEmpty(getParameter("path"))) {
											f.write(new File(listName + this.filename[pathnum] + "."+ filepostfix[pathnum]));
										}else{
											f.write(new File(listName + filename + "."+ filepostfix[pathnum])); //+".dat"
										}
										this.uploadedFileNameList.add(filename + "."+ filepostfix[pathnum]);
									}
								}else if(!defaultfilename){
									//根据传入的目录列表和文件名列表上传文件
									String webpath = "";
									if (!uploadpath[pathnum].contains(":")&&!uploadpath[pathnum].substring(0, 1).equals("/")) {
										webpath = this.serverPath;
									}
									//创建目录
									String listName = webpath + uploadpath[pathnum];
									file list = null;
									//格式化路径
									if("windows".equals(BasicFile.WINDOWS_SYSTEM)){
										list = new file(listName.substring(0, listName.lastIndexOf("\\")+1));
									}else if("linux".equals(BasicFile.LINUX_SYSTEM)){
										list = new file(listName.substring(0, listName.lastIndexOf("/")+1));
									}
									list.createlist();
									
									f.write(new File(webpath + uploadpath[pathnum] + "." + filepostfix[pathnum]));
									this.uploadedFileNameList.add(uploadpath[pathnum] + "." + filepostfix[pathnum]);
								}else if(defaultfilename){
									//根据传入的目录列表和文件本来的文件名上传文件（很少使用）
									String webpath = "";
									if (!uploadpath[pathnum].contains(":")&&!uploadpath[pathnum].substring(0, 1).equals("/")) {
										webpath = this.serverPath;
									}
									
									String defaultfilenametemp = filename[pathnum];
									//创建目录
									String listName = webpath + uploadpath[pathnum];
									file list = new file(listName);
									list.createlist();
									
									f.write(new File(webpath + uploadpath[pathnum] + defaultfilenametemp + "." + filepostfix[pathnum]));
									this.uploadedFileNameList.add(uploadpath[pathnum] + defaultfilenametemp + "." + filepostfix[pathnum]);
								}
							}
						}
						pathnum++;
					}
				}
			}catch (Exception e) {
				log.error(e.getMessage(),e);
				this.uploadHint = "文件上传错误！";
			}
		}else{
			this.uploadHint = "上传失败，文件存在安全隐患";
		}
		//删除临时文件
		file f = new file(this.serverPath+uploadtemp);
		f.deleteDir(new File(this.serverPath+uploadtemp));
		
	}
	
	/**
	 * 
	 * @Description: 返回文件对象的List
	 * @author: gaolu
	 * @date: 2017年8月15日 下午1:45:29  
	 * @param: @return      
	 * @return: ArrayList<FileItem>      
	 * @throws
	 */
	public ArrayList<FileItem> getFileItemList() { 
		ArrayList<FileItem> fileItemList = new ArrayList<FileItem>();
		for(FileItem fileItem:list){
			if(!fileItem.isFormField()){
				fileItemList.add(fileItem);
			}
		}
		return fileItemList;
	}

	/**
	 * 
	 * @Description: set,get方法
	 * @author: gaolu
	 * @date: 2017年8月15日 下午1:47:05  
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	public String getUploadRootList() {
		return uploadRootList;
	}

	public List<String> getUploadedFileNameList() {
		return uploadedFileNameList;
	}

	public String getUploadHint() {
		return uploadHint;
	}

	public InputStream[] getFileInputStream() {
		return fileInputStream;
	}


}
