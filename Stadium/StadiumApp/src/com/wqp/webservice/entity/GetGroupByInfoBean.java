package com.wqp.webservice.entity;

import java.io.Serializable;

/** GrouByInfo—好友分组表*/
public class GetGroupByInfoBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer groubyinfo;//主键
	private Integer userid;//用户ID;会员表外键ID
	private String groubyname;//分组名称
	
	public GetGroupByInfoBean(){}
	
	public Integer getGroubyinfo() {
		return groubyinfo;
	}
	public void setGroubyinfo(Integer groubyinfo) {
		this.groubyinfo = groubyinfo;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getGroubyname() {
		return groubyname;
	}
	public void setGroubyname(String groubyname) {
		this.groubyname = groubyname;
	} 
}
