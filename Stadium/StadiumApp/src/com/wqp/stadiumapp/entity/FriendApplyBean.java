package com.wqp.stadiumapp.entity;

import java.io.Serializable;

public class FriendApplyBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer image;//ͷ��
	private String name;//����
	private String content;//����
	private Integer state;//״̬��ͬ�������ͬ��
	
	
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
