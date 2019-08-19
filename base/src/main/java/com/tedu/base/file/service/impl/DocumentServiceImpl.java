package com.tedu.base.file.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.file.dao.DocumentMapper;
import com.tedu.base.file.model.DocumentModel;
import com.tedu.base.file.service.DocumentService;

/**
 * 文档表service接口实现类
 * @author hejk
 *
 */
@Service("documentService")
public class DocumentServiceImpl implements DocumentService {
	@Resource
	public DocumentMapper documentMapper;
	Date date = new Date();
	@Value("${file.upload.path}")
	private String DISH;


	/**
	 * 操作文档表(insert/update).
	 * @author hejiakuo 
	 * @param docModel
	 * @return void
	 */
	@Override
	public void handleDocument(String fileId,String cataId,String fileName) {
		Date date = new Date();
		DocumentModel docModel =  null;
		//update or insert ?
		if(cataId == null || cataId == ""){
			//update
			docModel = documentMapper.getFileById(Integer.parseInt(fileId));
			docModel.setTitle(fileName);
			docModel.setUpdateBy(Integer.parseInt(SessionUtils.getUserInfo().getEmpId()+""));
			docModel.setUpdateTime(DateUtils.getDateToStr("yyyy-MM-dd HH:mm:ss", date));
			
			documentMapper.updateDocById(docModel);
		}else{
			//insert
			docModel = documentMapper.getFileByFileId(Integer.parseInt(cataId),Integer.parseInt(fileId));
			if(docModel==null){
				//insert
				docModel = new DocumentModel();
				docModel.setFileId(Integer.parseInt(fileId));
				docModel.setCataId(Integer.parseInt(cataId));
				docModel.setTitle(fileName);
				docModel.setCreateBy(Integer.parseInt(SessionUtils.getUserInfo().getEmpId()+""));
				docModel.setCreateTime(DateUtils.getDateToStr("yyyy-MM-dd HH:mm:ss", date));
				docModel.setKeyword("");
				docModel.setStatus("");
			}else{
				
			}
			documentMapper.handleDocument(docModel);
		}
		
	}


	@Override
	public void updateDocById(DocumentModel dm) {
		documentMapper.updateDocById(dm);
	}

	@Override
	public DocumentModel getFileById(Integer id) {
		return documentMapper.getFileByFileId(id);
	}

	@Override
	public DocumentModel getFileByFileId(Integer cataId, Integer fileId) {
		return documentMapper.getFileByFileId(cataId, fileId);
	}

	@Override
	public DocumentModel getFileByFileId(Integer fileId) {
		return documentMapper.getFileByFileId(fileId);
	}
}
