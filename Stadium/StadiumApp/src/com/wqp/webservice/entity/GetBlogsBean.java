package com.wqp.webservice.entity;

import java.io.Serializable;
import java.util.Date;

/** Blogs-���㲩��*/
public class GetBlogsBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer blogsid;//����
	private String blogscon;//��������
	private Date createtime;//����ʱ��
	private Integer createuser;//������
	
	public GetBlogsBean(){}
	
	public Integer getBlogsid() {
		return blogsid;
	}
	public void setBlogsid(Integer blogsid) {
		this.blogsid = blogsid;
	}
	public String getBlogscon() {
		return blogscon;
	}
	public void setBlogscon(String blogscon) {
		this.blogscon = blogscon;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Integer getCreateuser() {
		return createuser;
	}
	public void setCreateuser(Integer createuser) {
		this.createuser = createuser;
	}
	
}
