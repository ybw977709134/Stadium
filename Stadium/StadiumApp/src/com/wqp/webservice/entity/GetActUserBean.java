package com.wqp.webservice.entity;

import java.util.Date;

/** ActUser-��û���*/
public class GetActUserBean {
	private Integer auid;//����
	private Integer actid;//�ID;��� ���ID
	private Integer userid;//�û�ID;�û�������
	private Date createtime;//����ʱ��
	private String ispass;//�����Ƿ�ͨ��
	private  Integer usertype;//�û�����;1���� 2���� 3 ָ��Ա
	
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
