package com.wqp.stadiumapp.adapter;
 
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message; 
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
 




import com.wqp.stadiumapp.R;
import com.wqp.stadiumapp.utils.UserGlobal;
import com.wqp.webservice.WebGetUserOnAct;
import com.wqp.webservice.WebJoinAct;
import com.wqp.webservice.entity.JoinActResultBean;

public class FriendRecommendAdapter extends BaseAdapter{
	private ProgressDialog mProgressDialog;//进度对话框
	private JoinActResultBean jrb;//用户添加活动成功之后返回的数据信息集
	 
	private Context context;
	private List<Map<String,Object>> mListViewData; 
	private int resource;
	private String[] mFrom;
	private int[] mTo;	
	private LayoutInflater inflater;   

	public FriendRecommendAdapter(Context context,List<Map<String,Object>> mListViewData,
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
			vh.mVenuesName=(TextView) v.findViewById(mTo[0]);
			vh.mSportType=(TextView) v.findViewById(mTo[1]);
			vh.mStartTime=(TextView) v.findViewById(mTo[2]); 
			vh.mEndTime=(TextView) v.findViewById(mTo[3]); 
			vh.mTakeCount=(TextView)v.findViewById(mTo[4]);
			vh.mImageState=(ImageView)v.findViewById(mTo[5]);//条目右边的添加图标按钮
			v.setTag(vh);
		}else{
			v=convertView;  
		} 
		vh=(ViewHolder) v.getTag();
		Log.i("在应战列表当中","当前条目图标状态为:"+(Boolean)mListViewData.get(position).get("Imgstate")); 
		if(mListViewData!=null && (Boolean)mListViewData.get(position).get("Imgstate")){ //加入了 
			Log.i("ADAPTER","条目 "+position+"=true"+"YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
			vh.mImageState.setImageResource(R.drawable.friend_recommend_xlistview_add_ok); 
			vh.mImageState.setSaveEnabled(false);//设置不让图标使能
			vh.mImageState.setOnClickListener(null);
		}else if(mListViewData!=null && !(Boolean)mListViewData.get(position).get("Imgstate")){//未加入
			Log.i("ADAPTER","条目 "+position+"=false"+"NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN");
			vh.mImageState.setImageResource(R.drawable.friend_recommend_xlistview_add);
			vh.mImageState.setOnClickListener(new MyOnClickListener(position,vh));
			Log.i("ADAPTER","条目 "+position+"=false");
		} 
		
		//异步加载更新数据
		asyncTask(vh,position);   
		return v;
	}
	
	private class ViewHolder{
		public TextView mVenuesName;
		public TextView mSportType;
		public TextView mStartTime; 
		public TextView mEndTime;  
		public TextView mTakeCount;
		public ImageView mImageState;
	}
	
	
	
	/** 以下步分是异步加载 */
	private void asyncTask(ViewHolder vh,int position) {
		new MyAsyncTask(vh).execute(position);
	}
	
	private class MyAsyncTask extends AsyncTask<Integer,Integer,Map<String,Object>> { 
		private static final String TAG = "FriendApplyAdapter >>> MyAsyncTask"; 
		private ViewHolder vh; 
		public MyAsyncTask(ViewHolder vh ){
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
			}
			vh.mVenuesName.setText((String) result.get(mFrom[0]));//场馆名称
			vh.mSportType.setText((String)result.get(mFrom[1]));//运动类型
			vh.mStartTime.setText((String)result.get(mFrom[2]));//开始时间
			vh.mEndTime.setText((String) result.get(mFrom[3]));//结束时间
			vh.mTakeCount.setText((String) result.get(mFrom[4]));//人数上限
			
		} 
	} 
	
	//创建消息管理器
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x11://加入应战成功
				jrb  =  (JoinActResultBean) msg.obj; 
				if(jrb!=null){
					if(jrb.isUserExist() && jrb.getNoName().equals("0") && jrb.getOnName().equals("0")){ 
						((ImageView)jrb.getmView()).setImageResource(R.drawable.friend_recommend_xlistview_add_ok);//更新图标
						notifyDataSetChanged();//刷新
						mListViewData.get(jrb.getmPosition()).put("Imgstate", true); 
						Toast.makeText(context, "加入应战 成功", 1).show();
					} else if(!jrb.getNoName().equals("0")){//不是0的时候 说明加入这个应战的用户不存在
						Toast.makeText(context, "用户名 不存在,加入应战 失败!", 0).show();
					}else if(!jrb.getOnName().equals("0")){//不是0的时候，说明这个用户已经参加了这个应战  不能再参加了
						Toast.makeText(context, "加入申请已发送 等待审核中!", 0).show();
					} 	
				}  
				break; 
			case 0x55://加入应战失败 
				Toast.makeText(context, "加入应战失败", 1).show();
				break;
			case 0x99://表示用户已经参加该条目上面的活动了，不能再进行参加
				Toast.makeText(context, "您已经参加本次活动了", 0).show();
				break;
			} 
		};
	};
	
	/** 为ImageView创建点击事件响应*/
	class  MyOnClickListener implements OnClickListener {
		private static final String TAG = "加入应战里面的点击事件，MyOnClickListener";
		private int mPositions;
		private ViewHolder iv;
		
		public MyOnClickListener(int pos,ViewHolder iv){
			this.mPositions=pos;
			this.iv=iv;
		}
		
		@Override
		public void onClick(View v) {
			mProgressDialog=new ProgressDialog(context);
			try{
				final JSONObject object=new JSONObject();
				object.put("UserName", UserGlobal.UserName);//参数是正常登录的用户名(邮箱)
				object.put("ActID", ((Integer)mListViewData.get(mPositions).get("ActID")).intValue());//参数是活动的ID
				Log.i(TAG,"加入应战传入到服务端的参数为:"+object.toString());   
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgressDialog.setMessage("正在加入中...");
				mProgressDialog.setIndeterminate(true); 
				mProgressDialog.setCancelable(false);
				mProgressDialog.setCanceledOnTouchOutside(false);
				mProgressDialog.show();
				new Thread(){
					public void run(){  
						//首先判断用户点击的这个条目用户是否已经参加应战
						List<Integer> ids=WebGetUserOnAct.getGetUserOnActData(UserGlobal.UserID);
						if(ids!=null){//说明已经有参加应战活动,然后跟当前用户点击的条目活动ID进行匹配
							for (Integer integer : ids) {
								if(integer==((Integer)mListViewData.get(mPositions).get("ActID")).intValue()){
									mHandler.sendEmptyMessage(0x99);//发送已经参数该条目上活动的标识
									return;
								}
							}
						}
						//没有参加过任何活动就进行参加应战活动 
						JoinActResultBean token=WebJoinAct.getJoinActData(object.toString()); 
						Log.i(TAG,"点击加入应战图标之后返回的状态集是："+token);
						if(token!=null){ 
							Message msg=new Message();
							msg.what=0x11;
							token.setmView(iv.mImageState);
							token.setmPosition(mPositions);
							msg.obj=token;
							mHandler.sendMessage(msg);//发送加入应战成功标识 和 ImageView
						}else{
							mHandler.sendEmptyMessage(0x55);//发送加入应战失败标识
						} 
					}
				}.start();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				mProgressDialog.dismiss();
			}
		}  
	}  
	
	
}
