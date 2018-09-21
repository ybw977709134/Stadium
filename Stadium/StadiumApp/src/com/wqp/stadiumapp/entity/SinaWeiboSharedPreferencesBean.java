package com.wqp.stadiumapp.entity;

import java.io.Serializable;

public class SinaWeiboSharedPreferencesBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private String date;//ÈÕÆÚ
	private String access_token;
	private String expires_in;
	private String uid;
	
	
	public SinaWeiboSharedPreferencesBean() {
		super();
	}
	public SinaWeiboSharedPreferencesBean(String date, String access_token,
			String expires_in, String uid) {
		super();
		this.date = date;
		this.access_token = access_token;
		this.expires_in = expires_in;
		this.uid = uid;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	

}
