
package com.tedu.base.file.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.excel.ExcelOutputData;
import com.tedu.base.common.excel.ExcelUtil;
import com.tedu.base.common.model.DataGrid;
import com.tedu.base.common.redis.KeyNotFindException;
import com.tedu.base.common.redis.RedisService;
import com.tedu.base.common.utils.FileUtil;
import com.tedu.base.common.utils.ImageUtil;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.common.utils.ResponseJSON;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.model.ServerTokenData;
import com.tedu.base.engine.service.FormTokenService;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.engine.util.FormUtil;
import com.tedu.base.file.model.FileModel;
import com.tedu.base.file.service.DocumentService;
import com.tedu.base.file.service.FileService;
import com.tedu.base.file.service.MarkdownAsPDFService;
import com.tedu.base.file.util.HttpClientUtil;
import com.tedu.base.file.util.UploadFile;
import com.tedu.base.file.util.io.FilePathUtil;
import com.tedu.base.file.util.operation.LocalOperation;
import com.tedu.base.initial.model.xml.ui.FormConstants; 

/**
 * 文件操作接口，包含上传和下载
 * @author gaolu
 *
 */
@Controller
public class FileController {
	@Resource
	private FileService fileService ;
	@Value("${file.upload.path}")
	private String DISH;
    @Resource
    private FormTokenService formTokenService;
    @Resource
	private DocumentService documentService;
    @Autowired(required=false)
	private RedisService redisService;
    @Resource
    private MarkdownAsPDFService markdownAsPDFService;
	/** 
	 * 全局变量
	 */
	private final String PRIVATE = "private";
	private final String PUBLIC = "public";
	public final Logger log = Logger.getLogger(this.getClass());		// 日志记录器
	ResponseJSON responseJSON = null;	//返回json数据
	String hintMessage = "";	//提示消息
	@RequestMapping("play")
	public String play(String url, Model model){
		
		model.addAttribute("url",url);
		model.addAttribute("type",url.indexOf(".mp4")>-1?"mp4":"pdf");
		return FormConstants.VIEW_PLAY;
	}
	/**
	 *
	 * @Description: 本地上传
	 * @author: gaolu
	 * @date: 2017年8月16日 下午3:02:39
	 * @param: @param request
	 * @param: @return
	 * @return: ResponseJSON
	 * @throws IOException 
	 */
	@RequestMapping("/localUpload")
	public void localUpload(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Map<String, Object> map = new HashMap<>();
		String fileUUID = UUID.randomUUID().toString().replaceAll("-", "");
		Map<String, Object> fileData = new HashMap<>();
		//上传结果信息
		responseJSON = new ResponseJSON();
		//创建文件上传对象
		UploadFile upfiles = new UploadFile(request,""){
			//生成文件新名字
			@Override
			public String offerUploadFileNames() {
				return fileUUID;
			}
		};
		String uploadUrl = upfiles.getParameter("uploadUrl");
		String accessType = upfiles.getParameter("accessType");
		String module = upfiles.getParameter("module");
		//只能上传单文件判断
		map = fileService.localUpload(upfiles,fileUUID,accessType,uploadUrl,module);
		hintMessage = (String) map.get("hintMessage");
		if (StringUtils.isEmpty(hintMessage)) {
			//获取上传后文件信息对象
			FileModel fileModel = (FileModel)map.get("fileModel");
			if(!StringUtils.isEmpty(request.getParameter("x"))&&
					!StringUtils.isEmpty(request.getParameter("y"))&&
					!StringUtils.isEmpty(request.getParameter("h"))&&
					!StringUtils.isEmpty(request.getParameter("w"))){
				int x = Double.valueOf(request.getParameter("x")).intValue();
				int y = Double.valueOf(request.getParameter("y")).intValue();
				int h = Double.valueOf(request.getParameter("h")).intValue();
				int w = Double.valueOf(request.getParameter("w")).intValue();
				ImageUtil.cutImage(fileModel.getPath(),fileModel.getFileUUID(),fileModel.getFileType(), new java.awt.Rectangle(x,y,w,h));
			}
			//上传成功则返回id
			hintMessage = "上传成功";
			responseJSON.setStatus(fileModel.getId()==null?0:fileModel.getId());
			fileData.put("fileId", fileModel.getId()==null?0:fileModel.getId());
			fileData.put("massage", "上传成功");
			fileData.put("url", fileModel.getUrl());
			fileData.put("filename", fileModel.getTitle()+"."+fileModel.getFileType());
		}
		
//		request.getHeader("Accept");
		FormLogger.logFlow("header.Accept =" + request.getHeader("Accept"), FormLogger.LOG_TYPE_INFO);
		responseJSON.setMsg(hintMessage);
		responseJSON.setData(fileData);
		
		response.setContentType("text/html;charset=utf-8");  
		response.getWriter().write(FormUtil.toJsonString(responseJSON));
	}

	/**
	 *
	 * @Description: markdown上传
	 * @author: gaolu
	 * @date: 2017年8月16日 下午3:02:39
	 * @param: @param request
	 * @param: @return
	 * @return: ResponseJSON
	 */
	@RequestMapping("/mdupload")
	@ResponseBody
	public Map<String, Object> mdupload(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map = new HashMap<>();
		String fileUUID = UUID.randomUUID().toString().replaceAll("-", "");
		String accessType = "markdown";
		//上传结果信息
		responseJSON = new ResponseJSON();
		//创建文件上传对象
		UploadFile upfiles = new UploadFile(request,""){
			//生成文件新名字
			@Override
			public String offerUploadFileNames() {
				return fileUUID;
			}
		};

		//只能上传单文件判断
		map = fileService.localUpload(upfiles,fileUUID,accessType,"","");
		hintMessage = (String) map.get("hintMessage");
		if (StringUtils.isEmpty(hintMessage)) {
			String projectUr = request.getRequestURL().toString();
			//获取上传后文件信息对象
			FileModel fileModel = (FileModel)map.get("fileModel");
			//1表示上传成功
			map.put("success", 1);
			map.put("message", projectUr.replace("/mdupload","")+"/localDownload?methodType=markdown&fileId="+fileModel.getId());
			map.put("url", projectUr.replace("/mdupload","")+"/localDownload?methodType=markdown&fileId="+fileModel.getId());
		}else{
			//上传失败
			map.put("success", 0);
		}

		return map;
	}

	/**
	 *
	 * @Description: 云端上传
	 * @author: gaolu
	 * @date: 2017年8月16日 下午3:02:39
	 * @param: @param request
	 * @param: @return
	 * @return: ResponseJSON
	 */
	public ResponseJSON OSSUpload(HttpServletRequest request){
		//上传结果信息
		String hintMessage = "";
		responseJSON = new ResponseJSON();

		responseJSON.setStatus(1);
		responseJSON.setMsg(hintMessage);
		return responseJSON;
	}

	/**
	 *
	 * @Description: 本地下载
	 * @author: gaolu
	 * @date: 2017年8月16日 下午3:02:39
	 * @param: @param request
	 * @param: @return
	 * @return: ResponseJSON
	 */
	@RequestMapping("/localDownload")
	public void localDownload(HttpServletRequest request,HttpServletResponse response){
		//获取文件id
		String uniqueCode = request.getParameter("fileId");
		String preview = request.getParameter("preview");
		LocalOperation operationFile = new LocalOperation(response);
		
		if (StringUtils.isNotEmpty(uniqueCode)) {
		    Pattern pattern = Pattern.compile("[0-9]*"); 
		    Matcher isNum = pattern.matcher(uniqueCode);
		    //如果是数字则查询文件信息
		    if (isNum.matches()) {
		    	Integer fileId = Integer.parseInt(uniqueCode);
				FileModel file = fileService.getFileById(fileId);
				if (file != null) {
					//文件路径拼接
					String path = "";
					path = file.getPath()+file.getFileUUID()+"."+file.getFileType();  //+".dat"
					
					//如果是公有文件可以直接下载，私有文件验证session
					if(PUBLIC.equals(file.getAccessType())){
						if (StringUtils.isNotEmpty(preview)) {
							operationFile.fileDownload(path,true,file.getTitle()+"."+file.getFileType());
						}else{
							operationFile.fileDownload(path,false,file.getTitle()+"."+file.getFileType());
						}
					}else if(SessionUtils.getSession().getId().toString() != null){
						if (StringUtils.isNotEmpty(preview)) {
							operationFile.fileDownload(path,true,file.getTitle()+"."+file.getFileType());
						}else{
							operationFile.fileDownload(path,false,file.getTitle()+"."+file.getFileType());
						}
					}
				}
			}else{
				//如果不是数字则是路径
				if (SessionUtils.getSession().getId().toString() != null && StringUtils.isNotEmpty(preview)) {
					operationFile.fileDownload(uniqueCode,true,"");
				}else if(SessionUtils.getSession().getId().toString() != null){
					operationFile.fileDownload(uniqueCode,false,"");
				}
			}
		}
	}
	
	@RequestMapping("resetFile")
	@ResponseBody
	public FormEngineResponse resetFile(@RequestBody FormEngineRequest requestObj,HttpServletRequest request) throws Exception {
		FormEngineResponse response = new FormEngineResponse("");
		String exerciseFolder = ObjectUtils.toString(requestObj.getData().get("exerciseFolder"));
		String noForStudent = ObjectUtils.toString(requestObj.getData().get("noForStudent"));
		String scheduId = ObjectUtils.toString(requestObj.getData().get("scheduId"));
		String mobile = ObjectUtils.toString(requestObj.getData().get("mobile"));
		if(exerciseFolder.indexOf("课堂练习")==-1&&exerciseFolder.indexOf("课后作业")==-1){
			response.setCode("1");
			response.setMsg("课堂练习或者课后作业才能一键复原");
			return response;
		}
		if(exerciseFolder.indexOf("SCH")>-1){
			if(!(new File(exerciseFolder.replace("workspace/", "").replace("SCH"+scheduId, noForStudent))).exists()){
				response.setCode("1");
				response.setMsg("教研没有上传课程资料！");
				return response;
			}
			File dest = new File(exerciseFolder);
			FileUtil.deleteFile(dest);
			FileUtil.copyDir(exerciseFolder.replace("workspace/", "").replace("SCH"+scheduId, noForStudent), exerciseFolder);
		} else {
			File dest = new File(exerciseFolder);
			FileUtil.deleteFile(dest);
			if(exerciseFolder.indexOf("课后作业")>-1){
				if(!(new File(exerciseFolder.replace("课后作业/", "").replace("workspace/", "").replace("S"+mobile+"/", "")+"课后作业/")).exists()){
//					response.setCode("1");
					response.setMsg("教研没有上传课程资料！");
					return response;
				}
				FileUtil.copyDir(exerciseFolder.replace("课后作业/", "").replace("workspace/", "").replace("S"+mobile+"/", "")+"课后作业/", exerciseFolder);
			} else {
				if(!(new File(exerciseFolder.replace("workspace/", "").replace(noForStudent+"/", "").replace("S"+mobile, noForStudent))).exists()){
//					response.setCode("1");
					response.setMsg("教研没有上传课程资料！");
					return response;
				}
				FileUtil.copyDir(exerciseFolder.replace("workspace/", "").replace(noForStudent+"/", "").replace("S"+mobile, noForStudent), exerciseFolder);
			}
		}
		return response;
		
	}
/**
 *
 * @Description: 预览文件
 * @author: gaolu
 * @date: 2017年8月16日 下午3:02:39
 * @param: @param request
 * @param: @return
 * @return: ResponseJSON
 */
	
@RequestMapping("viewfile")
@ResponseBody
public FormEngineResponse viewFile(@RequestBody FormEngineRequest requestObj,HttpServletRequest request) {
    FormEngineResponse response = new FormEngineResponse("");
	String fileContent = "";
	String filePath = "";
	//获取文件id
	String uniqueCode = ObjectUtils.toString(requestObj.getData().get("uniqueCode"));
	if (StringUtils.isNotEmpty(uniqueCode)) {
		try {
		   Pattern pattern = Pattern.compile("[0-9]*"); 
		   Matcher isNum = pattern.matcher(uniqueCode);
		   if (isNum.matches()) {
			   Integer fileId = Integer.parseInt(uniqueCode);
			   FileModel fileModel = fileService.getFileById(fileId);
				if (fileModel != null) {
					filePath = fileModel.getPath()+fileModel.getFileUUID()+"."+fileModel.getFileType();  //+".dat"
				}
		   }else{
			   filePath = uniqueCode;
		   }
		   String lowerFilePath = filePath.toLowerCase();
		   if(lowerFilePath.endsWith("png") || lowerFilePath.endsWith("jpg") || 
				   lowerFilePath.endsWith("jpeg") || lowerFilePath.endsWith("gif")){
			   fileContent = Base64.encodeBase64String(FileUtils.readFileToByteArray(new File(filePath)));//返回Base64编码过的字节数组字符串
		   }else{
			   fileContent = FileUtils.readFileToString(new File(filePath),"UTF-8");
		   }
		} catch (IOException e) {
			FormLogger.error("预览文件接口",String.format("读取文件失败，文件不存在或操作异常,%s",e.getMessage()), FormLogger.LOG_TYPE_ERROR);
		}
	}
	response.setData(fileContent);
	return response;
}

/**
*
* @Description: 打开文件
* @author: gaolu
* @date: 2017年8月16日 下午3:02:39
* @param: @param request
* @param: @return
* @return: ResponseJSON
*/
@RequestMapping("openfile")
public void openFile(HttpServletRequest request,HttpServletResponse response){
	String fileContent = "";
	String filePath = "";
	//获取文件id
	String uniqueCode = request.getParameter("uniqueCode");
	String token = request.getParameter("token");
	if (StringUtils.isNotEmpty(uniqueCode)) {
	try {
		   Pattern pattern = Pattern.compile("[0-9]*"); 
		   Matcher isNum = pattern.matcher(uniqueCode);
		   if (isNum.matches()) {
			   Integer fileId = Integer.parseInt(uniqueCode);
			   FileModel fileModel = fileService.getFileById(fileId);
				if (fileModel != null) {
					filePath = fileModel.getPath()+fileModel.getFileUUID()+"."+fileModel.getFileType(); //+".dat"
				}
		   }else{
			   filePath = uniqueCode;
		   }
			
		   FileUtils file = new FileUtils();
		   fileContent = file.readFileToString(new File(filePath),"UTF-8");
	} catch (IOException e) {
			FormLogger.error("打开文件接口",String.format("读取文件失败，文件不存在或操作异常"), FormLogger.LOG_TYPE_ERROR);
			throw new ServiceException(ErrorCode.FILE_EXCEPTION, "文件操作异常");
		}
	}
	try {
		response.getWriter().write(fileContent);
	} catch (IOException e) {
		FormLogger.error("打开文件接口",String.format("写入失败，文件不存在或操作异常"), FormLogger.LOG_TYPE_ERROR);
		throw new ServiceException(ErrorCode.FILE_EXCEPTION, "文件操作异常");
	}
}

/**
*
* @Description: 运行文件
* @author: gaolu
* @date: 2017年8月16日 下午3:02:39
* @param: @param request
* @param: @return
* @return: ResponseJSON
*/
@Value("${Rdurl}")
private String Rdurl;
@RequestMapping("runCode")
@ResponseBody
public FormEngineResponse runCode(@RequestBody FormEngineRequest requestObj, HttpServletRequest request,Model model){
    FormEngineResponse response = new FormEngineResponse("");
	String token = UUID.randomUUID().toString().replaceAll("-", "");
	String rootCatalog = ObjectUtils.toString(requestObj.getData().get("rootCatalog"));
	String path = ObjectUtils.toString(requestObj.getData().get("uniqueCode"));
	if (StringUtils.isNotEmpty(path)) {
		if(StringUtils.isEmpty(rootCatalog)){
			//不传rootCatalog时,以运行文件所属目录作为根.用于解决课程资料下常有多个项目目录无法运行.
			File f = new File(path);
			rootCatalog = f.getParent();
		}
		redisService.set(token, path+","+rootCatalog, 30*30l);
		response.setCode("0");
		response.setData(Rdurl+"run.do?token="+token);
	}else{
		FormLogger.logFlow(String.format("runCode: do nothing with redis, path=%s ,rootCatalog=%s",path,rootCatalog), FormLogger.LOG_TYPE_INFO);
		response.setCode("1");
	}
    return response;
}
@RequestMapping("runCodeLua")
public String runCodeLua(HttpServletRequest request,Model model,HttpServletResponse response, RedirectAttributes redirectAttributes) throws UnsupportedEncodingException{
	String path = java.net.URLEncoder.encode(request.getParameter("uniqueCode").replace("/usr/local/etc/", ""),"UTF-8");
    long milliseconds = System.currentTimeMillis();
    String salt = "aicoders";
    String beforeEncryptionString = request.getParameter("uniqueCode").replace("/usr/local/etc", "") + salt + milliseconds;
    LogUtil.info("beforeEncryptionString="+beforeEncryptionString);
    // 这里使用 Spring 提供的 md5 加密工具进行 md5 加密
    String token = DigestUtils.md5DigestAsHex(beforeEncryptionString.getBytes());
    String url = path.replace("%2F", "/") + "?ts=" + milliseconds + "&token=" + token;
	LogUtil.info("redirect:"+Rdurl+url);
	return "redirect:"+Rdurl+url;
}

@Value("${RdurlCpp:''}")
private String RdurlCpp;
@RequestMapping("runCOrCppCode")
@ResponseBody
public FormEngineResponse runCOrCppCode(@RequestBody FormEngineRequest requestObj, HttpServletRequest request, Model model, HttpServletResponse response) throws IOException{
	
	String token = UUID.randomUUID().toString().replaceAll("-", "");
	String uniqueCode = (String)requestObj.getData().get("uniqueCode");
	String parameter = (String)requestObj.getData().get("parameter");
	
	String rootCatalog = (String)requestObj.getData().get("rootCatalog");
	RdurlCpp = RdurlCpp.substring(0,RdurlCpp.lastIndexOf("/")+1);
	String result = "";
	if (StringUtils.isNotEmpty(uniqueCode)) {
		HttpClientUtil client = new HttpClientUtil();
		Map<String, String> map = new HashMap<>();
		map.put("token", token);
//		map.put("value", "/usr/local/etc/course/少儿编程体验课02/课堂练习/test3.cpp");
		map.put("value", uniqueCode);
		map.put("parameter", parameter);
		try {
			result = client.doPost(RdurlCpp+"runCOrCpp.do", map);
		} catch (Exception e) {
			e.printStackTrace();
			FormLogger.logFlow(String.format("runCOrCppCode: connect HtmlContainer exception", uniqueCode+":"+parameter, rootCatalog), FormLogger.LOG_TYPE_ERROR);
		}
		System.out.println(result);
	}else{
		FormLogger.logFlow(String.format("runCOrCppCode: get uniqueCode fail, uniqueCode=%s ,parameter=%s", uniqueCode+":"+parameter, rootCatalog), FormLogger.LOG_TYPE_INFO);
		result = "runCOrCppCode: get uniqueCode fail";
	}
	return new FormEngineResponse(result);
}

/**
 *
 * @Description: 模板下载
 * @author: gaolu
 * @param: @param request
 * @param: @return
 * @return: ResponseJSON
 */
@RequestMapping(value="/downLoadTemp")
public String downLoadTemp(ExcelOutputData map, String titles, HttpServletRequest request,HttpServletResponse response){
	if(titles!=null){
		String[][] title = new String[titles.split(",").length-1][1];
		for(int i=0;i<titles.split(",").length-1;i++){
			title[i][0]=titles.split(",")[i];
		}
		map.setFileName(StringUtils.isEmpty(request.getParameter("title"))?"批量导入.xls":request.getParameter("title")+".xls");
		map.setExcelTitle(title);
	}
	return "excelView";
}
/**
 *
 * @Description: 本地上传
 * @author: gaolu
 * @date: 2017年8月16日 下午3:02:39
 * @param: @param request
 * @param: @return
 * @return: ResponseJSON
 */
@RequestMapping("/uploadExcelBat")
@ResponseBody
public FormEngineResponse uploadExcelBat(HttpServletRequest request, String token) throws IOException{
	UploadFile upFile = new UploadFile(request, "");
	  if (upFile!=null) {
		  String fileType = upFile.filepostfix[0];
	   if(fileType.length() > 0 ){
	    //得到上传文件的流
	    InputStream is = upFile.getFileInputStream()[0];
	    ServerTokenData serverTokenData  = formTokenService.geToken(token);

	    List<Map<String,Object>> list;
	    List<String[]> lstLine = new ArrayList<>();
	    if("csv".equalsIgnoreCase(fileType)){
	    	lstLine = ExcelUtil.csvToList(serverTokenData,is);
	    }else if("xls".equalsIgnoreCase(fileType) || "xlsx".equalsIgnoreCase(fileType)){
	    	lstLine = ExcelUtil.excelToList(serverTokenData,is);
	    }
    	list = ExcelUtil.genTableData(serverTokenData,lstLine);
	    DataGrid dgData = new DataGrid(list);
	    return new FormEngineResponse(dgData);
	   }
	  }
	  return null;
}



/**
 *
 * @Description: 二进制流形式传递内容 后台生成.md文件并上传至服务器
 * @author: hejk
 * @date: 2017年11月22日 下午3:02:39
 * @param: @param request
 * @param: @return
 * @return: ResponseJSON
 * @throws IOException 
 * @throws FileNotFoundException 
 * @throws Exception
 */
@RequestMapping("/createMdBat")
@ResponseBody
public int createMdBat(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, IOException {
		String fileId = request.getParameter("fileId"); // 文件ID
		String objStr = request.getParameter("fileName"); // 文档名称

		// server's path
		FilePathUtil filepath = new FilePathUtil(new Date(), DISH);

		String accessType = "markdown";
		String fileUUID = UUID.randomUUID().toString().replaceAll("-", "");
		String path = filepath.getMdPath("md", "", "","");
		// insert
		if (fileId == null || fileId == "") {
			
			String fileSave = path + fileUUID + ".md";  //.dat
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}

			// io write
			FileIOWrite(request, fileSave);
			FileModel fm = null;
			// get file's size
			file = new File(fileSave);
			fm = fileService.localUpload(objStr, file.length() + "", fileUUID, accessType, "",fm,path);
			if (fm != null) {
				// successed return file's ID
				return fm.getId();
			} else {
				return 0;
			}

		} else {
			// update
			/**
			 * 调用高璐的备份文件方法
			 */
			
			// 将文件内容更新到文件内
			FileModel fm = fileService.getFileById(Integer.parseInt(fileId));
			int fmId = fm.getId();
			path=fm.getPath();
			String filePath = filepath.getDownPath(fm, "markdown",""); // 当前md文档存放路径

			File file = new File(filePath);
			// io wirte
			FileIOWrite(request, filePath);

			file = new File(filePath);

			fm = fileService.localUpload(objStr, file.length() + "", fm.getFileUUID(), accessType, "edit",fm,path);
			return fmId;
		}

	}

/**
 * IO write
 * @param request
 * @param bytes
 * @param fileSave
 * @throws FileNotFoundException
 * @throws IOException
 */
private void FileIOWrite(HttpServletRequest request, String fileSave)
			throws IOException {
		int index;
		byte[] bytes = new byte[1024];
		try(FileOutputStream downloadFile = new FileOutputStream(fileSave)){
			while ((index = request.getInputStream().read(bytes)) != -1) {
				downloadFile.write(bytes, 0, index);
				downloadFile.flush();
			}
		}catch(Exception e){
			FormLogger.error(e.getMessage(),e);
		}finally{
			request.getInputStream().close();
		}
	}


/**
 *
 * @Description: 将file信息存入知识库目录表、文档信息表
 * @author: hejk
 * @date: 2017年11月29日 下午13:31:39
 * @param: @param request
 * @param: @return
 * @return: ResponseJSON
 * @throws Exception
 */
@RequestMapping("/saveDocCata")
@ResponseBody
public int saveDocCata(HttpServletRequest request, HttpServletResponse response) {
		int res = 0;
		String file_id =request.getParameter("file_id"); //t_file_index.id
		String cata_id =request.getParameter("cata_id"); //t_doc_cata.id
		String file_name =request.getParameter("file_name"); //t_file_index.name
		try {
			documentService.handleDocument(file_id, cata_id, file_name);
			res = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
/**
 * 将markdown文档另存为pdf
 * @author:	zhangzhiming
 * @date: 2018年6月27日 下午13:07:39
 * @param request
 * @param response
 * @return
 * @throws IOException 
 */
@RequestMapping("/saveAsPDF")
@ResponseBody
public int saveAsPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String fileId =request.getParameter("file_id"); 
		String fileName =request.getParameter("file_name"); 
		String fileUUID = UUID.randomUUID().toString().replaceAll("-", "");
		FilePathUtil filepath = new FilePathUtil(new Date(), DISH);
		if(fileId==null||fileId==""){
			return -1;
		}else{
			//获取markdown的路径
			FileModel fm = fileService.getFileById(Integer.parseInt(fileId));
			String filePath = filepath.getDownPath(fm, "markdown",""); // 当前md文档存放路径
			
			//html存放的路径
			String outUrl=filepath.getHtmlPath()+fileName+".html";
			File hfile = new File(filepath.getHtmlPath());
			if (!hfile.exists()) {
				hfile.mkdirs();
			}
			//pdf存放的路径
			String path=filepath.getPdfPath();
			String destPath=path+fileName+".pdf";
			File pfile = new File(path);
			if (!pfile.exists()) {
				pfile.mkdirs();
			}
			markdownAsPDFService.mdToHtml(filePath,outUrl);//markdown转html
			markdownAsPDFService.convert(outUrl, destPath);//html转pdf
			
			String accessType="pdf";
			FileModel fmpdf=null;
			File file = new File(destPath);
			//保存Pdf的信息
			fmpdf = fileService.localUpload(fileName, file.length() + "", fileUUID, accessType, "",fmpdf,path);
			System.out.println("fmpdf--------"+fmpdf.getId());
			return fmpdf.getId();
		}
	}
@Value("${base.url.k12:''}")
private String baseUrlk12;
@Value("${base.url.aicoders:''}")
private String baseUrlaicoders;
@RequestMapping("/saveCourseRemark")
@ResponseBody
public FormEngineResponse saveCourseRemark(@RequestBody FormEngineRequest requestObj) {
	Map<String, String> result = new HashMap<String, String>();
	result.put("0", "0");
	if("aicoders".equals(requestObj.getData().get("belonged").toString())){
		result.put("baseUrl", baseUrlaicoders);
	} else {
		result.put("baseUrl", baseUrlk12);
	}
	FormEngineResponse ret = new FormEngineResponse(result);
		String data = requestObj.getData().get("content").toString().replace("/localDownload", "/course/view");
			try {
		         File dir =new File(DISH+"html");
		         if(!dir.exists()){
		        	 dir.mkdirs();
		         }
				FileUtil.writeTxtFileUtf8(data, new File(DISH+"html/"+requestObj.getData().get("id")+".html"));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		return ret;
	}

/**
*
* 功能就是图片上传，同markdown相同，只是返回的json串指定参数.
* @Description: KindEditor 图片上传 
* @author: hejk
* @date: 2018年1月16日 下午3:02:39
* @param: @param request
* @param: @return
* @return: ResponseJSON
* 
*/
@RequestMapping("/localKEUpload")
@ResponseBody
public Map localKEUpload(HttpServletRequest request,HttpServletResponse response){
	Map<String, Object> map = new HashMap<>();
	String fileUUID = UUID.randomUUID().toString().replaceAll("-", "");
	String accessType = "public";
	//上传结果信息
	responseJSON = new ResponseJSON();
	//创建文件上传对象
	UploadFile upfiles = new UploadFile(request,""){
		//生成文件新名字
		@Override
		public String offerUploadFileNames() {
			return fileUUID;
		}
	};

	//只能上传单文件判断
	map = fileService.localUpload(upfiles,fileUUID,accessType,"","");
	hintMessage = (String) map.get("hintMessage");
	if (StringUtils.isEmpty(hintMessage)) {
		//获取上传后文件信息对象
		FileModel fileModel = (FileModel)map.get("fileModel");
		//上传成功则返回id
		hintMessage = "上传成功";
		responseJSON.setStatus(fileModel.getId()==null?0:fileModel.getId());
		String projectUr = request.getRequestURL().toString();
		map.put("error",0);
		map.put("url",projectUr.replace("/localKEUpload","")+"/localDownload?methodType=markdown&fileId="+fileModel.getId());
	}
	responseJSON.setMsg(hintMessage);
	return map;
}


}
