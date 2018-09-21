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
import com.wqp.webservice.entity.GetUserMakeBean;

/** ͨ���÷������Ի�ȡ��Web�˷�����Ϊ:GetUserMake���ص�Json��ʽ���ַ���*/
public class WebGetUserMake { 
	private static final String TAG = "WebGetUserMake";

	/** ��Web�˻�ȡָ����Ϣ
	 * @param str �������Ҫ��д��д""����,�������дSQL���
	 *@return <returns>����DataSet ���ػ��Ϣ Json��ʽ</returns>;��web�˽��������ݷ�װ��JavaBean�Լ��ϵ���ʽ���� 
	 */
	public static List<GetUserMakeBean> getGetUserMakeData(String str){
		//�����ռ�+��������
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.GETUSERMAKE;
		
		/**����HttpTransportSE����,����������˵�url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**ʵ����SoapObject����,���������Բ���,����1�������ռ�,����2����������*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.GETUSERMAKE);
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
				String result=soap.getProperty("GetUserMakeResult").toString();
				Log.i(TAG,"���ݼ�:"+result);
				return ParseGetArtInfo(result); 
			} 
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/** ���������JSON��ʽ�ַ������н�����Java����,����װ��List���Ͻ��з���*/
	public static List<GetUserMakeBean> ParseGetArtInfo(String result) throws Exception{  
		List<GetUserMakeBean> data = null;
		try {
			JSONObject jsonObject=new JSONObject(result);
			JSONArray jsonArray=jsonObject.getJSONArray("ds");
			int len=jsonArray.length();
			Log.i(TAG,"����Ϊ:"+len);
			if(len>0){
				data=new ArrayList<GetUserMakeBean>();
				for(int i=0;i<len;i++){
					JSONObject tempJSONObject=new JSONObject(jsonArray.getString(i));
					GetUserMakeBean um=new GetUserMakeBean();
					um.setMakeInfoID(tempJSONObject.getInt("MakeInfoID"));
					um.setVenuesID(tempJSONObject.getInt("VenuesID"));
					um.setProductID(tempJSONObject.getInt("ProductID"));
					um.setState(tempJSONObject.getString("State"));
					um.setUserID(tempJSONObject.getInt("UserID"));
					um.setMakeUserName(tempJSONObject.getString("MakeUserName")); 
					um.setMakeTime(dateParse(tempJSONObject.get("MakeTime").toString()));//����ԤԼʱ��	 
					um.setMakeCode(tempJSONObject.getString("MakeCode"));
					um.setBankCode(tempJSONObject.getString("BankCode"));
					um.setOpenBand(tempJSONObject.getString("OpenBank")); 
					um.setBankUserName(tempJSONObject.getString("BankUserName"));
					um.setPayType(tempJSONObject.getString("PayType")); 
					um.setProjectName(tempJSONObject.getString("ProjectName"));
					data.add(um);
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
	
}
