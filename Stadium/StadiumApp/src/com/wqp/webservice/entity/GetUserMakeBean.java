package com.wqp.webservice.entity;

import java.io.Serializable;

public class GetUserMakeBean implements Serializable { 
	private static final long serialVersionUID = 1L;
	
	private int MakeInfoID;//ԤԼ��ϢID
	private int VenuesID;//����ID
	private String VenuesName;//��������
	private int ProductID;//�˶���ĿID
	private String State;//ԤԼ״̬
	private int UserID;//ԤԼ�û�ID
	private String MakeUserName;//ԤԼ������
	private String MakeTime;//ԤԼʱ��
	private String MakeCode;//��֤��
	private String BankCode;//�����˺� //�˶�ʱ�Ż��õ�
	private String OpenBand;//�������� //�˶�ʱ�Ż��õ�
	private String BankUserName;//���� //�˶�ʱ�Ż��õ�
	private String PayType;//���ʽ(֧������΢��)
	private String ProjectName;//�˶���Ŀ����
	
	
	
	public String getVenuesName() {
		return VenuesName;
	}
	public void setVenuesName(String venuesName) {
		VenuesName = venuesName;
	}
	public int getMakeInfoID() {
		return MakeInfoID;
	}
	public void setMakeInfoID(int makeInfoID) {
		MakeInfoID = makeInfoID;
	}
	public int getVenuesID() {
		return VenuesID;
	}
	public void setVenuesID(int venuesID) {
		VenuesID = venuesID;
	}
	public int getProductID() {
		return ProductID;
	}
	public void setProductID(int productID) {
		ProductID = productID;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public int getUserID() {
		return UserID;
	}
	public void setUserID(int userID) {
		UserID = userID;
	}
	public String getMakeUserName() {
		return MakeUserName;
	}
	public void setMakeUserName(String makeUserName) {
		MakeUserName = makeUserName;
	}
	public String getMakeTime() {
		return MakeTime;
	}
	public void setMakeTime(String makeTime) {
		MakeTime = makeTime;
	}
	public String getMakeCode() {
		return MakeCode;
	}
	public void setMakeCode(String makeCode) {
		MakeCode = makeCode;
	}
	public String getBankCode() {
		return BankCode;
	}
	public void setBankCode(String bankCode) {
		BankCode = bankCode;
	}
	public String getOpenBand() {
		return OpenBand;
	}
	public void setOpenBand(String openBand) {
		OpenBand = openBand;
	}
	public String getBankUserName() {
		return BankUserName;
	}
	public void setBankUserName(String bankUserName) {
		BankUserName = bankUserName;
	}
	public String getPayType() {
		return PayType;
	}
	public void setPayType(String payType) {
		PayType = payType;
	}
	public String getProjectName() {
		return ProjectName;
	}
	public void setProjectName(String projectName) {
		ProjectName = projectName;
	}
	
	

}
