package com.wqp.webservice.entity;

import java.io.Serializable;
import java.util.Date;

/** UserManage--����Ա��*/
public class GetUserManageBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer userid;//����
	private String username;//�û���
	private String password;//����
	private String type;//�û�����
	private String name;//����
	private String phone;//�ֻ���
	private Date createtime;//���ʱ��
	private String state;//����״̬
	private String competence;//Ȩ�޹���
	
	public GetUserManageBean(){}
	
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
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
	public String getCompetence() {
		return competence;
	}
	public void setCompetence(String competence) {
		this.competence = competence;
	}
	
	
}
