package com.wqp.stadiumapp.adapter;

import java.util.List;
import java.util.Map;

import com.wqp.stadiumapp.R;
import com.wqp.stadiumapp.R.color;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyOrderAdapter extends BaseAdapter {
	private Context context;
	private List<Map<String,Object>> mListViewData; 
	private int resource;
	private String[] mFrom;
	private int[] mTo;	
	private LayoutInflater inflater;
	
	public MyOrderAdapter(Context context,List<Map<String,Object>> mListViewData,
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
		ViewHolder vh;
		if(convertView==null){
			vh=new ViewHolder();
			convertView=inflater.inflate(resource, null);
			vh.mVenuesName=(TextView) convertView.findViewById(mTo[0]);
			vh.mSportType=(TextView) convertView.findViewById(mTo[1]);
			vh.mMakeTime=(TextView) convertView.findViewById(mTo[2]);
			vh.mState=(TextView) convertView.findViewById(mTo[3]);
			convertView.setTag(vh); 
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		vh.mVenuesName.setText((String)mListViewData.get(position).get("VenuesName"));//场馆名称
		vh.mSportType.setText((String)mListViewData.get(position).get("SportType"));//运动类型
		vh.mMakeTime.setText((String)mListViewData.get(position).get("OrderTime"));//预约时间
		String _order_state=(String)mListViewData.get(position).get("State");//预约状态
		if("未使用".equals(_order_state)){//未使用状态
			vh.mState.setTextColor(context.getResources().getColor(R.color.black)); 
		}else if("已使用".equals(_order_state)){//已使用状态
			vh.mState.setTextColor(context.getResources().getColor(R.color.standard));
		}else if("已预订".equals(_order_state)){//已预订状态
			vh.mState.setTextColor(context.getResources().getColor(R.color.standard));
		}
		vh.mState.setText(_order_state);//预约状态
		return convertView;
	}
	
	public class ViewHolder{
		public TextView mVenuesName;//场馆名称
		public TextView mSportType;//运动类型
		public TextView mMakeTime;//预约时间
		public TextView mState;//预约状态 
	}

}
