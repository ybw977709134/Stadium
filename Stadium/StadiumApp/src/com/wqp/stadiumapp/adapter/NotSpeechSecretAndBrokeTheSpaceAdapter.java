package com.wqp.stadiumapp.adapter;

import java.util.List;
import java.util.Map;  

import android.content.Context; 
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter; 
import android.widget.TextView;

public class NotSpeechSecretAndBrokeTheSpaceAdapter extends BaseAdapter {
	@SuppressWarnings("unused")
	private Context context;
	private List<Map<String,Object>> mListViewData;
	private int resource;
	private String[] mFrom;
	private int[] mTo;	
	private LayoutInflater inflater;
	
	public NotSpeechSecretAndBrokeTheSpaceAdapter(Context context,List<Map<String,Object>> mListViewData,
									int resource,String[] mFrom,int[] mTo){
		this.context=context;
		this.mListViewData=mListViewData;
		this.resource=resource;
		this.mFrom=mFrom;
		this.mTo=mTo;
		inflater=LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() { 
		return mListViewData!=null ? mListViewData.size() : 0;
	}

	@Override
	public Object getItem(int position) {  
		return mListViewData.get(position);
	}

	@Override
	public long getItemId(int position) { 
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		ViewHolder vh;
		if(convertView==null){
			vh=new ViewHolder();
			v=inflater.inflate(resource, null);			
			vh.mTitle=(TextView) v.findViewById(mTo[0]);
			vh.mContent=(TextView) v.findViewById(mTo[1]); 
			v.setTag(vh);
		}else{
			v=convertView;  
		} 
		vh=(ViewHolder) v.getTag(); 
		vh.mTitle.setText(mListViewData.get(position).get(mFrom[0]).toString());
		vh.mContent.setText(mListViewData.get(position).get(mFrom[1]).toString());
		return v;
	}
	
	private class ViewHolder{
		public TextView mContent;
		public TextView mTitle; 
	} 

}
