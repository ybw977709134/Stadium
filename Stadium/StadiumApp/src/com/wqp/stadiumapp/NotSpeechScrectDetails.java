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
		
		//�������ݵ��������
		if(mGetGuideBean!=null){
			not_speech_secrect_details_title.setText(mGetGuideBean.getTitle());//���ñ���
			not_speech_secrect_details_time.setText("ʱ��:"+mGetGuideBean.getCreatetime());//����ʱ��
			not_speech_secrect_details_score.setText("�����:"+mGetGuideBean.getCtr()+"");//���õ����
			not_speech_secrect_details_article.setText(mGetGuideBean.getSgcontent());//��������
		}
	}
	
	/** ��ȡ��������г�ʼ�������Ͱ��¼�*/
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
