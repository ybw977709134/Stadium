package com.wqp.webservice.entity;

import java.io.Serializable;
import java.util.Date;

/** ActVideo-活动视频表*/
public class GetActVideoBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer actvideoid;//主键
	private Integer actid;//活动ID
	private String videourl;//视频地址
	private String videotitle;//视频标题
	private String videotag;//视频标签
	private Boolean isori;//是否原创
	private String videodes;//视频描述
	private Date createtime;//上传时间
	
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
