package com.wqp.webservice;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.wqp.stadiumapp.utils.WebServiceUtils;

/**
 * ��÷���˻ظ��û��Ĳ�����Ϣ 
 *
 */
public class WebGet5secondV {
	/**
	 * ������ͨ�û�ID��ѯ������Ƿ�����Ϣ�ظ�
	 * @param str {"UserID":"34"} Json��ʽ����
	 * @return ��ѯ������Ϣ
	 */
	public static String getGet5secondV(String str){
		//�����ռ�+��������
				String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.GET5SECONDV;
				
				/**����HttpTransportSE����,����������˵�url*/
				HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
				
				/**ʵ����SoapObject����,���������Բ���,����1�������ռ�,����2����������*/
				SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.GET5SECONDV);
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
						String result=soap.getProperty("Get5secondVResult").toString();
						return ParseGet5SecondV(result); 
					} 
				}catch(Exception e){
					e.printStackTrace();
				} 
				return null;
	}
	
	/**
	 * ���ݷ���˷��ص����ݽ��н�������
	 * @param result ����
	 * @return �����Ľ��
	 */
	private static String ParseGet5SecondV(String result) {
		StringBuffer sb = null;
		if(result!=null){
			try {
				sb=new StringBuffer();
				JSONObject object=new JSONObject(result); 
				sb.append(object.getString("MessageState")); 
			} catch (JSONException e) { 
				e.printStackTrace();
			}
		} 
		return sb.toString();
	}
	
	
}
