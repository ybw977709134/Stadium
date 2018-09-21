package com.wqp.webservice.entity;

import java.io.Serializable;

public class AddReservationBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer SportType;//运动类型
	private String ActTitle;//活动标题
	private String VenuesName;//使用场馆
	private String VenuesCost;//场馆费用
	private String StartTime;//开始时间
	private String EndTime;//结束时间
	private String ApplyEndTime;//报名截止日期
	private int TakeCount;//参与人数活动上限
	private int RefereeCount;//裁判人数
	private int GuideCount;//指导员人数
	private String ActCon;//活动具体内容
	private String ActNotice;//活动公告
	private String CreateUser;//创建人
	
	public AddReservationBean(){}
	 
	public Integer getSportType() {
		return SportType;
	}
	public void setSportType(Integer sportType) {
		SportType = sportType;
	}
	public String getActTitle() {
		return ActTitle;
	}
	public void setActTitle(String actTitle) {
		ActTitle = actTitle;
	}
	public String getVenuesName() {
		return VenuesName;
	}
	public void setVenuesName(String venuesName) {
		VenuesName = venuesName;
	}
	public String getVenuesCost() {
		return VenuesCost;
	}
	public void setVenuesCost(String venuesCost) {
		VenuesCost = venuesCost;
	}
	public String getStartTime() {
		return StartTime;
	}
	public void setStartTime(String startTime) {
		StartTime = startTime;
	}
	public String getEndTime() {
		return EndTime;
	}
	public void setEndTime(String endTime) {
		EndTime = endTime;
	}
	public String getApplyEndTime() {
		return ApplyEndTime;
	}
	public void setApplyEndTime(String applyEndTime) {
		ApplyEndTime = applyEndTime;
	}
	public int getTakeCount() {
		return TakeCount;
	}
	public void setTakeCount(int takeCount) {
		TakeCount = takeCount;
	}
	public String getCreateUser() {
		return CreateUser;
	}
	public void setCreateUser(String createUser) {
		CreateUser = createUser;
	}

	public int getRefereeCount() {
		return RefereeCount;
	}

	public void setRefereeCount(int refereeCount) {
		RefereeCount = refereeCount;
	}

	public int getGuideCount() {
		return GuideCount;
	}

	public void setGuideCount(int guideCount) {
		GuideCount = guideCount;
	}

	public String getActCon() {
		return ActCon;
	}

	public void setActCon(String actCon) {
		ActCon = actCon;
	}

	public String getActNotice() {
		return ActNotice;
	}

	public void setActNotice(String actNotice) {
		ActNotice = actNotice;
	}
	
	
	
}
