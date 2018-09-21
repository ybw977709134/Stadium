package com.wqp.webservice.entity;

import java.io.Serializable;

/** 保存用户登录成功的之后的信息*/
public class GetLoginBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private boolean UserExist;//用户是否存在
	private int UserID;//用户的ID
	private String NickName;//用户的昵称
	private String Name;//用户真实姓名
	private String UserName;//用户名
	private String sex;//用户的姓别
	private String Age;//用户的年龄
	private String Picture;//用户头像图片路径
	private String UserType;//用户类型
	
	public GetLoginBean(){}
	
	public String getPicture() {
		return Picture;
	} 

	public void setPicture(String picture) {
		Picture = picture;
	} 
	
	public String getUserType() {
		return UserType;
	}

	public void setUserType(String userType) {
		UserType = userType;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public boolean isUserExist() {
		return UserExist;
	}
	public void setUserExist(boolean userExist) {
		UserExist = userExist;
	}
	public int getUserID() {
		return UserID;
	}
	public void setUserID(int userID) {
		UserID = userID;
	}

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		NickName = nickName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
		return Age;
	}

	public void setAge(String age) {
		Age = age;
	} 


	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	@Override
	public String toString() {
		return "登录的用户信息为 [UserExist=" + UserExist + ", UserID=" + UserID
				+ ", NickName=" + NickName + ", Name=" + Name + ", UserName="
				+ UserName + ", sex=" + sex + ", Age=" + Age + ", Picture="
				+ Picture + ", UserType=" + UserType + "]";
	} 
	
	
}
