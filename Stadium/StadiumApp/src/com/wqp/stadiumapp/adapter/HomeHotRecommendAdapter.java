package com.wqp.stadiumapp.adapter;

import java.util.List;
import java.util.Map; 
  
import com.nostra13.universalimageloader.core.ImageLoader; 
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wqp.stadiumapp.AppApplication; 

import android.content.Context; 
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView; 
import android.widget.TextView;

public class HomeHotRecommendAdapter extends BaseAdapter {
	@SuppressWarnings("unused")
	private Context context;
	private List<Map<String,Object>> mListViewData;
	private int resource;
	private String[] mFrom;
	private int[] mTo;	
	private LayoutInflater inflater;
	
	public HomeHotRecommendAdapter(Context context,List<Map<String,Object>> mListViewData,
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
			vh.mTitle=(TextView) v.findViewById(mTo[1]);
			vh.mAddress=(TextView) v.findViewById(mTo[2]);
			vh.mTelephone=(TextView) v.findViewById(mTo[3]);
			vh.mRoute=(TextView) v.findViewById(mTo[4]);
			vh.mEnvironment=(TextView) v.findViewById(mTo[5]); 
			v.setTag(vh);
		}else{
			v=convertView;  
		} 
		vh=(ViewHolder) v.getTag();
		//异步加载更新数据
		asyncTask(vh,position);  
		
		return v;
	}
	
	private class ViewHolder{
		public ImageView mImage;
		public TextView mTitle;
		public TextView mAddress;
		public TextView mTelephone;
		public TextView mRoute; 
		public TextView mEnvironment;  
	}
	
	
	
	/** 以下步分是异步加载 */
	private void asyncTask(ViewHolder vh,int position) {
		new MyAsyncTask(vh).execute(position);
	}
	
	private class MyAsyncTask extends AsyncTask<Integer,Integer,Map<String,Object>>{ 
		private static final String TAG = "HomeHotRecommendAdapter >>> MyAsyncTask"; 
		private ViewHolder vh;
		
//        DisplayImageOptions options=new DisplayImageOptions.Builder()
//    	.showImageOnLoading(R.drawable.user_test)
//    	.showImageOnFail(R.drawable.user_test)
//    	.cacheInMemory(true)
//    	.cacheOnDisk(true)
//    	.bitmapConfig(Bitmap.Config.RGB_565)
//    	.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//    	.build();
		
		
		
		public MyAsyncTask(ViewHolder vh){
			this.vh=vh;
		} 
		
		/** 该方法是在UI线程当中运行,首先会执行该方法 */
		@Override
		protected void onPreExecute() { 
			super.onPreExecute(); 
		}
		
		/** 该方法是在子线程当中运行,会根据业务开启线程数量执行任务 */
		@Override
		protected Map<String,Object> doInBackground(Integer... params) {  
			return mListViewData.get(params[0]); 
		}
		
		/** 该方法是在UI线程当中运行,更新UI上的数据 */
		@Override
		protected void onPostExecute(Map<String,Object> result) {
			if(vh==null || result==null){
				Log.i(TAG,"result=null");
				return;
			} 
			//从Uri路径上加载图片资源进行显示vh.mImage
			ImageLoader.getInstance().displayImage((String)result.get(mFrom[0]), vh.mImage, AppApplication.options,
	        		new SimpleImageLoadingListener(),new ImageLoadingProgressListener(){

						@Override
						public void onProgressUpdate(String imageUri, View view,
								int current, int total) { 
							Log.i(TAG,"图片异步加载的数据:total="+total+",current="+current);
						}
	        	
	        });  
			vh.mTitle.setText((String)result.get(mFrom[1]));//title
			vh.mAddress.setText((String)result.get(mFrom[2]));//address
			vh.mTelephone.setText((String)result.get(mFrom[3]));//telephone
			vh.mRoute.setText((String)result.get(mFrom[4]));//route 
			vh.mEnvironment.setText((String)result.get(mFrom[5]));//environment 
		}  
	}

}
