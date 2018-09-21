package com.wqp.stadiumapp;
 
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wqp.webservice.entity.GetGuideBean;

public class NotSpeechScrectDetails extends Activity {
	private ImageView notspeechscret_details_back_arrow;
	private TextView not_speech_secrect_details_title,not_speech_secrect_details_time,
	not_speech_secrect_details_score,not_speech_secrect_details_article;	
	private GetGuideBean mGetGuideBean=null; 
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.not_speech_scret_details);
		initView(); 
		Intent mIntent=getIntent();
		mGetGuideBean = (GetGuideBean) mIntent.getSerializableExtra("GetGuideBean");
		
		//设置数据到组件上面
		if(mGetGuideBean!=null){
			not_speech_secrect_details_title.setText(mGetGuideBean.getTitle());//设置标题
			not_speech_secrect_details_time.setText("时间:"+mGetGuideBean.getCreatetime());//设置时间
			not_speech_secrect_details_score.setText("点击量:"+mGetGuideBean.getCtr()+"");//设置点击率
			not_speech_secrect_details_article.setText(mGetGuideBean.getSgcontent());//设置内容
		}
	}
	
	/** 获取组件并进行初始化操作和绑定事件*/
	private void initView(){
		notspeechscret_details_back_arrow=(ImageView) findViewById(R.id.notspeechscret_details_back_arrow);
		not_speech_secrect_details_title=(TextView) findViewById(R.id.not_speech_secrect_details_title);
		not_speech_secrect_details_time=(TextView) findViewById(R.id.not_speech_secrect_details_time);
		not_speech_secrect_details_score=(TextView) findViewById(R.id.not_speech_secrect_details_score);
		not_speech_secrect_details_article=(TextView) findViewById(R.id.not_speech_secrect_details_article);
		
		notspeechscret_details_back_arrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				finish();
			}
		}); 
	}
	
}
