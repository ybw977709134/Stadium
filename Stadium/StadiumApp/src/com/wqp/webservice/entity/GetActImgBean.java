package com.wqp.webservice.entity;

import java.util.Date;

/** ActImg-�ͼƬ��*/
public class GetActImgBean {
	private Integer actimgid;//����
	private Integer actid;//�ID
	private String imgdes;//�ͼƬ����
	private String imgurl;//�ͼƬ��ַ
	private Date createtime;//�ϴ�ʱ��
	
	public GetActImgBean(){}
	
	public Integer getActimgid() {
		return actimgid;
	}
	public void setActimgid(Integer actimgid) {
		this.actimgid = actimgid;
	}
	public Integer getActid() {
		return actid;
	}
	public void setActid(Integer actid) {
		this.actid = actid;
	}
	public String getImgdes() {
		return imgdes;
	}
	public void setImgdes(String imgdes) {
		this.imgdes = imgdes;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	
}
