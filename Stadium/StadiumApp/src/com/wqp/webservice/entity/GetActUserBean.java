package com.wqp.webservice.entity;

import java.util.Date;

/** ActUser-活动用户表*/
public class GetActUserBean {
	private Integer auid;//主键
	private Integer actid;//活动ID;外键 活动表ID
	private Integer userid;//用户ID;用户表主键
	private Date createtime;//报名时间
	private String ispass;//报名是否通过
	private  Integer usertype;//用户类型;1达人 2裁判 3 指导员
	
	public GetActUserBean(){}
	
	public Integer getAuid() {
		return auid;
	}
	public void setAuid(Integer auid) {
		this.auid = auid;
	}
	public Integer getActid() {
		return actid;
	}
	public void setActid(Integer actid) {
		this.actid = actid;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getIspass() {
		return ispass;
	}
	public void setIspass(String ispass) {
		this.ispass = ispass;
	}
	public Integer getUsertype() {
		return usertype;
	}
	public void setUsertype(Integer usertype) {
		this.usertype = usertype;
	}
	 
}
