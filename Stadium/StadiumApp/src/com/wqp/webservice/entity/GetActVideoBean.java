package com.wqp.webservice.entity;

import java.io.Serializable;
import java.util.Date;

/** ActVideo-���Ƶ��*/
public class GetActVideoBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer actvideoid;//����
	private Integer actid;//�ID
	private String videourl;//��Ƶ��ַ
	private String videotitle;//��Ƶ����
	private String videotag;//��Ƶ��ǩ
	private Boolean isori;//�Ƿ�ԭ��
	private String videodes;//��Ƶ����
	private Date createtime;//�ϴ�ʱ��
	
	public GetActVideoBean(){}
	
	public Integer getActvideoid() {
		return actvideoid;
	}
	public void setActvideoid(Integer actvideoid) {
		this.actvideoid = actvideoid;
	}
	public Integer getActid() {
		return actid;
	}
	public void setActid(Integer actid) {
		this.actid = actid;
	}
	public String getVideourl() {
		return videourl;
	}
	public void setVideourl(String videourl) {
		this.videourl = videourl;
	}
	public String getVideotitle() {
		return videotitle;
	}
	public void setVideotitle(String videotitle) {
		this.videotitle = videotitle;
	}
	public String getVideotag() {
		return videotag;
	}
	public void setVideotag(String videotag) {
		this.videotag = videotag;
	}
	public Boolean getIsori() {
		return isori;
	}
	public void setIsori(Boolean isori) {
		this.isori = isori;
	}
	public String getVideodes() {
		return videodes;
	}
	public void setVideodes(String videodes) {
		this.videodes = videodes;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	} 
	
	
}
