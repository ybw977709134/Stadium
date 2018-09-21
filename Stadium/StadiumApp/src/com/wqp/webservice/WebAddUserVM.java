package com.wqp.webservice;
 
 
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.text.TextUtils;
import android.util.Log;

import com.wqp.stadiumapp.utils.WebServiceUtils;

/** ͨ���÷������������ԤԼ����*/
public class WebAddUserVM {  
	/**
	 * ����û�ѡ��ĳ�����Ϣ���ύ����������
	 * @param { ProName:���ֵ�, MakeTime:2015-03-12 08:00:00, UserID:1, VenuesIDs:8} 
	 * @return �����ӳɹ��ͷ���true,����false
	 */
	public static boolean getAddUserVMData(String str){
		//�����ռ�+��������
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.ADDUSERVM;
		
		/**����HttpTransportSE����,����������˵�url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**ʵ����SoapObject����,���������Բ���,����1�������ռ�,����2����������*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.ADDUSERVM);
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
				String result=soap.getProperty("AddUserVMResult").toString();
				return ParseAddUserVM(result); 
//				Log.i("WebAddUserVM","��ýӿ�������ݷ��صĽ����:"+result);
			} 
		}catch(Exception e){
			e.printStackTrace();
		} 
		return false;
	}
	
	/** ����ע��֮��ķ���ֵΪ��֤�� */
	private static boolean ParseAddUserVM(String result){
		boolean temp=false;
		try {
			JSONObject jsonObject=new JSONObject(result);
			temp= jsonObject.getBoolean("AddUserVM"); 
		} catch (JSONException e) { 
			e.printStackTrace();
		} 
		return temp;
	}
	
}
