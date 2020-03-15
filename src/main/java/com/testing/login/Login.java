package com.testing.login;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.testing.mysql.MysqlUtils;

/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response) get方法的参数从url中获取，拼接格式是
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
//		设置请求内容的编码为utf8
		request.setCharacterEncoding("utf-8");
//		设置返回的编码是utf8以及content-type为html格式，utf8编码
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
//		//指定从请求中获取User和pwd参数的值
//		String userValue=request.getParameter("User");
//		String pwdValue=request.getParameter("pwd");
//		//如果url中没有pwd参数，如user=roy，那么pwdValue的值是null，下面语句会报错，导致500状态码
//		//如果url中pwd参数为空字符 如：user=roy&pwd=，下面语句不会报错，因为pwdValue有内存地址，只是值为空
////		pwdValue.equals("roy");

		// 拼接修改response返回内容
		response.getWriter().append("{\"statusCode\":\"-1\",\"msg\":\"不接受get方法调用！\"}");

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
//		设置请求内容的编码为utf8
		request.setCharacterEncoding("utf-8");
//		设置返回的编码是utf8以及content-type为html格式，utf8编码
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		// 从form表单的请求里，获取User/pwd/爱吃的菜参数
		String userPost = request.getParameter("User");
		String pwdPost = request.getParameter("pwd");
		// 可以通过servlet的机制获取到sessionid
		String sessionId = request.getSession().getId();
		System.out.println("本次访问的sessionId是" + sessionId);
		//设置session的生命周期，有效时间是60秒
		request.getSession().setMaxInactiveInterval(60);
//		如果服务器出现错误，导致ajax无法收到返回，那么会导致ajax请求报错。
//		String notEx=request.getParameter("notEx");
//		notEx.equals("");
		// 最后的返回信息
		String loginMsg = "";
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        System.out.println("本次提交登录的时间是：  "+df.format(new Date()));// new Date()为获取当前系统时间

		// 第一层输入判断，判断输入是否为空，以及是否为空字符串。
		if ((userPost != null && pwdPost != null) && (userPost.length() > 0 && pwdPost.length() > 0)) {
			// 判断用户名密码长度为[3,16]
			if (userPost.length() > 2 && userPost.length() < 17 && pwdPost.length() > 2 && pwdPost.length() < 17) {
				// 表示除了大小写字母和数字以及_@-以外的字符都是特殊字符
				Pattern specialWord = Pattern.compile("[^a-zA-Z0-9_@\\-]");
				// 分别为用户名和密码创建matcher
				Matcher userMatcher = specialWord.matcher(userPost);
				Matcher pwdMatcher = specialWord.matcher(pwdPost);
				// 判断是否有特殊字符,找不到特殊字符，才进行下一步
				if (!userMatcher.find() && !pwdMatcher.find()) {
					// 验证用户是否已经登录
					// 由于登录之后会在session中保存自己的用户名信息，所以，如果不能获取到loginName的信息，才需要进行登录
					// 如果获取到的结果为空，则说明没有登录，去完成登录操作。
					if (request.getSession().getAttribute("loginName") == null) {
						// 调用数据库完成连接
						MysqlUtils mysql = new MysqlUtils();
						mysql.CreateConnection();
						boolean sqlResult = mysql.PLogin(userPost, pwdPost);
						// 用常量字符串作为方法调用主体进行equals判断，避免空指针异常。
						// 通过数据库类中的login方法来完成登录验证，逻辑是，如果用户名和密码组合能够查询到数据，就认为登录成功。
						if (sqlResult) {
							loginMsg = "{\"statusCode\":\"1\",\"msg\":\"恭喜您登录成功\"}";
							// 如果登录成功，在服务端的session记录本次登录的用户名
							request.getSession().setAttribute("loginName", userPost);
							//登录成功则发放COokie
							//添加一个Cookie,用同名的JSSESIONID记录cookie，使用servlet自己的cookie校验机制。
							//值就是在servlet访问时，获取到的sessionId
							Cookie ssIdCookie=new Cookie("JSESSIONID", sessionId);
							//设置cookie超时时间，有效时间是60秒
							ssIdCookie.setMaxAge(60);
							//返回cookie给客户端
//							response.addCookie(ssIdCookie);
						} else {
							loginMsg = "{\"statusCode\":\"0\",\"msg\":\"不好意思，登录失败了\"}";
						}
					} // 判断是否已经登录
						// 如果已经登录了，分两种情况
					else {
						if (request.getSession().getAttribute("loginName").equals(userPost)) {
							loginMsg = "{\"statusCode\":\"0\",\"msg\":\"不好意思，您已经登录过了\"}";
						} else {
							loginMsg = "{\"statusCode\":\"0\",\"msg\":\"不好意思，已经有别人登录过了\"}";
						}

					}

				} // 判断是否有特殊字符
				else {
					loginMsg = "{\"statusCode\":\"0\",\"msg\":\"用户名密码不能包含特殊字符\"}";
				}

			} // 判断用户名密码长度
			else {
				loginMsg = "{\"statusCode\":\"0\",\"msg\":\"用户名密码必须是3到16位\"}";
			}

		} // 为空判断
		else {
			loginMsg = "{\"statusCode\":\"0\",\"msg\":\"用户名密码不能为空\"}";
		}

		response.getWriter().append(loginMsg);

	}

}
