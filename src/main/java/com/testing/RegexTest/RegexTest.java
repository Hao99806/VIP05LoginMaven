package com.testing.RegexTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String ipresult="/**/jQuery110204998942639895132_1579524470456({\"status\":\"0\",\"t\":\"1579524478461\",\"set_cache_time\":\"\",\"data\":[{\"location\":\"美国\",\"titlecont\":\"IP地址查询\",\"origip\":\"3.3.3.3\",\"origipquery\":\"3.3.3.3\",\"showlamp\":\"1\",\"showLikeShare\":1,\"shareImage\":1,\"ExtendedLocation\":\"\",\"OriginQuery\":\"3.3.3.3\",\"tplt\":\"ip\",\"resourceid\":\"6006\",\"fetchkey\":\"3.3.3.3\",\"appinfo\":\"\",\"role_id\":0,\"disp_type\":0}]});";
		//使用pattern类完成正则表达式的编译
		//正则表达式，表达字符串中的json内容的时候，特点是{.*]}中的内容
		//{(.*)?]}中的(.*)?是最常用的查找匹配方式。
		Pattern jsonPattern=Pattern.compile("\\{(.*)?\\]\\}");
		//基于pattern创建一个matcher，用于在字符串中匹配内容。
		Matcher jsonMatcher=jsonPattern.matcher(ipresult);
		//通过find方法，完成对于字符串的匹配
		jsonMatcher.find();
		//group(0)包含了{(.*)?]}中，()括号以外的内容
		System.out.println(jsonMatcher.group(0));
		//group(1)只包含{(.*)?]}中()括号以外的内容，也就是去掉了{和]}
		System.out.println(jsonMatcher.group(1));
		
		//表示*或者#或者空格类字符
		Pattern specialWord=Pattern.compile("\\*|\\#|\\s");
		Matcher specialMatcher=specialWord.matcher(ipresult);
		//是否有这几个特殊字符。
		System.out.println(specialMatcher.find());
		
		//表示出了大小写字母和数字以及_@-以外的字符。
		Pattern normalWord=Pattern.compile("[^a-zA-Z0-9_@\\-]");
		Matcher normalMatcher=normalWord.matcher(ipresult);
		//是否有出了指定字符以外的特殊字符
		System.out.println(normalMatcher.find());
		
		
	}

}
