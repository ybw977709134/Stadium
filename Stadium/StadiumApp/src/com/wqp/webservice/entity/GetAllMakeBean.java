package com.wqp.webservice.entity;

public class GetAllMakeBean {
	private int ProjectID;//�˶�����ID,��ĿID
	private int VenuesID;//����ID
	private String ProjectName;//�˶���Ŀ���ƣ��˶��������ƣ�
	private String MakePrice;//ԤԼ�۸�(֧������Ƹ�ͨ��Ҫ֧���ļ۸�)
	private String MakeDes;//ԤԼ��˵����Ϣ
	
	
	public int getProjectID() {
		return ProjectID;
	}
	public void setProjectID(int projectID) {
		ProjectID = projectID;
	}
	public int getVenuesID() {
		return VenuesID;
	}
	public void setVenuesID(int venuesID) {
		VenuesID = venuesID;
	}
	public String getProjectName() {
		return ProjectName;
	}
	public void setProjectName(String projectName) {
		ProjectName = projectName;
	}
	public String getMakePrice() {
		return MakePrice;
	}
	public void setMakePrice(String makePrice) {
		MakePrice = makePrice;
	}
	public String getMakeDes() {
		return MakeDes;
	}
	public void setMakeDes(String makeDes) {
		MakeDes = makeDes;
	}
	
	

}
