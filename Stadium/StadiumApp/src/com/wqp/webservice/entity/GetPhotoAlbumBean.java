package com.wqp.webservice.entity;

import java.io.Serializable;
import java.util.Date;

/** 相册信息表(PhotoAlbum)*/
public class GetPhotoAlbumBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer albumid;//主键;相册编号 
	private Integer albumcover;//相册封面;0/ PhotoID
	private String albumname;//相册名称
	private String ablumdes;//相册描述
	private Integer typeid;//类型编号;场馆ID/个人ID
	private String typename;//类型名称;场馆/个人
	private String creater;//创建人
	private Date createtime;//创建时间
	
	public GetPhotoAlbumBean(){}
	
	public Integer getAlbumid() {
		return albumid;
	}
	public void setAlbumid(Integer albumid) {
		this.albumid = albumid;
	}
	public Integer getAlbumcover() {
		return albumcover;
	}
	public void setAlbumcover(Integer albumcover) {
		this.albumcover = albumcover;
	}
	public String getAlbumname() {
		return albumname;
	}
	public void setAlbumname(String albumname) {
		this.albumname = albumname;
	}
	public String getAblumdes() {
		return ablumdes;
	}
	public void setAblumdes(String ablumdes) {
		this.ablumdes = ablumdes;
	}
	public Integer getTypeid() {
		return typeid;
	}
	public void setTypeid(Integer typeid) {
		this.typeid = typeid;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
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
