package com.wqp.webservice.entity;

import java.io.Serializable;
import java.util.Date;

/** SMSCInfo—发件箱表*/
public class GetSMSCInfoBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer smscid;//主键
	private String theme;//主题
	private String contents;//内容
	private Integer userid;//发送人ID;会员表外键ID
	private Date createtime;//发送时间
	private String receiveid;//收件人ID
	private String receivename;//收件人用户名
	
	public GetSMSCInfoBean(){}
	
	public Integer getSmscid() {
		return smscid;
	}
	public void setSmscid(Integer smscid) {
		this.smscid = smscid;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
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
	public String getReceiveid() {
		return receiveid;
	}
	public void setReceiveid(String receiveid) {
		this.receiveid = receiveid;
	}
	public String getReceivename() {
		return receivename;
	}
	public void setReceivename(String receivename) {
		this.receivename = receivename;
	}
	
}
