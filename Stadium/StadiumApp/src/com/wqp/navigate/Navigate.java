package com.wqp.navigate;
 
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat; 
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.interpolator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
   
import com.sina.weibo.sdk.auth.Oauth2AccessToken;  
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener; 
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.connect.UserInfo; 
import com.tencent.connect.common.Constants; 
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.wqp.sina.Sina_Constants; 
import com.wqp.stadiumapp.Login;
import com.wqp.stadiumapp.MainActivity;
import com.wqp.stadiumapp.R;
import com.wqp.stadiumapp.RegisterNewUser;
import com.wqp.stadiumapp.entity.SinaWeiboSharedPreferencesBean;
import com.wqp.stadiumapp.entity.TPOSLoginBean;
import com.wqp.stadiumapp.utils.Exit;
import com.wqp.stadiumapp.utils.ToolsNavigate;

public class Navigate extends Activity implements OnClickListener {
	private static String TAG="Navigate";
	private ViewPager mViewPager;
	private ImageView[] mDots;//存放底部每个小点的ImageView，小点是用ImageView组件 
	private View[] mView;
	private LinearLayout mDotLayout;//小点的布局
	private List<View> mViewList;//viewpager当中的所有视图
	private LayoutInflater inflater;
	private int currentIndex;//当前被选中的页面
	private NavigationPagerAdapter adapter;
	private SharedPreferences mSharedPrefs;
	private SharedPreferences.Editor mEditor;
	
	

	/**使用第三方插件声明QQ登录*/ 
	private UserInfo mUserInfo;
	private static Tencent mTencent;
	private String mNickName=null;
	private Bitmap mBitmap=null;
	
	/**使用第三方插件 微博登录*/
	/** 微博 Web 授权类，提供登陆等功能  */
	private WeiboAuth mWeiboAuth; 
	/** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
	private Oauth2AccessToken mAccessToken;
	/** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
	private SsoHandler mSsoHandler;
	
	/** 定义一个消息管理器*/
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case ToolsNavigate.TPOS_LOGIN_BITMAP:
				mBitmap=(Bitmap) msg.obj;
				if(mBitmap!=null && mNickName!=null){
					Intent intent=new Intent(Navigate.this,MainActivity.class);
					TPOSLoginBean tpos=new TPOSLoginBean();
					tpos.setBitmap(mBitmap);//存入头像
					tpos.setNickname(mNickName);//存入用户昵称
					ToolsNavigate.TPOS_LOG=tpos;
					startActivity(intent);
					finish();
				}
				break;
			}
		};
	};
	
	
	//导航栏的每一页背景图片
	private int[] navs={
			R.drawable.navigation_one1,R.drawable.navigation_two2,
			R.drawable.navigation_three3,R.drawable.navigation_four4,
			R.drawable.navigation_five5 };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.navigation); 
		Exit.getInstance().addActivity(this);//把Activity添加到退出应用的集合里面
		
		mViewPager=(ViewPager) findViewById(R.id.navigation);
		inflater=LayoutInflater.from(this);
		initNavigate();//初始化
		
		initDots();//初始化底部的小点 
		
	}
	
	//对导航栏的初始化
	@SuppressWarnings("deprecation")
	private void initNavigate(){
		mViewList=new ArrayList<View>();
		mView=new View[5];
		int sdk=android.os.Build.VERSION.SDK_INT;
		//导航栏的第一个页面
		mView[0]=inflater.inflate(R.layout.navigation_one, null);
		RelativeLayout navigation_one = (RelativeLayout) mView[0].findViewById(R.id.anim_navigation_one);
		Animation anim_navigation_one=new TranslateAnimation(-50, 0, 0, 0);
		anim_navigation_one.setInterpolator(new Interpolator() {
			
			@Override
			public float getInterpolation(float input) { 
				
				return 0.5f;
			}
		});//先加速再减速
		anim_navigation_one.setDuration(3000);
		navigation_one.setAnimation(anim_navigation_one);
		
		ImageView one_register_login=(ImageView) mView[0].findViewById(R.id.navigation_one_register_login);
		ImageView one_random_look=(ImageView) mView[0].findViewById(R.id.navigation_one_random_look);
		one_register_login.setOnClickListener(this);//为导航页面一的注册登录设置点击事件
		one_random_look.setOnClickListener(this);//为导航页面一的随便看看设置点击事件 
		mViewList.add(mView[0]);
		
		//导航栏第2-4页面 
		for(int i=1;i<=3;i++){
			mView[i]=inflater.inflate(R.layout.navigation_two_and_four, null); 
			if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN){
				mView[i].setBackgroundDrawable(this.getResources().getDrawable(navs[i]));
			}else{
				mView[i].setBackground(this.getResources().getDrawable(navs[i]));
			} 
			mViewList.add(mView[i]);
		}
		
		//导航栏的第五个页面
		mView[4]=inflater.inflate(R.layout.navigation_five, null);
		ImageView five_weibo=(ImageView) mView[4].findViewById(R.id.navigation_five_weibo);
		ImageView five_qq=(ImageView) mView[4].findViewById(R.id.navigation_five_qq);
		ImageView five_email=(ImageView) mView[4].findViewById(R.id.navigation_five_email);
		ImageView five_register=(ImageView) mView[4].findViewById(R.id.navigation_five_register);
		ImageView five_random_look=(ImageView) mView[4].findViewById(R.id.navigation_five_random_look);
		
		five_weibo.setOnClickListener(this);//为导航页面五的微博设置点击事件
		five_qq.setOnClickListener(this);//为导航页面五的qq设置点击事件
		five_email.setOnClickListener(this);//为导航页面五的邮箱/场馆汇设置点击事件
		five_register.setOnClickListener(this);//为导航页面五的注册设置点击事件
		five_random_look.setOnClickListener(this);//为导航页面五的随便看看设置点击事件 
		mViewList.add(mView[4]);
		
		//为ViewPager设置适配器
		adapter=new NavigationPagerAdapter(mViewList);
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(0);//设置ViewPager默认是第1页
		mViewPager.setOnPageChangeListener(new NavigationOnPageChangeListener());//为ViewPager设置响应事件
	}
	
	/**
	 * 初始化导航栏上面的四个小点图标
	 */
	private void initDots(){
		mDotLayout=(LinearLayout) findViewById(R.id.dot_layout);
		mDots=new ImageView[mViewList.size()];
		for(int i=0;i<mDots.length;i++){
			mDots[i]=(ImageView) mDotLayout.getChildAt(i);
			mDots[i].setImageResource(R.drawable.dot_white_hollow);
		}
		currentIndex=0;//设置默认被选中的页面
		mDots[currentIndex].setImageResource(R.drawable.dot_orange);//设置为选中状态 
	}

	@Override
	public void onClick(View v) {
		Intent intent=null;
		switch(v.getId()){
		case R.id.navigation_one_register_login://导航界面一的注册、登录按钮,直接跳转到登陆界面 
			mViewPager.setCurrentItem(4);
			mDots[4].setImageResource(R.drawable.dot_orange);
			break;
		case R.id.navigation_one_random_look://导航界面一的随便看看按钮 
			intent=new Intent(Navigate.this,MainActivity.class);
			startActivity(intent);
			finish(); 
			break;
		case R.id.navigation_five_weibo://导航界面五的微博按钮
			Toast.makeText(getApplicationContext(), "使用微博帐号登录", 0).show();
			if(ToolsNavigate.isNetworkAvailable(Navigate.this)){
				onClickSinaWeiboLogin();//进入第三方新浪微博登录验证
			}else{
				Toast.makeText(Navigate.this, "亲,网络没打开哦", 0).show();
			} 
			break;
		case R.id.navigation_five_qq://导航界面五的qq登录按钮
			Toast.makeText(getApplicationContext(), "使用QQ收号登录", 0).show(); 
			if(ToolsNavigate.isNetworkAvailable(Navigate.this)){
				onClickQQLogin();//进入 第三方登录验证
			}else{
				Toast.makeText(Navigate.this, "亲,网络没打开哦", 0).show();
			}
			break;
		case R.id.navigation_five_email://导航界面五的邮箱/场馆汇按钮
//			Toast.makeText(getApplicationContext(), "使用邮箱/场馆汇登录", 0).show();
			intent=new Intent(Navigate.this,Login.class);
			startActivity(intent); 
			break;
		case R.id.navigation_five_register://导航界面五的注册按钮
//			Toast.makeText(getApplicationContext(), "注册帐号", 0).show();
			intent=new Intent(Navigate.this,RegisterNewUser.class);
			startActivity(intent);
			break;
		case R.id.navigation_five_random_look://导航界面五的随便看看按钮
//			intent=new Intent(Navigate.this,MainActivity.class);
//			startActivity(intent);
			MainActivity.launch(Navigate.this);//调用了MainActivity类的API
			finish();  
			break; 
		} 
	}
	
	/**
	 * 为ViewPage设置滑动事件
	 * @author Administrator
	 *
	 */
	private class NavigationOnPageChangeListener implements OnPageChangeListener{ 
		@Override
		public void onPageScrollStateChanged(int arg0) { }

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) { }

		@Override
		public void onPageSelected(int position) {
			//当页面处在滑动时设置小点的样式 
			// 判断是否越界了
			updateSet(position); 
		} 
	}
	
	private void updateSet(int position) {
		if (position < 0 || position > mViewList.size() - 1) { return; }
		mDots[position].setImageResource(R.drawable.dot_orange);
		mDots[currentIndex].setImageResource(R.drawable.dot_white_hollow);
		currentIndex=position;
	} 
	
	@Override
	protected void onNewIntent(Intent intent) { 
		super.onNewIntent(intent);
		if((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags())!=0){
			finish();
		}
	}
	
	
	/** 
	 * QQ帐户登录应用程序部分 ,这一部分有使用到Handler接收一个标识ToolsNavigate.QQ_LOGIN_BITMAP
	 */
	private void onClickQQLogin(){
		//为第三插件QQ帐号登陆注册 
		mTencent=Tencent.createInstance("1103884834", getApplicationContext());
		if(!mTencent.isSessionValid()){
			mTencent.login(this, "all",loginListener);
			Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
		}else{
			mTencent.logout(this);
			updateUserInfo();
		}
	}
	
	IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
        	Log.d("SDKQQAgentPref", "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());//test
            initOpenidAndToken(values);
            updateUserInfo(); 
        }
    };
    
	private class BaseUiListener implements IUiListener { 
		@Override
		public void onCancel() {  }

		@Override
		public void onComplete(Object response) { 
			Log.i(TAG,"login success");//登录成功
			doComplete((JSONObject)response);
		}

		protected void doComplete(JSONObject values) {
			Log.i(TAG,"这个是在BaseUiListener类中接收到的数据:"+values);
		}
		
		@Override
		public void onError(UiError e) { } 
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    Log.d(TAG, "-->onActivityResult请求码:" + requestCode  + " resultCode结果码:" + resultCode);
	    if(requestCode == Constants.REQUEST_API) {
	        if(resultCode == Constants.RESULT_LOGIN) {
	            Tencent.handleResultData(data, loginListener);
	            Log.d(TAG, "-->onActivityResult handle logindata");
	        }
	    } else if (requestCode == Constants.REQUEST_APPBAR) { 
	    	if (resultCode == Constants.RESULT_LOGIN) {
	    		updateUserInfo(); 
	    	}
	    }
	    
		 // 这个是新浪微博 ，SSO 授权回调
		 // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
		 if (mSsoHandler != null) {
			 mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		 } 
	    super.onActivityResult(requestCode, resultCode, data);
	}
	
	public static void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch(Exception e) {
        }
    } 
	
	private void updateUserInfo(){
		Log.i(TAG, mTencent.isSessionValid()+"验证");
		
		if (mTencent!= null && mTencent.isSessionValid()){
			IUiListener listener = new IUiListener(){ 
				@Override
				public void onCancel() { } 
				@Override
				public void onError(UiError e) { Log.i(TAG,e.toString()); }
				
				@Override
				public void onComplete(final Object response) { 
					Log.i(TAG,"login success response 结果从这里返回的，如下:");//登录成功
					JSONObject res=(JSONObject) response;
					Log.i(TAG,res.toString());//把接收到的数据打印在控制台上面
					if(res.has("nickname")){
						try {
							Log.i(TAG,res.getString("province"));//获取省份
							Log.i(TAG,res.getString("city"));//获取城市
							Log.i(TAG,res.getString("gender"));//获取性别
							mNickName=res.getString("nickname");//获得昵称
						} catch (JSONException e) { 
							e.printStackTrace();
						}
					}
					//开启线程获取到用户头像
					new Thread(){
						public void run() {
							JSONObject json=(JSONObject) response;
							if(json.has("figureurl")){ 
								Bitmap tempBitmap=null;
								try {
									tempBitmap=ToolsNavigate.getBitmap(json.getString("figureurl_qq_2"));
								} catch (JSONException e) { 
									e.printStackTrace();
								}
								Message msg=new Message();
								msg.obj=tempBitmap;
								msg.what=ToolsNavigate.TPOS_LOGIN_BITMAP;//发送头像标识
								mHandler.sendMessage(msg);
							}
						};
					}.start();
				} 
			};
			mUserInfo = new UserInfo(this, mTencent.getQQToken());
			mUserInfo.getUserInfo(listener);
		}else{
			Log.i(TAG,"mTencent!= null && mTencent.isSessionValid() >>> false,为空了，请确认");
		} 
	}
	
	
	/** 
	 * 进入第三方新浪微博登录验证
	 */
	private void onClickSinaWeiboLogin(){
		//创建微博实例
		// 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
		mWeiboAuth=new WeiboAuth(this, Sina_Constants.APP_KEY, Sina_Constants.REDIRECT_URL, Sina_Constants.SCOPE);	
		
		//判断SharedPreferences当中是否有配置好的参数
		SinaWeiboSharedPreferencesBean swspb=getSinaWeiboSharedPreferences();
		if(swspb!=null){ 
			//根据用户存储的参数直接登录
			getSinaWeiboUserInfoThread(swspb.getAccess_token(),swspb.getExpires_in(),swspb.getUid()); 
		}else{
			//通过单点登录 (SSO) 获取 Token
			mSsoHandler=new SsoHandler(Navigate.this, mWeiboAuth);
			mSsoHandler.authorize(new AuthListener());
		} 
	}
	
	/** 微博认证授权回调类*/
	private class AuthListener implements WeiboAuthListener{ 
		@Override
		public void onCancel() { 
			Log.i("SinaWeiboLogin","微博认证取消授权了");
		}
		
		@Override
		public void onWeiboException(WeiboException e) { 
			Log.i("SinaWeiboLogin","新浪微博授权失败,onWeiboException="+e.toString());
		} 
		
		@Override
		public void onComplete(Bundle bundle) {
			//从bundle中解析Token
			mAccessToken=Oauth2AccessToken.parseAccessToken(bundle);
			Log.i("SinaWeiboLogin","新浪微博登录的结果回调方法被使用了,"+mAccessToken.getToken());
			if(mAccessToken.isSessionValid()){ 
				 
				String access_token=bundle.getString("access_token");
				String expires_in=bundle.getString("expires_in");
				String uid=bundle.getString("uid"); 
				String code=bundle.getString("code");
				String date=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(mAccessToken.getExpiresTime()));	
				
				//保存到SharedPreferences 保存数据
				Log.i("SinaWeiboLogin","access_token="+access_token+",expires_in="+expires_in+",date="+date+",uid="+uid+",code="+code);  
				
				setSinaWeiboSharedPreferences(access_token,expires_in,uid);
				
				//传入GET方法的请求地址,即用户信息API 
				final String result=getUserInfo(access_token,expires_in,uid);//获取用户的信息JSON格式的数据
				//开启线程下载头像的图片
				new Thread(){
					public void run() {
						JSONObject json;
						try {
							json = new JSONObject(result);
							mNickName=json.getString("screen_name");//获取到新浪微博的用户昵称
							if(json.has("avatar_hd")){
								Bitmap tempBitmap=null;
								tempBitmap=ToolsNavigate.getBitmap(json.getString("avatar_hd"));
								Message msg=new Message();
								msg.obj=tempBitmap;
								msg.what=ToolsNavigate.TPOS_LOGIN_BITMAP;//用户mHandler标识通过 TPOSLoginBean 进行封装数据
								mHandler.sendMessage(msg);
							}else{ }
						} 
						catch(JSONException e){
							e.printStackTrace();
						}  
					};
				}.start(); 
			}else{
				//您注册的应用签名不正确时，就会收到code,请确保签名正确
				String code=bundle.getString("code","");
				Log.i("SinaWeiboLogin","您微博的应用签名不正确,返回码是:"+code);
			} 
		}  
	}
	
	/**
	 * 新浪用户请求接口,根据传入的权限标识，获取到用户的基本信息
	 * @param access_token 
	 * @param expires_in
	 * @param uid 用户信息id字段
	 * @return JSON格式的字符串数据
	 */
	private String getUserInfo(String access_token,String expires_in,String uid){
		String path = "https://api.weibo.com/2/users/show.json?access_token="+access_token+"&uid="+uid;
		try {   
			URL url = new URL(path);  
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();   
			conn.setConnectTimeout(5000);   
			conn.setRequestMethod("GET");   
			conn.setDoInput(true);   
			int code = conn.getResponseCode();   
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			byte[] buffer = new byte[1024];   
			int len = 0;   
			System.out.println("服务端的返回码 code = "+code);   
			if(code == 200){  
				InputStream is = conn.getInputStream();   
				while ((len = is.read(buffer)) != -1) {     
					baos.write(buffer, 0, len);    
				}  
				//这里获取到的是JSON格式的字符串
				String response = new String(baos.toByteArray());    
				System.out.println("打印user对象**********  "+response); 
				return response;
			}  
			} catch (Exception e) {} 
		return null;
	}
	
	/** 保存新浪微博用户登录成功后的参数到SharedPreferences当中 */
	public void setSinaWeiboSharedPreferences(String access_token, String expires_in, String uid) { 
		if(access_token!=null && expires_in!=null && uid!=null){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date=sdf.format(new Date());//保存时间  
			
			mSharedPrefs=getSharedPreferences("sina", Context.MODE_PRIVATE);
			mEditor=mSharedPrefs.edit();
			
			mEditor.putString("date", date);
			mEditor.putString("uid", uid);
			mEditor.putString("expires_in", expires_in);
			mEditor.putString("access_token", access_token);
			mEditor.commit();
		}else{
			Log.i("Navigate","新浪微博用户配置参数保存无效");
		}
	}
	
	/** 从新浪微博用户登录成功后的参数到SharedPreferences当中获取access_token,uid*/
	public SinaWeiboSharedPreferencesBean getSinaWeiboSharedPreferences(){
		mSharedPrefs=getSharedPreferences("sina", Context.MODE_PRIVATE);
		String loc_date=mSharedPrefs.getString("date", null);
		String loc_access_token=mSharedPrefs.getString("access_token", null);
		String loc_expires_in=mSharedPrefs.getString("expires_in", null);
		String loc_uid=mSharedPrefs.getString("uid", null);
		if(loc_access_token!=null && loc_expires_in!=null && loc_uid!=null){
			SinaWeiboSharedPreferencesBean sspb=new SinaWeiboSharedPreferencesBean();
			sspb.setDate(loc_date);
			sspb.setAccess_token(loc_access_token);
			sspb.setExpires_in(loc_expires_in);
			sspb.setUid(loc_uid);
			return sspb;
		}
		return null;
	}
	
	/** 通过线程获取到用户的信息*/
	private void getSinaWeiboUserInfoThread(String access_token, String expires_in, String uid){
		//传入GET方法的请求地址,即用户信息API 
		final String result=getUserInfo(access_token,expires_in,uid);//获取用户的信息JSON格式的数据
		//开启线程下载头像的图片
		new Thread(){
			public void run() {
				JSONObject json;
				try {
					json = new JSONObject(result);
					mNickName=json.getString("screen_name");//获取到新浪微博的用户昵称
					if(json.has("avatar_hd")){
						Bitmap tempBitmap=null;
						tempBitmap=ToolsNavigate.getBitmap(json.getString("avatar_hd"));
						Message msg=new Message();
						msg.obj=tempBitmap;
						msg.what=ToolsNavigate.TPOS_LOGIN_BITMAP;//用户mHandler标识通过 TPOSLoginBean 进行封装数据
						mHandler.sendMessage(msg);
					}else{ }
				} 
				catch(JSONException e){
					e.printStackTrace();
				}  
			};
		}.start();
	}
	
}
