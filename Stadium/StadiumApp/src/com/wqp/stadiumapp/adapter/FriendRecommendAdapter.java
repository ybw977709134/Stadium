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
	private ProgressDialog mProgressDialog;//���ȶԻ���
	private JoinActResultBean jrb;//�û���ӻ�ɹ�֮�󷵻ص�������Ϣ��
	 
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
			vh.mImageState=(ImageView)v.findViewById(mTo[5]);//��Ŀ�ұߵ����ͼ�갴ť
			v.setTag(vh);
		}else{
			v=convertView;  
		} 
		vh=(ViewHolder) v.getTag();
		Log.i("��Ӧս�б���","��ǰ��Ŀͼ��״̬Ϊ:"+(Boolean)mListViewData.get(position).get("Imgstate")); 
		if(mListViewData!=null && (Boolean)mListViewData.get(position).get("Imgstate")){ //������ 
			Log.i("ADAPTER","��Ŀ "+position+"=true"+"YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
			vh.mImageState.setImageResource(R.drawable.friend_recommend_xlistview_add_ok); 
			vh.mImageState.setSaveEnabled(false);//���ò���ͼ��ʹ��
			vh.mImageState.setOnClickListener(null);
		}else if(mListViewData!=null && !(Boolean)mListViewData.get(position).get("Imgstate")){//δ����
			Log.i("ADAPTER","��Ŀ "+position+"=false"+"NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN");
			vh.mImageState.setImageResource(R.drawable.friend_recommend_xlistview_add);
			vh.mImageState.setOnClickListener(new MyOnClickListener(position,vh));
			Log.i("ADAPTER","��Ŀ "+position+"=false");
		} 
		
		//�첽���ظ�������
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
	
	
	
	/** ���²������첽���� */
	private void asyncTask(ViewHolder vh,int position) {
		new MyAsyncTask(vh).execute(position);
	}
	
	private class MyAsyncTask extends AsyncTask<Integer,Integer,Map<String,Object>> { 
		private static final String TAG = "FriendApplyAdapter >>> MyAsyncTask"; 
		private ViewHolder vh; 
		public MyAsyncTask(ViewHolder vh ){
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
			vh.mVenuesName.setText((String) result.get(mFrom[0]));//��������
			vh.mSportType.setText((String)result.get(mFrom[1]));//�˶�����
			vh.mStartTime.setText((String)result.get(mFrom[2]));//��ʼʱ��
			vh.mEndTime.setText((String) result.get(mFrom[3]));//����ʱ��
			vh.mTakeCount.setText((String) result.get(mFrom[4]));//��������
			
		} 
	} 
	
	//������Ϣ������
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x11://����Ӧս�ɹ�
				jrb  =  (JoinActResultBean) msg.obj; 
				if(jrb!=null){
					if(jrb.isUserExist() && jrb.getNoName().equals("0") && jrb.getOnName().equals("0")){ 
						((ImageView)jrb.getmView()).setImageResource(R.drawable.friend_recommend_xlistview_add_ok);//����ͼ��
						notifyDataSetChanged();//ˢ��
						mListViewData.get(jrb.getmPosition()).put("Imgstate", true); 
						Toast.makeText(context, "����Ӧս �ɹ�", 1).show();
					} else if(!jrb.getNoName().equals("0")){//����0��ʱ�� ˵���������Ӧս���û�������
						Toast.makeText(context, "�û��� ������,����Ӧս ʧ��!", 0).show();
					}else if(!jrb.getOnName().equals("0")){//����0��ʱ��˵������û��Ѿ��μ������Ӧս  �����ٲμ���
						Toast.makeText(context, "���������ѷ��� �ȴ������!", 0).show();
					} 	
				}  
				break; 
			case 0x55://����Ӧսʧ�� 
				Toast.makeText(context, "����Ӧսʧ��", 1).show();
				break;
			case 0x99://��ʾ�û��Ѿ��μӸ���Ŀ����Ļ�ˣ������ٽ��вμ�
				Toast.makeText(context, "���Ѿ��μӱ��λ��", 0).show();
				break;
			} 
		};
	};
	
	/** ΪImageView��������¼���Ӧ*/
	class  MyOnClickListener implements OnClickListener {
		private static final String TAG = "����Ӧս����ĵ���¼���MyOnClickListener";
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
				object.put("UserName", UserGlobal.UserName);//������������¼���û���(����)
				object.put("ActID", ((Integer)mListViewData.get(mPositions).get("ActID")).intValue());//�����ǻ��ID
				Log.i(TAG,"����Ӧս���뵽����˵Ĳ���Ϊ:"+object.toString());   
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgressDialog.setMessage("���ڼ�����...");
				mProgressDialog.setIndeterminate(true); 
				mProgressDialog.setCancelable(false);
				mProgressDialog.setCanceledOnTouchOutside(false);
				mProgressDialog.show();
				new Thread(){
					public void run(){  
						//�����ж��û�����������Ŀ�û��Ƿ��Ѿ��μ�Ӧս
						List<Integer> ids=WebGetUserOnAct.getGetUserOnActData(UserGlobal.UserID);
						if(ids!=null){//˵���Ѿ��вμ�Ӧս�,Ȼ�����ǰ�û��������Ŀ�ID����ƥ��
							for (Integer integer : ids) {
								if(integer==((Integer)mListViewData.get(mPositions).get("ActID")).intValue()){
									mHandler.sendEmptyMessage(0x99);//�����Ѿ���������Ŀ�ϻ�ı�ʶ
									return;
								}
							}
						}
						//û�вμӹ��κλ�ͽ��вμ�Ӧս� 
						JoinActResultBean token=WebJoinAct.getJoinActData(object.toString()); 
						Log.i(TAG,"�������Ӧսͼ��֮�󷵻ص�״̬���ǣ�"+token);
						if(token!=null){ 
							Message msg=new Message();
							msg.what=0x11;
							token.setmView(iv.mImageState);
							token.setmPosition(mPositions);
							msg.obj=token;
							mHandler.sendMessage(msg);//���ͼ���Ӧս�ɹ���ʶ �� ImageView
						}else{
							mHandler.sendEmptyMessage(0x55);//���ͼ���Ӧսʧ�ܱ�ʶ
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
