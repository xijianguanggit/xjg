package com.tedu.base.initial.model.xml.ui;

public class FormConstants {
	private FormConstants(){}
	public static final String UI_ID = "uiid";//唯一标识ui.每次构造UI时都生成唯一UUID值
	public static final String PARENT_UI_ID = "puid";//父UI唯一标识,跳转时带入
	public static final String EVENT_CLICK = "OnClick";
	public static final String EVENT_ONLOAD = "OnLoad";
	public static final String RESET_FIELD_VALUE = "$RESET";

	public static final String METHOD_GETSOURCE = "getSource";
	public static final String METHOD_GETTARGET = "getTarget";
	
	public static final String DEFAULT_PAGE_SIZE = "50"; 

	//解析表达式的常用环境变量名
	public static final String AVIATOR_ENV_LOGIC = "logic";
	public static final String AVIATOR_ENV_MODEL = "model";
	public static final String AVIATOR_ENV_SESSION = "session";
	public static final String AVIATOR_ENV_SESSION_USERINFO = "userInfo";
	public static final String AVIATOR_ENV_SESSION_EXTINFO = "extInfo";
	public static final String AVIATOR_ENV_SESSION_UI_PARAMETER = "parameter";
	public static final String AVIATOR_ENV_SELF = "self";
	public static final String AVIATOR_ENV_MODE_ALIAS = "editMode";//两种都支持
	public static final String AVIATOR_ENV_MODE = "Mode";
	public static final String AVIATOR_ENV_PARAM = "param";

	public static final String AVIATOR_ENV_ROW = "row";
	public static final String AVIATOR_ENV_ROOT = "root";
	
	public static final String SESSION_REQUEST = "defaultRequest";//请求需要携带的基本信息
	public static final String SESSION_AUTH_CONTROL = "accessibleControl";//可访问的元素级组件
	public static final String SESSION_UINODE = "uinode";//UI path node
	public static final String SESSION_AUTH_URL = "accessibleUrl";
	public static final String SESSION_TOKENS = "tokens";
	public static final String SESSION_REDIRECT_NAME = "redirectName";//菜单项名称
	public static final String SESSION_REDIRECT_URL = "redirectUrl";//重定向
	public static final String SESSION_REDIRECT_CODE = "redirectCode";//menuCode
	public static final String SESSION_REDIRECT_TYPE = "redirectMenu";//menu or ..
	public static final String SESSION_PARAMETER = "parameter";//menu or ..
	public static final String SESSION_LAST_URL = "lastUrl";//最近访问的url
	public static final String SESSION_LAST_URL_NAME = "lastUrlName";//最近访问的url对应的资源名称
	public static final String SESSION_LAST_URL_PARAM_ID = "lastUrlId";//用于定位UI数据，在mainFrame中打开前encodeId此值.目前仅允许id,未来可扩展data用于携带json描述，以满足复杂界面多值传递(EncodeParam)
	
	public static final String LOGIC_ADDROW = "AddRow";
	public static final String LOGIC_CLOSE = "Close";
	public static final String LOGIC_CONFIRM_MSG = "ConfirmMsg";
	public static final String LOGIC_ENCODE_ID = "EncodeId";
	public static final String LOGIC_DECODE_ID = "DecodeId";
	
	public static final String LOGIC_POPUP = "Popup";
	public static final String LOGIC_BACK = "Back";
	public static final String LOGIC_BRANCH = "Branch";
	public static final String LOGIC_BATCHIMPORT = "BatchImport";
	public static final String LOGIC_BATCHUPDATE = "BatchUpdate";
	public static final String LOGIC_CLEAR = "Clear";
	public static final String LOGIC_DELETE = "Delete";
	public static final String LOGIC_FIND = "Find";
	public static final String LOGIC_LIST = "List";
	public static final String LOGIC_QUERY = "Query";
	public static final String LOGIC_SEARCH = "Search";
	public static final String LOGIC_QUERYBYID = "QueryById";
	public static final String LOGIC_SAVE = "Save";
	public static final String LOGIC_SAVEC = "SaveCustom";
	public static final String LOGIC_VALIDATE = "Validate";
	public static final String LOGIC_TRANSFORM = "Transform";
	public static final String LOGIC_TRANSITION = "Transition";
	public static final String LOGIC_PREVFILE = "PreviewFile";
	public static final String LOGIC_PRINTPDF = "PrintPdf";
	public static final String LOGIC_EXPORT = "Export";
	public static final String LOGIC_EXPFORMAT = "ExportFormat";
	public static final String LOGIC_UPLOAD = "Upload";
	public static final String LOGIC_START_WORK_FLOW = "StartWorkflow";
	public static final String LOGIC_EXEC_ACTIVITY = "ExecActivity";
	public static final String LOGIC_LIST_ACTIVITY = "ListActivity";
	public static final String LOGIC_MARKDOWN = "MarkDown"; 

	
	public static final String ROLE_ADMIN = "Administrator";
	public static final String ROLE_OPEN = "open";//fixed role for open view
	//view标签
	public static final String TAG_FUNCTION_ID = "functionId";
	public static final String TAG_PANEL_PROPERTY = "panelPropery";
	public static final String TAG_LOGIC = "logic";
	public static final String TAG_UI_NAME = "ui";
	public static final String TAG_SMALL = "Small";
	public static final String TAG_MEDIUM = "Medium";
	public static final String TAG_LARGE = "Large";
	public static final String TAG_Xlarge = "Xlarge";
	public static final String TAG_LOG_BEGIN_LOGIC = "logBegin";
	
	public static final String REQUEST_TOKEN = "token";
	
	public static final String XML_ROOT = "engine/form";
	

	public static final String UI_TYPE_MENU_ITEM = "MenuItem";
	
	public static final String TEMPLATE_VALIDATE = "view/ValidExceptionView.ftl";
	public static final String VIEW_FIND = "common/dlgFindSingle";
	public static final String VIEW_FIND_MULTI = "common/dlgFindMulti";
	public static final String VIEW_IMPORTBAT = "common/dlgImportBat";
	public static final String VIEW_IMPORTBATCUST = "common/dlgImportBatCust";
	public static final String VIEW_PLAY = "common/Play";
	public static final String VIEW_RPEVIEW = "common/previewFile";
	public static final String VIEW_OPENFILE = "common/openfile";
	public static final String VIEW_EXEC_ACTIVITY = "common/execActivity";
	public static final String VIEW_LIST_ACTIVITY = "common/listActivity";

	public static final String FIELD_VALUE_TEMPLATE = "%s=#{data.%s}";//预编译语句中的字段=值部分。常用于where或者update语句
	public static final String FIELD_NULL_TEMPLATE = "%s=null";
	public static final String FIELD_VER_TEMPLATE = "%s=%s+1";//optimistic lock
	public static final String FIELD_INSERT_TEMPLATE = "#{data.%s}";//预编译语句中的构造insert字段值的部分。
	public static final String VIEW_MARKDOWN = "common/editorMd";
	public static final String VIEW_UNDERCONSTRUCT = "common/underConst";

	public static final String VIEW_EDIT_CODE= "common/editCode";

	public static final String XML_YES = "Y";//配置里通常boolean用Y表示
	public static final String XML_UI_MAPPING = "/ui/";
	public static final String XML_PUBLIC_MAPPING = "/open/";
	
	public static final String SHARE_LINK_KEY = "share";
	
	public static final String CONTEXT_ATTRIBUTE_LOGICS = "logics";
	
	
	//markdown flow
	public static final String MARKDOWN_FORM_NAME = "名称";
	public static final String MARKDOWN_FORM_LINK = "链接";
	public static final String MARKDOWN_FORM_BUTTON = "按钮";
	public static final String MARKDOWN_FORM_TITLE = "标题";
	public static final String MARKDOWN_FORM_PANEL = "区域";
	public static final String MARKDOWN_LOGIC_DECODEID_WORD = "解码";
	public static final String MARKDOWN_LOGIC_ECODEID_WORD = "传值";
	public static final String MARKDOWN_FLOW_ONLOAD_WORD = "装载";
	public static final String MARKDOWN_FLOW_ONCLICK_WORD = "点击";
	public static final String MARKDOWN_FLOW_ONSELECT_WORD = "选中";
	public static final String MARKDOWN_FLOW_ONLOAD_JSP = "界面";
	public static final String MARKDOWN_FORM_QUERY = "查询";
	public static final String MARKDOWN_LOGIC_FIND_WORD = "查找";
	public static final String MARKDOWN_LOGIC_SAVEW_WORD = "保存";
	public static final String MARKDOWN_LOGIC_MASTER_WORD = "详情";
	public static final String MARKDOWN_LOGIC_DEL_WORD = "删除";
	public static final String MARKDOWN_LOGIC_CONFIRM_WORD = "确认";
	
	public static final String UNKNOWN = "unknown";
	
	//wkhtmltopdf在系统中的路径  
	public static final String TOPDFTOOL= "D:\\Program Files\\wkhtmltopdf\\bin\\wkhtmltopdf.exe";
	
}

