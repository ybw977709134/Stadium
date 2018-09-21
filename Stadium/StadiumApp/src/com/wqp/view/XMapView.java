package com.wqp.view;

import android.content.Context;

import android.util.AttributeSet;
import android.view.MotionEvent;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PopupOverlay;

/**
 * 继承MapView重写onTouchEvent实现泡泡处理操作  
 */
public class XMapView extends MapView {
	public static PopupOverlay pop = null;// 弹出泡泡图层，点击图标使用

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
			// 消隐泡泡
			if (pop != null && event.getAction() == MotionEvent.ACTION_UP)
				pop.hidePop();
		}
		return true;
	}

}

