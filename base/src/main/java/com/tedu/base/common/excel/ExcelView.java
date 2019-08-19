package com.tedu.base.common.excel;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.tedu.base.engine.model.ServerTokenData;
import com.tedu.base.engine.service.FormTokenService;
import com.tedu.base.engine.util.FormConfiguration;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.initial.model.xml.ui.Control;
import com.tedu.base.initial.model.xml.ui.Panel;
import com.tedu.base.initial.model.xml.ui.Param;
import com.tedu.base.initial.model.xml.ui.Procedure;
import com.tedu.base.task.SpringUtils;

public class ExcelView extends AbstractExcelView {
	public static final Logger log = Logger.getLogger(ExcelView.class);
	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String token = request.getParameter("token");
		if(token!=null && !token.isEmpty()) {
			configImportTemplate(response,workbook,request.getParameter("token"));
		}else {
			configDefault(response,model,workbook);
		}

		response.setCharacterEncoding("UTF-8");  
		response.setContentType("application/ms-excel");// 文件下载  
	}

	private void configDefault(HttpServletResponse response,Map<String, Object> model,HSSFWorkbook workbook) throws UnsupportedEncodingException {
		ExcelOutputData excelOutputData = (ExcelOutputData) model.get("excelOutputData");
		Object object = excelOutputData.getExcelData();
		HSSFSheet sheet = workbook.createSheet("导出结果");// 创建一页
		HSSFRow header = sheet.createRow(0);// 创建第一行

		for (int i = 0; i < excelOutputData.getExcelTitle().length; i++) {
			HSSFCell cell = header.createCell(i);
			cell.setCellValue(excelOutputData.getExcelTitle()[i][0]);
		}
		if (object != null) {
			List<?> data = (List<?>) object;
			for (int i = 0; i < data.size(); i++) {
				HSSFRow row = sheet.createRow(i + 1);
				List<String> result = this.getString(data.get(i), data.get(i).getClass(), excelOutputData.getExcelTitle());
				for (int j = 0; j < result.size(); j++) {
					row.createCell(j).setCellValue(result.get(j));
				}
			}
		}else {
			response.setHeader("Content-Disposition", "inline; filename=" + new String(excelOutputData.getFileName().getBytes(), "iso8859-1"));
		}
	}
	
	/**
	 * 导入模板
	 * @param response
	 * @param workbook
	 * @param token
	 */
	private void configImportTemplate(HttpServletResponse response,HSSFWorkbook workbook,String token) {
		HSSFCellStyle style=workbook.createCellStyle();
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.RED.index);
		style.setFont(font);
		HSSFSheet sheet = workbook.createSheet("导入模板");
		sheet.setDefaultColumnWidth(10);
		HSSFRow header = sheet.createRow(0);// 创建第一行	      
		
		try {
			FormTokenService formTokenService = SpringUtils.getBean("formTokenService");
			ServerTokenData serverTokenData = formTokenService.geToken(token);
			Procedure proc = FormConfiguration.getProcedure(serverTokenData);
			Param paramIn = proc.getParam(Param.P_InputPanelId);
			Param paramTitle = proc.getParam(Param.P_Title);
			String strIn = paramIn.getValue();
			String[] arrParamIn = strIn.split("[.]");
			if(arrParamIn.length>1) {
				String uiName = arrParamIn[0];
				String panelName = arrParamIn[1];
				Panel p = FormConfiguration.getPanel(uiName, panelName);
				List<Control> controlList = p.getControlList();
				int pos = 0;
				for(Control c:controlList)
				{
					if(c.isEdit()&&!c.getType().equals("Hidden")){
						HSSFCell cell = header.createCell(pos++);
						cell.setCellValue(c.getTitle());
						if(c.isRequired()) {
							cell.setCellStyle(style);				
						}
					}
				}
			}
			String strTitle = paramTitle==null?"导入模板":paramTitle.getValue()+".xls";
			response.setHeader("Content-Disposition", "inline; filename=" + new String(strTitle.getBytes(), "iso8859-1"));
		}catch(Exception e) {
			FormLogger.logFlow("通过token获取BatchImport逻辑相关必填项时出错:" + e.getMessage(), FormLogger.LOG_TYPE_WARN);
		}
	}
	
	private List<String> getString(Object o, Class<?> c, String[][] title) {
		List<String> result = new ArrayList<String>();
		
		for (int i = 0; i < title.length; i++) {
			try {
				Field field = c.getDeclaredField(title[i][1]);
				field.setAccessible(true);
				result.add(field.get(o)==null?"":field.get(o).toString());
			} catch (NoSuchFieldException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
			}
		}
		return result;
	}
}