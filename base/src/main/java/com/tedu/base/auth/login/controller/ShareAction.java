package com.tedu.base.auth.login.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tedu.base.auth.login.model.ShareModel;
import com.tedu.base.common.utils.ConstantUtil;
import com.tedu.base.common.utils.ContextUtils;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.initial.model.xml.ui.XML;

/**
 * 生成基于表单引擎配置生成的页面的对外链接
 * 用户打开链接时，若未登录系统，则进入登录页
 * 身份验证成功后进入系统，主框架界面加载完毕时提示用户是否打开XX页面，点确认，功能以tab页方式打开
 * 若构造url时带有参数,则打开后的界面中，对应panel将按传参初始值
 * @author wangdanfeng
 *
 */
@Controller
public class ShareAction {
	@Value("${base.website}")
	private String baseSite;
	/**
	 * 原用于工作项打开等复杂场景带界面初始值的UI打开(需登录)
	 * 现使用/ui/{uiName}接口结合LastUrl记录特性及携带ID，打开指定UI并加载对应ID的数据
	 * @deprecated
	 * 
	 * @param request
	 * @param model
	 * @param uiName
	 * @return
	 */
	@RequestMapping(value = "/open/{uiName}")
	public String openMenuItem(HttpServletRequest request, Model model,@PathVariable String uiName){
//	    ContextUtils.saveShareRequest(request);
		XML xml = (XML) ContextUtils.getAttrbute(ConstantUtil.XML);
		if(xml.getUI(uiName) == null){
			return "404";
		}
		if(!SessionUtils.isAccessibleUrl("/ui/"+uiName)){//需要open角色有处理此URL的权限
			WebUtils.getAndClearSavedRequest(request);//remove saved request
			FormLogger.logFlow("外部链接无权访问ui{" + uiName + "},已移除请求", FormLogger.LOG_TYPE_INFO);
			return "404";
		}
		FormLogger.logFlow("校验外部链接访问的ui{" + uiName + "}有效", FormLogger.LOG_TYPE_INFO);
	    return "redirect:/";//之后触发module.ftl中的地址判断逻辑，导航到根页面
	}
	
	@RequestMapping(value = "/open/gen")
	@ResponseBody
	public String genToken(Model model,String id) {
		ShareModel shareModel = new ShareModel("工作项管理","frmIssueList",null);
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("eq_issueId", id);
//		paramMap.put("lk_title", "测试标题传参");
		shareModel.setTarget("pCondition",paramMap);
		
		String ret = "issueList<br>" + shareModel.toUrl(baseSite) + "<br>";
		
		shareModel = new ShareModel("工作项详情","frmIssueView",id);
		ret += "issueView<br>" + shareModel.toUrl(baseSite) + "<br>";
		
		return ret;
	}
}
