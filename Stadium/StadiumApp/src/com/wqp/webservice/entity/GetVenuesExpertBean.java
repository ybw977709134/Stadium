package com.wqp.webservice.entity;

import java.io.Serializable;

public class GetVenuesExpertBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private int Number;//���ֱ��
	private int ExpertID;//ר��ID
	private String VenuesName;//��������
	private int Creater;//������
	private String Sex;//�Ա�
	private String ZhuanJiaType;//ר������
	private String NickName;//�ǳ�
	private String Hobby;//ר�ҵ��˶���ĿID�ļ��ϣ��������������
	
	
	
	public int getNumber() {
		return Number;
	}
	public void setNumber(int number) {
		Number = number;
	}
	public int getExpertID() {
		return ExpertID;
	}
	public void setExpertID(int expertID) {
		ExpertID = expertID;
	}
	public String getVenuesName() {
		return VenuesName;
	}
	public void setVenuesName(String venuesName) {
		VenuesName = venuesName;
	}
	public int getCreater() {
		return Creater;
	}
	public void setCreater(int creater) {
		Creater = creater;
	}
	public String getSex() {
		return Sex;
	}
	public void setSex(String sex) {
		Sex = sex;
	}
	public String getZhuanJiaType() {
		return ZhuanJiaType;
	}
	public void setZhuanJiaType(String zhuanJiaType) {
		ZhuanJiaType = zhuanJiaType;
	}
	public String getNickName() {
		return NickName;
	}
	public void setNickName(String nickName) {
		NickName = nickName;
	}
	public String getHobby() {
		return Hobby;
	}
	public void setHobby(String hobby) {
		Hobby = hobby;
	}
	
	
}
