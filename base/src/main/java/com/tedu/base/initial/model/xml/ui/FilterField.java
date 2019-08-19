package com.tedu.base.initial.model.xml.ui;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("field") 
public class FilterField {
	@XStreamAsAttribute
	private String name;
	@XStreamAsAttribute
	private String sign;
	@XStreamAsAttribute
	private String condition;
	@XStreamAsAttribute
	private String conjunction;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getConjunction() {
		return conjunction;
	}
	public void setConjunction(String conjunction) {
		this.conjunction = conjunction;
	}

}
