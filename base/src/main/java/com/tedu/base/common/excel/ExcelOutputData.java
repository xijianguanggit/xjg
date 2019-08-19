package com.tedu.base.common.excel;

import java.util.List;

public class ExcelOutputData {
	String [][] excelTitle;
	String fileName;
	List<?> excelData;
	public String[][] getExcelTitle() {
		return excelTitle;
	}
	public void setExcelTitle(String[][] excelTitle) {
		this.excelTitle = excelTitle;
	}
	public List<?> getExcelData() {
		return excelData;
	}
	public void setExcelData(List<?> excelData) {
		this.excelData = excelData;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
