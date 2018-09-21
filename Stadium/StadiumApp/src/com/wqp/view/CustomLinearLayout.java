package com.wqp.view;
 

import android.content.Context;
import android.util.AttributeSet; 
import android.widget.LinearLayout;



public class CustomLinearLayout extends LinearLayout {
//	private LinearLayout mWrapper;
//	private boolean once;
	
	public CustomLinearLayout(Context context) {
		super(context); 
	}
	public CustomLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs); 
	}
	public CustomLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle); 
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 
		
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}


}
