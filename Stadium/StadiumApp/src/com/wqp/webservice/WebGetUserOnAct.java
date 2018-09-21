package com.wqp.webservice;
 
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
import org.json.JSONException;
import org.json.JSONObject; 
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.text.TextUtils;
import android.util.Log;

import com.wqp.stadiumapp.utils.WebServiceUtils; 

public class WebGetUserOnAct {
	
	private static final String TAG = "WebGetUserOnAct";

	/** ��Web�˻�ȡ�û��Ѿ��μӻID����Ϣ
	 * @param str �������Ҫ��д��д""����,�������дSQL���
	 *@return <returns>����DataSet ���ػ��Ϣ Json��ʽ</returns>;��web�˽��������ݷ�װ��JavaBean�Լ��ϵ���ʽ���� 
	 */
	public static List<Integer> getGetUserOnActData(int userid){
		//�����ռ�+��������
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.GETUSERONACT;
		
		/**����HttpTransportSE����,����������˵�url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**ʵ����SoapObject����,���������Բ���,����1�������ռ�,����2����������*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.GETUSERONACT);
		soapObject.addProperty("UserID",userid);//����web�˷����Ĳ���
		
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
				String result=soap.getProperty("GetUserOnActResult").toString();
				Log.i(TAG,"���ݼ�:"+result);
				return ParseGetUserOnActInfo(result); 
			} 
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/** ���������JSON��ʽ�ַ������н�����Java����,����װ��List���Ͻ��з���*/
	public static List<Integer> ParseGetUserOnActInfo(String result) throws Exception{   
		try {
			JSONObject jsonObject=new JSONObject(result); 
			String res=jsonObject.getString("UserOnAct");
			 return dateParse(res);
		} catch (JSONException e) { 
			e.printStackTrace();
		}
		return null;
	}
	
	/** ����web�˵������ַ���,��ת���������� */
	private static List<Integer> dateParse(String result) {
		List<Integer> data=null;
		if(!TextUtils.isEmpty(result)){
			data=new ArrayList<Integer>();
			String regEx="[0-9\\.]+";
			Pattern p=Pattern.compile(regEx);
			Matcher m=p.matcher(result); 
			while(m.find()){
				System.out.println(TAG+",number="+Integer.valueOf(m.group()));
				data.add(Integer.valueOf(m.group()));  
			} 
		} 
		return data;
	}
	
}
