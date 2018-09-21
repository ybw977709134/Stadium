package com.wqp.stadiumapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class AboutUs extends Activity {
	private ImageView about_us_back_arrow;
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us);
		about_us_back_arrow=(ImageView) findViewById(R.id.about_us_back_arrow);
		
		about_us_back_arrow.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View v) { 
				finish();
			} 
		}); 
	}
	
}
