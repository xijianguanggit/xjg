package com.tedu.base.initial.model.xml.ui;

import java.util.Arrays;
import java.util.List;

import org.springframework.util.StringUtils;

import com.tedu.base.engine.util.FormLogger;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;

@XStreamAlias("panel")  
public class Panel {
	public static final String TYPE_GRID = "Grid";
	public static final String TYPE_GROUP = "Group";
	public static final String TYPE_TOOLBAR = "Toolbar";
	public static final String TYPE_TREE = "Tree";
	public static final String TYPE_HIDDEN = "Hidden";
	public static final String TYPE_IMAGE = "Image";
	public static final String TYPE_LINE_CHART="LineChart";
	public static final String TYPE_Gantt_CHART="GanttChart";
	public static final String TYPE_PIE_CHART="PieChart";
	public static final String TYPE_BAR_CHART="BarChart";
	public static final String TYPE_COLUMN_CHART="ColumnChart";
	
	public static final String TYPE_HIDDEN_TABLE="HiddenTable";
	
	//名字
	@XStreamAsAttribute
	private String name;
	//标题
	@XStreamAsAttribute
	private String title;
	//类型 Toolbar 取值为 Group Picture Grid Tree LineChart/PieCh rt/BarChart
	@XStreamAsAttribute
	private String type;
	@XStreamAsAttribute
	private String order;	
	// 对应modul
	@XStreamAsAttribute
	private String object;
	// 对应ModuleObject
	private ModuleObject moduleObject;
	//对应controlList
	@XStreamImplicit(itemFieldName="control")
	private List<Control> controlList;
	@XStreamImplicit(itemFieldName="filter")
	private List<Filter> filterList;
	@XStreamAsAttribute
	private String multiple;
	@XStreamAsAttribute
	private String isCheckAll;
	@XStreamAsAttribute
	private String page;//​page填写数字，0表示不分页，>0表示一页中记录数量，默认50;分页下拉选项提供N组page的倍数
	// panel是否可编辑 只有datagrid才有
	
	// 是否可以编辑
	@XStreamAsAttribute
	@XStreamConverter(value=BooleanConverter.class, booleans={false}, strings={"Y", "N"})
	private boolean optimize;	
	private String edit;
	private String modelId;
	private String sortName;
	private String sortOrder;
	private Flow flow;
	
	@XStreamAsAttribute
	@XStreamConverter(value=BooleanConverter.class, booleans={false}, strings={"Y", "N"})
	private boolean search;
	//总宽度
	private int totalWidth=1;
	private String UIName;
	public Panel(String UIName,String name,String object){
		this.UIName = UIName;
		this.name = name;
		this.object = object;
	}
	// 此panel对应的flow
	public void setFlow(List<Flow> flowList){
		for(Flow flow:flowList){
			// 规则如果为空则返回uiName上的flow 如果为pnlUp.ctlQuery这种格式则是返回pnlUp
			// panel内的ctlQuerycontrol 的flow
			if(!StringUtils.isEmpty(flow)&&flow.getTrigger().indexOf(".")==-1&& flow.getTrigger().equals(this.name)){
				this.flow = flow;
			}
		}
	}
	// 此panel对应的flow
	public Flow getFlow(){
		return flow;
	}
	public void setModelId(List<ModuleObject> moduleObjectList){
		if(this.object==null) return;
		for(ModuleObject moduleObject:moduleObjectList){
			// 规则如果为空则返回uiName上的flow 如果为pnlUp.ctlQuery这种格式则是返回pnlUp
			// panel内的ctlQuerycontrol 的flow
			if(this.object.equals(moduleObject.getName())){
				if(moduleObject.getPrimary() ==null){
					break;
				}
				for(Property property:moduleObject.getPropertyList()){
					if(moduleObject.getPrimary().equals(property.getField())){
						this.modelId = property.getName();
						break;
					}
				}
				break;
			}
		}
	}
	
	/**
	 * 根据panel+主键对应的controlName，可以共用encode逻辑的js方法实现每个panel的运行时刻获取唯一标识值
	 * 用于实现delete,querybyId等逻辑时，入参inPanel
	 */
	public String getPrimaryControlName(String uiName){
		String primaryControlName = "";
		if(this.getType().equals(TYPE_GRID)){
			primaryControlName = modelId;//表格取主键对应的propertyName
		}else if(this.getType().equals(TYPE_GROUP) || this.getType().equals(TYPE_HIDDEN)){
			Control c = getPrimaryControl(uiName);
			primaryControlName = c==null?"":c.getName();
		}else if(this.getType().equals(TYPE_TREE)){
			primaryControlName="id";
		}else if(this.getType().equals(TYPE_IMAGE)){
			Control c = getPrimaryControl(uiName);
			primaryControlName = c==null?"":c.getName();
		} 
		return primaryControlName;
	}
	
	private Control getPrimaryControl(String uiName){
		if(modelId==null) {
			FormLogger.logFlow(String.format("%s获取主键所属组件失败,请先设置panel的model对象",uiName), FormLogger.LOG_TYPE_WARN);
			return null;
		}
		for(Control c:controlList){
			if(c.getProperty()!=null && c.getProperty().equals(modelId)){
				return c;
			}
		}
		return null;
	}
	
	public String getModelId(){
		return this.modelId;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public List<Control> getControlList() {
		return controlList;
	}
	public void setControlList(List<Control> controlList) {
		this.controlList = controlList;
	}
	public int getTotalWidth() {
		return totalWidth;
	}
	public void setTotalWidth(int totalWidth) {
		this.totalWidth = totalWidth;
	}

	public String getUIName() {
		return UIName;
	}

	public void setUIName(String uIName) {
		UIName = uIName;
	}

	/**
	 * 获取当前panel指定组件的客户端组件Id
	 * 常用于返回ui相关的json结构数据，如表单数据
	 * @return
	 */
	public String getControlId(String controlName){
		return String.format("%s_%s",this.getName(),controlName);
	}
	
	/**
	 * 按名称检索当前panel里的control
	 * @param controlName
	 * @return
	 */
	public Control getControl(String controlName){
		if(controlList == null) return null;
		for(Control c:controlList){
			if(c.getName().equals(controlName)){
				//TODO 这里改写了control对象的panelName属性
				c.setPanelName(name);
				return c;
			}
		}
		return null;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(List<ModuleObject> moduleObjectList) {
		for(ModuleObject m:moduleObjectList){
			if(m.getName().equalsIgnoreCase(this.object)){
				if(StringUtils.isEmpty(m.getOrder()))
					break;
				this.sortName = genSortName(m.getOrder());
				break;
			}
		}
	}

	private String genSortName(String strModuleOrder){
		String strOrder = (this.getOrder()!=null && !this.getOrder().isEmpty())?this.getOrder():strModuleOrder;
		String[] arrOrder = strOrder.split(",");
		StringBuilder sortBuffer = new StringBuilder();
		for(String str:arrOrder){
			if(str.trim().split(" ").length==1){
				sortBuffer.append("asc");
			} else {
				sortBuffer.append(str.trim().split(" ")[1]);
			}
			sortBuffer.append(",");
		}
		return sortBuffer.substring(0, sortBuffer.lastIndexOf(","));
	}
	
	private String genSortOrder(String strModuleOrder){
		String strOrder = (this.getOrder()!=null && !this.getOrder().isEmpty())?this.getOrder():strModuleOrder;
		String[] arrOrder = strOrder.split(",");
		StringBuilder sortBuffer = new StringBuilder();
		for(String str:arrOrder){
			sortBuffer.append(str.trim().split(" ")[0]);
			sortBuffer.append(",");
		}
		return sortBuffer.substring(0, sortBuffer.lastIndexOf(","));
	}
	
	public void setSortName() {
		if(getModuleObject()==null || StringUtils.isEmpty(getModuleObject().getOrder()))
			return;
		this.sortName = genSortName(getModuleObject().getOrder());
	}
	
	public String getSortOrder() {
		return sortOrder;
	}

	public void setSort(List<ModuleObject> moduleObjectList) {
		String orderValue = getOrder();
		if(orderValue!=null && !orderValue.isEmpty()){
			this.sortOrder = genSortOrder(orderValue);
			this.sortName = genSortName(orderValue);
			return;
		}
		setSortOrder(moduleObjectList);
		setSortName(moduleObjectList);
	}
	
	
	public void setSortOrder(List<ModuleObject> moduleObjectList) {
		for(ModuleObject m:moduleObjectList){
			if(m.getName().equalsIgnoreCase(this.object)){
				if(StringUtils.isEmpty(m.getOrder()))
					break;
				this.sortOrder = genSortOrder(m.getOrder());
				break;
			}
		}
	}
	
	public void setSortOrder() {
		if(getModuleObject()==null || StringUtils.isEmpty(getModuleObject().getOrder())){
			return;
		}
		this.sortOrder = genSortOrder(getModuleObject().getOrder());
	}
	
	public ModuleObject getModuleObject() {
		return moduleObject;
	}

	public void setModuleObject(List<ModuleObject> moduleObjectList) {
		for(ModuleObject moduleObject:moduleObjectList){
			if(moduleObject.getName().equalsIgnoreCase(this.object)){
				this.moduleObject = moduleObject;
				break;
			}
		}
	}

	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}
	public String getEdit() {
		return edit;
	}
	public void setEdit(String edit) {
		this.edit = edit;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getPagination(){
		if(page == null || page.trim().isEmpty()) {
			return "true";
		}else if(page.trim().equals("0")){
			return "false";
		}else{
			return "true";
		}
	}
	
	public String getPageSize(){
		if(page==null || page.trim().isEmpty() || page.trim().equals("0")) {
			return FormConstants.DEFAULT_PAGE_SIZE;
		}else{
			return page.trim();
		}
	}
	//string array
	public String getPageList(){
		int pageSize = Integer.parseInt(getPageSize());
		return String.format("[%s,%s,%s,%s]", pageSize,pageSize*2,pageSize*4,pageSize*8);
	}
	//注意顺序 @TODO 未来放配置文件
	public boolean isChart(){
		return Arrays.binarySearch(new String[]{
				TYPE_BAR_CHART,
				TYPE_COLUMN_CHART,
				TYPE_LINE_CHART,
				TYPE_PIE_CHART,
				TYPE_Gantt_CHART
		},type)>=0;
	}
	public List<Filter> getFilterList() {
		return filterList;
	}
	public void setFilterList(List<Filter> filterList) {
		this.filterList = filterList;
	}
	public String getIsCheckAll() {
		return isCheckAll;
	}
	public void setIsCheckAll(String isCheckAll) {
		this.isCheckAll = isCheckAll;
	}
	public boolean isOptimize() {
		return optimize;
	}
	public void setOptimize(boolean optimize) {
		this.optimize = optimize;
	}
	public boolean isSearch() {
		return search;
	}
	public void setSearch(boolean search) {
		this.search = search;
	}

	
}
