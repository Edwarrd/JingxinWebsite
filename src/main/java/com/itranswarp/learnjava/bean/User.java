package com.itranswarp.learnjava.bean;

import java.sql.Date;

public class User {

	public long account_id;
	public String name;
	public String gender;
	public String mobile;
	public String email;
	public Date birth;

	public String password;

//	public String description;

	public User() {
	}

	public User(long account_id, String name,String gender, String mobile, String email, Date birth, String password) {
		this.account_id = account_id;
		this.name = name;
		this.gender = gender;
		this. mobile = mobile;
		this.email = email;
		this. birth = birth;
		this.password = password;
//		this.description = description;
	}
}
