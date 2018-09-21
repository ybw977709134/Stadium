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
							"����="+getUserInfoBean.getUserid()
							+",�û���="+getUserInfoBean.getUsername()
							+",����="+getUserInfoBean.getPassword()
							+",����="+getUserInfoBean.getName()
							+",�ǳ�="+getUserInfoBean.getNickname()
							+",ͷ��="+getUserInfoBean.getPicture()
							+",����="+getUserInfoBean.getEmail()
							+",QQ="+getUserInfoBean.getQq()
							+",MSN="+getUserInfoBean.getMsn()
							+",�Ա�="+getUserInfoBean.getSex()
							+",�����="+getUserInfoBean.getAge()
							+",�û�����="+getUserInfoBean.getUsertype()
							+",���="+getUserInfoBean.getAbstracts()
							+",��ַ="+getUserInfoBean.getAddress()
							+",�ֻ���="+getUserInfoBean.getPhone()
							+",������վ="+getUserInfoBean.getWebsite()
							+",�˶�����="+getUserInfoBean.getHobby()
							+",����="+getUserInfoBean.getVenue()
							+",�����շ�="+getUserInfoBean.getCharge()
							+",��������="+getUserInfoBean.getBirthyear().toString()
							+",������λ="+getUserInfoBean.getDanwei()
							+",����="+getUserInfoBean.getRewards()
							+",֤��="+getUserInfoBean.getCertificate()
							+",�߼���Ϣ����="+getUserInfoBean.getAdvanced()
							+",������Ϣ����="+getUserInfoBean.getFriend()
							+",�������˺�="+getUserInfoBean.getInviter()
							+",������վ="+getUserInfoBean.getCgwebsite()
							+",�绰="+getUserInfoBean.getTelephone()
							+",�ṩ�̼���="+getUserInfoBean.getShopsname()
							+",��ַ="+getUserInfoBean.getFactory()
							+",��ַ="+getUserInfoBean.getShop()
							+",�ʱ�="+getUserInfoBean.getZipcode()
							+",�ṩ������="+getUserInfoBean.getTgstype()
							+",����ʱ��="+getUserInfoBean.getCreatertime().toString()
							+",����״̬="+getUserInfoBean.getState()
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
						Log.i(TAG+"ActInfo-���","����="+getActInfoBean.getActid()
								+",�����="+getActInfoBean.getActtypeid() 
								+",�˶�����="+getActInfoBean.getSporttype()
								+",�����="+getActInfoBean.getActtitle() 
								+",ʹ�ó���="+getActInfoBean.getVenuesname() 
								+",���ݷ���="+getActInfoBean.getVenuescost()
								+",����ͼƬ="+getActInfoBean.getPubimg() 
								+",��ʼʱ��="+getActInfoBean.getStarttime()
								+",����ʱ��="+getActInfoBean.getEndtime()
								+",������ֹ����="+getActInfoBean.getApplyendtime() 
								+",�������������="+getActInfoBean.getTakecount() 
								+",��������="+getActInfoBean.getRefereecount() 
								+",ָ��Ա����="+getActInfoBean.getGuidecount() 
								+",���������="+getActInfoBean.getActcon() 
								+",�����="+getActInfoBean.getActnotice() 
								+",������="+getActInfoBean.getCreateuser() 
								+",����ʱ��="+getActInfoBean.getCreatetime() 
								+",�ȵ�="+getActInfoBean.getHotspot() 
								+",�Ӿ�="+getActInfoBean.getAdddigest() 
								+",�������="+getActInfoBean.getBrowsecount() 
								+",״̬="+getActInfoBean.getState() 
								+",���������="+getActInfoBean.getActusertype()
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
							"����="+getCommentBean.getCommentid()+
							",������ID="+getCommentBean.getUserid()+
							",��������="+getCommentBean.getCon()+
							",��������ID="+getCommentBean.getKeyid()+
							",������������="+getCommentBean.getKeytype()+
							",����ʱ��="+getCommentBean.getCreatetime()+
							"\n");
				}
			}
		}.start();
	}
	
	/** GetGuide */
	public void GetGuide(View v){
		new Thread(){
			public void run() {
				List<GetGuideBean> result=WebGetGuide.getGetGuideData("SGType='����'");
				for (GetGuideBean getGuideBean : result) {
					Log.i(TAG+"List<GetGuideBean>",
							"����="+getGuideBean.getSgid()+
							",����="+getGuideBean.getTitle()+
							",ָ������="+getGuideBean.getSgtype()+
							",ָ������="+getGuideBean.getSgcontent()+
							",ָ����Ƶ��ַ="+getGuideBean.getUrl()+
							",ָ�������="+getGuideBean.getCreater()+
							",ָ�����ʱ��="+getGuideBean.getCreatetime()+
							",�Ƿ�ͨ�����="+getGuideBean.getWonori()+
							",�Ƿ�����="+getGuideBean.getWonlock()+
							",�����="+getGuideBean.getCtr()+
							",����="+getGuideBean.getSort()+
							",״̬="+getGuideBean.getStatus()+
							",ָ�����="+getGuideBean.getGenre()+
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
				List<GetSportBean> dataset=WebGetSports.getGetSportsData("");//��ȡ��Web�˵�����,��ʱ��ֱ�Ӵ�ӡLog�ڿ���̨����	
				for (GetSportBean getSportBean : dataset) {
					Log.i(TAG+"SportInfo-���","����="+getSportBean.getSportid()
							+",�˶�����="+getSportBean.getSportname()
							+",�Ƿ��Ƽ�="+getSportBean.isIsrec()+"\n");
				}
			};
		}.start(); 
	}
	
	/** GetVenues*/
	public void GetVenues(View v){
		new Thread(){
			public void run() {
				//��Ҫ��Service�л�ȡ��ȫ����Ϣ
				List<GetVenuesInfoBean> result=WebGetVenues.getGetVenuesData("");
				if(result!=null){
					Log.i("��Service���ص�����",result.toArray().toString());
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
