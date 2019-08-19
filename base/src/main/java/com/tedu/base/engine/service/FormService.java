package com.tedu.base.engine.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.aspect.ServicePluginHelper;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.engine.util.FormUtil;
import com.tedu.base.engine.util.TemplateUtil;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.initial.model.xml.ui.Param;

@Service("formService")
public class FormService {
	@Resource
	private FormMapper formMapper;	
	@Resource
	TemplateUtil templateUtil;	
	public final Logger log = Logger.getLogger(this.getClass());
	/**
	 * 根据SqlId，执行SQL，返回list<map>
	 * 如果是优化类型的查询，有如下不同处理:
	 * 1、将查询条件panel中的control.alias作为fieldName,
	 * 2、翻译SQL文件中的$where (必须标记)
	 * 3、不在拦截器中拼where
	 */
	public List<Map<String,Object>> queryBySqlId(QueryPage queryPage){
		return formMapper.query(queryPage);
	}
	
	/**
	 *   按SQL语句内容queryParam 执行查询
	 * @param queryPage
	 * @return
	 */
	public List<Map<String,Object>> queryBySQL(QueryPage queryPage){
		return formMapper.queryBySQL(queryPage);
	}
	
	/**
	 * 根据SqlId，执行SQL，返回单条<map>
	 * 常用于初始化表单等场景
	 */
	public Map<String,Object> selectOneBySqlId(QueryPage queryPage){
		return formMapper.selectOne(queryPage);
	}
	
	/**
	 * 新增记录
	 * @param map
	 */
	public int insert(FormModel formModel){
		formMapper.checkUnique(formModel);
		return formMapper.insert(formModel);
	}
	
	/**
	 * 编辑记录
	 * 根据主键，判断执行的SQL
	 * @param map
	 */
	public int update(FormModel formModel){
		formMapper.checkUnique(formModel);
		if(formModel.getTableName() == null){
			return 0;
		}
		return formMapper.update(formModel);
	}
	
	public Object saveCustom(CustomFormModel formModel){
		return formMapper.saveCustom(formModel);
	}

	/**
	 * DAOPlugin supported
	 * @param formModel
	 * @param requestObj
	 * @return
	 */
	public Object saveCustom(CustomFormModel formModel,FormEngineRequest requestObj){
		ServicePluginHelper servicePluginHelper = 
				new ServicePluginHelper(formModel.getServerTokenData());
		servicePluginHelper.doBefore(requestObj, formModel);
		int ret = formMapper.saveCustom(formModel);
		try {
			servicePluginHelper.doAfter(requestObj, formModel);
		} catch (NoSuchMethodException | IllegalAccessException |InvocationTargetException e) {
			throw new ServiceException(ErrorCode.PLUGIN_EXCEPTION,
					e.getMessage());
		}
		return ret;
	}
	
	/**
	 * 编辑记录
	 * 根据主键，判断执行的SQL
	 * @param map
	 */
	/**
	 * @param formModel
	 * @return
	 */
	public int delete(FormModel formModel){
		int ret = 0;
		String pks = ObjectUtils.toString(formModel.getPrimaryFieldValue());
		if(pks.indexOf(',')>0) {
			String[] arrPK = pks.split(",");
			for(String pk:arrPK) {
				formModel.setPrimaryFieldValue(pk);
				formMapper.checkConstraints(formModel.getContraints());//若有关联数据存在，抛出异常
				formMapper.deleteCascade(formModel.getCascade());//删除级联子表数据
				ret += formMapper.delete(formModel);
			}
		}else {
			formMapper.checkConstraints(formModel.getContraints());//若有关联数据存在，抛出异常
			formMapper.deleteCascade(formModel.getCascade());//删除级联子表数据
			ret += formMapper.delete(formModel);
		}
		formModel.setPrimaryFieldValue(pks);//restore
		return ret;
	}
	
	/**
	 * DAOPlugin支持
	 * @param formModel
	 * @param requestObj
	 * @return
	 */
	public int delete(FormModel formModel,FormEngineRequest requestObj){
		int ret = 0;
		ServicePluginHelper servicePluginHelper = 
				new ServicePluginHelper(formModel.getServerTokenData());
		ret = delete(formModel);
		try {
			servicePluginHelper.doAfter(requestObj, formModel);
		} catch (NoSuchMethodException | IllegalAccessException |InvocationTargetException e) {
			throw new ServiceException(ErrorCode.PLUGIN_EXCEPTION,
					e.getMessage());
		}
		return ret;
	}
	
	/**
	 * DAOPlugin支持
	 * @param model
	 * @return
	 */
	public int save(FormEngineRequest requestObj,FormModel mModel) {
		ServicePluginHelper servicePluginHelper = 
				new ServicePluginHelper(mModel.getServerTokenData());
		int n=0;
		int cnt = 0;
		int detailCnt=0;
		int i;
		List<FormModel> detailModelList = mModel.getDetailList();
		try{
			servicePluginHelper.doBefore(requestObj, mModel);
			if(mModel.isUpdateMaster()){//有时不需要处理主表,根据In配置逗号前第一个值决定
				//主表
				if(mModel.isEdit()){
					n = update(mModel);//乐观锁字段必须初始值
				}else{
					n = insert(mModel);
				}
				if(n<0){
					throw new ServiceException(ErrorCode.SAVE_MASTER_DATA_FAILED,
							String.format("%s条",n));
				}else if(n==0 && mModel.hasOptimisticLock()){
					throw new ServiceException(ErrorCode.SAVE_DIRTY_DATA,
							"实际更新0条,乐观锁记录疑似已被更改，保存前请重新装载");
				}
			}
			//循环保存多个子表的记录。通常只有一个子表
			int result = 0;
			boolean isValidRow = false;
			for (FormModel detailModel:detailModelList) {  
				List<Map<String,Object>> detailData = (List<Map<String,Object>>)detailModel.getDetailData();//DataGrid内容
				String rowEditMode;
				i=0;
				for(Map<String,Object> rowData:detailData){
					result = 0;
					isValidRow = FormUtil.isValidRow(rowData);
					i++;
					//build model by current row 
					rowEditMode = ObjectUtils.toString(rowData.get(Param.P_EditMode));
					detailModel.setEditMode(rowEditMode);
					detailModel.setFormDataByMap(rowData);
					
					//应在细表维护时 addRow，对row增加editMode属性，当前记录若有，则以当前为准，否则从master继承
					if(detailModel.isEdit()){
						if(isValidRow){
							result = update(detailModel);//基于已有记录保存,不应存在更新条数=0问题
						}else{
							result = delete(detailModel);//基于已有记录保存,不应存在更新条数=0问题
						}
					}else if(detailModel.isAdd()){
						//需要增加master table的主键值到data中
						if(isValidRow){//新增行你对在客户端已做删除的数据不做处理
							detailModel.getData().put(detailModel.getForeignControl(), mModel.getPrimaryFieldValue());
							result = insert(detailModel);
						}
					}else{
						FormLogger.logFlow("无法确定当前明细数据的编辑模式,跳过/抛出异常？",FormLogger.LOG_TYPE_INFO);
					}
					if(result<0){
						throw new ServiceException(ErrorCode.SAVE_DETAIL_DATA_FAILED,
								String.format("第%s行明细数据保存返回%s",i,n));
					}
					detailCnt += result;
					FormLogger.logFlow(String.format("%s表更新%s条",detailModel.getTableName(),cnt),FormLogger.LOG_TYPE_DEBUG );
					if( isValidRow && result < 1){
						FormLogger.error(FormConstants.LOGIC_SAVE, 
								String.format("更新0条:%s %s [%s]", 
										detailModel.getEditMode(),
										detailModel.getTableName(),
										detailModel.isEdit()?detailModel.getUpdateSql():detailModel.getInsertSql()), 
										ObjectUtils.toString(rowData));
						throw new ServiceException(ErrorCode.SAVE_DETAIL_DATA_FAILED,
								String.format("第%s行保存失败，实际更新%s条",(i+1),detailCnt));
					}
				}
			}
			servicePluginHelper.doAfter(requestObj, mModel);
		}catch(InvocationTargetException | IllegalAccessException | NoSuchMethodException e){
			throw new ServiceException(ErrorCode.PLUGIN_EXCEPTION,
					e.getMessage());
		}catch(ServiceException | ValidException e){
			FormLogger.logJDBC(FormConstants.LOGIC_SAVE, e.getMessage());
			throw e;
		}catch(Exception e){
			FormLogger.logJDBC(FormConstants.LOGIC_SAVE, e.getMessage());
			throw new ServiceException(ErrorCode.SAVE_TRANSACTION_FAILED,
					e.getMessage());
		}
		return n;
	}
	
}
