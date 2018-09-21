package com.wqp.stadiumapp.entity;

import java.io.Serializable;

public class HomeRecommendBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer image;//图片 
	private String title;//标题
	private String address;//地址
	private String telephone;//电话
	private String route;//路线
	private String environment;//所属环境 
	
	public HomeRecommendBean(Integer image, String title, String address,
			String telephone, String route, String environment) {
		super();
		this.image = image;
		this.title = title;
		this.address = address;
		this.telephone = telephone;
		this.route = route;
		this.environment = environment;
	}
	public HomeRecommendBean() {
		super();
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	} 
	
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
 
	
}
