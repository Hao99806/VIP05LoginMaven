/**
 * VIP05的登录页面中用于实现异步请求的ajax脚本
 */

function AjaxLogin() {

	$.ajax(
	// 通过json，也就是javascript对象来进行操作。
	{
		// 使用ajax向服务器发起请求
		// 接口请求四大要素 url http方法 请求体 请求头，完成向服务器的请求之后
		// ajax会自动获取到服务器的返回，作为result来进行使用
		// url信息，用于指定ajax提交请求的地址
		url : "./Login",
		// http方法
		type : "post",
		// 请求头，设置预期的返回数据类型
		dataType : "json",
		// 请求体，提交的参数，找到form表单将里面的信息序列化成为一个ajax可以提交的数据
		data : $("#formData").serialize(),

		// 返回信息的处理，result是ajax规定的获取到的返回
		// 如果请求成功（跟业务逻辑无关，只跟http协议是否获取到预期类型的返回有关），那么调用success键中的函数。
		success : function(result) {
			// alert("接口请求调用成功"+result["msg"]);
			$("#msg")[0].innerText = "接口返回信息是：" + result["msg"];
			if(result["statusCode"]=="1"){
				window.location.href="user.html";
			}
		},
		// 自动判断接口请求如果是失败的，那么调用error键中的函数
		error : function(result) {
			alert("接口请求调用失败，请检查ajax代码和服务器源码");
		}

	})
}

/**
 * getUser函数，通过user.html中body的onload事件响应异步提交
 * 在访问user.html时就调用Userinfo接口获取用户信息并且写到对应的元素中。
 */
function getUser() {
	// 定义一个存放url的变量，指定请求的接口的地址
	var AjaxURL = "./GetUserInfo";
	$.ajax({
		// 方法用post
		type : "post",
		// 返回和请求的参数类型传递方式。
		dataType : "json",
		// 请求的接口地址
		url : AjaxURL,
		// 接口执行的返回，当接口调用成功时，执行success中的方法
		success : function(result) {
			// 将返回结果解析出来的对应内容填写到对应的元素中
			document.getElementById("userid").innerHTML = result["id"];
			document.getElementById("nickname").innerHTML = result["nickname"];
			document.getElementById("describe").innerHTML = result["describe"];
		},
		// 接口调用出错时，执行该方法
		error : function() {
			alert("调用UserInfo接口出错，请检查。");
		}
	});
}

/**
 * logout()方法在user.html中的注销按钮通过onclick事件响应 调用Logout接口，完成注销的操作。
 */
function logout() {
	// 定义一个存放url的变量，指定请求的接口的地址
	var AjaxURL = "./Logout";
	$.ajax({
		// 方法用post
		type : "post",
		// 返回和请求的参数类型传递方式。
		dataType : "json",
		// 请求的接口地址
		url : AjaxURL,
		// 接口执行的返回，当接口调用成功时，执行success中的方法
		success : function(result) {
			alert(result["msg"]);
			window.location.href = "index.html";
		},
		// 接口调用出错时，执行该方法
		error : function() {
			alert("调用Logout接口出错，请检查。");
		}
	});
}