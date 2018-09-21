package com.wqp.webservice.entity;

import java.util.Date;

/** ActImg-活动图片表*/
public class GetActImgBean {
	private Integer actimgid;//主键
	private Integer actid;//活动ID
	private String imgdes;//活动图片描述
	private String imgurl;//活动图片地址
	private Date createtime;//上传时间
	
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
