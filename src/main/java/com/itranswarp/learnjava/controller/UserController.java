package com.itranswarp.learnjava.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itranswarp.learnjava.bean.SignInBean;
import com.itranswarp.learnjava.bean.User;
import com.itranswarp.learnjava.framework.GetMapping;
import com.itranswarp.learnjava.framework.ModelAndView;
import com.itranswarp.learnjava.framework.PostMapping;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.routines.EmailValidator;

public class UserController {

	//数据库
	String JDBC_URL = "jdbc:mysql://localhost:3306/test";
	String JDBC_USER = "testuser";
	String JDBC_PASSWORD = "password";

	private User JDBCData (String account, String password, String sql){

		{
			//获取连接
			try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
				// TODO: 访问数据库

				try (PreparedStatement prestmt = conn.prepareStatement(sql)) {
					prestmt.setObject(1,account);
					prestmt.setObject(2,password);
					try (ResultSet rs = prestmt.executeQuery()){
						if (rs.next()){
							User user = new User(rs.getLong("account_id"),rs.getString("name"), rs.getString("gender"), rs.getString("phone"), rs.getString("email"), rs.getDate("birth"), rs.getString("password"));
							return user;
						}
					}catch (Exception e){
						e.printStackTrace();
					}

				}catch (Exception e){
					e.printStackTrace();
				}
				//关闭连接
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

//	private Map<String, User> userDatabase = new HashMap<>() {
//		{
//			List<User> users = List.of( //
//					new User("bob@example.com", "bob123", "Bob", "This is bob."),
//					new User("tom@example.com", "tomcat", "Tom", "This is tom."));
//			users.forEach(user -> {
//				put(user.email, user);
//			});
//		}
//	};

	//Get 请求
	@GetMapping("/register")
    public ModelAndView register(){
	    return new ModelAndView("/Register.html");
    }

	@GetMapping("/signin")
	public ModelAndView signin() {
		//跳转到signin页面
		return new ModelAndView("/Signin.html");
	}

	//Post 请求
	@PostMapping("/signin")
	public ModelAndView doSignin(SignInBean bean, HttpServletResponse response, HttpSession session)
			throws IOException {
        //在数据库中比对账户和密码，创建bean
        //Check the email address
        EmailValidator validator = EmailValidator.getInstance();
        String SQL;
        if (validator.isValid(bean.name)){
            SQL = "SELECT * FROM accounts WHERE email=? AND password=?";
        }
        else{
            SQL = "SELECT * FROM accounts WHERE phone=? AND password=?";
        }
		User user = JDBCData(bean.name, bean.password, SQL);
		if (user!=null){
		    //如果比对成功，即登录成功，创建seesion
			session.setAttribute("user",user);
			//通过servlet返回内容，由于是js代码，所以用json格式
			response.setContentType("application/json");
			PrintWriter pw = response.getWriter();
			pw.write("{\"result\":true}");
			pw.flush();
		}
		else{
			response.setContentType("application/json");
			PrintWriter pw = response.getWriter();
			pw.write("{\"error\":\"未找到用户名或密码错误\"}");
			pw.flush();
		}
		return null;
	}

	@GetMapping("/signout")
	public ModelAndView signout(HttpSession session) {
	    //从session中剔除user信息
		session.removeAttribute("user");
		//重定向回首页
		return new ModelAndView("redirect:/");
	}

	@GetMapping("/user/profile")
	public ModelAndView profile(HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return new ModelAndView("redirect:/signin");
		}
		return new ModelAndView("/Account.html", "user", user);
	}
}
