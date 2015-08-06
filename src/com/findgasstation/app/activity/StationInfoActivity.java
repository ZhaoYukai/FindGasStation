package com.findgasstation.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.findgasstation.app.R;
import com.findgasstation.app.adapter.PriceListAdapter;
import com.findgasstation.app.bean.GasStation;
import com.findgasstation.app.util.Config;

public class StationInfoActivity extends Activity implements OnClickListener {

	private Context mContext;
	private TextView tv_title_right, tv_name, tv_distance, tv_area, tv_addr;
	private ImageView iv_back;

	private ScrollView sv;
	private ListView lv_gast_price, lv_price;
	
	private GasStation mGasStation = null;
	
	/**
	 * ****************************************************************************
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		
		mContext = this;
		initView();
		setText();
	}
	
	/**
	 * ****************************************************************************
	 */

	private void initView() {
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_distance = (TextView) findViewById(R.id.tv_distance);
		tv_area = (TextView) findViewById(R.id.tv_area);
		tv_addr = (TextView) findViewById(R.id.tv_addr);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		tv_title_right = (TextView) findViewById(R.id.tv_title_button);
		tv_title_right.setText(getResources().getString(R.string.navigate));
		tv_title_right.setOnClickListener(this);
		tv_title_right.setVisibility(View.VISIBLE);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setVisibility(View.VISIBLE);
		iv_back.setOnClickListener(this);
		
		//加油站的油价
		lv_gast_price = (ListView) findViewById(R.id.lv_gast_price);
		//省基准的油价
		lv_price = (ListView) findViewById(R.id.lv_price);
		sv = (ScrollView) findViewById(R.id.sv);
	}
	
	
	/*************************************************************************************
	 * 自定义的一个方法
	 * 用于设置文本控件，以及两个ListView的适配器
	 */
	public void setText() {
		mGasStation = getIntent().getParcelableExtra("GasStation");
		tv_name.setText(mGasStation.getName() + " - " + mGasStation.getBrand());
		tv_addr.setText(mGasStation.getAddress());
		tv_distance.setText(mGasStation.getDistance() + getResources().getString(R.string.metre));
		tv_area.setText(mGasStation.getArea());
		
		PriceListAdapter gastPriceAdapter = new PriceListAdapter(mContext, mGasStation.getGastPriceList());
		lv_gast_price.setAdapter(gastPriceAdapter);
		
		PriceListAdapter priceAdapter = new PriceListAdapter(mContext, mGasStation.getPriceList());
		lv_price.setAdapter(priceAdapter);
		
		sv.smoothScrollTo(0 , 0);
	}
	
	/**
	 * ****************************************************************************
	 */

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_title_button:
			Intent intent = new Intent(mContext , RouteActivity.class);
			intent.putExtra(Config.KEY_Lat , mGasStation.getLat());
			intent.putExtra(Config.KEY_Lon , mGasStation.getLon());
			intent.putExtra(Config.KEY_LocLat , getIntent().getDoubleExtra(Config.KEY_LocLat , 0));
			intent.putExtra(Config.KEY_LocLon , getIntent().getDoubleExtra(Config.KEY_LocLon , 0));
			startActivity(intent);
			break;
		default:
			break;
		}
	}

}
