package com.tedu.base.auth.login.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tedu.base.auth.login.service.impl.RestService;
import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.redis.RedisService;
import com.tedu.base.common.utils.SignUtil;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.common.utils.TokenUtils;
import com.tedu.base.common.utils.UCRequest;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.engine.util.FormLogger;

@Service("restService")
public class RestServiceImp implements RestService{
	private static final String PREFIX = "open";//username-->userCode
	private static final Long EXPIRE = 1*60*60L;
	
	@Value("${base.app}_")
	public String appName;
	
	@Autowired(required=false)
    private RedisService redisService;	
    
    @Resource
    private FormService formService;	
    
	@Override
	public String setUserToken(String userCode) {
		String token = TokenUtils.generateToken();
		redisService.set((getUserTokenKey(userCode)), token,EXPIRE);
		redisService.set(getTokenUserKey(token), userCode,EXPIRE);
		return token;
	}

	@Override
	public String getUserToken(String userCode) {
		return redisService.get(getUserTokenKey(userCode));
	}
	
	@Override
	public String getUserIdByToken(String userToken) {
		String userCode = redisService.get(getTokenUserKey(userToken));
		return redisService.get(getLoginKey(userCode));
	}
	
	
	/**
	 * 检查用户请求的token有效性
	 */
	@Override
	public boolean checkToken(String userToken){
		return true;
//		return redisService.get(getTokenUserKey(userToken))!=null;
	}

	@Override
	public String getUser(String userName) {
		return redisService.get(getLoginKey(userName));
	}

	@Override
	public String setUser(String userId) {
		String userCode = TokenUtils.generateToken(userId, TokenUtils.genUUID());
		redisService.set(getLoginKey(userId),userCode,EXPIRE);
		redisService.set(getLoginKey(userCode),userId,EXPIRE);
		return userCode;
	}

	private String getLoginKey(String userName){
		return String.format("%s%s_%s_%s", appName,PREFIX,"",userName); 
	}
	
	private String getUserTokenKey(String userCode){
		return String.format("%s_%s_%s_%s", appName,PREFIX,"",userCode); 
	}
	
	private String getTokenUserKey(String token){
		return String.format("%s%s_%s_%s", appName,PREFIX,"",token); 
	}
	
    /**
     * token拦截器需要将所有可访问资源初始在内存中
     * ShiroFilerChainManager中的查询不正确。
     * 暂用此方式代替
     */
	@Override
    public void initResource() {
        //load accessible url
        QueryPage queryPage = new QueryPage();
        queryPage.setQueryParam("ACLU");//所有当前用户可访问的url资源：满足授权的和不需授权的url
        List<Map<String, Object>> listUrl = formService.queryBySqlId(queryPage);
//    	//不限定权限的资源
        SessionUtils.setAccessibleUrl(listUrl);
        //load accessible control list
        try {
            queryPage = new QueryPage();
            queryPage.setQueryParam("ACL");
            List<Map<String, Object>> controlList = formService.queryBySqlId(queryPage);
            if (controlList != null) {
                Map<String, String> userControlMap = new HashMap<>();
                controlList.forEach(e -> userControlMap.put(ObjectUtils.toString(e.get("url")), ObjectUtils.toString(e.get("id"))));//"ui.panel.controlName"
                SessionUtils.setACL(userControlMap);
                FormLogger.logBegin(String.format("装载用户可访问组件{%s}个", controlList.size()));
            }
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.ACL_LOAD_FAILED, e.getMessage());
        }
    }

	@Override
	public boolean checkSign(UCRequest requestObj) {
		String sign = SignUtil.getSign(requestObj);
		String requestSign = requestObj.getSign();
		//验签
		return (sign.equals(requestSign));
	}

}
