package com.testing.mysql;

import java.util.List;
import java.util.Map;

public class MysqlUtilTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MysqlUtils mysql=new MysqlUtils();
		mysql.CreateConnection();
	  List<Map<String, String>> list=  mysql.getSqlData("SELECT * FROM userinfo");
	  System.out.println("第一条数据的用户名是"+list.get(0).get("username"));;
	  
	}

}
