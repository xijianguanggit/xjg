package com.tedu.base.file.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.tedu.base.file.dao.DocumentMapper;
import com.tedu.base.file.dao.MarkDownDocMapper;
import com.tedu.base.file.model.MarkDownDocModel;
import com.tedu.base.file.service.MarkDownDocService;

/**
 * 文档表service接口实现类
 * @author hejk
 *
 */
@Service("markDownDocService")
public class MarkDownDocServiceImpl implements MarkDownDocService {
	@Resource
	public MarkDownDocMapper markDownDocMapper;
	Date date = new Date();
	@Value("${file.upload.path}")
	private String DISH;


	@Override
	public void insertMDownDoc(MarkDownDocModel markdown) {
		markDownDocMapper.insertMDownDoc(markdown);
		
	}


	@Override
	public void del() {
		markDownDocMapper.del();
	}
}
