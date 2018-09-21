package com.wqp.stadiumapp.entity;

import java.io.Serializable;

public class FriendApplyBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer image;//头像
	private String name;//名称
	private String content;//内容
	private Integer state;//状态，同意或者已同意
	
	
	public FriendApplyBean() {
		super();
	}
	public FriendApplyBean(Integer image, String name, String content, Integer state) {
		super();
		this.image = image;
		this.name = name;
		this.content = content;
		this.state = state;
	}
	public Integer getImage() {
		return image;
	}
	public void setImage(Integer image) {
		this.image = image;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
}
