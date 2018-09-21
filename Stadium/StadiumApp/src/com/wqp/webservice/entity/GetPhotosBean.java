package com.wqp.webservice.entity;

import java.io.Serializable;
import java.util.Date;

/** 照片信息表(Photos)*/
public class GetPhotosBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer photoid;//照片ID
	private String photoname;//照片名称
	private String photodes;//照片描述
	private String photourl;//照片路径
	private Integer albumid;//所属相册
	private String creater;//创建人
	private Date createtime;//创建时间
	
	public GetPhotosBean(){}
	
	public Integer getPhotoid() {
		return photoid;
	}
	public void setPhotoid(Integer photoid) {
		this.photoid = photoid;
	}
	public String getPhotoname() {
		return photoname;
	}
	public void setPhotoname(String photoname) {
		this.photoname = photoname;
	}
	public String getPhotodes() {
		return photodes;
	}
	public void setPhotodes(String photodes) {
		this.photodes = photodes;
	}
	public String getPhotourl() {
		return photourl;
	}
	public void setPhotourl(String photourl) {
		this.photourl = photourl;
	}
	public Integer getAlbumid() {
		return albumid;
	}
	public void setAlbumid(Integer albumid) {
		this.albumid = albumid;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	
}
