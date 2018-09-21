package com.wqp.webservice.entity;

import java.io.Serializable;

/** �����û���¼�ɹ���֮�����Ϣ*/
public class GetLoginBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private boolean UserExist;//�û��Ƿ����
	private int UserID;//�û���ID
	private String NickName;//�û����ǳ�
	private String Name;//�û���ʵ����
	private String UserName;//�û���
	private String sex;//�û����ձ�
	private String Age;//�û�������
	private String Picture;//�û�ͷ��ͼƬ·��
	private String UserType;//�û�����
	
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
		return "��¼���û���ϢΪ [UserExist=" + UserExist + ", UserID=" + UserID
				+ ", NickName=" + NickName + ", Name=" + Name + ", UserName="
				+ UserName + ", sex=" + sex + ", Age=" + Age + ", Picture="
				+ Picture + ", UserType=" + UserType + "]";
	} 
	
	
}
