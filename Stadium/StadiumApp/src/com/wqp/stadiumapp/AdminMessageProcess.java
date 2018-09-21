package com.wqp.stadiumapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 用户收到场馆消息回复后,在通知栏里面响应点击，再显示此处理界面  
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
		
		//欢迎进入到管理员用户界面,这里使用一个对话框把管理员选择的消息展示出来进行选择操作
		//AlertDialog.Builder...
	}
	
}
