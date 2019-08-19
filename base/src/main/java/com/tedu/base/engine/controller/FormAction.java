package com.tedu.base.engine.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tedu.base.commitLog.service.CommitLogService;
import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.model.Column;
import com.tedu.base.common.model.ColumnCheckable;
import com.tedu.base.common.model.DataGrid;
import com.tedu.base.common.model.FieldCondition;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.ConstantUtil;
import com.tedu.base.common.utils.ContextUtils;
import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.common.utils.MD5Util;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.aspect.FindLogic;
import com.tedu.base.engine.model.ChangeLog;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.model.ServerTokenData;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.engine.service.FormTokenService;
import com.tedu.base.engine.util.FormConfiguration;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.engine.util.FormUtil;
import com.tedu.base.engine.util.PanelValidator;
import com.tedu.base.file.model.FileModel;
import com.tedu.base.file.service.FileService;
import com.tedu.base.file.service.impl.FileServiceImpl.mediaTypeEnum;
import com.tedu.base.file.util.ExportFormatUtil;
import com.tedu.base.file.util.PDFUtil;
import com.tedu.base.file.util.io.FilePathUtil;
import com.tedu.base.file.util.io.file;
import com.tedu.base.file.util.operation.ExportOperation;
import com.tedu.base.file.util.operation.WhiteListOperation;
import com.tedu.base.initial.model.xml.ui.Control;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.initial.model.xml.ui.Panel;
import com.tedu.base.initial.model.xml.ui.Param;
import com.tedu.base.initial.model.xml.ui.Procedure;
import com.tedu.base.initial.model.xml.ui.UI;
import com.tedu.base.initial.model.xml.ui.XML;
import com.tedu.base.rule.AviatorUtil;
import com.tedu.base.task.service.JobTaskService;

/**
 * 实现表单引擎的增删改查校验
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/api")
public class FormAction {
	public static final Logger log = Logger.getLogger(FormAction.class);
	PanelValidator panelValidator = new PanelValidator();
	@Resource
	private FormService formService;
	@Resource
	private FormLogService formLogService;
	@Resource
	private FormTokenService formTokenService;
	@Resource
	private FileService fileService;
	@Resource
	private JobTaskService jobTaskService;
	@Resource
	private CommitLogService commitLogService;

	@RequestMapping("save")
	@ResponseBody
	public FormEngineResponse save(@RequestBody FormEngineRequest requestObj,FormModel formModel){
		ServerTokenData serverTokenData  = formModel.getServerTokenData();
		//construct change log
		int n = -1;
		ChangeLog changeLog = new ChangeLog();
		changeLog.setFlowId(serverTokenData.getFlowId());
		changeLog.setEntityName(formModel.getTableName());
		changeLog.setUrl(serverTokenData.getUrl());
		changeLog.setProc(serverTokenData.getProcedure().getName());
		changeLog.setOldData(formLogService.getDataById(formModel));//先查库
		changeLog.setType(formModel.getSaveTypeByEditMode());
		changeLog.setEntityId(formModel.getPrimaryFieldValue());

		n = formService.save(requestObj,formModel);
		if(n<0){
			throw new ServiceException(ErrorCode.SAVE_MASTER_DATA_FAILED,n+"");
		}

		//无论新增还是更新,都使用primaryFieldValue
		HashMap<String, Object> newData = (HashMap<String, Object>)formLogService.getDataById(formModel);
		HashMap<String, Object> newDataClone = newData==null?
				new HashMap<String, Object>():
					(HashMap<String, Object>)newData.clone();
		//这里会shrink newData
		changeLog.setNewData(newData);
		formLogService.logDiffData(changeLog);
		//返回
		FormEngineResponse ret = new FormEngineResponse(newDataClone);
		formModel.prepareDataByPropertyName(newDataClone);
		return ret;
	}

	@RequestMapping("delete")
	@ResponseBody
	public FormEngineResponse deleteById(@RequestBody FormEngineRequest requestObj,FormModel formModel){
		ServerTokenData serverTokenData  = formModel.getServerTokenData();
		Map<String,Object> oldData = formLogService.getDataById(formModel);
		ChangeLog changeLog = new ChangeLog();
		changeLog.setFlowId(serverTokenData.getFlowId());
		changeLog.setEntityName(formModel.getTableName());
		changeLog.setUrl(serverTokenData.getUrl());
		changeLog.setProc(serverTokenData.getProcedure().getName());
		changeLog.setOldData(oldData);//先查库
		changeLog.setType("delete");
		changeLog.setEntityId(formModel.getPrimaryFieldValue());
		formService.delete(formModel,requestObj);
		changeLog.setNewData(null);
		formLogService.logDiffData(changeLog);

		formModel.prepareDataByPropertyName(oldData);
		return new FormEngineResponse(oldData);
	}


	/**
	 * 自定义保存
	 * @param formModel 这里需要根据sql文件中每行语句的属性实现不同的insertSql,updateSql等，因此使用CustomFormModel
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("savecustom")
	@ResponseBody
	public FormEngineResponse saveCustom(@RequestBody FormEngineRequest requestObj,CustomFormModel formModel){
		formService.saveCustom(formModel,requestObj);
		HashMap<String, Object> newData = (HashMap<String, Object>)formLogService.getDataById(formModel);//查
		//返回
		formModel.prepareDataByPropertyName(newData);
		return new FormEngineResponse(newData);
	}
	/**
	 * 立即执行
	 * @throws SchedulerException
	 */
	@RequestMapping("execjob")
	@ResponseBody
	public FormEngineResponse execjob(@RequestBody FormEngineRequest requestObj,FormModel formModel)throws SchedulerException{
		FormEngineResponse ret = new FormEngineResponse("");
		jobTaskService.runAJobNow(requestObj.getData().get("id").toString());
		return ret;
	}
	/**
	 * 根据SQLId获取单条数据，用于装载表单数据
	 * 需要实现sqlId,客户端请求需将queryParam参数设置为sqlId
	 * 找不到数据时返回空
	 * @param request
	 * @param uiName 要初始化的ui
	 * @param panelName 要初始化的ui中的panel
	 * @param queryPage
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("querybyid")
	@ResponseBody
	public FormEngineResponse selectOne(@RequestBody FormEngineRequest requestObj,FormModel formModel){
		FormEngineResponse retData;
		Map<String,Object> data = null;
		if(requestObj.getQuery().getQueryParam()!=null &&
				!requestObj.getQuery().getParams().isEmpty() //无条件时不查!!!
				){
			data =  formService.selectOneBySqlId(requestObj.getQuery());
			if(data!=null){
				FormConfiguration.setControlDefault(formModel,data,data,FormConstants.METHOD_GETSOURCE);//查询结果返回前端时
				data = FormConfiguration.getControlModel(formModel,data);
			}else{
				ServerTokenData serverTokenData = formModel.getServerTokenData();
				FormLogger.logJDBC(FormConstants.LOGIC_QUERYBYID,
						String.format("[%s][%s][%s]", serverTokenData.getUiName(),serverTokenData.getFunctionId(),requestObj.getData()));
			}
		}
		retData = new FormEngineResponse(data);
		return retData;
	}

	/**
	 * 通用表格查询
	 * @param request
	 * @param moduleName
	 * @param page
	 * @return
	 */
	@RequestMapping("query")
	@ResponseBody
	public FormEngineResponse query(@RequestBody FormEngineRequest requestObj){
		List<Map<String,Object>> list = formService.queryBySqlId(requestObj.getQuery());
		DataGrid dgData = new DataGrid(list);
		int total = requestObj.getQuery().getTotalRecord();
		if(requestObj.getQuery().getPage()==null){
			dgData.setTotal(list.size());
		} else {
			dgData.setTotal(total);
		}
		return new FormEngineResponse(dgData);
	}
	
	/**
	 * 自定义子查询条件拼接
	 * @param formModel
	 * @param 
	 * @param page
	 * @return
	 */
	@RequestMapping("querycustom")
	@ResponseBody
	public FormEngineResponse customquery(@RequestBody FormEngineRequest requestObj,CustomFormModel formModel){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		DataGrid dgData = new DataGrid(list);

		return new FormEngineResponse(dgData);
	}


	/**
	 * 通用表格查询
	 * find弹窗查询用
	 * @param request
	 * @param moduleName
	 * @param page
	 * @return
	 */
	@RequestMapping("commonquery")
	@ResponseBody
	public FormEngineResponse commonQuery(@RequestBody FormEngineRequest requestObj,ServerTokenData serverTokenData){
		List<Map<String,Object>> list = formService.queryBySqlId(requestObj.getQuery());
		Map<String,Object> param = new HashMap<>();
		param.put("id", requestObj.getData().get("id"));
		requestObj.getQuery().setDataByMap(param);
		//补充项
		if(list!=null){
	    	Procedure p = FormConfiguration.getProcedure(serverTokenData);
	    	Param paramOptions = p.getParam(Param.P_Options);
	    	Param paramColumns = p.getParam(Param.P_Columns);
	    	String[] arrColumns = paramColumns==null?new String[]{}:paramColumns.getValue().split("[|]");
	    	List<String> columnList = new ArrayList<>();
	    	for(String column:arrColumns){
	    		String[] arrColumnDesc = column.split(",");
	    		columnList.add(arrColumnDesc[0]);//列属性
	    	}
	    	int columnSize = columnList.size();
	    	if(paramOptions!=null){
		    	List<Map<String,Object>> optionRows = new ArrayList<>();

	    		String options = paramOptions.getValue();
	    		String[] arrOptions = options.split("[|]");//多行用竖线隔开
	    		for(String option:arrOptions){
	    			//以下两个是固定项，不加在表格中。跳过显示在表格顶部的固定项
	    			if(FindLogic.isFixedOption(option)){
	    					continue;
	    				}
	    			String[] arrValues = option.split(",");
	    			int loopSize = Math.min(arrValues.length, columnSize);
	    			Map<String,Object> row = new HashMap<>();
	    			for(int i=0;i<loopSize;i++){
	    				row.put(columnList.get(i), arrValues[i]);
	    			}
	    			optionRows.add(row);
	    		}
	    		list.addAll(0, optionRows);
	    	}
		}

		DataGrid dgData = new DataGrid(list);
		dgData.setTotal(requestObj.getQuery().getTotalRecord());
		return new FormEngineResponse(dgData);
	}

	/**
	 * 通用下拉组件数据源，类似query
	 * 查询用条件通常需要加权限,非空等默认项
	 * Options选项+SQL数据源
	 * 两者均不要求必填
	 * @param request
	 * @param moduleName
	 * @param page
	 * @return
	 */
	@RequestMapping("list")
	@ResponseBody
	public FormEngineResponse queryItem(@RequestBody FormEngineRequest requestObj,ServerTokenData serverTokenData){
		List<Map<String,Object>> list = new ArrayList<>();
		if(requestObj.getQuery().getQueryParam().isEmpty()){
			FormLogger.logFlow(String.format("UI[%s]的下拉组件数据源未配置Sql", serverTokenData.getUiName()), FormLogger.LOG_TYPE_WARN);
		}else{
			list = formService.queryBySqlId(requestObj.getQuery());//sql返回列中需含value,text两列。额外特性列selected
		}
		if(list==null){
			list= new ArrayList<>();
		}
		FormUtil.appendDefaultQueryItems(list,serverTokenData);
		return new FormEngineResponse(list);
	}

	/**
	 * 初始化指定panel里的值 并按{组件名：值,...}方式返回
	 * 初始化值的依据是control的initial属性的aviator表达式取值结果
	 * @param request
	 * @param moduleName
	 * @param page
	 * @return
	 */
	@RequestMapping("clear")
	@ResponseBody
	public FormEngineResponse init(@RequestBody FormEngineRequest requestObj,FormModel formModel){
		Map<String,Object> map = FormUtil.getInitialData(formModel);
		return new FormEngineResponse(map.isEmpty()?"":map);//空map序列化返回结果时嵌套错误
	}

	/**
	 *
	 * @param request
	 * @param id 选择对话框数据源唯一标识
	 * @return
	 */
	@RequestMapping("find")
	public String showFind(FormEngineRequest requestObj,HttpServletRequest request,FormModel formModel,ServerTokenData serverTokenData,Model model){
		Procedure proc = FormConfiguration.getProcedure(serverTokenData);
		String strColumns = proc.getParam(Param.P_Columns).getValue();
		Param paramMulti = proc.getParam(Param.P_Multi);
		boolean selMulti =
				(paramMulti!=null && paramMulti.getValue().equalsIgnoreCase(FormConstants.XML_YES));

		StringBuilder sFilter = new StringBuilder();
		if(ObjectUtils.toString(strColumns).isEmpty()){
			strColumns = "id,编码|name,名称";
		}
		List<Column> columns = new ArrayList<>();
		String[] arrColumns = strColumns.split("[|]");
		for(String c:arrColumns){
			Column col = new Column(c);
			columns.add(col);
			if(!col.getField().toLowerCase().endsWith("time") &&
					!col.getField().toLowerCase().endsWith("date")	){
				sFilter.append(String.format("%s_%s,", FieldCondition.OP_LIKE,col.getField()));
			}
		}

		if(sFilter.length()==0){
			sFilter.append("lk_name,eq_id");//按默认列配置
		}else{
			sFilter.deleteCharAt(sFilter.length()-1);
		}
		if(SessionUtils.getAttrbute(serverTokenData.getFlowId())!=null){
			model.addAttribute("remember", SessionUtils.getAttrbute(serverTokenData.getFlowId()));
		}
		
		model.addAttribute("id", request.getParameter("id"));
		model.addAttribute("filterName", sFilter.toString());
		model.addAttribute("columns", columns);
		model.addAttribute("token", serverTokenData.getToken());
		//检查options选项中是否有固定项（已设置，未设置）
    	Param paramOptions = proc.getParam(Param.P_Options);
    	Param paramIn = proc.getParam(Param.P_InputPanelId);
    	if(paramIn!=null){//入参，构造查询用
    		model.addAttribute(Param.P_InputPanelId,paramIn.getValue());
    	}

    	String options = paramOptions==null?"":paramOptions.getValue();
    	if(options.indexOf(FieldCondition.NULL_FIELD_VALUE)>=0){
    		model.addAttribute("displayOp2","inline");
    	}else{
    		model.addAttribute("displayOp2","none");
    	}
    	if(options.indexOf(FieldCondition.NOT_NULL_FIELD_VALUE)>=0){
    		model.addAttribute("displayOp3","inline");
    	}else{
    		model.addAttribute("displayOp3","none");
    	}
    	if(options.indexOf(FormConstants.RESET_FIELD_VALUE)>=0){
    		model.addAttribute("displayOp4","inline");
    	}else{
    		model.addAttribute("displayOp4","none");
    	}
		String[] arrOptions = options.split("[|]");
		int len = arrOptions.length;
		//把固定项的数据描述构造出来，用于界面选择后返回
		for(int i=0;i<len;i++){
			String[] arrCurrentOp = arrOptions[i].split(",");
			Map<String,String> mapCurrentOp = new HashMap<>();
			String controlName;
			for(int j=0;j<columns.size() && j<arrCurrentOp.length;j++){
				controlName = columns.get(j).getControlName();
				//有映射目标组件的属性,才构造到对象里
				if(!ObjectUtils.toString(controlName).isEmpty()){
					if(arrOptions[i].indexOf(FormConstants.RESET_FIELD_VALUE)>=0){//重置的含义为，Column配置中的所有映射目标组件的值都设置为空
						mapCurrentOp.put(columns.get(j).getControlName(), "");
					}else{
						mapCurrentOp.put(columns.get(j).getControlName(), arrCurrentOp[j]);
					}
				}
			}
			String jsonObj = FormUtil.toJsonString(mapCurrentOp);
			if(arrOptions[i].indexOf(FieldCondition.NULL_FIELD_VALUE)>=0){
	    		model.addAttribute("op2",jsonObj);
				model.addAttribute("op2Title",arrCurrentOp[1]);
			}else if(arrOptions[i].indexOf(FieldCondition.NOT_NULL_FIELD_VALUE)>=0){
	    		model.addAttribute("op3",jsonObj);
				model.addAttribute("op3Title",arrCurrentOp[1]);
			}else if(arrOptions[i].indexOf(FormConstants.RESET_FIELD_VALUE)>=0){
				model.addAttribute("op4",jsonObj);
				model.addAttribute("op4Title",arrCurrentOp[1]);
			}
		}
		if(selMulti){
			ColumnCheckable ckColumn = new ColumnCheckable();
			columns.add(0, ckColumn);
			return FormConstants.VIEW_FIND_MULTI;
		}else{
			return FormConstants.VIEW_FIND;
		}
	}

	/**
	 *
	 * @param request
	 * @param id 选择对话框数据源唯一标识
	 * @return
	 */
	@RequestMapping("batchimport")
	public String showImportBat(FormEngineRequest requestObj,HttpServletRequest request,FormModel formModel,ServerTokenData serverTokenData,Model model){
		prepareBatch(requestObj,request,serverTokenData,model);
		return FormConstants.VIEW_IMPORTBAT;
	}
	
	/**
	 *
	 * @param request
	 * @param id 选择对话框数据源唯一标识
	 * @return
	 */
	@RequestMapping("batchimportcust")
	public String showImportBatCust(FormEngineRequest requestObj,HttpServletRequest request,FormModel formModel,ServerTokenData serverTokenData,Model model){
		prepareBatch(requestObj,request,serverTokenData,model);
		return FormConstants.VIEW_IMPORTBATCUST;
	}
	
	@RequestMapping("test")
	public String showTest(){
		return "common/test1";
	}
	/**
	 *
	 * @Description: 数据导出
	 * @author: gaolu
	 * @date: 2017年8月22日 下午8:54:44
	 * @param:
	 * @return: String
	 * @throws Exception
	 */
	@Value("${file.upload.path}")
	private String DISH;
	@RequestMapping("/export")
	@ResponseBody
	public FormEngineResponse  exportData(@RequestBody FormEngineRequest requestObj) {
		FormEngineResponse ret = new FormEngineResponse("");
		List<String> titleList = (List<String>)requestObj.getData().get("dataTitle");
        String[] dataTitle =titleList.stream().toArray(String[]::new);
        List<String> dataValueList = (List<String>)requestObj.getData().get("dataValue");
        String[] dataValue = dataValueList.stream().toArray(String[]::new);;
        String exportType = requestObj.getData().get("exportType").toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String expFileName = requestObj.getData().get("title").toString()+sdf.format(new Date());  //导出文件的文件名
        requestObj.getQuery().getData().clear();
		//获取数据
		List<Map<String,Object>> list = formService.queryBySqlId(requestObj.getQuery());

		//commitLogService.saveCommitLog();
		//创建文件并生成文件信息
		ExportOperation export = new ExportOperation();
		FileModel fileModel = export.createFile(dataTitle, dataValue, exportType, list,DISH,expFileName);
		if (fileModel!=null) {
			fileService.insertFile(fileModel);
			ret.setMsg(fileModel.getId().toString());
		}else{
			ret.setMsg("0");
		}
		
		return ret;
	}

	/**
	 *
	 * @Description: validate分支校验
	 * @author: gaolu
	 * @date: 2017年9月29日 下午7:32:01
	 * @param:
	 * @return: FormEngineResponse
	 */
	@RequestMapping("/validate")
	@ResponseBody
	public FormEngineResponse validate(@RequestBody FormEngineRequest requestObj,CustomFormModel formModel){

		//取得panel并获取panel下所有contrl
		ServerTokenData serverTokenData = formModel.getServerTokenData();
		Map<String,Object> data = requestObj.getData();
		List<Control> cList = formModel.getControlListAll();

		//判断Procedure必填参数
		Procedure proc = FormConfiguration.getProcedure(serverTokenData);
    	Param paramOption = proc.getParam(Param.P_Options);//Param.P_Options
    	String validExp = ObjectUtils.toString(paramOption.getValue());
		String option = paramOption==null?"":validExp;
		if(option.isEmpty()){
			throw new ServiceException(ErrorCode.ILLEGAL_REQUEST_PARAMETER,
					String.format("[%s][%s].validate不存在option",
							serverTokenData.getUiName(),serverTokenData.getFlowId()));
		}

		//解析表达式，得到control值和表达式
		int start = option.indexOf("(");
		int end = option.lastIndexOf(")");
		String optionParam = option.substring(start+1, end);
		String[] param = optionParam.split(",");
		for(int i=0,j=param.length; i < j ; i++){
			if (param[i].contains("'")) {
				param[i] = "";
			}else{
				String controlName = param[i].substring(param[i].lastIndexOf(".")+1);

				Iterator iter = data.keySet().iterator();
				while (iter.hasNext()) {
					String key = (String) iter.next();
				    if (key!=null && !key.equals(controlName)) {
				    	iter.remove();
					}
				}
			}
		}
		FormModel fModel = new FormModel();
		fModel.setData(data);

		//执行表达式并得到返回结果
		Boolean result = (Boolean) AviatorUtil.validExpression(validExp, formModel);
		return new FormEngineResponse(result);
	}

	@RequestMapping("transform")
	@ResponseBody
	public FormEngineResponse transform(@RequestBody FormEngineRequest requestObj,CustomFormModel formModel){
		//这里做什么取决于插件配置
		return new FormEngineResponse("");
	}

	/**
	 *
	 * @Description: 文件预览
	 * @author: gaolu
	 * @date: 2017年10月30日 上午11:54:03
	 * @param:
	 * @return: String
	 */
	@RequestMapping("previewfile")
	public String proviwFile(FormEngineRequest requestObj,HttpServletRequest request, Model model,ServerTokenData serverTokenData){
		String uniqueCode = (String)request.getParameter("id");	//文件主键或者路径
		String fileType = "";	//文件后缀
		String md = "MARKDOWN";
		String fileContent = "";	//文件内容
		String filePath = "";	//文件路径

		//根据id得到该文件信息根据文件后缀归类文件
		if (StringUtils.isNotEmpty(uniqueCode)) {
		    Pattern pattern = Pattern.compile("[0-9]*"); 
		    Matcher isNum = pattern.matcher(uniqueCode);
		    //如果是数字则查询文件信息
		    if (isNum.matches()) {
		    	Integer id = Integer.parseInt(uniqueCode);
		    	FileModel fileModel = fileService.getFileById(id);
		    	if (fileModel!=null) {
		    		fileType = fileModel.getFileType();
		    		filePath = fileModel.getPath()+fileModel.getFileUUID()+"."+fileModel.getFileType(); //+".dat"
		    	}
		    }else{
		    	//如果不是数字则是文件路径
		    	uniqueCode.replace(".dat", "");
		    	fileType = uniqueCode.substring(uniqueCode.lastIndexOf(".")+1);
		    	filePath = uniqueCode;
		    }
			
			fileType = new WhiteListOperation().classify(fileType);
			if (fileType.equals(md)) {
				request.setAttribute("fileId", uniqueCode);
				request.setAttribute("EditMode", "Readonly");
				request.setAttribute("flowchart", "flowchart");
//				System.out.println(request.getParameter("EditMode"));
//				System.out.println(request.getAttribute("EditMode"));
				return showMarkDown(null,request,null,serverTokenData,model);
			}
		}

		model.addAttribute("fileContent",fileContent);
		model.addAttribute("id", uniqueCode);
		model.addAttribute("type", fileType);
		return FormConstants.VIEW_RPEVIEW;
	}
	
	/**
	 *
	 * @Description: 播放
	 * @author: gaolu
	 * @date: 2017年10月30日 上午11:54:03
	 * @param:
	 * @return: String
	 */
	@RequestMapping("play")
	 public String play(FormEngineRequest requestObj,HttpServletRequest request, Model model,ServerTokenData serverTokenData) {
	  String uniqueCode = (String)request.getParameter("id"); //文件主键或者路径
	  String business = (String)request.getParameter("business"); //文件主键或者路径  
	  String empid = SessionUtils.getUserInfo().getEmpId().toString();  
	  String empname = SessionUtils.getUserInfo().getEmpName();  
	  String fileType = ""; //文件后缀
	  String md = "MARKDOWN";
	  String fileContent = ""; //文件内容
	  String filePath = ""; //文件路径
	  String fileid = "";  
	  if (StringUtils.isNotEmpty("id")) {
	      Pattern pattern = Pattern.compile("[0-9]*"); 
	      Matcher isNum = pattern.matcher(uniqueCode);
	      //如果是数字则查询文件信息
	      if (isNum.matches()) {
	       Integer id = Integer.parseInt(uniqueCode);
	       FileModel fileModel = fileService.getFileById(id);
	       if (fileModel!=null) {
	        fileType = fileModel.getFileType();
	        filePath = fileModel.getPath()+fileModel.getFileUUID()+"."+fileModel.getFileType(); //+".dat"
	       }
	      }else{
	       //如果不是数字则是文件路径
	       uniqueCode.replace(".dat", "");
	       fileType = uniqueCode.substring(uniqueCode.lastIndexOf(".")+1);
	       filePath = uniqueCode;
	      }
	   
	   fileType = new WhiteListOperation().classify(fileType);
	   if (fileType.equals(md)) {
	    request.setAttribute("fileId", uniqueCode);
	    request.setAttribute("EditMode", "Readonly");
	    request.setAttribute("flowchart", "flowchart");
//	    System.out.println(request.getParameter("EditMode"));
//	    System.out.println(request.getAttribute("EditMode"));
	    return showMarkDown(null,request,null,serverTokenData,model);
	   }
	  }

	  model.addAttribute("fileContent",fileContent);
	  model.addAttribute("id", uniqueCode);
	  model.addAttribute("type", fileType);
	  model.addAttribute("empid", empid);
	  model.addAttribute("empname", empname);
	  model.addAttribute("business", business);
	//  model.addAttribute("fileid", fileid);
	  return FormConstants.VIEW_PLAY;
	 }

	/**
	 * 关联的父级UI已经在transition中清除
	 * @param request
	 * @param requestObj
	 */
	@RequestMapping("close")
	@ResponseBody
	public FormEngineResponse closeTabs(HttpServletRequest request,@RequestBody FormEngineRequest requestObj){
		FormEngineResponse responseObj = new FormEngineResponse("");
		@SuppressWarnings("unchecked")
		List<String> uiList = (ArrayList<String>)requestObj.getData().get("ui");
		if(uiList!=null) {
			for(String uiId:uiList){
				formTokenService.cleanTokens(uiId);
			}
		}
		return responseObj;
	}

	/**
	 *
	 * @Description: 带格式导出
	 * @author: gaolu
	 * @date: 2017年10月30日 上午11:54:03
	 * @param:
	 * @return: String
	 */
	@Value("${file.exFormat.path}")
	private String modelPath;
	@RequestMapping("exportformat")
	@ResponseBody
	public FormEngineResponse exportFormat(@RequestBody FormEngineRequest requestObj,CustomFormModel formModel){
		//获取数据（如果不是Map<penal,list<map<contol,value>>>需要转换格式）
		Map<String,Object> data = requestObj.getData();
		Map<String, List<Map>> resultData = new HashMap<>();
	  for (Map.Entry<String, Object> entry : data.entrySet()) {
		  List<Map> controlList = new ArrayList<>();
		  Map map = (Map)entry.getValue();
		  if (map.get("total")!=null) {
			controlList = (List)map.get("rows");
		  }else{
			controlList.add(map);
		  }
		  resultData.put(entry.getKey(), controlList);
		  //System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
	  }
		//		List<Control> cList = formModel.getControlListAll();


		Date date = new Date();
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		//创建文件路径对象
	    FilePathUtil filePath = new FilePathUtil(date,DISH);
		file f = new file(filePath.getExportPath());
		f.createlist();
		String path = filePath.getExportPath() + uuid+"."+"xls"; //.dat

		//根据模板生成报表文件
		ExportFormatUtil exFormat = new ExportFormatUtil("");
		exFormat.getExcelFormat(modelPath,resultData,path);

		//存入文件数据到表中
		FileModel fileModel = new FileModel();
		fileModel.setMediaType(mediaTypeEnum.local.toString());
		fileModel.setAccessType("private");
		fileModel.setFileUUID(uuid);
		fileModel.setLength("0");
		fileModel.setTitle("exportFormat");
		fileModel.setCreateTime(DateUtils.getDateToStr("yyyy-MM-dd HH:mm:ss",date));
		fileModel.setFileType("xls");
		fileModel.setSource("export");
		fileModel.setPath(filePath.getExportPath());
		fileModel.setCreateBy(Integer.parseInt(SessionUtils.getUserInfo().getEmpId().toString()));
		FormEngineResponse ret = null;
		if (fileModel!=null) {
			fileService.insertFile(fileModel);
			ret = new FormEngineResponse(fileModel.getId().toString());
		}

		//把id传给前台
		return ret;
	}

	//启动工作流

	/**
	 *
	 * @Description: 带格式导出
	 * @author: gaolu
	 * @date: 2017年10月30日 上午11:54:03
	 * @param:
	 * @return: String
	 */
	@RequestMapping("printpdf")
	@ResponseBody
	public FormEngineResponse printPDF(@RequestBody FormEngineRequest requestObj,CustomFormModel formModel){
		//获取数据（如果不是Map<penal,list<map<contol,value>>>需要转换格式）
		Map<String,Object> data = requestObj.getData();
		Map<String, List<Map>> resultData = new HashMap<>();
	  for (Map.Entry<String, Object> entry : data.entrySet()) {
		  List<Map> controlList = new ArrayList<>();
		  Map map = (Map)entry.getValue();
		  if (map.get("total")!=null) {
			controlList = (List)map.get("rows");
		  }else{
			controlList.add(map);
		  }
		  resultData.put(entry.getKey(), controlList);
		   //System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
	  }
		//		List<Control> cList = formModel.getControlListAll();


		Date date = new Date();
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		//创建文件路径对象
	    FilePathUtil filePath = new FilePathUtil(date,DISH);
		file f = new file(filePath.getExportPath());
		f.createlist();
		String path = filePath.getExportPath() + uuid+"."+"xls";

		//根据模板生成报表文件
		ExportFormatUtil exFormat = new ExportFormatUtil("");
		exFormat.getExcelFormat(modelPath,resultData,path);

		//excel转pdf文件
		String pdfPath = filePath.getExportPath() + uuid+"."+"pdf";
		try {
			PDFUtil pdfUtil = new PDFUtil(path);
			pdfUtil.office2PDF(path, pdfPath);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		//存入文件数据到表中
		FileModel fileModel = new FileModel();
		fileModel.setMediaType(mediaTypeEnum.local.toString());
		fileModel.setAccessType("private");
		fileModel.setFileUUID(uuid);
		fileModel.setLength("0");
		fileModel.setTitle("printPDF");
		fileModel.setCreateTime(DateUtils.getDateToStr("yyyy-MM-dd HH:mm:ss",date));
		fileModel.setFileType("pdf");
		fileModel.setSource("export");
		fileModel.setRemark("printPDF");
		fileModel.setPath(filePath.getExportPath());
		fileModel.setCreateBy(Integer.parseInt(SessionUtils.getUserInfo().getEmpId().toString()));
		FormEngineResponse ret = null;
		if (fileModel!=null) {
			fileService.insertFile(fileModel);
			ret = new FormEngineResponse(fileModel.getId().toString());
		}

		//把id传给前台
		return ret;
	}

	/**
	 *
	 * @param request
	 * @param id 选择对话框数据源唯一标识
	 * @return
	 */
	@RequestMapping("markdown")
	public String showMarkDown(FormEngineRequest requestObj,HttpServletRequest request,FormModel formModel,ServerTokenData serverTokenData,Model model){
		String strColumns = null; 
		//strColumns = FormConfiguration.getProcedure(serverTokenData).getParam(Param.P_Columns).getValue();

		StringBuilder sFilter = new StringBuilder();//"lk_name,eq_id";
		if(ObjectUtils.toString(strColumns).isEmpty()){
			strColumns = "id,编码|name,名称";
		}
		List<Column> columns = new ArrayList<>();
		String[] arrColumns = strColumns.split("[|]");
		for(String c:arrColumns){
			Column col = new Column(c);
			columns.add(col);
			sFilter.append(String.format("%s_%s,", FieldCondition.OP_LIKE,col.getField()));
		}

		if(sFilter.length()==0){
			sFilter.append("lk_name,eq_id");//按默认列配置
		}else{
			sFilter.deleteCharAt(sFilter.length()-1);
		}

		//编辑模式(insert/update)
		String eMode = request.getParameter("EditMode")!=null?request.getParameter("EditMode"):(String) request.getAttribute("EditMode");
		if(eMode.equals("Edit") || eMode.equals("Readonly")){
			//查询md文件的内容并加载到markdown编辑器内容区
			String fileId =  request.getParameter("fileId")!=null?request.getParameter("fileId"):(String)request.getAttribute("fileId");
			FileModel fm = fileService.getFileById(Integer.parseInt(fileId));
			String fmName = fm.getTitle(); //文档名称
			String filePath = fm.getPath()+fm.getFileUUID()+"."+fm.getFileType(); //当前md文档存放路径   +".dat"
//			System.out.println("当前md文档存放路径:"+filePath);
//			System.out.println("文档名称:"+fmName);
			//读取文件内容
			FileUtils file = new FileUtils();
			String fileContent = null;
			try {
				String flowChart = request.getAttribute("flowchart")!=null?(String)request.getAttribute("flowchart"):"";
				if(!flowChart.equals("") && flowChart.equals("flowchart")){ 
					//flowchart
					fileContent = file.readFileToString(new File(filePath));
				}else{
					fileContent = file.readFileToString(new File(filePath),"utf-8");
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
			model.addAttribute("fileContent", fileContent);
			model.addAttribute("fileTitle", fmName);
			model.addAttribute("fileId",fileId);
		}

		model.addAttribute("id", request.getParameter("id"));
		model.addAttribute("filterName", sFilter.toString());
		model.addAttribute("columns", columns);
		model.addAttribute("token", serverTokenData.getToken());
		model.addAttribute("Mode",eMode );



		//检查options选项中是否有固定项（已设置，未设置）
    	Procedure p = FormConfiguration.getProcedure(serverTokenData);
    	Param paramOptions = p.getParam(Param.P_Options);
    	String options = paramOptions==null?"":paramOptions.getValue();
    	if(options.indexOf(FieldCondition.NULL_FIELD_VALUE)>=0){
    		model.addAttribute("displayOp2","inline");
    	}else{
    		model.addAttribute("displayOp2","none");
    	}
    	if(options.indexOf(FieldCondition.NOT_NULL_FIELD_VALUE)>=0){
    		model.addAttribute("displayOp3","inline");
    	}else{
    		model.addAttribute("displayOp3","none");
    	}
    	if(options.indexOf(FormConstants.RESET_FIELD_VALUE)>=0){
    		model.addAttribute("displayOp4","inline");
    	}else{
    		model.addAttribute("displayOp4","none");
    	}
		String[] arrOptions = options.split("[|]");
		//把固定项的数据描述构造出来，用于界面选择后返回
		for(int i=0;i<arrOptions.length;i++){
			String[] arrCurrentOp = arrOptions[i].split(",");
			Map<String,String> mapCurrentOp = new HashMap<>();
			String controlName;
			for(int j=0;j<columns.size() && j<arrCurrentOp.length;j++){
				controlName = columns.get(j).getControlName();
				//有映射目标组件的属性,才构造到对象里
				if(!ObjectUtils.toString(controlName).isEmpty()){
					if(arrOptions[i].indexOf(FormConstants.RESET_FIELD_VALUE)>=0){//重置的含义为，Column配置中的所有映射目标组件的值都设置为空
						mapCurrentOp.put(columns.get(j).getControlName(), "");
					}else{
						mapCurrentOp.put(columns.get(j).getControlName(), arrCurrentOp[j]);
					}
				}
			}
			String jsonObj = FormUtil.toJsonString(mapCurrentOp);
			if(arrOptions[i].indexOf(FieldCondition.NULL_FIELD_VALUE)>=0){
	    		model.addAttribute("op2",jsonObj);
			}else if(arrOptions[i].indexOf(FieldCondition.NOT_NULL_FIELD_VALUE)>=0){
	    		model.addAttribute("op3",jsonObj);
			}else if(arrOptions[i].indexOf(FormConstants.RESET_FIELD_VALUE)>=0){
				model.addAttribute("op4",jsonObj);
			}
		}
		return FormConstants.VIEW_MARKDOWN;
	}

	@RequestMapping("editcode")
	public ModelAndView showEditCode(FormEngineRequest requestObj, HttpServletRequest request, FormModel formModel, ServerTokenData serverTokenData, Model model){

		ModelAndView modelAndView = new ModelAndView(FormConstants.VIEW_EDIT_CODE);
		modelAndView.addObject("token",requestObj.getToken());
		modelAndView.addObject("mode","htmlmixed");
		String catalogPath = request.getParameter("catalog");
		String title = "";
		if(request.getParameter("title")!=null){
			title= request.getParameter("title");
		}
		String key =request.getParameter("key");
		String md5Key = MD5Util.MD5Encode(catalogPath).toUpperCase();
		//含非数字字母的字符串MD5函数结果客户端服务端结果不一致，暂时去掉。
		if(1==1 || key.equals(md5Key)) {
			File file = new File(catalogPath);
			FormLogger.logFlow("catalogPath="+catalogPath ,FormLogger.LOG_TYPE_INFO);
			File[] fList = file.listFiles();
			FormLogger.logFlow((fList==null?"null":fList.length)+"条" ,FormLogger.LOG_TYPE_INFO);
			List<File> catalogs = new ArrayList<>();
			List<File> allFiles = new ArrayList<>();
			if(fList!=null){
				for (File f : fList) {
					FormLogger.logFlow("f=" + f.getAbsolutePath(), FormLogger.LOG_TYPE_INFO);
					if (f.isDirectory()) {
						catalogs.add(f);
					}
					allFiles.add(f);
				}
			}
			modelAndView.addObject("catalogs", catalogs);
			if(allFiles.isEmpty()){
				FormLogger.logFlow("catalogPath is empty, ", FormLogger.LOG_TYPE_WARN);
			}
		}else{
			FormLogger.logFlow("key verification failed, ", FormLogger.LOG_TYPE_WARN);
			modelAndView.addObject("catalogs", "");
		}
		modelAndView.addObject("title", title);

		return modelAndView;
	}
	
	private void prepareBatch(FormEngineRequest requestObj,HttpServletRequest request,ServerTokenData serverTokenData,Model model){
		model.addAttribute("columns", prepareColumns(serverTokenData,model));
		model.addAttribute("title", request.getParameter("title"));
		model.addAttribute("token", requestObj.getToken());
	}
	
	/**
	 * 批处理共用
	 * @param serverTokenData
	 * @param model
	 * @return
	 */
	private List<Column> prepareColumns(ServerTokenData serverTokenData,Model model){
		Procedure procInput = FormConfiguration.getProcedure(serverTokenData);//temp
		Param paramInput = procInput.getParam(Param.P_InputPanelId);
		UI ui = ((XML) ContextUtils.getAttrbute(ConstantUtil.XML)).getUI(paramInput.getValue().split("\\.")[0]);
		Panel panel = ui.getUiPanel(paramInput.getValue().split("\\.")[1]);
		List<Control> controlList = panel.getControlList();
		List<Column> columns = new ArrayList<>();
		columns.add(new Column("验证结果","验证结果"));
		StringBuilder titles = new StringBuilder();
		for(Control c: controlList){
			if(c.isEdit()&&!c.getType().equals("Hidden")){
				titles.append(c.getTitle()).append(",");
				boolean flg =false;
				if(procInput.getParam(Param.P_Dict)!=null){
					String[] paramDict = procInput.getParam(Param.P_Dict).getValue().split(",");
					for(String str:paramDict){
						if(c.getName().equals(str.split("\\|")[0])){
							flg=true;
							break;
						}
					}
					if(flg){
						columns.add(new Column(c.getName()+"_name", c.getTitle()));
					} else {
						columns.add(new Column(c));
					}
				} else {
					columns.add(new Column(c));
				}
			}
		}
		model.addAttribute("titles", titles);
		return columns;
	}
}
