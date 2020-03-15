package com.testing.mysql;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MysqlUtils {
	// 声明Conncetion对象为私有成员变量，在类中可以通过方法调用，但是在类外不能使用。
	private Connection myConnector;

	// 建立和数据库之间的连接
	public void CreateConnection() {
		try {
			// 创建properties实例化对象
			Properties prop = new Properties();
			// 通过load方法加载properties文件。注意/别丢了。
			prop.load(this.getClass().getResourceAsStream("/inter.properties"));
			String dbUrl = prop.getProperty("DBURL");
			String dbUser = prop.getProperty("DBUSER");
			String dbPwd = prop.getProperty("DBPWD");
			// 加载驱动
			Class.forName("com.mysql.jdbc.Driver");
			// 通过url、用户名、密码建立数据库连接
			myConnector = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
			// 设置超时时间
			DriverManager.setLoginTimeout(10);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 方法完成登录校验，逻辑为如果能够通过输入的用户名和密码查询到数据，则登录成功
	 * 
	 * @param name
	 * @param pwd
	 * @return
	 */
	public boolean Login(String name, String pwd) {
		String sql = "SELECT * FROM userinfo where username='" + name + "' AND password='" + pwd + "';";
		System.out.println("拼接完的sql语句："+sql);
		// 声明statement对象，通过这个对象查询数据库
		Statement sm=null;
		// 查询结果resultset对象
		ResultSet rs = null;
		try {
			// 通过数据库连接实例化statement对象
			sm = myConnector.createStatement();
			// 执行查询
			rs = sm.executeQuery(sql);
			// rs!=null说明sql语句执行查找成功，有内容返回，rs.next()代表着集合当中还有下一个元素（一行的数据），并且读取该行的值。
			// 由于只调用了一次rs.next()相当于将查到的第一行数据，以键值对的形式存储到map中，并且输出。
			if (rs != null && rs.next()) {
				// 元组数据代表数据库查询结果中的表头。
				ResultSetMetaData rsmd = rs.getMetaData();
				// 声明一个map来存储一行中的内容
				HashMap<String, String> map = new HashMap<String, String>();
				// 注意sql中的列从1开始，遍历一行数据中的每列内容，并以键值对形式存储到map中去
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					// 从第一列开始遍历一行数据中的每一列，将对应的键值对存储到map当中
					map.put(rsmd.getColumnName(i), rs.getString(i));
				}
				System.out.println(map);
				// 关闭statement对象和查询结果集合对象，释放资源
				sm.close();
				rs.close();
				// 如果查询结果不为空，就返回登录成功
				return true;
			}
			// 如果查询结果为空，也要关闭对象释放资源
			sm.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//不管是运行了try当中的语句还是报错进入了catch中的语句，最后必然执行finally语句块中的内容。
		//如果try或者catch中进行了return的操作，那么finally语句依然会在return之前先运行完成。
		//
		finally {
			//在finally中关闭mysql的连接
//			try {
//				myConnector.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			try {
				//在finally中关闭sm和rs对象，释放资源，
				//但是由于本身sm和rs的实例化过程就在try当中，如果失败，则这两个对象根本就没有创建，不需要清理
				sm.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// try建立查询失败或者查询结果为空都会执行到这里，返回false
		return false;
	}

	/**
	 * 方法完成查询结果的获取
	 * 
	 * @param sql 输入查询语句
	 * @return 以List<Map<String,String>>格式返回所有查询结果
	 */
	public List<Map<String, String>> getSqlData(String sql) {
		// 声明statement对象，通过这个对象查询数据库
		Statement sm;
		// 保存结果集
		ResultSet rs = null;
		// 创建一个list用于存储每一行数据库读取出来的map
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		try {
			// 通过数据库连接实例化statement对象
			sm = myConnector.createStatement();
			// 执行查询
			rs = sm.executeQuery(sql);
			// 元组数据代表数据库查询结果中的一行。
			ResultSetMetaData rsmd = rs.getMetaData();
			// rs!=null说明sql语句执行查找成功，有内容返回，rs.next()代表着集合当中还有下一个元素（一行的数据），并且读取该行的值。
			// 外层循环while，遍历每一行的数据
			while (rs != null && rs.next()) {
				// 实例化map，存储当前循环的这一行的所有数据，以列，值的键值对形式进行存储。
				HashMap<String, String> map = new HashMap<String, String>();
				// 注意sql中的列从1开始，遍历一行数据中的每列内容，并以键值对形式存储到map中去
				// 内层循环，基于每一行，读取每一个列单元的数据
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					// 从第一列开始遍历一行数据中的每一列，将对应的键值对存储到map当中
					map.put(rsmd.getColumnName(i), rs.getString(i));
				}
				// 把每一行的数据存储的map放到一个list当中去
				dataList.add(map);
				System.out.println(map.toString());

			}
			// 完成查询之后，关闭查询和结果集，释放资源
			System.out.println("所有的数据库中的元素：" + dataList);
			sm.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dataList;
	}

	/**
	 * 通过存储过程验证登录结果的登录方法，如果能够查询到结果，则说明登录成功 使用存储过程进行验证时，sql语句不再重新编译，可以防止sql注入
	 * 
	 * @param 登录用户名name
	 * @param 登录密码password
	 * @return 返回登录是否成功的布尔值，成功为true，失败为false
	 */
	public boolean PLogin(String name, String pwd) {
		try {
			// 创建调用存储过程的对象，参数用？号代替，不要直接传递参数
			CallableStatement cm = myConnector.prepareCall("{call login(?,?)}");
			// 通过set方法传递存储过程用到的参数，1和2代表第几个参数，name和pwd代表参数值
			cm.setString(1, name);
			// cm.setInt(1, 1);
			cm.setString(2, pwd);
			System.out.println(cm);
			// 获取查询结果
			ResultSet rs = cm.executeQuery();
			System.out.println(rs);
			// 处理查询结果，与Login之前的方法一致，不再重复添加注释了
			if (rs != null && rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				HashMap<String, String> map = new HashMap<String, String>();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					map.put(rsmd.getColumnName(i), rs.getString(i));
				}
				System.out.println(map.toString());
				cm.close();
				rs.close();
				return true;
			}
			cm.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			 e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 获取用户信息的方法
	 * 
	 * @param name登录用户名
	 * @return 返回以map形式存储的查询结果
	 */
	public Map<String, String> getUserInfo(String name) {
		String sql = "SELECT * FROM userinfo where username='" + name + "';";
		System.out.println("拼接完的sql语句是"+sql);
		// 保存查询结果的集合
		ResultSet rs = null;
		// 声明statement对象，通过这个对象查询数据库
		Statement sm;
		try {
			// 通过数据库连接实例化statement对象
			sm = myConnector.createStatement();
			// 执行查询
			rs = sm.executeQuery(sql);
			// 创建map存储表中信息
			Map<String, String> map = new HashMap<String, String>();
			//设置读取结果的循环控制变量，代表获取的数据的行数
			/* rs!=null说明sql语句执行查找成功，有内容返回。
			 * rs.next()代表着集合当中还有下一个元素（一行的数据），并且读取该行的值。
			 * 如果sql查询不止一条语句，则可以用while循环取这些值
			 */
			if (rs != null && rs.next()) {
				// 元组数据代表数据库查询结果中的一行,通过rsmd来获取数据的列数
				ResultSetMetaData rsmd = rs.getMetaData();
				// 注意sql中的列从1开始，遍历一行数据中的每列内容，并以键值对形式存储到map中去
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					//展示的信息去除密码和用户名
					if (!(rsmd.getColumnName(i).equals("password") || rsmd.getColumnName(i).equals("username")))
						// 将每一列的名称和数据作为键值对存放到map当中，将行数拼接到键里
						map.put(rsmd.getColumnName(i), rs.getString(i));
				}
				System.out.println(map.toString());
			}
			// 关闭statement对象和查询结果集合对象，释放资源
			sm.close();
			rs.close();
			return map;
		} catch (SQLException e) {
		}
		return null;
	}
}
