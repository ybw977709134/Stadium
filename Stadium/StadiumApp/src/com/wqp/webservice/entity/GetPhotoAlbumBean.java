package com.wqp.webservice.entity;

import java.io.Serializable;
import java.util.Date;

/** �����Ϣ��(PhotoAlbum)*/
public class GetPhotoAlbumBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer albumid;//����;����� 
	private Integer albumcover;//������;0/ PhotoID
	private String albumname;//�������
	private String ablumdes;//�������
	private Integer typeid;//���ͱ��;����ID/����ID
	private String typename;//��������;����/����
	private String creater;//������
	private Date createtime;//����ʱ��
	
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
