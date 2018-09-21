package com.wqp.webservice.entity;

import java.io.Serializable;

/** SportInfo-���*/
public class GetSportBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer sportid;//id����
	private String sportname;//name�˶�����
	private boolean isrec;//�Ƿ��Ƽ�
	
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
