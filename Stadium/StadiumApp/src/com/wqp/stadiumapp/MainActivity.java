package com.wqp.stadiumapp;
  
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.wqp.stadiumapp.fragment.HomeFragment;
import com.wqp.stadiumapp.fragment.PersonalFragment;
import com.wqp.stadiumapp.fragment.StadiumFragment;
import com.wqp.stadiumapp.utils.CommonSlidMenu;
import com.wqp.stadiumapp.utils.Exit;
import com.wqp.stadiumapp.utils.ToolsHome;
import com.wqp.stadiumapp.utils.UserGlobal;
import com.wqp.webservice.WebGetSports;
import com.wqp.webservice.entity.GetSportBean;

public class MainActivity extends FragmentActivity implements OnClickListener { 
	private static String TAG="MainActivity";
	private ImageView main_home_icon,main_stadium_icon,main_personalcenter_icon;
	
	//声明三个Fragment
	private HomeFragment mHomeFragment;
	private StadiumFragment mStadiumFragment;
	private PersonalFragment mPersonalFragment; 
	
	//声明FragmentManager管理器
	private FragmentManager fm; 
	
	private List<GetSportBean> DataSet;//从服务端返回的数据集
	private int xx=0;
	//定义消息管理器
	private Handler mHandler=new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x11:
				DataSet=(List<GetSportBean>) msg.obj; 
				if(DataSet!=null && DataSet.size()>0){ 
					Log.i(TAG,"从服务端获取到的运动类型数据长度DataSet的参数为:"+DataSet.size()+"条");
					if(!ToolsHome.addDataSet(DataSet)){
						xx++;
						if(xx<2){
							conditionQuery();
						}else{
							xx=0;
						}
					}else{
						Log.i(TAG,"从服务端获取到的运动类型数据长度为:"+ToolsHome.SPORTTYPE_Integer.size()+"条");
					}
				} 
				break; 
			case 0x22:
				Log.i(TAG,"应用首次启动加载运动类型失败了");
				break;
			} 
		};
	};
	/**
	 * Launch Home activity helper
	 * @param c c context where launch home from (used by Navigate)
	 */
	public static void launch(Context c){
		Intent intent=new Intent(c,MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		c.startActivity(intent);
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        
        //开启普通用户监听场馆回复的消息服务
        UserGlobal.user_intent.setAction("com.wqp.stadiumapp.UserService");
        startService(UserGlobal.user_intent);
        
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		conditionQuery();//从服务端加载数据
        fm=getSupportFragmentManager();//获取到FragmentManager管理器
        initView();//初始化组件 
        
        setChoiceFragment(R.id.main_stadium);//因为界面上有约战点击需要直接跳转，所以在这里需要把界面实例化一下。
        if(CommonSlidMenu.mSlidMenu!=null && CommonSlidMenu.mSlidMenu.isSlidingEnabled()){ 
			CommonSlidMenu.mSlidMenu.setSlidingEnabled(false); 
		} 
        setChoiceFragment(R.id.main_home);//设置程序进入的默认页面  
    }
    
    /** 对组件进行初始化并绑定点击事件 */
    private void initView(){ 
    	main_home_icon=(ImageView) findViewById(R.id.main_home);
    	main_stadium_icon=(ImageView) findViewById(R.id.main_stadium);
    	main_personalcenter_icon=(ImageView) findViewById(R.id.main_personalcenter); 
    	
    	main_home_icon.setOnClickListener(this);
    	main_stadium_icon.setOnClickListener(this);
    	main_personalcenter_icon.setOnClickListener(this); 
    } 
    
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.main_home:
			setChoiceFragment(R.id.main_home);
			if(CommonSlidMenu.mSlidMenu!=null && CommonSlidMenu.mSlidMenu.isSlidingEnabled()){ 
				CommonSlidMenu.mSlidMenu.setSlidingEnabled(false); 
			} 
			break;
		case R.id.main_stadium:
			if((UserGlobal.UserExist && UserGlobal.UserID!=-1) || UserGlobal.SYSTEM){//判断用户是否已经正常登录
				setChoiceFragment(R.id.main_stadium); 
				if(CommonSlidMenu.mSlidMenu!=null && !CommonSlidMenu.mSlidMenu.isSlidingEnabled()){ 
					CommonSlidMenu.mSlidMenu.setSlidingEnabled(true);
					CommonSlidMenu.mSlidMenu.toggle();
				}
			}else{
				Toast.makeText(MainActivity.this, "请先登录", 0).show();
				Intent intenteLogin=new Intent(MainActivity.this,Login.class);
				MainActivity.this.startActivity(intenteLogin);
			} 
			break;
		case R.id.main_personalcenter:
			if((UserGlobal.UserExist && UserGlobal.UserID!=-1) || UserGlobal.SYSTEM){//如果用户成功登录就可以切换页面操作
				setChoiceFragment(R.id.main_personalcenter);
				if(CommonSlidMenu.mSlidMenu!=null && CommonSlidMenu.mSlidMenu.isSlidingEnabled()){ 
					CommonSlidMenu.mSlidMenu.setSlidingEnabled(false); 
				}
			}else{//没有登录就跳转到登录界面
				Toast.makeText(MainActivity.this, "请先登录", 0).show();
				Intent jump=new Intent(MainActivity.this,Login.class);
				startActivity(jump);//跳转到登录界面
			}
			break;
		} 
	}
	
	/**对用户点击的底部进行处理 */
	public void setChoiceFragment(int id){
		//开启FragmentTransaction事务,最后需要提交事务
		FragmentTransaction transaction=fm.beginTransaction(); 
		
		//首先重置+隐藏所有的Fragment,避免出现界面不流畅的现象发生
		resetAllFragment();//重置Fragment里面的所有组件
		hideAllFragment(transaction);//隐藏 
		switch(id){
		case R.id.main_home:
			//设置ImageView 
			main_home_icon.setImageResource(R.drawable.home_select);
			if(mHomeFragment==null){
				mHomeFragment=new HomeFragment();
				transaction.add(R.id.main_framelayout, mHomeFragment,"HomeFragment");
			}else{
				transaction.show(mHomeFragment);
			}
			break;
		case R.id.main_stadium:
			main_stadium_icon.setImageResource(R.drawable.stadium_select);
			if(mStadiumFragment==null){
				mStadiumFragment=new StadiumFragment();
				transaction.add(R.id.main_framelayout, mStadiumFragment,"StadiumFragment");
			}else{
				transaction.show(mStadiumFragment);
			}
			break;
		case R.id.main_personalcenter:
			//设置ImageView 
			main_personalcenter_icon.setImageResource(R.drawable.personal_select);
			if(mPersonalFragment==null){
				mPersonalFragment=new PersonalFragment();
				transaction.add(R.id.main_framelayout, mPersonalFragment,"PersonalFragment");
			}else{
				transaction.show(mPersonalFragment);
			}
			break;
		}
		//切记需要提交事务，否则会不显示界面
		transaction.commit();
	}
    
    
	/** 重置Fragment里面的所有组件(包括ImageView,TextView) */
	private void resetAllFragment(){
		//设置ImageView重置 
		main_home_icon.setImageResource(R.drawable.home_normal);
		main_stadium_icon.setImageResource(R.drawable.stadium_normal);
		main_personalcenter_icon.setImageResource(R.drawable.personal_normal); 
	}
	
	/** 把Fragment页面全部隐藏 */
	public void hideAllFragment(FragmentTransaction transaction){
		if(mHomeFragment!=null){
			transaction.hide(mHomeFragment);
		}
		if(mStadiumFragment!=null){
			transaction.hide(mStadiumFragment);
		}
		if(mPersonalFragment!=null){
			transaction.hide(mPersonalFragment);
		}
	}
	
	
    /** 菜单*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { 
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) { 
//		stopService(UserGlobal.user_intent);//停止开启的普通用户服务
//		stopService(UserGlobal.admin_intent);//停止开启的场馆用户服务
		Exit.getInstance().destroyActivity();//销毁所有的Activity对象
		android.os.Process.killProcess(android.os.Process.myPid());//直接杀死进程进行退出软件
		return super.onOptionsItemSelected(item);
	}
	
	private int mBackKeyPressedTimes=0;
	//后退键
	@Override
	public void onBackPressed() { 
		if(mBackKeyPressedTimes==0){
			Toast.makeText(MainActivity.this, "再按一次退出程序", 0).show();
			mBackKeyPressedTimes=1;
			new Thread(){
				public void run(){
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) { 
						e.printStackTrace();
					}finally{
						mBackKeyPressedTimes=0;
					}
				}
			}.start();
			return;
		}else{
//			stopService(UserGlobal.user_intent);//停止开启的普通用户服务
//			stopService(UserGlobal.admin_intent);//停止开启的场馆用户服务
			Exit.getInstance().destroyActivity();//销毁所有的Activity对象
			android.os.Process.killProcess(android.os.Process.myPid());//直接杀死进程进行退出软件
		} 
		super.onBackPressed(); 
	}
	
	/** 加载运动类型数据*/
	public void conditionQuery() {   
		new Thread(){   
			public void run() {
				try { 
					List<GetSportBean> results=WebGetSports.getGetSportsData("");
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
				}  
			};
		}.start();
	}
    
}
