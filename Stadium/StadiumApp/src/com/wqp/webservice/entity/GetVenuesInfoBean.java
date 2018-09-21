package com.wqp.webservice.entity;

import java.io.Serializable; 

/** 场馆信息表(VenuesInfo)*/
public class GetVenuesInfoBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer venuesid;//场馆ID
	private String venuesname;//场馆名称
	private String venuesarea;//场馆所在区域
	private String address;//详细地址
	private String reservephone;//预定电话
	private String venuesemail;//场馆邮箱
	private String zipcode;//邮编
	private String rideroute;//乘车路线
	private Double zhangdiarea;//占地面积
	private Double jianzhuarea;//建筑面积
	private Double changdiarea;//场地面积
	private String internalfacilities;//内部设施
	private String groundmaterial;//地面材料
	private Double watertemperature;//水温
	private Double headroom;//净空高度
	private Double waterdepth;//水深
	private String other;//其他
	private String venuestype;//运营性质(场馆类型)
	private String venuesenvironment;//场馆所属环境
	private String bridfing;//场馆简介
	private String directordept;//主管部门
	private String responsiblepeople;//场馆负责人
	private String contact;//场馆联系人
	private String contactphone;//联系电话
	private Double investment;//投资金额
	private String builttime;//建成时间
	private String usetime;//投入使用时间
	private Integer seatnumber;//场馆坐席数量
	private Integer avgnumber;//每日平均客流量
	private String venuesimage;//场馆图片
	private String whetherverify;//是否核实
	private String whetherlock;//是否锁定
	private String whetherrecomm;//是否推荐
	private Integer sort;//推荐排序
	private String map;//地图
	private String creater;//创建人
	private String createtime;//创建时间
	
	public GetVenuesInfoBean(){}
	
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
	public String getVenuesarea() {
		return venuesarea;
	}
	public void setVenuesarea(String venuesarea) {
		this.venuesarea = venuesarea;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getReservephone() {
		return reservephone;
	}
	public void setReservephone(String reservephone) {
		this.reservephone = reservephone;
	}
	public String getVenuesemail() {
		return venuesemail;
	}
	public void setVenuesemail(String venuesemail) {
		this.venuesemail = venuesemail;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getRideroute() {
		return rideroute;
	}
	public void setRideroute(String rideroute) {
		this.rideroute = rideroute;
	}
	public Double getZhangdiarea() {
		return zhangdiarea;
	}
	public void setZhangdiarea(Double zhangdiarea) {
		this.zhangdiarea = zhangdiarea;
	}
	public Double getJianzhuarea() {
		return jianzhuarea;
	}
	public void setJianzhuarea(Double jianzhuarea) {
		this.jianzhuarea = jianzhuarea;
	}
	public Double getChangdiarea() {
		return changdiarea;
	}
	public void setChangdiarea(Double changdiarea) {
		this.changdiarea = changdiarea;
	}
	public String getInternalfacilities() {
		return internalfacilities;
	}
	public void setInternalfacilities(String internalfacilities) {
		this.internalfacilities = internalfacilities;
	}
	public String getGroundmaterial() {
		return groundmaterial;
	}
	public void setGroundmaterial(String groundmaterial) {
		this.groundmaterial = groundmaterial;
	}
	public Double getWatertemperature() {
		return watertemperature;
	}
	public void setWatertemperature(Double watertemperature) {
		this.watertemperature = watertemperature;
	}
	public Double getHeadroom() {
		return headroom;
	}
	public void setHeadroom(Double headroom) {
		this.headroom = headroom;
	}
	public Double getWaterdepth() {
		return waterdepth;
	}
	public void setWaterdepth(Double waterdepth) {
		this.waterdepth = waterdepth;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public String getVenuestype() {
		return venuestype;
	}
	public void setVenuestype(String venuestype) {
		this.venuestype = venuestype;
	}
	public String getVenuesenvironment() {
		return venuesenvironment;
	}
	public void setVenuesenvironment(String venuesenvironment) {
		this.venuesenvironment = venuesenvironment;
	}
	public String getBridfing() {
		return bridfing;
	}
	public void setBridfing(String bridfing) {
		this.bridfing = bridfing;
	}
	public String getDirectordept() {
		return directordept;
	}
	public void setDirectordept(String directordept) {
		this.directordept = directordept;
	}
	public String getResponsiblepeople() {
		return responsiblepeople;
	}
	public void setResponsiblepeople(String responsiblepeople) {
		this.responsiblepeople = responsiblepeople;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getContactphone() {
		return contactphone;
	}
	public void setContactphone(String contactphone) {
		this.contactphone = contactphone;
	}
	public Double getInvestment() {
		return investment;
	}
	public void setInvestment(Double investment) {
		this.investment = investment;
	}
	public String getBuilttime() {
		return builttime;
	}
	public void setBuilttime(String builttime) {
		this.builttime = builttime;
	}
	public String getUsetime() {
		return usetime;
	}
	public void setUsetime(String usetime) {
		this.usetime = usetime;
	}
	public Integer getSeatnumber() {
		return seatnumber;
	}
	public void setSeatnumber(Integer seatnumber) {
		this.seatnumber = seatnumber;
	}
	public Integer getAvgnumber() {
		return avgnumber;
	}
	public void setAvgnumber(Integer avgnumber) {
		this.avgnumber = avgnumber;
	}
	public String getVenuesimage() {
		return venuesimage;
	}
	public void setVenuesimage(String venuesimage) {
		this.venuesimage = venuesimage;
	}
	public String getWhetherverify() {
		return whetherverify;
	}
	public void setWhetherverify(String whetherverify) {
		this.whetherverify = whetherverify;
	}
	public String getWhetherlock() {
		return whetherlock;
	}
	public void setWhetherlock(String whetherlock) {
		this.whetherlock = whetherlock;
	}
	public String getWhetherrecomm() {
		return whetherrecomm;
	}
	public void setWhetherrecomm(String whetherrecomm) {
		this.whetherrecomm = whetherrecomm;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}

	@Override
	public String toString() {
		return "GetVenuesInfoBean [venuesid=" + venuesid + ", venuesname="
				+ venuesname + ", venuesarea=" + venuesarea + ", address="
				+ address + ", reservephone=" + reservephone + ", venuesemail="
				+ venuesemail + ", zipcode=" + zipcode + ", rideroute="
				+ rideroute + ", zhangdiarea=" + zhangdiarea + ", jianzhuarea="
				+ jianzhuarea + ", changdiarea=" + changdiarea
				+ ", internalfacilities=" + internalfacilities
				+ ", groundmaterial=" + groundmaterial + ", watertemperature="
				+ watertemperature + ", headroom=" + headroom + ", waterdepth="
				+ waterdepth + ", other=" + other + ", venuestype="
				+ venuestype + ", venuesenvironment=" + venuesenvironment
				+ ", bridfing=" + bridfing + ", directordept=" + directordept
				+ ", responsiblepeople=" + responsiblepeople + ", contact="
				+ contact + ", contactphone=" + contactphone + ", investment="
				+ investment + ", builttime=" + builttime + ", usetime="
				+ usetime + ", seatnumber=" + seatnumber + ", avgnumber="
				+ avgnumber + ", venuesimage=" + venuesimage
				+ ", whetherverify=" + whetherverify + ", whetherlock="
				+ whetherlock + ", whetherrecomm=" + whetherrecomm + ", sort="
				+ sort + ", map=" + map + ", creater=" + creater
				+ ", createtime=" + createtime + "]";
	}
	
	
}
