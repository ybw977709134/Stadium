package com.wqp.stadiumapp;

import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog; 
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wqp.stadiumapp.utils.UserGlobal;
import com.wqp.stadiumapp.utils.WebServiceUtils;
import com.wqp.webservice.WebGetUserOnAct;
import com.wqp.webservice.WebJoinAct;
import com.wqp.webservice.entity.GetActInfoBean;
import com.wqp.webservice.entity.JoinActResultBean;

public class JoinActDetails extends Activity implements OnClickListener {
	private String TAG="JoinActDetails";
	private ImageView joinact_detail_back_arrow,joinact_detail_pubimg;
	private TextView joinact_detail_venuesname,joinact_detail_sporttype,joinact_detail_takecount,
	joinact_detail_starttime,joinact_detail_applyendtime,joinact_detail_venuesaddress;
	private Button joinact_detail_attend;
	
	private GetActInfoBean mGAIB;
	private ProgressDialog mProgressDialog;//���ȶԻ���
	private JoinActResultBean jrb;//�û���ӻ�ɹ�֮�󷵻ص�������Ϣ��
	
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x44:
				jrb=(JoinActResultBean) msg.obj;
				if(jrb!=null){
					if(jrb.isUserExist() && jrb.getNoName().equals("0") && jrb.getOnName().equals("0")){
						joinact_detail_attend.setText("�ѱ���");
						Toast.makeText(JoinActDetails.this, "����Ӧս�ɹ�!", 0).show(); 
					}else if(!jrb.getNoName().equals("0")){
						Toast.makeText(JoinActDetails.this, "�û���������,����Ӧսʧ��!", 0).show();
					}else if(!jrb.getOnName().equals("0")){
						Toast.makeText(JoinActDetails.this, "���������ѷ��� �ȴ������!", 0).show();
						finish();
					} 	
				} 
				break; 
			case 0x55://���ռ���Ӧսʧ��
				Toast.makeText(JoinActDetails.this, "����Ӧսʧ����!", 0).show();
				break;
			case 0x99://��ʾ�û��Ѿ��μӸ���Ŀ����Ļ�ˣ������ٽ��вμ�
				Toast.makeText(JoinActDetails.this, "���Ѿ��μӱ��λ��", 0).show();
				break;
			} 
		};
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.join_act_detail);
		mGAIB=(GetActInfoBean) getIntent().getSerializableExtra("GetActInfoBean"); 
		initView();
		
		if(mGAIB!=null){
			//����������ݲ���
			initPageData();
		} 
	}
	
	/** ��ҳ�����������ʼ�������¼�*/
	public void initView(){
		joinact_detail_back_arrow=(ImageView) findViewById(R.id.joinact_detail_back_arrow);//����ͼ�갴ť
		joinact_detail_pubimg=(ImageView) findViewById(R.id.joinact_detail_pubimg);//����ͼƬ
		joinact_detail_venuesname=(TextView) findViewById(R.id.joinact_detail_venuesname);//��������
		joinact_detail_sporttype=(TextView) findViewById(R.id.joinact_detail_sporttype);//�˶�����
		joinact_detail_takecount=(TextView) findViewById(R.id.joinact_detail_takecount);//�����
		joinact_detail_starttime=(TextView) findViewById(R.id.joinact_detail_starttime);//�ʱ��
		joinact_detail_applyendtime=(TextView) findViewById(R.id.joinact_detail_applyendtime);//��ֹ����ʱ��
		joinact_detail_venuesaddress=(TextView) findViewById(R.id.joinact_detail_venuesaddress);//���ݵ�ַ
		joinact_detail_attend=(Button) findViewById(R.id.joinact_detail_attend);//������ť
		
		joinact_detail_back_arrow.setOnClickListener(this);
		joinact_detail_attend.setOnClickListener(this); 
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.joinact_detail_back_arrow://����˷���ͼ�갴ť
			finish();
			break;

		case R.id.joinact_detail_attend://����˱�����ť
			if(mGAIB!=null && !mGAIB.isImgstate()){ //δ����ʱ
				joinAction();
			}else{ //�ѱ���
//				Toast.makeText(JoinActDetails.this, "�Ѿ�����", 0).show(); 
			}
			break;
		} 
	}
	
	/** ��ҳ�����������ݲ���*/
	public void initPageData(){
		joinact_detail_venuesname.setText(mGAIB.getVenuesname());//��������
		joinact_detail_sporttype.setText("�����:"+mGAIB.getSporttypestring());//�����
		joinact_detail_takecount.setText("�����:"+mGAIB.getTakecount());//�����
		joinact_detail_starttime.setText("�ʱ��:"+mGAIB.getStarttime());//�ʱ��
		joinact_detail_applyendtime.setText("������ֹʱ��:"+mGAIB.getApplyendtime());//������ֹʱ��
		joinact_detail_venuesaddress.setText("�����:"+mGAIB.getActnotice());//���ݵ�ַ����Ϊ�����
		//�ѳ��ݻͼƬ���õ�ImageView�������
		ImageLoader.getInstance().displayImage( WebServiceUtils.IMAGE_URL+mGAIB.getPubimg(), joinact_detail_pubimg, 
				AppApplication.options, new SimpleImageLoadingListener(), new ImageLoadingProgressListener(){ 					@Override
					public void onProgressUpdate(String arg0, View view, int current, int total) { 
						Log.i(TAG,"���ݻͼƬ�첽���ص�����:total="+total+",current="+current);
					} 
				});
		if(mGAIB.isImgstate()){//�����ǰ�û��Ѿ��μ��˻
			joinact_detail_attend.setText("�ѱ���");
		}else{//��ǰ�û���δ�μӻ
			joinact_detail_attend.setText("����");
		}
	}
	
	/** ����˱�����ť����Ӧս*/
	private void joinAction(){ 
		//�������ݵ�����˽��м���Ӧս
		try{
			final JSONObject object=new JSONObject();
			object.put("UserName", UserGlobal.UserName);//������������¼���û���(����)
			object.put("ActID", mGAIB.getActid().intValue());//�����ǻ��ID
			Log.i(TAG,"����Ӧս���뵽����˵Ĳ���Ϊ:"+object.toString());  
			mProgressDialog=new ProgressDialog(JoinActDetails.this);
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
							if(integer==mGAIB.getActid().intValue()){//�����ѯ���ĻID���ڵ�ǰ�û��������ĿID
								mHandler.sendEmptyMessage(0x99);//�����Ѿ���������Ŀ�ϻ�ı�ʶ
								return;
							}
						}
					}  
					//û�вμӹ��κλ�ͽ��вμ�Ӧս� 
					JoinActResultBean token=WebJoinAct.getJoinActData(object.toString());  
					Log.i(TAG,"�����������ť����Ӧսʱ���ص�״̬����:"+token);
					if(token!=null){
						Message msg=new Message();
						msg.what=0x44; 
						msg.obj=token;
						mHandler.sendMessage(msg);//���ͼ���Ӧս�ɹ���ʶ �Ͳ�����Ŀ��View
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
