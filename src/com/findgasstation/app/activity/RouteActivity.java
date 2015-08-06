package com.findgasstation.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.findgasstation.app.R;
import com.findgasstation.app.util.Config;

public class RouteActivity extends Activity {
	
	private Context mContext = null;
	private MapView mMapView = null;
	private BaiduMap mBaiduMap = null;
	private ImageView iv_back = null;
	
	private RoutePlanSearch mRoutePlanSearch = null;
	
	
	/**
	 * ****************************************************************************
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route);
		
		mContext = RouteActivity.this;
		initView();
	}
	
	/**
	 * ****************************************************************************
	 */

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setVisibility(View.VISIBLE);
		
		iv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.showScaleControl(false);
		mMapView.showZoomControls(false);
		mBaiduMap = mMapView.getMap();
		
		
		// 初始化搜索模块，注册事件监听
		mRoutePlanSearch = RoutePlanSearch.newInstance();
		mRoutePlanSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
			@Override
			public void onGetWalkingRouteResult(WalkingRouteResult result) {
				/**
				 * 步行的路径
				 */
			}
			
			@Override
			public void onGetTransitRouteResult(TransitRouteResult result) {
				/**
				 * 公交的路径
				 */
			}
			
			@Override
			public void onGetDrivingRouteResult(DrivingRouteResult result) {
				/**
				 * 行车的路径
				 */
				if(result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
					Toast.makeText(mContext , getResources().getString(R.string.have_not_find_result) , Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
					Toast.makeText(mContext , getResources().getString(R.string.start_and_end_has_different) , Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(result.error == SearchResult.ERRORNO.NO_ERROR) {
					DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
					overlay.setData(result.getRouteLines().get(0));
					overlay.addToMap();
					overlay.zoomToSpan();
				}
				
				
			}
		});
		
		
		Intent intent = getIntent();
		
		LatLng locLatlng = new LatLng(
				intent.getDoubleExtra(Config.KEY_LocLat, 0), 
				intent.getDoubleExtra(Config.KEY_LocLon, 0));
		
		LatLng destLatlng = new LatLng(
				intent.getDoubleExtra(Config.KEY_Lat, 0), 
				intent.getDoubleExtra(Config.KEY_Lon, 0));
		
		PlanNode st = PlanNode.withLocation(locLatlng);
		PlanNode en = PlanNode.withLocation(destLatlng);
		
		mRoutePlanSearch.drivingSearch(new DrivingRoutePlanOption().from(st).to(en));
	}
	
	/**
	 * ****************************************************************************
	 */

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}
	
	/**
	 * ****************************************************************************
	 */

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}
	
	/**
	 * ****************************************************************************
	 */

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
	}

}
