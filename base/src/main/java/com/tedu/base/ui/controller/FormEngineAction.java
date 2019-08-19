package com.tedu.base.ui.controller;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.tedu.base.common.utils.ConstantUtil;
import com.tedu.base.common.utils.ContextUtils;
import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.common.utils.JsonUtil;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.common.utils.TokenUtils;
import com.tedu.base.engine.aspect.ClearLogic;
import com.tedu.base.engine.model.ClientContext;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.TreeNode;
import com.tedu.base.engine.service.FormTokenService;
import com.tedu.base.engine.util.FormConfiguration;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.engine.util.FormUtil;
import com.tedu.base.engine.util.FormValidator;
import com.tedu.base.engine.util.TemplateUtil;
import com.tedu.base.initial.controller.LoadComponents;
import com.tedu.base.initial.model.xml.ui.Control;
import com.tedu.base.initial.model.xml.ui.Flow;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.initial.model.xml.ui.Panel;
import com.tedu.base.initial.model.xml.ui.Param;
import com.tedu.base.initial.model.xml.ui.Procedure;
import com.tedu.base.initial.model.xml.ui.Region;
import com.tedu.base.initial.model.xml.ui.UI;
import com.tedu.base.initial.model.xml.ui.XML;
import com.tedu.base.task.SpringUtils;

@Controller
public class FormEngineAction {
	// 日志记录器
	public final Logger log = Logger.getLogger(this.getClass());
	private static final String TOKEN_CLAUSE = "getToken('%s')";// 从当前ui上下文获取token的语句
	private static final String TAG_GET_TOKEN = "getToken";// 预定义语句这里用于定义从client

	@Value("${ui.dialog.size.small}")
	private String small;
	@Value("${ui.dialog.size.medium}")
	private String medium;
	@Value("${ui.dialog.size.large}")
	private String large;
	@Value("${git.commit.id}")
	private String gitCommitId;
	@Value("${base.app}")
	private String app;
	@Value("${base.ver}")
	private String ver;
	@Value("${git.commit.id}")
	private String commitId;
	@Value("${git.commit.message.full}")
	private String commitMessageFull;
	@Value("${git.commit.message.short}")
	private String commitMessageShort;

	@Value("${git.commit.time}")
	private String commitTime;
	@Value("${git.build.time}")
	private String buildTime;
	@Value("${git.commit.user.name}")
	private String commitUsername;
	@Value("${git.build.user.name}")
	private String buildUsername;
	@Value("${git.commit.user.email}")
	private String commitEmail;
	@Value("${git.build.user.email}")
	private String buildEmail;
	@Value("${timestamp:''}")
	private String timestamp;
	@Value("${git.remote.origin.url}")
	private String url;
	@Value("${git.branch}")
	private String branch;
	@Value("${git.commit.id.describe}")
	private String idDescribe;
	@Value("${git.commit.id.abbrev}")
	private String idAbbrev;

	@Resource
	private FormTokenService formTokenService;
	@Resource
	private TemplateUtil templateUtil;

	// 刷新配置
	@RequestMapping("refreshXML")
	public String refreshXML(HttpServletRequest request, Model model) {
		List<String> msg = new ArrayList<String>();
		FormEngineResponse res = new FormEngineResponse("");
		res.setCode("0");
		try {
			ContextUtils.setAttrbute(ConstantUtil.XML, FormConfiguration.refreshXML());
			msg.add("配置加载成功==>");
			FormValidator.validate();
			msg.add("配置校验完毕，校验详情请查看后台日志");

		} catch (Exception e) {
			SessionUtils.removeAttrbute("xml");
			msg.add("异常:" + e.getMessage());
			msg.add("配置加载失败,请检查!");
			res.setCode("1");
		}
		res.setData(msg);
		model.addAttribute("response", res);
		return "common/formEngineResponseView";
	}

	@RequestMapping("about")
	public String about(HttpServletRequest request, Model model) {
		model.addAttribute("app", app);
		model.addAttribute("ver", ver);
		model.addAttribute("commitId", commitId);
		model.addAttribute("commitMessageFull", commitMessageFull);
		model.addAttribute("commitMessageShort", commitMessageShort);
		model.addAttribute("commitTime", chanceTime(commitTime));
		model.addAttribute("buildTime", chanceTime(buildTime));
		model.addAttribute("commitUsername", commitUsername);
		model.addAttribute("buildUsername", buildUsername);
		model.addAttribute("commitEmail", commitEmail);
		model.addAttribute("buildEmail", buildEmail);
		model.addAttribute("timestamp", timestamp);
		model.addAttribute("url", url);
		model.addAttribute("branch", branch);
		model.addAttribute("idDescribe", idDescribe);
		model.addAttribute("idAbbrev", idAbbrev);
		return "common/about";
	}

	// 修改时间格式
	public String chanceTime(String data) {
		String reg = "(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
		data = data.replaceAll(reg, "$1-$2-$3 $4:$5:$6");
		return data;
	}

	// 模板引擎ftl渲染
	@RequestMapping(value = "/ui/{uiName}")
	public String form(HttpServletRequest request, Model model, @PathVariable String uiName, String EditMode,
			String code, String from, String type) throws Exception {
		// 直接打开或外链打开时,需要从框架运行
//		if (FormConfiguration.isOuterAccess(request)) {// 浏览器直接打开或标明外部打开时
//			return "redirect:/";
//		}
		model.addAttribute("theme", request.getParameter("theme"));
		preLoad(model, uiName);
		loadForm(request, model, uiName, EditMode, code, from, type);
		afterLoad(model, uiName);
		XML xml = (XML) ContextUtils.getAttrbute(ConstantUtil.XML);
		UI ui = xml.getUiByName(uiName);
		if (ui != null && ui.getTemplate() != null && !ui.getTemplate().isEmpty()) {
			FormLogger.logFlow("return custom template:" + ui.getTemplate(), FormLogger.LOG_TYPE_INFO);
			return ui.getTemplate();// 自定义模板
		}
		return "module";
	}

	/**
	 * 可定制模板
	 * 
	 * @param request
	 * @param model
	 * @param uiName
	 * @param EditMode
	 * @param code
	 * @param from
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ue/{uiName}")
	public String form1(HttpServletRequest request, Model model, @PathVariable String uiName, String EditMode,
			String code, String from, String type) throws Exception {
		// 插件方法
		preLoad(model, uiName);
		loadForm(request, model, uiName, EditMode, code, from, type);
		afterLoad(model, uiName);
		return uiName;
	}

	/**
	 * 绘制界面准备工作
	 * 
	 * @param request
	 * @param model
	 * @param uiName
	 * @param EditMode
	 * @param code
	 * @param from
	 * @param type
	 * @throws Exception
	 */
	private void loadForm(HttpServletRequest request, Model model, String uiName, String EditMode, String code,
			String from, String type) throws Exception {
		XML xml = (XML) ContextUtils.getAttrbute(ConstantUtil.XML);
		// 样式主题(动态加载easyui.css)
		Session s = SessionUtils.getSession();
		model.addAttribute("EditMode", EditMode);
		model.addAttribute("from", from);
		model.addAttribute("code", code);
		model.addAttribute("type", type);

		String uiId = TokenUtils.genUUID();
		String parentUiId = request.getParameter(FormConstants.PARENT_UI_ID);
		model.addAttribute(FormConstants.UI_ID, uiId);
		model.addAttribute(FormConstants.PARENT_UI_ID, parentUiId);

		// 这些是解析ui用的公共变量,没有必要在方法间传来传去,一次性构造好
		Map<String, Object> env = new HashMap<String, Object>();
		env.put("ctx", request.getContextPath());
		env.put(FormConstants.AVIATOR_ENV_MODE, EditMode);
		env.put(FormConstants.TAG_UI_NAME, uiName);
		env.put(FormConstants.TAG_SMALL, small);
		env.put(FormConstants.TAG_MEDIUM, medium);
		env.put(FormConstants.TAG_LARGE, large);
		env.put(FormConstants.TAG_Xlarge, "Xlarge");
		env.put(FormConstants.UI_ID, uiId);
		env.put(FormConstants.PARENT_UI_ID, parentUiId);
		env.put("theme", request.getParameter("theme"));

		SessionUtils.setWindowMode(uiId, EditMode);
		UI ui = xml.getUiByName(uiName);
		if (ui == null) {
			FormConfiguration.printError(String.format("名字为%s的ui不存在，请检查xml配置uiname", uiName));
		}
		String back = request.getParameter("back");
		setPageProp(model, ui, back, env);
		// ui tree path
		if (type == null) {
			type = FormConstants.UI_TYPE_MENU_ITEM;// maybe
		}
		// 根据ui请求相关参数构造成当前ui节点对象
		TreeNode uiNode = new TreeNode(uiId, uiName, type, parentUiId);
		formTokenService.initNode(uiNode, model);
		model.addAttribute("initControl",
				FormUtil.getStatements(formTokenService.genControlStatusStatements(uiId, env)));
		FormLogger.logFlow("绘制UI{" + uiName + "}完毕", FormLogger.LOG_TYPE_INFO);
		model.addAttribute("date", DateUtils.getDateToStr("yyyyMMdd", new Date()));
		model.addAttribute("ctx", request.getContextPath());
	}

	// 模板引擎ftl渲染
	/**
	 * @param request
	 * @param model
	 * @param uiName
	 * @param EditMode
	 * @param code
	 * @param from
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "ui/{uiName}/app")
	@ResponseBody
	public FormEngineResponse appForm(HttpServletRequest request, Model model, @PathVariable String uiName,
			String EditMode, String code, String from, String type) {
		FormEngineResponse resObj = new FormEngineResponse("");
		String uiId = TokenUtils.genUUID();// uiId源自UI请求时
		String parentUiId = request.getParameter(FormConstants.PARENT_UI_ID);
		// 这些是解析ui用的公共变量,没有必要在方法间传来传去,一次性构造好
		SessionUtils.setWindowMode(uiId, EditMode);
		// ui tree path
		String uiType = FormConstants.UI_TYPE_MENU_ITEM;
		if (type != null)
			uiType = type;
		// 根据ui请求相关参数构造成当前ui节点对象
		TreeNode uiNode = new TreeNode(uiId, uiName, uiType, parentUiId);
		ClientContext tokenObj = formTokenService.initNode(uiNode, model);
		resObj.setToken(tokenObj);
		return resObj;
	}

	// 弹窗后点击确定关闭弹窗
	private void setPageProp(Model model, UI ui, String back, Map<String, Object> baseEnv) throws Exception {
		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
		ServletContext servletContext = webApplicationContext.getServletContext();
		Map<String, Object> componentMap = (Map<String, Object>) servletContext.getAttribute("componentMap");

		String uiName = ui == null ? "" : ui.getName();

		StringBuffer js = new StringBuffer();
		if (ui == null) {
			FormConfiguration.printError(String.format("名字为%s的ui不存在，请检查xml配置uiname", uiName));
		}
		for (Panel p : ui.getPanelList()) {
			if (p.getType().equalsIgnoreCase(Panel.TYPE_TOOLBAR) || p.getType().equalsIgnoreCase(Panel.TYPE_GROUP))
				for (Control c : p.getControlList()) {
					if (c.getType().equals(Control.TYPE_BUTTON)) {
						if (c.getFlow() != null) {
							c.getFlow().setUiName(uiName);
							c.setAllowed(formTokenService.isAllowed(c.getFlow()));
						} else {
							c.setAllowed(true);
						}
					}
				}
		}
//		calculate(model,ui, xml);

		Map<String, Object> xmlMap = (Map<String, Object>) ContextUtils.getAttrbute(ConstantUtil.XMLMAP);
		Map<String, Object> uiMap = (Map<String, Object>) xmlMap.get(uiName);
		model.addAttribute("hiddenPanel", uiMap.get("hiddenPanel"));
		model.addAttribute("hiddenTable", uiMap.get("hiddenTable"));
		model.addAttribute("validatorRules", uiMap.get("validatorRules"));
		model.addAttribute(FormConstants.TAG_PANEL_PROPERTY, uiMap.get(FormConstants.TAG_PANEL_PROPERTY));

		model.addAttribute("ui", ui);
		// 弹出层是否重新设定高度 只含有南北中
		model.addAttribute("resizeHeight", true);
		for (Region region : ui.getLayout().getRegionList()) {
			if (region.getLocation().equalsIgnoreCase("west") || region.getLocation().equalsIgnoreCase("east")
					|| region.getLocation().equalsIgnoreCase("center")) {
				model.addAttribute("resizeHeight", false);
				break;
			}
		}

		List<Flow> flowList = ui.getFlowList();
		
		String defaultEnterFunction = "";
		String currentFunction = "";
		// 解析js的相关参数
		if (flowList != null && flowList.size() > 0) {
			Map<String, Object> method = (Map<String, Object>) componentMap.get(LoadComponents.METHOD);
			for (Flow flow : flowList) {

				Set<String> procedureSet = new HashSet<String>();
				if (flow != null && flow.getProcedureList() != null && flow.getProcedureList().size() > 0) {
					// onload事件
					if (StringUtils.isEmpty(flow.getTrigger())) {
						js.append("\n$(function(){\n");
						// panel事件方法 uppackage
						if ("back".equalsIgnoreCase(back)) {
							for (Panel p : ui.getPanelList()) {
								if (method.get(p.getType()) != null) {
									Map<String, String> map = (Map<String, String>) method.get(p.getType());
									if (map != null && map.size() != 0)
										js.append("eval(").append(map.get(LoadComponents.UNPACKAGE)).append("('")
												.append(p.getName()).append("'));");
								}
							}
						}
						js.append(flow.getEvent()).append("__").append(ui.getName()).append("();\n");
						js.append("\n});\n");
					}
					List<Procedure> procedureList = flow.getProcedureList();
					// 确保解析每段procedure时不会引起同名环境变量覆盖或继承问题
					Map<String, Object> env = new HashMap<String, Object>();
					env.putAll(baseEnv);// 重构环境变量用于当前
					// onselect
					if("OnSelect".equalsIgnoreCase(flow.getEvent())){
						currentFunction = flow.getTrigger().replace(".", "_")+"_OnSelect();";
						js.append("function "+ flow.getTrigger().replace(".", "_")+"_OnSelect(){\n");
						for (Procedure procedure : procedureList) {
							setMap(procedure, env, procedureSet, uiName, flow);
							js.append(getJs(procedure, env, flow, componentMap, ui));
							env.clear();
							env.putAll(baseEnv);
						}
						js.append(flow.getProcedureList().get(0).getName()).append("();\n");
						js.append("}\n");
					} else {
						currentFunction = flow.getFlowFunctionName(uiName) +"();";
						js.append("//").append(flow.getTrigger()).append(flow.getEvent()).append("开始\n");
						js.append("function ").append(flow.getFlowFunctionName(uiName)).append("(){\n");
						for (Procedure procedure : procedureList) {
							if (!procedureSet.contains(procedure.getName())) {
								setMap(procedure, env, procedureSet, uiName, flow);
								js.append(getJs(procedure, env, flow, componentMap, ui)).append("\n");
								env.clear();
								env.putAll(baseEnv);
							}
						}

						js.append(flow.getProcedureList().get(0).getName()).append("();\n");
						js.append("}\n");
					}
					
					if(flow.isDefaultEnter()){
						defaultEnterFunction = currentFunction;
						model.addAttribute("defaultEnter", defaultEnterFunction);
					}
					
					js.append("//").append(flow.getTrigger()).append(flow.getEvent()).append("结束\n");
				}
			}
		}
		// 所有控制可用性的语句

		model.addAttribute("js", js);//
	}

	// 获取渲染js模板文件
	private String getJs(Procedure procedureInfo, Map<String, Object> map, Flow flow, Map<String, Object> componentMap,
			UI ui) throws Exception {
		String jsFileName = "";
		String logic = procedureInfo.getLogic().toLowerCase();
		if (logic.equals("popup") || logic.equals("transition") || logic.equals("back")) {
			map.put("server", "/ui");
		} else {
			if (ObjectUtils.toString(procedureInfo.getServer()).isEmpty()) {
				map.put("server", "/api/" + procedureInfo.getLogic().toLowerCase());
			}
		}

		map.put("sync", procedureInfo.getSync());
		map.put("trigger", flow.getTrigger());// self(panel.control)
		if ("N".equals(procedureInfo.getSync())) {
			jsFileName = "/" + procedureInfo.getLogic() + "/" + procedureInfo.getLogic() + ".js";
		} else {
			jsFileName = "/" + procedureInfo.getLogic() + "/" + procedureInfo.getLogic() + "_sync.js";
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (procedureInfo.getParamList() != null) {
			for (int i = 0; i < procedureInfo.getParamList().size(); i++) {
				if (procedureInfo.getParamList().get(i).getName().equalsIgnoreCase("window")) {
					// 自定义宽度和高度
					if (map.get(procedureInfo.getParamList().get(i).getValue()) == null&&procedureInfo.getParamList().get(i).getValue()!=null&&procedureInfo.getParamList().get(i).getValue().contains("|")) {
						paramMap.put("windowSize", procedureInfo.getParamList().get(i).getValue().replace("|", ","));
					} else {
						paramMap.put("windowSize", map.get(procedureInfo.getParamList().get(i).getValue()));
					}
				} else {
					paramMap.put(procedureInfo.getParamList().get(i).getName(),
							procedureInfo.getParamList().get(i).getValue());
				}
			}
		}
		//update parameter个别参数需加工
		if(logic.equals("infomsg")){
			Param pMsg = procedureInfo.getParam("Msg");
			if(pMsg!=null && pMsg.isJSExpression()){
				paramMap.put("InterMsg", pMsg.getValue());
			}else{
				paramMap.put("InterMsg","\""+pMsg.getValue()+"\"");
			}
		}
//		参数map部分必须每个 逻辑重新构建，否则可能会导致显示上一逻辑的值
		map.keySet().removeAll(paramMap.keySet());
		map.putAll(paramMap);

		Map<String, Object> method = (Map<String, Object>) componentMap.get(LoadComponents.METHOD);
		Map<String, String> loadData = (Map<String, String>) method.get(procedureInfo.getOutObjectType());
		Set<String> panelSet = ui.getPanelSet();
		Map<String, Object> pMap = procedureInfo.getParamList().stream()
				.collect(Collectors.toMap(Param::getName, (p) -> p));
		switch (procedureInfo.getLogic()) {
		case FormConstants.LOGIC_QUERY:
			if (loadData == null) {
				FormLogger.logFlow(String.format("query 逻辑[%s]没有输出参数，页面可能会出错", procedureInfo.getName()),
						FormLogger.LOG_TYPE_WARN);
				break;
			}
			map.put("loadData", loadData.get(LoadComponents.LOADDATA));
			map.put("setOption", loadData.get(LoadComponents.SETOPTION));
			map.put("getOption", loadData.get(LoadComponents.GETOPTION));
			map.put("setParam", loadData.get(LoadComponents.SETPARAM));
			break;
		case FormConstants.LOGIC_SEARCH:
			if (loadData == null) {
				FormLogger.logFlow(String.format("Search 逻辑[%s]没有输出参数,页面可能会出错", procedureInfo.getName()),
						FormLogger.LOG_TYPE_WARN);
				break;
			}
			map.put("loadData", loadData.get(LoadComponents.LOADDATA));
			map.put("setOption", loadData.get(LoadComponents.SETOPTION));
			map.put("getOption", loadData.get(LoadComponents.GETOPTION));
			map.put("setParam", loadData.get(LoadComponents.SETPARAM));
			break;
		case FormConstants.LOGIC_CLEAR:
			map.put("emptyControlsStatemet",
					ClearLogic.getEmptyControlsStatemet(procedureInfo.getParam(Param.P_OutputPannelId).getValue()));
			if (loadData == null) {
				FormLogger.logFlow(String.format("Clear 逻辑[%s]没有输出参数，页面可能会出错", procedureInfo.getName()),
						FormLogger.LOG_TYPE_WARN);
				break;
			}
			map.put("loadData", loadData.get(LoadComponents.LOADDATA));
			map.put("setOption", loadData.get(LoadComponents.SETOPTION));
			map.put("loadData", loadData.get(LoadComponents.LOADDATA));
			break;
		case FormConstants.LOGIC_SAVE:
			StringBuffer sbaccept = new StringBuffer();
			for (String type : panelSet) {
				Map<String, String> panelmap = (Map<String, String>) method.get(type);
				if (!StringUtils.isEmpty(panelmap.get(LoadComponents.SAVEACCEPT)))
					sbaccept.append(panelmap.get(LoadComponents.SAVEACCEPT)).append("(out);");
			}
			map.put(LoadComponents.SAVEACCEPT, sbaccept);
			break;
		case "Transition":
			StringBuffer sb = new StringBuffer();
			for (String type : panelSet) {
				Map<String, String> panelmap = (Map<String, String>) method.get(type);
				if (!StringUtils.isEmpty(panelmap.get(LoadComponents.PACKAGE)))
					sb.append(panelmap.get(LoadComponents.PACKAGE)).append("();");
			}
			map.put(LoadComponents.PACKAGE, sb);
			XML xml = (XML) ContextUtils.getAttrbute(ConstantUtil.XML);
			map.put("ToTitle", StringUtils.isEmpty(xml.getUI(pMap.get("To").toString())) ? ""
					: xml.getUI(pMap.get("To").toString()).getTitle());
			break;
		case "Popup":
			if (procedureInfo.getParamList() != null) {
				if (pMap.get("Title") == null) {
					String mode = pMap.get("Mode").toString();
					if ("add".equalsIgnoreCase(mode)) {
						map.put("Title", "新增" + ui.getTitle());
					} else if ("edit".equalsIgnoreCase(mode)) {
						map.put("Title", "编辑" + ui.getTitle());
					} else if ("readonly".equalsIgnoreCase(mode)) {
						map.put("Title", "查看" + ui.getTitle());
					}
				} else {
					map.put("Title", pMap.get("Title").toString());
				}
			}
			break;
		case "Export":
			String Format = String.valueOf(pMap.get("Format"));
			List<Control> cList = null;
			List<Panel> pList = ui.getPanelList();
			for (Panel p : pList) {
				if (Format.equals(p.getName())) {
					cList = p.getControlList();
					break;
				}
			}
			map.put("cList", JsonUtil.listToJson(cList));
			break;
		case FormConstants.LOGIC_TRANSFORM:
			if (loadData == null) {
				FormLogger.logFlow(String.format("loadTransform 逻辑[%s]没有输出参数，页面可能会出错", procedureInfo.getName()),
						FormLogger.LOG_TYPE_WARN);
				break;
			}
			map.put(LoadComponents.LOADTRANSFORM, loadData.get(LoadComponents.LOADTRANSFORM));
			break;
		default:
			break;
		}
		if (procedureInfo.getIfyesProcedure() != null) {
			map.put("ifyes", procedureInfo.getIfyes() + "();");
		} else {
			if (StringUtils.isEmpty(flow.getTrigger())) {
				map.put("ifyes", "loaded();");
			} else {
				map.put("ifyes", "");
			}
		}

		if (procedureInfo.getIfnoProcedure() != null) {
			map.put("ifno", procedureInfo.getIfno() + "();");
		} else {
			if (StringUtils.isEmpty(flow.getTrigger())) {
				map.put("ifno", "loaded();");
			} else {
				map.put("ifno", "");
			}
		}
		map.put("isAntiDup", formTokenService.isAntidupLogic(procedureInfo.getLogic()) ? "true" : "false");// 防重复提交逻辑在前端使用token时和普通逻辑有差异
		String result = new String();
		try {
			result = templateUtil.getContent(jsFileName, map);
		} catch (Exception e) {
			FormConfiguration.printError("模板解析出错！");
		}
		return result;
	}

	/**
	 * 获取屏幕宽度 screen 自定义window显示百分比格式50%|60%
	 */
	public String GetScreenSize(String screen) {
		String[] size = screen.split("%");
		String width = size[0];
		String height = size[1].replace("|", "");
		/*
		 * Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); int
		 * screenWidth = (int) screenSize.getWidth(); int screenHeight = (int)
		 * screenSize.getHeight();
		 */
		Toolkit tk = Toolkit.getDefaultToolkit();
		int screenWidth = tk.getScreenSize().width; // 返回宽度
		int screenHeight = tk.getScreenSize().height;// 高度
		screenWidth = screenWidth - screenWidth * 15 / 100;
		screenHeight = screenHeight - screenHeight * 25 / 100;
		return screenWidth * Integer.parseInt(width) / 100 + "|" + screenHeight * Integer.parseInt(height) / 100;
	}

	public void findXml(String path, List<String> xmlList) {
		File file = new File(path);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				return;
			} else {
				for (File file2 : files) {
					if (file2.isDirectory()) {
						findXml(file2.getAbsolutePath(), xmlList);
					} else {
						xmlList.add(file2.getAbsolutePath());
					}
				}
			}
		} else {
			System.out.println("文件不存在!");
		}
	}

	private void setMap(Procedure procedure, Map<String, Object> map, Set<String> procedureSet, String uiName,
			Flow flow) {
		procedureSet.add(procedure.getName());
		String functionId = flow.getProcedureFunctionId(uiName, procedure.getName());
		map.put("server", procedure.getServer());
		map.put("name", procedure.getName());
		map.put(FormConstants.TAG_FUNCTION_ID, functionId);
		map.put(TAG_GET_TOKEN, String.format(TOKEN_CLAUSE, functionId));// 生成当前function中的获取token语句
		map.put(FormConstants.TAG_LOGIC, procedure.getLogic());
		map.put(FormConstants.TAG_LOG_BEGIN_LOGIC, String.format("console.log('%s 逻辑开始');", procedure.getLogic()));
	}

	/**
	 * 从项目的projectService中加载对应ui的方法
	 * 
	 * @param model
	 * @param uiName
	 */
	private void preLoad(Model model, String uiName) {
		Object objService = null;
		String methodBeforeName = "before" + uiName;
		try {
			objService = SpringUtils.getBean("projectService");// 固定加载项目中的服务类projectService,在其中按命名规则实现ue方法的前后置处理
			if (objService != null) {
				MethodUtils.invokeMethod(objService, methodBeforeName, model);
			}
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {

		} catch (Exception e) {
			FormLogger.logFlow("customized ui content preLoad: " + e.getMessage(), FormLogger.LOG_TYPE_ERROR);
		}
	}

	private void afterLoad(Model model, String uiName) {
		Object objService = null;
		String methodAfterName = "after" + uiName;
		try {
			objService = SpringUtils.getBean("projectService");// 固定加载项目中的服务类projectService,在其中按命名规则实现ue方法的前后置处理
			if (objService != null) {
				MethodUtils.invokeMethod(objService, methodAfterName, model);
			}
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
		} catch (Exception e) {
			FormLogger.logFlow("customized ui content afterLoad: " + e.getMessage(), FormLogger.LOG_TYPE_ERROR);
		}
	}
}
