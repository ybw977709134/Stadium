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

/** ͨ���÷������Ի�ȡ��Web�˷�����Ϊ:Login���ص�Json��ʽ���ַ���
 * @see UserInfo-��Ա��
 */
public class WebGetAUser {
	private static final String TAG = "WebGetAUser";

	/**
	 * �������ƻ�ȡ���û�����Ϣ
	 * @param name
	 * @see ���磺str={ "UserID": "1"}
	 */
	public static List<GetUserInfoBean> getGetAUserData(String str){
		//�����ռ�+��������
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.GETUSER;
		
		/**����HttpTransportSE����,����������˵�url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**ʵ����SoapObject����,���������Բ���,����1�������ռ�,����2����������*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.GETUSER);
		soapObject.addProperty("str",str);//����web�˷����Ĳ���
		
		/**ʹ��SOAPЭ�������л���Envelope
		 * ��SoapObject��������ΪSoapSerializationEnvelope����Ĵ���SOAP��Ϣ
		 * ������SoapEnvelope�汾��
		 */
		SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(SoapEnvelope.VER10);
		envelope.bodyOut=soapObject;
		envelope.dotNet=true;
		envelope.setOutputSoapObject(soapObject);
		envelope.encodingStyle="UTF-8";
		try{
			/**��ʼ����Զ�̷���,����1:�����ռ�+��������,����2��ʵ�������Envelope*/
			httpse.call(soapAction, envelope);
			if(envelope.getResponse()!=null){
				//��ȡ��������Ӧ���ص�SOAP��Ϣ
				SoapObject soap=(SoapObject) envelope.bodyIn;
				/**��ȡ���ص�����,����������˷��صķ�����(�����ڷ���˲���)*/
				String result=soap.getProperty("GetAUserResult").toString();
				return ParseGetAUser(result); 
			} 
		}catch(Exception e){
			e.printStackTrace();
		} 
		return null;
	}
	
	/** ���������JSON��ʽ�ַ������н�����Java����,����װ��List���Ͻ��з���*/
	public static List<GetUserInfoBean> ParseGetAUser(String result){ 
		List<GetUserInfoBean> data = null;
		try {
			JSONObject jsonObject=new JSONObject(result);
			JSONArray jsonArray=jsonObject.getJSONArray("ds");
			int len=jsonArray.length();
			Log.i(TAG,"����Ϊ="+len);
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
					uid.setPicture(dateParseImageAddress(tempJSONObject.getString("Picture")));//������û���ͷ��ͼƬ·�����򷵻�·���ַ������⣬�Ѿ��������⴦��������.	
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
					uid.setBirthyear(tempJSONObject.get("BirthYear").toString());//������������Ϊ�ַ���
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
					uid.setCreatertime(dateParse(tempJSONObject.get("CreaterTime").toString()));//��������ʱ��
					uid.setState(tempJSONObject.getString("State"));  
					Log.i(TAG,"��ȡ�����û�ͷ��ͼƬ·��:"+tempJSONObject.getString("Picture"));
					data.add(uid);
				}
			}  
		} catch (JSONException e) { 
			e.printStackTrace();
		}
		return data!=null ? data : null;
	}
	
	/** ����web�˵�ʱ���ַ���,��ת���ɱ�ʱʱ�䷵�� */
	private static String dateParse(String result) { 
		if(result!=null){ 
			return result.replace('T', ' ');
		} 
		return null;
	} 
	
	/** ����web�˵�ͼƬ��ַ�ַ���,��ת������Ч·������ */
	private static String dateParseImageAddress(String img) { 
		if(img!=null){ 
			return "/Admin"+img.substring(2);
		} 
		return null;
	}
	
}
