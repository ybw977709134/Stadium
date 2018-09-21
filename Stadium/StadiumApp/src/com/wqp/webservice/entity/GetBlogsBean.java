package com.wqp.webservice.entity;

import java.io.Serializable;
import java.util.Date;

/** Blogs-迷你博客*/
public class GetBlogsBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer blogsid;//主键
	private String blogscon;//博客内容
	private Date createtime;//发布时间
	private Integer createuser;//发布人
	
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
