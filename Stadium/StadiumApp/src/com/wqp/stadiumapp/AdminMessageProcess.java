package com.wqp.stadiumapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * �û��յ�������Ϣ�ظ���,��֪ͨ��������Ӧ���������ʾ�˴������  
 */
public class AdminMessageProcess extends Activity {
	private TextView show;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_message_process);
		show=(TextView) findViewById(R.id.admin_showtextview);
		
		Intent intent=getIntent();
		String message=intent.getStringExtra("message");
		show.setText(message);
		
		//��ӭ���뵽����Ա�û�����,����ʹ��һ���Ի���ѹ���Աѡ�����Ϣչʾ��������ѡ�����
		//AlertDialog.Builder...
	}
	
}
