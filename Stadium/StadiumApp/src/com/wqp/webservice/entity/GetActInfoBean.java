package com.wqp.webservice.entity;

import java.io.Serializable; 

/** ActInfo-���*/
public class GetActInfoBean implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private Integer actid;//����
	private Integer acttypeid;//�����
	private Integer sporttype;//�˶�����
	private String sporttypestring;//�˶������ַ�������(��������Ǹ���ҳ��������ʾ�Լ���ӵ�һ���������ͣ����ֵ���������ݱ��޹�)				
	private String acttitle;//�����
	private String venuesname;//ʹ�ó���
	private String venuescost;//���ݷ���
	private String pubimg;//����ͼƬ
	private String starttime;//��ʼʱ��
	private String endtime;//����ʱ��
	private String applyendtime;//������ֹ����
	private Integer takecount;//�������������
	private Integer refereecount;//��������
	private Integer guidecount;//ָ��Ա����
	private String actcon;//���������
	private String actnotice;//�����
	private Integer createuser;//������
	private String createtime;//����ʱ��
	private Boolean hotspot;//�ȵ�,�Ƿ������Ż
	private Boolean adddigest;//�Ӿ�,�Ƿ�Ӿ�
	private Integer browsecount;//�������
	private String state;//״̬:�ȴ���ˡ�ͨ����ˡ�����
	private String actusertype;//���������:���ˡ����ݡ����ֲ�  
	private boolean imgstate=false;//�Զ����һ����ʶ,����ָ����ǰ��û��Ƿ��Ѿ��μ���
	private boolean user_act_state;//��ʶ�û��Ƿ�μ���������״̬
	
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
