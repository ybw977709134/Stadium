package com.wqp.webservice.entity;

import java.io.Serializable; 

/** Comment-������*/
public class GetCommentBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer commentid;//����
	private Integer userid;//������ID
	private String con;//��������
	private Integer keyid;//��������ID
	private String keytype;//������������
	private String createtime;//����ʱ�� 
	
	
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
