package com.wqp.webservice.entity;

import java.io.Serializable;
import java.util.Date;

/** ��Ŀ��Ϣ��(ProjectInfo)*/
public class GetProjectInfoBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer projectid;//����;��Ŀ���
	private Integer venuesid;//���ݱ��;������Ϣ�����
	private String venuesname;//��Ŀ����
	private String timeprice;//ʱ��μ��۸�
	private String price;//�۸�
	private String discountinfo;//�Ż���Ϣ
	private String sitespec;//���ع��
	private String remarks;//��ע
	private String creater;//������
	private Date createtime;//����ʱ��
	
	public GetProjectInfoBean(){}
	
	public Integer getProjectid() {
		return projectid;
	}
	public void setProjectid(Integer projectid) {
		this.projectid = projectid;
	}
	public Integer getVenuesid() {
		return venuesid;
	}
	public void setVenuesid(Integer venuesid) {
		this.venuesid = venuesid;
	}
	public String getVenuesname() {
		return venuesname;
	}
	public void setVenuesname(String venuesname) {
		this.venuesname = venuesname;
	}
	public String getTimeprice() {
		return timeprice;
	}
	public void setTimeprice(String timeprice) {
		this.timeprice = timeprice;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getDiscountinfo() {
		return discountinfo;
	}
	public void setDiscountinfo(String discountinfo) {
		this.discountinfo = discountinfo;
	}
	public String getSitespec() {
		return sitespec;
	}
	public void setSitespec(String sitespec) {
		this.sitespec = sitespec;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	
}
