package com.wqp.stadiumapp.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class InvitationWarViewPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> mFragmentList;
	public InvitationWarViewPagerAdapter(FragmentManager fragmentManager,List<Fragment> mFragmentList) {
		super(fragmentManager); 
		this.mFragmentList=mFragmentList;
	}

	@Override
	public Fragment getItem(int arg0) { 
		return mFragmentList.get(arg0);
	}

	@Override
	public int getCount() { 
		return mFragmentList.size();
	}

}
