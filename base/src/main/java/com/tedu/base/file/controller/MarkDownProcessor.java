package com.tedu.base.file.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.tedu.base.common.utils.ConstantUtil;
import com.tedu.base.common.utils.ContextUtils;
import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.common.utils.ResponseJSON;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.aspect.ILogicReviser;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.util.FormConfiguration;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.engine.util.FormValidator;
import com.tedu.base.file.model.FileModel;
import com.tedu.base.file.model.MarkDownDocModel;
import com.tedu.base.file.service.FileService;
import com.tedu.base.file.service.MarkDownDocService;
import com.tedu.base.file.util.io.FilePathUtil;
import com.tedu.base.initial.model.xml.logic.LogicModel;
import com.tedu.base.initial.model.xml.logic.LogicsModel;
import com.tedu.base.initial.model.xml.ui.Control;
import com.tedu.base.initial.model.xml.ui.Flow;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.initial.model.xml.ui.ModuleObject;
import com.tedu.base.initial.model.xml.ui.Panel;
import com.tedu.base.initial.model.xml.ui.Param;
import com.tedu.base.initial.model.xml.ui.Procedure;
import com.tedu.base.initial.model.xml.ui.UI;
import com.tedu.base.initial.model.xml.ui.XML;
import com.tedu.base.task.SpringUtils;

public class MarkDownProcessor implements ILogicReviser{

	FileService fileService = SpringUtils.getBean("fileService");
	MarkDownDocService markDownDocService= SpringUtils.getBean("markDownDocService");
	public final Logger log = Logger.getLogger(this.getClass()); // 日志记录器
	ResponseJSON responseJSON = null; // 返回json数据
	String hintMessage = ""; // 提示消息

	
	@Override
	public FormModel beforeLogic(FormEngineRequest requestObj) {
		FormLogger.info("Transform逻辑插件前置方法");
		return null;
	}

	@Override
	public void afterLogic(FormEngineRequest requestObj, FormEngineResponse responseObj) {
		
		FormLogger.info("Transform.plugin.UIReload逻辑后置方法");
		List<String> msg = new ArrayList<String>();
		responseObj.setMsg("加载成功");
		responseObj.setCode("0");
		try {
			this.flowChartMD();
			msg.add("配置加载成功==>");
			FormValidator.validate();
			msg.add("配置校验完毕，校验详情请查看后台日志");
		} catch (Exception e) {
			SessionUtils.removeAttrbute("xml");
			msg.add("异常:" + e.getMessage());
			msg.add("配置加载失败,请检查!");
			responseObj.setCode("1");
		}
		responseObj.setData(null);
		responseObj.setMsg(StringUtils.join(msg.toArray(),";"));
//		return "common/formEngineResponseView";				
	}
	
	/**
	 * 重新生成markdown流程图.
	 * 
	 * @author hejiakuo
	 * @param request
	 * @param model
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	//@RequestMapping("flowChartMD")
	public String flowChartMD() throws FileNotFoundException, IOException {
		//清空markdown_doc表重新记录
		markDownDocService.del();
		
		List<UI> uiList = new ArrayList<UI>();
		List<ModuleObject> moduleObjectList = new ArrayList<ModuleObject>();
		String CRLF = "\r\n";
		
	    String xmlRootFolder = FormConfiguration.class.getClassLoader().getResource(FormConfiguration.XML_BUSINESS_PATH).getPath();

	    Collection<File> xmlList = FileUtils.listFiles(new File(xmlRootFolder), new String[]{"xml"}, true);
	    
		LogicsModel logicsmodel = (LogicsModel) ContextUtils.getAttrbute(FormConstants.CONTEXT_ATTRIBUTE_LOGICS); // 描述逻辑的
					
		// 读取所有ui配置
		XML xml = (XML) ContextUtils.getAttrbute(ConstantUtil.XML);
		
		if (xml.getUi_layer() != null) {
			uiList = xml.getUi_layer().getUiList();
			
		}
		if (xml.getModel_layer() != null) {
			moduleObjectList = xml.getModel_layer().getModuleObjectList();
			
		}
		
		
		int j = 0;
		List<String> xmlname = new ArrayList<String>();
	    for(File f:xmlList){
	    	xmlname.add(f.getPath());	    	
	    }  
	    
	    	
		int num = 0;
		for (UI ui : uiList) {
			String xmlPath = "";
			
			// md's name
			String mdFileName = ui.getName();
			// md’s content
			StringBuffer mdContent = new StringBuffer();
			List<Panel> panelList = new ArrayList<Panel>();
			
			
			for (Iterator iterator = xmlList.iterator(); iterator.hasNext();) {
				File file = (File) iterator.next();
				String fileName = file.getName().substring(0,file.getName().indexOf("."));
				String pFile = file.getParentFile().toString();
				
				if(mdFileName.equals(fileName)){
					xmlPath =  pFile.substring(pFile.lastIndexOf("\\")+1,pFile.length());
					break;
				}
			}
			

			if (ui.getPanelList() != null) {
				panelList = ui.getPanelList();
			}
			if (ui.getFlowList() != null && ui.getFlowList().size() > 0) {
				mdContent.append("# 界面详细设计说明书").append(CRLF);
				mdContent.append("## 1. 概述").append(CRLF);
				mdContent.append("### 1.1  界面标识").append(CRLF).append(ui.getName()).append(CRLF);
				mdContent.append("### 1.2  界面名称").append(CRLF).append(ui.getTitle()).append(CRLF);
				mdContent.append("## 2. 功能点").append(CRLF);
				int sti = 1;
				for (Flow flow : ui.getFlowList()) {
					StringBuffer sbuffer = new StringBuffer();
					mdInit(CRLF, mdContent, flow, sbuffer, ui, sti);
					sbuffer.append("e=>end: 结束" + CRLF);

					if (flow.getFilter() == null || flow.getFilter().equals("")) {
						mdContent.append("#### 2." + sti + ".1  权限限制").append(CRLF + "无" + CRLF);
					} else {
						mdContent.append("#### 2." + sti + ".1  权限限制").append(CRLF + flow.getFilter() + CRLF);
					}

					mdContent.append("#### 2." + sti + ".2  流程图").append(CRLF);

					flow.setBeginEndProcedure();
					List<Procedure> procedureList = flow.getProcedureList();

					StringBuffer contDetail = new StringBuffer();// 详细步骤表头
					contDetail.append("|   名称|  描述 |" + "\r" + "| ------------ | ------------ |" + CRLF);

					if (procedureList != null) {
						StringBuffer sb = new StringBuffer("st->");
						StringBuffer sb1 = new StringBuffer();
						for (int i = 0; i < procedureList.size(); i++) {
							// 流程数据处理逻辑
							Procedure procedure = procedureList.get(i);
							LogicModel lm = logicsmodel.getLogic(procedure.getLogic());

							if (i == procedureList.size() - 1) {
								if (lm != null && !lm.isMulti()) {
									sbuffer.append(procedure.getName() + "=>operation: " + procedure.getName() + ":{"
											+ mdHandle(procedure.getLogic()) + "} \r");
									sb1.append(procedure.getName() + "->");
								}
								sb1.append("e" + "\r");
								sb.append(sb1.toString());

							} else if (procedure.getIfno() == null || procedure.getIfno().equals("")) {
								if (lm != null && !lm.isMulti()) {
									// 无分支
									sbuffer.append(procedure.getName() + "=>operation: " + procedure.getName() + ":{"
											+ mdHandle(procedure.getLogic()) + "} \r");
									sb1.append(procedure.getName() + "->");
								} else {
									// 带判断
									sbuffer.append(procedure.getName() + "=>condition: " + procedure.getName() + ":{"
											+ mdHandle(procedure.getLogic()) + "}\r");
									sb.append(procedure.getName() + "(no)->e" + "\r");
									sb1.append(procedure.getName() + "(yes)->");
								}
							}
							//详细步骤
							execDetail(panelList, contDetail, procedure);

						}
						sbuffer.append("");

						mdContent.append("```flow" + "\r" + sbuffer.toString() + sb.toString() + "```" + "\r");
						mdContent.append("#### 2." + sti + ".3  详细步骤").append(CRLF);
						mdContent.append("\r");
						mdContent.append(contDetail.toString());
						mdContent.append("\r");

					}
					sti++;
				}

				mdContent.append("## 3. 数据项:").append(CRLF);
				int p = 1;
				// 遍历数据项内容
				for (Panel panel : panelList) {
					mdContent.append("### 3." + p + " 区域{" + panel.getTitle() + "}").append(CRLF);
					mdContent.append(" 区域标识：" + panel.getName() + CRLF + "区域名称：" + panel.getTitle() + CRLF);
					mdContent.append("\r");
					StringBuffer sbTitle = new StringBuffer();
					// 数据项表头
					sbTitle.append("|   数据项标识|   数据项名称|  控件类型 |  可编辑 |   必录项|   数据约束|  存储字段 |" + "\r"
							+ "| ------------ | ------------ | ------------ | ------------ | ------------ | ------------ | ------------ |"
							+ CRLF);

					for (Control con : panel.getControlList()) {
						sbTitle.append("|" + con.getName() + "   |" + con.getTitle() + "   |" + con.getType() + "   |"
								+ con.isEdit() + "   |" + con.isRequired() + "   |" + con.getFormat() + "   |"
								+ "&nbsp;&nbsp;" + con.getProperty() + "   |" + CRLF);
					}
					mdContent.append(sbTitle).append(CRLF);
					p++;
				}

				FilePathUtil filepath = new FilePathUtil(new Date(), fileService.getFileDish());
				String path = filepath.getFormPath();
				String fileSave = "";
				//save DB
				String fileUUID = UUID.randomUUID().toString().replaceAll("-", "");
				FileModel  fm = null;
				fm = fileService.getFileByFileName(mdFileName);
				//更新t_file_index表
				if(fm == null){
					// 将内容写入md文件
					//fileSave = path +xmlPath +"\\"+ fileUUID + ".md.dat";
					fileSave = path + fileUUID + ".md";  //.dat
					// io
					FileUtils fileU = new FileUtils();
					File file = new File(fileSave);
					fileU.writeStringToFile(file, mdContent.toString(),"utf-8",true);
					//insert
					fm = fileService.localUpload(mdFileName, file.length() + "", fileUUID, "markdownFlow", "",null,path);
				}else{
					// 将内容写入md文件
					//fileSave = path +xmlPath +"\\"+ fm.getFileUUID() + ".md.dat";
					fileSave = path+ fm.getFileUUID() + ".md"; //.dat
					// io
					FileUtils fileU = new FileUtils();
					File file = new File(fileSave);
					fileU.writeStringToFile(file, mdContent.toString(),"utf-8",true);
					//update
					fm = fileService.localUpload(mdFileName, file.length() + "", fm.getFileUUID(), "markdownFlow", "edit",fm,path);
				}
				
				File file = new File(path);
				if (!file.exists()) {
					file.mkdirs();
				}
				
				String xmlfilename = null;
								
				 for (int i = 0; i <= num; i++) {
					 if(i==num){
						// System.out.println(i);
				         xmlfilename = xmlname.get(i);
				         break;
					 }					  
			        }
				// System.out.println(num);
				num++;
				
				System.out.println(xmlfilename);
				System.out.println(fileSave);
				//
				MarkDownDocModel mdd = new MarkDownDocModel();
				mdd.setFileId(fm.getId());
				mdd.setName(mdFileName);
				mdd.setTitle(ui.getTitle());
				mdd.setProperty(ui.getName());
				mdd.setPath(fileSave); //文件存放路径
				mdd.setXmlpath(xmlfilename);//xml文件路径
				mdd.setType("1");
				mdd.setCreateTime(DateUtils.getDateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
				mdd.setCreateBy(Integer.parseInt(SessionUtils.getUserInfo().getEmpId()+""));
				markDownDocService.insertMDownDoc(mdd);
				
			}
		}

		List<String> msg = new ArrayList<String>();
		FormEngineResponse res = new FormEngineResponse("");
		res.setMsg("加载成功");
		res.setCode("0");
		msg.add("配置加载成功==>");
		res.setData(msg);
		//model.addAttribute("response", res);
		return "common/formEngineResponseView";
	}

	/**
	 * 详细步骤.
	 * @param panelList
	 * @param contDetail
	 * @param procedure
	 */
	private void execDetail(List<Panel> panelList, StringBuffer contDetail, Procedure procedure) {
		String pan = "";
		String per = "";
		String panName = "", perName = "";

		// 详细描述处理逻辑
		if (procedure.getParamList().size() > 0) {
			if (procedure.getParamList().size() == 1) {

				Param param = procedure.getParamList().get(0);
				if (param.getValue().indexOf(".") != -1) {
					// panel.属性
					pan = param.getValue().substring(0, param.getValue().indexOf("."));
					per = param.getValue().substring(param.getValue().indexOf(".") + 1,
							param.getValue().length());
				} else {
					// panel
					pan = param.getValue();
				}
				// panel's title
				for (Panel panel : panelList) {
					if (panel.getName().equals(pan)) {
						panName = panel.getTitle();
					}
					// control's title
					if (!per.equals("") && panel.getControl(per) != null) {
						perName = panel.getControl(per).getTitle();
					}
				}

				if (procedure.getParamList().size() <= 1) {
					if (param.getName().equals("Out")) {
						contDetail.append("| " + procedure.getName() + "  |  取中间变量的值,将数据填充至区域{"
								+ panName + "}");
						if (!"".equals(per)) {
							contDetail.append("的数据项{" + perName + "} |").append("\r");
						} else {
							contDetail.append(" |").append("\r");
						}
					} else if (param.getName().equals("In")) {
						contDetail.append("| " + procedure.getName() + "  | 传值中间变量至区域{" + panName
								+ "}的数据项{" + perName + "} |").append("\r");
					} else if (procedure.getLogic().equals("Back")) {
						contDetail.append("| " + procedure.getName() + "  | 关闭当前页面|").append("\r");
					}
				}

			} else {
				String panName0 = "", panName1 = "";
				/*String v1 = procedure.getParamList().get(0).getValue();
				String vName = "";
				if(v1.indexOf(".") != -1){
					v1 = v1.substring(0,v1.indexOf("."));
					for(Panel p:panelList){
						//System.out.println(p.getName());
						System.out.println(v1);
						//System.out.println(panelList);
						System.out.println(panelList.contains(v1));
						panelList.getClass(Panel.class).
						
						System.out.println("-----------");
						if(p.getName().equals(v1)){
							vName = p.getTitle();
							System.out.println("**:"+vName);
						}
					}
				}*/
				// panel's title
				if (procedure.getParamList().get(1).getName().equals("Out")) {
					contDetail.append("| " + procedure.getName() + "  | 将区域{"
							+ procedure.getParamList().get(0).getValue() + "}作为查询条件,返回数据至区域{"
							+ procedure.getParamList().get(1).getValue() + "}|").append("\r");
				} else if (procedure.getParamList().get(0).getName().equals("In")) {
					if (procedure.getParamList().get(1).getName().equals("Out")) {
						contDetail.append("| " + procedure.getName() + "  | 将区域{"
								+ procedure.getParamList().get(0).getValue() + "}作为查询条件,返回数据至区域{"
								+ procedure.getParamList().get(1).getValue() + "}|").append("\r");
					} else if (procedure.getParamList().get(1).getName().equals("Msg")) {
						contDetail.append("| " + procedure.getName() + "  | 将区域{"
								+ procedure.getParamList().get(0).getValue() + "}选中数据删除,成功后返回提示信息{"
								+ procedure.getParamList().get(1).getValue() + "}|").append("\r");
					} else if (procedure.getParamList().get(0).getValue().indexOf(".") != -1) {
						contDetail
								.append("| " + procedure.getName() + "  | 将区域{"
										+ procedure.getParamList().get(0).getValue().substring(0,
												procedure.getParamList().get(0).getValue()
														.indexOf("."))
										+ "}作为查询条件,返回数据至区域{"
										+ procedure.getParamList().get(1).getValue() + "}|")
								.append("\r");
					}
				} else if (procedure.getParamList().get(1).getName().equals("Msg")
						&& !procedure.getLogic().equals("ConfirmMsg")) {
					contDetail.append("| " + procedure.getName() + "  | 将区域{" + panName0
							+ "}作为查询条件,返回提示信息{" + procedure.getParamList().get(1).getValue() + "}|")
							.append("\r");
				} else if (procedure.getParamList().get(1).getName().equals("Msg")
						&& procedure.getLogic().equals("ConfirmMsg")) {
					contDetail
							.append("| " + procedure.getName() + "  | 提示信息{"
									+ procedure.getParamList().get(1).getValue() + "}|")
							.append("\r");
				}
			}
		} else {
			if (procedure.getLogic().equals("Back")) {
				contDetail.append("| " + procedure.getName() + "  | 关闭当前页面|").append("\r");
			}
		}
	}

	/**
	 * 功能点名称.
	 * 
	 * @param CRLF
	 * @param mdContent
	 * @param flow
	 * @param sbuffer
	 */
	private void mdInit(String CRLF, StringBuffer mdContent, Flow flow, StringBuffer sbuffer, UI ui, int i) {
		if (flow.getEvent().equals("OnLoad") && flow.getTrigger().equals("")) {
			mdContent.append("### 2." + i).append(" 装载<" + ui.getTitle() + ">界面" + CRLF);
		} else if (flow.getEvent().equals("OnClick")) {
			mdContent.append("### 2." + i).append(flow.getEvent() + CRLF);
		} else if (flow.getEvent().equals("OnSelect")) {
			mdContent.append("### 2." + i).append(flow.getEvent() + CRLF);
		} else {
			mdContent.append("### 2." + i).append(flow.getEvent() + CRLF);
		}
		sbuffer.append("st=>start: 开始" + CRLF);
	}

	/**
	 * 转名称.
	 * 
	 * @param operatioin
	 * @return
	 */
	private String mdHandle(String operation) {
		if (operation.equals("DecodeId")) {
			return FormConstants.MARKDOWN_LOGIC_DECODEID_WORD;
		} else if (operation.equals("EncodeId")) {
			return FormConstants.MARKDOWN_LOGIC_ECODEID_WORD;
		} else if (operation.equals("Find")) {
			return FormConstants.MARKDOWN_LOGIC_FIND_WORD;
		} else if (operation.equals("SaveCustom")) {
			return FormConstants.MARKDOWN_LOGIC_SAVEW_WORD;
		} else if (operation.equals("Master2Detail")) {
			return FormConstants.MARKDOWN_LOGIC_MASTER_WORD;
		} else if (operation.equals("Query")) {
			return FormConstants.MARKDOWN_FORM_QUERY;
		} else if (operation.equals("Delete")) {
			return FormConstants.MARKDOWN_LOGIC_DEL_WORD;
		} else if (operation.equals("ConfirmMsg")) {
			return FormConstants.MARKDOWN_LOGIC_CONFIRM_WORD;
		}

		return operation;
	}
}
