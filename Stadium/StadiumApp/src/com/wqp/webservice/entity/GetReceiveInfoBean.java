package com.wqp.webservice.entity;

import java.io.Serializable;
import java.util.Date;

/** ReceiveInfo���ռ����*/
public class GetReceiveInfoBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer receiveid;//����
	private String theme;//����
	private String contents;//����
	private String receid;//�ռ���ID;��Ա�����ID
	private Integer userid;//������ID;��Ա�����ID
	private Date createtime;//�ռ�ʱ��
	private String state;//�Ѷ�δ��
	
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
