package com.wqp.view;

import android.content.Context;

import android.util.AttributeSet;
import android.view.MotionEvent;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PopupOverlay;

/**
 * �̳�MapView��дonTouchEventʵ�����ݴ������  
 */
public class XMapView extends MapView {
	public static PopupOverlay pop = null;// ��������ͼ�㣬���ͼ��ʹ��

	public XMapView(Context context) {
		super(context);
	}

	public XMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public XMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!super.onTouchEvent(event)) {
			// ��������
			if (pop != null && event.getAction() == MotionEvent.ACTION_UP)
				pop.hidePop();
		}
		return true;
	}

}

