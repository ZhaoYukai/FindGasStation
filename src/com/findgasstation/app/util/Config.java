package com.findgasstation.app.util;

public class Config {
	
	//国测局经纬度坐标系
	public static final String NationCal_LatLon = "gcj02";
	//百度墨卡托坐标系
	public static final String BaiduMokatuo_LatLon = "bd09";
	//百度经纬度坐标系
	public static final String Baidu_LatLon = "bd09ll";
	
	public static final String KEY_GasStation = "GasStation";
	public static final String KEY_GasStationList = "GasStationList";
	public static final String KEY_LocLat = "locLat";
	public static final String KEY_LocLon = "locLon";
	public static final String KEY_Lat = "lat";
	public static final String KEY_Lon = "lon";
	public static final String KEY_R = "r";
	
	public static final int KEY_ID = 7;
	public static final String API_URL = "http://apis.juhe.cn/oil/local";
	
	public static final String JSON_error_code = "error_code";
	public static final String JSON_result = "result";
	public static final String JSON_data = "data";
	public static final String JSON_name = "name";
	public static final String JSON_address = "address";
	public static final String JSON_areaname = "areaname";
	public static final String JSON_brandname = "brandname";
	public static final String JSON_lat = "lat";
	public static final String JSON_lon = "lon";
	public static final String JSON_distance = "distance";
	public static final String JSON_price = "price";
	public static final String JSON_gastprice = "gastprice";
	
	
	public static final int RESULT_SUCCESS_CODE = 0x01;
	public static final int RESULT_FAIL_CODE = 0x02;
	
	public static final int STATUS_CODE_SUCCESS = 200;
	public static final int STATUS_CODE_NO_NETWORK = 30002;
	public static final int STATUS_CODE_NO_INIT = 30003;
	
}
