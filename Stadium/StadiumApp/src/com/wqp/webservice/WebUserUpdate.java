package com.wqp.webservice;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.wqp.stadiumapp.utils.WebServiceUtils;

/** ͨ���÷������Ի�ȡ��Web�˷�����Ϊ:UserUpdate���ص�Json��ʽ���ַ���*/
public class WebUserUpdate {
	/**
	 * �������ƽ��е�½����
	 * @param name
	 */
	public static boolean getUserUpdateData(String str){
		//�����ռ�+��������
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.USERUPDATE;
		
		/**����HttpTransportSE����,����������˵�url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**ʵ����SoapObject����,���������Բ���,����1�������ռ�,����2����������*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.USERUPDATE);
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
				String result=soap.getProperty("UserUpdateResult").toString();
				return ParseUserUpdate(result);
//				Log.i("WEB_SERVICE,���º�Ľ����:",result);
			} 
		}catch(Exception e){
			e.printStackTrace();
		} 
		return false;
	}
	
	/** ����������ַ������н������صĽ��*/
	public static boolean ParseUserUpdate(String result){
		if(result!=null){
			try {
				JSONObject object=new JSONObject(result);
				return object.getBoolean("UserUpdate"); 
			} catch (JSONException e) { 
				e.printStackTrace();
			} 
		}
		return false;
	}
}
