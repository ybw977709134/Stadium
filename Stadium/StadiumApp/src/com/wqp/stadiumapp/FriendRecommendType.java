package com.wqp.stadiumapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message; 
import android.util.Log; 
import android.view.View; 
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.wqp.stadiumapp.JoinActDetails;
import com.wqp.stadiumapp.R;
import com.wqp.stadiumapp.adapter.FriendRecommendAdapter;
import com.wqp.stadiumapp.utils.ToolsHome;
import com.wqp.stadiumapp.utils.ToolsNavigate; 
import com.wqp.view.XListView;
import com.wqp.view.XListView.IXListViewListener;
import com.wqp.webservice.WebGetArtInfo; 
import com.wqp.webservice.entity.GetActInfoBean; 

/** Ӧս�б�ҳ�� ,����ǵ������������ViewPager���������֮���ֱ�ӽ��뵽�˽���*/
public class FriendRecommendType extends Activity implements IXListViewListener, OnClickListener{ 
	private static String TAG="FriendRecommendFragment";
	private ImageView friend_recommend_type_back_arrow;
	private XListView mXListView;
	private List<Map<String,Object>> mListViewData;//Ϊlistview�����������
	private FriendRecommendAdapter mFriendRecommendAdapter;
	@SuppressWarnings("unused")
	private ImageView mImageViewState;//Ӧս��Ŀ�����ұ��и����ͼ�꣬���û��ɹ���ӻ֮�����Ҫ�������ͼ��
	
	private ProgressDialog mProgressDialog;//���ȶԻ���
	private List<GetActInfoBean> WebDataSet=null;
	private int WebDataSetCount=0;//�洢��ѯ�������ݼ��ܳ���
	private int mTotalPage=0;//���������ܳ�����Ҫ��ҳ��ʾ����
	private int TEN=10;//Ĭ��ÿҳ��ʾ�����ݳ���
	private int current=0;//��ǰҳ,Ĭ��Ϊ��һҳ  mTotalPage - currentIndex 
	
	private int sporttype;//�û���������ViewPager�е�����˶����ͱ��
	
	/** ������Ϣ������*/
	private Handler mHandler=new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x11://���߳��в�ѯ�����ݼ����ش˴� 
				WebDataSet=(List<GetActInfoBean>) msg.obj; 
				WebDataSetCount=WebDataSet.size();//���÷������ݼ����ܳ���
				mTotalPage=WebDataSetCount%10==0 ? WebDataSetCount/10 : WebDataSetCount/10+1;//������Ҫ�ּ�ҳ��ʾ���� 
				initListView();//Ϊlistview������ݲ����¼�  
				mFriendRecommendAdapter.notifyDataSetChanged();//����ListView������������  
				break;
			case 0x22://��ʶ��ȡ��������Ϊ�� 
				Toast.makeText(FriendRecommendType.this, "û���ҵ�Ŷ!", 0).show();
				break;
			case 0x33://��ʶ��������ʱ�Ѿ�û��������
				Toast.makeText(FriendRecommendType.this, "��,û����Ŷ!", 0).show(); 
				break;  
			};
		};
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_recommend_type); 
		sporttype=getIntent().getIntExtra("text", 0); 
		conditionQuery();//�ӷ���˼���Ӧս����   
		
		friend_recommend_type_back_arrow=(ImageView) findViewById(R.id.friend_recommend_type_back_arrow);
		mXListView=(XListView)findViewById(R.id.friend_recommend_type_xlistview);
		
		//ΪListView������ˢ���¼�,����������
		mXListView.setPullLoadEnable(true);
		mXListView.setPullRefreshEnable(false);//ͷ��ˢ�¹ر�
		mXListView.toggleHeadView=true;//��ʹ��ͷ��ˢ�¹��� 
		
		friend_recommend_type_back_arrow.setOnClickListener(this);
	}
	
	/** Ϊlistview������ݲ����¼�*/
	private void initListView(){
		mListViewData=new ArrayList<Map<String,Object>>();
		mFriendRecommendAdapter=new FriendRecommendAdapter(
				FriendRecommendType.this,
				getListViewData(),
				R.layout.friend_recommend_xlistview_item_type,
				new String[]{"VenuesName","SportType","StartTime","EndTime","TakeCount"},
				new int[]{
					R.id.friend_recommend_xlistview_venuesname_type,//��������
					R.id.friend_recommend_xlistview_sporttype_type,//�˶�����
					R.id.friend_recommend_xlistview_starttime_type,//��ʼʱ��
					R.id.friend_recommend_xlistview_endtime_type,//����ʱ��
					R.id.friend_recommend_xlistview_takecount_type,//��������
					R.id.friend_recommend_xlistview_state_type}); //��ӻ��״̬
		mXListView.setAdapter(mFriendRecommendAdapter);
		mXListView.setXListViewListener(this);//Ϊlistview��������ˢ���¼�
		mXListView.setOnItemClickListener(new MyOnItemClickListener()); //Ϊlistview�󶨵���¼�
	}
	
	/**��XListView�󶨵���¼�*/
	private class MyOnItemClickListener implements OnItemClickListener{ 
		@Override
		public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) { 
//			Toast.makeText(FriendRecommendType.this, position+"", 0).show(); 
			
			//��ת�������ҳ����ϸ����
			Intent itemIntent=new Intent(FriendRecommendType.this,JoinActDetails.class);
			Bundle zzextras=new Bundle();
			zzextras.putSerializable("GetActInfoBean", WebDataSet.get(position-1));
			itemIntent.putExtras(zzextras);
			FriendRecommendType.this.startActivity(itemIntent);//Я��������ת��JoinActDetailsӦս����ҳ�� 
		} 
	}
	
	
	/**
	 * ���û�����˳��ݲ�ѯҳ��Ĳ�ѯ��ť֮�����ô˷���
	 */
	public void conditionQuery() { 
			if(!ToolsNavigate.isNetworkAvailable(FriendRecommendType.this)){ return;}//�жϵ�ǰ�����Ƿ����
			mProgressDialog=new ProgressDialog(FriendRecommendType.this);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setMessage("������...");
			mProgressDialog.setIndeterminate(true); 
			mProgressDialog.show();
			new Thread(){  
				public void run() {
					try { 
						//����ActTypeID=5�ǹ涨д�ģ��Ѿ��̶�������,��ʱ���������Ϳ��Ի�ȡ��ȫ������
						Log.i(TAG,"�û�����Ĳ�ѯ�Ĳ�����:SportType="+sporttype);
						 List<GetActInfoBean> results=WebGetArtInfo.getGetArtInfoData("SportType="+sporttype);
						 //�����ݼ������и�����ΪSportType������
						 if(results!=null){
							 for (GetActInfoBean getActInfoBean : results) {
								 //����SportID=where�����в�ѯ�����صľ�ֻ��һ������ 
								 String str=ToolsHome.getSportName(getActInfoBean.getSporttype());
								 if(str!=null){
									 getActInfoBean.setSporttypestring(str); 
								 } 
							} 
						 } 
//						 if(results!=null){
//							 for (GetActInfoBean getActInfoBean : results) {
//								 //����SportID=where�����в�ѯ�����صľ�ֻ��һ������
//								 List<GetSportBean> oo = WebGetSports.getGetSportsData("SportID="+getActInfoBean.getSporttype().intValue());
//								 if(oo!=null){
//									 getActInfoBean.setSporttypestring(oo.get(0).getSportname());
//								 } 
//							}  
//						 }
						if(results!=null && results.size() > 0 ){
							Message msg=new Message();
							msg.what=0x11;//��ʶ������ȡ��������
							msg.obj=results;
							mHandler.sendMessage(msg); 
							Log.i(TAG,"Web���̼߳������ݽ���,���Ѿ��������ݵ�Handler��");
						}else{
							mHandler.sendEmptyMessage(0x22);//���ͱ�ʶ��ȡ��������Ϊ��
						}
					} catch (Exception e) {  
						e.printStackTrace();
					}finally{
						mProgressDialog.dismiss();
					}
				};
			}.start(); 
	}

	/** ��ListView�����������,Ĭ��ÿҳֻ��ʾ10������ */
	private List<Map<String,Object>> getListViewData(){
		if(WebDataSet==null || WebDataSet.size() == 0){//���WebDataSet���ݼ���Ϊ�գ����ҳ��ȵ���0�����û����Ѳ���
//			Toast.makeText(FriendRecommendType.this, "û���ҵ�Ŷ", 0).show();
		}else if(WebDataSet!=null && WebDataSet.size() > 0){//���WebDataSet���ݼ���Ϊ�գ����ҳ��ȴ���0�����������ݲ���		
			/** �����ǰҳ�� С�� ��ҳ��,Ĭ�ϵ�ǰҳ��0��ʼ����,mTotalPageΪ�����ܹ��ֶ���ҳ*/
			if(current < mTotalPage){ 
				//����1��i��ֵ���ڵ�ǰҳ����ÿҳ������ʾ����Ŀ����,Ĭ����10��
				//����2�� var��ֵ����
				int var=0;
				//������ݳ�����Ĭ��10������
				if(WebDataSetCount < 10){ var=WebDataSetCount; }
				else{ var = ((WebDataSetCount-(current*TEN))/TEN)>0 ? (current+1)*TEN : ((current+1)*TEN + WebDataSetCount%TEN)-TEN;}
				for (int i=current * TEN;i < var; i++) {
					Map<String,Object> map=new HashMap<String,Object>(); 
					map.put("VenuesName", "��������:"+WebDataSet.get(i).getVenuesname());//��������	
					map.put("SportType", "�˶�����:"+WebDataSet.get(i).getSporttypestring());//�˶����� 
					map.put("StartTime", "��ʼʱ��:"+WebDataSet.get(i).getStarttime());//��ʼʱ��
					map.put("EndTime", "����ʱ��:"+WebDataSet.get(i).getEndtime());//����ʱ��
					map.put("TakeCount", "��������:"+WebDataSet.get(i).getTakecount()+"��");//��������
					map.put("Imgstate", WebDataSet.get(i).isImgstate());//ͼƬ��״̬
					map.put("ActID", WebDataSet.get(i).getActid().intValue());//�˲�����Ϊ�˴����������������ʹ�õ�
					mListViewData.add(map); 
				} 
				current++;//�ѵ�ǰҳ��1 
			} 
			Log.i("��ǰ�����ݳ�����:",mListViewData.size()+"");
		} 
		return mListViewData; 
	}

	/** ͷ������ˢ��,��δʹ��*/
	@Override
	public void onRefresh() { 
		
	}

	
	/**�����ListView�ײ����ظ���ʹ��,����ʹ�õ���Handler���м�������*/
	@Override
	public void onLoadMore() { 
		mHandler.postDelayed(new Runnable(){  
			@Override
			public void run() { 
				if(current==mTotalPage || WebDataSetCount==0){
					mHandler.sendEmptyMessage(0x33);//�����Ѿ�û�������ݵı�ʶ
					Log.i(TAG,"��ListView������Ҳ��Handler������Ϣ��ȥ��");
					onLoad();
					return;
				}
				getListViewData();
				mFriendRecommendAdapter.notifyDataSetChanged();
				onLoad();
			} 
		}, 2000);
	}
	
	/** ֹͣListView�ײ�ˢ�� */
	private void onLoad() {
		mXListView.stopRefresh();
		mXListView.stopLoadMore();
		mXListView.setRefreshTime("�ո�");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.friend_recommend_type_back_arrow://����˷��ذ�ť
			finish();
			break; 
		} 
	} 
	

}
 