package com.wqp.webservice;
 
 
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE; 

import android.text.TextUtils;

import com.wqp.stadiumapp.utils.WebServiceUtils; 

/** ͨ���÷������������ԤԼ����*/
public class WebAddMake {  
	/**
	 * �����ԤԼ����
	 * @param { VenuesID:8, ProductID:1, UserID:1, MakeUserName:'����', MakeTime:'2015-2-19 13:30:00', PayType:'֧����/΢��'}
	 * @return Ԥ���ɹ�������֤�� { AddMake:'678750' },Ԥ��ʧ�ܷ��ؿ��ַ��� 
	 */
	public static String getAddMakeData(String str){
		//�����ռ�+��������
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.ADDMAKE;
		
		/**����HttpTransportSE����,����������˵�url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**ʵ����SoapObject����,���������Բ���,����1�������ռ�,����2����������*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.ADDMAKE);
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
				String result=soap.getProperty("AddMakeResult").toString();
				return ParseAddUser(result); 
			} 
		}catch(Exception e){
			e.printStackTrace();
		} 
		return null;
	}
	
	/** ����ע��֮��ķ���ֵΪ��֤�� */
	private static String ParseAddUser(String result){  
		try {
			JSONObject jsonObject=new JSONObject(result);
			String temp= jsonObject.getString("AddMake");
			if(!TextUtils.isEmpty(temp)){
				return temp;
			}
		} catch (JSONException e) { 
			e.printStackTrace();
		} 
		return null;
	}
	
}
