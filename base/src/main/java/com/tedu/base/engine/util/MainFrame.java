package com.tedu.base.engine.util;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.common.utils.MD5Util;

import net.sf.json.JSONObject;

public class MainFrame extends JFrame {
	JTextField txtServer = new JTextField("http://localhost:8080",30);
	JTextField txtApi = new JTextField("/api/query",20);
	JTextField txtForm = new JTextField("frmCrmOrderList",20);
	JTextField txtTokenKey = new JTextField("OnClick_pToolbar_btnQuery_frmCrmOrderList_jsquery",30);
	
	JTextField txtUser = new JTextField("lichunming",30);
	JTextField txtPassword = new JTextField("123456",30);
	JTextField txtLogin = new JTextField("/login",30);
	
	JTextArea txtRequest = new JTextArea(4,50);
	JTextArea txtResponse = new JTextArea(4,30);
	JTextArea txtConsole = new JTextArea(14,20);
	JButton btnSend = new JButton("send post S");
	JButton btnCommonSend = new JButton("common post T");
	JButton btnLogin = new JButton("login A");
	JButton btnClear = new JButton("clear C");
	JButton btnLoginAn = new JButton("匿名登录 ");
	JButton btnClearAll = new JButton("clear all X");
	JPanel nPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,12,12));
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	JSONObject tokens = new JSONObject();
	public MainFrame (){
		this.setLayout(new BorderLayout());
		redirectConsoleTo(txtConsole);
		initLookAndFeel();
		initConfigPanel();
		
		btnLogin.setMnemonic('A');
		btnCommonSend.setMnemonic('T');
		btnCommonSend.setToolTipText("普通http post请求,只使用action 和请求串构造");
		btnSend.setMnemonic('S');
		btnClear.setMnemonic('C');
		btnClearAll.setMnemonic('X');
		
		JPanel cPanel = new JPanel(new BorderLayout(2,2));
		JPanel cCenterPanel = new JPanel(new BorderLayout(4,4));
		
		JScrollPane scroll1 = new JScrollPane(txtRequest,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JScrollPane scroll2 = new JScrollPane(txtResponse,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JScrollPane scroll3 = new JScrollPane(txtConsole,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"请求串(data:)"));
		scroll2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"返回串"));
		scroll3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"控制台"));
		nPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"设置"));

		txtResponse.setEditable(false);
		txtConsole.setEditable(false);
		txtConsole.setLineWrap(true);
		
		JSplitPane splitLeft = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitLeft.setTopComponent(scroll1);
		splitLeft.setBottomComponent(scroll2);
		
		cCenterPanel.add(splitLeft,BorderLayout.CENTER);
		
		JSplitPane splitMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitMain.setLeftComponent(cCenterPanel);
		splitMain.setRightComponent(scroll3);
		cPanel.add(splitMain,BorderLayout.CENTER);
		
		JPanel sPanel = new JPanel();
		sPanel.add(btnLoginAn);
		sPanel.add(btnLogin);
//		sPanel.add(btnCommonSend);
		sPanel.add(btnSend);
		sPanel.add(btnClear);
		sPanel.add(btnClearAll);
		initListener();
		this.add(nPanel,BorderLayout.NORTH);
		this.add(cPanel,BorderLayout.CENTER);
		this.add(sPanel,BorderLayout.SOUTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		txtRequest.setText("{'lk_mobile':'1'}");
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("lk_mobile", "1");
		try{
			txtRequest.setText(gson.toJson(jsonObj));
		}catch(Exception e){
			System.out.println("格式化请求串异常:" + e.getMessage());
		}

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2018);
		cal.set(Calendar.MONTH, Calendar.JUNE);
		cal.set(Calendar.DAY_OF_MONTH, 30);
	}
	
	private void initListener(){
		btnLogin.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				btnLogin.setEnabled(false);
				boolean ret = doLogin();
				btnSend.setEnabled(ret);
				btnLogin.setEnabled(true);
			}
		});
		btnLoginAn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean ret = doLoginAn();
			}
		});
		btnSend.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				doSend();
			}
		});		
		btnCommonSend.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				doCommonSend();
			}
		});		
		
		
		btnClear.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				doClear();
			}
		});		
		btnClearAll.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				doClearAll();
			}
		});	
	}
	
	private void doClear(){
		redirectConsoleTo(txtConsole);
		txtConsole.setText("");
	}

	private void doClearAll(){
		doClear();
		txtRequest.setText("");
		txtResponse.setText("");
	}

	private void doSend(){
		//2、获取token
		String token = FormEngineHttpClient.getFormToken(
				txtServer.getText(),txtForm.getText(),txtTokenKey.getText());
		
		//3、构造查询参数,获取查询结果
		String strRequest = txtRequest.getText();
		JSONObject param = new JSONObject();
		if(!strRequest.isEmpty()){
			param = JSONObject.fromObject(strRequest);
			try{
				txtRequest.setText(gson.toJson(param));
			}catch(Exception e){
				System.out.println("格式化请求串异常:" + e.getMessage());
			}
		}
		JSONObject resObj = FormEngineHttpClient.post(txtServer.getText(),txtApi.getText(),param,token);
		System.out.println("..........");
		txtResponse.setText(gson.toJson(resObj));
		txtResponse.setCaretPosition(0);
	}
	
	
	//普通请求，读取action + post 构造
	private void doCommonSend(){
		//2、获取token
		//3、构造查询参数,获取查询结果
		String strRequest = txtRequest.getText();
		JSONObject param = new JSONObject();
		if(!strRequest.isEmpty()){
			param = JSONObject.fromObject(strRequest);
			try{
				txtRequest.setText(gson.toJson(param));
			}catch(Exception e){
				System.out.println("格式化请求串异常:" + e.getMessage());
			}
		}
		JSONObject resObj = FormEngineHttpClient.post(txtServer.getText(),txtApi.getText(),param,"");
		txtResponse.setText(gson.toJson(resObj));
		txtResponse.setCaretPosition(0);
	}
	
	public boolean doLogin(){
		boolean isAuth = false;
		JSONObject data = new JSONObject();
		data.put("userName", txtUser.getText());
		data.put("password", txtPassword.getText());//"E10ADC3949BA59ABBE56E057F20F883E"
		data.put("validateCode", "");
		
		System.out.println("request login :" + txtServer.getText() + txtLogin.getText().trim() + "\n" + data);
		
		JSONObject res = FormEngineHttpClient.post(txtServer.getText(),txtLogin.getText().trim(), data,null);
		if(res!=null && res.getString("code").equals(FormEngineHttpClient.CODE_SUCCEED)) {
			System.out.println("login succeeded");
			isAuth = true;
		}
		System.out.println("doLogin.result=" + res + "," + (isAuth?"succeeded":"failed"));	
		
		txtRequest.setText(gson.toJson(data));
		txtResponse.setText(gson.toJson(res));
		txtResponse.setCaretPosition(0);
		txtRequest.setCaretPosition(0);
		return isAuth;
	}
	
	public boolean doLoginAn(){
		boolean isAuth = false;
		JSONObject data = new JSONObject();
		System.out.println("request login :" + txtServer.getText() + txtLogin.getText().trim() + "\n" + data);
		
		JSONObject res = FormEngineHttpClient.post(txtServer.getText(),txtLogin.getText().trim(), data,null);
		if(res!=null && res.getString("code").equals(FormEngineHttpClient.CODE_SUCCEED)) {
			System.out.println("login succeeded");
			isAuth = true;
		}
		System.out.println("doLogin.result=" + res + "," + (isAuth?"succeeded":"failed"));	
		
		txtRequest.setText(gson.toJson(data));
		txtResponse.setText(gson.toJson(res));
		txtResponse.setCaretPosition(0);
		txtRequest.setCaretPosition(0);
		return isAuth;
	}
	
	private void redirectConsoleTo(final JTextArea textarea) {
	    PrintStream out = new PrintStream(new ByteArrayOutputStream() {
	        public synchronized void flush() throws IOException {
	            textarea.setText(toString());
	        }
	    }, true){
	        public void println(String s) {
	            super.println( DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24,new Date()) + "  " + s);
	        }
	    };
	    System.setErr(out);
	    System.setOut(out);
	}
	
	private void initLookAndFeel(){
		try {
	        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
	    } catch (Exception e) {
	    	System.out.println(e.getMessage());
	    }
		Color bgColor = Color.BLACK;
		UIDefaults defaults = new UIDefaults();
		defaults.put("EditorPane[Enabled].backgroundPainter", bgColor);
		txtConsole.putClientProperty("Nimbus.Overrides", defaults);
		txtConsole.putClientProperty("Nimbus.Overrides.InheritDefaults", true);
		txtConsole.setBackground(bgColor);
		txtConsole.setForeground(Color.GREEN);
	}
	
	private void initConfigPanel(){
		GridBagLayout gbl = new GridBagLayout();  
		GridBagConstraints gbs = new GridBagConstraints();  
		nPanel.setLayout(gbl);
		
		
		JLabel lblServer = new JLabel("Server:");
		JLabel lblUser = new JLabel("User:");
		JLabel lblPassword = new JLabel("Password:");
		JLabel lblUI = new JLabel("UI Name:");
		JLabel lblAction = new JLabel("Action:");
		JLabel lblToken = new JLabel("TokenKey:");
		JLabel lblLogin = new JLabel("Login Action:");
		
		nPanel.add(lblServer);
		nPanel.add(txtServer);
		nPanel.add(lblUser);
		nPanel.add(txtUser);
		nPanel.add(lblPassword);
		nPanel.add(txtPassword);
		nPanel.add(lblLogin);
		nPanel.add(txtLogin);
		
		nPanel.add(lblUI);
		nPanel.add(txtForm);
		nPanel.add(lblAction);
		nPanel.add(txtApi);	
		nPanel.add(lblToken);
		nPanel.add(txtTokenKey);
		
		
		Insets insets = new Insets(5, 5, 5, 5);
		gbs.insets=insets;
		gbs.weightx=1;gbs.weighty=1;
		gbs.fill=GridBagConstraints.BOTH;gbs.gridwidth=1;gbs.gridheight=1;  
		
	    gbs.gridx=GridBagConstraints.RELATIVE;gbs.gridy=0;gbs.gridwidth=1;  
	    gbl.setConstraints(lblServer, gbs);  

	    gbs.gridx=GridBagConstraints.RELATIVE;gbs.gridy=0;gbs.gridwidth=10;  
	    gbl.setConstraints(txtServer, gbs);  

	    gbs.gridx=GridBagConstraints.RELATIVE;gbs.gridy=0;gbs.gridwidth=1;   
	    gbl.setConstraints(lblUser, gbs);  
		
	    
	    gbs.gridx=GridBagConstraints.RELATIVE;gbs.gridy=0;gbs.gridwidth=6;  
	    gbl.setConstraints(txtUser, gbs);  
	    
	    gbs.gridx=GridBagConstraints.RELATIVE;gbs.gridy=0;gbs.gridwidth=1;    
	    gbl.setConstraints(lblPassword, gbs);  
	    
	    gbs.gridx=GridBagConstraints.RELATIVE;gbs.gridy=0;gbs.gridwidth=4;  
	    gbl.setConstraints(txtPassword, gbs);  
	   
	    gbs.gridx=GridBagConstraints.RELATIVE;gbs.gridy=0;gbs.gridwidth=1;  
	    gbl.setConstraints(lblLogin, gbs);  
	    
	    gbs.gridx=GridBagConstraints.RELATIVE;gbs.gridy=0;gbs.gridwidth=14;  
	    gbl.setConstraints(txtLogin, gbs);  
	    
	    //第二行
	    gbs.gridx=GridBagConstraints.RELATIVE;gbs.gridy=1;gbs.gridwidth=1;  
	    gbl.setConstraints(lblUI, gbs);  

	    gbs.gridx=GridBagConstraints.RELATIVE;gbs.gridy=1;gbs.gridwidth=10;  
	    gbl.setConstraints(txtForm, gbs);  

	    gbs.gridx=GridBagConstraints.RELATIVE;gbs.gridy=1;gbs.gridwidth=1;   
	    gbl.setConstraints(lblAction, gbs);  
		
	    gbs.gridx=GridBagConstraints.RELATIVE;gbs.gridy=1;gbs.gridwidth=6;  
	    gbl.setConstraints(txtApi, gbs);  
	    
	    gbs.gridx=GridBagConstraints.RELATIVE;gbs.gridy=1;gbs.gridwidth=1;    
	    gbl.setConstraints(lblToken, gbs);  
	    
	    gbs.gridx=GridBagConstraints.RELATIVE;gbs.gridy=1;gbs.gridwidth=20;  
	    gbl.setConstraints(txtTokenKey, gbs);
	}
	

	public static void main(String[] args){
		MainFrame frm = new MainFrame();
		frm.setTitle("模拟请求工具");
		frm.setSize(new Dimension(1280,700));
//		frm.setExtendedState(frm.getExtendedState()|JFrame.MAXIMIZED_BOTH );
		frm.setVisible(true);
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		int x = (int)(toolkit.getScreenSize().getWidth()-frm.getWidth())/2;
		int y = (int)(toolkit.getScreenSize().getHeight()-frm.getHeight())/2;

		frm.setLocation(x, y);
		frm.setVisible(true);
	}
}
