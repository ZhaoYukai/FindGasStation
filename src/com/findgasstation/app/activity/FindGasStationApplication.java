package com.findgasstation.app.activity;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.thinkland.sdk.android.JuheSDKInitializer;

public class FindGasStationApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		
		SDKInitializer.initialize(getApplicationContext());
		JuheSDKInitializer.initialize(getApplicationContext());
	}
}
