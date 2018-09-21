package com.wqp.webservice.entity;

import java.io.Serializable;

/** BlogRoll—友情链接*/
public class GetBlogRollBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer blogrollid;//主键
	private String title;//友情链接名称
	private Integer linktype;//类型;图片or文字
	private String imgurl;//图片地址
	private String linkurl;//友情链接地址
	
	public GetBlogRollBean(){}
	
	public Integer getBlogrollid() {
		return blogrollid;
	}
	public void setBlogrollid(Integer blogrollid) {
		this.blogrollid = blogrollid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getLinktype() {
		return linktype;
	}
	public void setLinktype(Integer linktype) {
		this.linktype = linktype;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getLinkurl() {
		return linkurl;
	}
	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}
	
	
}
