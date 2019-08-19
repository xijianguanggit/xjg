package com.tedu.base.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class CrosFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) servletResponse; 
		String origin = (String) servletRequest.getRemoteHost()+":"+servletRequest.getRemotePort(); 
		 response.setHeader("Access-Control-Allow-Origin", "*");
		 response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		 response.setHeader("Access-Control-Max-Age", "3600");
		 response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Authorization");
		 response.setHeader("Access-Control-Allow-Credentials","true");
		 chain.doFilter(servletRequest, servletResponse);		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
	
}
