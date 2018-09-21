package com.wqp.webservice.entity;

import java.io.Serializable;

public class GetUserMakeBean implements Serializable { 
	private static final long serialVersionUID = 1L;
	
	private int MakeInfoID;//预约信息ID
	private int VenuesID;//场馆ID
	private String VenuesName;//场馆名称
	private int ProductID;//运动项目ID
	private String State;//预约状态
	private int UserID;//预约用户ID
	private String MakeUserName;//预约人姓名
	private String MakeTime;//预约时间
	private String MakeCode;//验证码
	private String BankCode;//银行账号 //退订时才会用到
	private String OpenBand;//开户银行 //退订时才会用到
	private String BankUserName;//户名 //退订时才会用到
	private String PayType;//付款方式(支付宝，微信)
	private String ProjectName;//运动项目名称
	
	
	
	public String getVenuesName() {
		return VenuesName;
	}
	public void setVenuesName(String venuesName) {
		VenuesName = venuesName;
	}
	public int getMakeInfoID() {
		return MakeInfoID;
	}
	public void setMakeInfoID(int makeInfoID) {
		MakeInfoID = makeInfoID;
	}
	public int getVenuesID() {
		return VenuesID;
	}
	public void setVenuesID(int venuesID) {
		VenuesID = venuesID;
	}
	public int getProductID() {
		return ProductID;
	}
	public void setProductID(int productID) {
		ProductID = productID;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public int getUserID() {
		return UserID;
	}
	public void setUserID(int userID) {
		UserID = userID;
	}
	public String getMakeUserName() {
		return MakeUserName;
	}
	public void setMakeUserName(String makeUserName) {
		MakeUserName = makeUserName;
	}
	public String getMakeTime() {
		return MakeTime;
	}
	public void setMakeTime(String makeTime) {
		MakeTime = makeTime;
	}
	public String getMakeCode() {
		return MakeCode;
	}
	public void setMakeCode(String makeCode) {
		MakeCode = makeCode;
	}
	public String getBankCode() {
		return BankCode;
	}
	public void setBankCode(String bankCode) {
		BankCode = bankCode;
	}
	public String getOpenBand() {
		return OpenBand;
	}
	public void setOpenBand(String openBand) {
		OpenBand = openBand;
	}
	public String getBankUserName() {
		return BankUserName;
	}
	public void setBankUserName(String bankUserName) {
		BankUserName = bankUserName;
	}
	public String getPayType() {
		return PayType;
	}
	public void setPayType(String payType) {
		PayType = payType;
	}
	public String getProjectName() {
		return ProjectName;
	}
	public void setProjectName(String projectName) {
		ProjectName = projectName;
	}
	
	

}
