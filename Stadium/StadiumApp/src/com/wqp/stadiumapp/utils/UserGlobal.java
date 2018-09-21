package com.wqp.stadiumapp.utils;

import android.content.Intent;

/** 该类是用于标识在整个应用当中用户正常登录成功之后的关键字段信息*/
public class UserGlobal {
	
	/** 保存该用户是否存在
	 * 默认是false ,如果用户登录成功之后该字段就会变成true;
	 * 用于标识在整个应用中用户是否正常登录成功
	 */
	public static boolean UserExist=false;
	
	/** 
	 * 标识用户正常登录成功之后从服务器端返回的用户ID编号,默认为-1,当用户正常登录之后就会更新为用户的ID号	
	 */
	public static int UserID=-1;
	
	/**
	 * 标识用户正常成功登录之后查找到的昵称信息，该字段只有在用户正常登录之生才会更新
	 */
	public static String NickName=null;
	
	/**
	 * 标识用户正常成功登录之后查找到的用户姓名信息，该字段只有在用户正常登录之生才会更新
	 */
	public static String Name=null;
	

	/**
	 * 标识用户正常成功登录之后查找到的用户名信息，该字段只有在用户正常登录之生才会更新
	 */
	public static String UserName=null;
	
	/**
	 * 标识用户正常登录之后查找到的性别信息，该字段只有在用户正常登录之生才会更新
	 */
	public static String sex=null;
	

	/**
	 * 标识用户正常登录之后查找到的年龄段信息，该字段只有在用户正常登录之生才会更新
	 */
	public static String Age=null;
	
	/**
	 * 标识用户正常登录之后查找到的头像图片路径信息，该字段只有在用户正常登录之生才会更新
	 */
	public static String Picture=null;
	
	
	/**
	 * 属于应用端通用的权限,用于用户登录设置,该字段用于调试使用
	 */
	public static boolean SYSTEM=false;
	
	/**
	 *普通用户登录使用到的监听服务意图,service当中使用
	 */ 
	public static Intent user_intent=new Intent();
	
	/**
	 *场馆用户登录使用到的监听服务意图,servie当中使用
	 */ 
	public static Intent admin_intent=new Intent();
	
}
