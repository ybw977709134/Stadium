package com.wqp.stadiumapp.entity;

import java.io.Serializable;

public class FavorablePlanBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer image;//ͷ��
	private String title;//����
	private String initiator;//������
	private String plantime;//�ʱ��
	private String planaddress;//���ַ
	private Integer praise;//��������
	private Integer message;//��Ϣ����
	
	public FavorablePlanBean() {
		super();
	}

	public FavorablePlanBean(Integer image, String title, String initiator,
			String plantime, String planaddress, Integer praise, Integer message) {
		super();
		this.image = image;
		this.title = title;
		this.initiator = initiator;
		this.plantime = plantime;
		this.planaddress = planaddress;
		this.praise = praise;
		this.message = message;
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
	public String getInitiator() {
		return initiator;
	}
	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}
	public String getPlantime() {
		return plantime;
	}
	public void setPlantime(String plantime) {
		this.plantime = plantime;
	}
	public String getPlanaddress() {
		return planaddress;
	}
	public void setPlanaddress(String planaddress) {
		this.planaddress = planaddress;
	}
	public Integer getPraise() {
		return praise;
	}
	public void setPraise(Integer praise) {
		this.praise = praise;
	}
	public Integer getMessage() {
		return message;
	}
	public void setMessage(Integer message) {
		this.message = message;
	}
	
	
}
