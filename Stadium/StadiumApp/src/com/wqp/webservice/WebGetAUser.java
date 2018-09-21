package com.wqp.webservice; 
import java.util.ArrayList; 
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

import com.wqp.stadiumapp.utils.WebServiceUtils; 
import com.wqp.webservice.entity.GetUserInfoBean;

/** 通过该方法可以获取到Web端方法名为:Login返回的Json格式的字符串
 * @see UserInfo-会员表
 */
public class WebGetAUser {
	private static final String TAG = "WebGetAUser";

	/**
	 * 输入名称获取到用户名信息
	 * @param name
	 * @see 例如：str={ "UserID": "1"}
	 */
	public static List<GetUserInfoBean> getGetAUserData(String str){
		//命名空间+方法名称
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.GETUSER;
		
		/**创建HttpTransportSE对象,参数：服务端的url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**实例化SoapObject对象,并设置属性参数,参数1：命名空间,参数2：方法名称*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.GETUSER);
		soapObject.addProperty("str",str);//设置web端方法的参数
		
		/**使用SOAP协议获得序列化的Envelope
		 * 将SoapObject对象设置为SoapSerializationEnvelope对象的传出SOAP消息
		 * 参数：SoapEnvelope版本号
		 */
		SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(SoapEnvelope.VER10);
		envelope.bodyOut=soapObject;
		envelope.dotNet=true;
		envelope.setOutputSoapObject(soapObject);
		envelope.encodingStyle="UTF-8";
		try{
			/**开始调用远程方法,参数1:命名空间+方法名称,参数2：实例化后的Envelope*/
			httpse.call(soapAction, envelope);
			if(envelope.getResponse()!=null){
				//获取服务器响应返回的SOAP消息
				SoapObject soap=(SoapObject) envelope.bodyIn;
				/**获取返回的数据,参数：服务端返回的方法名(可以在服务端查找)*/
				String result=soap.getProperty("GetAUserResult").toString();
				return ParseGetAUser(result); 
			} 
		}catch(Exception e){
			e.printStackTrace();
		} 
		return null;
	}
	
	/** 根据输入的JSON格式字符串进行解析成Java对象,并封装成List集合进行返回*/
	public static List<GetUserInfoBean> ParseGetAUser(String result){ 
		List<GetUserInfoBean> data = null;
		try {
			JSONObject jsonObject=new JSONObject(result);
			JSONArray jsonArray=jsonObject.getJSONArray("ds");
			int len=jsonArray.length();
			Log.i(TAG,"长度为="+len);
			if(len!=0){
				data=new ArrayList<GetUserInfoBean>();
				for(int i=0;i<len;i++){
					JSONObject tempJSONObject=new JSONObject(jsonArray.getString(i));
					GetUserInfoBean uid=new GetUserInfoBean();
					uid.setUserid(tempJSONObject.getInt("UserID"));
					uid.setUsername(tempJSONObject.getString("UserName"));
					uid.setPassword(tempJSONObject.getString("password"));
					uid.setName(tempJSONObject.getString("Name"));
					uid.setNickname(tempJSONObject.getString("Nickname"));
					uid.setPicture(dateParseImageAddress(tempJSONObject.getString("Picture")));//这个是用户的头像图片路径，因返回路径字符有问题，已经做了特殊处理，请留意.	
					uid.setEmail(tempJSONObject.getString("Email"));
					uid.setQq(tempJSONObject.getString("QQ"));
					uid.setMsn(tempJSONObject.getString("MSN"));
					uid.setSex(tempJSONObject.getString("Sex"));
					uid.setAge(tempJSONObject.getString("Age"));
					uid.setUsertype(tempJSONObject.getString("UserType"));
					uid.setAbstracts(tempJSONObject.getString("Abstract"));
					uid.setAddress(tempJSONObject.getString("Address"));
					uid.setPhone(tempJSONObject.getString("Phone"));
					uid.setWebsite(tempJSONObject.getString("Website"));
					uid.setHobby(tempJSONObject.getString("Hobby"));
					uid.setVenue(tempJSONObject.getString("Venue"));
					uid.setCharge(tempJSONObject.getString("Charge"));
					uid.setBirthyear(tempJSONObject.get("BirthYear").toString());//解析出生日期为字符串
					uid.setDanwei(tempJSONObject.getString("DanWei"));
					uid.setRewards(tempJSONObject.getString("Rewards"));
					uid.setCertificate(tempJSONObject.getString("Certificate"));
					uid.setAdvanced(tempJSONObject.getString("Advanced"));
					uid.setFriend(tempJSONObject.getString("Friend"));
					uid.setInviter(tempJSONObject.getString("Inviter"));
					uid.setCgwebsite(tempJSONObject.getString("CGWebsite"));
					uid.setTelephone(tempJSONObject.getString("Telephone"));
					uid.setShopsname(tempJSONObject.getString("ShopsName"));
					uid.setFactory(tempJSONObject.getString("Factory"));
					uid.setShop(tempJSONObject.getString("shop"));
					uid.setZipcode(tempJSONObject.getString("ZipCode"));
					uid.setTgstype(tempJSONObject.getString("TGStype")); 
					uid.setCreatertime(dateParse(tempJSONObject.get("CreaterTime").toString()));//解析加入时间
					uid.setState(tempJSONObject.getString("State"));  
					Log.i(TAG,"获取到的用户头像图片路径:"+tempJSONObject.getString("Picture"));
					data.add(uid);
				}
			}  
		} catch (JSONException e) { 
			e.printStackTrace();
		}
		return data!=null ? data : null;
	}
	
	/** 输入web端的时间字符串,并转换成本时时间返回 */
	private static String dateParse(String result) { 
		if(result!=null){ 
			return result.replace('T', ' ');
		} 
		return null;
	} 
	
	/** 输入web端的图片地址字符串,并转换成有效路径返回 */
	private static String dateParseImageAddress(String img) { 
		if(img!=null){ 
			return "/Admin"+img.substring(2);
		} 
		return null;
	}
	
}
