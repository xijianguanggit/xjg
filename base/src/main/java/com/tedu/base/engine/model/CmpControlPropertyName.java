package com.tedu.base.engine.model;

import java.util.Comparator;

import org.apache.commons.lang.ObjectUtils;

import com.tedu.base.initial.model.xml.ui.Control;

/**
 * 组件按绑定属性名检索用
 * @author wangdanfeng
 *
 */
public class CmpControlPropertyName implements Comparator<Control>{
	@Override
	public int compare(Control o1, Control o2) {
		return (ObjectUtils.toString(o1.getProperty()).compareTo(o2.getProperty()));
	}
}
