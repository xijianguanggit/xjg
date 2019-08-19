package com.tedu.base.auth.login.validate;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 验证码servlet
 * @author xijianguang
 */
public class PlatformValidateCodeServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9223262023086737761L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PlatformRandomValidateCode validateCode = new PlatformRandomValidateCode();
		validateCode.generateValidateCode(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doPost(req, resp);
	}
}
