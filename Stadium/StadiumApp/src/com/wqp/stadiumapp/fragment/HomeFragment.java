package com.wqp.stadiumapp.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 




import android.app.ProgressDialog;
import android.content.Intent; 
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wqp.baidu.BaiduLocationLaunchThread;
import com.wqp.baidu.BaiduMapLocation;
import com.wqp.stadiumapp.AppApplication;
import com.wqp.stadiumapp.BrokeTheSpace;
import com.wqp.stadiumapp.ConditionQueryPage;
import com.wqp.stadiumapp.MainFriendApply; 
import com.wqp.stadiumapp.Login;
import com.wqp.stadiumapp.MainActivity;
import com.wqp.stadiumapp.MainFriendRecommend;
import com.wqp.stadiumapp.NotSpeechSecret;
import com.wqp.stadiumapp.R; 
import com.wqp.stadiumapp.StadiumSpecify; 
import com.wqp.stadiumapp.adapter.HomeHotRecommendAdapter;
import com.wqp.stadiumapp.adapter.HomeStadiumViewPagerAdapter; 
import com.wqp.stadiumapp.utils.ToolsHome;
import com.wqp.stadiumapp.utils.ToolsNavigate;
import com.wqp.stadiumapp.utils.UserGlobal;
import com.wqp.stadiumapp.utils.WebServiceUtils;
import com.wqp.view.XListView;
import com.wqp.view.XListView.IXListViewListener; 
import com.wqp.webservice.WebGetVenues; 
import com.wqp.webservice.entity.GetVenuesInfoBean;

public class HomeFragment extends Fragment implements IXListViewListener{
	private static String TAG="HomeFragment";
	private LocationClient mLocationClient;//��ȡ�ٶȶ�λʵ��;//�ٶ�SDK��λ,�������ҪUI�߳���������
	private TextView home_location_address;//��λ
	private ImageView home_register_login;
	private ImageView home_stadium_navigation_icon_one,home_stadium_navigation_icon_two;
	private ViewPager home_stadium_navigation;
	private ImageView[] dots;//GridView�����СԲ��
	private LinearLayout home_notsaysecret,home_brokespace;
	private ImageView home_friend_domain,home_adopt;//Լս,Ӧս
	private XListView home_hotrecommend;
	private HomeHotRecommendAdapter mHomeHotRecommendAdapter;
	private List<GridView> mGridViewList;
	private GridView mGridViewOne,mGridViewTwo;//����GridViewҳ��
	private List<Map<String,Object>> mGridViewOneList;
	private int currentIndex=0;//��ǰViewPagerѡ�е�ҳ��
	private List<Map<String,Object>> mListViewData;//����listviewÿ����Ŀ������  
	private ProgressDialog mProgressDialog;//���ȶԻ���

	private List<GetVenuesInfoBean> WebDataSet=null;
	private int WebDataSetCount=0;//�洢��ѯ�������ݼ��ܳ���
	private int mTotalPage=0;//���������ܳ�����Ҫ��ҳ��ʾ���� 
	private int current=0;//XListView��ǰҳ,Ĭ��Ϊ��һҳ  mTotalPage - currentIndex 
	private int TEN=10;//�̶�����//Ĭ��ÿҳ��ʾ�����ݳ���
	
	
	private Handler mHandler=new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {  
			switch (msg.what) {
			case ToolsHome.AUTO_LOCATION_FAILURE:
				Toast.makeText(getActivity(),(String)msg.obj, 0).show();
				break;
			case ToolsHome.AUTO_LOCATION_SUCCESS://���¶�λ��ʾ����
				home_location_address.setText((String)msg.obj);//���¶�λ��ʾ����
				break;
			case 0x11://���߳��в�ѯ�����ݼ����ش˴�
				WebDataSet= (List<GetVenuesInfoBean>) msg.obj;
				WebDataSetCount=WebDataSet.size();//���÷������ݼ����ܳ���
				mTotalPage=WebDataSetCount%10==0 ? WebDataSetCount/10 : WebDataSetCount/10+1;//������Ҫ�ּ�ҳ��ʾ���� 
				current=0;//���°ѵ�ǰҳ������ΪĬ��ֵ0
				Log.i(TAG,"mListViewData�������֮ǰ�ĳ���Ϊ:"+mListViewData.size());
				getListViewData();//�������
				Log.i(TAG,"mListViewData�������֮��ĵĳ���Ϊ:"+mListViewData.size());
				mHomeHotRecommendAdapter.notifyDataSetChanged();//����ListView������������
				Log.i(TAG,"�״μ�����������");
				break;
			case 0x22://��ʶ��ȡ��������Ϊ�� 
//				Toast.makeText(getActivity(), "û���ҵ�Ŷ!", 0).show();
			case 0x33://��ʶ��������ʱ�Ѿ�û��������
				Toast.makeText(getActivity(), "��,û����Ŷ!", 0).show();	
			}
		};
	}; 
 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View vv=inflater.inflate(R.layout.home_fragment_listview, container,false); 
		mLocationClient=new LocationClient(getActivity());
		
		//�жϵ�ǰ�����Ƿ���ã������ٶȶ�λ�߳�,���������������ݼ����߳�
		if(ToolsNavigate.isNetworkAvailable(getActivity())){
			new BaiduLocationLaunchThread(getActivity(),mHandler,mLocationClient).start();
			conditionQuery();//�����̴߳ӷ������˼��س�����������ݵ��ͻ���
		} 
		
		home_hotrecommend=(XListView) vv.findViewById(R.id.home_hotrecommend);
		home_location_address=(TextView)vv.findViewById(R.id.home_location_address); 
		home_register_login=(ImageView) vv.findViewById(R.id.home_register_login);
		
		home_register_login.setOnClickListener(new MyOnclickListener());//Ϊ�û������¼�����¼�
		home_hotrecommend.setPullRefreshEnable(false);
		home_hotrecommend.setPullLoadEnable(true);
		home_hotrecommend.toggleHeadView=true;
		
		//Ϊlistview���ͷ��
		View view=inflater.inflate(R.layout.home_fragment, null);
		initView(view,inflater);//�����������ʼ�� 
		initDots();
		home_hotrecommend.addHeaderView(view);  
		
		mListViewData=new ArrayList<Map<String,Object>>(); 
		
		//ΪListView�������
		mHomeHotRecommendAdapter=new HomeHotRecommendAdapter(
				getActivity(),
				getListViewData(),
				R.layout.home_hotrecommend_listview_item,
				new String[]{"VenuesImager","VenuesName","Address","ReservePhone","RideRoute","VenuesEnvironment"},	
				new int[]{R.id.home_hotrecommend_listview_image,
						  R.id.home_hotrecommend_listview_title,
						  R.id.home_hotrecommend_listview_address,
						  R.id.home_hotrecommend_listview_telephone,
						  R.id.home_hotrecommend_listview_route, 
						  R.id.home_hotrecommend_listview_environment});
		//ΪListView����������
		home_hotrecommend.setAdapter(mHomeHotRecommendAdapter);
		home_hotrecommend.setXListViewListener(this);//���ù�ת����
		home_hotrecommend.setOnItemClickListener(new AdapterView.OnItemClickListener(){ 
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//�����ж��û��Ƿ��Ѿ�������¼
				if((UserGlobal.UserExist && UserGlobal.UserID!=-1) || UserGlobal.SYSTEM){
//					Toast.makeText(getActivity(), WebDataSet.get(position-2).getVenuesname(), 0).show(); 
					Intent intent=new Intent(getActivity(),StadiumSpecify.class);
					Bundle extras=new Bundle();
					extras.putSerializable("VenuesInfo", WebDataSet.get(position-2));
					intent.putExtras(extras);
					getActivity().startActivity(intent);//��ת�������������
				}else{
					//�û�δ��¼�������¼��ע��ҳ��
					Toast.makeText(getActivity(), "���ȵ�¼", 0).show();
					Intent not_intent=new Intent(getActivity(),Login.class);
					startActivity(not_intent);
				} 
			} 
		}); 
		
		//�жϵ�¼��ʱ���Ƿ�ʹ�õ��˵�����QQ�ʻ�,����΢���ȵ�¼,���õ��� QQLoginBean���з�װ����
		if(ToolsNavigate.TPOS_LOG!=null){
//			home_register_login.setImageBitmap(ToolsNavigate.QQ_LOG.getBitmap());
			home_register_login.setImageBitmap(ToolsHome.toRoundBitmap(ToolsNavigate.TPOS_LOG.getBitmap()));
			Log.i("HomeFragment","��ǰ��¼�ĵ������ʻ��ǳ���:"+ToolsNavigate.TPOS_LOG.getNickname());
		}else if(UserGlobal.Picture!=null){ //�ж��Ƿ���ʹ���������ʺŵ�¼
			//��Uri·���ϼ���ͼƬ��Դ������ʾvh.mImage
			ImageLoader.getInstance().displayImage(WebServiceUtils.IMAGE_URL+UserGlobal.Picture, home_register_login, AppApplication.options,
	        		new SimpleImageLoadingListener(),new ImageLoadingProgressListener(){ 
						@Override
						public void onProgressUpdate(String imageUri, View view,
								int current, int total) { 
							Log.i(TAG,"ͼƬ�첽���ص�����:total="+total+",current="+current);
						} 
	        });
		} 
		
		return vv;
	}
	
	/** ��ҳ���������г�ʼ��,����������¼�*/
	private void initView(View v,LayoutInflater inflater){ 
		home_stadium_navigation=(ViewPager) v.findViewById(R.id.home_stadium_navigation);
		home_stadium_navigation_icon_one=(ImageView) v.findViewById(R.id.home_stadium_navigation_icon_one);
		home_stadium_navigation_icon_two=(ImageView) v.findViewById(R.id.home_stadium_navigation_icon_two); 
		home_notsaysecret=(LinearLayout) v.findViewById(R.id.home_notsaysecret);
		home_brokespace=(LinearLayout) v.findViewById(R.id.home_brokespace);  
		home_friend_domain=(ImageView) v.findViewById(R.id.home_friend_domain);
		home_adopt=(ImageView) v.findViewById(R.id.home_adopt);
		
		home_friend_domain.setOnClickListener(new MyOnclickListener());//ΪԼս���¼�
		home_adopt.setOnClickListener(new MyOnclickListener());//ΪӦս���¼�
		
		home_location_address.setOnClickListener(new MyOnclickListener());//Ϊ��λ���¼� 
		home_notsaysecret.setOnClickListener(new MyOnclickListener());//Ϊ����˵�����ܰ��¼�
		home_brokespace.setOnClickListener(new MyOnclickListener());//Ϊ���Ͽռ���¼� 
		
		mGridViewList=new ArrayList<GridView>();
		//��̬����GridView��������岼��
		//ViewPager��һҳ
		mGridViewOne=(GridView) inflater.inflate(R.layout.home_stadium_navigation, null);
		mGridViewOne.setSelector(new ColorDrawable(Color.WHITE));//��gridview�ı���ɫ����Ϊ��ɫ
		
		//ΪGrigView������ݣ�Ȼ�����������,�ٽ��а��¼�
		mGridViewOneList=new ArrayList<Map<String,Object>>();
		for(int i=0;i<ToolsHome.STADIUM_ITEM_IMAGE_ONE.length;i++){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("image", ToolsHome.STADIUM_ITEM_IMAGE_ONE[i]);
			map.put("text", ToolsHome.STADIUM_ITEM_TEXT_ONE[i]);
			mGridViewOneList.add(map); 
		}
		mGridViewOne.setAdapter(new SimpleAdapter(
				getActivity(), 
				mGridViewOneList, 
				R.layout.home_stadium_navigation_item, 
				new String[]{"image","text"}, 
				new int[]{R.id.home_stadium_navigation_item_image,R.id.home_stadium_navigation_item_text}));
		mGridViewOne.setOnItemClickListener(new GridViewOneOnItemClickListener());
		//��GridView��䵽mGridViewList������
		mGridViewList.add(mGridViewOne);
		
		//ViewPager�ڶ�ҳ
		mGridViewTwo=(GridView) inflater.inflate(R.layout.home_stadium_navigation, null);
		mGridViewTwo.setSelector(new ColorDrawable(Color.WHITE));//��gridview�ı���ɫ����Ϊ��ɫ,Color.TRANSPARENT	
		
		//ΪGrigView������ݣ�Ȼ�����������,�ٽ��а��¼�
		mGridViewOneList=new ArrayList<Map<String,Object>>();
		for(int i=0;i<ToolsHome.STADIUM_ITEM_MAGE_TWO.length;i++){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("image", ToolsHome.STADIUM_ITEM_MAGE_TWO[i]);
			map.put("text", ToolsHome.STADIUM_ITEM_TEXT_TWO[i]);
			mGridViewOneList.add(map); 
		}
		mGridViewTwo.setAdapter(new SimpleAdapter(
				getActivity(), 
				mGridViewOneList, 
				R.layout.home_stadium_navigation_item, 
				new String[]{"image","text"}, 
				new int[]{R.id.home_stadium_navigation_item_image,R.id.home_stadium_navigation_item_text}));
		mGridViewTwo.setOnItemClickListener(new GridViewTwoOnItemClickListener());
		
		//��GridView��䵽mGridViewList������
		mGridViewList.add(mGridViewTwo);
		
		//��GridView��䵽ViewPager����,���������������,�������ü����¼�,����ViewPager��ǰ��ʾҳ��
		home_stadium_navigation.setAdapter(new HomeStadiumViewPagerAdapter(mGridViewList));
		home_stadium_navigation.setOnPageChangeListener(new ViewPagerOnPageChangeListener());
		home_stadium_navigation.setCurrentItem(0);
		
	}
	
	private void initDots(){
		dots=new ImageView[2];
		dots[0]=home_stadium_navigation_icon_one;
		dots[1]=home_stadium_navigation_icon_two;
		
		dots[0].setImageResource(R.drawable.dot_white);
		dots[1].setImageResource(R.drawable.dot_white);
		currentIndex=0;
		dots[currentIndex].setImageResource(R.drawable.dot_red);//����Ĭ��ѡ����
	}
	
	//��GridViewOne������Ŀ����¼��Ĵ���
	private class GridViewOneOnItemClickListener implements OnItemClickListener{ 
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			@SuppressWarnings("unchecked")
			HashMap<String,Object> item=(HashMap<String, Object>) parent.getItemAtPosition(position);
//			Toast.makeText(getActivity(), (String)item.get("text"), 0).show();
			//ֱ����ת��Ӧսҳ��
			if((UserGlobal.UserExist && UserGlobal.UserID!=-1) || UserGlobal.SYSTEM){
				//�Ѿ���¼�ɹ�����ת��Ӧս��ҳ�� 
				Intent nenIntent=new Intent(getActivity(),ConditionQueryPage.class);//FriendRecommendType.class
				Integer ss=ToolsHome.getSportSerial((String)item.get("text"));
				if(ss!=null){
					nenIntent.putExtra("text", ss);//�����˶�����
					getActivity().startActivity(nenIntent);
				}else{
					Log.i(TAG,"û���ҵ��˶�����="+(String)item.get("text")+" ��Ӧ�ı��Ŷ");
				}
//				nenIntent.setAction("ZZ_COM.WQP.STADIUMAPP_YINGZHAN");
//				getActivity().sendBroadcast(nenIntent); 
//				((MainActivity)getActivity()).setChoiceFragment(R.id.main_stadium);
			}else{
				//�û�δ��¼�������¼��ע��ҳ��
				Toast.makeText(getActivity(), "���ȵ�¼", 0).show();
				Intent yingzhan_intent=new Intent(getActivity(),Login.class);
				startActivity(yingzhan_intent);
			} 
		} 
	}
	
	//��GridViewTwo������Ŀ����¼��Ĵ���
	private class GridViewTwoOnItemClickListener implements OnItemClickListener{ 
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			@SuppressWarnings("unchecked")
			HashMap<String,Object> item=(HashMap<String, Object>) parent.getItemAtPosition(position);
//			Toast.makeText(getActivity(), (String)item.get("text"), 0).show();
			//ֱ����ת��Ӧսҳ��
			if((UserGlobal.UserExist && UserGlobal.UserID!=-1) || UserGlobal.SYSTEM){
				//�Ѿ���¼�ɹ�����ת��Ӧս��ҳ�� 
				Intent nenIntent=new Intent(getActivity(),ConditionQueryPage.class);//FriendRecommendType.class
				Integer ss=ToolsHome.getSportSerial((String)item.get("text"));
				if(ss!=null){
					nenIntent.putExtra("text", ss);//�����˶�����
					getActivity().startActivity(nenIntent);
				} else{
					Log.i(TAG,"û���ҵ��˶�����="+(String)item.get("text")+" ��Ӧ�ı��Ŷ");
				}
//				nenIntent.setAction("ZZ_COM.WQP.STADIUMAPP_YINGZHAN");
//				getActivity().sendBroadcast(nenIntent); 
//				((MainActivity)getActivity()).setChoiceFragment(R.id.main_stadium);
			}else{
				//�û�δ��¼�������¼��ע��ҳ��
				Toast.makeText(getActivity(), "���ȵ�¼", 0).show();
				Intent yingzhan_intent=new Intent(getActivity(),Login.class);
				startActivity(yingzhan_intent);
			}
		} 
	}
	
	//��ViewPager�����¼�����Ӧ
	private class ViewPagerOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) { }

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) { }

		@Override
		public void onPageSelected(int position) {
			home_stadium_navigation.setCurrentItem(position); 
			dots[position].setImageResource(R.drawable.dot_red);
			dots[currentIndex].setImageResource(R.drawable.dot_white);
			currentIndex=position;
		} 
	} 
	
	//����������� ����ʱ����Ӧ�¼�����
	private class MyOnclickListener implements OnClickListener{ 
		@Override
		public void onClick(View v) {
			Intent intent=null;
			switch(v.getId()){
			case R.id.home_location_address://��λ
				if((UserGlobal.UserExist && UserGlobal.UserID!=-1) || UserGlobal.SYSTEM){
					if(ToolsNavigate.isNetworkAvailable(getActivity())){
						intent=new Intent(getActivity(),BaiduMapLocation.class);//��ת����λҳ�� //BaiduLocationActivity(�ٶȶ�λ);
						startActivity(intent);
						intent=null;
					}else{
						Toast.makeText(getActivity(), "��,����û��Ŷ", 0).show();
					}
				}else{
					Toast.makeText(getActivity(), "���ȵ�¼", 0).show();
					Intent intenteLogin=new Intent(getActivity(),Login.class);
					getActivity().startActivity(intenteLogin);
				} 
				break;
			case R.id.home_register_login://�û���¼��ע��
//				Toast.makeText(getActivity(), "�û���¼��ע��", 0).show(); 
				if((UserGlobal.UserExist && UserGlobal.UserID!=-1) || UserGlobal.SYSTEM){
					//�Ѿ���¼�ɹ������뵽�û�����ҳ�� 
					((MainActivity)getActivity()).setChoiceFragment(R.id.main_personalcenter);
				}else{
					//�û�δ��¼�������¼��ע��ҳ��
					Toast.makeText(getActivity(), "���ȵ�¼", 0).show();
					intent=new Intent(getActivity(),Login.class);
					startActivity(intent); 
				} 
				break; 
			case R.id.home_notsaysecret://����˵������
//				Toast.makeText(getActivity(), "����˵������", 0).show(); 
				if((UserGlobal.UserExist && UserGlobal.UserID!=-1) || UserGlobal.SYSTEM){
					intent=new Intent(getActivity(),NotSpeechSecret.class);
					startActivity(intent); 
				}else{
					//�û�δ��¼�������¼��ע��ҳ��
					Toast.makeText(getActivity(), "���ȵ�¼", 0).show();
					intent=new Intent(getActivity(),Login.class);
					startActivity(intent);
				} 
				break;
			case R.id.home_brokespace://���Ͽռ�
//				Toast.makeText(getActivity(), "���Ͽռ�", 0).show();
				if((UserGlobal.UserExist && UserGlobal.UserID!=-1) || UserGlobal.SYSTEM){
					intent=new Intent(getActivity(),BrokeTheSpace.class);
					startActivity(intent); 
				}else{
					//�û�δ��¼�������¼��ע��ҳ��
					Toast.makeText(getActivity(), "���ȵ�¼", 0).show();
					intent=new Intent(getActivity(),Login.class);
					startActivity(intent);
				}
				break;  
			case R.id.home_adopt://Ӧս 
//				Toast.makeText(getActivity(), "Ӧս", 0).show();
				if((UserGlobal.UserExist && UserGlobal.UserID!=-1) || UserGlobal.SYSTEM){
					
					Intent neIntent=new Intent(getActivity(),MainFriendRecommend.class);
					getActivity().startActivity(neIntent);
					
					//�Ѿ���¼�ɹ�����ת��Ӧս��ҳ�� 
//					Intent neIntent=new Intent();
//					neIntent.setAction("ZZ_COM.WQP.STADIUMAPP_YINGZHAN");
//					getActivity().sendBroadcast(neIntent); 
//					((MainActivity)getActivity()).setChoiceFragment(R.id.main_stadium);
				}else{
					//�û�δ��¼�������¼��ע��ҳ��
					Toast.makeText(getActivity(), "���ȵ�¼", 0).show();
					intent=new Intent(getActivity(),Login.class);
					startActivity(intent);
				}
				break;
			case R.id.home_friend_domain://Լս  
//				Toast.makeText(getActivity(), "Լս", 0).show();
				if((UserGlobal.UserExist && UserGlobal.UserID!=-1) || UserGlobal.SYSTEM){

					Intent newIntent=new Intent(getActivity(),MainFriendApply.class);
					getActivity().startActivity(newIntent);
					
					//�Ѿ���¼�ɹ������뵽�û�����ҳ�� 
//					Intent newIntent=new Intent();
//					newIntent.setAction("COM.WQP.STADIUMAPP_YUEZHAN");
//					getActivity().sendBroadcast(newIntent); 
//					((MainActivity)getActivity()).setChoiceFragment(R.id.main_stadium);
				}else{
					//�û�δ��¼�������¼��ע��ҳ��
					Toast.makeText(getActivity(), "���ȵ�¼", 0).show();
					intent=new Intent(getActivity(),Login.class);
					startActivity(intent);
				} 
				break;
			} 
		} 
	} 
	
	/** ��ListView�����������,Ĭ��ÿҳֻ��ʾ10������ */
	private List<Map<String,Object>> getListViewData(){ 
		if(WebDataSet!=null)Log.i(TAG,"����WebDataSet�ĳ���Ϊ:"+WebDataSet.size());
		
		if(WebDataSet!=null && WebDataSet.size() == 0){//���WebDataSet���ݼ���Ϊ�գ����ҳ��ȵ���0�����û����Ѳ���
//			Toast.makeText(getActivity(), "û���ҵ�Ŷ", 0).show();
		}else if(WebDataSet!=null && WebDataSet.size() > 0){//���WebDataSet���ݼ���Ϊ�գ����ҳ��ȴ���0�����������ݲ���		
			/** �����ǰҳ�� С�� ��ҳ��,Ĭ�ϵ�ǰҳ��0��ʼ����,mTotalPageΪ�����ܹ��ֶ���ҳ*/
			Log.i(TAG,"current="+current+",mTotalPage="+mTotalPage);
			
			if(current < mTotalPage){ 
				//����1��i��ֵ���ڵ�ǰҳ����ÿҳ������ʾ����Ŀ����,Ĭ����10��
				//����2�� var��ֵ����
				int var=0;
				//������ݳ�����Ĭ��10������
				if(WebDataSetCount < 10){ var=WebDataSetCount; } 
				else{ var = ((WebDataSetCount-(current*TEN))/TEN)>0 ? (current+1)*TEN : ((current+1)*TEN + WebDataSetCount%TEN)-TEN;}
				Log.i(TAG,"varֵ����="+var);
				for (int i=current * TEN;i < var; i++) {
					Log.i(TAG,"iֵ����="+i);
					Map<String,Object> map=new HashMap<String,Object>();
					Log.i(TAG,"��ȡ����ͼƬ·��Ϊ="+WebDataSet.get(i).getVenuesimage()); 
					map.put("VenuesImager", WebServiceUtils.IMAGE_URL+WebDataSet.get(i).getVenuesimage());//����ͼƬ,������ͼƬ��Uri·����Ȼ��ListView���������н����첽���ػ�ȡ��ͼƬ	
					map.put("VenuesName", WebDataSet.get(i).getVenuesname());//��������
					map.put("Address", "��ַ��"+WebDataSet.get(i).getAddress());//��ϸ��ַ
					map.put("ReservePhone", "�绰��"+WebDataSet.get(i).getReservephone());//Ԥ���绰
					map.put("RideRoute", "·�ߣ�"+WebDataSet.get(i).getRideroute());//�˳�·��
					map.put("VenuesEnvironment", "����������"+WebDataSet.get(i).getVenuesenvironment());//������������ 
					mListViewData.add(map);
				} 
				current++;//��ListView�ĵ�ǰҳ��1 
			} 
		}else{
			Log.i(TAG,"����������������Ϊnull");
		}
		return mListViewData; 
	}
	
	/**�����ListView����ˢ��ʹ��,�ݽ�δʹ�õ�*/
	@Override
	public void onRefresh() { 
		
	}
	
	/**�����ListView�ײ����ظ���ʹ��,����ʹ�õ���Handler���м�������*/
	@Override
	public void onLoadMore() { 
		mHandler.postDelayed(new Runnable() { 
			@Override
			public void run() {
				if(!ToolsNavigate.isNetworkAvailable(getActivity())){
					Toast.makeText(getActivity(), "��,����û��Ŷ", 1).show();return;
				}
				if(currentIndex==mTotalPage || WebDataSetCount==0){
					mHandler.sendEmptyMessage(0x33);//�����Ѿ�û�������ݵı�ʶ
					Log.i(TAG,"��ListView������Ҳ��Handler������Ϣ��ȥ��");
					onStopLoad();
					return;
				}
				if(WebDataSet==null){
					conditionQuery();//�ӷ���˼�������
					onStopLoad();
					return;
				}
				getListViewData();
				mHomeHotRecommendAdapter.notifyDataSetChanged();
				onStopLoad();
			}
		}, 2000); 
	}
	
	/** ֹͣ�ײ�ˢ�� */
	private void onStopLoad() {
		home_hotrecommend.stopRefresh();//ֹͣˢ��, ����ͷ��ͼ
		home_hotrecommend.stopLoadMore();//stop load more, reset footer view
		home_hotrecommend.setRefreshTime("�ո�");
	}
	
	
	
	@Override
	public void onStart() { 
		super.onStart();
		if(ToolsHome.LOCATION_CITY!=null && ToolsHome.IS_LOCATION){ 
			home_location_address.setText(ToolsHome.LOCATION_CITY); //�����Ѿ��ɹ���λ�ĳ�������
		}
	}
	
	/**����״̬*/
	@Override
	public void onDestroyView() { 
		super.onDestroyView();
		home_location_address.setText("");
	}

	public void conditionQuery() {  
		mProgressDialog=new ProgressDialog(getActivity());
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage("������...");
		mProgressDialog.setIndeterminate(true); 
		mProgressDialog.setCancelable(false);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.show();
		new Thread(){   
			public void run() {
				try {
//					JSONObject object=new JSONObject();
//					object.put("VenuesID", 0);
//					object.put("VenuesName", 0); //������������Ϊ0ʱ��ʾ��ѯ������Ϣ
					List<GetVenuesInfoBean> results=WebGetVenues.getGetVenuesData("");
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
				} finally{
					if(mProgressDialog!=null){
						mProgressDialog.dismiss();
					}
				}
			};
		}.start();
	}
	 
}
