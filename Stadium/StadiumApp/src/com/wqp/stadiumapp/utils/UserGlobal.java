package com.wqp.stadiumapp.utils;

import android.content.Intent;

/** ���������ڱ�ʶ������Ӧ�õ����û�������¼�ɹ�֮��Ĺؼ��ֶ���Ϣ*/
public class UserGlobal {
	
	/** ������û��Ƿ����
	 * Ĭ����false ,����û���¼�ɹ�֮����ֶξͻ���true;
	 * ���ڱ�ʶ������Ӧ�����û��Ƿ�������¼�ɹ�
	 */
	public static boolean UserExist=false;
	
	/** 
	 * ��ʶ�û�������¼�ɹ�֮��ӷ������˷��ص��û�ID���,Ĭ��Ϊ-1,���û�������¼֮��ͻ����Ϊ�û���ID��	
	 */
	public static int UserID=-1;
	
	/**
	 * ��ʶ�û������ɹ���¼֮����ҵ����ǳ���Ϣ�����ֶ�ֻ�����û�������¼֮���Ż����
	 */
	public static String NickName=null;
	
	/**
	 * ��ʶ�û������ɹ���¼֮����ҵ����û�������Ϣ�����ֶ�ֻ�����û�������¼֮���Ż����
	 */
	public static String Name=null;
	

	/**
	 * ��ʶ�û������ɹ���¼֮����ҵ����û�����Ϣ�����ֶ�ֻ�����û�������¼֮���Ż����
	 */
	public static String UserName=null;
	
	/**
	 * ��ʶ�û�������¼֮����ҵ����Ա���Ϣ�����ֶ�ֻ�����û�������¼֮���Ż����
	 */
	public static String sex=null;
	

	/**
	 * ��ʶ�û�������¼֮����ҵ����������Ϣ�����ֶ�ֻ�����û�������¼֮���Ż����
	 */
	public static String Age=null;
	
	/**
	 * ��ʶ�û�������¼֮����ҵ���ͷ��ͼƬ·����Ϣ�����ֶ�ֻ�����û�������¼֮���Ż����
	 */
	public static String Picture=null;
	
	
	/**
	 * ����Ӧ�ö�ͨ�õ�Ȩ��,�����û���¼����,���ֶ����ڵ���ʹ��
	 */
	public static boolean SYSTEM=false;
	
	/**
	 *��ͨ�û���¼ʹ�õ��ļ���������ͼ,service����ʹ��
	 */ 
	public static Intent user_intent=new Intent();
	
	/**
	 *�����û���¼ʹ�õ��ļ���������ͼ,servie����ʹ��
	 */ 
	public static Intent admin_intent=new Intent();
	
}
