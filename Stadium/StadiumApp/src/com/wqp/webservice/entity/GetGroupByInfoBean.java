package com.wqp.webservice.entity;

import java.io.Serializable;

/** GrouByInfo�����ѷ����*/
public class GetGroupByInfoBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer groubyinfo;//����
	private Integer userid;//�û�ID;��Ա�����ID
	private String groubyname;//��������
	
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
