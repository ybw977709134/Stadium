package com.wqp.webservice; 
 
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE; 
 

import com.wqp.stadiumapp.utils.WebServiceUtils; 

/**ͨ���÷�������ȡ��ԤԼ����*/
public class WebRemoveMake {  
	/**
	 * ȡ��ԤԼ����
	 * @param { MakeID:1, BankCode:'6222520617777777', OpenBank:'�й���������', BankUserName:'����' } 
	 * @return �˶��ɹ�����true,ʧ�ܷ���false
	 */
	public static boolean getRemoveMakeData(String str){
		//�����ռ�+��������
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.REMOVEMAKE;
		
		/**����HttpTransportSE����,����������˵�url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**ʵ����SoapObject����,���������Բ���,����1�������ռ�,����2����������*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.REMOVEMAKE);
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
				String result=soap.getProperty("RemoveMakeResult").toString();
				return ParseAddUser(result); 
			} 
		}catch(Exception e){
			e.printStackTrace();
		} 
		return false;
	}
	
	/** ����ȡ��ԤԼ֮��ķ���ֵ*/
	private static boolean ParseAddUser(String result){  
		try {
			JSONObject jsonObject=new JSONObject(result);
			boolean temp= jsonObject.getBoolean("RemoveMake"); 
			return temp;
		} catch (JSONException e) { 
			e.printStackTrace();
		}
		return false;  
	}
	
}
