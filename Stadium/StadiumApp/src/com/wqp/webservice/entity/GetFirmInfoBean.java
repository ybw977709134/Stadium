package com.wqp.webservice.entity;

import java.io.Serializable;

/** FirmInfo—企业管理*/
public class GetFirmInfoBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer firmid;//主键
	private String firmtype;//类型;如公司简介、联系我们、关于我们等
	private String firmcon;//内容 
	
	public GetFirmInfoBean() { }
	
	public Integer getFirmid() {
		return firmid;
	}
	public void setFirmid(Integer firmid) {
		this.firmid = firmid;
	}
	public String getFirmtype() {
		return firmtype;
	}
	public void setFirmtype(String firmtype) {
		this.firmtype = firmtype;
	}
	public String getFirmcon() {
		return firmcon;
	}
	public void setFirmcon(String firmcon) {
		this.firmcon = firmcon;
	}
	
	
}
