package com.tedu.base.engine.model;

import java.util.Comparator;

import org.apache.commons.lang.ObjectUtils;

import com.tedu.base.initial.model.xml.ui.Property;

/**
 * 属性名检索用
 * @author wangdanfeng
 *
 */
public class CmpObjectPropertyName implements Comparator<Property>{
	@Override
	public int compare(Property o1, Property o2) {
		return (ObjectUtils.toString(o1.getName()).compareTo(o2.getName()));
	}
}
