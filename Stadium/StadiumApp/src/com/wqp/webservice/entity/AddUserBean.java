package com.wqp.webservice.entity;

import java.io.Serializable;

/** ������������һ�����û���Ϣ*/
public class AddUserBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private String username;//�û�����
	private String password;//�û�����
	private String name;//�û�����
	private String nickname;//�ǳ�
	private String phone;//�ֻ�����
	
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
