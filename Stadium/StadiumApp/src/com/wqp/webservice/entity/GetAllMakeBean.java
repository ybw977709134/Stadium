package com.wqp.webservice.entity;

public class GetAllMakeBean {
	private int ProjectID;//运动类型ID,项目ID
	private int VenuesID;//场馆ID
	private String ProjectName;//运动项目名称（运动类型名称）
	private String MakePrice;//预约价格(支付宝或财付通需要支付的价格)
	private String MakeDes;//预约的说明信息
	
	
	public int getProjectID() {
		return ProjectID;
	}
	public void setProjectID(int projectID) {
		ProjectID = projectID;
	}
	public int getVenuesID() {
		return VenuesID;
	}
	public void setVenuesID(int venuesID) {
		VenuesID = venuesID;
	}
	public String getProjectName() {
		return ProjectName;
	}
	public void setProjectName(String projectName) {
		ProjectName = projectName;
	}
	public String getMakePrice() {
		return MakePrice;
	}
	public void setMakePrice(String makePrice) {
		MakePrice = makePrice;
	}
	public String getMakeDes() {
		return MakeDes;
	}
	public void setMakeDes(String makeDes) {
		MakeDes = makeDes;
	}
	
	

}
