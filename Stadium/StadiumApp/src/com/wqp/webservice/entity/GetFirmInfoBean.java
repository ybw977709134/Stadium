package com.wqp.webservice.entity;

import java.io.Serializable;

/** FirmInfo����ҵ����*/
public class GetFirmInfoBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer firmid;//����
	private String firmtype;//����;�繫˾��顢��ϵ���ǡ��������ǵ�
	private String firmcon;//���� 
	
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
