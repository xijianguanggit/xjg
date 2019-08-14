package org.dubbo.consumer;

import org.dubbo.api.DemoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.extension.factory.AdaptiveExtensionFactory;
import com.alibaba.dubbo.common.extension.factory.SpiExtensionFactory;

/**
 * Hello world!
 *
 */
public class Consumer 

{
    public static void main(String[] args) {
        //测试常规服务
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("consumer.xml");
        context.start();
        System.out.println("consumer start");
        DemoService demoService = (DemoService) context.getBean(DemoService.class);
//        System.out.println("consumer");
//        System.out.println(demoService.getPermissions(1L));
//        ExtensionLoader<DemoService> extensionLoader = 
//                ExtensionLoader.getExtensionLoader(DemoService.class);
//        DemoService demoService = extensionLoader.getExtension("demoService1");
        System.out.println(demoService.getPermissions(1L));
        
    }
}
