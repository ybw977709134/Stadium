package com.wqp.webservice.entity;

import java.io.Serializable;

/** SportInfo-活动表*/
public class GetSportBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer sportid;//id主键
	private String sportname;//name运动名称
	private boolean isrec;//是否推荐
	
	public GetSportBean(){}
	
	public GetSportBean(int sportid, String sportname, boolean isrec) {
		super();
		this.sportid = sportid;
		this.sportname = sportname;
		this.isrec = isrec;
	}
	public int getSportid() {
		return sportid;
	}
	public void setSportid(int sportid) {
		this.sportid = sportid;
	}
	public String getSportname() {
		return sportname;
	}
	public void setSportname(String sportname) {
		this.sportname = sportname;
	}
	public boolean isIsrec() {
		return isrec;
	}
	public void setIsrec(boolean isrec) {
		this.isrec = isrec;
	} 
}
