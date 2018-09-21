package com.wqp.webservice.entity;

import java.io.Serializable; 

/** ActInfo-活动表*/
public class GetActInfoBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer actid;//主键
	private Integer acttypeid;//活动类型
	private Integer sporttype;//运动类型
	private String sporttypestring;//运动类型字符串类型(这个类型是根据页面数据显示自己添加的一种数据类型，与字典里面的数据表无关)				
	private String acttitle;//活动标题
	private String venuesname;//使用场馆
	private String venuescost;//场馆费用
	private String pubimg;//宣传图片
	private String starttime;//开始时间
	private String endtime;//结束时间
	private String applyendtime;//报名截止日期
	private Integer takecount;//参与人数活动上限
	private Integer refereecount;//裁判人数
	private Integer guidecount;//指导员人数
	private String actcon;//活动具体内容
	private String actnotice;//活动公告
	private Integer createuser;//发布人
	private String createtime;//发布时间
	private Boolean hotspot;//热点,是否是热门活动
	private Boolean adddigest;//加精,是否加精
	private Integer browsecount;//浏览次数
	private String state;//状态:等待审核、通过审核、锁定
	private String actusertype;//活动创建类型:个人、场馆、俱乐部  
	private boolean imgstate=false;//自定义的一个标识,用于指定当前活动用户是否已经参加了
	private boolean user_act_state;//标识用户是否参加了这个活动的状态
	
	public GetActInfoBean() { }
	
	
	public boolean isUser_act_state() {
		return user_act_state;
	}


	public void setUser_act_state(boolean user_act_state) {
		this.user_act_state = user_act_state;
	}


	public boolean isImgstate() {
		return imgstate;
	}


	public void setImgstate(boolean imgstate) {
		this.imgstate = imgstate;
	}


	public String getSporttypestring() {
		return sporttypestring;
	}

	public void setSporttypestring(String sporttypestring) {
		this.sporttypestring = sporttypestring;
	}

	public Integer getActid() {
		return actid;
	}
	public void setActid(Integer actid) {
		this.actid = actid;
	}
	public Integer getActtypeid() {
		return acttypeid;
	}
	public void setActtypeid(Integer acttypeid) {
		this.acttypeid = acttypeid;
	}
	public Integer getSporttype() {
		return sporttype;
	}
	public void setSporttype(Integer sporttype) {
		this.sporttype = sporttype;
	}
	public String getActtitle() {
		return acttitle;
	}
	public void setActtitle(String acttitle) {
		this.acttitle = acttitle;
	}
	public String getVenuesname() {
		return venuesname;
	}
	public void setVenuesname(String venuesname) {
		this.venuesname = venuesname;
	}
	public String getVenuescost() {
		return venuescost;
	}
	public void setVenuescost(String venuescost) {
		this.venuescost = venuescost;
	}
	public String getPubimg() {
		return pubimg;
	}
	public void setPubimg(String pubimg) {
		this.pubimg = pubimg;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getApplyendtime() {
		return applyendtime;
	}
	public void setApplyendtime(String applyendtime) {
		this.applyendtime = applyendtime;
	}
	public Integer getTakecount() {
		return takecount;
	}
	public void setTakecount(Integer takecount) {
		this.takecount = takecount;
	}
	public Integer getRefereecount() {
		return refereecount;
	}
	public void setRefereecount(Integer refereecount) {
		this.refereecount = refereecount;
	}
	public Integer getGuidecount() {
		return guidecount;
	}
	public void setGuidecount(Integer guidecount) {
		this.guidecount = guidecount;
	}
	public String getActcon() {
		return actcon;
	}
	public void setActcon(String actcon) {
		this.actcon = actcon;
	}
	public String getActnotice() {
		return actnotice;
	}
	public void setActnotice(String actnotice) {
		this.actnotice = actnotice;
	}
	public Integer getCreateuser() {
		return createuser;
	}
	public void setCreateuser(Integer createuser) {
		this.createuser = createuser;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public Boolean getHotspot() {
		return hotspot;
	}
	public void setHotspot(Boolean hotspot) {
		this.hotspot = hotspot;
	}
	public Boolean getAdddigest() {
		return adddigest;
	}
	public void setAdddigest(Boolean adddigest) {
		this.adddigest = adddigest;
	}
	public Integer getBrowsecount() {
		return browsecount;
	}
	public void setBrowsecount(Integer browsecount) {
		this.browsecount = browsecount;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getActusertype() {
		return actusertype;
	}
	public void setActusertype(String actusertype) {
		this.actusertype = actusertype;
	}
}
