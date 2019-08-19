package com.tedu.base.engine.util;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.model.FormModel.Mode;
import com.tedu.base.initial.model.xml.ui.Control;
import com.tedu.base.initial.model.xml.ui.Property;
import com.tedu.base.rule.AviatorUtil;

/**
 * 用于统一校验当前保存提交的数据
 * 
 * 非空校验
 * 1、根据固定field name获取相关panel描述
 * 2、循环校验panel中所有control的required属性是true的
 * 3、循环model中的所有属性
 * 
 * 值类型校验
 * 1、根据panel获取绑定的model名
 * 2、根据model名获取model描述
 * 3、循环model中的所有属性，对类型进行校验
 * 
 * @author wangdanfeng
 *
 */
public class PanelValidator implements Validator {
	private String dataTypeGroup = String.format("|%s|%s|%s", Property.TYPE_DOUBLE,Property.TYPE_STRING,Property.TYPE_LONG);

	@Override
	public boolean supports(Class<?> clazz) {
		 return FormModel.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		FormModel formModel = (FormModel)target;
		//逐个panel校验，先校验panel的object属性对应的model的必填项
		//校验是基于整个model;校验的文字从panel中按照property反查
		//校验必填项
		validateRequired(formModel,errors);
		if (errors.hasErrors()) return;//先校验一遍必填项，若未通过，不再继续
		//校验数据类型\validate表达式验证
		
		validateRule(formModel, errors);
		validateTypeAndLength(formModel, errors);
		validateOptimisticLockField(formModel);//乐观锁没传值
	}
	
	/**
	 * 乐观锁字段必填
	 * @param formModel
	 * @param errors
	 */
	private void validateOptimisticLockField(FormModel formModel){
		if(!formModel.hasOptimisticLock()) return;
		String pName = formModel.getOptimisticProperty();
		if(formModel.getEditMode().equals(Mode.Update.value) 
				&& ObjectUtils.toString(formModel.getData().get(pName)).isEmpty()){//不填写不能未空字符串或null
			throw new ValidException(ErrorCode.ILLEGAL_FORM_CONFIGURATION,
					pName,"乐观锁无值");
		}
	}
	
	
	/**
	 * 必填校验
	 * 第一个循环校验可见组件
	 * 若可见组件不存在必填为空错误，则继续校验必填隐藏组件，若发现，按异常抛出。通常是配置不当或者组件bug引起
	 * @param formModel
	 * @param errors
	 */
	private void validateRequired(FormModel formModel, Errors errors){
		String cName;
		List<Control> cList = formModel.getControlList();
		boolean foundVisibleError = false;
		for(Control c:cList){
			cName = c.getHtmlName();
			if(!c.isRequired() || c.getType().equalsIgnoreCase(Control.TYPE_HIDDEN)) continue;
			if(ObjectUtils.toString(formModel.getData().get(cName)).isEmpty()){//不填写不能未空字符串或null
				foundVisibleError = true;
				errors.rejectValue(c.getTitle(), "0000", "不能为空");
			}
		}
		if(foundVisibleError) return;//如果已有可见组件未填写，则返回；否则继续校验完整性
		for(Control c:cList){
			cName = c.getHtmlName();
			if(c.isRequired() && c.getType().equalsIgnoreCase(Control.TYPE_HIDDEN) 
					&& ObjectUtils.toString(formModel.getData().get(cName)).isEmpty()) {//隐藏必填组件未填，通常因为配置或者组件bug导致，作为异常处理
				FormLogger.logValidate("隐藏组件值为空["+c.getName()+"]", FormLogger.LOG_TYPE_ERROR);
				throw new ValidException(ErrorCode.REQUIRED_HIDDEN_FIELD_MISSING,
						c.getName(),"隐藏组件值为空");
			}
		}		
	}
	
	public static void validateRequired(List<Control> cList,Map<String,Object> data,Errors errors){
		validateRequired(cList,data,errors,true);
	}
	
	public static void validateRequired(List<Control> cList,Map<String,Object> data,Errors errors,boolean shouldThrow){
		String cName;
		for(Control c:cList){
			cName = c.getHtmlName();
			if(!c.isRequired()) continue;
			if(ObjectUtils.toString(data.get(cName)).isEmpty()){//不填写不能未空字符串或null
				if(c.getType().equalsIgnoreCase(Control.TYPE_HIDDEN)){
					if(shouldThrow){
						throw new ServiceException(ErrorCode.REQUIRED_HIDDEN_FIELD_MISSING,
								String.format("[%s]", 
										c.getName()));
					}else{
						FormLogger.logFlow(String.format("[%s]", c.getName()), FormLogger.LOG_TYPE_DEBUG);
						errors.rejectValue(c.getTitle(), "0000", "不能为空");
					}
				}else{
					errors.rejectValue(c.getTitle(), "0000", "不能为空");
				}
			}
		}
	}
	
	/**
	 * 校验表达式
	 * 范围:panel对应的所有配置了校验表达式的property
	 * 前提:配置初始化时control对象已设置了propertyObj属性
	 * @param formModel
	 * @param errors
	 */
	private void validateRule(FormModel formModel, Errors errors){
		String currentPropertyName;
		Property currentProperty;
		List<Control> cList = formModel.getControlListAll();
		for(Control c:cList){
			currentPropertyName = ObjectUtils.toString(c.getProperty());
			currentProperty = c.getPropertyObj();
			Object currentVal = formModel.getData().get(c.getHtmlName());//表单当前组件值
			String validExp = currentProperty==null?"":ObjectUtils.toString((currentProperty.getValidate()));

			if(currentPropertyName.isEmpty() || currentProperty==null || validExp.isEmpty()){
				if(!validExp.isEmpty()){
					FormLogger.logValidate("validateRule跳过,未满足[属性]不能为空" , 
							String.format("[%s]",currentPropertyName));
				}
				continue;//未绑定属性的组件忽略其他校验
			}
			if(ObjectUtils.toString(currentVal).isEmpty()){
				continue;
			}
			try{
				Boolean result = (Boolean) AviatorUtil.validExpression(validExp, formModel,currentVal);
		        if(!result){
			        errors.rejectValue(c.getTitle(),"errormsg.control","不符合验证表达式要求");
		        }
			}catch(NullPointerException e){
				FormLogger.logConf("validateRule空指针异常",String.format("表达式[%s],值[%s]", validExp,currentVal));
			}
		}
	}
	
	
	/**
	 * 根据组件名获取的值不为空时才校验
	 * 校验范围(同时满足)
	 * 1、组件绑定了属性
	 * 2、组件值不为空
	 * 3、属性绑定了字段
	 * @param formModel
	 * @param errors
	 */
	private void validateTypeAndLength(FormModel formModel, Errors errors){
		String currentPropertyName;
		Property currentProperty;
		List<Control> cList = formModel.getControlList();
		for(Control c:cList){
			currentPropertyName = ObjectUtils.toString(c.getProperty());
			currentProperty = c.getPropertyObj();
			String currentVal = ObjectUtils.toString(formModel.getData().get(c.getHtmlName())).trim();//表单当前组件值

			if(currentPropertyName.isEmpty() || c.getPropertyObj()==null || 
					currentVal.isEmpty() ||
					ObjectUtils.toString(currentProperty.getField()).isEmpty()){
				//未满足[属性|字段|组件值]均不能为空
				continue;//未绑定属性的组件忽略其他校验
			}
			validateType(errors,currentProperty.getType(),c,currentVal);
			validateLength(errors,c,currentProperty,currentVal);
		}
	}
	
	/**
	 * 验证数据类型
	 * ValidatorUtil中已实现对应数据类型的isDatatype方法，且均返回boolean
	 * @param errors
	 * @param dataType
	 * @param control
	 * @param val
	 */
	private void validateType(Errors errors,String dataType,Control control,Object val){
		boolean match = true;
		String method = "is" + FormUtil.capitalFirst(dataType);
		  if(dataType.equalsIgnoreCase(Property.TYPE_STRING) || dataType.equalsIgnoreCase(Property.TYPE_TIME)){
			//继续检查长度
			return;
		}
		try{
			match = (Boolean)MethodUtils.invokeStaticMethod(ValidatorUtil.class, method, val);
		}catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
			throw new ServiceException(ErrorCode.UNSUPPORTED_DATA_TYPE,e.getMessage());
		}
		if(!match){
			errors.rejectValue(control.getTitle(),"errormsg.control","数据类型不正确");
			FormLogger.logConf(
					String.format("组件[%s]的绑定的属性[%s]数据类型[%s]和存入值不匹配", 
							control.getName(),
							control.getProperty(),
							ObjectUtils.toString(dataType)), 
					ObjectUtils.toString(val));
		}
	}

	/**
	 * 数据类型正确的基础上,校验长度
	 * - String类型为自然数，表示字符个数，一个英文字符或一个汉字字符，均算一个字符。
     * - Int类型为自然数，表示数字个数。例如，12345长度为5。
     * - Float类型为m.n格式，m、n均为自然数，m表示数字个数，n表示小数个数且m>n。例如，123.45长度为5.2。
	 * @param errors
	 * @param dataType
	 * @param control
	 * @param val
	 */
	private void validateLength(Errors errors,Control c,Property p,Object val){
		String dataType = p.getType();
		String pLength = ObjectUtils.toString(p.getLength());
		//日期暂不检测长度问题。已有spring统一处理
		if(pLength.isEmpty() || dataTypeGroup.indexOf(dataType.toLowerCase())<0){//未定义或非指定三种类型的,忽略
			return;
		}
		int valLength = ObjectUtils.toString(val).length();//实际长度
		int maxLength = 0;//总长度
		
		if(dataType.equalsIgnoreCase(Property.TYPE_STRING) || dataType.equalsIgnoreCase(Property.TYPE_LONG)){
			maxLength = Integer.parseInt(pLength);
			if(valLength>maxLength){
				errors.rejectValue(c.getTitle(),"errormsg.property","长度超过" + maxLength);
				return;
			}
		}else{
			String[] arr = pLength.split("\\.");
			int mLen = Integer.parseInt(arr[0]);//总长
			int nLen = arr.length>1?Integer.parseInt(arr[1]):0;//小数点长度
			if(mLen<=nLen) {
				FormLogger.debug("skip invalid length expression " + pLength);
				return;	
			}
			int len1 = mLen-nLen;//整数部分长度
			int len2 = nLen;//小数部分长度
			String regex = String.format("^-?\\d{0,%s}(\\.\\d{0,%s})?$", len1,len2);
			if(!ObjectUtils.toString(val).matches(regex)){
				errors.rejectValue(c.getTitle(),"errormsg.property",
						String.format("请检查输入格式:小数点前最多%s位,小数点后最多%s位", len1,len2));
			}
		}
	}
	
	public static void main(String[] args){
		String regex = String.format("^-?\\d{0,%s}(\\.\\d{0,%s})?$", 10,2);
		System.out.println("6300".matches(regex));
		
	}
}
