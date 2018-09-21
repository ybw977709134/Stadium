package com.wqp.navigate;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class NavigationPagerAdapter extends PagerAdapter {
	private List<View> mViewList;
	
	public NavigationPagerAdapter(List<View> mViewList){
		this.mViewList=mViewList;
	} 
	
	@Override
	public int getCount() { 
		return mViewList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) { 
		return arg0==arg1;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) { 
		container.removeView(mViewList.get(position));
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(mViewList.get(position)); 
		return mViewList.get(position);
	}

}
