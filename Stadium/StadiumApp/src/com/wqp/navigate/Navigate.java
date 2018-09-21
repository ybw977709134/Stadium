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
	private ImageView[] mDots;//��ŵײ�ÿ��С���ImageView��С������ImageView��� 
	private View[] mView;
	private LinearLayout mDotLayout;//С��Ĳ���
	private List<View> mViewList;//viewpager���е�������ͼ
	private LayoutInflater inflater;
	private int currentIndex;//��ǰ��ѡ�е�ҳ��
	private NavigationPagerAdapter adapter;
	private SharedPreferences mSharedPrefs;
	private SharedPreferences.Editor mEditor;
	
	

	/**ʹ�õ������������QQ��¼*/ 
	private UserInfo mUserInfo;
	private static Tencent mTencent;
	private String mNickName=null;
	private Bitmap mBitmap=null;
	
	/**ʹ�õ�������� ΢����¼*/
	/** ΢�� Web ��Ȩ�࣬�ṩ��½�ȹ���  */
	private WeiboAuth mWeiboAuth; 
	/** ��װ�� "access_token"��"expires_in"��"refresh_token"�����ṩ�����ǵĹ�����  */
	private Oauth2AccessToken mAccessToken;
	/** ע�⣺SsoHandler ���� SDK ֧�� SSO ʱ��Ч */
	private SsoHandler mSsoHandler;
	
	/** ����һ����Ϣ������*/
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case ToolsNavigate.TPOS_LOGIN_BITMAP:
				mBitmap=(Bitmap) msg.obj;
				if(mBitmap!=null && mNickName!=null){
					Intent intent=new Intent(Navigate.this,MainActivity.class);
					TPOSLoginBean tpos=new TPOSLoginBean();
					tpos.setBitmap(mBitmap);//����ͷ��
					tpos.setNickname(mNickName);//�����û��ǳ�
					ToolsNavigate.TPOS_LOG=tpos;
					startActivity(intent);
					finish();
				}
				break;
			}
		};
	};
	
	
	//��������ÿһҳ����ͼƬ
	private int[] navs={
			R.drawable.navigation_one1,R.drawable.navigation_two2,
			R.drawable.navigation_three3,R.drawable.navigation_four4,
			R.drawable.navigation_five5 };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.navigation); 
		Exit.getInstance().addActivity(this);//��Activity��ӵ��˳�Ӧ�õļ�������
		
		mViewPager=(ViewPager) findViewById(R.id.navigation);
		inflater=LayoutInflater.from(this);
		initNavigate();//��ʼ��
		
		initDots();//��ʼ���ײ���С�� 
		
	}
	
	//�Ե������ĳ�ʼ��
	@SuppressWarnings("deprecation")
	private void initNavigate(){
		mViewList=new ArrayList<View>();
		mView=new View[5];
		int sdk=android.os.Build.VERSION.SDK_INT;
		//�������ĵ�һ��ҳ��
		mView[0]=inflater.inflate(R.layout.navigation_one, null);
		RelativeLayout navigation_one = (RelativeLayout) mView[0].findViewById(R.id.anim_navigation_one);
		Animation anim_navigation_one=new TranslateAnimation(-50, 0, 0, 0);
		anim_navigation_one.setInterpolator(new Interpolator() {
			
			@Override
			public float getInterpolation(float input) { 
				
				return 0.5f;
			}
		});//�ȼ����ټ���
		anim_navigation_one.setDuration(3000);
		navigation_one.setAnimation(anim_navigation_one);
		
		ImageView one_register_login=(ImageView) mView[0].findViewById(R.id.navigation_one_register_login);
		ImageView one_random_look=(ImageView) mView[0].findViewById(R.id.navigation_one_random_look);
		one_register_login.setOnClickListener(this);//Ϊ����ҳ��һ��ע���¼���õ���¼�
		one_random_look.setOnClickListener(this);//Ϊ����ҳ��һ����㿴�����õ���¼� 
		mViewList.add(mView[0]);
		
		//��������2-4ҳ�� 
		for(int i=1;i<=3;i++){
			mView[i]=inflater.inflate(R.layout.navigation_two_and_four, null); 
			if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN){
				mView[i].setBackgroundDrawable(this.getResources().getDrawable(navs[i]));
			}else{
				mView[i].setBackground(this.getResources().getDrawable(navs[i]));
			} 
			mViewList.add(mView[i]);
		}
		
		//�������ĵ����ҳ��
		mView[4]=inflater.inflate(R.layout.navigation_five, null);
		ImageView five_weibo=(ImageView) mView[4].findViewById(R.id.navigation_five_weibo);
		ImageView five_qq=(ImageView) mView[4].findViewById(R.id.navigation_five_qq);
		ImageView five_email=(ImageView) mView[4].findViewById(R.id.navigation_five_email);
		ImageView five_register=(ImageView) mView[4].findViewById(R.id.navigation_five_register);
		ImageView five_random_look=(ImageView) mView[4].findViewById(R.id.navigation_five_random_look);
		
		five_weibo.setOnClickListener(this);//Ϊ����ҳ�����΢�����õ���¼�
		five_qq.setOnClickListener(this);//Ϊ����ҳ�����qq���õ���¼�
		five_email.setOnClickListener(this);//Ϊ����ҳ���������/���ݻ����õ���¼�
		five_register.setOnClickListener(this);//Ϊ����ҳ�����ע�����õ���¼�
		five_random_look.setOnClickListener(this);//Ϊ����ҳ�������㿴�����õ���¼� 
		mViewList.add(mView[4]);
		
		//ΪViewPager����������
		adapter=new NavigationPagerAdapter(mViewList);
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(0);//����ViewPagerĬ���ǵ�1ҳ
		mViewPager.setOnPageChangeListener(new NavigationOnPageChangeListener());//ΪViewPager������Ӧ�¼�
	}
	
	/**
	 * ��ʼ��������������ĸ�С��ͼ��
	 */
	private void initDots(){
		mDotLayout=(LinearLayout) findViewById(R.id.dot_layout);
		mDots=new ImageView[mViewList.size()];
		for(int i=0;i<mDots.length;i++){
			mDots[i]=(ImageView) mDotLayout.getChildAt(i);
			mDots[i].setImageResource(R.drawable.dot_white_hollow);
		}
		currentIndex=0;//����Ĭ�ϱ�ѡ�е�ҳ��
		mDots[currentIndex].setImageResource(R.drawable.dot_orange);//����Ϊѡ��״̬ 
	}

	@Override
	public void onClick(View v) {
		Intent intent=null;
		switch(v.getId()){
		case R.id.navigation_one_register_login://��������һ��ע�ᡢ��¼��ť,ֱ����ת����½���� 
			mViewPager.setCurrentItem(4);
			mDots[4].setImageResource(R.drawable.dot_orange);
			break;
		case R.id.navigation_one_random_look://��������һ����㿴����ť 
			intent=new Intent(Navigate.this,MainActivity.class);
			startActivity(intent);
			finish(); 
			break;
		case R.id.navigation_five_weibo://�����������΢����ť
			Toast.makeText(getApplicationContext(), "ʹ��΢���ʺŵ�¼", 0).show();
			if(ToolsNavigate.isNetworkAvailable(Navigate.this)){
				onClickSinaWeiboLogin();//�������������΢����¼��֤
			}else{
				Toast.makeText(Navigate.this, "��,����û��Ŷ", 0).show();
			} 
			break;
		case R.id.navigation_five_qq://�����������qq��¼��ť
			Toast.makeText(getApplicationContext(), "ʹ��QQ�պŵ�¼", 0).show(); 
			if(ToolsNavigate.isNetworkAvailable(Navigate.this)){
				onClickQQLogin();//���� ��������¼��֤
			}else{
				Toast.makeText(Navigate.this, "��,����û��Ŷ", 0).show();
			}
			break;
		case R.id.navigation_five_email://���������������/���ݻ㰴ť
//			Toast.makeText(getApplicationContext(), "ʹ������/���ݻ��¼", 0).show();
			intent=new Intent(Navigate.this,Login.class);
			startActivity(intent); 
			break;
		case R.id.navigation_five_register://�����������ע�ᰴť
//			Toast.makeText(getApplicationContext(), "ע���ʺ�", 0).show();
			intent=new Intent(Navigate.this,RegisterNewUser.class);
			startActivity(intent);
			break;
		case R.id.navigation_five_random_look://�������������㿴����ť
//			intent=new Intent(Navigate.this,MainActivity.class);
//			startActivity(intent);
			MainActivity.launch(Navigate.this);//������MainActivity���API
			finish();  
			break; 
		} 
	}
	
	/**
	 * ΪViewPage���û����¼�
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
			//��ҳ�洦�ڻ���ʱ����С�����ʽ 
			// �ж��Ƿ�Խ����
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
	 * QQ�ʻ���¼Ӧ�ó��򲿷� ,��һ������ʹ�õ�Handler����һ����ʶToolsNavigate.QQ_LOGIN_BITMAP
	 */
	private void onClickQQLogin(){
		//Ϊ�������QQ�ʺŵ�½ע�� 
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
			Log.i(TAG,"login success");//��¼�ɹ�
			doComplete((JSONObject)response);
		}

		protected void doComplete(JSONObject values) {
			Log.i(TAG,"�������BaseUiListener���н��յ�������:"+values);
		}
		
		@Override
		public void onError(UiError e) { } 
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    Log.d(TAG, "-->onActivityResult������:" + requestCode  + " resultCode�����:" + resultCode);
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
	    
		 // ���������΢�� ��SSO ��Ȩ�ص�
		 // ��Ҫ������ SSO ��½�� Activity ������д onActivityResult
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
		Log.i(TAG, mTencent.isSessionValid()+"��֤");
		
		if (mTencent!= null && mTencent.isSessionValid()){
			IUiListener listener = new IUiListener(){ 
				@Override
				public void onCancel() { } 
				@Override
				public void onError(UiError e) { Log.i(TAG,e.toString()); }
				
				@Override
				public void onComplete(final Object response) { 
					Log.i(TAG,"login success response ��������ﷵ�صģ�����:");//��¼�ɹ�
					JSONObject res=(JSONObject) response;
					Log.i(TAG,res.toString());//�ѽ��յ������ݴ�ӡ�ڿ���̨����
					if(res.has("nickname")){
						try {
							Log.i(TAG,res.getString("province"));//��ȡʡ��
							Log.i(TAG,res.getString("city"));//��ȡ����
							Log.i(TAG,res.getString("gender"));//��ȡ�Ա�
							mNickName=res.getString("nickname");//����ǳ�
						} catch (JSONException e) { 
							e.printStackTrace();
						}
					}
					//�����̻߳�ȡ���û�ͷ��
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
								msg.what=ToolsNavigate.TPOS_LOGIN_BITMAP;//����ͷ���ʶ
								mHandler.sendMessage(msg);
							}
						};
					}.start();
				} 
			};
			mUserInfo = new UserInfo(this, mTencent.getQQToken());
			mUserInfo.getUserInfo(listener);
		}else{
			Log.i(TAG,"mTencent!= null && mTencent.isSessionValid() >>> false,Ϊ���ˣ���ȷ��");
		} 
	}
	
	
	/** 
	 * �������������΢����¼��֤
	 */
	private void onClickSinaWeiboLogin(){
		//����΢��ʵ��
		// ������Ȩʱ���벻Ҫ���� SCOPE��������ܻ���Ȩ���ɹ�
		mWeiboAuth=new WeiboAuth(this, Sina_Constants.APP_KEY, Sina_Constants.REDIRECT_URL, Sina_Constants.SCOPE);	
		
		//�ж�SharedPreferences�����Ƿ������úõĲ���
		SinaWeiboSharedPreferencesBean swspb=getSinaWeiboSharedPreferences();
		if(swspb!=null){ 
			//�����û��洢�Ĳ���ֱ�ӵ�¼
			getSinaWeiboUserInfoThread(swspb.getAccess_token(),swspb.getExpires_in(),swspb.getUid()); 
		}else{
			//ͨ�������¼ (SSO) ��ȡ Token
			mSsoHandler=new SsoHandler(Navigate.this, mWeiboAuth);
			mSsoHandler.authorize(new AuthListener());
		} 
	}
	
	/** ΢����֤��Ȩ�ص���*/
	private class AuthListener implements WeiboAuthListener{ 
		@Override
		public void onCancel() { 
			Log.i("SinaWeiboLogin","΢����֤ȡ����Ȩ��");
		}
		
		@Override
		public void onWeiboException(WeiboException e) { 
			Log.i("SinaWeiboLogin","����΢����Ȩʧ��,onWeiboException="+e.toString());
		} 
		
		@Override
		public void onComplete(Bundle bundle) {
			//��bundle�н���Token
			mAccessToken=Oauth2AccessToken.parseAccessToken(bundle);
			Log.i("SinaWeiboLogin","����΢����¼�Ľ���ص�������ʹ����,"+mAccessToken.getToken());
			if(mAccessToken.isSessionValid()){ 
				 
				String access_token=bundle.getString("access_token");
				String expires_in=bundle.getString("expires_in");
				String uid=bundle.getString("uid"); 
				String code=bundle.getString("code");
				String date=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(mAccessToken.getExpiresTime()));	
				
				//���浽SharedPreferences ��������
				Log.i("SinaWeiboLogin","access_token="+access_token+",expires_in="+expires_in+",date="+date+",uid="+uid+",code="+code);  
				
				setSinaWeiboSharedPreferences(access_token,expires_in,uid);
				
				//����GET�����������ַ,���û���ϢAPI 
				final String result=getUserInfo(access_token,expires_in,uid);//��ȡ�û�����ϢJSON��ʽ������
				//�����߳�����ͷ���ͼƬ
				new Thread(){
					public void run() {
						JSONObject json;
						try {
							json = new JSONObject(result);
							mNickName=json.getString("screen_name");//��ȡ������΢�����û��ǳ�
							if(json.has("avatar_hd")){
								Bitmap tempBitmap=null;
								tempBitmap=ToolsNavigate.getBitmap(json.getString("avatar_hd"));
								Message msg=new Message();
								msg.obj=tempBitmap;
								msg.what=ToolsNavigate.TPOS_LOGIN_BITMAP;//�û�mHandler��ʶͨ�� TPOSLoginBean ���з�װ����
								mHandler.sendMessage(msg);
							}else{ }
						} 
						catch(JSONException e){
							e.printStackTrace();
						}  
					};
				}.start(); 
			}else{
				//��ע���Ӧ��ǩ������ȷʱ���ͻ��յ�code,��ȷ��ǩ����ȷ
				String code=bundle.getString("code","");
				Log.i("SinaWeiboLogin","��΢����Ӧ��ǩ������ȷ,��������:"+code);
			} 
		}  
	}
	
	/**
	 * �����û�����ӿ�,���ݴ����Ȩ�ޱ�ʶ����ȡ���û��Ļ�����Ϣ
	 * @param access_token 
	 * @param expires_in
	 * @param uid �û���Ϣid�ֶ�
	 * @return JSON��ʽ���ַ�������
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
			System.out.println("����˵ķ����� code = "+code);   
			if(code == 200){  
				InputStream is = conn.getInputStream();   
				while ((len = is.read(buffer)) != -1) {     
					baos.write(buffer, 0, len);    
				}  
				//�����ȡ������JSON��ʽ���ַ���
				String response = new String(baos.toByteArray());    
				System.out.println("��ӡuser����**********  "+response); 
				return response;
			}  
			} catch (Exception e) {} 
		return null;
	}
	
	/** ��������΢���û���¼�ɹ���Ĳ�����SharedPreferences���� */
	public void setSinaWeiboSharedPreferences(String access_token, String expires_in, String uid) { 
		if(access_token!=null && expires_in!=null && uid!=null){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date=sdf.format(new Date());//����ʱ��  
			
			mSharedPrefs=getSharedPreferences("sina", Context.MODE_PRIVATE);
			mEditor=mSharedPrefs.edit();
			
			mEditor.putString("date", date);
			mEditor.putString("uid", uid);
			mEditor.putString("expires_in", expires_in);
			mEditor.putString("access_token", access_token);
			mEditor.commit();
		}else{
			Log.i("Navigate","����΢���û����ò���������Ч");
		}
	}
	
	/** ������΢���û���¼�ɹ���Ĳ�����SharedPreferences���л�ȡaccess_token,uid*/
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
	
	/** ͨ���̻߳�ȡ���û�����Ϣ*/
	private void getSinaWeiboUserInfoThread(String access_token, String expires_in, String uid){
		//����GET�����������ַ,���û���ϢAPI 
		final String result=getUserInfo(access_token,expires_in,uid);//��ȡ�û�����ϢJSON��ʽ������
		//�����߳�����ͷ���ͼƬ
		new Thread(){
			public void run() {
				JSONObject json;
				try {
					json = new JSONObject(result);
					mNickName=json.getString("screen_name");//��ȡ������΢�����û��ǳ�
					if(json.has("avatar_hd")){
						Bitmap tempBitmap=null;
						tempBitmap=ToolsNavigate.getBitmap(json.getString("avatar_hd"));
						Message msg=new Message();
						msg.obj=tempBitmap;
						msg.what=ToolsNavigate.TPOS_LOGIN_BITMAP;//�û�mHandler��ʶͨ�� TPOSLoginBean ���з�װ����
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
