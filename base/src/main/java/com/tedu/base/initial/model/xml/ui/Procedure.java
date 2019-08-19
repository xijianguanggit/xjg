package com.tedu.base.initial.model.xml.ui;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XStreamAlias("procedure")
public class Procedure implements Serializable{
	// 客户端生成js名字 同一个form不能重复
	@XStreamAsAttribute
	private String name;
	// 服务端调用URL
	@XStreamAsAttribute
	private String server;
	// Flow调用的业务逻辑
	@XStreamAsAttribute
	private String logic;
	// 对应Procedure为true时候执行的下一个Procedure
	@XStreamAsAttribute
	private String ifyes;
	// 同上
	@XStreamAsAttribute
	private String ifno;
	//需和指定名称的方法同步
	@XStreamAsAttribute
	private String sync;	
	// 参数
	@XStreamImplicit(itemFieldName = "param")
	private List<Param> paramList;
	// 对应Procedure为true时候执行的下一个Procedure
	private Procedure ifyesProcedure;
	// 同上
	private Procedure ifnoProcedure;
	// 对应的输出组件类型
	private String outObjectType;

//	private Panel panel;
	
	public void setIfyesProcedure(List<Procedure> procedureList){
		for(Procedure procedure:procedureList){
			if(procedure.getName().equals(this.ifyes))
				this.ifyesProcedure = procedure;
		}
	}
	public Procedure getIfyesProcedure(){
		return ifyesProcedure;
	}
	public void setIfnoProcedure(List<Procedure> procedureList){
		for(Procedure procedure:procedureList){
			if(procedure.getName().equals(this.ifno))
				this.ifnoProcedure = procedure;
		}
	}
	
	public Procedure getIfnoProcedure(){
		return ifnoProcedure;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getLogic() {
		return logic;
	}

	public void setLogic(String logic) {
		this.logic = logic;
	}

	public String getIfyes() {
		return ifyes;
	}

	/**
	 * ifyes标签
	 * @return
	 */
	public String getIfyesTag() {
		return ifyesProcedure == null?"":(ifyes + "();");
	}
	
	public void setIfyes(String ifyes) {
		this.ifyes = ifyes;
	}

	public String getIfno() {
		return ifno;
	}

	public String getIfnoTag(){
		return ifnoProcedure==null?"":ifno;
	}
	
	public void setIfno(String ifno) {
		this.ifno = ifno;
	}

	public List<Param> getParamList() {
		if(paramList == null){
			paramList = new ArrayList<Param>();
		}
		return paramList;
	}

	public void setParamList(List<Param> paramList) {
		this.paramList = paramList;
	}
	public String getSync() {
		return sync;
	}
	public void setSync(String sync) {
		this.sync = sync;
	}
	/**
	 * 找到参数
	 * @param paramName
	 * @return
	 */
	public Param getParam(String paramName) {
		for(Param param:paramList){
			if(param.getName().equalsIgnoreCase(paramName)){
				return param;
			}
		}
		return null;
	}
	public String getOutObjectType() {
		return outObjectType;
	}
	public void setOutObjectType(List<Panel> panelList) {
		if(this.paramList!=null)
		for(Param p : this.paramList){
			if(!StringUtils.isEmpty(p.getValue())&&p.getName().equals(Param.P_OutputPannelId)){
				for(Panel panel : panelList){
					if(p.getValue().split("\\. ")[0].equals(panel.getName())){
						this.outObjectType = panel.getType();
						break;
					}
				}
			}
		}
	}

	private boolean end = false;
	private boolean begin = false;

	public boolean isEnd() {
		return end;
	}
	public void setEnd(boolean end) {
		this.end = end;
	}
	public boolean isBegin() {
		return begin;
	}
	public void setBegin(boolean begin) {
		this.begin = begin;
	}

}
