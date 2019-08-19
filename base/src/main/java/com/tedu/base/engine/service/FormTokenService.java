package com.tedu.base.engine.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.common.utils.TokenUtils;
import com.tedu.base.engine.model.CSTokenData;
import com.tedu.base.engine.model.ClientContext;
import com.tedu.base.engine.model.ServerTokenData;
import com.tedu.base.engine.model.TreeNode;
import com.tedu.base.engine.util.FormConfiguration;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.engine.util.FormTokenUtil;
import com.tedu.base.initial.model.xml.ui.Control;
import com.tedu.base.initial.model.xml.ui.Flow;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.initial.model.xml.ui.Panel;
import com.tedu.base.initial.model.xml.ui.Param;
import com.tedu.base.initial.model.xml.ui.Procedure;

/**
 * 存储消费token数据
 * @author wangdanfeng
 *
 */
@Service("formTokenService")
public class FormTokenService {
	private static final String P_CLIENT_CONTEXT = "clientContext";
	private Long LIFE = new Long(1000*60);

	@Value("#{'${auth.antidupLogic}'.split(',')}")
	private List<String> antidupLogic;//只能使用一次,消费即删除
	
	@Value("#{'${auth.serverLogic}'.split(',')}")
	private List<String> serverLogic;
	
	@Value("#{'${auth.controlLogic}'.split(',')}")
	private List<String> controlLogic;

	//token数据移至session
	@SuppressWarnings("unchecked")
	private Map<String,ServerTokenData> getTokens(){
		Object o = SessionUtils.getAttrbute(FormConstants.SESSION_TOKENS);
		if(o==null) {
			o = new HashMap<String,ServerTokenData>();
			SessionUtils.setAttrbute(FormConstants.SESSION_TOKENS,o);
			return (Map<String,ServerTokenData>)o;
		}
		return (Map<String,ServerTokenData>)(o);
	}
	
    public Map<String,String> genTokens(String uiId){
		String uiName = getUIName(uiId);
    	//生成token
		CSTokenData csTokenData = genCommonTokens(uiId,uiName);
		getTokens().putAll(csTokenData.getTokenMap());
		setUITokens(uiId,csTokenData.getTokenMap());//记录uiid对应的token set
		FormLogger.logToken(String.format("更新界面{%s}Token成功 [本次生成%s个,累计缓存%s个]",
				uiName==null?uiId:uiName,
				csTokenData.getTokenMap().size(),
				getTokens().size()));		
		return csTokenData.getClientTokenMap();
    }
    
    /**
     * 只在特定逻辑下生成组件可用性控制语句，不再随生成token一起产生
     * @param serverTokenData
     */
    public List<String> genControlStatusStatements(String uiId,Map<String,Object> env){
    	String uiName = getUIName(uiId);
    	String root = this.getRoot(uiId,uiName);
    	env.put(FormConstants.AVIATOR_ENV_ROOT,root );//ui出现的原始入口。常用于返回按钮状态控制表达式.从菜单出现时值为empty string
		List<Flow> listFlow = FormConfiguration.getFlow(uiName);//当前ui下的所有flow
		Boolean isAuth;
		List<String> sList = new ArrayList<>();//控制客户端组件
		if(listFlow==null || listFlow.isEmpty()) return sList;
		for(Flow flow:listFlow){
			if(flow.isOnLoadFlow() || !isAllowed(flow)) continue;//无权限时,UI不输出组件,不需生成可用性控制语句

			//其实不用考虑界面是否真有这个control存在,应该不影响语句执行
			Control c = FormConfiguration.getFlowTriggerControl(uiName,
					ObjectUtils.toString(flow.getTrigger()));//flow对应的触发组件
			if(c==null || !FormTokenUtil.isCommonControl(c)) continue;
				//只对普通组件构造enable语句
			flow.setUiName(uiName);
			//有权限时再校验表达式
			Boolean isEnabledFilter = FormTokenUtil.isEnabledFlow(flow,env);
			//如果无法正常解析权限表达式,则按不可用处理
			isAuth=isEnabledFilter==null?false:isEnabledFilter;
			String clause = FormTokenUtil.genControlStatusStatement(c,isAuth);
			if(!clause.isEmpty()){//解析表达式失败
				sList.add(clause);//根据权限生成组件的可用性语句
				sList.add(FormTokenUtil.genControlStatus(c,isAuth));
			}
		}
		return sList;
    }
    
    
	/**
	 * 过滤ui对应的出合法的flow,创建客户端服务器端token
	 * 控制flow指向的control可用状态
	 * 
	 * 
	 * @param uiName 唯一标识ui
	 * @param env 运行时刻解析表达式用的数据
	 * @return token相关客户端&服务端需要的所有数据
	 */
	private CSTokenData genCommonTokens(String uiId,String uiName){
		CSTokenData ret = new CSTokenData();
		List<Flow> listFlow = FormConfiguration.getFlow(uiName);//当前ui下的所有flow
		String currentTrigger;
		long createTime = System.currentTimeMillis();
		List<Procedure> pList;
		List<ServerTokenData> tokenList = new ArrayList<>();
		Map<String,ServerTokenData> tokenMap = new HashMap<>();
		Map<String,String> clientTokenMap = new HashMap<>();
		String controlName = "";
		if(listFlow==null || listFlow.isEmpty()) return ret;
		
		for(Flow flow:listFlow){
			currentTrigger = ObjectUtils.toString(flow.getTrigger());
			//获取所有procedure的token以及上下文数据
			pList = flow.getProcedureList();
			String flowId = TokenUtils.genUUID();
			ServerTokenData tokenData = null;
			int size = pList==null?0:pList.size();
			for(int i=0;i<size;i++){
				Procedure p = pList.get(i);
				String logic = p.getLogic();
				if(!isServerLogic(logic)){
					continue;
				}
				String functionId = flow.getProcedureFunctionId(uiName, p.getName());
				tokenData = new ServerTokenData(uiName,currentTrigger,flow.getEvent(),p,functionId,p.getLogic());
				tokenData.setUiId(uiId);
				tokenData.setControlName(controlName);
				tokenData.setToken(TokenUtils.genUUID());
				tokenData.setCreateTime(createTime);
				tokenData.setFlowId(flowId);//便于记录userLog
				tokenData.setUrl(p.getServer());
				
				tokenList.add(tokenData);
				tokenMap.put(tokenData.getToken(), tokenData);
				clientTokenMap.put(functionId,tokenData.getToken());
				//flow.setBeginEndProcedure()计算得出
				tokenData.setBeginLogic(p.isBegin());
				tokenData.setEndLogic(p.isEnd());
			}
		}
		ret.setTokenList(tokenList);
		ret.setTokenMap(tokenMap);
		ret.setClientTokenMap(clientTokenMap);
		return ret;
	}

	/**
	 * 有服务端请求的逻辑，用于判断是否生成token
	 * @param logicName
	 * @return
	 */
	private boolean isServerLogic(String logicName){
		return serverLogic.contains(logicName);
	}
	
	/**
	 * 需要携带组件状态更新语句的逻辑
	 * @param logicName
	 * @return
	 */
	public boolean isControlUpdateLogic(String logicName){
		return controlLogic.contains(logicName);
	}
	
	/**
	 * 逻辑执行完毕携带自己的新token
	 * @param serverTokenData
	 */
	public void renewToken(ServerTokenData serverTokenData){
		String oldVal = serverTokenData.getToken();
		String newVal = TokenUtils.genUUID();
		serverTokenData.setToken(newVal);//renew 20171113
		Map<String,ServerTokenData> tokens = getTokens();
		tokens.put(newVal, serverTokenData);
		tokens.remove(oldVal);
		FormLogger.logFlow(String.format("更新Procedure{%s}的防重复提交Logic{%s}的Token ",
				serverTokenData.getTrigger(),
				serverTokenData.getLogic()),FormLogger.LOG_TYPE_INFO);		
	}
	/**
	 * 根据授权表判断当前功能可用性
	 * 格式:ui.panel.control
	 * @param flow
	 * @return 
	 */
    public boolean isAllowed(Flow flow){
    	String userAuthKey = String.format("%s.%s", flow.getUiName(),flow.getTrigger());
    	return (SessionUtils.isAccessibleControl(userAuthKey)) 
    			|| SessionUtils.getUserInfo().isAdminRole() ;    	
    }
    
    /**
     * 
     * 对update,delete,insert等表单操作逻辑防重复提交
     * 其他连击触发时可能会执行多次
     * @param token
     * @return
     */
    public ServerTokenData consumeToken(String token){
    	Map<String,ServerTokenData> tokens = getTokens();
    	ServerTokenData tokenData = tokens.get(token);
    	if(tokenData!=null && isAntidupLogic(tokenData.getLogic())){//防重复提交类的logic，如save，才只允许用一次，一般logic可以继续使用
    		FormLogger.logFlow(String.format("移除Token: Procedure{%s}的防重复提交Logic{%s} ",
    				tokenData.getTrigger(),
    				tokenData.getLogic()),FormLogger.LOG_TYPE_INFO);		
    		return tokens.remove(token);
    	}
    	return tokenData;//除防重复使用的logic外，其他token只验证、续期，不删除
    }

    public ServerTokenData geToken(String token){
    	return getTokens().get(token);
    }
    
    public void seToken(ServerTokenData tokenData){
    	getTokens().put(tokenData.getToken(),tokenData);
    }
    
    /**
     * 当前token的生成时间已经距现在超过LIFE的设定
     * @param token
     * @return
     */
    public boolean expiredToken(String token){
    	ServerTokenData tokenData = getTokens().get(token);
    	return expiredToken(tokenData);
    }
    
    public boolean expiredToken(ServerTokenData tokenData){
    	if(tokenData!=null){
    		return System.currentTimeMillis()>tokenData.getCreateTime()+LIFE;
    	}
    	return false;
    }
    
    /**
     * 生成view时初始化页面token及其他相关信息到页面上下文对象中
     * prepare token & 客户端数据附到model
     * @param uiName
     * @param model
     * @param editMode
     */
	public ClientContext setViewTokens(String uiId,Model model){
		//gen client server tokens, return client tokens
		Map<String,String> clientToken = genTokens(uiId);//for testing
		//construct client context 
		String uiName = getUIName(uiId);//js引用
		ClientContext clientContext = new ClientContext(uiName,clientToken);
		model.addAttribute(P_CLIENT_CONTEXT,clientContext.toString());//用于view解析
		return clientContext;
	}
	
	/**
	 * datagird row based token
	 * 验证当前datagrid里所有GridButton类型的control，重写每行功能权限。
	 * 20180828：DataLink的filter未实现，先解决前端datalink组件null值进行了显示处理的问题
	 * @param uiName
	 * @param env
	 * @return
	 */
	public void setQueryTokens(ServerTokenData serverTokenData,Map<String,Object> env,List<Map<String,Object>> dataList){
		String uiName = serverTokenData.getUiName();
		if(env == null){
			env = new HashMap<>();
		}
		//查询表格返回时才加工操作列
		Panel dgPanel = null;
		if(!(FormConstants.LOGIC_QUERY.equals(serverTokenData.getLogic()) || FormConstants.LOGIC_SEARCH.equals(serverTokenData.getLogic()))){
			return ;
		}
		Procedure proc = FormConfiguration.getProcedure(serverTokenData);
		Param param = proc.getParam(Param.P_OutputPannelId);
		dgPanel = FormConfiguration.getPanel(uiName, param.getValue());//表格panel
		String dgPanelName = param.getValue();
		if(dgPanel==null){
			throw new ServiceException(ErrorCode.ILLEGAL_REQUEST_PARAMETER,
					String.format("[%s][%s].Out不存在", 
							serverTokenData.getUiName(),serverTokenData.getFlowId()));				
		}
		List<Control> dgControlList = dgPanel.getControlList();
		int n = 1;
		for(Control c:dgControlList){
			if(!c.getType().equalsIgnoreCase(Control.TYPE_LINK) &&  
					!c.getType().equalsIgnoreCase(Control.TYPE_DATALINK)) continue;//遍历操作列or数据链接
			String opTrigger = String.format("%s.%s", dgPanelName,c.getName());
			Flow flow = FormConfiguration.getUI(uiName).getFlow(opTrigger, FormConstants.EVENT_CLICK);
			if(flow == null){
				FormLogger.logConf("未配置操作列flow", opTrigger);
				flow = new Flow();//绕过权限控制，未配置的操作链接名称可显示
			}
			flow.setUiName(uiName);
			boolean isFlowAllowed = isAllowed(flow);//有授权
			//找到操作列对应的flow的filter，
			if(isFlowAllowed && dataList!=null){
				for(Map<String,Object> dataMap:dataList){
					env.put("row", dataMap);
					Boolean isAuthFlow = FormTokenUtil.isEnabledFlow(flow,env);
					n++;
					processControl(c,dataMap,isAuthFlow,isFlowAllowed);
				}
			}else if(dataList!=null){
				FormLogger.logFlow(String.format("操作列 {%s} {%s}未授权", c.getTitle(),opTrigger), FormLogger.LOG_TYPE_DEBUG);
			}
		}
	}

	private void processControl(Control c,Map<String,Object> dataMap,Boolean isAuthFlow,Boolean isFlowAllowed){
		if(c.getType().equalsIgnoreCase(Control.TYPE_LINK)){
			processLink(c,dataMap,isAuthFlow,isFlowAllowed);
		}else if(c.getType().equalsIgnoreCase(Control.TYPE_DATALINK)){
			processDataLink(c,dataMap,isAuthFlow,isFlowAllowed);
		}
	}

	private void processLink(Control c,Map<String,Object> dataMap,Boolean isAuthFlow,Boolean isFlowAllowed){
		if(isAuthFlow!=null && isFlowAllowed && isAuthFlow){//操作在当前数据行环境下是否可用
			dataMap.put(c.getName(), c.getTitle());
		}else{
			dataMap.put(c.getName(), "");
		}
	}

	private void processDataLink(Control c,Map<String,Object> dataMap,Boolean isAuthFlow,Boolean isFlowAllowed){
		if(isAuthFlow!=null && isFlowAllowed && isAuthFlow){//操作在当前数据行环境下是否可用
//			dataMap.put(c.getName(), c.getTitle());//有权限的datalink直接放行不处理，无权限的加入标识列
		}else{
			dataMap.put("x_"+c.getName(), "");
		}
	}

	public boolean isAntidupLogic(String logic){
		return antidupLogic.contains(logic);
	}
	
	/**
	 * 缓存当前UI节点,若是从transition导航过来的，可以清除其父UI缓存
	 * 生成ui相关token
	 * 之后flow执行时只重新生成防重复提交的token
	 * @param node
	 * @param model
	 */
	public ClientContext initNode(TreeNode node,Model model){
		String parentId = node.getPid();
		SessionUtils.setUINode(node);
		//父节点类型如果是可清除的,清除相关缓存:token等
		if(parentId!=null &&ObjectUtils.toString(node.getUiType()).equals(FormConstants.LOGIC_TRANSITION)
					){
			this.cleanTokens(parentId);//如果父页面是transition过来，则不保留token
		}
		//ui token
		return setViewTokens(node.getId(), model);
	}
	
	
	/**
	 * 根据ui唯一标识获取uiname
	 * @param uiId
	 * @return
	 */
	public String getUIName(String uiId){
		TreeNode node = SessionUtils.getUINode(uiId);
		return node==null?null:node.getName();
	}
	
	/**
	 * 每次生成ui tokens时，清理掉之前的token，防止持续增长
	 * @param uiId
	 * @param tokens
	 */
	public void setUITokens(String uiId,Map<String, ServerTokenData> tokenMap){
		if(uiId==null || uiId.isEmpty()) return;//没传
		if(SessionUtils.getUINode(uiId)==null){
			FormLogger.logFlow(String.format("uiId[%s]对应的ui未记录", uiId),FormLogger.LOG_TYPE_INFO);
			return;
		}
		//缓存token到ui节点
		TreeNode currentNode = SessionUtils.getUINode(uiId);
		currentNode.setTokens(tokenMap,getTokens());
		String currentUIType = currentNode.getUiType();
		//transition跳转的页面可以直接清除父页面token
		if(currentNode.hasParent() && 
				(currentUIType.equals(FormConstants.LOGIC_TRANSITION) || 
						currentUIType.equals(FormConstants.LOGIC_BACK))){
			cleanTokens(currentNode.getPid());//返回到当前UI时,清除父页面token
		}else if(currentUIType.equals(FormConstants.LOGIC_POPUP)){
			//do nothing
			//等popup关闭回到下一页面时再销毁token
		}
	}
	
	/**
	 * treeMap中有parentId && parentId有指向自己的，中间UI，清除.
	 * 不过会有一些弹窗不适合清除。否则返回若不配刷新逻辑，会造成父UI的TOKEN全部失效
	 * 
	 * 20171031 ：客户端会在UI关闭时主动请求清理。
	 * 请求中的uiid及下属所有子页面的token均被清除
	 */
	public void cleanTokens(String uiId){
		TreeNode node = SessionUtils.getUINode(uiId);
		//顶层是菜单入口UI，不删
		if(node!=null && node.getTokens()!=null ){
			getTokens().keySet().removeAll(node.getTokens().keySet());//
			FormLogger.logToken(String.format("-%s个=%s个", node.getTokens().size(),getTokens().keySet().size()));
		}
	}
	
	/**
	 * 展示访问线路(含所有中间步骤)
	 * @param uiId
	 * @param uiName
	 * @return
	 */
	public String getTreePath(String uiId,String uiName){
		TreeNode node = SessionUtils.getUINode(uiId);
		if(node==null) return "";
		if(node.getUiType()!=null && node.getUiType().equals(FormConstants.UI_TYPE_MENU_ITEM)){
			return "点击菜单项" + node.getName();
		}
		if(uiId!=null && !uiId.isEmpty()){
			return getTreePath(node.getPid(),
					String.format("%s...%s<%s>", node.getName(),uiName,
							ObjectUtils.toString(node.getUiType()).isEmpty()?FormConstants.UI_TYPE_MENU_ITEM:node.getUiType())
					);
		}else{
			return uiName;
		}
	}
	
	/**
	 * 其他UI，返回uiName
	 * @param uiId
	 * @param uiName
	 * @return ui的顶层入口,若是菜单项,返回空字符串
	 */
	public String getRoot(String uiId,String uiName){
		TreeNode node = SessionUtils.getUINode(uiId);
		if(node==null) return null;
		if(node.getPid()==null){
			if(uiName.equals(node.getName())){
				return "";//点击菜单项
			}else{
				return node.getName();
			}
		}else{
			return getRoot(node.getPid(),uiName);
		}
	}
	
	/**
	 * 用于ui内的filter等解析表达式
	 */
	private void setUIModel(String uiId,String panelName,String type,Object data){
		if(!(data instanceof Map)) return;//只存放key value map
		TreeNode node = SessionUtils.getUINode(uiId);
		if(node==null) return;
		
		@SuppressWarnings("unchecked")
		Map<String,Object> subModel = (Map<String,Object>)node.getModel().get(type);
		if(subModel == null) {
			subModel = new HashMap<>();
		}
		subModel.put(panelName, data);
	}
	
	/**
	 * 获得ui下缓存的in out panel data用于解析表达式
	 * @param uiId
	 * @return
	 */
	public Map<String,Object> getUIModel(String uiId){
		TreeNode node = SessionUtils.getUINode(uiId);
		if(node==null) return new HashMap<>();
		return node.getModel();
	}
	
	
	public void setUILogicModel(String uiId,String panelName,Object data){
		setUIModel(uiId,panelName,FormConstants.AVIATOR_ENV_LOGIC,data);
	}
	
	public void setUIModel(String uiId,String panelName,Object data){
		if(!(data instanceof Map)) return;//只存放key value map
		TreeNode node = SessionUtils.getUINode(uiId);
		if(node==null) return;
		node.getModel().put(panelName, data);
	}
}
