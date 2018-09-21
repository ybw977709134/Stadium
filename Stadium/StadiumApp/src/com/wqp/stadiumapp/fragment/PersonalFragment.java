package com.wqp.stadiumapp.fragment;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wqp.stadiumapp.AboutUs;
import com.wqp.stadiumapp.AppApplication;
import com.wqp.stadiumapp.MainActivity;
import com.wqp.stadiumapp.R;
import com.wqp.stadiumapp.UserDetails;
import com.wqp.stadiumapp.UserUpdate;
import com.wqp.stadiumapp.utils.UserGlobal;
import com.wqp.stadiumapp.utils.WebServiceUtils;
import com.wqp.webservice.WebGetAUser;
import com.wqp.webservice.entity.GetUserInfoBean;

public class PersonalFragment extends Fragment implements OnClickListener {
	private static final String TAG = "PersonalFragment";
	private ImageView setting_personal_image;
	private Button setting_nullify;
	private TextView setting_specify_info, setting_data_update,
			setting_tick_suggesting, setting_aboutus;

	private List<GetUserInfoBean> mData;
	private static int record=0;

	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x11://
				mData = (List<GetUserInfoBean>) msg.obj; 
				// ��Uri·���ϼ���ͼƬ��Դ������ʾ��¼�û���ͷ��
				ImageLoader.getInstance().displayImage(
						WebServiceUtils.IMAGE_URL + mData.get(0).getPicture(),
						setting_personal_image, AppApplication.options,
						new SimpleImageLoadingListener(),
						new ImageLoadingProgressListener() {
							@Override
							public void onProgressUpdate(String imageUri, View view, int current, int total) {
								Log.i(TAG, "ͼƬ�첽���ص�����:total=" + total + ",current=" + current);
							}
						});
				break;

			case 0x22:// ��ѯ�û�����ʧ��
				record++;
				if(record <= 2){
					conditionQuery();//�����ѯʧ�ܾ��ٲ�ѯһ��
				}else{
					record=0;//����
					Log.i(TAG, "û�в�ѯ�����û�����Ϣ");
				} 
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
		View view = inflater.inflate(R.layout.setting, container, false);
		initView(view); 
		conditionQuery();//��ѯһ���û�����
		return view;
	}

	/** ��ҳ���ϵ�����������г�ʼ��,�����¼� */
	public void initView(View v) {
		setting_personal_image = (ImageView) v .findViewById(R.id.setting_personal_image);// ����ͼƬ
		setting_specify_info = (TextView) v .findViewById(R.id.setting_specify_info);// ��ϸ��Ϣ
		setting_data_update = (TextView) v .findViewById(R.id.setting_data_update);// ���ϸ���
		setting_tick_suggesting = (TextView) v .findViewById(R.id.setting_tick_suggesting);// �������
		setting_aboutus = (TextView) v.findViewById(R.id.setting_aboutus);
		setting_nullify = (Button) v.findViewById(R.id.setting_nullify);// ע��

		setting_specify_info.setOnClickListener(this);
		setting_data_update.setOnClickListener(this);
		setting_aboutus.setOnClickListener(this);
		setting_nullify.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.setting_specify_info:// ��ϸ��Ϣ
			if(mData!=null){
				intent = new Intent(getActivity(), UserDetails.class);
				Bundle extras=new Bundle();
				extras.putSerializable("GetUserInfoBean", mData.get(0));
				intent.putExtras(extras);
				getActivity().startActivity(intent);// ��ת����ϸ��Ϣ�Ľ���
//				Toast.makeText(getActivity(), "��ϸ��Ϣ", 0).show();
			}else{
				Toast.makeText(getActivity(), "���޸�������", 0).show();
			}
			break;
		case R.id.setting_data_update:// ��������ϸ���
			intent = new Intent(getActivity(), UserUpdate.class);
			getActivity().startActivity(intent);// ��ת�������û����ϵĽ���
//			Toast.makeText(getActivity(), "���ϸ���", 0).show();
			break;
		case R.id.setting_tick_suggesting:// �������
//			Toast.makeText(getActivity(), "�������", 0).show();
			break;
		case R.id.setting_aboutus:// ����˹�������
			intent = new Intent(getActivity(), AboutUs.class);
			getActivity().startActivity(intent);// ��ת���������ǵĽ���
//			Toast.makeText(getActivity(), "��������", 0).show();
			break;
		case R.id.setting_nullify:// ע��
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
					.setTitle("��,��ȷ��ע�����ε�¼��?")
					.setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// �����ȡ�������κβ���
								}
							})
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// �����ȷ��,�����˳���¼����
									setting_personal_image.setImageResource(R.drawable.personal_default);// ����ΪĬ��ͼƬ
									UserGlobal.UserExist = false;
									UserGlobal.UserID = -1;
									UserGlobal.NickName = null;
									UserGlobal.Name = null;
									UserGlobal.UserName=null;
									UserGlobal.sex = null;
									UserGlobal.Age = null;
									((MainActivity) getActivity()).setChoiceFragment(R.id.main_home);// ��ת��������
									getActivity().stopService(UserGlobal.user_intent);//��ע����ǰ��¼�û�ʱ�˰��û���������ֹͣ	
								}
							});
			builder.create().show();
		}
	}
	
	private void conditionQuery(){
		//����û�������¼�����˾Ͳ�ѯһ���û����ݣ����û�����ҳ������ʹ��
				if (UserGlobal.UserExist) {// ����û��Ѿ�������¼�ˣ��������ѯ�û���Ϣ
					new Thread() {// �����̴߳���
						public void run() {
							try {
								JSONObject object = new JSONObject();
								object.put("UserID", UserGlobal.UserID);
								List<GetUserInfoBean> result = WebGetAUser.getGetAUserData(object.toString());
								if (result != null) {
									Message msg = new Message();
									msg.what = 0x11;
									msg.obj = result;
									mHandler.sendMessage(msg);// �������ݵ�Handler
								} else {
									mHandler.sendEmptyMessage(0x22);//���Ͳ�ѯ����ʧ��
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}.start();
				} 
	}

}
