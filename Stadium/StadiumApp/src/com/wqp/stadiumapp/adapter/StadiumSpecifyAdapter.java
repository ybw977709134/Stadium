package com.wqp.stadiumapp.adapter;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wqp.stadiumapp.AppApplication;
import com.wqp.webservice.WebGetAUser;
import com.wqp.webservice.entity.GetUserInfoBean;

public class StadiumSpecifyAdapter extends BaseAdapter {
	@SuppressWarnings("unused")
	private Context context;
	private List<Map<String,Object>> mListViewData;
	private int resource;
	private String[] mFrom;
	private int[] mTo;	
	private LayoutInflater inflater;
	
	public StadiumSpecifyAdapter(Context context,List<Map<String,Object>> mListViewData,
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
			vh.mImage=(ImageView) v.findViewById(mTo[0]);
			vh.mName=(TextView) v.findViewById(mTo[1]);
			vh.mSpacetime=(TextView) v.findViewById(mTo[2]);
			vh.mContent=(TextView) v.findViewById(mTo[3]); 
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
		public ImageView mImage;
		public TextView mName;
		public TextView mSpacetime; 
		public TextView mContent;   
	}
	
	
	
	/** ���²������첽���� */
	private void asyncTask(ViewHolder vh,int position) {
		new MyAsyncTask(vh).execute(position);
	}
	
	private class MyAsyncTask extends AsyncTask<Integer,Integer,Map<String,Object>>{ 
		private static final String TAG = "StadiumSpecifyAdapter >>> MyAsyncTask"; 
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
		protected void onPostExecute(final Map<String,Object> result) {
			final Handler mHandler=new Handler(){
				@Override
				public void handleMessage(Message msg) { 
					if(msg.what==0x11){
						//��Uri·���ϼ���ͼƬ��Դ������ʾvh.mImage
						ImageLoader.getInstance().displayImage((String)result.get(mFrom[0]), vh.mImage, AppApplication.options,
				        		new SimpleImageLoadingListener(),new ImageLoadingProgressListener(){

									@Override
									public void onProgressUpdate(String imageUri, View view,
											int current, int total) { 
										Log.i(TAG,"ͼƬ�첽���ص�����:total="+total+",current="+current);
									} 
				        }); 
					}
				}
			};
			
			if(vh==null || result==null){
				Log.i(TAG,"result=null");
			}
			new Thread(){
				public void run() {
					JSONObject object=new JSONObject(); 
					try {
						object.put("", ((Integer)result.get(mFrom[0])).intValue());
						 List<GetUserInfoBean> resu=WebGetAUser.getGetAUserData(object.toString());
						 if(resu!=null){
							 Message msg=new Message();
							 msg.what=0x11;
							 msg.obj=resu.get(0).getPicture();
							 mHandler.sendMessage(msg);
						 }
					} catch (JSONException e) { 
						e.printStackTrace();
					} 
				};
			}.start();
			
			vh.mName.setText((String)result.get(mFrom[1]));//name �����˵�����
			vh.mSpacetime.setText((String)result.get(mFrom[2]));//spaceteim������ʱ�� 
			vh.mContent.setText(String.valueOf((String)result.get(mFrom[3])));//content,���۵����� 
		}  
	}

}
