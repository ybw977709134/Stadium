package com.wqp.stadiumapp.utils;  
 
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class WebServiceUtils {
	/** Service namespace*/
	public static String NAMESPACE="http://tempuri.org/";
	public static String SERVICEURL="http://whjunt.vicp.cc:9011/LWebservice.asmx";
	public static String IMAGE_URL="http://whjunt.vicp.cc:9011";
	
	/** invoke method name*/
	public static String ADDRESERVATION="AddReservation";//添加活动信息
	public static String ADDUSER="AddUser";//注册
	public static String LOGIN="Login";//登陆
	public static String GETUSER="GetAUser";//查询个人信息(获取个人信息)
	public static String USERUPDATE="UserUpdate";//修改个人信息
	public static String GETARTINFO="GetArtInfo";//获取活动信息
	public static String GETCOMMENT="GetComment";//获取点评信息
	public static String GETGUIDE="GetGuide";//获取指导信息
	public static String GETSPORTS="GetSports";//获取运动信息
	public static String GETVENUES="GetVenues";//获取场馆信息
	public static String JOINACT="JoinAct";//应战方法
	public static String UPLOAD="Upload ";//待定 
	public static String PWDUPDATE="PWDUpdate";//更新密码方法
	public static String GETVENUESEXPERT="GetVenuesExpert";//获取私人教练信息
	public static String GETUSERONACT="GetUserOnAct";//根据用户ID查询用户已经参加了哪些活动
	public static String GETALLMAKE="GetAllMake";//根据场馆ID获取到场馆信息
	public static String ADDMAKE="AddMake";//添加场馆预约信息到服务端
	public static String GETUSERMAKE="GetUserMake";//根据用户ID获取到当前用户我的预约订单信息
	public static String REMOVEMAKE="RemoveMake";//取消预约订单信息
	public static String GET5SECONDU="Get5secondU";//个人用户五秒查询服务器端消息
	public static String GET5SECONDV="Get5secondV";//场馆用户五秒查询客户端提交的数据
	public static String GETSPORTSVENUES="GetSportsVenues";//根据区域名称和运动项目名称进行模糊查询操作,返回场馆信息集合
	public static String ADDUSERVM="AddUserVM";//向服务端添加用户选择的场馆信息
	
	/**
	 * 根据输入的参数创建Json format string
	 * @param key 表示json字符串的头信息
	 * @param value 表示解析的集合类型值
	 * @return Json格式字符串
	 */
	public static String createJsonString(String key,Object value){   
		JSONObject jsonObject=new JSONObject();
		String res = null;
		try {
			jsonObject.put(key,value);
			res=jsonObject.toString();
			Log.i("测试Json解析之后的值：",res);
		} catch (JSONException e) { 
			e.printStackTrace();
		} 
		return res;
	} 
}
