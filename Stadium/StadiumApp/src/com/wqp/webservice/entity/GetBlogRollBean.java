package com.wqp.webservice.entity;

import java.io.Serializable;

/** BlogRoll����������*/
public class GetBlogRollBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer blogrollid;//����
	private String title;//������������
	private Integer linktype;//����;ͼƬor����
	private String imgurl;//ͼƬ��ַ
	private String linkurl;//�������ӵ�ַ
	
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
