package com.wqp.stadiumapp.adapter;

import java.util.List;
import java.util.Map; 

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter; 
import android.widget.TextView;

public class PrivateFormulateAdapter extends BaseAdapter {

	@SuppressWarnings("unused")
	private Context context;
	private List<Map<String,Object>> mListViewData;
	private int resource;
	private String[] mFrom;
	private int[] mTo;	
	private LayoutInflater inflater;
	
	public PrivateFormulateAdapter(Context context,List<Map<String,Object>> mListViewData,
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
		return mListViewData.size();
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
			vh.mNickname=(TextView) v.findViewById(mTo[0]);
			vh.mSex=(TextView) v.findViewById(mTo[1]);
			vh.mZhuanJiaType=(TextView) v.findViewById(mTo[2]); 
			vh.mVenuesName=(TextView) v.findViewById(mTo[3]);
			vh.mHobby=(TextView)v.findViewById(mTo[4]);
			v.setTag(vh);
		}else{
			v=convertView;  
		} 
		vh=(ViewHolder) v.getTag();
		//�첽���ظ�������
		asyncTask(vh,position);  
		
		return v;
	}
	
	private class ViewHolder{
		public TextView mNickname;//�ǳ�
		public TextView mSex;//�Ա�
		public TextView mZhuanJiaType;//ר������
		public TextView mVenuesName;//��������
		public TextView mHobby;//�˶���Ŀ
	}
	
	
	
	/** ���²������첽���� */
	private void asyncTask(ViewHolder vh,int position) {
		new MyAsyncTask(vh).execute(position);
	}
	
	private class MyAsyncTask extends AsyncTask<Integer,Integer,Map<String,Object>>{ 
		private static final String TAG = "PrivateFormulateGridViewAdapter >>> MyAsyncTask"; 
		private ViewHolder vh;
		public MyAsyncTask(ViewHolder vh){
			this.vh=vh;
		} 
		
		/** �÷�������UI�̵߳�������,���Ȼ�ִ�и÷��� */
		@Override
		protected void onPreExecute() { 
			super.onPreExecute(); 
		}
		
		/** �÷����������̵߳�������,�����ҵ�����߳�����ִ������ */
		@Override
		protected Map<String,Object> doInBackground(Integer... params) {  
			return mListViewData.get(params[0]); 
		}
		
		/** �÷�������UI�̵߳�������,����UI�ϵ����� */
		@Override
		protected void onPostExecute(Map<String,Object> result) {
			if(vh==null || result==null){
				Log.i(TAG,"result=null");
			}
			vh.mNickname.setText((String)result.get(mFrom[0]));//�ǳ�
			vh.mSex.setText((String)result.get(mFrom[1]));//�Ա�
			vh.mZhuanJiaType.setText((String)result.get(mFrom[2]));//ר������
			vh.mVenuesName.setText((String)result.get(mFrom[3]));//��������
			vh.mHobby.setText((String)result.get(mFrom[4]));//�˶���Ŀ
		}  
	}

}
