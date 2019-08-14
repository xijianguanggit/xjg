package org.dubbo.provider;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.config.annotation.Service;

public class Provider {

	  public static void main(String[] args) throws IOException{
	    	
	        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:provider.xml");
	        System.out.println(context.getDisplayName() + ": here");
	        context.start();
	        
	        System.out.println("服务已经启动...");
	        System.in.read();
	    }

}
