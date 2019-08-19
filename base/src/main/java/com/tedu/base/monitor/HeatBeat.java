package com.tedu.base.monitor;

import java.io.BufferedReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tedu.base.common.utils.ResponseJSON;
import com.tedu.base.task.SpringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 心跳监测接口
 * 监控tomcat/mysql/redis运行是否正常
 * @author hejiakuo
 *
 */
@Controller
public class HeatBeat {
	private static final Logger log = Logger.getLogger(HeatBeat.class);
	private static final long serialVersionUID = 1L;
	@Value("${file.upload.path}")
	private String DISH;
	@Value("${redis.maxActive}")
	private String MaxActive;
	@Value("${redis.maxIdle}")
	private int MaxIdle;
	@Value("${redis.maxWait}")
	private int MaxWait;
	@Value("${redis.host}")
	private String RedisIP;
	@Value("${redis.port}")
	private int RedisPort;
	@Value("${redis.pass}")
	private String RedisPass;
	@Value("${base.website}")
	private String webSite;

	ResponseJSON responseJSON = null;
	
	private static JedisPool jedisPool = null;
	
	/**
	 * tomcat-0:成功,tomcat-error:失败;1:mysql;2:redis
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/heatBeat")
	@ResponseBody
	public ResponseJSON heatBeat(HttpServletRequest request, HttpServletResponse response) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sysTime = df.format(new Date());
		String serverTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		String res = "0";
		responseJSON = new ResponseJSON();
		if (keepTomcatAlive()) {
			if (!mySqlIsOpen()) {
				res = "1";
			}
			if (!redisMonitor()) {
				res = "2";
			}
		} else {
			res = "error";
		}

		if (res.equals("0")) {
			responseJSON.setMsg("systemTime:" + sysTime + ";serverTime:" + serverTime);
			responseJSON.setStatus(0);
		} else {
			responseJSON.setMsg(res);
			responseJSON.setStatus(1);
		}
		return responseJSON;
	}
	
	
	
	/**
	 * tomcat
	 * 
	 * @author hejiakuo
	 * @throws NullPointerException
	 */
	private boolean keepTomcatAlive() throws NullPointerException {
		String s;
		boolean isTomcatAlive = false;
		BufferedReader in;
		try {
			URL url = new URL(webSite);
			URLConnection con = url.openConnection();
			in = new java.io.BufferedReader(new java.io.InputStreamReader(con.getInputStream()));
			con.setConnectTimeout(1000);
			con.setReadTimeout(4000);
			while ((s = in.readLine()) != null) {
				if (s.length() > 0) {
					isTomcatAlive = true;
				}
			}
		} catch (Exception ex) {
			return isTomcatAlive;
		}
		return isTomcatAlive;
	}
	
	/**
	 * mysql
	 * @return
	 */
	private boolean mySqlIsOpen() {
		DataSource ds = SpringUtils.getBean("dataSource");
		try {
			ds.getConnection();
			PreparedStatement pre = ds.getConnection().prepareStatement("select 1");
			ResultSet rs = pre.executeQuery(); 
			while(rs.next()){
			}
			ds.getConnection().close();
		} catch (SQLException e) {
			return false;
		}
        return true;
	}

	/**
	 * redis
	 */
	private boolean redisMonitor(){
		JedisPoolConfig config = new JedisPoolConfig();
//		config.setMaxActive(MaxActive);
		config.setMaxIdle(MaxIdle);
//		config.setMaxWait(MaxWait);
		config.setTestOnBorrow(true);
		jedisPool = new JedisPool(config,RedisIP, RedisPort, 10000, RedisPass);
		try {
			if(jedisPool != null){
				Jedis resource = jedisPool.getResource();
				resource.ping();
				System.out.println("ok");
			}else{
				System.out.println("redis运行异常");
				return false;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return true;
	}

    /**
     * @see HttpServlet#HttpServlet()
     */
    public HeatBeat() {
        super();
    }
    
}

