package com.wqp.stadiumapp;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.wqp.stadiumapp.utils.UserGlobal;
import com.wqp.webservice.WebGetAUser;
import com.wqp.webservice.WebGetArtInfo;
import com.wqp.webservice.WebGetComment;
import com.wqp.webservice.WebGetGuide;
import com.wqp.webservice.WebGetSports;
import com.wqp.webservice.WebGetVenues;
import com.wqp.webservice.entity.GetActInfoBean;
import com.wqp.webservice.entity.GetCommentBean;
import com.wqp.webservice.entity.GetGuideBean;
import com.wqp.webservice.entity.GetSportBean;
import com.wqp.webservice.entity.GetUserInfoBean;
import com.wqp.webservice.entity.GetVenuesInfoBean;

public class WebServiceTest extends Activity { 
	private static String TAG="WebServiceTest";
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.webservicetest);  
	}
	
	/** AddReservation*/
	public void AddReservation(View v){
//		new Thread(){
//			public void run() {
//				WebAddReservation.getAddReservationData(str, fs, fileType);
//			};
//		}.start();
	}
	
	/** AddUser*/
	public void AddUser(View v){
		
	}
	
	/** GetAUser*/
	public void GetAUser(View v){
		new Thread(){
			public void run() { 
				JSONObject object=new JSONObject();
				try {
					object.put("UserID", UserGlobal.UserID);
				} catch (JSONException e) { 
					e.printStackTrace();
				}
				List<GetUserInfoBean> result=WebGetAUser.getGetAUserData(object.toString());
				for (GetUserInfoBean getUserInfoBean : result) {
					Log.i(TAG+"List<GetUserInfoBean>",
							"主键="+getUserInfoBean.getUserid()
							+",用户名="+getUserInfoBean.getUsername()
							+",密码="+getUserInfoBean.getPassword()
							+",姓名="+getUserInfoBean.getName()
							+",昵称="+getUserInfoBean.getNickname()
							+",头像="+getUserInfoBean.getPicture()
							+",邮箱="+getUserInfoBean.getEmail()
							+",QQ="+getUserInfoBean.getQq()
							+",MSN="+getUserInfoBean.getMsn()
							+",性别="+getUserInfoBean.getSex()
							+",年龄段="+getUserInfoBean.getAge()
							+",用户类型="+getUserInfoBean.getUsertype()
							+",简介="+getUserInfoBean.getAbstracts()
							+",地址="+getUserInfoBean.getAddress()
							+",手机号="+getUserInfoBean.getPhone()
							+",个人网站="+getUserInfoBean.getWebsite()
							+",运动爱好="+getUserInfoBean.getHobby()
							+",场馆="+getUserInfoBean.getVenue()
							+",服务收费="+getUserInfoBean.getCharge()
							+",出生日期="+getUserInfoBean.getBirthyear().toString()
							+",工作单位="+getUserInfoBean.getDanwei()
							+",奖励="+getUserInfoBean.getRewards()
							+",证书="+getUserInfoBean.getCertificate()
							+",高级信息设置="+getUserInfoBean.getAdvanced()
							+",好友信息设置="+getUserInfoBean.getFriend()
							+",邀请人账号="+getUserInfoBean.getInviter()
							+",场馆网站="+getUserInfoBean.getCgwebsite()
							+",电话="+getUserInfoBean.getTelephone()
							+",提供商家名="+getUserInfoBean.getShopsname()
							+",厂址="+getUserInfoBean.getFactory()
							+",店址="+getUserInfoBean.getShop()
							+",邮编="+getUserInfoBean.getZipcode()
							+",提供商类型="+getUserInfoBean.getTgstype()
							+",加入时间="+getUserInfoBean.getCreatertime().toString()
							+",激活状态="+getUserInfoBean.getState()
							+"\n");
				}
			};
		}.start(); 
	}
	
	/** GetArtInfo*/
	public void GetArtInfo(View v){
		new Thread(){ 
			public void run() {
				List<GetActInfoBean> result=WebGetArtInfo.getGetArtInfoData("");
				if(result!=null){
					for (GetActInfoBean getActInfoBean : result) {
						Log.i(TAG+"ActInfo-活动表","主键="+getActInfoBean.getActid()
								+",活动类型="+getActInfoBean.getActtypeid() 
								+",运动类型="+getActInfoBean.getSporttype()
								+",活动标题="+getActInfoBean.getActtitle() 
								+",使用场馆="+getActInfoBean.getVenuesname() 
								+",场馆费用="+getActInfoBean.getVenuescost()
								+",宣传图片="+getActInfoBean.getPubimg() 
								+",开始时间="+getActInfoBean.getStarttime()
								+",结束时间="+getActInfoBean.getEndtime()
								+",报名截止日期="+getActInfoBean.getApplyendtime() 
								+",参与人数活动上限="+getActInfoBean.getTakecount() 
								+",裁判人数="+getActInfoBean.getRefereecount() 
								+",指导员人数="+getActInfoBean.getGuidecount() 
								+",活动具体内容="+getActInfoBean.getActcon() 
								+",活动公告="+getActInfoBean.getActnotice() 
								+",发布人="+getActInfoBean.getCreateuser() 
								+",发布时间="+getActInfoBean.getCreatetime() 
								+",热点="+getActInfoBean.getHotspot() 
								+",加精="+getActInfoBean.getAdddigest() 
								+",浏览次数="+getActInfoBean.getBrowsecount() 
								+",状态="+getActInfoBean.getState() 
								+",活动创建类型="+getActInfoBean.getActusertype()
								+"\n");
					}
				}else{Log.i(TAG,"List<GetActInfoBean> is null");}
			};
		}.start();
	}
	
	/** GetComment*/
	public void GetComment(View v){
		new Thread(){
			public void run(){
				List<GetCommentBean> result=WebGetComment.getGetCommentData("");
				for (GetCommentBean getCommentBean : result) {
					Log.i(TAG+"List<GetCommentBean>",
							"主键="+getCommentBean.getCommentid()+
							",点评人ID="+getCommentBean.getUserid()+
							",点评内容="+getCommentBean.getCon()+
							",所属内容ID="+getCommentBean.getKeyid()+
							",所属内容类型="+getCommentBean.getKeytype()+
							",点评时间="+getCommentBean.getCreatetime()+
							"\n");
				}
			}
		}.start();
	}
	
	/** GetGuide */
	public void GetGuide(View v){
		new Thread(){
			public void run() {
				List<GetGuideBean> result=WebGetGuide.getGetGuideData("SGType='新闻'");
				for (GetGuideBean getGuideBean : result) {
					Log.i(TAG+"List<GetGuideBean>",
							"主键="+getGuideBean.getSgid()+
							",标题="+getGuideBean.getTitle()+
							",指导类型="+getGuideBean.getSgtype()+
							",指导内容="+getGuideBean.getSgcontent()+
							",指导视频地址="+getGuideBean.getUrl()+
							",指导添加人="+getGuideBean.getCreater()+
							",指导添加时间="+getGuideBean.getCreatetime()+
							",是否通过审核="+getGuideBean.getWonori()+
							",是否锁定="+getGuideBean.getWonlock()+
							",点击率="+getGuideBean.getCtr()+
							",排序="+getGuideBean.getSort()+
							",状态="+getGuideBean.getStatus()+
							",指导类别="+getGuideBean.getGenre()+
							"\n");
					if(getGuideBean.getUrl()!=null){
						Intent intent=new Intent(Intent.ACTION_VIEW);
						intent.setDataAndType(Uri.parse(getGuideBean.getUrl()), "video/*");
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						getApplicationContext().startActivity(intent);
					}
				}
			};
		}.start();
	}
	
	/** GetSports */
	public void GetSports(View v){
		new Thread(){
			public void run() {
				List<GetSportBean> dataset=WebGetSports.getGetSportsData("");//获取到Web端的数据,暂时会直接打印Log在控制台上面	
				for (GetSportBean getSportBean : dataset) {
					Log.i(TAG+"SportInfo-活动表","主键="+getSportBean.getSportid()
							+",运动名称="+getSportBean.getSportname()
							+",是否推荐="+getSportBean.isIsrec()+"\n");
				}
			};
		}.start(); 
	}
	
	/** GetVenues*/
	public void GetVenues(View v){
		new Thread(){
			public void run() {
				//需要从Service中获取到全部信息
				List<GetVenuesInfoBean> result=WebGetVenues.getGetVenuesData("");
				if(result!=null){
					Log.i("从Service返回的数据",result.toArray().toString());
				}
			};
		}.start();
	}
	
	/** JoinAct */
	public void JoinAct(View v){
		
	}
	
	/** Login*/
	public void Login(View v){
		
	}
	
	/** Upload*/
	public void Upload(View v){
		
	}
	
	/** UserUpdate*/
	public void UserUpdate(View v){
		
	}
	
}
