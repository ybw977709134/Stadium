package com.wqp.webservice.entity;

import java.io.Serializable;

/** 向服务器端添加一条新用户信息*/
public class AddUserBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private String username;//用户名称
	private String password;//用户密码
	private String name;//用户姓名
	private String nickname;//昵称
	private String phone;//手机号码
	
	public AddUserBean(){} 
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
 
  
	
}
