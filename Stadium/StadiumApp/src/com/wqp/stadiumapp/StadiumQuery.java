package com.wqp.stadiumapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 

import android.app.ProgressDialog;
import android.content.Intent; 
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wqp.stadiumapp.adapter.StadiumQueryListViewAdapter;
import com.wqp.stadiumapp.utils.CommonSlidMenu;
import com.wqp.stadiumapp.utils.WebServiceUtils;
import com.wqp.view.XListView;
import com.wqp.view.XListView.IXListViewListener;
import com.wqp.webservice.WebGetVenues;
import com.wqp.webservice.entity.GetVenuesInfoBean;

public class StadiumQuery extends Fragment implements View.OnClickListener,IXListViewListener{
	private static String TAG="StadiumQuery"; 
	private StringBuilder sql=new StringBuilder();
	private ImageView stadium_query_back_arrow;
	private Button stadium_query_button;
	private EditText stadium_query_searchbox_content;
	private XListView stadium_query_listview;
	private List<Map<String,Object>> mListViewData;
	private View mListViewHeader;//�����ͼ��listviewͷ��������
	private StadiumQueryListViewAdapter mStadiumQueryListViewAdapter;
	private ProgressDialog mProgressDialog;//���ȶԻ���
	
	private List<GetVenuesInfoBean> WebDataSet=null;
	private int WebDataSetCount=0;//�洢��ѯ�������ݼ��ܳ���
	private int mTotalPage=0;//���������ܳ�����Ҫ��ҳ��ʾ���� 
	private int current=0;//��ǰҳ,Ĭ��Ϊ��һҳ  mTotalPage - currentIndex 
	private int TEN=10;//Ĭ��ÿҳ��ʾ�����ݳ���
	
	
	/** ������Ϣ������*/
	private Handler mHandler=new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x11://���߳��в�ѯ�����ݼ����ش˴� 
				WebDataSet=(List<GetVenuesInfoBean>) msg.obj; 
				WebDataSetCount=WebDataSet.size();//���÷������ݼ����ܳ���
				mTotalPage=WebDataSetCount%10==0 ? WebDataSetCount/10 : WebDataSetCount/10+1;//������Ҫ�ּ�ҳ��ʾ���� 
				mListViewData.clear();//������ݻ���
				current=0;//���°ѵ�ǰҳ������ΪĬ��ֵ0
				getListViewData();//������� 
				mStadiumQueryListViewAdapter.notifyDataSetChanged();//����ListView������������  
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
		View view=inflater.inflate(R.layout.stadium_query, container,false);
		initView(view);//��ҳ��������г�ʼ�� 
		initListView();//��listView������ݲ���
		
		return view;
	}
	
	/** Ϊ���ذ�ť���¼� */
	private void initView(View view){
		stadium_query_back_arrow=(ImageView) view.findViewById(R.id.stadium_query_back_arrow); 
		stadium_query_listview=(XListView) view.findViewById(R.id.stadium_query_listview);
		
		//ΪXlistview���ͷ��������
		mListViewHeader=LayoutInflater.from(getActivity()).inflate(R.layout.stadium_searchbox, null);
		stadium_query_button=(Button) mListViewHeader.findViewById(R.id.stadium_searchbox_button);
		stadium_query_searchbox_content=(EditText) mListViewHeader.findViewById(R.id.stadium_searchbox_content);
		stadium_query_listview.addHeaderView(mListViewHeader);
		
		//ΪXListView������ˢ���¼�,����������
		stadium_query_listview.setPullLoadEnable(true);
		stadium_query_listview.setPullRefreshEnable(false);
		stadium_query_listview.toggleHeadView=true;//��ʹ��ͷ��ˢ�¹��� 
		
		//Ϊ���غͲ�ѯ��ť���¼�
		stadium_query_back_arrow.setOnClickListener(this);  
		stadium_query_button.setOnClickListener(this); 
	}
	
	/** ��listview������ݲ��� */
	private void initListView(){  
		//Ϊlistview���������
		mListViewData=new ArrayList<Map<String,Object>>(); 
		
		mStadiumQueryListViewAdapter=new StadiumQueryListViewAdapter(
				getActivity(),
				getListViewData(),
				R.layout.stadium_query_listview_item,
				new String[]{"VenuesImager","VenuesName","Address","ReservePhone","RideRoute","VenuesEnvironment"},
				new int[]{R.id.stadium_query_listview_image,
						  R.id.stadium_query_listview_title,
						  R.id.stadium_query_listview_address,
						  R.id.stadium_query_listview_telephone,
						  R.id.stadium_query_listview_route,
						  R.id.stadium_query_listview_environment});
		
		stadium_query_listview.setAdapter(mStadiumQueryListViewAdapter);
		stadium_query_listview.setXListViewListener(this);//���ù�������
		
		//Ϊlistview�󶨵���¼�
		stadium_query_listview.setOnItemClickListener(new OnItemClickListener() { 
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) { 
//				Toast.makeText(getActivity(), WebDataSet.get(position-2).getVenuesname()+"", 0).show(); 
				Intent intent=new Intent(getActivity(),StadiumSpecify.class);
				Bundle extras=new Bundle();
				extras.putSerializable("VenuesInfo", WebDataSet.get(position-2));
				intent.putExtras(extras);
				getActivity().startActivity(intent);//��ת�������������
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.stadium_query_back_arrow://Ϊ���ذ�ť���¼�
			CommonSlidMenu.mSlidMenu.toggle();//��߲˵�����
			break;
		case R.id.stadium_searchbox_button://��ѯ��ť�Աߵ������,//����������ѯ 
//			Toast.makeText(getActivity(), "��ѯ", 0).show();
			conditionQuery();//�÷����������̴߳�web�˼�������
		break;
		} 
	}

	/**
	 * ���û�����˳��ݲ�ѯҳ��Ĳ�ѯ��ť֮�����ô˷���
	 */
	public void conditionQuery() {
		final String values=stadium_query_searchbox_content.getText().toString().trim();
		if(!TextUtils.isEmpty(values)){//�ж��û��ڵ����ѯ֮ǰ�Ƿ������˲��� 
			mProgressDialog=new ProgressDialog(getActivity());
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setMessage("������...");
			mProgressDialog.setIndeterminate(true); 
			mProgressDialog.show();
			new Thread(){  
				public void run() {
					try {
//						JSONObject object=new JSONObject();
//						object.put("VenuesID", 0);
//						object.put("VenuesName", values); //����ٸ����û�����ĳ������ƽ��в�ѯ 
						sql.delete(0, sql.length());//���StringBuilder 
						sql.append("vi.VenuesName like '%"+values+"%'");  //����ģ��������ѯ
						Log.i("�û�����Ĳ�ѯ������:",sql.toString());
						List<GetVenuesInfoBean> results=WebGetVenues.getGetVenuesData(sql.toString());
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
					map.put("VenuesImager", WebServiceUtils.IMAGE_URL+WebDataSet.get(i).getVenuesimage());//����ͼƬ,������ͼƬ��Uri·����Ȼ��ListView���������н����첽���ػ�ȡ��ͼƬ	
					map.put("VenuesName", WebDataSet.get(i).getVenuesname());//��������
					map.put("Address", "��ַ��"+WebDataSet.get(i).getAddress());//��ϸ��ַ
					map.put("ReservePhone", "�绰��"+WebDataSet.get(i).getReservephone());//Ԥ���绰
					map.put("RideRoute", "·�ߣ�"+WebDataSet.get(i).getRideroute());//�˳�·��
					map.put("VenuesEnvironment", "����������"+WebDataSet.get(i).getVenuesenvironment());//������������ 
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
				mStadiumQueryListViewAdapter.notifyDataSetChanged();//����ListView������������
				onLoad(); 
			} 
		}, 2000);  
	}
	
	/** ֹͣListView�ײ�ˢ�� */
	private void onLoad() {
		stadium_query_listview.stopRefresh();
		stadium_query_listview.stopLoadMore();
		stadium_query_listview.setRefreshTime("�ո�");
		stadium_query_listview.invalidate();
	}
	
	@Override
	public void onDestroy() {
		stadium_query_searchbox_content.setText("");
		super.onDestroy();
	}
}
