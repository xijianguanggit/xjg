package com.tedu.base.initial.model.xml.ui;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.util.StringUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;

@XStreamAlias("control") 
public class Control implements Serializable {
	/**
	 * @see 开发指南.md Control节点
	 */
	public static final String TYPE_BUTTON = "Button";
	public static final String TYPE_TEXTBOX = "TextBox";
	public static final String TYPE_DATEBOX = "DateBox";
	public static final String TYPE_COMBOBOX = "ComboBox";
	public static final String TYPE_MULTIPLECOMBOBOX = "MultipleComboBox";
	public static final String TYPE_POPBOX = "PopupBox";
	public static final String TYPE_PASSWORD = "PasswordBox";
	public static final String TYPE_LABEL = "Label";
	public static final String TYPE_FILEBOX = "FileBox";
	public static final String TYPE_CHECKBOX = "CheckBox";
	public static final String TYPE_HIDDEN = "Hidden";
	public static final String TYPE_DATALINK = "DataLink";
	public static final String TYPE_LINK = "Link";
	public static final String TYPE_PHOTO = "Photo";
	public static final String TYPE_KINDEDITOR = "KindEditor";
	public static final String TYPE_UEDITOR = "UEditor";
	public static final String TYPE_RADIOBUTTON = "RadioButton";
	
	
	public String getJsEventOnEnter() {
		return jsEventOnEnter;
	}

	//宽 指的是单位 例如等于1时是1单位宽
	@XStreamAsAttribute
	private String width;
	private double widthPencent;
	//同上
	@XStreamAsAttribute
	private String height;
	//是否折行
	@XStreamAsAttribute
	private String column;
	// 取值如下Button	Label	TextBox	DateBox	ComboBox	PopupBox	PasswordBox KindEditor
	//FileBox	CheckBox	Photo	Carousel	GridColumn	GridLink	GridButton	TreeNode	ChartX	ChartY
	@XStreamAsAttribute
	private String type;
	// 名字
	@XStreamAsAttribute
	private String name;
	// placeholder
	@XStreamAsAttribute
	private String tip;
	// 标题
	@XStreamAsAttribute
	private String title;
	// 对应modul
	@XStreamAsAttribute
	private String property;
	// 是否可以编辑
	@XStreamAsAttribute
	@XStreamConverter(value=BooleanConverter.class, booleans={false}, strings={"Y", "N"})
	private boolean edit;
	// 是否可以必填
	@XStreamAsAttribute
	@XStreamConverter(value=BooleanConverter.class, booleans={false}, strings={"Y", "N"})
	private boolean required;
	// 是否可以编辑
	@XStreamAsAttribute
	@XStreamConverter(value=BooleanConverter.class, booleans={false}, strings={"Y", "N"})
	private boolean multiple;
	@XStreamAsAttribute
	private String format;
	@XStreamAsAttribute
	private String initial = "";	
	//查询之后计算表达式对相应组件赋值
	@XStreamAsAttribute
	private String source;
	//保存之前计算表达式对相应属性赋值
	@XStreamAsAttribute
	private String target;
	@XStreamAsAttribute
	private String drill;//钻取表达式,aviator解析，可使用row,session对象
	@XStreamAsAttribute
	private String alias;
	
	private Flow flow;
	private Flow flowOnSelect;
	private Flow flowOnEnter;
	private boolean isAllowed;
	// control内div宽度
//	private float widthdiv;
	// control内input宽度
//	private float widthinput;
	private Property propertyObj;
	private String panelName;
	private String jsEvent;
	private String jsEventOnSelect;
	private String jsEventOnEnter;

	//为前端和后端同时支持校验表达式特定的属性
	private String jsFunctionName;

	public Flow getFlow() {
		return flow;
	}

	public void setFlow(List<Flow> flowList, String panelName) {
		if(flowList!=null && flowList.size()>0)
		for(Flow flow:flowList){
			// 规则如果为空则返回uiName上的flow 如果为pnlUp.ctlQuery这种格式则是返回pnlUp
			// panel内的ctlQuerycontrol 的flow
			if(!StringUtils.isEmpty(flow)&&flow.getTrigger().split("\\.").length==2
					&& flow.getTrigger().split("\\.")[0].equals(panelName)
					&& (flow.getEvent().equals("OnClick")||flow.getEvent().equals("OnBlur")||flow.getEvent().equals("OnEnter"))
					&& flow.getTrigger().split("\\.")[1].equals(this.name)){
				this.flow = flow;
				break;
			}
		}
	}

	public Flow getFlowOnSelect() {
		return flowOnSelect;
	}

	public void setFlowOnSelect(List<Flow> flowList, String panelName) {
		if(flowList!=null && flowList.size()>0)
		for(Flow flow:flowList){
			// 规则如果为空则返回uiName上的flow 如果为pnlUp.ctlQuery这种格式则是返回pnlUp
			// panel内的ctlQuerycontrol 的flow
			if(!StringUtils.isEmpty(flow)&&flow.getTrigger().split("\\.").length==2
					&& flow.getTrigger().split("\\.")[0].equals(panelName)
					&& flow.getEvent().equals("OnSelect")
					&& flow.getTrigger().split("\\.")[1].equals(this.name)){
				this.flowOnSelect = flow;
				break;
			}
		}
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return StringUtils.isEmpty(this.height)?"1":height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public boolean isEdit() {
		return edit;
	}
	public void setEdit(boolean edit) {
		this.edit = edit;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public boolean isMultiple() {
		return multiple;
	}
	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}

	public String getHtmlName(){
		return name;
	}
	
	public Control(Property property){

	}
	public Control(String name,String title,String property){
		this.name = name;
		this.title = title;
		this.property = property;
	}
	
	public Control(String name,String title,String property,boolean required){
		this.name = name;
		this.title = title;
		this.property = property;
		this.required = required;
	}

	public Property getPropertyObj() {
		return propertyObj;
	}

	public void setPropertyObj(Property propertyObj) {
		this.propertyObj = propertyObj;
	}
	public void setPropertyObj(ModuleObject moduleObject) {
		List<Property> propertyList=moduleObject.getPropertyList();
	    Map<String, Property> mappedMovies = propertyList.stream().collect(  
	            Collectors.toMap(Property::getName, (p) -> p));
	    this.propertyObj = mappedMovies.get(this.property);
	}
	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getFormatter(){
		return ObjectUtils.toString(format).isEmpty()?"":"formatter='" + format + "'";
	}

	public String getPanelName() {
		return panelName;
	}

	public void setPanelName(String panelName) {
		this.panelName = panelName;
	}

	public String getJsEvent() {
		return jsEvent;
	}

	public void setJsEvent(Flow flow, String uiName) {
		if(flow==null){
			this.jsEvent = "";
		} else if("OnClick".equals(flow.getEvent())||"OnBlur".equals(flow.getEvent())||"OnEnter".equals(flow.getEvent())){
			this.jsEvent = flow.getEvent()+"_"+flow.getTrigger().replace(".", "_")+"_"+uiName;
		}
	}

	public String getJsEventOnSelect() {
		return jsEventOnSelect;
	}

	public void setJsEventOnSelect(Flow flow, String uiName) {
		if(flow==null){
			this.jsEventOnSelect = "";
		} else if("OnSelect".equals(flow.getEvent())){
			this.jsEventOnSelect = flow.getEvent()+"_"+flow.getTrigger().replace(".", "_")+"_"+uiName;
		}
	}
	
	public void setJsEventOnEnter(Flow flow, String uiName) {
		if(flow==null){
			this.jsEventOnEnter = "";
		} else if("OnEnter".equals(flow.getEvent())){
			this.jsEventOnEnter = flow.getEvent()+"_"+flow.getTrigger().replace(".", "_")+"_"+uiName;
		}
	}
	
	
	public String getJsFunctionName() {
		return jsFunctionName;
	}

	public void setJsFunctionName() {

		if(this.getPropertyObj()!=null&&this.getPropertyObj().getValidate()!=null&&!this.getPropertyObj().getValidate().equals("")&&this.getPropertyObj().getValidate().contains("()")) {
			String validate = this.getPropertyObj().getValidate();
			this.jsFunctionName = validate.substring(0,validate.indexOf('('));
		}else{
			this.jsFunctionName ="";
		}
	}

	public String getDrill() {
		return drill;
	}

	public void setDrill(String drill) {
		this.drill = drill;
	}
	public double getWidthPencent() {
		return widthPencent;
	}

	public void setWidthPencent(double widthPencent) {
		this.widthPencent = widthPencent;
	}

	public boolean isAllowed() {
		return isAllowed;
	}

	public void setAllowed(boolean isAllowed) {
		this.isAllowed = isAllowed;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	

	
}
