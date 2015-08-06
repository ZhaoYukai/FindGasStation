package com.findgasstation.app.util;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.findgasstation.app.R;
import com.findgasstation.app.bean.GasStation;
import com.findgasstation.app.bean.Petrol;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

public class GasStationData {
	
	private Context mContext = null;
	private Handler mHandler = null;
	
	/**
	 * **************************************************************************
	 */
	
	public GasStationData(Context context , Handler handler) {
		mContext = context;
		mHandler = handler;
	}
	
	/**
	 * **************************************************************************
	 */
	
	public void getGasStationData(final Context context , double lat , double lon , int distance) {
		
		//用聚合SDK提供的Parameters类封装参数
		Parameters params = new Parameters();
		params.add(Config.KEY_Lat, lat);
		params.add(Config.KEY_Lon, lon);
		params.add(Config.KEY_R, distance);
		
		/**
		 * 请求的方法 
		 * 参数: 
		 * 第一个参数 当前请求的context 
		 * 第二个参数 接口id 
		 * 第三个参数 接口请求的url 
		 * 第四个参数 接口请求的方式
		 * 第五个参数 接口请求的参数,键值对com.thinkland.sdk.android.Parameters类型; 
		 * 第六个参数 请求的回调方法,com.thinkland.sdk.android.DataCallBack;
		 */
		
		JuheData.executeWithAPI(
				context , 
				Config.KEY_ID, //官网给出的id就是7
				Config.API_URL, //官网给出的接口地址
				JuheData.GET, 
				params, 
				new DataCallBack() {
					/**
					 * 请求成功时调用的方法 statusCode为http状态码,responseString为请求返回数据.
					 */
					@Override
					public void onSuccess(int statusCode, String responseString) {
						if(statusCode == Config.STATUS_CODE_SUCCESS) {
							ArrayList<GasStation> list = parseJSON(responseString);
							
							if(list != null && mHandler != null) {
								Message msg = Message.obtain(mHandler, Config.RESULT_SUCCESS_CODE, list);
								msg.sendToTarget();
							}
							
						}
						else {
							Message msg = Message.obtain(mHandler, Config.RESULT_FAIL_CODE, statusCode);
							msg.sendToTarget();
						}
					}
					/**
					 * 请求完成时调用的方法,无论成功或者失败都会调用.
					 */
					@Override
					public void onFinish() {
						//暂时为空
					}
					/**
					 * 请求失败时调用的方法,statusCode为http状态码,throwable为捕获到的异常
					 * statusCode:
					 * 30002 没有检测到当前网络
					 * 30003 没有进行初始化. 
					 * 0未明异常,具体查看Throwable信息
					 * 其他异常参照http状态码.
					 */
					@Override
					public void onFailure(int statusCode, String responseString, Throwable throwable) {
						Message msg = Message.obtain(mHandler, Config.RESULT_FAIL_CODE, statusCode + ":" + throwable.getMessage());
						msg.sendToTarget();
					}
				});
			

	}
	
	/***********************************************************************************
	 * 自定义一个方法，用于解析JSON字符串
	 */
	
	@SuppressWarnings("unchecked")
	private ArrayList<GasStation> parseJSON(String responseString) {
		
		ArrayList<GasStation> gasStationList = null;
		
		try {
			JSONObject jsonObj = new JSONObject(responseString);
			int code = jsonObj.getInt(Config.JSON_error_code); //根据服务器的对应key返回对应的value
			
			if(code == 0) {
				//如果代码是0，表示服务器返回数据是成功的
				gasStationList = new ArrayList<GasStation>();
				JSONArray jsonArr = jsonObj.getJSONObject(Config.JSON_result).getJSONArray(Config.JSON_data);
				int arrLength = jsonArr.length();
				for(int i = 0 ; i < arrLength ; i++) {
					JSONObject dataElement = jsonArr.getJSONObject(i);
					
					GasStation gasStation = new GasStation();
					gasStation.setName( dataElement.getString(Config.JSON_name) );
					gasStation.setAddress( dataElement.getString(Config.JSON_address) );
					gasStation.setArea( dataElement.getString(Config.JSON_areaname) );
					gasStation.setBrand( dataElement.getString(Config.JSON_brandname) );
					gasStation.setLat( dataElement.getDouble(Config.JSON_lat) );
					gasStation.setLon( dataElement.getDouble(Config.JSON_lon) );
					gasStation.setDistance( dataElement.getInt(Config.JSON_distance) );
					
					//----------------------------------------------------------------
					
					JSONObject priceJSON = dataElement.getJSONObject(Config.JSON_price);
					ArrayList<Petrol> priceList = new ArrayList<Petrol>();
					Iterator<String> priceIterator = priceJSON.keys();
					while(priceIterator.hasNext()) {
						Petrol petrol = new Petrol();
						String key = priceIterator.next();
						String value = priceJSON.getString(key);
						petrol.setType(key.replace("E", "") + "#");
						petrol.setPrice(value + mContext.getResources().getString(R.string.yuan_per_l));
						priceList.add(petrol);
					}
					gasStation.setPriceList(priceList);
					
					//----------------------------------------------------------------
					
					JSONObject gastPriceJSON = dataElement.getJSONObject(Config.JSON_gastprice);
					ArrayList<Petrol> gastPriceList = new ArrayList<Petrol>();
					Iterator<String> gastPriceIterator = gastPriceJSON.keys();
					while(gastPriceIterator.hasNext()) {
						Petrol petrol = new Petrol();
						String key = gastPriceIterator.next();
						String value = gastPriceJSON.getString(key);
						petrol.setType(key);
						petrol.setPrice(value + mContext.getResources().getString(R.string.yuan_per_l));
						gastPriceList.add(petrol);
					}
					gasStation.setGastPriceList(gastPriceList);
					
					//一次for循环结束，把gasStation添加到gasStationList中
					gasStationList.add(gasStation);
					
					
				}//for循环结束
			}//if结束
			else {
				Message msg = Message.obtain(mHandler, Config.RESULT_FAIL_CODE, code);
				msg.sendToTarget();
			}
			
		}//try块结束
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return gasStationList;

	} //parseJSON()自定义方法结束
	
	
}
