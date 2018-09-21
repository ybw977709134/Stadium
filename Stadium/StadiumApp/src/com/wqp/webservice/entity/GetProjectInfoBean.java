package com.wqp.webservice.entity;

import java.io.Serializable;
import java.util.Date;

/** 项目信息表(ProjectInfo)*/
public class GetProjectInfoBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer projectid;//主键;项目编号
	private Integer venuesid;//场馆编号;场馆信息表外键
	private String venuesname;//项目名称
	private String timeprice;//时间段及价格
	private String price;//价格
	private String discountinfo;//优惠信息
	private String sitespec;//场地规格
	private String remarks;//备注
	private String creater;//创建人
	private Date createtime;//创建时间
	
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
