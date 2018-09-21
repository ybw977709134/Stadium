package com.wqp.stadiumapp.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * ���������ǳ����࣬��Ҫ�ھ���ʵ���������Լ�ʵ��getView()������ҵ���߼�
 * @author wqp
 *
 * @param <T> ����
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
	
	/** ����ʹ�õ��˳��󷽷�,��Ҫ��ʵ���൱��ʵ�־���ķ���*/
	@Override
	abstract public View getView(int position, View convertView, ViewGroup parent);
	
	/** ��mList���Խ��и�ֵ*/
	public void setListData(ArrayList<T> list){
		this.mList=list;
		notifyDataSetChanged();
	}
	
	/** ��ȡmList��ֵ*/
	public ArrayList<T> getListData(){
		return this.mList;
	}
	
	/** �Դ��������������õ�mListֵ����*/
	public void setListData(T[] list){
		ArrayList<T> al=new ArrayList<T>(list.length);
		for (T t : al) {
			al.add(t);
		}
		setListData(al);
	}

}
