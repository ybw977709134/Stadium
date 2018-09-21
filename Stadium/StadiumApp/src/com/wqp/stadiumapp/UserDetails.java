package com.wqp.stadiumapp;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wqp.stadiumapp.utils.WebServiceUtils;
import com.wqp.webservice.entity.GetUserInfoBean;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserDetails extends Activity implements OnClickListener {
	private static String TAG="UserDetails";
	private LinearLayout user_details_arrow;
	private ImageView user_details_Picture;
	private TextView user_details_UserName,user_details_Nickname,user_details_Sex,user_details_Age,
	user_details_Email,user_details_QQ,user_details_MSN,user_details_Phone;
	
	private GetUserInfoBean mGetUserInfoBean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_details);
		initView();//初始化页面上的所有组件
		mGetUserInfoBean=(GetUserInfoBean) getIntent().getSerializableExtra("GetUserInfoBean");
		if(mGetUserInfoBean!=null){
			initData();//显示数据
		}
	}
	
	/** 从页面获取到组件，并绑定好事件*/
	private void initView(){
		user_details_arrow=(LinearLayout) findViewById(R.id.user_details_arrow);
		user_details_Picture=(ImageView) findViewById(R.id.user_details_Picture);//用户头像图片
		user_details_UserName=(TextView) findViewById(R.id.user_details_UserName);
		user_details_Nickname=(TextView) findViewById(R.id.user_details_Nickname);
		user_details_Sex=(TextView) findViewById(R.id.user_details_Sex);
		user_details_Age=(TextView) findViewById(R.id.user_details_Age);
		user_details_Email=(TextView) findViewById(R.id.user_details_Email);
		user_details_QQ=(TextView) findViewById(R.id.user_details_QQ);
		user_details_MSN=(TextView) findViewById(R.id.user_details_MSN);
		user_details_Phone=(TextView) findViewById(R.id.user_details_Phone); 
		
		user_details_arrow.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_details_arrow://点击了返回按钮
			finish();
			break; 
		} 
	}
	
	
	/** 填充数据操作*/
	public void initData(){ 
		// 从Uri路径上加载图片资源进行显示登录用户的头像
		ImageLoader.getInstance().displayImage(
				WebServiceUtils.IMAGE_URL + mGetUserInfoBean.getPicture(),
				user_details_Picture, AppApplication.options,
				new SimpleImageLoadingListener(),
				new ImageLoadingProgressListener() {
					@Override
					public void onProgressUpdate(String imageUri, View view, int current, int total) {
						Log.i(TAG, "图片异步加载的数据:total=" + total + ",current=" + current);
					}
				});
		user_details_UserName.setText("用 户 名:"+mGetUserInfoBean.getUsername());
		user_details_Nickname.setText("昵	称:"+mGetUserInfoBean.getNickname());
		user_details_Sex.setText("性		别:"+mGetUserInfoBean.getSex());
		user_details_Age.setText("年 龄 段:"+mGetUserInfoBean.getAge());
		user_details_Email.setText("E-MAIL:"+mGetUserInfoBean.getEmail());
		user_details_QQ.setText("QQ:"+mGetUserInfoBean.getQq());
		user_details_MSN.setText("MSN:"+mGetUserInfoBean.getMsn());
		user_details_Phone.setText("手机号:"+mGetUserInfoBean.getPhone()); 
	}
	
	
	
	
}
