package com.wqp.webservice.entity;

import java.io.Serializable;

public class Get5SecondUBean implements Serializable { 
	private static final long serialVersionUID = 1L;
	private int UserVenMesID;//消息标识列
	private int ProID;//运动项目名称
	private int UserID;//用户ID
	private String Con;//请求的文本内容
	private int VenuesIDs;//场馆ID(只存一个场馆ID)
	private int isVenuesID;//状态码
	private String CreateTime;//消息创建时间
	private float Price;//价格
	private String ProName;//运动项目名称
	private String MakeTime;//预约时间
	
	
	public Get5SecondUBean() { }
	
	public int getUserVenMesID() {
		return UserVenMesID;
	}
	public void setUserVenMesID(int userVenMesID) {
		UserVenMesID = userVenMesID;
	}
	public int getProID() {
		return ProID;
	}
	public void setProID(int proID) {
		ProID = proID;
	}
	public String getCon() {
		return Con;
	}
	public void setCon(String con) {
		Con = con;
	}
	public int getVenuesIDs() {
		return VenuesIDs;
	}
	public void setVenuesIDs(int venuesIDs) {
		VenuesIDs = venuesIDs;
	}
	public int getIsVenuesID() {
		return isVenuesID;
	}
	public void setIsVenuesID(int isVenuesID) {
		this.isVenuesID = isVenuesID;
	}
	public String getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	public float getPrice() {
		return Price;
	}
	public void setPrice(float price) {
		Price = price;
	}
	public String getProName() {
		return ProName;
	}
	public void setProName(String proName) {
		ProName = proName;
	}
	public String getMakeTime() {
		return MakeTime;
	}
	public void setMakeTime(String makeTime) {
		MakeTime = makeTime;
	}

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	} 
}
