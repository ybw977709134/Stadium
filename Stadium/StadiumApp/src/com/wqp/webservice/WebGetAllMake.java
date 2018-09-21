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
import com.wqp.webservice.entity.GetAllMakeBean; 

/** 
 *ͨ���÷������볡��ID��Ų�ѯ����ԤԼ����
 */
public class WebGetAllMake {
	private static final String TAG = "WebGetAllMake";

	/**
	 * ���볡��ID��ȡ��������ϸ��Ϣ
	 * @param name
	 * @see ���磺str={ "VenuesID": "8"}
	 */
	public static List<GetAllMakeBean> getGetGetAllMakeData(String str){
		//�����ռ�+��������
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.GETALLMAKE;
		
		/**����HttpTransportSE����,����������˵�url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**ʵ����SoapObject����,���������Բ���,����1�������ռ�,����2����������*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.GETALLMAKE);
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
				String result=soap.getProperty("GetAllMakeResult").toString();
				return ParseGetAUser(result); 
			} 
		}catch(Exception e){
			e.printStackTrace();
		} 
		return null;
	}
	
	/** ���������JSON��ʽ�ַ������н�����Java����,����װ��List���Ͻ��з���*/
	public static List<GetAllMakeBean> ParseGetAUser(String result){ 
		List<GetAllMakeBean> data = null;
		try {
			JSONObject jsonObject=new JSONObject(result);
			JSONArray jsonArray=jsonObject.getJSONArray("ds");
			int len=jsonArray.length();
			Log.i(TAG,"����Ϊ="+len);
			if(len!=0){
				data=new ArrayList<GetAllMakeBean>();
				for(int i=0;i<len;i++){
					JSONObject tempJSONObject=new JSONObject(jsonArray.getString(i));
					GetAllMakeBean amb=new GetAllMakeBean();
					amb.setProjectID(tempJSONObject.getInt("ProjectID"));
					amb.setVenuesID(tempJSONObject.getInt("VenuesID"));
					amb.setProjectName(tempJSONObject.getString("ProjectName"));
					amb.setMakePrice(tempJSONObject.getString("MakePrice"));
					amb.setMakeDes(tempJSONObject.getString("MakeDes")); 
					data.add(amb);
				}
			}  
		} catch (JSONException e) { 
			e.printStackTrace();
		}
		return data!=null ? data : null;
	}  
	
}
