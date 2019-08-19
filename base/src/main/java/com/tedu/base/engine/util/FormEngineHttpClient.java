package com.tedu.base.engine.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

public class FormEngineHttpClient {  
    private static final Logger LOG = LogManager.getLogger(HttpClient.class);  
    private static final String DEFALUT_ENCODE = "utf-8";
	public static String CODE_SUCCEED = "0";//返回成功标识
	public static String CODE_MESSAGE = "msg";//返回消息key
	public static String API_LOGIN = "/loginAssistant";//登录地址

    public static CloseableHttpClient httpClient = null;  
    public static HttpClientContext context = null;  
    public static CookieStore cookieStore = null;  
    public static RequestConfig requestConfig = null;  

    static {  
        init();  
    }  

    private static void init() {  
        context = HttpClientContext.create();  
        cookieStore = new BasicCookieStore();  
        requestConfig = RequestConfig.custom().setConnectTimeout(3000).setSocketTimeout(3000)  
                .setConnectionRequestTimeout(3000).build();  
        // 设置默认跳转以及存储cookie  
        httpClient = HttpClientBuilder.create().setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())  
                .setRedirectStrategy(new DefaultRedirectStrategy()).setDefaultRequestConfig(requestConfig)  
                .setDefaultCookieStore(cookieStore).build();  
    }  

    /** 
     * 发送get请求
     *  
     * @param url 
     * @return response 
     * @throws ClientProtocolException 
     * @throws IOException 
     */  
    public static JSONObject get(String url)  {  
        HttpGet httpget = new HttpGet(url);  
        CloseableHttpResponse response = null;
        try {  
            //设定请求的参数
            response= httpClient.execute(httpget, context);  
            return copyResponse(response);
        } catch(Exception e){
        	e.printStackTrace();
            LOG.debug("请求失败\t"+e.getMessage());
        }finally {  
            try {
                if(response != null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }  
        }
        return null;
    }  

    public static JSONObject getFormTokens(String site,String formName)  {  
        HttpGet httpget = new HttpGet(String.format("%s/ui/%s/app", site, formName));  
        CloseableHttpResponse response = null;
        try {  
            //设定请求的参数
            response= httpClient.execute(httpget, context);  
            JSONObject retObj = copyResponse(response);
            if(retObj != null){
            	System.out.println("tokens:"+retObj);
	    		JSONObject tokenObj = retObj.getJSONObject("token").getJSONObject("items");
	            return tokenObj;
            }
        } catch(Exception e){
            LOG.debug("getFormTokens.error\t"+e.getMessage());
        }finally {  
            try {
                if(response != null){
                    response.close();
                }
            } catch (IOException e) {
                LOG.debug("getFormTokens.error\t"+e.getMessage());
            }  
        }
        return null;
    }  
    
    public static String getFormToken(String site, String formName,String tokenKey)  {  
    	JSONObject tokenObj = getFormTokens(site,formName);
    	return tokenObj.getString(tokenKey);
    }

    /**
     * 将返回的Response转化成对象
     * @param response 返回的Response
     * @return
     */
    private static JSONObject copyResponse(CloseableHttpResponse response){
        try {
            int code = response.getStatusLine().getStatusCode();
            //当请求的code返回值不是400的情况
            if((code == HttpStatus.SC_MOVED_TEMPORARILY )
            || (code == HttpStatus.SC_MOVED_PERMANENTLY)
            || (code == HttpStatus.SC_SEE_OTHER)
            || (code == HttpStatus.SC_TEMPORARY_REDIRECT)) {
                return null;
            }else{
                return copyInputStream(response.getEntity().getContent());
            }
        } catch (Exception e) {
            LOG.debug("copyResponse.error\t"+e.getMessage());
        }
        return null;
    }

    private static JSONObject copyInputStream(InputStream in){
        try {
           BufferedReader reader = new BufferedReader(new InputStreamReader(in,DEFALUT_ENCODE));
           String line = null;
           StringBuffer sb = new StringBuffer();
           while((line = reader.readLine()) != null){
               sb.append(line);
           }
           LOG.debug("FormEngineResponse.result="+sb.toString());
           return JSONObject.fromObject(sb.toString());
       } catch (Exception e) {
           LOG.debug("copyInputStream.error:" + e.getMessage());
       }
       return null;
   }


    /**
     * 表单引擎发送标准post 请求
     * @param site 服务地址
     * @param actionUrl
     * @param parameters
     * @param token
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static JSONObject post(String site,String actionUrl, JSONObject parameters,String token){  
        HttpPost httpPost = new HttpPost(site+actionUrl); 
        CloseableHttpResponse response = null;  
        try {  
        	if(token!=null){
                httpPost.addHeader("token", token);
        	}
            //设定请求的参数
            setRequestParamter(parameters, httpPost);
            //发送请求
            response = httpClient.execute(httpPost, context);
            return copyResponse(response);
        }catch(Exception e){
            LOG.error("post.error:"+e.getMessage());
        }finally {  
            try {
                if(response != null){
                    response.close();
                }
            } catch (IOException e) {
            	LOG.error("post.error:"+e.getMessage());
            }  
        }  
        return null;  
    }

    /**
     * 设定POST请求的参数
     * @param parameters
     * @param httpPost
     * @throws UnsupportedEncodingException
     */
    private static void setRequestParamter(JSONObject parameters, HttpPost httpPost)
            throws UnsupportedEncodingException {
    	 JSONObject requestObj = new JSONObject();
		 requestObj.put("app", "crm");
		 requestObj.put("ver", "1.0");
		 requestObj.put("time", "2018-05-10 20:52:00");
		 requestObj.put("data", parameters);
		 httpPost.addHeader("Content-type", "application/json; charset=utf-8");
		 httpPost.setHeader("Accept", "application/json");
		 httpPost.setEntity(new StringEntity(requestObj.toString(), Charset.forName(DEFALUT_ENCODE)));
    }  


    /** 
     * 手动增加cookie 
     * @param name 
     * @param value 
     * @param domain 
     * @param path 
     */  
    public void addCookie(String name, String value, String domain, String path) {  
        BasicClientCookie cookie = new BasicClientCookie(name, value);  
        cookie.setDomain(domain);  
        cookie.setPath(path);  
        cookieStore.addCookie(cookie);  
    }  

    /** 
     * 把结果console出来 
     *  
     * @param httpResponse 
     * @throws ParseException 
     * @throws IOException 
     */  
    public static void printResponse(HttpResponse httpResponse) throws ParseException, IOException {  
        // 获取响应消息实体  
        HttpEntity entity = httpResponse.getEntity();  
        // 响应状态  
        System.out.println("status:" + httpResponse.getStatusLine());  
        System.out.println("headers:");  
        HeaderIterator iterator = httpResponse.headerIterator();  
        while (iterator.hasNext()) {  
            System.out.println("\t" + iterator.next());  
        }  
    }  

    /** 
     * 把当前cookie从控制台输出出来 
     *  
     */  
    public static void printCookies() {  
        cookieStore = context.getCookieStore();  
        List<Cookie> cookies = cookieStore.getCookies();  
        for (Cookie cookie : cookies) {  
            System.out.println("key:" + cookie.getName() + "  value:" + cookie.getValue());  
        }  
    }  

    /** 
     * 检查cookie的键值是否包含传参 
     *  
     * @param key 
     * @return 
     */  
    public static boolean checkCookie(String key) {  
        cookieStore = context.getCookieStore();  
        List<Cookie> cookies = cookieStore.getCookies();  
        boolean res = false;  
        for (Cookie cookie : cookies) {  
            if (cookie.getName().equals(key)) {  
                res = true;  
                break;  
            }  
        }  
        return res;  
    }  

    /** 
     * 直接把Response内的Entity内容转换成String 
     *  
     * @param httpResponse 
     * @return 
     * @throws ParseException 
     * @throws IOException 
     */  
    public static String toString(CloseableHttpResponse httpResponse) throws ParseException, IOException {  
        // 获取响应消息实体  
        HttpEntity entity = httpResponse.getEntity();  
        if (entity != null)  
            return EntityUtils.toString(entity);  
        else  
            return null;  
    }  
}  
