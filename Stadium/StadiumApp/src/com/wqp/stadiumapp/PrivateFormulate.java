package com.wqp.stadiumapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment; 
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener; 
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener; 
import android.widget.ImageView;  
import android.widget.Toast; 
import com.wqp.stadiumapp.adapter.PrivateFormulateAdapter; 
import com.wqp.stadiumapp.utils.CommonSlidMenu; 
import com.wqp.stadiumapp.utils.ToolsHome;
import com.wqp.view.XListView;
import com.wqp.view.XListView.IXListViewListener;  
import com.wqp.webservice.WebGetVenuesExpert;  
import com.wqp.webservice.entity.GetVenuesExpertBean; 

public class PrivateFormulate extends Fragment implements OnClickListener, IXListViewListener{
	private static String TAG="PrivateFormulate";
	private ImageView private_formulate_back_arrow,private_formulate_shared;
	private XListView private_formulate_listview; 
	private List<Map<String,Object>> mListViewData;
	private PrivateFormulateAdapter mPrivateFormulateAdapter;  

	private ProgressDialog mProgressDialog;//���ȶԻ��� 
	private List<GetVenuesExpertBean> WebDataSet=null;
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
				WebDataSet=(List<GetVenuesExpertBean>) msg.obj; 
				WebDataSetCount=WebDataSet.size();//���÷������ݼ����ܳ���
				mTotalPage=WebDataSetCount%10==0 ? WebDataSetCount/10 : WebDataSetCount/10+1;//������Ҫ�ּ�ҳ��ʾ���� 
				mListViewData.clear();//������ݻ���
				current=0;//���°ѵ�ǰҳ������ΪĬ��ֵ0
				getListViewData();//������� 
				mPrivateFormulateAdapter.notifyDataSetChanged();//����ListView������������  
				break;
			case 0x22://��ʶ��ȡ��������Ϊ�� 
				Toast.makeText(getActivity(), "û���ҵ�Ŷ!", 0).show();
			case 0x33://��ʶ��������ʱ�Ѿ�û��������
				Toast.makeText(getActivity(), "��,û����Ŷ!", 0).show();	
			default:
				break;
			} 
		};
	};
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.private_formulate, container,false); 
		initView(view);//��ҳ��������г�ʼ��
		initListView();;//��XListView������ݲ���
		
		conditionQuery();//�ӷ���˻�ȡ����
		return view;
	}
	
	private void initView(View view){
		private_formulate_back_arrow=(ImageView) view.findViewById(R.id.private_formulate_back_arrow);
		private_formulate_shared=(ImageView) view.findViewById(R.id.private_formulate_shared);
		
		private_formulate_listview=(XListView) view.findViewById(R.id.private_formulate_listview); 
		//ΪXListView������ˢ���¼�,����������
		private_formulate_listview.setPullLoadEnable(true);
		private_formulate_listview.setPullRefreshEnable(false);
		private_formulate_listview.toggleHeadView=true;//��ʹ��ͷ��ˢ�¹��� 
		
		private_formulate_back_arrow.setOnClickListener(this);
		private_formulate_shared.setOnClickListener(this);   
	}

	//��XListView��ʼ�����������,�����¼�
	private void initListView(){
		mListViewData=new ArrayList<Map<String,Object>>();
		
		mPrivateFormulateAdapter =new PrivateFormulateAdapter(getActivity(), 
				getListViewData(),
				R.layout.private_formulate_listview_item, 
				new String[]{"Nickname","Sex","ZhuanJiaType","VenuesName","Hobby"},
				new int[]{
						R.id.private_formulate_nickname,
						R.id.private_formulate_sex,
						R.id.private_formulate_zhuanjiatype,
						R.id.private_formulate_venuestype,
						R.id.private_formulate_hobby});
		//ʹ���������������
		private_formulate_listview.setAdapter(mPrivateFormulateAdapter); 
		private_formulate_listview.setXListViewListener(this);//���ù�������
		
		private_formulate_listview.setOnItemClickListener(new OnItemClickListener(){ 
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				
			} 
		});
	} 
	
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.private_formulate_back_arrow://���ذ�ť
			CommonSlidMenu.mSlidMenu.toggle();//��߲˵�����
			break;
		case R.id.private_formulate_shared://����ͼ��
			Toast.makeText(getActivity(), "����", 0).show();
			break; 
		} 
	} 

	/** ��ListView�����������,Ĭ��ÿҳֻ��ʾ10������ */
	private List<Map<String,Object>> getListViewData(){
		if(WebDataSet==null || WebDataSet.size() == 0){//���WebDataSet���ݼ���Ϊ�գ����ҳ��ȵ���0�����û����Ѳ���
//			Toast.makeText(getActivity(), "û���ҵ�Ŷ", 0).show();
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
					map.put("Nickname", "�ǳ�:"+WebDataSet.get(i).getNickName());//ר���ǳ�
					map.put("Sex", "�Ա�:"+WebDataSet.get(i).getSex());//ר���Ա�
					map.put("ZhuanJiaType", "ר������:"+WebDataSet.get(i).getZhuanJiaType());//ר������
					map.put("VenuesName", "��������:"+WebDataSet.get(i).getVenuesName());//��������
					map.put("Hobby", "�˶���Ŀ:"+WebDataSet.get(i).getHobby());//�˶���Ŀ 
					mListViewData.add(map); 
				} 
				current++;//�ѵ�ǰҳ��1 
			} 
		}
		Log.i("��ǰ�����ݳ�����:",mListViewData.size()+"");
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
				getListViewData();//�������
				mPrivateFormulateAdapter.notifyDataSetChanged();//����ListView������������
				onLoad(); 
			} 
		}, 2000);  
	}
	
	/** ֹͣListView�ײ�ˢ�� */
	private void onLoad() {
		private_formulate_listview.stopRefresh();
		private_formulate_listview.stopLoadMore();
		private_formulate_listview.setRefreshTime("�ո�");
		private_formulate_listview.invalidate();
	}
	
	/**
	 * ���û������˽�˶���ҳ��Ĳ�ѯ��ť֮�����ô˷���
	 */
	public void conditionQuery() { 
			mProgressDialog=new ProgressDialog(getActivity());
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setMessage("������...");
			mProgressDialog.setIndeterminate(true); 
			mProgressDialog.show();
			new Thread(){   
				public void run() {
					try {   //��ȡ�˶�ר����Ϣ(˽�˶���)�����β�ѯ�����˶�ר�� ,���������E.ExpertID =1 ��ȡĳ���˶�ר�ҵ���Ϣ 	
						List<GetVenuesExpertBean> results=WebGetVenuesExpert.getGetVenuesExpertData("");
						
						//��ȡ�˶�ר�Ҷ�Ӧ���˶���Ϣ
						if(results!=null){
							for (GetVenuesExpertBean getVenuesExpertBean : results) {
								String hobby=getVenuesExpertBean.getHobby();
								if(hobby!=null){
									String[] servial=hobby.split(",");
									StringBuilder sb=new StringBuilder();
									for(int i=0;i<servial.length;i++){
										//���� ���������SportID=1  ��ȡĳ���˶�����Ϣ
										String str=ToolsHome.getSportName(Integer.valueOf(servial[i]));
										if(str!=null){
											sb.append(str+" ");
										} 
//										List<GetSportBean> oo=WebGetSports.getGetSportsData("SportID="+servial[i]);
//										sb.append(oo.get(0).getSportname()+" ");
									}
									getVenuesExpertBean.setHobby(sb.toString());//�ٰѻ�ȡ����ϸ�������ûض�����
								} 
							}
						} 
						
						
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
  
}
