package org.dubbo.api;

import java.util.List;

import com.alibaba.dubbo.common.extension.SPI;

/**
 * Hello world!
 *
 */
@SPI(value="1111")
public interface DemoService 
{
	List<String> getPermissions(Long id);
}
