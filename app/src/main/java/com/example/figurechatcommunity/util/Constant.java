package com.example.figurechatcommunity.util;


public interface Constant {
	/*每页数量   pageSize*/
	public static final int PAGE_SIZE = 10;

	public static final String USER_NICK_CHANGE = "USER_NICK_CHANGE" ;  //昵称修改广播
	public static final String USER_SIGN_CHANGE = "USER_SIGN_CHANGE" ;  //签名修改广播
	public static final String USER_AVATER_CHANGE = "USER_AVATER_CHANGE" ;  //头像修改广播
	public static final String USER_FANSNUM_CHANGE = "USER_FANSNUM_CHANGE" ;  //粉丝数修改广播
	public static final String USER_FOCUSNUM_CHANGE = "USER_FOCUSNUM_CHANGE" ;  //关注数修改广播
	public static final String USER_PAOPAONUM_CHANGE = "USER_PAOPAONUM_CHANGE" ;  //泡泡数修改广播
	public static final String USER_LOCATION_CHANGE = "USER_LOCATION_CHANGE" ;  //位置修改广播
	public static final String USER_SEX_CHANGE = "USER_SEX_CHANGE" ;  //性别修改广播
	public static final String PREF_LATITUDE = "latitude";     // 维度
	public static final String PREF_LONGTITUDE = "longtitude"; // 经度
	public static final String FANS_NUM = "FANS_NUM";     // 粉丝数
	public static final String FOCUS_NUM = "FOCUS_NUM"; // 关注数
	public static final String PAOPAO_NUM = "PAOPAO_NUM"; // 泡泡数

	String BMOB_APP_ID = "";
	String TABLE_AI = "Mood";
	String TABLE_COMMENT = "Comment";
	
	String NETWORK_TYPE_WIFI = "wifi";
	String NETWORK_TYPE_MOBILE = "mobile";
	String NETWORK_TYPE_ERROR = "error";
	
	int AI = 0;
	int HEN = 1;
	int CHUN_LIAN = 2;
	int BIAN_BAI = 3;
	
	int CONTENT_TYPE = 4;
	
	String PRE_NAME = "my_pre";

	public static final int PUBLISH_COMMENT = 1;
	public static final int NUMBERS_PER_PAGE = 15;//每次请求返回评论条数
	public static final int SAVE_FAVOURITE = 2;
	public static final int GET_FAVOURITE = 3;
	public static final int GO_SETTINGS = 4;
	
	public static final String SEX_MALE = "male";
	public static final String SEX_FEMALE = "female";
}
