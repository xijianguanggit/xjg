package com.tedu.base.common.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tedu.base.initial.model.xml.ui.FormConstants;
/**
 * 错误页面处理
 * @author xijianguang
 */
@Controller
@RequestMapping("/open")
public class ExceptionController {
	/**
	 * 500错误
	 * @author xijianguang
	 */
	@RequestMapping("/500")
	public ModelAndView _500(HttpServletRequest request){
		ModelAndView view = new ModelAndView("exception/500");
		
		Throwable t = (Throwable)request.getAttribute("javax.servlet.error.exception");
		if(t.getClass().getSimpleName().equals("UnauthorizedException")){
			view = new ModelAndView("exception/unauthorizedUrl");
		}
		return view;
	}
	/**
	 * 404错误
	 * @author xijianguang
	 */
	@RequestMapping("/404")
	public ModelAndView _404(HttpServletRequest request){
		ModelAndView view = new ModelAndView(FormConstants.VIEW_UNDERCONSTRUCT);
		return view;
	}
	
	@RequestMapping("/400")
	public ModelAndView _400(HttpServletRequest request){
		ModelAndView view = new ModelAndView("exception/404");
		return view;
	}
	/**
	 * 没有权限
	 * @author xijianguang
	 */
	@RequestMapping("/unauthorizedUrl")
	public ModelAndView unauthorizedUrl(HttpServletRequest request){
		ModelAndView view = new ModelAndView("exception/unauthorizedUrl");
		return view;
	}
}
