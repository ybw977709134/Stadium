package com.wqp.stadiumapp;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wqp.stadiumapp.adapter.StadiumSpecifyAdapter;
import com.wqp.stadiumapp.utils.ToolsNavigate;
import com.wqp.stadiumapp.utils.UserGlobal;
import com.wqp.stadiumapp.utils.WebServiceUtils;
import com.wqp.view.XListView;
import com.wqp.view.XListView.IXListViewListener;
import com.wqp.webservice.WebGetComment;
import com.wqp.webservice.entity.GetCommentBean;
import com.wqp.webservice.entity.GetVenuesInfoBean;

public class StadiumSpecify extends Activity implements IXListViewListener, OnClickListener {  
	private static String TAG="StadiumSpecify";
	private Button order;//ҳ��ͼƬ�����ԤԼ��ť
	private ImageView stadium_news_back_arrow;//ͷ���ķ��ذ�ťͼ��
	private XListView stadium_specify_listview; 
	private View mHeadView;
	private LayoutInflater inflater;
	private List<Map<String,Object>> mListViewData;
	private StadiumSpecifyAdapter mStadiumSpecifyAdapter; 
	private Intent mIntent;//���մ�����ҳ�洫�ݹ�������ͼ
	private GetVenuesInfoBean mGetVenuesInfoBean;//���մ�����ҳ�洫�ݹ��������ݶ���
	//��������ҳ����������
	private ImageView specify_VenuesImager;//��ʾ����ͼƬ
	private TextView specify_VenuesName,specify_VenuesArea,specify_Address,specify_ReservePhone,
	specify_VenuesEmail,specify_RideRoute,specify_zhandiArea,specify_WaterTemperature,
	specify_jianzhuArea,specify_WaterDepth,specify_changdiArea,specify_Headroom,specify_GroundMaterial,
	specify_InternalFacilities,specify_Investment,specify_VenuesType,specify_VenuesEnvironment,
	specify_ResponsiblePeople,specify_Contact,specify_ContactPhone,specify_DirectorDept,specify_Briefing,
	specify_BuiltTime,specify_UseTime,specify_SeatNumber,specify_AvgNumber,specify_WhetherRecomm,specify_Map;
	

	private List<GetCommentBean> WebDataSet=null;
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
				WebDataSet= (List<GetCommentBean>) msg.obj;
				WebDataSetCount=WebDataSet.size();//���÷������ݼ����ܳ���
				mTotalPage=WebDataSetCount%10==0 ? WebDataSetCount/10 : WebDataSetCount/10+1;//������Ҫ�ּ�ҳ��ʾ���� 
				getListViewData();//�������
				mStadiumSpecifyAdapter.notifyDataSetChanged();//����ListView������������
				break;
			case 0x22://��ʶ��ȡ��������Ϊ�� 
//				Toast.makeText(StadiumSpecify.this, "û���ҵ�Ŷ!", 0).show();
			case 0x33://��ʶ��������ʱ�Ѿ�û��������
				Toast.makeText(StadiumSpecify.this, "��,û����Ŷ!", 0).show();	
			default:
				break;
			} 
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stadium_specify);
		//���մ���ʵҳ�洫�ݹ�����Intent,����Я�����г�����ϸ����
		mIntent=getIntent();
		mGetVenuesInfoBean=(GetVenuesInfoBean) mIntent.getSerializableExtra("VenuesInfo");
		
		//�����̼߳����û�������Ϣ
		newThreadLoadComment();
		
		stadium_specify_listview=(XListView) findViewById(R.id.stadium_specify_listview);
		stadium_news_back_arrow=(ImageView) findViewById(R.id.stadium_news_back_arrow);
		inflater=LayoutInflater.from(this);
		initHeaderView();//ΪListView���ͷ����ͼ
		
		//Ϊҳ��ͷ���ķ��ذ�ťͼ��󶨵���¼�
		stadium_news_back_arrow.setOnClickListener(this); 
		
	}
	
	/** ΪListView���ͷ����ͼ�����Ҷ�listview����������� */
	private void initHeaderView(){
		//��ó�������ҳ��ͷ�����������ʵ��
		mHeadView = inflater.inflate(R.layout.stadium_specify_header,null); 
		order=(Button) mHeadView.findViewById(R.id.order);
		order.setOnClickListener(this);//ΪԤԼ��ť�󶨵���¼�
		initSpecifyView(mHeadView);//�Գ�������ҳ������������������ʵ����������������ֵ
		stadium_specify_listview.addHeaderView(mHeadView); 
		
		//ΪListView������ˢ���¼�,����������
		stadium_specify_listview.setPullLoadEnable(true);
		stadium_specify_listview.setPullRefreshEnable(false);//ͷ��ˢ�¹ر�
		stadium_specify_listview.toggleHeadView=true;//��ʹ��ͷ��ˢ�¹��� 
		
		//��ListView�������
		mListViewData=new ArrayList<Map<String,Object>>(); 
		
		mStadiumSpecifyAdapter=new StadiumSpecifyAdapter(
				this, 
				getListViewData(), 
				R.layout.stadium_specify_listview_item, 
				new String[]{"Picture","UserName","Createtime","Con"}, 
				new int[]{
						R.id.stadium_specify_listview_image,
						R.id.stadium_specify_listview_name,
						R.id.stadium_specify_listview_spacetime,
						R.id.stadium_specify_listview_content});
		
		//Ϊlistview���������
		stadium_specify_listview.setAdapter(mStadiumSpecifyAdapter); 
		//ΪListViewע������¼�
		stadium_specify_listview.setXListViewListener(this);
		
	} 
	
	/** ��ListView�����������,Ĭ��ÿҳֻ��ʾ10������ */
	private List<Map<String,Object>> getListViewData(){
		if(WebDataSet!=null && WebDataSet.size() == 0){//���WebDataSet���ݼ���Ϊ�գ����ҳ��ȵ���0�����û����Ѳ���
//			Toast.makeText(StadiumSpecify.this, "û���ҵ�Ŷ", 0).show();
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
					map.put("Picture", WebDataSet.get(i).getUserid());//�û���ͷ��,������ͼƬ��Uri·����Ȼ��ListView���������н����첽���ػ�ȡ��ͼƬ	
					map.put("UserName", UserGlobal.NickName);//������(����Ǹ����û���ID)��ѯ�������ݣ���������͵��ڵ�ǰ������¼���û���
					map.put("Createtime", "����ʱ�䣺"+WebDataSet.get(i).getCreatetime());//����ʱ�� 
					map.put("Con", "�������ݣ�"+WebDataSet.get(i).getCon());//��������
					mListViewData.add(map);
				} 
				current++;//�ѵ�ǰҳ��1 
			} 
		}
		return mListViewData; 
	}
	
	private void newThreadLoadComment(){
		new Thread(){ 
			public void run() { 
					//���ݳ��ݵ�ID���в�ѯ
					String condition;
					if(mGetVenuesInfoBean!=null){
						condition="KeyID="+mGetVenuesInfoBean.getVenuesid().intValue()+" and KeyType='����'";
					}else{
						condition="";
					} 
					Log.i("�û����۲�ѯ��������:",condition);
					List<GetCommentBean> results=WebGetComment.getGetCommentData(condition);
					if(results!=null){
						Message msg=new Message();
						msg.what=0x11;//��ʶ������ȡ��������
						msg.obj=results;
						mHandler.sendMessage(msg); 
						Log.i(TAG,"Web���̼߳������ݽ���,���Ѿ��������ݵ�Handler��");
					}else{
						mHandler.sendEmptyMessage(0x22);//���ͱ�ʶ��ȡ��������Ϊ��
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
				if(!ToolsNavigate.isNetworkAvailable(getApplicationContext())){
					Toast.makeText(StadiumSpecify.this, "��,����û��Ŷ", 1).show();return;
				}
				if(current==mTotalPage || WebDataSetCount==0){
					mHandler.sendEmptyMessage(0x33);//�����Ѿ�û�������ݵı�ʶ
					Log.i(TAG,"��ListView������Ҳ��Handler������Ϣ��ȥ��");
					onLoad();
					return;
				}
				getListViewData();
				mStadiumSpecifyAdapter.notifyDataSetChanged();
				onLoad();
			} 
		}, 2000);
	}
	
	/** ֹͣListView�ײ�ˢ�� */
	private void onLoad() {
		stadium_specify_listview.stopRefresh();
		stadium_specify_listview.stopLoadMore();
		stadium_specify_listview.setRefreshTime("�ո�");
	}

	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.stadium_news_back_arrow://������ҳ��ͷ���ķ��ذ�ťͼ��
			finish();
			break;
		case R.id.order://�����ԤԼ��ť
			if(mGetVenuesInfoBean!=null){
				Intent order_intent=new Intent(StadiumSpecify.this,StadiumOrder.class);
				order_intent.putExtra("VenuesID", mGetVenuesInfoBean.getVenuesid());//д�볡��id
				order_intent.putExtra("VenuesImage", mGetVenuesInfoBean.getVenuesimage());//д�볡��ͼƬ·��
				order_intent.putExtra("VenuesName", mGetVenuesInfoBean.getVenuesname());//д�볡������
				startActivity(order_intent);
			}
			break;
		}
	}
	
	/**�Գ�������ҳ������������������ʵ��������*/
	private void initSpecifyView(View vv){
		specify_VenuesImager=(ImageView) vv.findViewById(R.id.specify_VenuesImager);
		specify_VenuesName=(TextView) vv.findViewById(R.id.specify_VenuesName);
		specify_VenuesArea=(TextView) vv.findViewById(R.id.specify_VenuesArea);
		specify_Address=(TextView) vv.findViewById(R.id.specify_Address);
		specify_ReservePhone=(TextView) vv.findViewById(R.id.specify_ReservePhone);
		specify_VenuesEmail=(TextView) vv.findViewById(R.id.specify_VenuesEmail);
		specify_RideRoute=(TextView) vv.findViewById(R.id.specify_RideRoute);
		specify_zhandiArea=(TextView) vv.findViewById(R.id.specify_zhandiArea);
		specify_WaterTemperature=(TextView) vv.findViewById(R.id.specify_WaterTemperature);
		specify_jianzhuArea=(TextView) vv.findViewById(R.id.specify_jianzhuArea);
		specify_WaterDepth=(TextView) vv.findViewById(R.id.specify_WaterDepth);
		specify_changdiArea=(TextView) vv.findViewById(R.id.specify_changdiArea);
		specify_Headroom=(TextView) vv.findViewById(R.id.specify_Headroom);
		specify_GroundMaterial=(TextView) vv.findViewById(R.id.specify_GroundMaterial);
		specify_InternalFacilities=(TextView) vv.findViewById(R.id.specify_InternalFacilities);
		specify_Investment=(TextView) vv.findViewById(R.id.specify_Investment);
		specify_VenuesType=(TextView) vv.findViewById(R.id.specify_VenuesType);
		specify_VenuesEnvironment=(TextView) vv.findViewById(R.id.specify_VenuesEnvironment);
		specify_ResponsiblePeople=(TextView) vv.findViewById(R.id.specify_ResponsiblePeople);
		specify_Contact=(TextView) vv.findViewById(R.id.specify_Contact);
		specify_ContactPhone=(TextView) vv.findViewById(R.id.specify_ContactPhone);
		specify_DirectorDept=(TextView) vv.findViewById(R.id.specify_DirectorDept);
		specify_Briefing=(TextView) vv.findViewById(R.id.specify_Briefing);
		specify_BuiltTime=(TextView) vv.findViewById(R.id.specify_BuiltTime);
		specify_UseTime=(TextView) vv.findViewById(R.id.specify_UseTime);
		specify_SeatNumber=(TextView) vv.findViewById(R.id.specify_SeatNumber);
		specify_AvgNumber=(TextView) vv.findViewById(R.id.specify_AvgNumber);
		specify_WhetherRecomm=(TextView) vv.findViewById(R.id.specify_WhetherRecomm);
		specify_Map=(TextView) vv.findViewById(R.id.specify_Map);
		
		if(mGetVenuesInfoBean!=null){
			//����ͼƬ��δ���� 
			ImageLoader.getInstance().displayImage(WebServiceUtils.IMAGE_URL+mGetVenuesInfoBean.getVenuesimage(),specify_VenuesImager, 
					AppApplication.options, new SimpleImageLoadingListener(),new ImageLoadingProgressListener(){

						@Override
						public void onProgressUpdate(String arg0, View view, int current, int total) {
							Log.i(TAG,"��������ͼƬ�첽���ص�����:total="+total+",current="+current);
							
						} 
					});
			specify_VenuesName.setText(mGetVenuesInfoBean.getVenuesname());//��������
			specify_VenuesArea.setText("������������:"+mGetVenuesInfoBean.getVenuesarea());//������������
			specify_Address.setText("��ϸ��ַ:"+mGetVenuesInfoBean.getAddress());//��ϸ��ַ
			specify_ReservePhone.setText("Ԥ���绰:"+mGetVenuesInfoBean.getReservephone());//Ԥ���绰
			specify_VenuesEmail.setText("��������:"+mGetVenuesInfoBean.getVenuesemail());//��������
			specify_RideRoute.setText("�˳�·��:"+mGetVenuesInfoBean.getRideroute());//�˳�·��
			specify_zhandiArea.setText("ռ�����:"+mGetVenuesInfoBean.getZhangdiarea().toString()+"�O");//ռ�����
			specify_WaterTemperature.setText("ˮ��:"+mGetVenuesInfoBean.getWatertemperature().toString()+"��");//ˮ��
			specify_jianzhuArea.setText("�������:"+mGetVenuesInfoBean.getJianzhuarea().toString()+"�O");//�������
			specify_WaterDepth.setText("ˮ��:"+mGetVenuesInfoBean.getWaterdepth()+"m");//ˮ��
			specify_changdiArea.setText("�������:"+mGetVenuesInfoBean.getChangdiarea().toString()+"�O");//�������
			specify_Headroom.setText("�߶�:"+mGetVenuesInfoBean.getHeadroom().toString()+"m");//���ո߶�
			specify_GroundMaterial.setText("�������:"+mGetVenuesInfoBean.getGroundmaterial());//�������
			specify_InternalFacilities.setText("�ڲ���ʩ:"+mGetVenuesInfoBean.getInternalfacilities());//�ڲ���ʩ
			specify_Investment.setText("Ͷ�ʽ��:"+mGetVenuesInfoBean.getInvestment().toString()+"(��Ԫ)");//Ͷ�ʽ��	
			specify_VenuesType.setText("��Ӫ����:"+mGetVenuesInfoBean.getVenuestype());//��Ӫ���� 
			specify_VenuesEnvironment.setText("��������:"+mGetVenuesInfoBean.getVenuesenvironment());//��������
			specify_ResponsiblePeople.setText("���ݸ�����:"+mGetVenuesInfoBean.getResponsiblepeople());//���ݸ�����
			specify_Contact.setText("������ϵ��:"+mGetVenuesInfoBean.getContact());//������ϵ��
			specify_ContactPhone.setText("��ϵ�绰:"+mGetVenuesInfoBean.getContactphone());//��ϵ�绰
			specify_DirectorDept.setText("���ܲ���:"+mGetVenuesInfoBean.getDirectordept());//���ܲ���
			specify_Briefing.setText("���ݼ��:"+mGetVenuesInfoBean.getBridfing());//���ݼ��
			specify_BuiltTime.setText("����ʱ��:"+mGetVenuesInfoBean.getBuilttime());//����ʱ��
			specify_UseTime.setText("Ͷ��ʹ��ʱ��:"+mGetVenuesInfoBean.getUsetime());//Ͷ��ʹ��ʱ��
			specify_SeatNumber.setText("������ϯ����:"+mGetVenuesInfoBean.getSeatnumber().toString());//������ϯ����
			specify_AvgNumber.setText("ÿ��ƽ��������:"+mGetVenuesInfoBean.getAvgnumber().toString());//ÿ��ƽ��������
			specify_WhetherRecomm.setText("�Ƿ��Ƽ�:"+mGetVenuesInfoBean.getWhetherlock());//�Ƿ��Ƽ� 
			specify_Map.setText("��ͼ����:"+mGetVenuesInfoBean.getMap());//��ͼ����  
		}
	}
	
}
