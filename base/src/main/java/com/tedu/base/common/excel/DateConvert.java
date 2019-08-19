package com.tedu.base.common.excel;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.core.convert.converter.Converter;

public class DateConvert implements Converter<String, Date> {
	public static final Logger log = Logger.getLogger(DateConvert.class);

    @Override
    public Date convert(String stringDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return simpleDateFormat.parse(stringDate);
        } catch (java.text.ParseException e) {
        	log.error(e.getMessage());
		}
        return null;
    }

}
