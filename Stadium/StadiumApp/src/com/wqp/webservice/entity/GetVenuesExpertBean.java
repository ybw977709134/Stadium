package com.wqp.webservice.entity;

import java.io.Serializable;

public class GetVenuesExpertBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private int Number;//数字编号
	private int ExpertID;//专家ID
	private String VenuesName;//场馆名称
	private int Creater;//创建人
	private String Sex;//性别
	private String ZhuanJiaType;//专家类型
	private String NickName;//昵称
	private String Hobby;//专家的运动项目ID的集合，比如足球、篮球等
	
	
	
	public int getNumber() {
		return Number;
	}
	public void setNumber(int number) {
		Number = number;
	}
	public int getExpertID() {
		return ExpertID;
	}
	public void setExpertID(int expertID) {
		ExpertID = expertID;
	}
	public String getVenuesName() {
		return VenuesName;
	}
	public void setVenuesName(String venuesName) {
		VenuesName = venuesName;
	}
	public int getCreater() {
		return Creater;
	}
	public void setCreater(int creater) {
		Creater = creater;
	}
	public String getSex() {
		return Sex;
	}
	public void setSex(String sex) {
		Sex = sex;
	}
	public String getZhuanJiaType() {
		return ZhuanJiaType;
	}
	public void setZhuanJiaType(String zhuanJiaType) {
		ZhuanJiaType = zhuanJiaType;
	}
	public String getNickName() {
		return NickName;
	}
	public void setNickName(String nickName) {
		NickName = nickName;
	}
	public String getHobby() {
		return Hobby;
	}
	public void setHobby(String hobby) {
		Hobby = hobby;
	}
	
	
}
