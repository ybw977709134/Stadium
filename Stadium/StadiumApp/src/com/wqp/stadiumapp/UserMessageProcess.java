package com.wqp.stadiumapp;
 
import java.text.DecimalFormat;
 
import com.wqp.stadiumapp.utils.UserGlobal; 
import com.wqp.webservice.entity.Get5SecondUBean;

import android.app.Activity;
import android.app.AlertDialog; 
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * �û��յ�������Ϣ�ظ���,��֪ͨ��������Ӧ���������ʾ�˴������  
 */
public class UserMessageProcess extends Activity {
	private TextView users_id,pro_name,venues_ids,pricess,create_times;
	private Get5SecondUBean mGet5SecondUBean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_message_process); 
		
		Intent intent=getIntent();
		mGet5SecondUBean=(Get5SecondUBean) intent.getSerializableExtra("Get5SecondUBean");
		if(mGet5SecondUBean==null){
			Toast.makeText(UserMessageProcess.this, "��ϢΪ��", 0).show();
			finish();
			return;
		}
		//��ӭ���뵽�û���Ϣ�������,����ʹ��һ���Ի�����û��������Ϣ�������û�ѡ�����
		//AlertDialog.Builder...
		//���ѡ��ȷ������֧��ҳ�棬���ѡ��ȡ���ͷ���ȡ����ʶ�������ʶ˼���
		View v=View.inflate(UserMessageProcess.this, R.layout.user_message_process_view, null);
		users_id=(TextView) v.findViewById(R.id.users_id);
		pro_name=(TextView) v.findViewById(R.id.pro_name);
		venues_ids=(TextView) v.findViewById(R.id.venues_ids);
		pricess=(TextView) v.findViewById(R.id.pricess);
		create_times=(TextView) v.findViewById(R.id.create_times);
		
/*		���ݳ��ݵ�ID�Ż�ȡ���ݵ�����
		new Thread(){
			public void run() {
				try {
					JSONObject obj=new JSONObject();
					obj.put("VenuesID", mGet5SecondUBean.getVenuesIDs());
					WebGetVenues.getGetVenuesData(obj.toString());
					
					
					
				} catch (JSONException e) { 
					e.printStackTrace();
				} 
			};
		}.start();
*/		
		//�����ӳ������
		users_id.setText("�����û�����"+UserGlobal.UserName);
		pro_name.setText("�˶�����Ŀ��"+mGet5SecondUBean.getProName());
		venues_ids.setText("�μӵĳ���(��ʱΪ���ݵ�ID)��"+mGet5SecondUBean.getVenuesIDs());
		pricess.setText("��Ʊ�Ľ�"+new DecimalFormat(".00").format(mGet5SecondUBean.getPrice()));//��float��������ת���ɱ�����λС��		
		create_times.setText("��������ʱ�䣺"+mGet5SecondUBean.getCreateTime()); 
		
		AlertDialog.Builder builder=new AlertDialog.Builder(UserMessageProcess.this)
		.setTitle("���ʵ���Ķ���,ȷ��������?")
		.setView(v)
		.setPositiveButton("����", new DialogInterface.OnClickListener() { 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//�������˸����������ת��֧��ҳ��
				Intent _intent_=new Intent(UserMessageProcess.this,StadiumOrder.class);//�ӵ�ǰҳ����ת��֧����ҳ��,��Ҫ��3��������ȥ
				
				startActivity(_intent_);
				finish();
			}
		})
		.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() { 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//��������ȡ��������ֱ���˳���ǰҳ��
				
				finish();
			}
		});
		builder.create().show();
	}
	
}
