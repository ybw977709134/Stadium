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
	private LocationClient mLocationClient;//获取百度定位实例;//百度SDK定位,这个必需要UI线程里面声明
	private TextView home_location_address;//定位
	private ImageView home_register_login;
	private ImageView home_stadium_navigation_icon_one,home_stadium_navigation_icon_two;
	private ViewPager home_stadium_navigation;
	private ImageView[] dots;//GridView下面的小圆点
	private LinearLayout home_notsaysecret,home_brokespace;
	private ImageView home_friend_domain,home_adopt;//约战,应战
	private XListView home_hotrecommend;
	private HomeHotRecommendAdapter mHomeHotRecommendAdapter;
	private List<GridView> mGridViewList;
	private GridView mGridViewOne,mGridViewTwo;//两个GridView页面
	private List<Map<String,Object>> mGridViewOneList;
	private int currentIndex=0;//当前ViewPager选中的页面
	private List<Map<String,Object>> mListViewData;//存入listview每个条目的数据  
	private ProgressDialog mProgressDialog;//进度对话框

	private List<GetVenuesInfoBean> WebDataSet=null;
	private int WebDataSetCount=0;//存储查询到的数据集总长度
	private int mTotalPage=0;//根据数据总长度需要几页显示数据 
	private int current=0;//XListView当前页,默认为第一页  mTotalPage - currentIndex 
	private int TEN=10;//固定常量//默认每页显示的数据长度
	
	
	private Handler mHandler=new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {  
			switch (msg.what) {
			case ToolsHome.AUTO_LOCATION_FAILURE:
				Toast.makeText(getActivity(),(String)msg.obj, 0).show();
				break;
			case ToolsHome.AUTO_LOCATION_SUCCESS://更新定位显示内容
				home_location_address.setText((String)msg.obj);//更新定位显示内容
				break;
			case 0x11://从线程中查询的数据集返回此处
				WebDataSet= (List<GetVenuesInfoBean>) msg.obj;
				WebDataSetCount=WebDataSet.size();//设置返回数据集的总长度
				mTotalPage=WebDataSetCount%10==0 ? WebDataSetCount/10 : WebDataSetCount/10+1;//计算需要分几页显示数据 
				current=0;//重新把当前页面设置为默认值0
				Log.i(TAG,"mListViewData数据添加之前的长度为:"+mListViewData.size());
				getListViewData();//添加数据
				Log.i(TAG,"mListViewData数据添加之后的的长度为:"+mListViewData.size());
				mHomeHotRecommendAdapter.notifyDataSetChanged();//更新ListView适配器的数据
				Log.i(TAG,"首次加载数据正常");
				break;
			case 0x22://标识获取到的数据为空 
//				Toast.makeText(getActivity(), "没有找到哦!", 0).show();
			case 0x33://标识上拉加载时已经没有数据了
				Toast.makeText(getActivity(), "亲,没有了哦!", 0).show();	
			}
		};
	}; 
 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View vv=inflater.inflate(R.layout.home_fragment_listview, container,false); 
		mLocationClient=new LocationClient(getActivity());
		
		//判断当前网络是否可用，开启百度定位线程,开启场馆详情数据加载线程
		if(ToolsNavigate.isNetworkAvailable(getActivity())){
			new BaiduLocationLaunchThread(getActivity(),mHandler,mLocationClient).start();
			conditionQuery();//开启线程从服务器端加载场馆详情的数据到客户端
		} 
		
		home_hotrecommend=(XListView) vv.findViewById(R.id.home_hotrecommend);
		home_location_address=(TextView)vv.findViewById(R.id.home_location_address); 
		home_register_login=(ImageView) vv.findViewById(R.id.home_register_login);
		
		home_register_login.setOnClickListener(new MyOnclickListener());//为用户点击登录监听事件
		home_hotrecommend.setPullRefreshEnable(false);
		home_hotrecommend.setPullLoadEnable(true);
		home_hotrecommend.toggleHeadView=true;
		
		//为listview添加头部
		View view=inflater.inflate(R.layout.home_fragment, null);
		initView(view,inflater);//对所有组件初始化 
		initDots();
		home_hotrecommend.addHeaderView(view);  
		
		mListViewData=new ArrayList<Map<String,Object>>(); 
		
		//为ListView填充数据
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
		//为ListView设置适配器
		home_hotrecommend.setAdapter(mHomeHotRecommendAdapter);
		home_hotrecommend.setXListViewListener(this);//设置滚转监听
		home_hotrecommend.setOnItemClickListener(new AdapterView.OnItemClickListener(){ 
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//首先判断用户是否已经正常登录
				if((UserGlobal.UserExist && UserGlobal.UserID!=-1) || UserGlobal.SYSTEM){
//					Toast.makeText(getActivity(), WebDataSet.get(position-2).getVenuesname(), 0).show(); 
					Intent intent=new Intent(getActivity(),StadiumSpecify.class);
					Bundle extras=new Bundle();
					extras.putSerializable("VenuesInfo", WebDataSet.get(position-2));
					intent.putExtras(extras);
					getActivity().startActivity(intent);//跳转到场馆详情界面
				}else{
					//用户未登录，进入登录和注册页面
					Toast.makeText(getActivity(), "请先登录", 0).show();
					Intent not_intent=new Intent(getActivity(),Login.class);
					startActivity(not_intent);
				} 
			} 
		}); 
		
		//判断登录的时候是否使用到了第三方QQ帐户,新浪微博等登录,利用到了 QQLoginBean进行封装数据
		if(ToolsNavigate.TPOS_LOG!=null){
//			home_register_login.setImageBitmap(ToolsNavigate.QQ_LOG.getBitmap());
			home_register_login.setImageBitmap(ToolsHome.toRoundBitmap(ToolsNavigate.TPOS_LOG.getBitmap()));
			Log.i("HomeFragment","当前登录的第三方帐户昵称是:"+ToolsNavigate.TPOS_LOG.getNickname());
		}else if(UserGlobal.Picture!=null){ //判断是否是使用来动吧帐号登录
			//从Uri路径上加载图片资源进行显示vh.mImage
			ImageLoader.getInstance().displayImage(WebServiceUtils.IMAGE_URL+UserGlobal.Picture, home_register_login, AppApplication.options,
	        		new SimpleImageLoadingListener(),new ImageLoadingProgressListener(){ 
						@Override
						public void onProgressUpdate(String imageUri, View view,
								int current, int total) { 
							Log.i(TAG,"图片异步加载的数据:total="+total+",current="+current);
						} 
	        });
		} 
		
		return vv;
	}
	
	/** 对页面的组件进行初始化,并给组件绑定事件*/
	private void initView(View v,LayoutInflater inflater){ 
		home_stadium_navigation=(ViewPager) v.findViewById(R.id.home_stadium_navigation);
		home_stadium_navigation_icon_one=(ImageView) v.findViewById(R.id.home_stadium_navigation_icon_one);
		home_stadium_navigation_icon_two=(ImageView) v.findViewById(R.id.home_stadium_navigation_icon_two); 
		home_notsaysecret=(LinearLayout) v.findViewById(R.id.home_notsaysecret);
		home_brokespace=(LinearLayout) v.findViewById(R.id.home_brokespace);  
		home_friend_domain=(ImageView) v.findViewById(R.id.home_friend_domain);
		home_adopt=(ImageView) v.findViewById(R.id.home_adopt);
		
		home_friend_domain.setOnClickListener(new MyOnclickListener());//为约战绑定事件
		home_adopt.setOnClickListener(new MyOnclickListener());//为应战绑定事件
		
		home_location_address.setOnClickListener(new MyOnclickListener());//为定位绑定事件 
		home_notsaysecret.setOnClickListener(new MyOnclickListener());//为不能说的秘密绑定事件
		home_brokespace.setOnClickListener(new MyOnclickListener());//为爆料空间绑定事件 
		
		mGridViewList=new ArrayList<GridView>();
		//动态加载GridView组件的整体布局
		//ViewPager第一页
		mGridViewOne=(GridView) inflater.inflate(R.layout.home_stadium_navigation, null);
		mGridViewOne.setSelector(new ColorDrawable(Color.WHITE));//把gridview的背景色更改为无色
		
		//为GrigView添加数据，然衙填充适配器,再进行绑定事件
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
		//把GridView填充到mGridViewList集合中
		mGridViewList.add(mGridViewOne);
		
		//ViewPager第二页
		mGridViewTwo=(GridView) inflater.inflate(R.layout.home_stadium_navigation, null);
		mGridViewTwo.setSelector(new ColorDrawable(Color.WHITE));//把gridview的背景色更改为无色,Color.TRANSPARENT	
		
		//为GrigView添加数据，然衙填充适配器,再进行绑定事件
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
		
		//把GridView填充到mGridViewList集合中
		mGridViewList.add(mGridViewTwo);
		
		//把GridView填充到ViewPager当中,并进行添加适配器,进行设置监听事件,设置ViewPager当前显示页面
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
		dots[currentIndex].setImageResource(R.drawable.dot_red);//设置默认选中项
	}
	
	//对GridViewOne里面项目点击事件的处理
	private class GridViewOneOnItemClickListener implements OnItemClickListener{ 
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			@SuppressWarnings("unchecked")
			HashMap<String,Object> item=(HashMap<String, Object>) parent.getItemAtPosition(position);
//			Toast.makeText(getActivity(), (String)item.get("text"), 0).show();
			//直接跳转到应战页面
			if((UserGlobal.UserExist && UserGlobal.UserID!=-1) || UserGlobal.SYSTEM){
				//已经登录成功，跳转到应战的页面 
				Intent nenIntent=new Intent(getActivity(),ConditionQueryPage.class);//FriendRecommendType.class
				Integer ss=ToolsHome.getSportSerial((String)item.get("text"));
				if(ss!=null){
					nenIntent.putExtra("text", ss);//传入运动类型
					getActivity().startActivity(nenIntent);
				}else{
					Log.i(TAG,"没有找到运动类型="+(String)item.get("text")+" 对应的编号哦");
				}
//				nenIntent.setAction("ZZ_COM.WQP.STADIUMAPP_YINGZHAN");
//				getActivity().sendBroadcast(nenIntent); 
//				((MainActivity)getActivity()).setChoiceFragment(R.id.main_stadium);
			}else{
				//用户未登录，进入登录和注册页面
				Toast.makeText(getActivity(), "请先登录", 0).show();
				Intent yingzhan_intent=new Intent(getActivity(),Login.class);
				startActivity(yingzhan_intent);
			} 
		} 
	}
	
	//对GridViewTwo里面项目点击事件的处理
	private class GridViewTwoOnItemClickListener implements OnItemClickListener{ 
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			@SuppressWarnings("unchecked")
			HashMap<String,Object> item=(HashMap<String, Object>) parent.getItemAtPosition(position);
//			Toast.makeText(getActivity(), (String)item.get("text"), 0).show();
			//直接跳转到应战页面
			if((UserGlobal.UserExist && UserGlobal.UserID!=-1) || UserGlobal.SYSTEM){
				//已经登录成功，跳转到应战的页面 
				Intent nenIntent=new Intent(getActivity(),ConditionQueryPage.class);//FriendRecommendType.class
				Integer ss=ToolsHome.getSportSerial((String)item.get("text"));
				if(ss!=null){
					nenIntent.putExtra("text", ss);//传入运动类型
					getActivity().startActivity(nenIntent);
				} else{
					Log.i(TAG,"没有找到运动类型="+(String)item.get("text")+" 对应的编号哦");
				}
//				nenIntent.setAction("ZZ_COM.WQP.STADIUMAPP_YINGZHAN");
//				getActivity().sendBroadcast(nenIntent); 
//				((MainActivity)getActivity()).setChoiceFragment(R.id.main_stadium);
			}else{
				//用户未登录，进入登录和注册页面
				Toast.makeText(getActivity(), "请先登录", 0).show();
				Intent yingzhan_intent=new Intent(getActivity(),Login.class);
				startActivity(yingzhan_intent);
			}
		} 
	}
	
	//对ViewPager滑动事件的响应
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
	
	//点击其它功能 区域时的响应事件处理
	private class MyOnclickListener implements OnClickListener{ 
		@Override
		public void onClick(View v) {
			Intent intent=null;
			switch(v.getId()){
			case R.id.home_location_address://定位
				if((UserGlobal.UserExist && UserGlobal.UserID!=-1) || UserGlobal.SYSTEM){
					if(ToolsNavigate.isNetworkAvailable(getActivity())){
						intent=new Intent(getActivity(),BaiduMapLocation.class);//跳转到定位页面 //BaiduLocationActivity(百度定位);
						startActivity(intent);
						intent=null;
					}else{
						Toast.makeText(getActivity(), "亲,网络没打开哦", 0).show();
					}
				}else{
					Toast.makeText(getActivity(), "请先登录", 0).show();
					Intent intenteLogin=new Intent(getActivity(),Login.class);
					getActivity().startActivity(intenteLogin);
				} 
				break;
			case R.id.home_register_login://用户登录和注册
//				Toast.makeText(getActivity(), "用户登录和注册", 0).show(); 
				if((UserGlobal.UserExist && UserGlobal.UserID!=-1) || UserGlobal.SYSTEM){
					//已经登录成功，进入到用户设置页面 
					((MainActivity)getActivity()).setChoiceFragment(R.id.main_personalcenter);
				}else{
					//用户未登录，进入登录和注册页面
					Toast.makeText(getActivity(), "请先登录", 0).show();
					intent=new Intent(getActivity(),Login.class);
					startActivity(intent); 
				} 
				break; 
			case R.id.home_notsaysecret://不能说的秘密
//				Toast.makeText(getActivity(), "不能说的秘密", 0).show(); 
				if((UserGlobal.UserExist && UserGlobal.UserID!=-1) || UserGlobal.SYSTEM){
					intent=new Intent(getActivity(),NotSpeechSecret.class);
					startActivity(intent); 
				}else{
					//用户未登录，进入登录和注册页面
					Toast.makeText(getActivity(), "请先登录", 0).show();
					intent=new Intent(getActivity(),Login.class);
					startActivity(intent);
				} 
				break;
			case R.id.home_brokespace://爆料空间
//				Toast.makeText(getActivity(), "爆料空间", 0).show();
				if((UserGlobal.UserExist && UserGlobal.UserID!=-1) || UserGlobal.SYSTEM){
					intent=new Intent(getActivity(),BrokeTheSpace.class);
					startActivity(intent); 
				}else{
					//用户未登录，进入登录和注册页面
					Toast.makeText(getActivity(), "请先登录", 0).show();
					intent=new Intent(getActivity(),Login.class);
					startActivity(intent);
				}
				break;  
			case R.id.home_adopt://应战 
//				Toast.makeText(getActivity(), "应战", 0).show();
				if((UserGlobal.UserExist && UserGlobal.UserID!=-1) || UserGlobal.SYSTEM){
					
					Intent neIntent=new Intent(getActivity(),MainFriendRecommend.class);
					getActivity().startActivity(neIntent);
					
					//已经登录成功，跳转到应战的页面 
//					Intent neIntent=new Intent();
//					neIntent.setAction("ZZ_COM.WQP.STADIUMAPP_YINGZHAN");
//					getActivity().sendBroadcast(neIntent); 
//					((MainActivity)getActivity()).setChoiceFragment(R.id.main_stadium);
				}else{
					//用户未登录，进入登录和注册页面
					Toast.makeText(getActivity(), "请先登录", 0).show();
					intent=new Intent(getActivity(),Login.class);
					startActivity(intent);
				}
				break;
			case R.id.home_friend_domain://约战  
//				Toast.makeText(getActivity(), "约战", 0).show();
				if((UserGlobal.UserExist && UserGlobal.UserID!=-1) || UserGlobal.SYSTEM){

					Intent newIntent=new Intent(getActivity(),MainFriendApply.class);
					getActivity().startActivity(newIntent);
					
					//已经登录成功，进入到用户设置页面 
//					Intent newIntent=new Intent();
//					newIntent.setAction("COM.WQP.STADIUMAPP_YUEZHAN");
//					getActivity().sendBroadcast(newIntent); 
//					((MainActivity)getActivity()).setChoiceFragment(R.id.main_stadium);
				}else{
					//用户未登录，进入登录和注册页面
					Toast.makeText(getActivity(), "请先登录", 0).show();
					intent=new Intent(getActivity(),Login.class);
					startActivity(intent);
				} 
				break;
			} 
		} 
	} 
	
	/** 给ListView进行填充数据,默认每页只显示10条数据 */
	private List<Map<String,Object>> getListViewData(){ 
		if(WebDataSet!=null)Log.i(TAG,"现在WebDataSet的长度为:"+WebDataSet.size());
		
		if(WebDataSet!=null && WebDataSet.size() == 0){//如果WebDataSet数据集不为空，并且长度等于0就做用户提醒操作
//			Toast.makeText(getActivity(), "没有找到哦", 0).show();
		}else if(WebDataSet!=null && WebDataSet.size() > 0){//如果WebDataSet数据集不为空，并且长度大于0就做更新数据操作		
			/** 如果当前页数 小于 总页数,默认当前页从0开始增长,mTotalPage为数据总共分多少页*/
			Log.i(TAG,"current="+current+",mTotalPage="+mTotalPage);
			
			if(current < mTotalPage){ 
				//参数1：i的值等于当前页乘以每页限制显示的条目数量,默认是10条
				//参数2： var的值等于
				int var=0;
				//如果数据长度在默认10条以内
				if(WebDataSetCount < 10){ var=WebDataSetCount; } 
				else{ var = ((WebDataSetCount-(current*TEN))/TEN)>0 ? (current+1)*TEN : ((current+1)*TEN + WebDataSetCount%TEN)-TEN;}
				Log.i(TAG,"var值等于="+var);
				for (int i=current * TEN;i < var; i++) {
					Log.i(TAG,"i值等于="+i);
					Map<String,Object> map=new HashMap<String,Object>();
					Log.i(TAG,"获取到的图片路径为="+WebDataSet.get(i).getVenuesimage()); 
					map.put("VenuesImager", WebServiceUtils.IMAGE_URL+WebDataSet.get(i).getVenuesimage());//场馆图片,这里是图片的Uri路径，然后到ListView适配器当中进行异步加载获取到图片	
					map.put("VenuesName", WebDataSet.get(i).getVenuesname());//场馆名称
					map.put("Address", "地址："+WebDataSet.get(i).getAddress());//详细地址
					map.put("ReservePhone", "电话："+WebDataSet.get(i).getReservephone());//预定电话
					map.put("RideRoute", "路线："+WebDataSet.get(i).getRideroute());//乘车路线
					map.put("VenuesEnvironment", "所属环境："+WebDataSet.get(i).getVenuesenvironment());//场馆所属环境 
					mListViewData.add(map);
				} 
				current++;//把ListView的当前页加1 
			} 
		}else{
			Log.i(TAG,"给适配器填充的数据为null");
		}
		return mListViewData; 
	}
	
	/**这个是ListView顶部刷新使用,暂进未使用到*/
	@Override
	public void onRefresh() { 
		
	}
	
	/**这个是ListView底部加载更多使用,这里使用到了Handler进行加载数据*/
	@Override
	public void onLoadMore() { 
		mHandler.postDelayed(new Runnable() { 
			@Override
			public void run() {
				if(!ToolsNavigate.isNetworkAvailable(getActivity())){
					Toast.makeText(getActivity(), "亲,网络没打开哦", 1).show();return;
				}
				if(currentIndex==mTotalPage || WebDataSetCount==0){
					mHandler.sendEmptyMessage(0x33);//发送已经没有新数据的标识
					Log.i(TAG,"在ListView当中我也向Handler发送消息过去了");
					onStopLoad();
					return;
				}
				if(WebDataSet==null){
					conditionQuery();//从服务端加载数据
					onStopLoad();
					return;
				}
				getListViewData();
				mHomeHotRecommendAdapter.notifyDataSetChanged();
				onStopLoad();
			}
		}, 2000); 
	}
	
	/** 停止底部刷新 */
	private void onStopLoad() {
		home_hotrecommend.stopRefresh();//停止刷新, 重置头视图
		home_hotrecommend.stopLoadMore();//stop load more, reset footer view
		home_hotrecommend.setRefreshTime("刚刚");
	}
	
	
	
	@Override
	public void onStart() { 
		super.onStart();
		if(ToolsHome.LOCATION_CITY!=null && ToolsHome.IS_LOCATION){ 
			home_location_address.setText(ToolsHome.LOCATION_CITY); //设置已经成功定位的城市名称
		}
	}
	
	/**销毁状态*/
	@Override
	public void onDestroyView() { 
		super.onDestroyView();
		home_location_address.setText("");
	}

	public void conditionQuery() {  
		mProgressDialog=new ProgressDialog(getActivity());
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage("加载中...");
		mProgressDialog.setIndeterminate(true); 
		mProgressDialog.setCancelable(false);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.show();
		new Thread(){   
			public void run() {
				try {
//					JSONObject object=new JSONObject();
//					object.put("VenuesID", 0);
//					object.put("VenuesName", 0); //当两个参数都为0时表示查询所有信息
					List<GetVenuesInfoBean> results=WebGetVenues.getGetVenuesData("");
					if(results!=null){
						Message msg=new Message();
						msg.what=0x11;//标识正常获取到数据了
						msg.obj=results;
						mHandler.sendMessage(msg); 
						Log.i(TAG,"Web端线程加载数据结束,我已经发送数据到Handler了");
					}else{
						mHandler.sendEmptyMessage(0x22);//发送标识获取到的数据为空
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
