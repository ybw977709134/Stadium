package com.wqp.stadiumapp.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 该适配器是抽象类，需要在具体实现类里面自己实现getView()方法的业务逻辑
 * @author wqp
 *
 * @param <T> 参数
 */
public abstract class ArrayListAdapter<T> extends BaseAdapter {
	protected ArrayList<T> mList;
	protected Context mContext;
	
	@Override
	public int getCount() { 
		return mList==null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) { 
		return mList==null ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) { 
		return position;
	}
	
	/** 这里使用到了抽象方法,需要在实现类当中实现具体的方法*/
	@Override
	abstract public View getView(int position, View convertView, ViewGroup parent);
	
	/** 对mList属性进行赋值*/
	public void setListData(ArrayList<T> list){
		this.mList=list;
		notifyDataSetChanged();
	}
	
	/** 获取mList的值*/
	public ArrayList<T> getListData(){
		return this.mList;
	}
	
	/** 对传入的数组参数设置到mList值当中*/
	public void setListData(T[] list){
		ArrayList<T> al=new ArrayList<T>(list.length);
		for (T t : al) {
			al.add(t);
		}
		setListData(al);
	}

}
