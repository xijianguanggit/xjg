package com.tedu.base.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:存储系统常量
 * @author ChenYanZhong
 * @date 2016-6-3 17:33:00
 *
 */
public class ConstantUtil {

	/** 放到session中的验证码key 唯一标示 */
	public static final String RANDOMCODEKEY = "RANDOMVALIDATECODEKEY";
	// Session中的个人信息
	public static final String USER_INFO = "userInfo";
	public static final String EXT_INFO = "extInfo";
	public static final String XML = "xml";
	public static final String XMLMAP = "xmlMap";
	/** 放到session中的用户key 唯一标示 */
	public static final String USER = "userSession";// 登录人

	public static final String RESOUCE_MENUS = "resouceMenus";

	public static final String SESSION_FILTER = "filter";

	public static final String UI_TOKEN = "uitoken";
	public static final String ENGINE_FORM_MODEL_PATH = "/WEB-INF/classes/engine/form/modelLayer/";
	public static final String ENGINE_FORM_UI_PATH = "/WEB-INF/classes/engine/form/uiLayer/";
	public static final String ENGINE_FORM_XSD_PATH = "/WEB-INF/classes/engine/form/componentConfig.xsd";
	public static final String TOOLBARSET = "toolbarSet";
	public static final String GROUPSET = "groupSet";
	public static final String GRIDSET = "gridSet";
	public static final String TREESET = "treeSet";
	public static final String IMAGESET = "imageSet";
	public static final String LINE_CHARTSET = "LineChart";
	public static final String Gantt_CHARTSET = "GanttChart";
	public static final String PIE_CHARTSET = "PieChart";
	public static final String BAR_CHARTSET = "BarChart";
	public static final String COLUMN_CHARTSET = "ColumnChart";
		public static Map<String, String> dayNames = new HashMap<String, String>();
		static{
		  dayNames.put("星期一", "1"); 
		  dayNames.put("星期二", "2");
		  dayNames.put("星期三", "3");
		  dayNames.put("星期四", "4");
		  dayNames.put("星期五", "5");
		  dayNames.put("星期六", "6");
		  dayNames.put("星期日", "7");
	}
}
