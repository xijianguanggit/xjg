
package com.tedu.base.auth.login.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.googlecode.aviator.AviatorEvaluator;
import com.tedu.base.auth.login.model.UserModel;
import com.tedu.base.auth.login.service.LoginService;
import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.ConstantUtil;
import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.common.utils.TokenUtils;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.UserLog;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.engine.service.FormTokenService;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.engine.util.FormUtil;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.msg.mail.SendMsgService;

/**
 * 登录类
 *
 * @author xijianguang
 */
@Controller
public class LoginController {
    @Value("${base.image.logImg}")
    private String logImg;
    @Value("${base.image.loginImg}")
    private String loginImg;
    @Value("${base.image.sysName}")
    private String sysName;
    @Value("${base.image.loginLogo}")
    private String loginLogo;

    @Value("${base.notice}")
    private String notice;

    @Value("${base.title}")
    private String baseTitle;
    @Value("${base.copyright}")
    private String copyRight;

    @Value("${base.app}")
    private String app;

    @Value("${base.ver}")
    private String ver;

    @Value("${ui.dialog.size.small}")
    private String small;
    @Value("${ui.dialog.size.medium}")
    private String medium;
    @Value("${ui.dialog.size.large}")
    private String large;

    @Value("${base.image.favicon.png}")
    private String faviconPng;

    @Value("${base.image.favicon.ico}")
    private String faviconIco;

    private String cid;

    @Resource
    private LoginService loginService;
    @Resource
    private FormTokenService formTokenService;
    @Resource
    private FormService formService;
    @Resource
    private FormLogService formLogService;
    @Resource
    private SendMsgService sendMsgService;

    // 日志记录器
    public final Logger log = Logger.getLogger(this.getClass());

    /**
     * 登出操作
     */
    @RequestMapping("/logOut")
    public String logOut(HttpServletRequest request, Model model) {

        model.addAttribute("baseTitle", baseTitle);
        model.addAttribute("loginImg", loginImg);
        model.addAttribute("loginLogo", loginLogo);
        model.addAttribute("notice", notice);
        model.addAttribute("copyRight", copyRight);
        model.addAttribute("faviconPng", faviconPng);
        model.addAttribute("faviconIco", faviconIco);

        //登出记录日志
        UserLog log = new UserLog();
        log.setUiName("login");//必填
        log.setUiTitle("登录页");
        log.setAction("logout");
        log.setUserId(SessionUtils.getUserInfo().getUserId());
        log.setEmpId(SessionUtils.getUserInfo().getEmpId());
        log.setControlName("");
        log.setControlTitle("");
        log.setPanelName("");
        log.setPanelTitle("");
        log.setFlowId(TokenUtils.genUUID());//必填
        log.setSessionId(SessionUtils.getSession().getId().toString());
        log.setCreateTime(new Date());
        log.setCreateBy(SessionUtils.getUserInfo().getEmpId());


        formLogService.save(log);

        //清Session
        SessionUtils.removeAll();
        return "login";
    }

    /**
     * 验证是否需要验证码
     */
    @RequestMapping("/getValidateStatus")
    @ResponseBody
    public FormEngineResponse getValidateStatus(HttpServletRequest request) {
        FormEngineResponse formEngineResponse = new FormEngineResponse("");
        List<UserModel> userModels = null;
        try {
            userModels = loginService.getUserInfoByName(request.getParameter("userName"));
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.SQL_PREPARE_FAILED, "登录用户查询失败");
        }
        if (userModels == null) {
            formEngineResponse.setCode("0");
            formEngineResponse.setMsg("0");
        } else if (!userModels.isEmpty()) {
            formEngineResponse.setCode("0");
            UserModel userModel = userModels.get(0);
            formEngineResponse.setMsg(String.valueOf(userModel.getValidate()));
        }

        return formEngineResponse;
    }


    /**
     * 登录操作
     *
     * @throws Exception
     */
    @RequestMapping("/login")
    @ResponseBody
    public FormEngineResponse toLogin(@RequestBody FormEngineRequest requestObj, HttpServletRequest request) {
    	return login(requestObj, request);
    }
    private FormEngineResponse login(FormEngineRequest requestObj, HttpServletRequest request){

        FormEngineRequest engineRequest = requestObj;
        Map<String, Object> param = engineRequest.getData();
        FormEngineResponse response = new FormEngineResponse("");


        String password = param.get("password").toString();
        String userName = param.get("userName").toString();
        String validateCode = ObjectUtils.toString(param.get("validateCode"));

        UserLog log = new UserLog();
        log.setUiName("mainFrame");
        log.setUiTitle("主页");
        log.setAction("login");
        String errorReason = "";

        Session s = SessionUtils.getSession();
        s.setAttribute("validateCode", validateCode);

        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        Subject subject = SecurityUtils.getSubject();
        subject.hasRole("");

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

        formLogService.save(log);
        return response;
    }
    
    @RequestMapping ( "/loginAn" )
    @ResponseBody
    public FormEngineResponse toLoginAn(@RequestBody FormEngineRequest requestObj, HttpServletRequest request) throws Exception {
        if (SessionUtils.getUserInfo() == null ||(SessionUtils.getUserInfo()!=null && SessionUtils.getUserInfo().getUserId()!=-1l)) {
            UserModel user = new UserModel();
            user.setUserId(-1l);
            user.setName(UserModel.ANONYMOUS);
            SessionUtils.setAttrbute(ConstantUtil.USER_INFO, user);
            initResource();
        }
        return new FormEngineResponse("");
    }
    
    /**
     * 跳转首页
     */
    @RequestMapping("")
    public String welcome(HttpServletRequest request, Model model) {
    	return index(request, model, "");
    }
    private String index(HttpServletRequest request, Model model, String type){
        UserModel userModel = (UserModel) SessionUtils.getAttrbute(ConstantUtil.USER_INFO);
        model.addAttribute("baseTitle", baseTitle);
        //TODO 菜单类型从数据字典中动态获得
        if (userModel != null) {
            List<Map<String, String>> allMenuList = new ArrayList<Map<String,String>>();
            if(SessionUtils.getUserInfo().isAdminRole()){
                allMenuList = loginService.getAllAuthorization();
            }else {
            	if("forCustom".equals(type)){
            		allMenuList = loginService.getAuthorization("student", "menu");
            	} else {
            		allMenuList = loginService.getAuthorization(userModel.getUserName(), "menu");
            	}
            }

            List<Map<String, String>> mainMenuList = allMenuList.stream().filter(stringStringMap -> !stringStringMap.isEmpty() && !stringStringMap.get("type").isEmpty() && stringStringMap.get("type").equals("mainMenu")).collect(Collectors.toList());
            List<Map<String, String>> quickMenuList = allMenuList.stream().filter(stringStringMap -> !stringStringMap.isEmpty() && !stringStringMap.get("type").isEmpty() && stringStringMap.get("type").equals("quickMenu") && stringStringMap.get("parent") != null).collect(Collectors.toList());
            List<Map<String, String>> index = allMenuList.stream().filter(stringStringMap -> !stringStringMap.isEmpty() && !stringStringMap.get("type").isEmpty() && stringStringMap.get("type").equals("index") && stringStringMap.get("parent") != null).collect(Collectors.toList());

            List<String> menuParentList = new ArrayList<String>();

            mainMenuList.stream().forEach(stringStringMap -> menuParentList.add(stringStringMap.get("parentName")));

            List<String> menuParentDistinct = menuParentList.stream().distinct()
                    .collect(Collectors.toList());
            
            List<Map<String, String>> menuSonList = new ArrayList<Map<String, String>>();
            Iterator<Map<String, String>> mainMenuIter = mainMenuList.iterator(); 
            boolean ifSon = false;
            while (mainMenuIter.hasNext()) {    
            	Map<String, String> sonMap = mainMenuIter.next();  
            	String parent = sonMap.get("parent");
            	
                for(Map<String, String> codeMap:mainMenuList){
                	String parentName = codeMap.get("parent");
                	if(parent.equals(String.valueOf(codeMap.get("code")))
                			&&!"1".equals(parentName))
                	{
                		menuSonList.add(sonMap);
                		codeMap.put("ifParent", "1");
                		menuParentDistinct.remove(codeMap.get("name"));
                		ifSon = true;
                		break;
                	}
                }
                
                if (ifSon){
                	mainMenuIter.remove();
                    ifSon = false;
                }
                	
              }
            
            

            //倒序排列快捷菜单
            Collections.sort(quickMenuList, new Comparator<Map<String, String>>() {
                public int compare(Map<String, String> o1, Map<String, String> o2) {
                    Integer seq1 = Integer.valueOf(String.valueOf(o1.get("seq")));
                    Integer seq2 = Integer.valueOf(String.valueOf(o2.get("seq")));
                    return seq2.compareTo(seq1);
                }
            });

            if (!index.isEmpty()) {
                Map<String, String> indexResource = index.get(0);
                model.addAttribute("indexTarget", indexResource.get("target"));

                model.addAttribute("indexName", indexResource.get("name"));
                model.addAttribute("indexUrl", indexResource.get("url"));
                model.addAttribute("indexMenuCode", indexResource.get("code"));
            } else {
                model.addAttribute("indexName", "");
                model.addAttribute("indexUrl", "");
                model.addAttribute("indexMenuCode", "");
            }
            model.addAttribute("menuList", mainMenuList);
            model.addAttribute("quickMenuList", quickMenuList);

            model.addAttribute("menuParentList", menuParentDistinct);
            
            model.addAttribute("menuSonList", menuSonList);

            model.addAttribute("windowSize", large);

            model.addAttribute("logImg", logImg);
            model.addAttribute("sysName", sysName);

            initResource();
            FormUtil.checkOutSavedRequest(request,model);
            //有些需要自动重定向至登录前访问的地址，用url:做前缀表示
            Map<String, Object> modelMap = model.asMap();
            String redUrl = modelMap==null?"":ObjectUtils.toString(modelMap.get("redirect"));
            if(redUrl.startsWith("url:")){//返回登录前地址，非在框架内打开
            	return "redirect:"+redUrl.substring(4);
            }
            setDefaultMenuItem(model);
            //登陆成功跳转主页
            return "mainFrame";
        } else {
            model.addAttribute("loginImg", loginImg);
            model.addAttribute("loginLogo", loginLogo);

            model.addAttribute("notice", notice);
            model.addAttribute("cid", AviatorEvaluator.execute("Guid()").toString());
            model.addAttribute("app", app);
            model.addAttribute("ver", ver);
            model.addAttribute("copyRight", copyRight);
            model.addAttribute("faviconPng", faviconPng);
            model.addAttribute("faviconIco", faviconIco);
            model.addAttribute("date", DateUtils.getDateToStr("yyyyMMdd", new Date()));
            //交换秘钥HMAC
            if("".equals(type))
            	return loginRemoteUser(request);
            else
            	return "loginForCustom";
        }
    
    }
    
    /**
     * 如果用户在登录前访问的是一个菜单资源,则在登录后打开此tab
     * 或在mainFrame中根据需要决定前端行为 defaultMenuItem
     * @param model
     * @param menuList
     */
    private void setDefaultMenuItem(Model model) {
        String lastUrl = ObjectUtils.toString(SessionUtils.getAttrbute(FormConstants.SESSION_LAST_URL));
        String lastUrlName = ObjectUtils.toString(SessionUtils.getAttrbute(FormConstants.SESSION_LAST_URL_NAME));
        if(!lastUrl.isEmpty() && SessionUtils.isAccessibleUrl(lastUrl)) {//有权限
        	Map<String,String> menu = new HashMap<String,String>();
        	menu.put("name", lastUrlName);
        	menu.put("url", lastUrl);
        	menu.put("code", "-2");
        	menu.put("target", "tab");
        	model.addAttribute("defaultMenuItem", menu);
        }
		SessionUtils.removeAttrbute(FormConstants.SESSION_LAST_URL);
		SessionUtils.removeAttrbute(FormConstants.SESSION_LAST_URL_NAME);
    }
    
    /**
     * 在构造ACL时,判断登录前访问的资源是否在ACL中,如存在,记录对应的资源名称在session中
     * 在setLastUI中构造默认tab时，可使用这个值作为显示标签名
     * @param listUrl
     */
    private void setLastUIName(List<Map<String, Object>> listUrl) {
    	String lastUrl = ObjectUtils.toString(SessionUtils.getAttrbute(FormConstants.SESSION_LAST_URL));
    	String lastUrlName = "页签";
        for(Map<String, Object> res:listUrl) {
        	if(ObjectUtils.toString(res.get("url")).equals(lastUrl)){
        		lastUrlName = ObjectUtils.toString(res.get("name"));
        		SessionUtils.setAttrbute(FormConstants.SESSION_LAST_URL_NAME,lastUrlName);
        		break;
        	}
        }
    }

    /**
     * tmp for nuc
     * @param request
     * @return
     */
    private String loginRemoteUser(HttpServletRequest request){
    	String loginUser = request.getRemoteUser();
    	SessionUtils.setAttrbute("ctx", request.getContextPath());
    	if(ObjectUtils.toString(loginUser).isEmpty()){
    		return "login";
    	}
        List<UserModel> userList  = loginService.getUserInfoByName(loginUser);
        if(userList.size()==1){
        	SessionUtils.getSession().setAttribute(ConstantUtil.USER_INFO, userList.get(0));
            return "mainFrame";
        }
        return "login";
    }
    
    /**
     * token拦截器需要将所有可访问资源初始在内存中
     * ShiroFilerChainManager中的查询不正确。
     * 暂用此方式代替
     */
    public void initResource() {
        //load accessible url
        QueryPage queryPage = new QueryPage();
        queryPage.setQueryParam("ACLU");//所有当前用户可访问的url资源：满足授权的和不需授权的url
        List<Map<String, Object>> listUrl = formService.queryBySqlId(queryPage);
        setLastUIName(listUrl);
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

}

