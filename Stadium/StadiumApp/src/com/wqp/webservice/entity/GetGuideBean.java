package com.wqp.webservice.entity;

import java.io.Serializable; 

/** SportsGuide-ָ����*/
public class GetGuideBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer sgid;//����
	private String title;//����
	private String sgtype;//ָ������
	private String sgcontent;//ָ������
	private String url;//ָ����Ƶ��ַ
	private Integer creater;//ָ�������
	private String createtime;//ָ�����ʱ��
	private String wonori;//�Ƿ�ͨ�����
	private String wonlock;//�Ƿ�����
	private Integer ctr;//�����
	private Integer sort;//����
	private String status;//״̬
	private Integer genre;//ָ�����
	
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
