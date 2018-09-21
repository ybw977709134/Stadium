package com.wqp.stadiumapp.adapter; 

public class FriendApplyAdapter {//extends BaseAdapter implements OnClickListener {
//	private Context context;
//	private List<Map<String,Object>> mListViewData;
//	private int resource;
//	private String[] mFrom;
//	private int[] mTo;	
//	private LayoutInflater inflater;
//	
//	public FriendApplyAdapter(Context context,List<Map<String,Object>> mListViewData,
//			int resource,String[] mFrom,int[] mTo){
//		this.context=context;
//		this.mListViewData=mListViewData;
//		this.resource=resource;
//		this.mFrom=mFrom;
//		this.mTo=mTo;
//		inflater=LayoutInflater.from(context);
//	}
//	
//	@Override
//	public int getCount() { 
//		return mListViewData.size();
//	}
//
//	@Override
//	public Object getItem(int position) { 
//		return mListViewData.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) { 
//		return position;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		View v;
//		ViewHolder vh;
//		if(convertView==null){
//			vh=new ViewHolder();
//			v=inflater.inflate(resource, null);			
//			vh.mImage=(ImageView) v.findViewById(mTo[0]);
//			vh.mName=(TextView) v.findViewById(mTo[1]);
//			vh.mContent=(TextView) v.findViewById(mTo[2]); 
//			vh.mState=(ImageView) v.findViewById(mTo[3]); 
//			v.setTag(vh);
//		}else{
//			v=convertView;  
//		} 
//		vh=(ViewHolder) v.getTag();
//		//异步加载更新数据
//		asyncTask(vh,position);  
//		vh.mState.setOnClickListener(this);//为状态图标绑定点击事件
//		return v;
//	}
//	
//	private class ViewHolder{
//		public ImageView mImage;
//		public TextView mName;
//		public TextView mContent; 
//		public ImageView mState;   
//	}
//	
//	
//	
//	/** 以下步分是异步加载 */
//	private void asyncTask(ViewHolder vh,int position) {
//		new MyAsyncTask(vh).execute(position);
//	}
//	
//	private class MyAsyncTask extends AsyncTask<Integer,Integer,Map<String,Object>>{ 
//		private static final String TAG = "FriendApplyAdapter >>> MyAsyncTask"; 
//		private ViewHolder vh;
//		public MyAsyncTask(ViewHolder vh){
//			this.vh=vh;
//		} 
//		
//		/** 该方法是在UI线程当中运行,首先会执行该方法 */
//		@Override
//		protected void onPreExecute() { 
//			super.onPreExecute(); 
//		}
//		
//		/** 该方法是在子线程当中运行,会根据业务开启线程数量执行任务 */
//		@Override
//		protected Map<String,Object> doInBackground(Integer... params) {  
//			return mListViewData.get(params[0]); 
//		}
//		
//		/** 该方法是在UI线程当中运行,更新UI上的数据 */
//		@Override
//		protected void onPostExecute(Map<String,Object> result) {
//			if(vh==null || result==null){
//				Log.i(TAG,"result=null");
//			}
//			vh.mImage.setImageResource((Integer) result.get(mFrom[0]));//image头像
//			vh.mName.setText((String)result.get(mFrom[1]));//name 名称
//			vh.mContent.setText((String)result.get(mFrom[2]));//content内容 
////			vh.mState.setImageResource((Integer) result.get(mFrom[3]));//state 状态
//		}  
//	}
//
//
//	/** 为ListView组件当中的同意或者已同意图标绑定点击事件*/
//	@Override
//	public void onClick(View v) { 
//		switch(v.getId()){
//		case R.id.friend_apply_xlistview_state:
//			Toast.makeText(context, "你点击了我", 0).show();
//			break;
//		} 
//	}

}
