package com.wqp.stadiumapp.entity;

import java.io.Serializable;

public class PrivateForumlateBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer image;//ͷ��
	private String name;//����
	private String sparetime;//��ҵʱ��
	private String sportevent;//�˶���Ŀ 
	
	public PrivateForumlateBean() {
		super();
	}

	public PrivateForumlateBean(Integer image, String name, String sparetime,
			String sportevent) {
		super();
		this.image = image;
		this.name = name;
		this.sparetime = sparetime;
		this.sportevent = sportevent;
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
	public String getSparetime() {
		return sparetime;
	}
	public void setSparetime(String sparetime) {
		this.sparetime = sparetime;
	}
	public String getSportevent() {
		return sportevent;
	}
	public void setSportevent(String sportevent) {
		this.sportevent = sportevent;
	}
	
	
}
