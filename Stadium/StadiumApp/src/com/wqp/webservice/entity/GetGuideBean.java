package com.wqp.webservice.entity;

import java.io.Serializable; 

/** SportsGuide-指导表*/
public class GetGuideBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer sgid;//主键
	private String title;//标题
	private String sgtype;//指导类型
	private String sgcontent;//指导内容
	private String url;//指导视频地址
	private Integer creater;//指导添加人
	private String createtime;//指导添加时间
	private String wonori;//是否通过审核
	private String wonlock;//是否锁定
	private Integer ctr;//点击率
	private Integer sort;//排序
	private String status;//状态
	private Integer genre;//指导类别
	
	public GetGuideBean(){}
	
	public Integer getSgid() {
		return sgid;
	}
	public void setSgid(Integer sgid) {
		this.sgid = sgid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSgtype() {
		return sgtype;
	}
	public void setSgtype(String sgtype) {
		this.sgtype = sgtype;
	}
	public String getSgcontent() {
		return sgcontent;
	}
	public void setSgcontent(String sgcontent) {
		this.sgcontent = sgcontent;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getCreater() {
		return creater;
	}
	public void setCreater(Integer creater) {
		this.creater = creater;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getWonori() {
		return wonori;
	}
	public void setWonori(String wonori) {
		this.wonori = wonori;
	}
	public String getWonlock() {
		return wonlock;
	}
	public void setWonlock(String wonlock) {
		this.wonlock = wonlock;
	}
	public Integer getCtr() {
		return ctr;
	}
	public void setCtr(Integer ctr) {
		this.ctr = ctr;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getGenre() {
		return genre;
	}
	public void setGenre(Integer genre) {
		this.genre = genre;
	}
	
}
