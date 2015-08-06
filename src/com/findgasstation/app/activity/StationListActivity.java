package com.findgasstation.app.activity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.findgasstation.app.R;
import com.findgasstation.app.adapter.StationListAdapter;
import com.findgasstation.app.bean.GasStation;
import com.findgasstation.app.util.Config;

public class StationListActivity extends Activity {

	private Context mContext;
	private ListView lv_station;
	private ImageView iv_back;
	
	/**
	 * ************************************************************************
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		mContext = this;
		initView();
	}
	
	/**
	 * ************************************************************************
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
		
		lv_station = (ListView) findViewById(R.id.lv_station);
		
		final List<GasStation> gasStationList = getIntent().getParcelableArrayListExtra(Config.KEY_GasStationList);
		StationListAdapter adapter = new StationListAdapter(mContext, gasStationList);
		lv_station.setAdapter(adapter);
		
		
		lv_station.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent intent = new Intent(mContext , StationInfoActivity.class);
				intent.putExtra(Config.KEY_GasStation , gasStationList.get(position));
				intent.putExtra(Config.KEY_LocLat , getIntent().getDoubleExtra(Config.KEY_LocLat , 0));
				intent.putExtra(Config.KEY_LocLon , getIntent().getDoubleExtra(Config.KEY_LocLon , 0));
				startActivity(intent);
			}
		});
	}

}