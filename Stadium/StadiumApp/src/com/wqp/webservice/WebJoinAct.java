package com.wqp.webservice;
 

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
 
import android.util.Log;

import com.wqp.stadiumapp.utils.WebServiceUtils;
import com.wqp.webservice.entity.JoinActResultBean;

/** ͨ���÷������Ի�ȡ��Web�˷�����Ϊ:JoinAct���ص�Json��ʽ���ַ���*/
public class WebJoinAct {
	/**
	 * �������ƽ��е�½����
	 * @param str <param name="str">ActID �ID ;UserName �û��� �����ö��Ÿ��� Json��ʽ</param>
	 * @return <returns>UserExist �û���½�Ƿ�ɹ� NoName �����޴���(UserExistΪTure��Ϊ0) Json��ʽ</returns>	
	 */
	public static JoinActResultBean getJoinActData(String str){
		//�����ռ�+��������
		String soapAction=WebServiceUtils.NAMESPACE+WebServiceUtils.JOINACT;
		
		/**����HttpTransportSE����,����������˵�url*/
		HttpTransportSE httpse=new HttpTransportSE(WebServiceUtils.SERVICEURL); 
		
		/**ʵ����SoapObject����,���������Բ���,����1�������ռ�,����2����������*/
		SoapObject soapObject=new SoapObject(WebServiceUtils.NAMESPACE, WebServiceUtils.JOINACT);
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
				String result=soap.getProperty("JoinActResult").toString();
				Log.i("WEB_SERVICE",result);
				return ParseJoinAct(result); 
			} 
		}catch(Exception e){
			e.printStackTrace();
		} 
		return null;//����ʧ��,û�л�ȡ������
	}
	
	/** �����ӷ���˷��ص�JoinAct����
	 * @throws JSONException */
	public static JoinActResultBean ParseJoinAct(String result){ 
		if(result!=null){
			try { 
				JSONObject object=new JSONObject(result);
				JoinActResultBean jrb;
//				Iterator<?> it=object.keys();
//				while(it.hasNext()){
//					String useerexist=it.next().toString();
//					String noname=it.next().toString();
//					String onname=it.next().toString();
//					if(!TextUtils.isEmpty(useerexist)){
//						object.put("UserExist", useerexist.trim());
//					}
//					if(!TextUtils.isEmpty(noname)){
//						object.put("NoName", noname); 
//					}
//					if(!TextUtils.isEmpty(onname)){
//						object.put("OnName", onname);
//					}
//				}
				Log.i("WebJoinAct","����Ӧս֮��ӷ���˷������ݽ����ٴδ���õ�������Ϊ:"+object.toString());
				jrb=new JoinActResultBean();
				jrb.setUserExist(object.getBoolean("UserExist"));//������boolean���ͣ�ȴ�����String����	
				jrb.setNoName(object.getString("NoName"));//������Int����ȴ�����String����
				jrb.setOnName(object.getString("OnName"));//������Int����ȴ�����String���� 
				return jrb;  
			} catch (JSONException e) { 
				e.printStackTrace();
			} 
		}
		return null; 
	}
	
}
