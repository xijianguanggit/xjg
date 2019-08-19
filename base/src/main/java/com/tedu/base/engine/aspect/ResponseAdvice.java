package com.tedu.base.engine.aspect;

import javax.annotation.Resource;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.tedu.base.engine.util.FormUtil;
import com.tedu.base.engine.util.TemplateUtil;

@ControllerAdvice(basePackages = "com.tedu.base")
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

	@Resource
	TemplateUtil templateUtil;
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }
    
	@ExceptionHandler(Exception.class)
	public ModelAndView handleCustomException(Exception ex) {
		return FormUtil.getExceptionView(templateUtil, ex);
	}

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
            MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request, ServerHttpResponse response) {
        return body;
    }
}