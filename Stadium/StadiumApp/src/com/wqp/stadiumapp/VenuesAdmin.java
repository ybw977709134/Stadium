package com.wqp.stadiumapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.wqp.stadiumapp.adapter.VenuesAdminAdapter;
import com.wqp.stadiumapp.utils.UserGlobal;
import com.wqp.stadiumapp.utils.WebServiceUtils;
import com.wqp.webservice.entity.GetVenuesInfoBean;

/**���ݹ���Ա��¼�Ľ���*/
public class VenuesAdmin extends Activity {
	private static final String TAG = "VenuesAdmin"; 
	private ListView venuesadminlist; 
	private VenuesAdminAdapter mVenuesAdminAdapter;
	
	private List<GetVenuesInfoBean> WebDataSet=null;
	private List<Map<String,Object>> mListViewData;//����listviewÿ����Ŀ������
	
	//������Ϣ������
	private Handler mHandler=new Handler(){
		 @SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case 0x11://�ɹ����յ�����˵�����
				WebDataSet=(List<GetVenuesInfoBean>) msg.obj;
				showData();//������ListView������ʾ����
				break;
			case 0x12://��ȡ������ʧ��
				Toast.makeText(VenuesAdmin.this, "�����޻�ȡ����������", 0).show();
				break;
			} 
		 };
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.venuesadmin);
		venuesadminlist=(ListView) findViewById(R.id.venuesadmin_list);
		
		//�������ݼ����û��ύ��Ϣ����
		UserGlobal.admin_intent.setAction("com.wqp.stadiumapp.AdminService");
		startService(UserGlobal.admin_intent);
		
		//�����̴߳ӷ���˻���ͨ�û����뵽�ĳ�������
		openThredOpertion();
		
	}
	
	/** �������߳̽��дӷ���˻�ȡ���ݲ���*/
	private void openThredOpertion() {
		new Thread(){
			public void run() {
				//���÷���˻�ȡ�����û��������ݽӿ�
				
				
				mHandler.sendMessage(null);	
			};
		}.start(); 
	}

	/**
	 * ��ʾ��ǰ�����û����յ��Ķ�����Ϣ,��������Ǵӷ�������ȡ���ġ�
	 */
	private void showData() {
		mListViewData=new ArrayList<Map<String,Object>>();
		mVenuesAdminAdapter=new VenuesAdminAdapter(
				VenuesAdmin.this,
				getListViewData(),
				R.layout.venues_admin_listview_item,
				new String[]{"VenuesImager","VenuesName","Address","ReservePhone","RideRoute","VenuesEnvironment"},	
				new int[]{R.id.venuesadmin_listview_image,
						  R.id.venuesadmin_listview_title,
						  R.id.venuesadmin_listview_address,
						  R.id.venuesadmin_listview_telephone,
						  R.id.venuesadmin_listview_route, 
						  R.id.venuesadmin_listview_environment});
		
		venuesadminlist.setAdapter(mVenuesAdminAdapter);
		venuesadminlist.setOnItemClickListener(new OnItemClickListener(){ 
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//���ݹ����û������ListView���е�ĳһ��Ŀ֮�����Ӧ�˲���
				int venues_id=((Integer) mListViewData.get(position).get("VenuesID")).intValue();
				Log.i(TAG,"�����û�ѡ�񵽵ĳ��ݶ�ӦID�ǣ�"+venues_id);
			} 
		});
	}
	
	/** ��ListView�����������,Ĭ��ÿҳֻ��ʾ10������ */
	private List<Map<String,Object>> getListViewData(){ 
		if(WebDataSet!=null)Log.i(TAG,"����WebDataSet�ĳ���Ϊ:"+WebDataSet.size());
		
		if(WebDataSet!=null && WebDataSet.size() == 0){//���WebDataSet���ݼ���Ϊ�գ����ҳ��ȵ���0�����û����Ѳ���
//			Toast.makeText(getActivity(), "û���ҵ�Ŷ", 0).show();
		}else if(WebDataSet!=null && WebDataSet.size() > 0){ 
				for (int i=0; i<WebDataSet.size(); i++) {
					Log.i(TAG,"iֵ����="+i);
					Map<String,Object> map=new HashMap<String,Object>();
					Log.i(TAG,"��ȡ����ͼƬ·��Ϊ="+WebDataSet.get(i).getVenuesimage()); 
					map.put("VenuesImager", WebServiceUtils.IMAGE_URL+WebDataSet.get(i).getVenuesimage());//����ͼƬ,������ͼƬ��Uri·����Ȼ��ListView���������н����첽���ػ�ȡ��ͼƬ	
					map.put("VenuesName", WebDataSet.get(i).getVenuesname());//��������
					map.put("Address", "��ַ��"+WebDataSet.get(i).getAddress());//��ϸ��ַ
					map.put("ReservePhone", "�绰��"+WebDataSet.get(i).getReservephone());//Ԥ���绰
					map.put("RideRoute", "·�ߣ�"+WebDataSet.get(i).getRideroute());//�˳�·��
					map.put("VenuesEnvironment", "����������"+WebDataSet.get(i).getVenuesenvironment());//������������ 
					/**���������Ƕ�����ӵģ��������������ǲ����κ�ʹ��,ֻ���û���֮���ȡ����ID��ʹ��*/
					map.put("VenuesID", WebDataSet.get(i).getVenuesid());
					mListViewData.add(map);
				}  
		}else{
			Log.i(TAG,"����������������Ϊnull");
		}
		return mListViewData; 
	}
	
}
