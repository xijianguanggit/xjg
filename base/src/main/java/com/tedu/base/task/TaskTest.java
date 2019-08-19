package com.tedu.base.task;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

public class TaskTest {
	public final Logger log = Logger.getLogger(this.getClass());
 
	public void test() {
        try {
            // 延迟一秒返回结果
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            log.error("context", e1);
        }
			log.info(" test......................................" + (new Date()));
	}

	public void test2() {
			log.info(" test2......................................" + (new Date()));
	}
	
	public static void main(String[] args) {
		String c=null;
	    Map<String, Charset> charsets = Charset.availableCharsets();
	    for (Map.Entry<String, Charset> entry : charsets.entrySet()) {
	       System.out.println(entry.getKey());
	    }

	}
}
