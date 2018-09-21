package com.wqp.webservice;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

import com.wqp.stadiumapp.utils.WebServiceUtils;

/** ͨ���÷������Ի�ȡ��Web�˷�����Ϊ:Upload���ص�Json��ʽ���ַ���*/
public class WebUpload { 
	/**
	 * �������ݵ���������
	 * @param fs
	 * @param fileType
	 * @param UserID
	 */
	public static void getUploadData(byte[] fs, String fileType, String UserID){
		//�����ռ�+��������
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.UPLOAD;
		
		/**����HttpTransportSE����,����������˵�url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**ʵ����SoapObject����,���������Բ���,����1�������ռ�,����2����������*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.UPLOAD);
		soapObject.addProperty("fs",fs);//����web�˷����Ĳ���1
		soapObject.addProperty("fileType",fileType);//����web�˷����Ĳ���2
		soapObject.addProperty("UserID",UserID);//����web�˷����Ĳ���3
		
		/**ʹ��SOAPЭ�������л���Envelope
		 * ��SoapObject��������ΪSoapSerializationEnvelope����Ĵ���SOAP��Ϣ
		 * ������SoapEnvelope�汾��
		 */
		SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(SoapEnvelope.VER10);
		envelope.bodyOut=soapObject;
		try{
			/**��ʼ����Զ�̷���,����1:�����ռ�+��������,����2��ʵ�������Envelope*/
			httpse.call(soapAction, envelope);
			if(envelope.getResponse()!=null){
				//��ȡ��������Ӧ���ص�SOAP��Ϣ
				SoapObject soap=(SoapObject) envelope.bodyIn;
				/**��ȡ���ص�����,����������˷��صķ�����(�����ڷ���˲���)*/
				String result_one=soap.getProperty("UploadResult").toString(); 
				@SuppressWarnings("unused")
				byte[] result_two=(byte[]) soap.getProperty("fs");
				Log.i("WEB_SERVICE",result_one.toString());
			} 
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
}
