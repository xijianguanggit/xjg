package com.tedu.base.auth.login.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tedu.base.auth.login.dao.LoginMysqlDao;
import com.tedu.base.auth.login.model.UserModel;
import com.tedu.base.auth.login.service.LoginService;
import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.common.utils.TokenUtils;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.SimpleFormModel;
import com.tedu.base.engine.model.UserLog;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.engine.util.FormUtil;
/**
 * 登录service
 * @author xijianguang
 */
@Service("loginService")
public class LoginServiceImpl implements LoginService {

	@Resource
	public LoginMysqlDao loginDao;
	
	@Resource
	private FormMapper formMapper;
	/**
	 * 查询用户密码
	 * @author xijianguang
	 */
	public List<UserModel> getUserInfoByName(String userName) {
		return loginDao.getUserInfoByName(userName);
	}
	/**
	 * 查询用户密码（客户）
	 * @author xijianguang
	 */
	public List<UserModel> getCustomUserInfoByName(String userName) {
		return loginDao.getCustomUserInfoByName(userName);
	}
	/**
	 * 查询用户密码（客户）
	 * @author xijianguang
	 */
	public List<UserModel> getCustomUserInfoByOpenId(String openId) {
		return loginDao.getCustomUserInfoByOpenId(openId);
	}
	
	/**
	 * 查询用户密码（会员）
	 * @author hejiakuo
	 */
	public List<UserModel> getAssociatorInfoByName(String userName) {
		return loginDao.getAssociatorInfoByName(userName);
	}
	
	/**
	 * 查询用户菜单权限
	 * @author xijianguang
	 */
	public List<Map<String, String>> getAuthorization(String userName, String menu) {
		if(StringUtils.isEmpty(menu)){
			return loginDao.getResList(userName);
		} else {
			return loginDao.getAuthorization(userName, menu);
		}
	}

	public List<Map<String, String>> getAllAuthorization() {

		return loginDao.getAllAuthorization();

	}

	/**
	 * 更新密码错误次数
	 * @author xijianguang
	 */
	public void updateWrongCount(UserModel userModel) {
		loginDao.updateWrongCount(userModel);
	}

	public void updatePwd(UserModel user) {
		loginDao.updatePwd(user);
	}
	
	/**
	 * 同后台登录逻辑，在没全面测试前先复制。暂不去掉LoginController中的重复逻辑
	 * 在这里只是为了验证合法性，并返回用户信息
	 */
	public FormEngineResponse login(FormEngineRequest requestObj, HttpServletRequest request){
        FormEngineRequest engineRequest = requestObj;
        Map<String, Object> param = engineRequest.getData();
        FormEngineResponse response = new FormEngineResponse("");


        String password = param.get("password").toString();
        String userName = param.get("userName").toString();
        String validateCode = ObjectUtils.toString(param.get("validateCode"));


        String errorReason = "";

        Session s = SessionUtils.getSession();
        s.setAttribute("validateCode", validateCode);

        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        Subject subject = SecurityUtils.getSubject();
        subject.hasRole("");

        UserLog log = new UserLog();
        log.setUiName("mainFrame");
        log.setUiTitle("主页");
        log.setAction("login");
        try {
            Map<String, String> dataMap = new HashMap<String, String>();
            subject.login(token);
            SessionUtils.setAttrbute("ctx", request.getContextPath());
            s.setAttribute("ip", request.getLocalAddr());
            s.setAttribute("port", request.getLocalPort());            
            response.setCode("0");
            response.setMsg("登录成功");
            Long userId = SessionUtils.getUserInfo().getUserId();
            Long empId = SessionUtils.getUserInfo().getEmpId();
            log.setUserId(userId);
            log.setEmpId(empId);
            log.setSessionId(s.getId().toString());
            log.setCreateBy(empId);
            log.setExecResult("登录成功");
            dataMap.put("userId", userId + "");
            response.setData(dataMap);
        } catch (UnknownAccountException e) {
            response.setCode(ErrorCode.LOGIN_USER_INVALID.getCode());
            response.setMsg(ErrorCode.LOGIN_USER_INVALID.getMsg());
            errorReason = "账号不存在";
        } catch (UnauthenticatedException e) {
            response.setCode(ErrorCode.LOGIN_USER_INVALID.getCode());
            response.setMsg(ErrorCode.LOGIN_USER_INVALID.getMsg());
            errorReason = "用户名无角色";
        } catch (CredentialsException e) {
            response.setCode(ErrorCode.LOGIN_CODE_ERROR.getCode());
            response.setMsg(ErrorCode.LOGIN_CODE_ERROR.getMsg());
            errorReason = "验证码错误";
        } catch (DisabledAccountException e) {
            String[] megs = e.getMessage().split("_");
            Long empId = Long.parseLong(megs[2]);
            log.setUserId(Long.parseLong(megs[1]));
            log.setEmpId(empId);
            log.setCreateBy(empId);
            response.setCode(ErrorCode.LOGIN_USER_INVALID.getCode());
            response.setMsg(ErrorCode.LOGIN_USER_INVALID.getMsg());
            errorReason = "用户已被冻结";
        } catch (UnsupportedTokenException e) {
            response.setCode(ErrorCode.LOGIN_USER_INVALID.getCode());
            response.setMsg(ErrorCode.LOGIN_USER_INVALID.getMsg());
            //密码错误
            errorReason = "密码错误";
            try {
                // 延迟一秒返回结果
                Thread.sleep(300);
            } catch (InterruptedException e1) {
                throw new ServiceException(ErrorCode.UNKNOWN, "延迟登录异常");
            }

        } catch (ExcessiveAttemptsException e) {
            response.setCode(ErrorCode.LOGIN_USER_INVALID.getCode());
            response.setMsg(ErrorCode.LOGIN_USER_INVALID.getMsg());

            //密码错误,次数太多，出验证码
            errorReason = "密码错误";
            try {
                // 延迟一秒返回结果
                Thread.sleep(300);
            } catch (InterruptedException e1) {
                throw new ServiceException(ErrorCode.UNKNOWN, "延迟登录异常");
            }

        } catch (Exception e) {
        	FormLogger.logFlow(e.getMessage(), FormLogger.LOG_TYPE_ERROR);
            response.setCode(ErrorCode.UNKNOWN.getCode());
            response.setMsg(ErrorCode.UNKNOWN.getMsg());
            errorReason = e.getMessage();
        }
        //登录成功 记录用户日志
        log.setControlName("");
        log.setControlTitle("");
        log.setPanelName("");
        log.setPanelTitle("");
        log.setFlowId(TokenUtils.genUUID());
        log.setCreateTime(new Date());
        if (log.getExecResult() == null) {
            log.setExecResult("登录失败");
        }
        log.setErrorReason(errorReason);
        log.setClientIp(FormUtil.getClientIP(request));

		SimpleFormModel simpleModel = new SimpleFormModel(log);
		formMapper.insert(simpleModel);
        return response;
    }
}
