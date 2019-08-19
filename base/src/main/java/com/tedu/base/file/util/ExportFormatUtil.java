package com.tedu.base.file.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



/**
 * 带格式导出excel工具类，其中包含:读取模板文件->遍历模板内容->占位符替换->复制模板信息->保存新报表文件
 * @author gaolu
 *
 */
public class ExportFormatUtil {
	private static final Logger log = Logger.getLogger(ExportFormatUtil.class);
	private String path ;
	
	public ExportFormatUtil(String path){
		this.path = path;
	}
	
/*	public static void main(String[] args) {
		Map<String, List<Map>> testData = new HashMap<>();
		new ExportFormatUtil("").getExcelFormat("",testData,"");
	}*/
	
	
/*	public Map<String, List<Map>> readyData(){
		//设置测试值
		Map<String, List<Map>> TEST = new HashMap<String,List<Map>>();
		
		Map id = new HashMap<>();
		Map id1 = new HashMap<>();
		Map id2 = new HashMap<>();
		id.put("id1", "1");
		id.put("id2", "2");
		id1.put("id1", "11");
		id1.put("id2", "22");
		id2.put("id1", "111");
		id2.put("id2", "222");
		List<Map> listId = new ArrayList<>();
		listId.add(id);
		listId.add(id1);
		listId.add(id2);
		
		Map name = new HashMap<>();
		name.put("name1", "Tom");
		name.put("name2", "Jerry");
		List<Map> listName = new ArrayList<>();
		listName.add(name);
		
		Map age = new HashMap<>();
		age.put("age", "18");
		List<Map> listAge = new ArrayList<>();
		listAge.add(age);
		
		TEST.put("id", listId);
		TEST.put("name", listName);
		TEST.put("age", listAge);
		
		return TEST;
	}*/
	/**
	 * 
	 * @Description: 带格式导出excel工具接口，入参模板路径和数据，返回文件流
	 * @author: gaolu
	 * @date: 2017年10月23日 下午4:28:41  
	 * @param:      
	 * @return: void
	 */
	public void getExcelFormat(String modelPath,Map<String, List<Map>> Data,String savePath) {
		//modelPath = "D:\\files\\model\\introduce.xls";
		FileInputStream fileIS;
		try{
			//转换为文件流
			File fileXls = new File(modelPath);
			fileIS = new FileInputStream(fileXls);
			//FileInputStream  fis = new FileInputStream(modelPath);
			//POIFSFileSystem  fs = new POIFSFileSystem(fis);

			
			//获得XSSFWorkbook对象
			//XSSFWorkbook xlsxWB = readFile(fileIS);
			HSSFWorkbook xlsxWB = new HSSFWorkbook(fileIS);
			
			//复制并赋值
			xlsxWB = copySheet(xlsxWB,Data);
			
			//保存
			saveReport(xlsxWB,savePath);
			
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 
	 * @Description: 读取模板文件
	 * @author: gaolu
	 * @date: 2017年10月23日 下午4:12:45  
	 * @param:      
	 * @return: void
	 */
	public XSSFWorkbook readFile(FileInputStream fileIS) throws Exception{
		XSSFWorkbook xlsxWB = null;	//07以上
		HSSFWorkbook xlsWB = null;	//03以下
		//通过文件流转换为XSSFWorkbook
		if (fileIS!=null) {
			//xlsWB = new HSSFWorkbook(inputStream);
			xlsxWB = new XSSFWorkbook(fileIS);
		}
		return xlsxWB;
	}
	
	/**
	 * 
	 * @Description: 复制sheet页
	 * @author: gaolu
	 * @date: 2017年10月23日 下午4:14:10  
	 * @param:      
	 * @return: void
	 */
	public HSSFWorkbook copySheet(HSSFWorkbook xlsWB,Map<String, List<Map>> TEST) throws Exception{
		
		boolean flag = false;
		//得到第一个sheet
		Sheet sheet = xlsWB.getSheetAt(0);
		//判断sheet是否为空
		if(sheet != null){
			//循环sheet里面的行
			for (int i = 0; i <= sheet.getLastRowNum(); i++) {
				//System.out.println("第"+i+"行数据");
				Row row = sheet.getRow(i);
				if (row!=null) {
					for (int j = 0; j < row.getLastCellNum(); j++) {
						//获得一个单元格信息
						String text = getCellValue(row.getCell(j));
						//检测到有#{start}的标志则要开始循环行，循环行步骤是：复制并插入行->插入数据，执行完之后直接break跳出本行循环
						if(StringUtils.isNotBlank(text)){
							if (text.contains("#{start}")) {
								sheet = replaceData4Line(text,TEST,sheet,i);
								break;
							}else if (text.contains("#{sfixL}")) {
								sheet = replaceData4FixLine(TEST,sheet,i,j);
								i = i-1;
								break;
							}else{
								//遍历每一个单元格，如果包含占位符的就替换，如果是没有占位符的就保留原有内容
								if (judgeCellValue(text)) {
									//报表头数据替换占位符
									text = replaceData4Head(text,TEST);
								}
							}
							row.getCell(j).setCellValue(text);
						}
					}
				}
			}
		}else{//模板为空抛异常
			throw new Exception("模板为空,请重新上传模板！");
		}
		return xlsWB;
	}
	
	/**
	 * 
	 * @Description: 判断cell是否含有占位符
	 * @author: gaolu
	 * @date: 2017年10月23日 下午4:16:59  
	 * @param:      
	 * @return: void
	 */
	public boolean judgeCellValue(String Cellvalue){
		boolean flag = false;
		//正则去判断单元格内容是否包含占位符
		Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(Cellvalue);
		if (matcher.matches()) {
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 
	 * @Description: 保存报表
	 * @author: gaolu
	 * @date: 2017年10月23日 下午4:17:28  
	 * @param:      
	 * @return: void
	 */
	public void saveReport(HSSFWorkbook wb,String path) throws Exception{
        FileOutputStream fileOut = null;  
        //path = "D:\\files\\model\\test11.xls";
        if(StringUtils.isNotEmpty(path)){
        	fileOut = new FileOutputStream(path);  
        	wb.write(fileOut);  
        	if(fileOut != null){
        		fileOut.close();
        	}
        }
	}
	
	/**
	 * 
	 * @Description: 获取单元格值
	 * @author: gaolu
	 * @param:      
	 * @return: String
	 */
	private  String getCellValue(Cell cell) {
        Object result = "";  
        if (cell != null) {
            switch (cell.getCellType()) {  
            case Cell.CELL_TYPE_STRING:  
                result = cell.getStringCellValue();  
                break;  
            case Cell.CELL_TYPE_NUMERIC:  
                result = cell.getNumericCellValue();  
                break;  
            case Cell.CELL_TYPE_BOOLEAN:  
                result = cell.getBooleanCellValue();  
                break;  
            case Cell.CELL_TYPE_FORMULA:  
                result = cell.getCellFormula();  
                break;  
            case Cell.CELL_TYPE_ERROR:  
                result = cell.getErrorCellValue();  
                break;  
            case Cell.CELL_TYPE_BLANK:  
                break;  
            default:  
                break;  
            }  
        }
        return result.toString();  
    }

	/**
	 * 
	 * @Description: 数据一行填数据
	 * @author: gaolu
	 * @date: 2017年10月24日 下午4:13:21  
	 * @param:      
	 * @return: void
	 */
	private String replaceData4Head(String cellValue, Map<String, List<Map>> Data){
		String DataValue = "";
		Map<String, String> controlMap = new HashMap<>();
		if (Data!=null) {
			cellValue = cellValue.replace("${", "").replace("}", "");
			//根据格式将占位符分为数组，分别为panel，control，String（类型）
			String[] cellValues = cellValue.split("\\.");
			controlMap = (Map<String, String>)Data.get(cellValues[0]).get(0);
			//缺格式转换
			DataValue = controlMap.get(cellValues[1]);
			/*if ("${id.id2.String}".equals(cellValue)) {
				DataValue = "#{start}${id.id2.String}";
			}else if("${name.name1.String}".equals(cellValue)){
				DataValue = "";
			}*/
		}
			
			
		return DataValue;
	}
	
	/**
	 * 
	 * @Description: 数据多行在固定单元格里填数据
	 * @author: gaolu
	 * @date: 2017年10月31日 上午2:13:01  
	 * @param:      
	 * @return: Sheet
	 */
	private Sheet replaceData4FixLine(Map<String,List<Map>> Data,Sheet sheet,Integer rowX,Integer cellY){
		Row sourceRow = null;	
		Cell sourceCell = null;
		Cell targetCell = null;
		String targetValue = null;
		int cellCount = 0;
		int rowCount = 0;

		//获取列表数据
		String cellValue = getCellValue(sheet.getRow(rowX).getCell(cellY)).replace("#{sfixL}${", "").replace("}", "");
		String[] cellValues = cellValue.split("\\.");
		List<Map> lineDataList = Data.get(cellValues[0]);
		
		//获取循环的行数
		for(int x = rowX; x<sheet.getLastRowNum(); x++){
			sourceRow = sheet.getRow(x);
			sourceCell = sourceRow.getCell(cellY);
			cellValue = getCellValue(sourceCell);
			if (!cellValue.contains("#{sfixL}")) break;
			rowCount = x;
		}
		
		//获取循环的列数
		sourceRow = sheet.getRow(rowX);
		for(int y = cellY; y<sheet.getRow(rowX).getLastCellNum(); y++){
			sourceCell = sourceRow.getCell(y);
			cellValue = getCellValue(sourceCell);
			cellCount = y;
			if (cellValue.contains("#{efixL}")) break;
		}
		
		//根据占位符取到相应数据的临时值
		String value = null;
		//一列一列的循环填数据
		for (int i = cellY; i <= cellCount; i++) {
			//这里在循环行的时候是倒序循环的，因为如果顺序会将带占位符的第一行填充数据门后面的循环就无法取到第一行的占位符
			for (int j = rowCount; j >= rowX; j--) {
				//根据第一行的占位符取数字
				sourceRow = sheet.getRow(rowX);
				sourceCell = sourceRow.getCell(i);
				cellValue = getCellValue(sourceCell);
				cellValue = cellValue.replace("#{sfixL}", "").replace("#{efixL}", "");
				cellValue = cellValue.replace("${", "").replace("}", "");
				cellValues = cellValue.split("\\.");	
				value = (String)lineDataList.get(j-rowX).get(cellValues[1]);
				
				sheet.getRow(j).getCell(i).setCellValue(value);
			}
		}
		
		return sheet; 
	}
	
	/**
	 * 
	 * @Description: 数据多行复制行并添加数据
	 * @author: gaolu
	 * @date: 2017年10月24日 下午4:13:21  
	 * @param:      
	 * @return: void
	 */
	private Sheet replaceData4Line(String cellValue, Map<String,List<Map>> Data,Sheet sheet,Integer i){
		
		Integer lineLength = null; //行数据list长度
		
		//获取循环行的数据长度和数据list
		cellValue = cellValue.replace("#{start}", "").replace("#{end}", "");
		cellValue = cellValue.replace("${", "").replace("}", "");
		String[] cellValues = cellValue.split("\\.");
		List<Map> lineDataList = Data.get(cellValues[0]); 
		lineLength = lineDataList.size();
		
		if (lineLength!=null) {
			//复制并增加行
			sheet = insertRow(sheet, i, lineLength);
			if(lineLength==0){
				Row row=sheet.getRow(i);
				for(int m=0;m<row.getLastCellNum();m++){
					String text="";
					row.getCell(m).setCellValue(text);
				}
			}else{
			//循环行添加数字
			for(int x = i; x<(i+lineLength); x++){
				Row row = sheet.getRow(x);
				for(int y = 0; y<row.getLastCellNum(); y++){
					//获得一个单元格信息
					String text = getCellValue(row.getCell(y));
					if(StringUtils.isNotBlank(text)){
						//解析占位符找到行对应control名字从数据List里找到对应数据填入
						if (text.contains("${")) {
							if (text.contains("#")) {
								text = text.replace("#{start}", "").replace("#{end}", "");
							}
							int index = text.indexOf("$");
							text = text.substring(index+2, text.length()-1);
							String[] lineCellValues = text.split("\\.");
							text = (String) lineDataList.get(x-i).get(lineCellValues[1]);
						}
					}
					row.getCell(y).setCellValue(text);
				}
			}
		}
	}
		return sheet;
}
	
	/**
	 * 
	 * @Description: 复制并插入行
	 * @author: gaolu
	 * @date: 2017年10月25日 下午2:37:58  
	 * @param:      
	 * @return: Sheet
	 */
	private Sheet insertRow(Sheet sheet,Integer i,Integer lineLength){
		   Row sourceRow = null;
		   Row targetRow = null;
		   Cell sourceCell = null;
		   Cell targetCell = null;
		   short m = 0;
		
	    //循环数据列，复制并插入行
		for(int a = 0; a<lineLength-1; a++){
			sourceRow = sheet.getRow(i);
			sheet.shiftRows(i+a, sheet.getLastRowNum(), 1);
			targetRow = sheet.createRow(i+a);
			//循环列，复制每一列的内容和格式
			for (m = sourceRow.getFirstCellNum(); m < sourceRow.getLastCellNum(); m++){
			    sourceCell = sourceRow.getCell(m);
			    targetCell = targetRow.createCell(m);
			    if(sourceCell.getCellComment() != null) {
			    	targetCell.setCellComment(sourceCell.getCellComment());
		        }
			    
			    targetCell.setCellValue(getCellValue(sourceCell));
			    targetCell.setCellStyle(sourceCell.getCellStyle());
			    targetCell.setCellType(sourceCell.getCellType());
			}
		}
		
		return sheet;
	}
}
