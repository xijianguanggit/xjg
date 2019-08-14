package org.dubbo.provider.impl;

import java.util.ArrayList;
import java.util.List;

import org.dubbo.api.DemoService;

import com.alibaba.dubbo.config.annotation.Service;
@Service
public class DemoServiceImpl implements DemoService {

	public List<String> getPermissions(Long id) {
		List<String> demo = new ArrayList<String>();
		demo.add("DemoServiceImpl");
		return demo;
	}

}
