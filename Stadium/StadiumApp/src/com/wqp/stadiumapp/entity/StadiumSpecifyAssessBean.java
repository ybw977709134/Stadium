package com.wqp.stadiumapp.entity;

import java.io.Serializable;

public class StadiumSpecifyAssessBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer image;//头像
	private String name;//姓名
	private String spacetime;//评论时间
	private String content;//评论内容
	private Integer grade;//星级评分 
	
	
	public StadiumSpecifyAssessBean() {
		super();
	}
	public StadiumSpecifyAssessBean(Integer image, String name,
			String spacetime, String content, Integer grade ) {
		super();
		this.image = image;
		this.name = name;
		this.spacetime = spacetime;
		this.content = content;
		this.grade = grade; 
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
	public String getSpacetime() {
		return spacetime;
	}
	public void setSpacetime(String spacetime) {
		this.spacetime = spacetime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getGrade() {
		return grade;
	}
	public void setGrade(Integer grade) {
		this.grade = grade;
	}  

}
