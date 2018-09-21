package com.wqp.webservice.entity;

import java.io.Serializable; 

/** UserInfo-会员表*/
public class GetUserInfoBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer userid;//主键
	private String username;//用户名
	private String password;//密码
	private String name;//姓名
	private String nickname;//昵称
	private String picture;//头像
	private String email;//邮箱
	private String qq;//QQ
	private String msn;//MSN
	private String sex;//性别
	private String age;//年龄段
	private String usertype;//用户类型
	private String abstracts;//简介
	private String address;//地址
	private String phone;//手机号
	private String website;//个人网站
	private String hobby;//运动爱好
	private String venue;//场馆
	private String charge;//服务收费
	private String birthyear;//出生日期 ;//该字段数据库当中为Date类型,这里为了方便处理更新为String
	private String danwei;//工作单位
	private String rewards;//奖励
	private String certificate;//证书
	private String advanced;//高级信息设置
	private String friend;//好友信息设置
	private String inviter;//邀请人账号
	private String cgwebsite;//场馆网站
	private String telephone;//电话
	private String shopsname;//提供商家名
	private String factory;//厂址
	private String shop;//店址
	private String zipcode;//邮编 
	private String tgstype;//提供商类型
	private String creatertime;//加入时间
	private String state;//激活状态
	
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
