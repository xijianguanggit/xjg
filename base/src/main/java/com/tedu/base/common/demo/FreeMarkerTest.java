package com.tedu.base.common.demo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.template.TemplateException;  
  
public class FreeMarkerTest {  
    @Test  
    public void printInc() throws TemplateException, IOException{  
        Map<String,Object> rootMap = new HashMap<String,Object>();  
        rootMap.put("visitor", "吴邪");  
        new FreeMarkerUtil().print("theImport.ftl", "/ftl", rootMap);  
        new FreeMarkerUtil().printFile("theImport.ftl", "/ftl", rootMap, new File("D:\\ftl\\my.html"));  
    }  
}  