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
				// 从Uri路径上加载图片资源进行显示登录用户的头像
				ImageLoader.getInstance().displayImage(
						WebServiceUtils.IMAGE_URL + mData.get(0).getPicture(),
						setting_personal_image, AppApplication.options,
						new SimpleImageLoadingListener(),
						new ImageLoadingProgressListener() {
							@Override
							public void onProgressUpdate(String imageUri, View view, int current, int total) {
								Log.i(TAG, "图片异步加载的数据:total=" + total + ",current=" + current);
							}
						});
				break;

			case 0x22:// 查询用户资料失败
				record++;
				if(record <= 2){
					conditionQuery();//如果查询失败就再查询一次
				}else{
					record=0;//清零
					Log.i(TAG, "没有查询到该用户的信息");
				} 
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
		View view = inflater.inflate(R.layout.setting, container, false);
		initView(view); 
		conditionQuery();//查询一次用户数据
		return view;
	}

	/** 对页面上的所有组件进行初始化,并绑定事件 */
	public void initView(View v) {
		setting_personal_image = (ImageView) v .findViewById(R.id.setting_personal_image);// 个人图片
		setting_specify_info = (TextView) v .findViewById(R.id.setting_specify_info);// 详细信息
		setting_data_update = (TextView) v .findViewById(R.id.setting_data_update);// 资料更新
		setting_tick_suggesting = (TextView) v .findViewById(R.id.setting_tick_suggesting);// 意见反馈
		setting_aboutus = (TextView) v.findViewById(R.id.setting_aboutus);
		setting_nullify = (Button) v.findViewById(R.id.setting_nullify);// 注销

		setting_specify_info.setOnClickListener(this);
		setting_data_update.setOnClickListener(this);
		setting_aboutus.setOnClickListener(this);
		setting_nullify.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.setting_specify_info:// 详细信息
			if(mData!=null){
				intent = new Intent(getActivity(), UserDetails.class);
				Bundle extras=new Bundle();
				extras.putSerializable("GetUserInfoBean", mData.get(0));
				intent.putExtras(extras);
				getActivity().startActivity(intent);// 跳转到详细信息的界面
//				Toast.makeText(getActivity(), "详细信息", 0).show();
			}else{
				Toast.makeText(getActivity(), "暂无个人资料", 0).show();
			}
			break;
		case R.id.setting_data_update:// 点击了资料更新
			intent = new Intent(getActivity(), UserUpdate.class);
			getActivity().startActivity(intent);// 跳转到更新用户资料的界面
//			Toast.makeText(getActivity(), "资料更新", 0).show();
			break;
		case R.id.setting_tick_suggesting:// 意见反馈
//			Toast.makeText(getActivity(), "意见反馈", 0).show();
			break;
		case R.id.setting_aboutus:// 点击了关于我们
			intent = new Intent(getActivity(), AboutUs.class);
			getActivity().startActivity(intent);// 跳转到关于我们的界面
//			Toast.makeText(getActivity(), "关于我们", 0).show();
			break;
		case R.id.setting_nullify:// 注销
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
					.setTitle("亲,您确定注销本次登录吗?")
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// 点击了取消不做任何操作
								}
							})
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// 点击了确定,进行退出登录操作
									setting_personal_image.setImageResource(R.drawable.personal_default);// 设置为默认图片
									UserGlobal.UserExist = false;
									UserGlobal.UserID = -1;
									UserGlobal.NickName = null;
									UserGlobal.Name = null;
									UserGlobal.UserName=null;
									UserGlobal.sex = null;
									UserGlobal.Age = null;
									((MainActivity) getActivity()).setChoiceFragment(R.id.main_home);// 跳转到主界面
									getActivity().stopService(UserGlobal.user_intent);//当注销当前登录用户时了把用户监听服务停止	
								}
							});
			builder.create().show();
		}
	}
	
	private void conditionQuery(){
		//如果用户正常登录存在了就查询一次用户数据，作用户详情页面数据使用
				if (UserGlobal.UserExist) {// 如果用户已经正常登录了，在这里查询用户信息
					new Thread() {// 开启线程处理
						public void run() {
							try {
								JSONObject object = new JSONObject();
								object.put("UserID", UserGlobal.UserID);
								List<GetUserInfoBean> result = WebGetAUser.getGetAUserData(object.toString());
								if (result != null) {
									Message msg = new Message();
									msg.what = 0x11;
									msg.obj = result;
									mHandler.sendMessage(msg);// 发送数据到Handler
								} else {
									mHandler.sendEmptyMessage(0x22);//发送查询数据失败
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}.start();
				} 
	}

}
