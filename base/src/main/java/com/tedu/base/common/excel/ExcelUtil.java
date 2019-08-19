package com.tedu.base.common.excel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.googlecode.aviator.AviatorEvaluator;
import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.service.CommonService;
import com.tedu.base.common.utils.ConstantUtil;
import com.tedu.base.common.utils.ContextUtils;
import com.tedu.base.common.utils.ResponseJSON;
import com.tedu.base.engine.model.ServerTokenData;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.engine.util.FormConfiguration;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.initial.model.xml.ui.Control;
import com.tedu.base.initial.model.xml.ui.Panel;
import com.tedu.base.initial.model.xml.ui.Param;
import com.tedu.base.initial.model.xml.ui.Procedure;
import com.tedu.base.initial.model.xml.ui.UI;
import com.tedu.base.initial.model.xml.ui.XML;
import com.tedu.base.rule.AviatorUtil;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * @Comments : 导入导出Excel工具类
 * @Version : 1.0.0
 */
@Component
public class ExcelUtil {
	public static final Logger log = Logger.getLogger(ExcelUtil.class);

	@Autowired
	private CommonService commonService;
	@Autowired
	private SqlSessionTemplate sqlSession;
	@Resource
	private FormService formService;
	public SqlSessionTemplate getSqlSession() {
		return sqlSession;
	}

	public void setSqlSession(SqlSessionTemplate sqlSession) {
		this.sqlSession = sqlSession;
	}

	public FormService getFormService() {
		return formService;
	}

	public void setFormService(FormService formService) {
		this.formService = formService;
	}

	private static ExcelUtil excelUtil;

	@PostConstruct
	public void init() {
		excelUtil = this;
//		excelUtil.sqlSession = this.sqlSession;
//		excelUtil.formService = this.formService;
	}

	public static List<String[]> excelToList(ServerTokenData serverTokenData,InputStream in) throws IOException {
		List<String[]> lstLine = new ArrayList<>();
		Workbook wb = null;
		try {
			wb = Workbook.getWorkbook(in);
			Sheet sheet = wb.getSheet(0);
			String[] data;
			for(int i=1;i<sheet.getRows();i++){
				Cell[] cells = sheet.getRow(i);
				int len = cells.length;
				data = new String[len];
				for (int n=0;n<cells.length;n++) {
					if(cells[n] instanceof DateCell){
						Date dateValue = ((DateCell)cells[n]).getDate();
						data[n] = DateUtil.formatDate(dateValue, "yyyy-MM-dd");
					}else{
						data[n] = cells[n].getContents();
					}
				}
				lstLine.add(data);
			}
		} catch (BiffException e) {
        	FormLogger.logFlow("导入Excel异常," + e.getMessage(), FormLogger.LOG_TYPE_ERROR);
		}finally{
			if(wb!=null){
				wb.close();
			}
		}
		return lstLine;
	}

		
	/**
	 * @deprecated 
	 * @MethodName : excelToList
	 * @return ：List
	 * @throws BiffException 
	 * @throws ExcelException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws InvocationTargetException
	 */
	public static List<Map<String,Object>> excelToList(InputStream in, List<Control> controlList, String[]paramForeign, String[]paramDict) throws BiffException, IOException {
		List<Control> displayList = new ArrayList<Control>();
		for(Control c:controlList){
			if(c.isEdit()&&!c.getType().equals("Hidden"))
				displayList.add(c);
		}
//		Map<String, Object> displayMap = displayList.stream().collect(Collectors.toMap(Control::getName, (p1) -> p1));
		
		// 根据Excel数据源创建WorkBook
		Workbook wb = Workbook.getWorkbook(in);
		// 获取工作表
		Sheet sheet = wb.getSheet(0);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		// 获取工作表的有效行数
		for(int i=1;i<sheet.getRows();i++){
			Cell[] cells = sheet.getRow(i);
			Map<String, Object> map = new HashMap<String, Object>();
			for(Control c:controlList){
				String initialExpression = ObjectUtils.toString(c.getInitial());
				Map env = new HashMap();
				AviatorUtil.prepareSessionEnv(env);
				if(!initialExpression.isEmpty()){
					Object val = AviatorEvaluator.execute(initialExpression,env,true);
					if(val == null) {
						throw new ServiceException(ErrorCode.ILLEGAL_FORM_CONFIGURATION,
								String.format("组件名的initial表达式[%s]解析结果为null {%s}", c.getName(),initialExpression));
					}
					map.put(c.getName(), val);
				}
			}
			StringBuffer checkResult = new StringBuffer();
			for (int j=0;j<cells.length;j++) {
				map.put(displayList.get(j).getName(), cells[j].getContents().toString().trim());
				
				for(String str:paramDict){
					if(displayList.get(j).getName().equals(str.split("\\|")[0]) && !StringUtils.isEmpty(cells[j].getContents().toString().trim())){
						QueryPage qp= new QueryPage();
						qp.setQueryParam(str.split("\\|")[1]);
						if(str.split("\\|").length==3){
							Map<String, Object> dataMap = new HashMap<String, Object>();
							dataMap.put(str.split("\\|")[2], map.get(str.split("\\|")[2]));
							qp.setDataByMap(dataMap);
						}
						
						List<Map<String,Object>> dicList = excelUtil.formService.queryBySqlId(qp);//sql返回列中需含value,text两列。额外特性列selected
						
						boolean flg = false;
						if(dicList!=null && dicList.size()>0)
						for(Map<String, Object> dicMap:dicList){
							if(dicMap.get("text").equals(cells[j].getContents().toString().trim())){
								map.put(str.split("\\|")[0]+"_name", dicMap.get("text"));
								map.put(str.split("\\|")[0], dicMap.get("value"));
								flg = true;
								break;
							}
						}
						if(!flg){
							map.put(str.split("\\|")[0]+"_name", null);
							map.put(str.split("\\|")[0], null);
							checkResult.append(cells[j].getContents().toString()).append(" 对应的字典不存在");
						} 
					}
				}
				
				for(String str:paramForeign){
					if(displayList.get(j).getName().equals(str.split("\\|")[2])&&!StringUtils.isEmpty(cells[j].getContents().toString().trim())){
						QueryPage queryPage = new QueryPage();
						queryPage.setPage(1);
						queryPage.setRows(1);
						queryPage.setQueryParam("select id from "+str.split("\\|")[1].split("\\.")[0]
								+" where "+str.split("\\|")[1].split("\\.")[1]+"='"+cells[j].getContents().toString().trim()+"'");
						Map<String,Object> result = excelUtil.sqlSession.selectOne("query", queryPage);
						if(result==null){
							checkResult.append(cells[j].getContents().toString()).append(" 对应的外键不存在");
							map.put(str.split("\\|")[0], null);
						} else {
							map.put(str.split("\\|")[0], result.entrySet().iterator().next().getValue());
						}
					}
				}
				
			}
			map.put("验证结果", checkResult.length()==0?"成功":checkResult);
			list.add(map);
		}
		return list;
	}

	private static void genInitial(List<Control> controlList,Map<String,Object> map){
		for(Control c:controlList){
			String initialExpression = ObjectUtils.toString(c.getInitial());
			Map env = new HashMap();
			AviatorUtil.prepareSessionEnv(env);
			if(!initialExpression.isEmpty()){
				Object val = AviatorEvaluator.execute(initialExpression,env,true);
				if(val == null) {
					throw new ServiceException(ErrorCode.ILLEGAL_FORM_CONFIGURATION,
							String.format("组件名的initial表达式[%s]解析结果为null {%s}", c.getName(),initialExpression));
				}
				map.put(c.getName(), val);
			}
		}
	}
	
	/**
	 * 批量导入数据之CSV
	 * 将数据构造成List<String[]>后生成表格数据给前端确认
	 * @param in
	 * @param controlList
	 * @param paramForeign
	 * @param paramDict
	 * @return
	 * @throws BiffException
	 * @throws IOException
	 */
	public static List<String[]> csvToList(ServerTokenData serverTokenData,InputStream in) throws IOException {
        List<String[]> lstLine = new ArrayList<>();
		Reader reader = new InputStreamReader(in,"utf-8");
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim());
        try{
	        Iterable<CSVRecord> csvRecords = csvParser.getRecords();
	        String[] data;
	        int columnCount = csvParser.getHeaderMap().size();
	        for (CSVRecord csvRecord : csvRecords) {
	        	data = new String[columnCount];
	        	for(int n=0;n<columnCount;n++){
	        		data[n] = csvRecord.get(n);
	        	}
	        	lstLine.add(data);
	        }
        }catch(Exception e){
        	FormLogger.logFlow("导入异常," + e.getMessage(), FormLogger.LOG_TYPE_ERROR);
        }finally{
        	csvParser.close();
        	reader.close();
        }
		return lstLine;
	}
	
	/**
	 * 原导入数据的核心处理逻辑
	 * @param controlList
	 * @param lstLine
	 * @param paramForeign
	 * @param paramDict
	 * @return
	 */
	public static List<Map<String,Object>> genTableData(ServerTokenData serverTokenData,List<String[]> lstLine){
	    Procedure procInput = FormConfiguration.getProcedure(serverTokenData);//temp
	    Param paramInput = procInput.getParam(Param.P_InputPanelId);
	    Param pDict = procInput.getParam(Param.P_Dict);
	    UI ui = ((XML) ContextUtils.getAttrbute(ConstantUtil.XML)).getUI(paramInput.getValue().split("\\.")[0]);
	    Panel panel = ui.getUiPanel(paramInput.getValue().split("\\.")[1]);
	    List<Control> controlList = panel.getControlList();
	    
	    String[] arrDict = ObjectUtils.toString(pDict==null?"":pDict.getValue()).split(",");
		
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<Control> displayList = new ArrayList<Control>();
		for(Control c:controlList){
			if(c.isEdit()&&!c.getType().equals("Hidden")){
				displayList.add(c);
			}
		}
		Map<String,Object> map ;
		int i = 0 ;
        for (String[] arrData : lstLine) {
        	map = new HashMap<>();
        	list.add(map);
        	genInitial(controlList,map);
			StringBuffer checkResult = new StringBuffer();
			i++;
			String cellContent = "";
			for (int j=0;j<displayList.size();j++) {
				if(j>=arrData.length){
					FormLogger.logFlow("忽略不含值j=" + j, FormLogger.LOG_TYPE_WARN);
					cellContent = "";
				}else{
					cellContent = arrData[j];
				}
				map.put(displayList.get(j).getName(),cellContent);
				for(String str:arrDict){
					if(displayList.get(j).getName().equals(str.split("\\|")[0]) && !StringUtils.isEmpty(cellContent)){
						QueryPage qp= new QueryPage();
						qp.setQueryParam(str.split("\\|")[1]);
						if(str.split("\\|").length==3){
							Map<String, Object> dataMap = new HashMap<String, Object>();
							dataMap.put(str.split("\\|")[2], map.get(str.split("\\|")[2]));
							qp.setDataByMap(dataMap);
						}
						
						List<Map<String,Object>> dicList = excelUtil.formService.queryBySqlId(qp);//sql返回列中需含value,text两列。额外特性列selected
						
						boolean flg = false;
						if(dicList!=null && dicList.size()>0)
						for(Map<String, Object> dicMap:dicList){
							if(dicMap.get("text").equals(cellContent)){
								map.put(str.split("\\|")[0]+"_name", dicMap.get("text"));
								map.put(str.split("\\|")[0], dicMap.get("value"));
								flg = true;
								break;
							}
						}
						if(!flg){
							map.put(str.split("\\|")[0]+"_name", null);
							map.put(str.split("\\|")[0], null);
							checkResult.append(cellContent).append(" 对应的字典不存在");
						} 
					}
				}
				if(procInput.getParam(Param.P_Foreign)!=null){
					String[] arrForeign = procInput.getParam(Param.P_Foreign).getValue().split(",");
					for(String str:arrForeign){
						if(!StringUtils.isEmpty(str)&&displayList.get(j).getName().equals(str.split("\\|")[2])&&!StringUtils.isEmpty(cellContent)){
							QueryPage queryPage = new QueryPage();
							queryPage.setPage(1);
							queryPage.setRows(1);
							queryPage.setQueryParam("select * from "+str.split("\\|")[1].split("\\.")[0]
									+" where "+str.split("\\|")[1].split("\\.")[1]+"='"+cellContent+"'");
							Map<String,Object> result = excelUtil.sqlSession.selectOne("query", queryPage);
							if(result==null){
								checkResult.append(cellContent).append(" 对应的外键不存在");
								map.put(str.split("\\|")[0], null);
							} else {
								int len = str.split("\\|").length;
								if(len ==4){
									map.put(str.split("\\|")[0], result.get("id")==null?result.get("code"):result.get(str.split("\\|")[3])); //取表中具体某列的值，非id/code外的列
								}else{
									map.put(str.split("\\|")[0], result.get("id")==null?result.get("code"):result.get("id"));
								}
								
							}
						}
					}
				}				
				}
			map.put("验证结果", checkResult.length()==0?"成功":checkResult);

        }			
        return list;
	}
	/**
	 * @MethodName : getFieldByName
	 * @Description : 根据字段名获取字段
	 * @param fieldName
	 *            字段名
	 * @param clazz
	 *            包含该字段的类
	 * @return 字段
	 */
	public static Field getFieldByName(String fieldName, Class<?> clazz) {
		// 拿到本类的所有字段
		Field[] selfFields = clazz.getDeclaredFields();

		// 如果本类中存在该字段，则返回
		for (Field field : selfFields) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}

		// 否则，查看父类中是否存在此字段，如果有则返回
		Class<?> superClazz = clazz.getSuperclass();
		if (superClazz != null && superClazz != Object.class) {
			return getFieldByName(fieldName, superClazz);
		}

		// 如果本类和父类都没有，则返回空
		return null;
	}

	/**
	 * 根据字段名得到实例的字段值
	 * 
	 * @param object
	 *            实例对象
	 * @param fieldName
	 *            字段名称
	 * @return 实例字段的值，如果没找到该字段则返回null
	 * @throws IllegalAccessException
	 */
	public static Object getFieldValue(Object object, String fieldName) throws IllegalAccessException {
		Set<Field> fields = new HashSet<Field>();
		// 本类中定义的所有字段
		Field[] tempFields = object.getClass().getDeclaredFields();
		for (Field field : tempFields) {
			field.setAccessible(true);
			fields.add(field);
		}
		// 所有的public字段，包括父类中的
		tempFields = object.getClass().getFields();
		for (Field field : tempFields) {
			fields.add(field);
		}

		for (Field field : fields) {
			if (field.getName().equals(fieldName)) {
				return field.get(object);
			}
		}
		return null;
	}

	/**
	 * @MethodName : setFieldValueByName
	 * @Description : 根据字段名给对象的字段赋值
	 * @param fieldName
	 *            字段名
	 * @param fieldValue
	 *            字段值
	 * @param o
	 *            对象
	 */
	private static void setFieldValueByName(String fieldName, Sheet sheet, Object o, Map<String, Object> config,
			int index, int col, List<?> resultList) throws Exception {

		Field field = getFieldByName(fieldName, o.getClass());
		if (field != null) {
			field.setAccessible(true);
			// 获取字段类型
			Class<?> fieldType = field.getType();

			// excel单元格长度检查
			String data = sheet.getCell(col, index).getContents().toString().trim();
			if (config.get("length") != null) {
				if (data.length() > (Integer) config.get("length")) {
					Field checkResult = getFieldByName("checkResult", o.getClass());
					checkResult.setAccessible(true);
					checkResult.set(o, "第" + index + "行，" + config.get("display_name") + "字段超长");
				}
			}

			// 正则检查
			if (config.get("regular") != null) {
				String check = config.get("regular").toString();
				Pattern regex = Pattern.compile(check);
				Matcher matcher = regex.matcher(data);
				if (!matcher.matches()) {
					field.set(o, "第" + index + "行，" + config.get("display_name") + "表达式正则错误");
				}
			}
			// 外键设置
			// if (config.get("foreign_key") != null) {
			// String check = config.get("foreign_key").toString();
			// if(foreignResult.get(check) == null){
			// List<String> foreignList = new ArrayList<String>();
			// foreignList.add(data);
			// foreignResult.put(check, foreignList);
			// } else{
			// foreignResult.get(check).add(data);
			// }
			// return;
			// }
			// 类型检查
			if ((String.class == fieldType) && !StringUtils.isEmpty(data)) {
				field.set(o, data);
			} else if ((Integer.TYPE == fieldType || Integer.class == fieldType) && !StringUtils.isEmpty(data)) {
				try {
					field.set(o, Integer.parseInt(data));
				} catch (NumberFormatException e) {
					field.set(o, "第" + index + "行，" + config.get("display_name") + "必须是一个整数");
				}
			} else if ((Long.TYPE == fieldType || Long.class == fieldType) && !StringUtils.isEmpty(data)) {
				try {
					field.set(o, Long.valueOf(data));
				} catch (NumberFormatException e) {
					field.set(o, "第" + index + "行，" + config.get("display_name") + "必须是一个long型");
				}
			} else if ((Float.TYPE == fieldType || Float.class == fieldType) && !StringUtils.isEmpty(data)) {
				try {
					field.set(o, Float.valueOf(data));
				} catch (NumberFormatException e) {
					field.set(o, "第" + index + "行，" + config.get("display_name") + "必须是一个Float型");
				}
			} else if ((Short.TYPE == fieldType || Short.class == fieldType) && !StringUtils.isEmpty(data)) {
				try {
					field.set(o, Short.valueOf(data));
				} catch (NumberFormatException e) {
					field.set(o, "第" + index + "行，" + config.get("display_name") + "必须是一个Short型");
				}
			} else if ((Double.TYPE == fieldType || Double.class == fieldType) && !StringUtils.isEmpty(data)) {
				try {
					field.set(o, Double.valueOf(data));
				} catch (NumberFormatException e) {
					field.set(o, "第" + index + "行，" + config.get("display_name") + "必须是一个Double型");
				}
			} else if ((Character.TYPE == fieldType && !StringUtils.isEmpty(data)) && !StringUtils.isEmpty(data)) {
				if (data != null && data.length() > 0) {
					field.set(o, Character.valueOf(data.charAt(0)));
				}
			} else if (Date.class == fieldType && !StringUtils.isEmpty(data)) {
				try {
					field.set(o, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data.toString()));
				} catch (Exception e) {
					field.set(o, "第" + index + "行，" + config.get("display_name") + "日期格式必须是yyyy-MM-dd HH:mm:ss");
				}
			}

		} else {
			throw new ExcelException(o.getClass().getSimpleName() + "类不存在字段名 " + fieldName);
		}
	}

	// 写入导入失败日志
	public static ResponseJSON writeLog(HttpServletRequest request, ResponseJSON responseJSON, List<String> checkList,
			String batId) throws IOException, IllegalAccessException, InvocationTargetException {
		StringBuffer content = new StringBuffer();
		String downlloadPath = batId + ".txt";
		for (int i = 0; i < checkList.size(); i++) {
			if (!StringUtils.isEmpty(checkList.get(i))) {
				// 发生部分错误
				responseJSON.setStatus(1);
				if ("外键不存在".equals(checkList.get(i)))
					content.append("第" + i + "行外键重复").append("\n");
				content.append(checkList.get(i)).append("\n");
			}
		}
		File f = new File(request.getSession().getServletContext().getRealPath("/excelLog"));
		if (!f.exists())
			f.mkdirs();
		RandomAccessFile mm = null;
		FileOutputStream o = null;
		try {
			o = new FileOutputStream(
					request.getSession().getServletContext().getRealPath("/excelLog/") + "excelLog_" + downlloadPath);
			o.write(content.toString().getBytes("GBK"));
			o.close();
			// mm=new RandomAccessFile(fileName,"rw");
			// mm.writeBytes(content);
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			if (mm != null) {
				mm.close();
			}
		}
		request.getSession().getServletContext().getRealPath("/a");
		return responseJSON;
	}

	// 下载文件
	public static void downLoad(HttpServletRequest request, HttpServletResponse response, String batId, String path)
			throws IOException, IllegalAccessException, InvocationTargetException {

		String fileName = "excelLog_" + batId + ".txt";
		if (fileName != null) {
			String realPath = request.getSession().getServletContext().getRealPath(path) + fileName;
			File file = new File(realPath);
			if (file.exists()) {
				response.setContentType("application/force-download");// 设置强制下载不打开
				response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
				byte[] buffer = new byte[1024];
				FileInputStream fis = null;
				BufferedInputStream bis = null;
				try {
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					OutputStream os = response.getOutputStream();
					int i = bis.read(buffer);
					while (i != -1) {
						os.write(buffer, 0, i);
						i = bis.read(buffer);
					}
				} catch (Exception e) {
					// TODO: handle exception
					log.error(e.getMessage());
				} finally {
					if (bis != null) {
						try {
							bis.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							log.error(e.getMessage());
						}
					}
					if (fis != null) {
						try {
							fis.close();
						} catch (IOException e) {
							log.error(e.getMessage());
						}
					}
				}
			}
		}
	}

	public static void continueImport(String batId, String tableName)
			throws IllegalArgumentException, IllegalAccessException {
		excelUtil.commonService.saveFinalTable(batId, tableName);
	}
}