package com.tedu.base.initial.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.tedu.base.common.utils.ConstantUtil;
import com.tedu.base.common.utils.ContextUtils;
import com.tedu.base.engine.util.FormConfiguration;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.initial.model.xml.comp.Component;
import com.tedu.base.initial.model.xml.comp.Item;
import com.tedu.base.initial.model.xml.comp.Panel;
import com.tedu.base.initial.model.xml.logic.LogicsModel;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.initial.model.xml.ui.XML;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * 服务器启动时加载所有权限
 * 
 * @author xijianguang
 */
public class LoadComponents extends HttpServlet{
	private static final Logger log = Logger.getLogger(LoadComponents.class);
	private static final long serialVersionUID = 1L;
	public static String[] METHODArr = { "package", "unpackage", "loadData", "getIds", "getOption", "setOption",
			"acceptChanges", "setParam", "loadTransform" };
	public static String CHILDREN = "children";
	public static String METHOD = "method";

	public static String PACKAGE = "package";
	public static String UNPACKAGE = "unpackage";
	public static String SAVEACCEPT = "acceptChanges";
	public static String LOADDATA = "loadData";
	public static String GETIDS = "getids";
	public static String GETOPTION = "getOption";
	public static String SETOPTION = "setOption";
	public static String SETPARAM = "setParam";
	public static String LOADTRANSFORM = "loadTransform";
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}
	
	private void loadLogic(){
		FormLogger.logInit(String.format("开始装载%s", FormConfiguration.XML_LOGIC_PATH), FormLogger.LOG_TYPE_INFO);
		InputStream  ins = null;
		try {
			ins = getClass().getClassLoader().getResourceAsStream(FormConfiguration.XML_LOGIC_PATH);
			LogicsModel logics = new LogicsModel();
			XStream xstream = new XStream();
			xstream.processAnnotations(LogicsModel.class);
			xstream.autodetectAnnotations(true);
			xstream.fromXML(ins,logics);
			ContextUtils.setAttrbute(FormConstants.CONTEXT_ATTRIBUTE_LOGICS, logics);
			FormLogger.logInit(logics.toSummaryString(), FormLogger.LOG_TYPE_INFO);
			FormLogger.logInit(String.format("装载%s条Logic描述", logics.getLogicList().size()), FormLogger.LOG_TYPE_INFO);
		}catch(Exception e){
			FormLogger.logInit(String.format("开始装载%s", FormConfiguration.XML_LOGIC_PATH), FormLogger.LOG_TYPE_ERROR);
		}finally{
			if(ins!=null){
				try {
					ins.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		}
		
	}
	
	public void init() throws ServletException{
		// 需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
		ServletContext servletContext = this.getServletConfig().getServletContext();
		FormLogger.logInit(String.format("开始装载文件%s", FormConfiguration.XML_CPMPONENT_PATH), FormLogger.LOG_TYPE_INFO);
		Component xml = new Component();
		Set<String> functionSet = new HashSet<String>();
		try {
//			FileUtils.readFileToString(new File(filePath), "utf-8");
//			String xmlStr = FormConfiguration
//					.getFile(filePath);
			String xmlStr = 
					IOUtils.toString(getClass().getClassLoader().getResourceAsStream(FormConfiguration.XML_CPMPONENT_PATH),"utf-8");

			functionSet.add(PACKAGE);
			functionSet.add(UNPACKAGE);
			functionSet.add(SAVEACCEPT);
			functionSet.add(LOADDATA);
			functionSet.add(GETIDS);
			functionSet.add(GETOPTION);
			functionSet.add(SETOPTION);
			functionSet.add(SETPARAM);
			functionSet.add(LOADTRANSFORM);
			XStream xstream = new XStream();
			xstream.processAnnotations(Component.class);
			xstream.autodetectAnnotations(true);
			xstream.registerConverter(new Converter() {
				
				@Override
				public boolean canConvert(Class type) {
					return type.equals(Item.class);
				}
				
				@Override
				public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
					Item s = new Item();
					s.setName(reader.getAttribute("name"));
					s.setValue(reader.getAttribute("value"));
					s.setPath(reader.getAttribute("path"));
					s.setItem(reader.getValue());
					return s;
				}
				
				@Override
				public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
					Item status = (Item) source;
					writer.addAttribute("name", status.getName());
					writer.addAttribute("path", status.getPath());
					writer.addAttribute("value", status.getValue());
					writer.setValue(status.getItem());
				}
			});
			xml = (Component) xstream.fromXML(xmlStr);
		} catch (Exception e) {
			FormLogger.logInit(String.format("装载文件%s失败", FormConfiguration.XML_CPMPONENT_PATH), FormLogger.LOG_TYPE_ERROR);
			throw new ServletException();
		}
		FormLogger.logInit(String.format("成功装载文件%s", FormConfiguration.XML_CPMPONENT_PATH), FormLogger.LOG_TYPE_INFO);
		Map<String, Object> componentMap = new HashMap<String, Object>();
		
		List<Panel> panelList = xml.getPanels().getPanelList();
		Map<String, Object> panelMap = panelList.stream().collect(Collectors.toMap(Panel::getName, (p) -> p));
		Map<String, Object> map = new HashMap<String, Object>();
		for (Map.Entry<String, Object> entry : panelMap.entrySet()) {
			Panel p = (Panel) entry.getValue();
			componentMap.put(entry.getKey() + CHILDREN, p.getChildren().getItemList());
			Map<String, String> methodMap = new HashMap<String, String>();
			for (String str : METHODArr) {
				if (p.getMethods().getItemList() != null) {
					Map<String, Object> panelMethodMap = p.getMethods().getItemList().stream()
							.collect(Collectors.toMap(Item::getName, (p2) -> p2));
					if (panelMethodMap.get(str) != null) {
						Item item = (Item) panelMethodMap.get(str);
						methodMap.put(str, item.getValue());
					}
				}
				
			}
			map.put(p.getName(), methodMap);
		}
		componentMap.put(METHOD, map);
		servletContext.setAttribute("componentMap", componentMap);
		FormLogger.logCheckXSD(String.format("开始xsd校验文件[%s]", FormConfiguration.XML_CPMPONENT_PATH), FormLogger.LOG_TYPE_INFO);
		// 1 .xsd校验
//		try {
//			getClass().getClassLoader().getResourceAsStream(FormConfiguration.XSD_CPMPONENT_PATH)
////			FormConfiguration.validateXml(servletContext.getRealPath(FormConfiguration.XSD_CPMPONENT_PATH), servletContext.getRealPath(FormConfiguration.XML_CPMPONENT_PATH));
//		} catch (SAXException | IOException e) {
//			FormLogger.logCheckXSD(String.format("校验xml[%s]数据格式失败", servletContext.getRealPath(FormConfiguration.XML_CPMPONENT_PATH)), FormLogger.LOG_TYPE_ERROR);
//			throw new ServletException();
//		}
		FormLogger.logCheckXSD(String.format("成功xsd校验文件%s", servletContext.getRealPath(FormConfiguration.XML_CPMPONENT_PATH)), FormLogger.LOG_TYPE_INFO);
		// 2.panel内的control类型加载
		List<Item> list = ((Panel)panelMap.get(com.tedu.base.initial.model.xml.ui.Panel.TYPE_TOOLBAR)).getChildren().getItemList();
		Set<String> set = new HashSet<String>();
		for(Item item:list){
			set.add(item.getItem());
		}
		ContextUtils.setAttrbute(ConstantUtil.TOOLBARSET, getSet(panelMap, com.tedu.base.initial.model.xml.ui.Panel.TYPE_TOOLBAR));
		ContextUtils.setAttrbute(ConstantUtil.GROUPSET, getSet(panelMap, com.tedu.base.initial.model.xml.ui.Panel.TYPE_GROUP));
		ContextUtils.setAttrbute(ConstantUtil.GRIDSET, getSet(panelMap, com.tedu.base.initial.model.xml.ui.Panel.TYPE_GRID));
		ContextUtils.setAttrbute(ConstantUtil.TREESET,  getSet(panelMap, com.tedu.base.initial.model.xml.ui.Panel.TYPE_TREE));
		ContextUtils.setAttrbute(ConstantUtil.IMAGESET,  getSet(panelMap, com.tedu.base.initial.model.xml.ui.Panel.TYPE_IMAGE));
		ContextUtils.setAttrbute(ConstantUtil.LINE_CHARTSET,  getSet(panelMap, com.tedu.base.initial.model.xml.ui.Panel.TYPE_LINE_CHART));
		ContextUtils.setAttrbute(ConstantUtil.Gantt_CHARTSET,  getSet(panelMap, com.tedu.base.initial.model.xml.ui.Panel.TYPE_Gantt_CHART));
		ContextUtils.setAttrbute(ConstantUtil.PIE_CHARTSET,  getSet(panelMap, com.tedu.base.initial.model.xml.ui.Panel.TYPE_PIE_CHART));
		ContextUtils.setAttrbute(ConstantUtil.BAR_CHARTSET,  getSet(panelMap, com.tedu.base.initial.model.xml.ui.Panel.TYPE_BAR_CHART));
		ContextUtils.setAttrbute(ConstantUtil.COLUMN_CHARTSET,  getSet(panelMap, com.tedu.base.initial.model.xml.ui.Panel.TYPE_COLUMN_CHART));

		for (Panel p : panelList) {
			FormLogger.logInit(String.format("开始初始化panel%s类型", p.getName()), FormLogger.LOG_TYPE_INFO);
			List<Item> itemList = p.getMethods().getItemList();
			if(itemList!=null&&itemList.size()>0){
				for (Item item : itemList) {
					File f = new File(servletContext.getRealPath(item.getPath()));
					if (!f.exists()) {
						FormLogger.logInit(String.format("启动失败，组件配置文件中%s中对应的%s不存在,文件地址%s", p.getName(), item.getPath(), servletContext.getRealPath(FormConfiguration.XML_CPMPONENT_PATH)),FormLogger.LOG_TYPE_ERROR);
					}
				}
			}
			FormLogger.logInit(String.format("初始化panel类型%s成功", p.getName()), FormLogger.LOG_TYPE_INFO);
		}
		FormLogger.logInit(String.format("总共初始化panel类型%s个", panelList.size()), FormLogger.LOG_TYPE_INFO);
		try {
			XML formXml = FormConfiguration.refreshXML();
			ContextUtils.setAttrbute(ConstantUtil.XML, formXml);
		} catch (Exception e) {
            FormLogger.logUi(String.format("校验业务xml失败异常{%s}",e.getMessage()), FormLogger.LOG_TYPE_WARN);
			throw new ServletException(String.format("校验业务xml失败异常{%s}",e.getMessage()));
		}
		//加载逻辑描述
		loadLogic();
	}

	private Set getSet(Map<String, Object> panelMap, String type){
		List<Item> list = ((Panel)panelMap.get(type)).getChildren().getItemList();
		Set<String> set = new HashSet<String>();
		for(Item item:list){
			set.add(item.getItem());
		}
		return set;
	}

}
