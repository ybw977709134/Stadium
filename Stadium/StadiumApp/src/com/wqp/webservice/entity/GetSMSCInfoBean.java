package com.wqp.webservice.entity;

import java.io.Serializable;
import java.util.Date;

/** SMSCInfo���������*/
public class GetSMSCInfoBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer smscid;//����
	private String theme;//����
	private String contents;//����
	private Integer userid;//������ID;��Ա�����ID
	private Date createtime;//����ʱ��
	private String receiveid;//�ռ���ID
	private String receivename;//�ռ����û���
	
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
