package com.tedu.base.engine.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.http.client.utils.CloneUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.xml.sax.SAXException;

import com.googlecode.aviator.AviatorEvaluator;
import com.tedu.base.common.utils.ConstantUtil;
import com.tedu.base.common.utils.ContextUtils;
import com.tedu.base.engine.model.CmpObjectPropertyName;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.model.ServerTokenData;
import com.tedu.base.initial.model.xml.ui.Control;
import com.tedu.base.initial.model.xml.ui.Flow;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.initial.model.xml.ui.ModelLayer;
import com.tedu.base.initial.model.xml.ui.ModuleObject;
import com.tedu.base.initial.model.xml.ui.Panel;
import com.tedu.base.initial.model.xml.ui.Param;
import com.tedu.base.initial.model.xml.ui.Procedure;
import com.tedu.base.initial.model.xml.ui.Property;
import com.tedu.base.initial.model.xml.ui.Region;
import com.tedu.base.initial.model.xml.ui.Subregion;
import com.tedu.base.initial.model.xml.ui.UI;
import com.tedu.base.initial.model.xml.ui.UiLayer;
import com.tedu.base.initial.model.xml.ui.XML;
import com.tedu.base.rule.AviatorUtil;
import com.thoughtworks.xstream.XStream;
/**
 * panel，model部分配置
 * 用于后台校验、保存等逻辑
 * @author wangdanfeng
 *
 */
public class FormConfiguration {
	public static final Logger log = Logger.getLogger(FormConfiguration.class);
	public static CmpObjectPropertyName propertyNameComparator = new CmpObjectPropertyName();
	public static ModuleObject model = new ModuleObject();
	public static ModuleObject getModel(String uiName,String modelName){
		return ContextUtils.getXML().getModel_layer().getModel(modelName);
	}
	public static String XML_BUSINESS_PATH="engine/form";
	public static String XSD_BUSINESS_PATH="engine/form/componentConfig.xsd";
	public static String XSD_CPMPONENT_PATH="/engine/form/componentListConfig.xsd";
	public static String XML_CPMPONENT_PATH="/engine/form/componentsList.xml";
	public static String XML_LOGIC_PATH="/engine/form/logicList.xml";
	/**
	 * 目前只能自己构造demo配置数据
	 * 未来从工具类获取
	 * 20170807 从内存中获取实际panel对象
	 * panel描述
	 * @param uiName
	 */
	public static Panel getPanel(String uiName,String panelName){
		return ContextUtils.getXML().getPanel(uiName, panelName);
	}

	/**
	 * 不一定和session相关，未来配置存放应单独用相关类管理
	 * @param uiName
	 * @return
	 */
	public static List<Flow> getFlow(String uiName){
		return ContextUtils.getXML().getFlow(uiName);
	}
	
	public static UI getUI(String uiName){
		return ContextUtils.getXML().getUI(uiName);
	}
	/**
	 * 根据token阿虎局找到procedure，相关参数可确定
	 * @param tokenData 生成时已将所属Procedure对象设置到属性中。不需要再遍历配置获取
	 * @return
	 */
	public static Procedure getProcedure(ServerTokenData tokenData){
		if(tokenData==null) return null;
		return tokenData.getProcedure();
	}
	/**
	 * 从controlList中去掉没有property的control
	 * 有property，但是property不存在propertyList的抛出非法表单异常
	 * 有property,但是property未绑定field的去掉
	 * @param cList
	 * @param pList
	 */
	public static void filterSqlField(List<Control> cList,List<Property> pList){
		Collections.sort(pList,propertyNameComparator);
		//先把pane组件list中没绑定属性的排除掉
        CollectionUtils.filter(cList, new Predicate(){
            @Override
            public boolean evaluate(Object o) {
            	String pName = ObjectUtils.toString(((Control)o).getProperty()); 
            	//过滤掉未绑定属性的组件
            	if(pName.isEmpty()){
            		return false;
            	}
            	int cPropertyIndex = indexOfPropertyByName(pList,new Property(pName));
            	if(cPropertyIndex < 0) {
            		FormLogger.logFlow(String.format("Panel{%s}的组件{%s}绑定的属性未找到", pName,((Control)o).getName())
            				,FormLogger.LOG_TYPE_DEBUG);
            		return false;
            	}
            	Property p = pList.get(cPropertyIndex);
            	if(p==null || p.getField().isEmpty()) {
            		FormLogger.logFlow(String.format("忽略没有绑定Field的Property{%s}", pName), FormLogger.LOG_TYPE_DEBUG);
            		return true;
            	}
        		//将属性对象设置到control对象方便存取
        		((Control)o).setPropertyObj(p);//将属性对象绑定到control对象
        		return true;
            }
        });
        //移除field是空的property
        CollectionUtils.filter(pList, new Predicate(){
            @Override
            public boolean evaluate(Object o) {
            	return !(((Property)o).getField().isEmpty());
         }});
	}
	
	/**
	 * 按属性名从属性列表中检索指定属性对象
	 * @param pList
	 * @param searchObj
	 * @return
	 */
	public static int indexOfPropertyByName(List<Property> pList,Property searchObj){
		Collections.sort(pList,propertyNameComparator);
		int index = Collections.binarySearch(pList, searchObj, propertyNameComparator);
     	if(index < 0) {
    		FormLogger.logFlow(String.format("找不到组件绑定的属性{%s}",searchObj.getName()),FormLogger.LOG_TYPE_DEBUG);
    	}
		 return index;
	}
	
	/**
	 * 遍历mapData,根据key找到同名的property，再找到绑定此属性的control，构造controlName:value
	 * 用于querybyid后的表单初始化
	 * @param uiName
	 * @param panelName
	 * @param data
	 * @return
	 */
	public static Map<String,Object> getControlModel(FormModel formModel,Map<String,Object> data){
		List<Control> cList = formModel.getPanel().getControlList();//这个才是全集
		Map<String,Object> mapRet = new HashMap<String,Object>();//加工后的返回对象
		//以control为准，有property属性的，根据propertyName从data中获取值，只要不是null就put到返回对象
     	for(Control c:cList){
     		String pName = ObjectUtils.toString(c.getProperty());
     		if(!pName.isEmpty() && data.containsKey(pName)){
     			mapRet.put(c.getName(), data.get(pName));
     		}else{
     			FormLogger.debug("querybyid返回数据中没有" + pName + "键");
     		}
     	}
		return mapRet;
	}
	
	/**
	 * 设置source的control属性由source表达式的返回值决定
	 * @param formModel
	 * @param data
	 * @return
	 */
	public static void setControlDefault(FormModel formModel,Map<String,Object> env,Map<String,Object> modelMap
			,String source){
		if(formModel == null) return;//do nothing
		

		//panel和model配置对象
		List<Control> cList = formModel.getControlList();
		List<Control> expControlList = new ArrayList<>();

		String initial;
		try {
			Map<String,Object> currentEnv = (Map<String,Object>)CloneUtils.clone(env);
			AviatorUtil.prepareEnv(currentEnv,formModel);
			
			for(Control c:cList){
				initial = ObjectUtils.toString((MethodUtils.invokeMethod(c, source,null)));
				if(initial.isEmpty()){
					continue;
				}
				expControlList.add(c);
			}
			int size = expControlList.size();//有配置表达式的组件数
			if(size == 0) return;
			FormLogger.logFlow(String.format("扫描%s标签成功 [发现%s个表达式]", source,expControlList.size()), FormLogger.LOG_TYPE_INFO);
			for(Control c:expControlList){
				initial = ObjectUtils.toString((MethodUtils.invokeMethod(c, source,null)));
				Object result = AviatorEvaluator.execute(initial,currentEnv,true);
				modelMap.put(c.getName(), result);
			}
		} catch (Exception e) {
			FormLogger.logFlow(String.format("解析{get%s}表达式异常 %s", source,e.getMessage()), FormLogger.LOG_TYPE_INFO);
		}
	}
	
	/**
	 * 根据trigger找到对应panel下的control
	 * trigger:
	 * 1.empty;
	 * 2.panelName.controlName
	 * 3.panelName
	 * @param formModel
	 * @param trigger
	 * @return
	 */
	public static Control getFlowTriggerControl(String uiName,String trigger){
		if(trigger.isEmpty()) return null;
		String[] arr = trigger.split("\\.");
		String panelName = arr[0];
		if(arr.length==1) {
			FormLogger.debug(trigger + "是panel事件，不参与过滤权限");
			return null;
		}
		String controlName = arr[1];
		//这里不是提交表单的panel,而是当前ui下所有panel里名称=当前trigger所属panel的
		Control  flowControl = null;
		Panel panel = getUI(uiName).getUiPanel(panelName);
		if(panel == null){
			FormLogger.logConf(uiName + "的panel不存在,请检查配置", panelName);
		}else{
			flowControl = panel.getControl(controlName);
		}
		return flowControl;
	}
	
    /**
     * xml校验语法
     *
     * @param xsdPath
     * @param xmlPath
     *
     * @return
     */
    public static boolean validateXml(String xsdPath, String xmlPath) throws SAXException, IOException {
        // 建立schema工厂
        SchemaFactory schemaFactory = SchemaFactory
                .newInstance("http://www.w3.org/2001/XMLSchema");
        // 建立验证文档文件对象，利用此文件对象所封装的文件进行schema验证
        File schemaFile = new File(xsdPath);
        // 利用schema工厂，接收验证文档文件对象生成Schema对象
        Schema schema = schemaFactory.newSchema(schemaFile);
        
        // 通过Schema产生针对于此Schema的验证器，利用schenaFile进行验证
        Validator validator = schema.newValidator();
        // 得到验证的数据源
        Source source = new StreamSource(new File(xmlPath));
        // 开始验证，成功输出success!!!，失败输出fail
        validator.validate(source);

        return true;
    }

    /**
     * xml校验逻辑
     *
     * @throws IOException
     */
    public static void validateXmlLogic(XML xml) throws ServletException {
        // 1.唯一性校验
        // model_layer内Object标签name属性验证
        List<ModuleObject> moduleObjectList = xml.getModel_layer().getModuleObjectList();
        Set<String> objectNameSet = new HashSet<String>();
        Map<String, Set<String>> objectMap = new HashMap<String, Set<String>>();
        FormLogger.logModule("开始校验module", FormLogger.LOG_TYPE_INFO);
        for (ModuleObject m : moduleObjectList) {
        	FormLogger.logModule(String.format("开始校验module[%s]",m.getName()), FormLogger.LOG_TYPE_INFO);
            if (objectNameSet.contains(m.getName())) {
            	printError(String.format("校验失败.名字为  [%s] 的Object标签已经存在了",m.getName()));
            }
            objectNameSet.add(m.getName());
            // Object内Property标签name属性验证
            Set<String> propertyNameSet = new HashSet<String>();
            List<Property> propertyList = m.getPropertyList();
            FormLogger.logModule(String.format("开始校验module[%s]内的property",m.getName()), FormLogger.LOG_TYPE_INFO);
            for (Property p : propertyList) {
                if (propertyNameSet.contains(p.getName())) {
                    printError(String.format("校验失败.名字为  [%s]的module内的name=[%s]的property已经存在了", m.getName(), p.getName()));
                }
                propertyNameSet.add(p.getName());
            }
            FormLogger.logModule(String.format("校验module[%s]内的property成功总共校验了[%s]个property",m.getName(), propertyList.size()), FormLogger.LOG_TYPE_INFO);
            objectMap.put(m.getName(), propertyNameSet);
            FormLogger.logModule(String.format("校验module[%s]成功",m.getName()), FormLogger.LOG_TYPE_INFO);
        }
        FormLogger.logModule(String.format("校验module成功总共校验了%s个module对象",moduleObjectList.size()), FormLogger.LOG_TYPE_INFO);
        FormLogger.logUi("开始校验ui", FormLogger.LOG_TYPE_INFO);
        Map<String, Set<String>> flowMap = new HashMap<String, Set<String>>();
        List<UI> uiList = xml.getUi_layer().getUiList();
		Map<String, Object> uiMap = uiList.stream().collect(Collectors.toMap(UI::getName, (p1) -> p1));
        Set<String> uiNameSet = new HashSet<String>();
        for (UI ui : uiList) {
        	FormLogger.logUi(String.format("开始校验ui[%s]",ui.getName()), FormLogger.LOG_TYPE_INFO);
            if (uiNameSet.contains(ui.getName())) {
                printError(String.format("校验失败.ui标签[%s]name重复", ui.getName()));
            }
            uiNameSet.add(ui.getName());
            // ui内panel标签name属性验证
            List<Panel> panelList = ui.getPanelList();
			Map<String, Object> panelMap = panelList.stream().collect(Collectors.toMap(Panel::getName, (p) -> p));
            Set<String> panelNameSet = new HashSet<String>();
            FormLogger.logPanel(String.format("开始校验ui[%s]内的panel",ui.getName()));
            for (Panel panel : panelList) {
            	FormLogger.logPanel(String.format("开始校验的panel[%s]", panel.getName()));
                if (panelNameSet.contains(panel.getName())) {
                    printError(String.format("校验失败.名字为  [%s]的ui内的name=[%s]的panel已经存在了", ui.getName(), panel.getName()));
                }
                if (!StringUtils.isEmpty(panel.getObject()) && objectMap.get(panel.getObject()) == null) {
                	printError(String.format("panel内object[%s]不存在", ObjectUtils.toString(panel.getObject())));
                }
                panelNameSet.add(panel.getName());
                // panel内Control标签name属性验证
                List<Control> controlList = panel.getControlList();
                if(controlList==null){
                	FormLogger.logControl(panel.getName()+"没有control", FormLogger.LOG_TYPE_ERROR);
                }
                Set<String> controlNameSet = new HashSet<String>();
                Set<String> controlPropertySet = new HashSet<String>();
                FormLogger.logControl(String.format("开始校验control"), FormLogger.LOG_TYPE_INFO);
                for (Control control : controlList) {
                	FormLogger.logControl(String.format("开始校验control[%s]", control.getName()), FormLogger.LOG_TYPE_INFO);
                    if (!StringUtils.isEmpty(control.getProperty()) && !StringUtils.isEmpty(panel.getObject())
                            && !objectMap.get(panel.getObject()).contains(control.getProperty())) {
                        printError(String.format("%s panel[%s]的object[%s]不存在",ui.getName(), panel.getName(), control.getProperty()));
                    }
                    if (controlNameSet.contains(control.getName())) {
                    	printError(String.format("校验失败.名字为  [%s]的ui内的name=[%s]的panel内的name=[%s]的control已经存在了", ui.getName(), panel.getName(), control.getName()));
                    }
                    if (!StringUtils.isEmpty(control.getProperty())&&controlPropertySet.contains(control.getProperty())) {
                    	FormLogger.logControl(String.format("ui[%s]内panel[%s]control属性property[%s]重复可能会造成control无法显示等问题", ui.getName(), panel.getName(), control.getProperty()), FormLogger.LOG_TYPE_WARN);
                    }
                    if (panel.getType().equalsIgnoreCase(Panel.TYPE_TOOLBAR)) {
                        if (!((Set) ContextUtils.getAttrbute(ConstantUtil.TOOLBARSET)).contains(control.getType())) {
                            printError(String.format("[%s]中的toolbar类型panel不包括[%s]类型", ui.getName(), control.getType()));
                        }
                    }
                    if (panel.getType().equalsIgnoreCase(Panel.TYPE_GROUP)) {
                        if (!((Set) ContextUtils.getAttrbute(ConstantUtil.GROUPSET)).contains(control.getType())) {
                            printError(String.format("[%s]中的Group类型panel不包括[%s]类型", ui.getName(), control.getType()));
                        }
                    }
                    if (panel.getType().equalsIgnoreCase(Panel.TYPE_TREE)) {
                        if (!((Set) ContextUtils.getAttrbute(ConstantUtil.TREESET)).contains(control.getType())) {
                            printError(String.format("[%s]中的tree类型panel不包括[%s]类型", ui.getName(), control.getType()));
                        }
                    }
                    if (panel.getType().equalsIgnoreCase(Panel.TYPE_GRID)) {
                        if (!((Set) ContextUtils.getAttrbute(ConstantUtil.GRIDSET)).contains(control.getType())) {
                            printError(String.format("[%s]中的grid类型panel不包括[%s]类型", ui.getName(), control.getType()));
                        } 
                    }
                    FormLogger.logControl(String.format("校验control[%s]成功",ui.getName(),panel.getName(),control.getName()), FormLogger.LOG_TYPE_INFO);
                    controlNameSet.add(control.getName());
                    controlPropertySet.add(control.getProperty());
                }
                FormLogger.logPanel(String.format("校验control成功总共校验了[%s]个control",controlList.size()));
            }
            FormLogger.logModule(String.format("校验panel成功总共校验了[%s]个panel",panelList.size()), FormLogger.LOG_TYPE_INFO);
            // ui内flow标签trigger+event属性验证
            List<Flow> flowList = ui.getFlowList();
            Set<String> flowSet = new HashSet<String>();
            Set<String> triggerSet = new HashSet<String>();
            if (flowList != null && flowList.size() > 0){
            	FormLogger.logFlow(String.format("开始校验flow"), FormLogger.LOG_TYPE_INFO);
            	for (Flow flow : flowList) {
            		FormLogger.logFlow(String.format("开始校验flow[%s%s]",flow.getTrigger(), flow.getEvent()), FormLogger.LOG_TYPE_INFO);
            		if (flowSet.contains(flow.getTrigger() + " " + flow.getEvent())) {
            			printError(String.format("校验失败.ui标签%s内Flow标签trigger+event重复", ui.getName()));
            		}
            		flowSet.add(flow.getTrigger() + " " + flow.getEvent());
            		if (StringUtils.isEmpty(flow.getTrigger()))
            			triggerSet.add(flow.getEvent());
            		else
            			triggerSet.add(flow.getTrigger());
            		// flow内procedure标签name属性验证
					if(!StringUtils.isEmpty(flow.getTrigger())&&!panelMap.containsKey(flow.getTrigger().split("\\.")[0])){
		                FormLogger.logUi(String.format("ui[%s]中的flow[%s]不存在对应的绑定control或者panel",ui.getName(), flow.getTrigger()), FormLogger.LOG_TYPE_WARN);
					}
            		List<Procedure> procedureList = flow.getProcedureList();
            		Set<String> procedureNameSet = new HashSet<String>();
            		if(procedureList!=null){
            			FormLogger.logProcedure(String.format("开始校验内的procedure"), FormLogger.LOG_TYPE_INFO);
            			for (Procedure procedure : procedureList) {
            				//批量导入校验
            				if(procedure.getName().equals(FormConstants.LOGIC_BATCHIMPORT)){
            					List<Param> paramList = procedure.getParamList();
            					if(paramList==null){
            						printError(String.format("校验失败.ui[%s]内部有BatchImport，参数不能为空", ui.getName()));
            					}
            					Map<String, Object> paramMap = paramList.stream().collect(Collectors.toMap(Param::getName, (p) -> p));
            					String inparam = String.valueOf(paramMap.get(Param.P_InputPanelId));
            					if(inparam.split("\\.").length==2){
            						if(!uiMap.containsKey(inparam.split("\\.")[0])){
            							printError(String.format("校验失败.ui[%s]内部有BatchImport，参数错误[%s]不是有效的ui", ui.getName(), inparam.split("\\.")[0]));
            						}
            						if(!panelMap.containsKey(inparam.split("\\.")[1])){
            							printError(String.format("校验失败.ui[%s]内部有BatchImport，参数错误[%s]不是有效的panel", ui.getName(), inparam.split("\\.")[1]));
            						}
            					} else{
            						printError(String.format("校验失败.ui[%s]内部有BatchImport，参数格式错误，正确格式应该是ui.panel", ui.getName()));
            					}
            				}
            				FormLogger.logProcedure(String.format("开始校验procedure[%s]", procedure.getName()), FormLogger.LOG_TYPE_INFO);
            				if (procedureNameSet.contains(procedure.getName())) {
            					printError("校验失败.flow标签内procedure标签name重复");
            				}
            				procedureNameSet.add(procedure.getName());
            				// Procedure内param标签name属性验证
            				List<Param> paramList = procedure.getParamList();
            				Set<String> paramNameSet = new HashSet<String>();
            				if (paramList != null){
            					if(!StringUtils.isEmpty(flow.getTrigger())&&
            							panelMap.containsKey(flow.getTrigger().split("\\.")[0])&&
            							flow.getTrigger().split("\\.").length==2){
            						Panel p = (Panel) panelMap.get(flow.getTrigger().split("\\.")[0]);
            						Map<String, Object> controlMap = p.getControlList().stream().collect(Collectors.toMap(Control::getName, (p1) -> p1));
            						if(controlMap.containsKey(flow.getTrigger().split("\\.")[0])){
                						for (Param param : paramList) {
                							if (paramNameSet.contains(param.getName())) {
                								printError(String.format("校验失败.procedure标签%s内param标签name重复", procedure.getName()));
                							}
                							if(Param.P_TO.equals(param.getName())){
                								if(!uiMap.containsKey(param.getValue())){
                									printError(String.format("校验失败.[%s]ui中的procedure[%s]的To参数必须对应一个有效的ui", ui.getName(),procedure.getName()));
                								}
                							}
                							if(Param.P_InputPanelId.equals(param.getName()) || Param.P_OutputPannelId.equals(param.getName())){
                								
                								if(!StringUtils.isEmpty(param.getValue())&&!panelMap.containsKey(param.getValue().split("\\.")[0])&&
                										!FormConstants.LOGIC_BRANCH.equals(procedure.getLogic())&&
                										!FormConstants.LOGIC_SAVE.equals(procedure.getLogic())&&
                										!FormConstants.LOGIC_BATCHIMPORT.equals(procedure.getLogic())&&
                										!FormConstants.LOGIC_EXPFORMAT.equals(procedure.getLogic())){
                									printError(String.format("校验失败.[%s]ui中的procedure[%s]的[%s]参数[%s]必须对应一个有效的panel", ui.getName(),procedure.getName(),param.getName(),param.getValue().split("\\.")[0]));
                								}
                								if(!StringUtils.isEmpty(param.getValue())&&param.getValue().split("\\.").length==2&&
                										!FormConstants.LOGIC_BRANCH.equals(procedure.getLogic())&&
                										!FormConstants.LOGIC_SAVE.equals(procedure.getLogic())&&
                										!FormConstants.LOGIC_BATCHIMPORT.equals(procedure.getLogic())&&
                										!FormConstants.LOGIC_EXPFORMAT.equals(procedure.getLogic())){
                									if(!controlMap.containsKey(param.getValue().split("\\.")[1])&&!Panel.TYPE_GRID.equals(p.getType())){
                										printError(String.format("校验失败.[%s]ui中的procedure[%s]的[%s]参数[%s]必须对应一个有效的panel.control", ui.getName(),procedure.getName(),param.getName(),param.getValue().split("\\.")[0]));
                									}
                								}
                							}
                							paramNameSet.add(param.getName());
                						}
                					
            						} else {
            							FormLogger.logUi(String.format("ui[%s]中的flow[%s]不存在对应的绑定control或者panel",ui.getName(), flow.getTrigger()), FormLogger.LOG_TYPE_WARN);
            						}
            					}else if(flow.getTrigger().split("\\.").length>2){
            						printError(String.format("校验失败.[%s]ui中的flow trigger[%s]格式错误", ui.getName(),flow.getTrigger()));
            					}
            				}
            				FormLogger.logProcedure(String.format("校验procedure[%s]成功", procedure.getName()), FormLogger.LOG_TYPE_INFO);
            			}
            			FormLogger.logProcedure(String.format("校验procedure成功，总共校验了[%s]个procedure",procedureList.size()), FormLogger.LOG_TYPE_INFO);
            		}
            		FormLogger.logFlow(String.format("校验flow[%s%s]成功",flow.getTrigger(), flow.getEvent()), FormLogger.LOG_TYPE_INFO);
            	}
            	FormLogger.logFlow(String.format("校验flow成功总共有[%s]个flow",flowList.size()), FormLogger.LOG_TYPE_INFO);
            }
            flowMap.put(ui.getName(), triggerSet);
            FormLogger.logUi(String.format("成功校验ui[%s]",ui.getName()), FormLogger.LOG_TYPE_INFO);
        }
        FormLogger.logUi(String.format("成功校验ui,一共有[%s]个ui",uiList.size()), FormLogger.LOG_TYPE_INFO);
        FormLogger.logUi(String.format("开始校验ui的组件和对应的flow关系"), FormLogger.LOG_TYPE_INFO);
        for (UI ui : uiList) {
        	FormLogger.logUi(String.format("开始校验ui[%s]的组件和对应的flow关系",ui.getName()), FormLogger.LOG_TYPE_INFO);
            if (flowMap.get(ui.getName())!=null && !flowMap.get(ui.getName()).contains(FormConstants.EVENT_ONLOAD)) {
                FormLogger.logUi(ui.getName() + " ui 没有对应的onLoad事件", FormLogger.LOG_TYPE_INFO);
            }
        	List<Panel> panelList = ui.getPanelList();
        	for (Panel panel : panelList) {
        		List<Control> controlList = panel.getControlList();
        		for (Control control : controlList) {
        			if (Control.TYPE_BUTTON.equalsIgnoreCase(control.getType()) || Control.TYPE_POPBOX.equalsIgnoreCase(control.getType())) {
				          if (flowMap.get(ui.getName())!=null && !flowMap.get(ui.getName()).contains(panel.getName() + "." + control.getName())) {
				              FormLogger.logFlow(String.format("[%s][%s]没有对应的事件", ui.getName(),control.getName()), FormLogger.LOG_TYPE_WARN);
				          }
        			}
        		}
        	}
        	FormLogger.logUi(String.format("完成校验ui[%s]的组件和对应的flow关系",ui.getName()), FormLogger.LOG_TYPE_INFO);
        }
        FormLogger.logUi(String.format("成功校验共有[%s]个ui的组件和对应的flow关系", uiList.size()), FormLogger.LOG_TYPE_INFO);
        // 3.存在性校验
        for (UI ui : uiList) {
        	FormLogger.logUi(String.format("开始校验ui[%s]内的flow内的逻辑",ui.getName()), FormLogger.LOG_TYPE_INFO);
            // 4.合理性校验
            List<Flow> flowList = ui.getFlowList();
            Map<String, Set<String>> ifnoMap = new HashMap<String, Set<String>>();
            Map<String, Set<String>> ifyesMap = new HashMap<String, Set<String>>();
            if (flowList != null && flowList.size() > 0)
                for (Flow flow : flowList) {
                    Set<String> ifyesSet = new HashSet<String>();
                    Set<String> ifnoSet = new HashSet<String>();
                    List<Procedure> procedureList = flow.getProcedureList();
                    if(procedureList!=null){
                    	for (Procedure procedure : procedureList) {
                    		ifyesSet.add(procedure.getIfyes());
                    		ifnoSet.add(procedure.getIfno());
                    	}
                    	ifnoMap.put(flow.getTrigger() + flow.getEvent(), ifnoSet);
                    	ifyesMap.put(flow.getTrigger() + flow.getEvent(), ifyesSet);
                    }

                }
            if (flowList != null && flowList.size() > 0)
                for (Flow flow : flowList) {
                	FormLogger.logUi(String.format("开始校验ui[%s]内的flow[%s%s]内的逻辑",ui.getName(),flow.getTrigger(),flow.getEvent()), FormLogger.LOG_TYPE_INFO);
                    List<Procedure> procedureList = flow.getProcedureList();
                    if(procedureList!=null){
                    	for (Procedure procedure : procedureList) {
                    		if (!ifnoMap.get(flow.getTrigger() + flow.getEvent()).contains(procedure.getIfno())) {
                    			FormLogger.warn(procedure.getIfno() + " 没有对应的procedure");
                    		}
                    		if (!ifyesMap.get(flow.getTrigger() + flow.getEvent()).contains(procedure.getIfyes())) {
                    			FormLogger.warn(procedure.getIfyes() + " 没有对应的procedure");
                    		}
                    	}
                    }
                    FormLogger.logUi(String.format("校验ui[%s]内的flow[%s%s]内的逻辑成功",ui.getName(),flow.getTrigger(),flow.getEvent()), FormLogger.LOG_TYPE_INFO);
                }
            FormLogger.logUi(String.format("校验ui[%s]内的flow内的逻辑成功，总共校验了[%s]个flow",ui.getName(),flowList.size()), FormLogger.LOG_TYPE_INFO);
        }

    }
    public static String getXmlPath(){
    	return FormConfiguration.class.getClassLoader().getResource(XML_BUSINESS_PATH).getPath();
    }
    public static String getXsdPath(){
    	return FormConfiguration.class.getClassLoader().getResource(XSD_BUSINESS_PATH).getPath();
    }
	/**
	 * 加载xml
	 * @param request
	 */
    public static XML refreshXML() throws Exception {
        // panel内的control type集合
    	XML xml = new XML();
    	try {
			
		
//        List<String> xmlList = new ArrayList<String>();
        String xsd = getXsdPath();
        String xmlRootFolder = FormConfiguration.class.getClassLoader().getResource(FormConfiguration.XML_BUSINESS_PATH).getPath();
//		this.class.getClassLoader().getResourceAsStream(FormConfiguration.XML_CPMPONENT_PATH);

        Collection<File> xmlList = FileUtils.listFiles(new File(xmlRootFolder), new String[]{"xml"}, true);
//        findXml(getXmlPath(), xmlList);
        
        List<UI> uiList = new ArrayList<UI>();
        List<ModuleObject> moduleObjectList = new ArrayList<ModuleObject>();
//        for (String path : xmlList.) {
        
        for(File f:xmlList){
            try {
            	FormLogger.logInit(String.format("开始装载文件[%s]", f.getPath()), FormLogger.LOG_TYPE_INFO);
                XML XML = getXML(f.getPath());
                if (XML.getUi_layer() != null)
                    uiList.addAll(XML.getUi_layer().getUiList());
                if (XML.getModel_layer() != null)
                    moduleObjectList.addAll(XML.getModel_layer().getModuleObjectList());
                FormLogger.logInit(String.format("成功装载文件[%s]", f.getPath()), FormLogger.LOG_TYPE_INFO);
            } catch (Exception e) {
    			FormLogger.error("装载xml失败",f.getPath(), e.getMessage());
    			throw new ServletException();
            }
        }
        FormLogger.logInit(String.format("总共装载了[%s]个业务配置xml", xmlList.size()), FormLogger.LOG_TYPE_INFO);
        xml.setModel_layer(new ModelLayer(moduleObjectList));
        xml.setUi_layer(new UiLayer(uiList));
        String path = "";
        for (File f : xmlList) {
        	path = f.getPath();
        	FormLogger.logCheckXSD(String.format("开始xsd校验文件[%s]", path), FormLogger.LOG_TYPE_INFO);
            try {
            	validateXml(xsd, path);
            } catch (SAXException e) {
    			FormLogger.error("校验xml数据格式失败",path, e.getMessage());
    			e.printStackTrace();
    			throw new ServletException();
            } catch (IOException e) {
    			FormLogger.error("校验xml数据格式失败",path, e.getMessage());
    			throw new ServletException();
            }
            FormLogger.logCheckXSD(String.format("成功xsd校验文件[%s]", path), FormLogger.LOG_TYPE_INFO);
        }
        FormLogger.logInit(String.format("XSD校验了[%s]个业务配置xml", xmlList.size()), FormLogger.LOG_TYPE_INFO);
        FormLogger.logInit("ui对象ModuleObject对象开始校验", FormLogger.LOG_TYPE_INFO);
        validateXmlLogic(xml);
        FormLogger.logInit(String.format("ui对象ModuleObject队形校验成功总共校验了[%s]个UI对象,[%s]个", xml.getUi_layer().getUiList().size(), xml.getModel_layer().getModuleObjectList().size()), FormLogger.LOG_TYPE_INFO);
        Map<String, Object> modulMap = new HashMap<String, Object>();
        FormLogger.logInit("开始初始化xml对象数据", FormLogger.LOG_TYPE_INFO);
        for(UI ui:uiList){
        	try {
        		modulMap.put(ui.getName(), initUI(ui, xml));
			} catch (Exception e) {
				log.error("初始化xml对象数据失败," + e.getMessage());
    			throw new ServletException();
			}
        }
        ContextUtils.setAttrbute(ConstantUtil.XMLMAP, modulMap);
        FormLogger.logInit("初始化xml对象的数据成功", FormLogger.LOG_TYPE_INFO);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
        return xml;
        
    }
    
	// 填充ui对象
	private static Map<String,Object> initUI(UI ui, XML xml) throws ServletException {
		Map<String,Object> model = new HashMap<String,Object>();

        if(ui.getLayout().getRegionList() == null){
        	printError(String.format("校验xml数据失败,name为%s的ui，region为空",ui.getName()));
        }
		List<Region> regionList = ui.getLayout().getRegionList();
		for (Region region : regionList) {
			List<Subregion> subList = region.getSubregionList();
	        if(subList == null){
	        	printError(String.format("校验xml数据失败,name为%s的ui，其中某些region内的subregion为空",ui.getName()));
	        }
			for (Subregion s : subList) {
				s.setPanelObj(ui);
			}
		}
		// 计算tab以及排除隐藏panel
		Map<String, Panel> hiddenPanel = new HashMap<String, Panel>();
		Map<String, Panel> hiddenTable = new HashMap<String, Panel>();
		List<Panel> panelList = ui.getPanelList();
		for (Panel panel : panelList) {
			if(panel.getType().equalsIgnoreCase("hidden")){
				hiddenPanel.put(panel.getName(), panel);
			}
			if(panel.getType().equalsIgnoreCase("HiddenTable")){
				hiddenTable.put(panel.getName(), panel);
				panel.setModelId(xml.getModel_layer().getModuleObjectList());
				panel.setModuleObject(xml.getModel_layer().getModuleObjectList());
				panel.setSort(xml.getModel_layer().getModuleObjectList());
				setControls(panel, panel.getControlList(), ui);
			}
		}
		for (Region region : regionList) {
			Set<String> subSet = new HashSet<String>();
			for (Subregion subregion : region.getSubregionList()) {
				List<Subregion> tabList = new ArrayList<Subregion>();
				if (!subSet.contains(subregion.getLocation())) {
					for (Subregion s : region.getSubregionList()) {
						try {
							if (!s.getPanelObj().getType().equalsIgnoreCase("hidden")&&
									!s.getPanelObj().getType().equalsIgnoreCase("HiddenTable")&&
									s.getLocation().equalsIgnoreCase(subregion.getLocation()))
								tabList.add(s);
//							if(s.getPanelObj().getType().equalsIgnoreCase("hidden")){
//								s.getPanelObj().setTotalWidth(1);
//								hiddenPanel.put(s.getPanel(), s.getPanelObj());
//							}
//							if(s.getPanelObj().getType().equalsIgnoreCase("HiddenTable")){
//								s.getPanelObj().setTotalWidth(1);
//								hiddenTable.put(s.getPanel(), s.getPanelObj());
//							}
							
						} catch (Exception e) {
							printError(String.format("校验xml数据失败,可能是在名为[%s]的ui中的布局里，name为%s的panel不存在",ui.getName(), s.getPanel()));
						}
					}
				} 
				subSet.add(subregion.getLocation());
				subregion.setListSubregion(tabList);
			}
		}
		// 填充js分支逻辑
		if (ui.getFlowList() != null && ui.getFlowList().size() > 0){
			for (Flow flow : ui.getFlowList()) {
				flow.setBeginEndProcedure();
				List<Procedure> procedureList = flow.getProcedureList();
				if(procedureList!=null){
					for (Procedure procedure : procedureList) {
						procedure.setOutObjectType(ui.getPanelList());
						procedure.setIfyesProcedure(procedureList);
						procedure.setIfnoProcedure(procedureList);
					}
				}
			}
		}
		HashMap<String,String> mapPanel = new HashMap<String,String>(); 
		model.put("hiddenPanel",hiddenPanel);
		model.put("hiddenTable",hiddenTable);
		for (Region region : regionList) {
			List<Subregion> subList = region.getSubregionList();
			for (Subregion s : subList) {
				List<Subregion> subregionList = s.getListSubregion();
				if (subregionList != null && subregionList.size() > 0) {
					FormLogger.debug("开始界面布局,注意不存在的panel引用会导致空指针异常");
					// 计算group宽度
					for (Subregion subregion : subregionList) {
						subregion.setPanelObj(ui);
						Panel panel = subregion.getPanelObj();
						panel.setModelId(xml.getModel_layer().getModuleObjectList());
						panel.setModuleObject(xml.getModel_layer().getModuleObjectList());
						panel.setSort(xml.getModel_layer().getModuleObjectList());
						mapPanel.put(panel.getName(), panel.getPrimaryControlName(ui.getName()));
						if(ui.getFlowList()!=null){
							panel.setFlow(ui.getFlowList());
						}
                        List<Control> controlList = panel.getControlList();

                            panel.setTotalWidth(1);
                            if (controlList != null) {
                            	setControls(panel, controlList, ui);
                            }

                        }

					}
				}
			}

		for (String key : hiddenPanel.keySet()) {
			hiddenPanel.get(key).setModelId(xml.getModel_layer().getModuleObjectList());
			mapPanel.put(key, hiddenPanel.get(key).getPrimaryControlName(ui.getName()));
		}
		for (String key : hiddenTable.keySet()) {
			hiddenTable.get(key).setModelId(xml.getModel_layer().getModuleObjectList());
			mapPanel.put(key, hiddenTable.get(key).getPrimaryControlName(ui.getName()));
		}
		model.put("panelPropery",FormUtil.toJsonString(mapPanel));
		FormLogger.debug(mapPanel.toString());
		
		return model;
	}
	/**
	 * 递归查找xml
	 * @param path
	 * @param xmlList
	 */
    public static void findXml(String path, List<String> xmlList) {

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

    private static void setControls(Panel panel, List<Control> controlList, UI ui){

        for (Control control : controlList) {
            control.setHeight(StringUtils.isEmpty(control.getHeight()) ? "0" : control.getHeight());
            control.setWidth(StringUtils.isEmpty(control.getWidth()) ? "0" : control.getWidth());
            control.setFlow(ui.getFlowList(), panel.getName());
            control.setFlowOnSelect(ui.getFlowList(), panel.getName());
            control.setJsEventOnSelect(control.getFlow(), ui.getName());
            control.setJsEventOnEnter(control.getFlow(), ui.getName());
            control.setJsEvent(control.getFlow(), ui.getName());
            if (panel.getModuleObject() != null){
        		control.setPropertyObj(panel.getModuleObject());
            }
            control.setJsFunctionName();
        }
        if (panel.getType().equalsIgnoreCase(Panel.TYPE_GROUP)) {
            for (int i=controlList.size()-1; i>=0;i--) {
            	if(!controlList.get(i).getType().equalsIgnoreCase(Control.TYPE_HIDDEN) && 
            			"Y".equals(controlList.get(i).getColumn())){
            		break;
            	}
            	double totalWidth = 0;
            	double countWidth = 0;
            	int index =0;
                if (!controlList.get(i).getType().equalsIgnoreCase(Control.TYPE_HIDDEN)&& 
                		!StringUtils.isEmpty(controlList.get(i).getColumn())){
                	totalWidth = Double.parseDouble(controlList.get(i).getColumn());
                	for (int j=i; j>=0;j--) {
                		if(!StringUtils.isEmpty(controlList.get(j).getColumn()) &&j!=i){
                			break;
                		}
                		if (!controlList.get(j).getType().equalsIgnoreCase(Control.TYPE_HIDDEN)){
                			countWidth += Double.parseDouble(StringUtils.isEmpty(controlList.get(j).getWidth())?"1":controlList.get(j).getWidth());
                		}
                		index++;
                	}
                	for (int j=i; j>i-index;j--) {
                		if(!controlList.get(j).getType().equalsIgnoreCase(Control.TYPE_HIDDEN)){
                			if(countWidth<=totalWidth) {
                				controlList.get(j).setWidthPencent(Double.parseDouble(controlList.get(j).getWidth())/totalWidth);
                			} else {
                				controlList.get(j).setWidthPencent(Double.parseDouble(controlList.get(j).getWidth())/countWidth);
                			}
                		}
                	}
                }

            }

        }
        //判断grid是否可以编辑
        if (panel.getType().equalsIgnoreCase(Panel.TYPE_GRID)) {
            for (Control control : controlList) {
                if (control.getType().equals(Control.TYPE_TEXTBOX) || control.getType().equals(Control.TYPE_DATEBOX) ||
                        control.getType().equals(Control.TYPE_COMBOBOX) || control.getType().equals(Control.TYPE_POPBOX)) {
                    if (control.isEdit()) {
                        panel.setEdit("edit");
                        break;
                    }
                }
            }

        }
    
    }
	/**
	 * 解析xml
	 * @param request
	 * @param path
	 */
    public static XML getXML(String path) {
        XStream xstream = new XStream();
        xstream.processAnnotations(XML.class);
        xstream.autodetectAnnotations(true);
        XML xml = null;
        String xmlStr = getFile(path);
        xml = (XML) xstream.fromXML(xmlStr);
        return xml;
    }
    //读取文件
    public static String getFile(String filePath) {
        FileInputStream file = null;
        BufferedReader reader = null;
        InputStreamReader inputFileReader = null;
        StringBuffer content = new StringBuffer();
        String tempString = null;
        try {
            file = new FileInputStream(filePath);
            inputFileReader = new InputStreamReader(file, "utf-8");
            reader = new BufferedReader(inputFileReader);
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                content.append(tempString).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return content.toString();
    }
	public static void printError(String error) throws ServletException{
		FormLogger.error("校验xml数据格式失败",error, "失败");
		throw new ServletException(error);
	}
 
	/**
	 * UI直接通过外部访问时通过此参数标识决定是否进行登录后自动打开操作
	 * @param request
	 * @return
	 */
	public static boolean isOuterAccess(HttpServletRequest request) {
		return request.getHeader("Referer")==null || request.getParameter("out")!=null;
	}
	/**
	 * 是表单引擎url
	 * @param url
	 * @return
	 */
	public static boolean isUIUrl(String url) {
		return url!=null && !url.isEmpty() && url.startsWith("/ui/");
	}
}