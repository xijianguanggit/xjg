package com.tedu.base.common.demo.Aviator;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class Test {
	public static void main(String[] args) throws InterruptedException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Resource re = new ClassPathResource("applicationContext.xml");
		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
		reader.loadBeanDefinitions(re);
		FileSystemXmlApplicationContext fc = new FileSystemXmlApplicationContext("applicationContext.xml");

		//		System.out.print("");
//		   Field field = String.class.getDeclaredField("value");
//		   field.setAccessible(true);
//		   char[] value = (char[]) field.get("hello");
//		   System.out.print(value[2]);
	}
}
