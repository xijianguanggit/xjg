package com.tedu.base.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
/**
 * 日期工具类
 * @author xijianguang
 *
 */
public class DateUtils {

	public static final String YYMMDD_HHMMSS="yyyy-MM-dd hh:mm:ss";
	public static final String YYMMDD_HHMMSS_24="yyyy-MM-dd HH:mm:ss";
	public static final String YYMMDD="yyyy-MM-dd";
	public static final String YYMM="yyyy-MM";
	/**
	 * 获取下一天
	 * @param date
	 * @return
	 * @throws Exception 
	 */
	public static String getNextDay(String date) throws Exception {
		return getDiffDay(date,1);
	}
	
	public static String getPreviousDay(String date) throws Exception {
		return getDiffDay(date,-1);
	}
	
	public static String getDiffDay(String date,int diff) throws Exception {
		if(date == null || date.length() < 1) return null ;
		if(diff == 0) return date;
		return DateUtils.getDateToStr( YYMMDD , 
			   DateUtils.addDays(DateUtils.getStrToDate(DateUtils.YYMMDD, date), diff ));
	}
	/**
	 * 日期类型转字符串类型
	 * @param format 日期格式化
	 * @param date 请求参数
	 * @return
	 */
	public static String getDateToStr(String format , Date date){
		return new SimpleDateFormat(format).format(date);
	}
	
	/**
	 * 长整型类型转字符串类型
	 * @param format 日期格式化
	 * @param time 请求参数
	 * @return
	 */
	public static String getLongToStr(String format , Long time){
		return DateUtils.getDateToStr(format, new Date(time));
	}
	/**
	 * 字符串转日期
	 * @param format  日期格式化
	 * @param timeStr 请求参数
	 * @return
	 */

	public static Date getStrToDate( String format, String timeStr) throws ParseException{
			return new SimpleDateFormat(format).parse(timeStr);
	}

	/**
	 * 增加月份
	 * @param Date 日期，不能为null
	 * @param months 增加的月份数
	 * @return
	 */
	public static Date addMonths(Date beginDate , int months){
		Calendar date = Calendar.getInstance();
		date.setTime(beginDate);
		date.add(Calendar.MONTH, months);
		return date.getTime();
	}

	/**
	 * 增加天数
	 * @param beginDate 日期，不能为null
	 * @param days 增加的天数
	 * @return
	 */
	public static Date addDays(Date beginDate , int days){
		Calendar date = Calendar.getInstance();
		date.setTime(beginDate);
		date.add(Calendar.DAY_OF_MONTH, days);
		return date.getTime();
	}
	public static String addDays(String beginDate , int days) throws ParseException{
		Date beginDate1 =DateUtils.getStrToDate(DateUtils.YYMMDD, beginDate);
		Calendar date = Calendar.getInstance();
		date.setTime(beginDate1);
		date.add(Calendar.DAY_OF_MONTH, days);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date.getTime());
	}
	/**
	 * 增加分钟
	 * @param beginDate 日期，不能为null
	 * @param mins  增加的分钟数
	 * @return
	 */
	public static Date addMinute(Date beginDate , int mins){
		Calendar date = Calendar.getInstance();
		date.setTime(beginDate);
		date.add(Calendar.MINUTE, mins);
		return date.getTime();
	}

	/**
	 * 获取某年某月的第一天
	 * @param year  年
	 * @param month 月
	 * @return
	 */
	 
	public static Date getMonthFirstDay(int year, int month){
		Calendar curCal = Calendar.getInstance();
		curCal.set( Calendar.YEAR, year);
		curCal.set( Calendar.MONTH, month-1);
		curCal.set(Calendar.DAY_OF_MONTH, 1);
		curCal.set(Calendar.HOUR_OF_DAY , 0);
		curCal.set(Calendar.MINUTE, 0);
		curCal.set(Calendar.SECOND, 0);
		curCal.set(Calendar.MILLISECOND, 0);
		return curCal.getTime();
	}
	 /**
	  * 得到某年某月的最后一天
	  * @param year
	  * @param month
	  * @return
	  */
	 public Date getMonthLastDay(int year, int month){
	  Calendar cal = Calendar.getInstance();
	  cal.set(Calendar.YEAR, year);
	  cal.set(Calendar.MONTH, month-1);
	  cal.set(Calendar.DAY_OF_MONTH, 1);

	  int value = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	  cal.set(Calendar.DAY_OF_MONTH, value);
	  cal.set(Calendar.HOUR_OF_DAY, 0);
	  cal.set(Calendar.MINUTE, 0);
	  cal.set(Calendar.SECOND, 0);
	  cal.set(Calendar.MILLISECOND, 0);

	  return cal.getTime();

	 }

	/**
	 * 获取某年某月的下个月的第一天
	 * @param year 年
	 * @param month 月
	 * @return
	 */
	public static Date getAfterMonthFirstDay( int year, int month){
		Calendar curCal = Calendar.getInstance();
		curCal.set( Calendar.YEAR , year);
		curCal.set( Calendar.MONTH, month);
		curCal.set(Calendar.DAY_OF_MONTH, 1);
		curCal.set(Calendar.HOUR_OF_DAY, 0);
		curCal.set(Calendar.MINUTE, 0);
		curCal.set(Calendar.SECOND, 0);
		curCal.set(Calendar.MILLISECOND, 0);
		return curCal.getTime();
	}
	/**
	 * 获取当前日期的天
	 * @param date 日期
	 * @return
	 */
	public static int getDayOfMonth(Date date){
		if(date == null) return 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return day;
	}
	/**
	 * 获取当前日期的月份
	 * @param date 日期
	 * @return
	 */
	public static int getMonth(Date date){
		if(date == null) return 0 ;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH) + 1;
		return month ;
	}
	/**
	 * 获取当前日期的年
	 * @param date 日期
	 * @return
	 */
	public static int getYear(Date date){
		if(date == null) return 0 ;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR) ;
		return year;
	}
	/**
	 * 获得两个日期之前相差的月份
	 * @param start
	 * @param e
	 * @return
	 */
	public static int getMonthNum(Date start, Date end){
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(start);
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(end);
		Calendar temp = Calendar.getInstance();
		temp.setTime(end);
		int year = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
		int month = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
		return (year * 12 + month);
	}

	/**
	 * 获得当天的0点0分0秒
	 * @param date 日期
	 * @return
	 */
	public static Date getDateStart(Date date) throws Exception{
		String date_str = new SimpleDateFormat("yyyy-MM-dd").format(date);
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date_str+" 0:0:0");
	}
	/**
	 * 获得当天的0点0分0秒
	 * @param date 字符串日期
	 * @return
	 */
	public static Date getDateStart(String date) throws Exception{
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date+" 0:0:0");
	}
	/**
	 * 获得时间之前一个月的第一天
	 * @param date
	 * @return
	 */
	public static Date getBeforeMonthFirstDay(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date lastDate = calendar.getTime();
		return lastDate;
	}
	/**
	 * 获得时间之前一个月的最后一天
	 * @param date
	 * @return
	 */
	public static Date getBeforeMonthLastDay(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
		int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, lastDay);
		Date lastDate = calendar.getTime();
		return lastDate;
	}
	/**
	 * 判断两个日期是否是在同一天
	 * @param day1
	 * @param day2
	 * @return
	 */
	public static boolean isOneDay(Date day1, Date day2){
		if(day1 == null ||  day2 == null) return false;
		String day1_str = DateUtils.getDateToStr(DateUtils.YYMMDD, day1);
		String day2_str = DateUtils.getDateToStr(DateUtils.YYMMDD, day2);
		if(day1_str.equals(day2_str)) {
			return true;
		}
		return false;
	}
	/**
	 * 获得此月的天数
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getDaysOfMonth(int year, int month){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month-1);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 获取日期
	 * @param timeType 时间类型，譬如：Calendar.DAY_OF_YEAR
	 * @param timenum  时间数字，譬如：-1 昨天，0 今天，1 明天
	 * @param format_string 时间格式，譬如："yyyy-MM-dd HH:mm:ss"
	 * @return 字符串
	 */
	public static String getDateFromNow(int timeType, int timenum, String format_string){
		if ((format_string == null)||(format_string.equals("")))
			format_string = "yyyy-MM-dd HH:mm:ss";
		Calendar cld = Calendar.getInstance();
		Date date = null;
	    DateFormat df = new SimpleDateFormat(format_string);
		cld.set(timeType, cld.get(timeType) + timenum);
	    date = cld.getTime();
	    return df.format(date);
	}
	/**
	 * 获取当前日期的字符串
	 * @param format_string 时间格式，譬如："yyyy-MM-dd HH:mm:ss"
	 * @return 字符串
	 */
	public static String getDateNow(String format_string){
		if ((format_string == null)||(format_string.equals("")))
			format_string = "yyyy-MM-dd HH:mm:ss";
		Calendar cld = Calendar.getInstance();
	    DateFormat df = new SimpleDateFormat(format_string);
	    return df.format(cld.getTime());
	}
	
	/**
	 * 
	 * @Description: 时间戳转换时间
	 * @author: gaolu
	 * @date: 2017年11月7日 下午6:18:16  
	 * @param:      
	 * @return: String
	 */
    public static String stampToDate(String time){
	    Long timestamp = Long.parseLong(time)*1000;  
	    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp)); 
        return date;
    }
    /**
     * date2比date1多的天数
     * @param date1    
     * @param date2
     * @return    
     */
    public static int differentDays(Date date1,Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
       int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);
        
        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2)   //同一年
        {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++)
            {
                if(i%4==0 && i%100!=0 || i%400==0)    //闰年            
                {
                    timeDistance += 366;
                }
                else    //不是闰年
                {
                    timeDistance += 365;
                }
            }
            
            return timeDistance + (day2-day1) ;
        }
        else    //不同年
        {
            System.out.println("判断day2 - day1 : " + (day2-day1));
            return day2-day1;
        }
    }
    
    public static String getWeekDay (String date){
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(DateUtils.getStrToDate(DateUtils.YYMMDD, date));
			return ConstantUtil.dayNames.get(new SimpleDateFormat("EEEE", Locale.CHINA).format(c.getTime()));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return "";
    }
}