package com.findgasstation.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.findgasstation.app.R;
import com.findgasstation.app.bean.GasStation;
import com.findgasstation.app.bean.Petrol;
import com.findgasstation.app.util.Config;
import com.findgasstation.app.util.GasStationData;

public class MainActivity extends Activity implements OnClickListener {
	
	private Context mContext = null;
	private MapView mMapView = null;
	private BaiduMap mBaiduMap = null;
	private LocationClient mLocationClient = null;
	private BDLocation loc = null;
	private BDLocationListener mListener = new MyLocationListener();

	private ImageView iv_list, iv_loc;
	private Toast mToast;
	private TextView tv_title_right, tv_name, tv_distance, tv_price_a, tv_price_b;
	private LinearLayout ll_summary;

	private Dialog selectDialog, loadingDialog;
	
	private GasStation mGasStation = null;
	private Marker lastMarker = null;
	
	private ArrayList<GasStation> gasStationList = null;
	private GasStationData mGasStationData = null;
	
	//初始的定位公里数，暂定为3公里
	private int mDistance = 3000;
	
	
	/**
	 * *****************************************************************************
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mContext = MainActivity.this;
		mGasStationData = new GasStationData(mContext , mHandler);
		initView();
	}
	
	/**
	 * *****************************************************************************
	 */
	
	private void initView() {
		iv_list = (ImageView) findViewById(R.id.iv_list);
		iv_list.setOnClickListener(this);
		iv_loc = (ImageView) findViewById(R.id.iv_loc);
		iv_loc.setOnClickListener(this);
		tv_title_right = (TextView) findViewById(R.id.tv_title_button);
		tv_title_right.setText(getResources().getString(R.string.kilometre_3) + " >");
		tv_title_right.setVisibility(View.VISIBLE);
		tv_title_right.setOnClickListener(this);

		ll_summary = (LinearLayout) findViewById(R.id.ll_summary);
		ll_summary.setOnClickListener(this);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_distance = (TextView) findViewById(R.id.tv_distance);
		tv_price_a = (TextView) findViewById(R.id.tv_price_a);
		tv_price_b = (TextView) findViewById(R.id.tv_price_b);

		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.showScaleControl(false);
		mMapView.showZoomControls(false);
		mBaiduMap = mMapView.getMap();

		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null));
		mBaiduMap.setMyLocationEnabled(true);

		mLocationClient = new LocationClient(mContext);
		mLocationClient.registerLocationListener(mListener);

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Battery_Saving);// 模拟器测试用高精度;
															// 手机真机测试用Battery_Saving:低精度.
		option.setCoorType(Config.Baidu_LatLon); // 返回国测局经纬度坐标系：gcj02 返回百度墨卡托坐标系 ：bd09
										         // 返回百度经纬度坐标系 ：bd09ll
		option.setScanSpan(0);// 设置扫描间隔，单位毫秒，当<1000(1s)时，定时定位无效
		option.setIsNeedAddress(true);// 设置是否需要地址信息，默认为无地址
		option.setNeedDeviceDirect(true);// 在网络定位时，是否需要设备方向
		mLocationClient.setLocOption(option);
	}
	
	
	/*******************************************************************************
	 * 自定义方法
	 */
	public void setMarker(ArrayList<GasStation> gasStationList) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.marker , null);
		final TextView tv = (TextView) view.findViewById(R.id.tv_marker);
		int gasStationListSize = gasStationList.size();
		
		for(int i = 0 ; i < gasStationListSize ; i++) {
			GasStation gasStation = gasStationList.get(i);
			tv.setText((i + 1) + "");
			
			if(i == 0) {
				tv.setBackgroundResource(R.drawable.icon_focus_mark); //设置成蓝色的图标
			}
			else {
				tv.setBackgroundResource(R.drawable.icon_mark); //设置成红色的图标
			}
			BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(tv);
			LatLng latLng = new LatLng(gasStation.getLat() , gasStation.getLon());
			
			Bundle b = new Bundle();
			b.putParcelable(Config.KEY_GasStation, gasStationList.get(i));
			
			OverlayOptions oo = new MarkerOptions().position(latLng).icon(bitmap).title((i + 1) + "").extraInfo(b);
			
			if(i == 0) {
				lastMarker = (Marker) mBaiduMap.addOverlay(oo);
				mGasStation = gasStation;
				showLayoutInfo((i + 1) + "" , mGasStation);
			}
			else {
				mBaiduMap.addOverlay(oo);
			}
		}
		
		//给baidumap添加一个点击事件
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				if(lastMarker != null) {
					tv.setText(lastMarker.getTitle());
					tv.setBackgroundResource(R.drawable.icon_mark);
					BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(tv);
					lastMarker.setIcon(bitmap);
				}
				lastMarker = marker;
				String position = marker.getTitle();
				tv.setText(position);
				tv.setBackgroundResource(R.drawable.icon_focus_mark);
				BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(tv);
				marker.setIcon(bitmap);
				
				mGasStation = marker.getExtraInfo().getParcelable(Config.KEY_GasStation);
				showLayoutInfo(position, mGasStation);
				
				return false;
			}
		});
		
		
	}
	
	
	
	/*******************************************************************************
	 * Handler处理异步消息
	 */
	Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Config.RESULT_SUCCESS_CODE:
				gasStationList = (ArrayList<GasStation>) msg.obj;
				setMarker(gasStationList);
				loadingDialog.dismiss();
				break;
			case Config.RESULT_FAIL_CODE:
				loadingDialog.dismiss();
				showToast(getResources().getString(R.string.error_code_text) + msg.obj.toString());
				break;
			}
		}
	};
	
	
	
	
	/*******************************************************************************
	 * 自定义一个方法，来调用
	 */
	public void searchGasStation(double lat , double lon , int distance) {
		showLoadingDialog();
		//清除之前地图加载过的
		mBaiduMap.clear();
		ll_summary.setVisibility(View.GONE);
		mGasStationData.getGasStationData(mContext, lat, lon, distance);
	}
	
	
	
	
	
	/*******************************************************************************
	 * 显示加载信息
	 */
	public void showLayoutInfo(String position , GasStation gasStation) {
		tv_name.setText(position + "." + gasStation.getName());
		tv_distance.setText(gasStation.getDistance() + "");
		
		List<Petrol> list = gasStation.getGastPriceList();
		
		if(list != null && list.size() > 0) {
			tv_price_a.setText(list.get(0).getType() + " " + list.get(0).getPrice());
			if(list.size() > 1) {
				tv_price_b.setText(list.get(1).getType() + " " + list.get(1).getPrice());
			}
		}

		ll_summary.setVisibility(View.VISIBLE);
	}
	
	/*******************************************************************************
	 * 
	 */
	
	
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				return;
			}
			loc = location;
			MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius()).direction(location.getDirection()).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			searchGasStation(location.getLatitude() , location.getLongitude() , mDistance);
		}

	}

	/*******************************************************************************
	 * dialog点击事件
	 * 参数v是指点击的view
	 */
	public void onDialogClick(View v) {
		switch (v.getId()) {
		case R.id.bt_3km:
			distanceSearch(getResources().getString(R.string.kilometre_3) + " >", 3000);
			break;
		case R.id.bt_5km:
			distanceSearch(getResources().getString(R.string.kilometre_5) + " >", 5000);
			break;
		case R.id.bt_8km:
			distanceSearch(getResources().getString(R.string.kilometre_8) + " >", 8000);
			break;
		case R.id.bt_10km:
			distanceSearch(getResources().getString(R.string.kilometre_10) + " >", 10000);
			break;
		default:
			break;
		}
	}
	
	/*******************************************************************************
	 * 配合上面的onDialogClick()方法，统一进行选择
	 */
	public void distanceSearch(String text , int distance) {
		mDistance = distance;
		tv_title_right.setText(text);
		searchGasStation(loc.getLatitude() , loc.getLongitude() , distance);
		selectDialog.dismiss();
	}

	/*******************************************************************************
	 * 
	 */
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_list:
			Intent listIntent = new Intent(mContext , StationListActivity.class);
			listIntent.putParcelableArrayListExtra(Config.KEY_GasStationList , gasStationList);
			listIntent.putExtra(Config.KEY_LocLat , loc.getLatitude());
			listIntent.putExtra(Config.KEY_LocLon , loc.getLongitude());
			startActivity(listIntent);
			break;
		case R.id.iv_loc:
			int r = mLocationClient.requestLocation();
			switch (r) {
			case 1:
				showToast(getResources().getString(R.string.server_not_work));
				break;
			case 2:
				showToast(getResources().getString(R.string.has_no_listen_param));
				break;
			case 6:
				showToast(getResources().getString(R.string.request_time_too_short));
				break;
			default:
				break;
			}
			break;
		case R.id.tv_title_button:
			showSelectDialog();
			break;
		case R.id.ll_summary:
			Intent infoIntent = new Intent(mContext , StationInfoActivity.class);
			infoIntent.putExtra(Config.KEY_GasStation , mGasStation);
			infoIntent.putExtra(Config.KEY_LocLat , loc.getLatitude());
			infoIntent.putExtra(Config.KEY_LocLon , loc.getLongitude());
			startActivity(infoIntent);
			break;
		default:
			break;
		}

	}

	
	/*******************************************************************************
	 * 显示范围选择dialog
	 */
	private void showSelectDialog() {
		if (selectDialog != null) {
			selectDialog.show();
			return;
		}
		selectDialog = new Dialog(mContext, R.style.dialog);
		View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_distance, null);
		selectDialog.setContentView(view, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		selectDialog.setCanceledOnTouchOutside(true);
		selectDialog.show();
	}

	/*******************************************************************************
	 * 
	 */
	
	private void showLoadingDialog() {
		if (loadingDialog != null) {
			loadingDialog.show();
			return;
		}
		loadingDialog = new Dialog(mContext, R.style.dialog_loading);
		View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_loading, null);
		loadingDialog.setContentView(view, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		loadingDialog.setCancelable(false);
		loadingDialog.show();
	}
	

	/*******************************************************************************
	 * 显示通知
	 */
	
	private void showToast(String msg) {
		if (mToast == null) {
			mToast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
		}
		mToast.setText(msg);
		mToast.show();
	}
	
	/*******************************************************************************
	 * 
	 */

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
		mLocationClient.start();
	}
	
	/*******************************************************************************
	 * 
	 */

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
		mLocationClient.stop();
	}
	
	/*******************************************************************************
	 * 
	 */

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
		mHandler = null;
	}

}
