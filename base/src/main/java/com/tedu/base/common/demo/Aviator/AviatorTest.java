package com.tedu.base.common.demo.Aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.tedu.base.auth.login.util.LdapUtil;

import java.util.*;

import org.apache.log4j.Logger;

public class AviatorTest implements Runnable {
	public static final Logger log = Logger.getLogger(AviatorTest.class);
	public void run(){
		Map<String, Object> map1 = plus(999);
		int r1 = (int) map1.get("arg1");
		Map<String, Object> map2 = min(999);
		int r2 = (int) map2.get("arg1");
		Map<String, Object> map3 = muli(999l);
		long r3 = (long) map3.get("arg1");
		Map<String, Object> map4 = dev(1.2);
		double r4 = (double) map4.get("arg1");


		AviatorEvaluator.addFunction(new AddFunction());
		long index = 0;
		while (true) {
			try {
				long dateTime = (new Date()).getTime();
				if ((r1 + r2 + r3 + r4+dateTime) != (double) AviatorEvaluator.execute("add(add(add(" + map1.get("arg2") + "+"
						+ map2.get("arg2") + "," + map3.get("arg2") + ")," + map4.get("arg2") + "), "+dateTime+")")) {
					throw new Exception("第"+index+"比对结果不正确");
				} else{
					index++;
					System.out.println("结果正常,已经执行了"+index+"次");
					System.out.println("执行结果为:"+(r1 + r2 + r3 + r4-dateTime));
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				break;
			}
		}

	}
	private Map<String, Object> plus(int count) {
		// 加法
		StringBuffer countStr = new StringBuffer(String.valueOf(count));
		for (int i = 0; i < 101; i++) {
			count += i;
			countStr.append("+").append(i);
		}
		// Long result = (Long) AviatorEvaluator.execute(countStr.toString());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("arg1", count);
		map.put("arg2", countStr);
		return map;
	}

	private Map<String, Object> min(int count) {

		// 减法
		StringBuffer countStr = new StringBuffer(String.valueOf(count));
		for (int i = 0; i < 101; i++) {
			count -= i;
			countStr.append("-").append(i);
		}
		// Long result = (Long) AviatorEvaluator.execute(countStr.toString());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("arg1", count);
		map.put("arg2", countStr);
		return map;
	}

	private Map<String, Object> muli(Long... num) {
		Long count = 1l;
		StringBuffer countStr = new StringBuffer("1");
		for (int i = 0; i < num.length; i++) {
			count = count * num[i];
			countStr.append("*").append(num[i]);
		}
		// Long result = (Long) AviatorEvaluator.execute(countStr.toString());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("arg1", count);
		map.put("arg2", countStr);
		return map;
	}

	private Map<String, Object> dev(double... num) {
		double count = 1000000.00;
		StringBuffer countStr = new StringBuffer("1000000.00");
		for (int i = 0; i < num.length; i++) {
			count = count / num[i];
			countStr.append("/").append(num[i]);
		}
		// double result = (Double)
		// AviatorEvaluator.execute(countStr.toString());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("arg1", count);
		map.put("arg2", countStr);
		return map;
	}

	private void strPlus(String... str) {
		try {
			Map<String, Object> env = new HashMap<String, Object>();
			StringBuffer sb = new StringBuffer();
			StringBuffer resultsb = new StringBuffer();
			for (int i = 0; i < str.length; i++) {
				sb.append(str[i]);
				resultsb.append(str[i] + "+");
				env.put(str[i], str[i]);
			}
			System.out.println(sb.toString());
			String result = (String) AviatorEvaluator
					.execute(resultsb.toString().substring(0, resultsb.toString().lastIndexOf("+")), env);
			System.out.println(result);
			if (result.equals(resultsb.toString())) {
				throw new Exception("比对结果不正确");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	private void invokeInnerFun(String param) {
		try {
			String str = "string.length('" + param + "')";
			long length = (Long) AviatorEvaluator.execute(str);
			if (length != param.length())
				throw new Exception("比对结果不正确");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	private void defindeFun() {
		AviatorEvaluator.addFunction(new AddFunction());
		System.out.println(AviatorEvaluator.execute("add(1,2)"));
		System.out.println(AviatorEvaluator.execute("add(add(1,2),100)"));
	}

    public static void main(String[] args) {
        Map<String, Object> env = new HashMap<String, Object>();


		//CurrentTime
		//Object o = AviatorEvaluator.execute("CurrentTime()", env);

        //Contains函数
//        env.put("s1", "addsdfdd");
//        env.put("s2", "a");
//        env.put("s", "sdfddd");
//
//
//		System.out.println("Contain:+++" + AviatorEvaluator.execute("Contain(s1,s2)", env));
//
//		Object l =  AviatorEvaluator.execute("Length(s)", env);
//        System.out.println("Length:+++" + AviatorEvaluator.execute("Length(s)", env));
//
//        System.out.println("StartsWith:+++" + AviatorEvaluator.execute("StartWith(s1,s2)", env));
//
//
//        env.put("s", "s_d_ddddds");
//        env.put("regex", "_");
//		env.put("limit", 2);
//
//        env.put("begin", 1);
//        env.put("end", 8);
//        System.out.println("Substring:+++" + AviatorEvaluator.execute("Substring(s,begin,end)", env));
//
//        System.out.println("IndexOf:+++" + AviatorEvaluator.execute("IndexOf(s1,s2)", env));
//
//        String[] a1 = (String[]) AviatorEvaluator.execute("Split(s,regex,limit)", env);
//        //AviatorEvaluator.execute("Split(s,regex)",env);
//
//        String[] testJoinStr = new String[]{"first", "second", "third"};
//
//        String separator = "*";
//
//        env.put("s", testJoinStr);
//        env.put("separator", separator);
//
//        System.out.println("Join:+++" + AviatorEvaluator.execute("Join(s,separator)", env));
//
//        env.put("s", "他sdddf人dsfadfsadf");
//        env.put("regex", "[\\u4e00-\\u9fa5]");
//        env.put("replacement", "*");
//        System.out.println("ReplaceFirst:+++" + AviatorEvaluator.execute("ReplaceFirst(s,regex,replacement)", env));
//        System.out.println("ReplaceAll:+++" + AviatorEvaluator.execute("ReplaceAll(s,regex,replacement)", env));
//
//        env.put("d", 321222.122);
//
//        env.put("s", "今天你好么");
//		env.put("type", 1);
//
//        System.out.println("Double2Chinese:+++" + AviatorEvaluator.execute("Double2Chinese(d)", env));
//        System.out.println("Chinese2Pinyin:+++手动阀撒旦飞洒" + AviatorEvaluator.execute("Chinese2Pinyin(s,type)", env));
//		env.put("type", 2);
//		System.out.println("Chinese2Pinyin:+++手动阀撒旦飞洒" + AviatorEvaluator.execute("Chinese2Pinyin(s,type)", env));
//		env.put("type", 3);
//		System.out.println("Chinese2Pinyin:+++手动阀撒旦飞洒" + AviatorEvaluator.execute("Chinese2Pinyin(s,type)", env));
//
//        env.put("d1", 4);
//        env.put("d2", 2);
//        env.put("s", "dsfasfafa");
//        Object we = AviatorEvaluator.execute("Pow(d1,d2)", env);
//        //System.out.println(ChineseNumberUtil.convertToCapitalCurrency(232323.123));
//        System.out.println("MD5:+++" + AviatorEvaluator.execute("MD5(s)", env));
//        System.out.println("GUID:+++" + AviatorEvaluator.execute("Guid()", env));
//        System.out.println("Rand:+++" + AviatorEvaluator.execute("Rand()", env));
////        env.put("date", new Date());
////        env.put("format", "yyyy-MM-dd HH:mm:ss");
////        env.put("source", "2017-09-02 12:23:22");
////
////        Date date = (Date) AviatorEvaluator.execute("Str2Date(source,format) ", env);
////        System.out.println("Date2Str:+++" + AviatorEvaluator.execute("Date2Str(date,format)", env));
////        System.out.println("Str2Date:+++" + date.toString());
//		env.put("v", 123.32);
//        Object changeStr = AviatorEvaluator.execute("Str(v)", env);
//
//        Object changeLong = AviatorEvaluator.execute("Long(v)", env);
//
//        env.put("s","叁拾贰万壹仟贰佰贰拾贰元壹角贰分");
//        String chinese2Double = (String )AviatorEvaluator.execute("Chinese2Double(s)",env);
//
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("5", "e");
//        map.put("6", "f");
//        map.put("1", "a");
//        map.put("2", "b");
//        map.put("3", "c");
//        map.put("4", "d");
//
//
//        String[] strArray = new String[]{"1", "2", "5", "6", "3", "9"};
//
//
//        List<Double> list = new ArrayList<Double>();
//        List<Integer> reduceList = new ArrayList<Integer>();
//
//        list.add(4d);
//        list.add(-1d);
//		list.add(-1d);
//		list.add(-2d);
//        list.add(3d);
//        list.add(-5.2);
//        list.add(9d);
//
//
//
//
//        env.put("seq", list);
//
//        ///Object sum  = AviatorEvaluator.execute("Sum(seq)", env);
//        env.put("element", "1");
//
//        Object strArraySorted = AviatorEvaluator.execute("Sort(seq)", env);
//        env.put("seq", list);
//        env.put("init", 0);
//        Object a = AviatorEvaluator.execute("Sort(seq)", env);
//
////		Object reduce = AviatorEvaluator.execute("reduce(seq,,init)",env);
//
//        System.out.println("Str:+++" + AviatorEvaluator.execute("Str(v)", env));
//        System.out.println("Long:+++" + changeLong);
//        System.out.println("Double:+++" + AviatorEvaluator.execute("Double(v)", env));
//        env.put("list", list);
//        System.out.println("Avg:+++" + AviatorEvaluator.execute("Avg(list)", env));
//        env.put("seq", strArray);
//        env.put("element", 1);
//
//        System.out.println("Include:+++" + AviatorEvaluator.execute("Include(seq,element)", env));
//
//		Object q = AviatorEvaluator.execute("First(seq)", env);
//
//        Object o1 = AviatorEvaluator.execute("Last(seq)", env);
//
//        System.out.println("Include:+++" + AviatorEvaluator.execute("Include(seq,element)", env));
//
//        String a2 = "1";
//
//		String[] strArray1 = new String[]{"9", "1", "2", "21", "1", "2", "0"};
//		env.put("seq", strArray1);
//
//        env.put("func", AviatorEvaluator.getFunction("string.length"));
//        Object o2 = AviatorEvaluator.execute("Map(seq,func)", env);
//
//
//        Object o3 = AviatorEvaluator.execute("Min(seq)", env);
//        Object o4 = AviatorEvaluator.execute("Max(seq)", env);
//		env.put("seq", list);
//		Object o10 = AviatorEvaluator.execute("Avg(seq)", env);
//
//        System.out.println("Include:+++" + AviatorEvaluator.execute("Include(seq,element)", env));
//        System.out.println("Min:+++" + AviatorEvaluator.execute("Min(seq)", env));
//
//        System.out.println("Include:+++" + AviatorEvaluator.execute("Include(seq,element)", env));
//		env.put("value", "9");
//
//		//Object o5 = AviatorEvaluator.execute("IsEq(value)");
//
//
//        System.out.println("Include:+++" + AviatorEvaluator.execute("Include(seq,element)", env));
//
//
//		//env.put("predicate", AviatorEvaluator.execute("IsEq(value)",env));
//		//Object o8 = AviatorEvaluator.execute("Filter(seq,predicate)", env);
//		//System.out.println("Include:+++" + AviatorEvaluator.execute("Filter(seq,predicate)", env));
//		//Object o6 = AviatorEvaluator.execute("Distinct(seq)", env);
//		Long[] strArray2 = new Long[]{1l, 2l, 2l, 1l, 2l, 0l, 0l};
//		env.put("seq", strArray2);
//		env.put("func",AviatorEvaluator.getFunction("-sub"));
//		env.put("init",0);
//		Object o9 = AviatorEvaluator.execute("Reduce(seq,func,init)", env);
//
//		Object o7 = AviatorEvaluator.execute("Distinct(seq)", env);
//
//		env.put("salt","6499875BAF404E5EB95B020F0A4B5F99");
//		env.put("password","202cb962ac59075b964b07152d234b70");
//		Object dd=  AviatorEvaluator.
//				execute("Password('6499875BAF404E5EB95B020F0A4B5F99','202cb962ac59075b964b07152d234b70')");
//		System.out.println(dd);
//
//		Object test = AviatorEvaluator.
//				execute("StartWith(Guid(),'0102')",env);
//
//		Object test1 = AviatorEvaluator.
//				execute("Contain(Guid(),'0102')",env);
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//		Map<String, Object> map1 = new HashMap<String, Object>();
//		map1.put("name", "p");
//		map1.put("cj", "5");
//		Map<String, Object> map2 = new HashMap<String, Object>();
//		map2.put("name", "h");
//		map2.put("cj", "12");
//		Map<String, Object> map3 = new HashMap<String, Object>();
//		map3.put("name", "f");
//		map3.put("cj", "31");
//		list.add(map1);
//		list.add(map3);
//		list.add(map2);
//		//排序前
//		for (Map<String, Object> map : list) {
//			System.out.println(map.get("cj"));
//		}
//		Collections.sort(list, new Comparator<Map<String, Object>>() {
//			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
//				Integer name1 = Integer.valueOf(o1.get("cj").toString()) ;//name1是从你list里面拿出来的一个
//				Integer name2 = Integer.valueOf(o2.get("cj").toString()) ; //name1是从你list里面拿出来的第二个name
//				return name2.compareTo(name1);
//			}
//		});
//		//排序后
//		System.out.println("-------------------");
//		for (Map<String, Object> map : list) {
//			System.out.println(map.get("cj"));
//		}

		env.put("email","sadfsdf'");
		System.out.println(AviatorEvaluator.execute("\"a\'b\"=~/^.*'.*$/", env));

	}
}
