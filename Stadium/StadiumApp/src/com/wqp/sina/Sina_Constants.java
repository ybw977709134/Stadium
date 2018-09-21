package com.wqp.sina;

public interface Sina_Constants {
	//应用的key appkey
	public static final String APP_KEY="3135302899";
	public static final String REDIRECT_URL = "http://weibo.com/wqpweb";
	//新支持scope:支持传入多个scope权限，用逗号分隔
	public static final String SCOPE="email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";
	
	public static final String CLIENT_ID="client_id";
	public static final String CLIENT_SERCRET="";
	public static final String RESPONSE_TYPE="response_type";
	public static final String USER_REDIRECT_URL="redirect_url";
	public static final String DISPLAY="display";
	public static final String USER_SCOPE="scope";
	public static final String PACKAGE_NAME="package_name";
	public static final String KEY_HASH="key_hash"; 
	
}
