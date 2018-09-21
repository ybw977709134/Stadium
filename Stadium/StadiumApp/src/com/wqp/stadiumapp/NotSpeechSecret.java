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

import com.wqp.stadiumapp.adapter.NotSpeechSecretAndBrokeTheSpaceAdapter;
import com.wqp.view.XListView;
import com.wqp.view.XListView.IXListViewListener;
import com.wqp.webservice.WebGetGuide; 
import com.wqp.webservice.entity.GetGuideBean; 

public class NotSpeechSecret extends Activity implements IXListViewListener, OnClickListener{
	private static String TAG="NotSpeechSecret";
	private ImageView notspeechscret_back_arrow;
	private XListView notspeechscret_listview;
	private List<Map<String,Object>> mListViewData;
	private NotSpeechSecretAndBrokeTheSpaceAdapter mNotSpeechSecretAdapter;
	
	private ProgressDialog mProgressDialog;//���ȶԻ���
	private List<GetGuideBean> WebDataSet=null;
	private int WebDataSetCount=0;//�洢��ѯ�������ݼ��ܳ���
	private int mTotalPage=0;//���������ܳ�����Ҫ��ҳ��ʾ����
	private int TEN=10;//Ĭ��ÿҳ��ʾ�����ݳ���
	private int current=0;//��ǰҳ,Ĭ��Ϊ��һҳ  mTotalPage - currentIndex 
	
	/** ������Ϣ������*/
	private Handler mHandler=new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x11://���߳��в�ѯ�����ݼ����ش˴� 
				WebDataSet=(List<GetGuideBean>) msg.obj; 
				WebDataSetCount=WebDataSet.size();//���÷������ݼ����ܳ���
				mTotalPage=WebDataSetCount%10==0 ? WebDataSetCount/10 : WebDataSetCount/10+1;//������Ҫ�ּ�ҳ��ʾ���� 
//				mListViewData.clear();//������ݻ���
//				current=0;//���°ѵ�ǰҳ������ΪĬ��ֵ0
				getListViewData();//������� 
				mNotSpeechSecretAdapter.notifyDataSetChanged();//����ListView������������  
				break;
			case 0x22://��ʶ��ȡ��������Ϊ�� 
				Toast.makeText(NotSpeechSecret.this, "û���ҵ�Ŷ!", 0).show();
			case 0x33://��ʶ��������ʱ�Ѿ�û��������
				Toast.makeText(NotSpeechSecret.this, "��,û����Ŷ!", 0).show();	
			default:
				break;
			} 
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.not_speech_scret);
		notspeechscret_back_arrow=(ImageView) findViewById(R.id.notspeechscret_back_arrow);
		notspeechscret_listview=(XListView) findViewById(R.id.notspeechscret_listview);
		
		//ΪXListView������ˢ���¼�,����������
		notspeechscret_listview.setPullLoadEnable(true);
		notspeechscret_listview.setPullRefreshEnable(false);
		notspeechscret_listview.toggleHeadView=true;//��ʹ��ͷ��ˢ�¹���
		
		//Ϊ���ذ�ť���¼�
		notspeechscret_back_arrow.setOnClickListener(this); 
		conditionQuery();
		initXListView();
		
	}
	
	public void initXListView(){ 
		mListViewData=new ArrayList<Map<String,Object>>();
		
		//���������������
		mNotSpeechSecretAdapter=new NotSpeechSecretAndBrokeTheSpaceAdapter(
				NotSpeechSecret.this, 
				getListViewData(), 
				R.layout.notspeechsecret_listview_item, 
				new String[]{"Title","SGContent"}, 
				new int[]{R.id.notspeechsecret_title,R.id.notspeechsecret_content});
		//����������������
		notspeechscret_listview.setAdapter(mNotSpeechSecretAdapter);
		//ΪXListView���¼�
		notspeechscret_listview.setOnItemClickListener(new OnItemClickListener(){ 
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) { 
//				Toast.makeText(NotSpeechSecret.this, position+"", 0).show(); 
				Intent intent=new Intent(NotSpeechSecret.this,NotSpeechScrectDetails.class);
				Bundle extras=new Bundle();
				extras.putSerializable("GetGuideBean", WebDataSet.get(position-1));
				intent.putExtras(extras);
				NotSpeechSecret.this.startActivity(intent);//��ת������˵�����ܵ��������
			} 
		});
		
	}
	
	/** ��ListView�����������,Ĭ��ÿҳֻ��ʾ10������ */
	private List<Map<String,Object>> getListViewData(){
		if(WebDataSet==null || WebDataSet.size() == 0){//���WebDataSet���ݼ���Ϊ�գ����ҳ��ȵ���0�����û����Ѳ���
//			Toast.makeText(NotSpeechSecret.this, "û���ҵ�Ŷ", 0).show();
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
					map.put("Title", "����:"+WebDataSet.get(i).getTitle());//����	
					map.put("SGContent", "����:"+WebDataSet.get(i).getSgcontent());//����
					mListViewData.add(map); 
				} 
				current++;//�ѵ�ǰҳ��1 
			}  
			Log.i("��ǰ�����ݳ�����:",mListViewData.size()+"");
		}  
		return mListViewData; 
	}
	
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.notspeechscret_back_arrow://����˷��ذ�ť
			finish();
			break; 
		} 
	}
	
	/**
	 * ���û�����˳��ݲ�ѯҳ��Ĳ�ѯ��ť֮�����ô˷���
	 */
	public void conditionQuery() { 
			mProgressDialog=new ProgressDialog(NotSpeechSecret.this);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setMessage("������...");
			mProgressDialog.setIndeterminate(true); 
			mProgressDialog.show();
			new Thread(){   
				public void run() {
					try {   //����˵�����ܣ������ǣ�SGType='����'
						List<GetGuideBean> results=WebGetGuide.getGetGuideData("SGType='����'");
						if(results!=null){
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
				getListViewData();//�������
				mNotSpeechSecretAdapter.notifyDataSetChanged();//����ListView������������
				onLoad(); 
			} 
		}, 2000);  
	}
	
	/** ֹͣListView�ײ�ˢ�� */
	private void onLoad() {
		notspeechscret_listview.stopRefresh();
		notspeechscret_listview.stopLoadMore();
		notspeechscret_listview.setRefreshTime("�ո�");
		notspeechscret_listview.invalidate();
	}

	
}
