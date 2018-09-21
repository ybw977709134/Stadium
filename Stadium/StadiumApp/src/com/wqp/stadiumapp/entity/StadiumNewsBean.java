package com.wqp.stadiumapp.entity;

import java.io.Serializable;

public class StadiumNewsBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer image;//Í¼Æ¬
	private String title;//±êÌâ
	private String content;//ÄÚÈİ
	
	
	public StadiumNewsBean() {
		super();
	}
	public StadiumNewsBean(Integer image, String title, String content) {
		super();
		this.image = image;
		this.title = title;
		this.content = content;
	}
	public Integer getImage() {
		return image;
	}
	public void setImage(Integer image) {
		this.image = image;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	

}
