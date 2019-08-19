package com.tedu.base.acl;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tedu.base.auth.login.service.LoginService;

/**
 * 服务器启动时加载所有权限
 * @author xijianguang
 */

@Service
public class ShiroFilerChainManager {

    @Autowired
    private ShiroFilterFactoryBean shiroFilter;
    @Resource
    private LoginService loginService;
    private Map<String, NamedFilterList> defaultFilterChains;
	// 日志记录器
	public final Logger log = Logger.getLogger(this.getClass());

    @PostConstruct
    public void init() {
		try {
			// 读取配置文件
			AbstractShiroFilter abstractShiroFilter = (AbstractShiroFilter)shiroFilter.getObject();
			PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) abstractShiroFilter.getFilterChainResolver();  
			DefaultFilterChainManager filterChainManager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();
			defaultFilterChains = new HashMap<String, NamedFilterList>(filterChainManager.getFilterChains());
//			// 查询权限拼接初始化配置文件权限
			initFilterChains(loginService.getAuthorization("", ""));
		} catch (Exception e) {
			log.error("context", e);
		}
    }
    public void initFilterChains(List<Map<String, String>> urlFilters) {
    	AbstractShiroFilter abstractShiroFilter;
		try {
			abstractShiroFilter = (AbstractShiroFilter)shiroFilter.getObject();
			PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) abstractShiroFilter.getFilterChainResolver();  
			DefaultFilterChainManager filterChainManager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();
			filterChainManager.getFilterChains().clear();
			if(defaultFilterChains != null) {
				filterChainManager.getFilterChains().putAll(defaultFilterChains);
			}
//			
//			//2、循环URL Filter 注册filter chain
			for (Map<String, String> m: urlFilters) {
				if(m!=null){
					String url = m.get("url");
					if (!StringUtils.isEmpty(m.get("url"))) {
						filterChainManager.addToChain(url, "perms", m.get("name"));
					}
				}
			}
		} catch (Exception e) {
			log.error("context", e);
		}


    }

}
