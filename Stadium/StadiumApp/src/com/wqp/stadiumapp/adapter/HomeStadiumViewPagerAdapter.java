package com.wqp.stadiumapp.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class HomeStadiumViewPagerAdapter extends PagerAdapter {
	private List<GridView> mGridViewList;
	
	public HomeStadiumViewPagerAdapter(List<GridView> mGridViewList){
		this.mGridViewList=mGridViewList;
	}
	
	
	@Override
	public int getCount() { 
		return mGridViewList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) { 
		return arg0==arg1;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(mGridViewList.get(position));
		return mGridViewList.get(position);
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) { 
		container.removeView(mGridViewList.get(position));
	}

}
