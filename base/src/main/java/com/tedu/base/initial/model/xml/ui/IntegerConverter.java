package com.tedu.base.initial.model.xml.ui;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class IntegerConverter implements Converter {

    @Override
    public boolean canConvert(Class type) {
    	 return type.equals(int.class) || type.equals(Integer.class);
    }

    /**
     * 将java对象转为xml时使用
     */
    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
    }

    /**
     * 将xml转为java对象使用
     */
    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        return null;
    }
}
