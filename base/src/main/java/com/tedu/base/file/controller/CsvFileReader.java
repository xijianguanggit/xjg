package com.tedu.base.file.controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
 
/**
 * @author ashraf_sarhan
 *
 */
public class CsvFileReader {
     
    //CSV文件头
     
    /**
     * @param fileName
     */
    public static void readCsvFile(String fileName) {
        FileReader fileReader = null;
        CSVParser csvFileParser = null;
        //创建CSVFormat（header mapping）
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader("课程名称","序号","大纲标题","学习内容","课后建议");
        try {
            //初始化FileReader object
            fileReader = new FileReader(fileName);
            //初始化 CSVParser object
            csvFileParser = new CSVParser(fileReader, csvFileFormat);
//            csvFileParser.spliterator()
            //CSV文件records
            List<CSVRecord> csvRecords = csvFileParser.getRecords(); 
            // CSV
            // 
            for (int i = 1; i < csvRecords.size(); i++) {
                CSVRecord record = csvRecords.get(i);
                //创建用户对象填入数据
                System.out.println("0:: "+record.get(0));
            
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
                csvFileParser.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
     
    /**
     * @param args
     */
    public static void main(String[] args){
        readCsvFile("d://temp.csv");
    }
 
}