package com.wqp.webservice.entity;

import java.io.Serializable; 

/** Comment-点评表*/
public class GetCommentBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer commentid;//主键
	private Integer userid;//点评人ID
	private String con;//点评内容
	private Integer keyid;//所属内容ID
	private String keytype;//所属内容类型
	private String createtime;//点评时间 
	
	
	public GetCommentBean() { }
	
	public Integer getCommentid() {
		return commentid;
	}
	public void setCommentid(Integer commentid) {
		this.commentid = commentid;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getCon() {
		return con;
	}
	public void setCon(String con) {
		this.con = con;
	}
	public Integer getKeyid() {
		return keyid;
	}
	public void setKeyid(Integer keyid) {
		this.keyid = keyid;
	}
	public String getKeytype() {
		return keytype;
	}
	public void setKeytype(String keytype) {
		this.keytype = keytype;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	
	
}
