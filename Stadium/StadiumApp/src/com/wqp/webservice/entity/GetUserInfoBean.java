package com.wqp.webservice.entity;

import java.io.Serializable; 

/** UserInfo-��Ա��*/
public class GetUserInfoBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer userid;//����
	private String username;//�û���
	private String password;//����
	private String name;//����
	private String nickname;//�ǳ�
	private String picture;//ͷ��
	private String email;//����
	private String qq;//QQ
	private String msn;//MSN
	private String sex;//�Ա�
	private String age;//�����
	private String usertype;//�û�����
	private String abstracts;//���
	private String address;//��ַ
	private String phone;//�ֻ���
	private String website;//������վ
	private String hobby;//�˶�����
	private String venue;//����
	private String charge;//�����շ�
	private String birthyear;//�������� ;//���ֶ����ݿ⵱��ΪDate����,����Ϊ�˷��㴦�����ΪString
	private String danwei;//������λ
	private String rewards;//����
	private String certificate;//֤��
	private String advanced;//�߼���Ϣ����
	private String friend;//������Ϣ����
	private String inviter;//�������˺�
	private String cgwebsite;//������վ
	private String telephone;//�绰
	private String shopsname;//�ṩ�̼���
	private String factory;//��ַ
	private String shop;//��ַ
	private String zipcode;//�ʱ� 
	private String tgstype;//�ṩ������
	private String creatertime;//����ʱ��
	private String state;//����״̬
	
	public GetUserInfoBean(){}
	
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public String getZipcode() {
		return zipcode;
	} 
	
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getMsn() {
		return msn;
	}
	public void setMsn(String msn) {
		this.msn = msn;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public String getAbstracts() {
		return abstracts;
	}
	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getHobby() {
		return hobby;
	}
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}
	public String getVenue() {
		return venue;
	}
	public void setVenue(String venue) {
		this.venue = venue;
	}
	public String getCharge() {
		return charge;
	}
	public void setCharge(String charge) {
		this.charge = charge;
	}
	public String getBirthyear() {
		return birthyear;
	}
	public void setBirthyear(String birthyear) {
		this.birthyear = birthyear;
	}
	public String getDanwei() {
		return danwei;
	}
	public void setDanwei(String danwei) {
		this.danwei = danwei;
	}
	public String getRewards() {
		return rewards;
	}
	public void setRewards(String rewards) {
		this.rewards = rewards;
	}
	public String getCertificate() {
		return certificate;
	}
	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}
	public String getAdvanced() {
		return advanced;
	}
	public void setAdvanced(String advanced) {
		this.advanced = advanced;
	}
	public String getFriend() {
		return friend;
	}
	public void setFriend(String friend) {
		this.friend = friend;
	}
	public String getInviter() {
		return inviter;
	}
	public void setInviter(String inviter) {
		this.inviter = inviter;
	}
	public String getCgwebsite() {
		return cgwebsite;
	}
	public void setCgwebsite(String cgwebsite) {
		this.cgwebsite = cgwebsite;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getShopsname() {
		return shopsname;
	}
	public void setShopsname(String shopsname) {
		this.shopsname = shopsname;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public String getShop() {
		return shop;
	}
	public void setShop(String shop) {
		this.shop = shop;
	}
	public String getTgstype() {
		return tgstype;
	}
	public void setTgstype(String tgstype) {
		this.tgstype = tgstype;
	}
	public String getCreatertime() {
		return creatertime;
	}
	public void setCreatertime(String creatertime) {
		this.creatertime = creatertime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "GetUserInfoBean [userid=" + userid + ", username=" + username
				+ ", password=" + password + ", name=" + name + ", nickname="
				+ nickname + ", picture=" + picture + ", email=" + email
				+ ", qq=" + qq + ", msn=" + msn + ", sex=" + sex + ", age="
				+ age + ", usertype=" + usertype + ", abstracts=" + abstracts
				+ ", address=" + address + ", phone=" + phone + ", website="
				+ website + ", hobby=" + hobby + ", venue=" + venue
				+ ", charge=" + charge + ", birthyear=" + birthyear
				+ ", danwei=" + danwei + ", rewards=" + rewards
				+ ", certificate=" + certificate + ", advanced=" + advanced
				+ ", friend=" + friend + ", inviter=" + inviter
				+ ", cgwebsite=" + cgwebsite + ", telephone=" + telephone
				+ ", shopsname=" + shopsname + ", factory=" + factory
				+ ", shop=" + shop + ", zipcode=" + zipcode + ", tgstype="
				+ tgstype + ", creatertime=" + creatertime + ", state=" + state
				+ "]";
	} 
	
}
