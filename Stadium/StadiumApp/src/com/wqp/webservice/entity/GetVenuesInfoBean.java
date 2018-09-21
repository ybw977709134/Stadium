package com.wqp.webservice.entity;

import java.io.Serializable; 

/** ������Ϣ��(VenuesInfo)*/
public class GetVenuesInfoBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer venuesid;//����ID
	private String venuesname;//��������
	private String venuesarea;//������������
	private String address;//��ϸ��ַ
	private String reservephone;//Ԥ���绰
	private String venuesemail;//��������
	private String zipcode;//�ʱ�
	private String rideroute;//�˳�·��
	private Double zhangdiarea;//ռ�����
	private Double jianzhuarea;//�������
	private Double changdiarea;//�������
	private String internalfacilities;//�ڲ���ʩ
	private String groundmaterial;//�������
	private Double watertemperature;//ˮ��
	private Double headroom;//���ո߶�
	private Double waterdepth;//ˮ��
	private String other;//����
	private String venuestype;//��Ӫ����(��������)
	private String venuesenvironment;//������������
	private String bridfing;//���ݼ��
	private String directordept;//���ܲ���
	private String responsiblepeople;//���ݸ�����
	private String contact;//������ϵ��
	private String contactphone;//��ϵ�绰
	private Double investment;//Ͷ�ʽ��
	private String builttime;//����ʱ��
	private String usetime;//Ͷ��ʹ��ʱ��
	private Integer seatnumber;//������ϯ����
	private Integer avgnumber;//ÿ��ƽ��������
	private String venuesimage;//����ͼƬ
	private String whetherverify;//�Ƿ��ʵ
	private String whetherlock;//�Ƿ�����
	private String whetherrecomm;//�Ƿ��Ƽ�
	private Integer sort;//�Ƽ�����
	private String map;//��ͼ
	private String creater;//������
	private String createtime;//����ʱ��
	
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
