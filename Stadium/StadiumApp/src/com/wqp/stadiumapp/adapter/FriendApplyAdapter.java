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
//		//�첽���ظ�������
//		asyncTask(vh,position);  
//		vh.mState.setOnClickListener(this);//Ϊ״̬ͼ��󶨵���¼�
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
//	/** ���²������첽���� */
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
//		/** �÷�������UI�̵߳�������,���Ȼ�ִ�и÷��� */
//		@Override
//		protected void onPreExecute() { 
//			super.onPreExecute(); 
//		}
//		
//		/** �÷����������̵߳�������,�����ҵ�����߳�����ִ������ */
//		@Override
//		protected Map<String,Object> doInBackground(Integer... params) {  
//			return mListViewData.get(params[0]); 
//		}
//		
//		/** �÷�������UI�̵߳�������,����UI�ϵ����� */
//		@Override
//		protected void onPostExecute(Map<String,Object> result) {
//			if(vh==null || result==null){
//				Log.i(TAG,"result=null");
//			}
//			vh.mImage.setImageResource((Integer) result.get(mFrom[0]));//imageͷ��
//			vh.mName.setText((String)result.get(mFrom[1]));//name ����
//			vh.mContent.setText((String)result.get(mFrom[2]));//content���� 
////			vh.mState.setImageResource((Integer) result.get(mFrom[3]));//state ״̬
//		}  
//	}
//
//
//	/** ΪListView������е�ͬ�������ͬ��ͼ��󶨵���¼�*/
//	@Override
//	public void onClick(View v) { 
//		switch(v.getId()){
//		case R.id.friend_apply_xlistview_state:
//			Toast.makeText(context, "��������", 0).show();
//			break;
//		} 
//	}

}
