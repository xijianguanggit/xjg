package com.tedu.base.file.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tedu.base.common.model.TreeItem;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.file.service.FileService;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/12/18
 */
@Controller
public class CodeController {
    @Resource
    private FileService fileService;

    @RequestMapping("saveCode")
    @ResponseBody
    public Map<String, String> saveCode(HttpServletRequest request, HttpServletResponse response) {
        String filePath = request.getParameter("uniqueCode");
        String fileContent = ObjectUtils.toString(request.getParameter("editorText"));
        String fileName = request.getParameter("fileName");
        
        Map<String, String> map = new  HashMap<String, String>();
        try {
        	String strFilepath;
        	File currentFile = new File(filePath);
        	if(currentFile.isDirectory()){
        		strFilepath = filePath.concat(File.separator).concat(fileName);
        	}else{
        		strFilepath =currentFile.getParent().concat(File.separator).concat(fileName);
        	}
            //创建文件
            Boolean bool = createFile(strFilepath,fileContent);

            //上传成功返回1，失败返回0
            if (bool) {
            	map.put("msg", "1");
			}else{
				map.put("msg", "0");
			}
            //入库

        }catch (Exception e) {
        	FormLogger.error("codemirror保存接口",String.format(filePath+"目录下,editCode文件保存失败"), FormLogger.LOG_TYPE_ERROR);
        }
        
        return map;
    }




   private Boolean createFile(String filePath,String fileContent){
        Boolean bool = false;
        File file = new File(filePath);
       try {
           //如果文件不存在，则创建新的文件
           if(!file.exists()){
               file.createNewFile();
           }
           //写文件
           bool = writeFileContent(filePath, fileContent);
       } catch (Exception e) {
    	   FormLogger.error("codemirror保存接口",String.format("保存內容失敗"), FormLogger.LOG_TYPE_ERROR);
       }

        return bool;

   }
    private   boolean writeFileContent(String filepath,String newstr) throws IOException{
        Boolean bool = false;
        String filein = newstr+"\r\n";//新写入的行，换行
        String temp  = "";
        //filepath.replace("\\", "\\\\");
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos  = null;
        PrintWriter pw = null;
        try {
            File file = new File(filepath);//文件路径(包括文件名称)
            //将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();

            buffer.append(filein);

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
            bool = true;
        } catch (Exception e) {
        	FormLogger.error("codemirror保存接口",String.format("文件操作失敗 %s",e.getMessage()) 
        			, FormLogger.LOG_TYPE_ERROR);
        }finally {
            //不要忘记关闭
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return bool;
    }


    @RequestMapping("codeTree")
    @ResponseBody
    public FormEngineResponse showEditCode(@RequestBody FormEngineRequest requestObj, HttpServletRequest request) {
        FormEngineResponse response = new FormEngineResponse("");
        File file = new File(ObjectUtils.toString(requestObj.getData().get("catalog")));

        List<TreeItem> catalogs = new ArrayList<>();
        File[] fList = file.listFiles();
        if(fList!=null){
        	catalogs = genTree(fList,catalogs);
        }
        TreeItem  item = new TreeItem(file.getPath(),file.getName());
        List<TreeItem> retList = new ArrayList<TreeItem>();
        retList.add(item);
        item.setChildren(catalogs);
        item.setIconCls("eclipse-project");
        item.setState("open");
        if(file.isDirectory())
        	item.setDirectory(true);
        
        response.setData(retList);
        response.setCode("0");
        return response;
    }

    private List<Map<String, Object>> getTree(File [] files,List<Map<String, Object>> treeNodes){
        for (File f : files) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", f.getPath());
            map.put("text", f.getName());
            if (f.isDirectory()) {
                File file = new File(f.getPath());
                File[] fList = file.listFiles();
                List fileList = Arrays.asList(files);
                Collections.sort(fileList, new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        if (o1.isDirectory() && o2.isFile())
                            return 1;
                        if (o1.isFile() && o2.isDirectory())
                            return -1;
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                List<Map<String, Object>> mapList = new ArrayList<>();
                map.put("children",getTree(fList,mapList));
            } else {
                List<Map<String, Object>> mapList = new ArrayList<>();
                map.put("children",mapList);
            }
            treeNodes.add(map);

        }




        return treeNodes;
    }

    private List<TreeItem> genTree(File [] files,List<TreeItem> treeNodes){
    	sortFiles(files);
        for (File f : files) {
        	TreeItem node = new TreeItem(f.getPath(),f.getName());
            if (f.isDirectory()) {
            	node.setDirectory(true);
                File file = new File(f.getPath());
                File[] fList = file.listFiles();
                sortFiles(fList);
                List<TreeItem> mapList = new ArrayList<>();
                node.setChildren(genTree(fList,mapList));
            } else {
                List<TreeItem> mapList = new ArrayList<>();
                node.setChildren(mapList);
            }
            treeNodes.add(node);
        }
        return treeNodes;
    }

    private void sortFiles(File[] arrFiles){
        List fileList = Arrays.asList(arrFiles);
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o2.isDirectory() && o1.isFile())
                    return 1;
                if (o2.isFile() && o1.isDirectory())
                    return -1;
                return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
            }
        });
    }
}
