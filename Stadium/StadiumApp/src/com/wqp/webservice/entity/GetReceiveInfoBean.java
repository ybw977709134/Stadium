package com.wqp.webservice.entity;

import java.io.Serializable;
import java.util.Date;

/** ReceiveInfo—收件箱表*/
public class GetReceiveInfoBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer receiveid;//主键
	private String theme;//主题
	private String contents;//内容
	private String receid;//收件人ID;会员表外键ID
	private Integer userid;//发送人ID;会员表外键ID
	private Date createtime;//收件时间
	private String state;//已读未读
	
	public GetReceiveInfoBean(){} 
	
	public Integer getReceiveid() {
		return receiveid;
	}
	public void setReceiveid(Integer receiveid) {
		this.receiveid = receiveid;
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
	public String getReceid() {
		return receid;
	}
	public void setReceid(String receid) {
		this.receid = receid;
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
}
