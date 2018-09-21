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
		initView();//��ʼ��ҳ���ϵ��������
		mGetUserInfoBean=(GetUserInfoBean) getIntent().getSerializableExtra("GetUserInfoBean");
		if(mGetUserInfoBean!=null){
			initData();//��ʾ����
		}
	}
	
	/** ��ҳ���ȡ����������󶨺��¼�*/
	private void initView(){
		user_details_arrow=(LinearLayout) findViewById(R.id.user_details_arrow);
		user_details_Picture=(ImageView) findViewById(R.id.user_details_Picture);//�û�ͷ��ͼƬ
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
		case R.id.user_details_arrow://����˷��ذ�ť
			finish();
			break; 
		} 
	}
	
	
	/** ������ݲ���*/
	public void initData(){ 
		// ��Uri·���ϼ���ͼƬ��Դ������ʾ��¼�û���ͷ��
		ImageLoader.getInstance().displayImage(
				WebServiceUtils.IMAGE_URL + mGetUserInfoBean.getPicture(),
				user_details_Picture, AppApplication.options,
				new SimpleImageLoadingListener(),
				new ImageLoadingProgressListener() {
					@Override
					public void onProgressUpdate(String imageUri, View view, int current, int total) {
						Log.i(TAG, "ͼƬ�첽���ص�����:total=" + total + ",current=" + current);
					}
				});
		user_details_UserName.setText("�� �� ��:"+mGetUserInfoBean.getUsername());
		user_details_Nickname.setText("��	��:"+mGetUserInfoBean.getNickname());
		user_details_Sex.setText("��		��:"+mGetUserInfoBean.getSex());
		user_details_Age.setText("�� �� ��:"+mGetUserInfoBean.getAge());
		user_details_Email.setText("E-MAIL:"+mGetUserInfoBean.getEmail());
		user_details_QQ.setText("QQ:"+mGetUserInfoBean.getQq());
		user_details_MSN.setText("MSN:"+mGetUserInfoBean.getMsn());
		user_details_Phone.setText("�ֻ���:"+mGetUserInfoBean.getPhone()); 
	}
	
	
	
	
}
