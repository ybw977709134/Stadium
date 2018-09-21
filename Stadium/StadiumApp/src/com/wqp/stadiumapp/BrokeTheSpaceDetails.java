package com.wqp.stadiumapp;
 
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wqp.webservice.entity.GetGuideBean;

public class BrokeTheSpaceDetails extends Activity {
	private ImageView brokethespace_details_back_arrow;
	private TextView broke_the_space_details_title,broke_the_space_details_time,
	broke_the_space_details_score,broke_the_space_details_article;	
	private GetGuideBean mGetGuideBean=null; 
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.broke_the_space_details);
		initView(); 
		Intent mIntent=getIntent();
		mGetGuideBean = (GetGuideBean) mIntent.getSerializableExtra("GetGuideBean");
		
		//�������ݵ��������
		if(mGetGuideBean!=null){
			broke_the_space_details_title.setText(mGetGuideBean.getTitle());//�������ű���
			broke_the_space_details_time.setText("ʱ��:"+mGetGuideBean.getCreatetime());//����ʱ��
			broke_the_space_details_score.setText("�����:"+mGetGuideBean.getCtr()+"");//���õ����
			broke_the_space_details_article.setText(mGetGuideBean.getSgcontent());//��������
		}
	}
	
	/** ��ȡ��������г�ʼ�������Ͱ��¼�*/
	private void initView(){
		brokethespace_details_back_arrow=(ImageView) findViewById(R.id.brokethespace_details_back_arrow);
		broke_the_space_details_title=(TextView) findViewById(R.id.broke_the_space_details_title);
		broke_the_space_details_time=(TextView) findViewById(R.id.broke_the_space_details_time);
		broke_the_space_details_score=(TextView) findViewById(R.id.broke_the_space_details_score);
		broke_the_space_details_article=(TextView) findViewById(R.id.broke_the_space_details_article);
		
		brokethespace_details_back_arrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				finish();
			}
		}); 
	}
	
}
