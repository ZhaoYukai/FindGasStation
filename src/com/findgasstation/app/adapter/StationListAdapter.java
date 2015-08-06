package com.findgasstation.app.adapter;

import java.util.List;

import com.findgasstation.app.R;
import com.findgasstation.app.bean.GasStation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class StationListAdapter extends BaseAdapter {
	
	private Context mContext = null;
	private List<GasStation> gasStationList = null;
	private LayoutInflater mLayoutInflater = null;
	
	/**
	 * **************************************************************************
	 */
	
	public StationListAdapter(Context context , List<GasStation> gasStationList) {
		this.mContext = context;
		this.gasStationList = gasStationList;
		mLayoutInflater = LayoutInflater.from(context);
	}
	
	/**
	 * **************************************************************************
	 */

	@Override
	public int getCount() {
		return gasStationList.size();
	}
	
	/**
	 * **************************************************************************
	 */

	@Override
	public GasStation getItem(int position) {
		return gasStationList.get(position);
	}
	
	/**
	 * **************************************************************************
	 */

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	/**
	 * **************************************************************************
	 */

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View rowView = null;
		
		if(convertView == null) {
			rowView = mLayoutInflater.inflate(R.layout.item_station_list , null);
			convertView = rowView;
		}
		else {
			rowView = convertView;
		}
		
		TextView tv_id = (TextView) rowView.findViewById(R.id.tv_id);
		TextView tv_name = (TextView) rowView.findViewById(R.id.tv_name);
		TextView tv_distance = (TextView) rowView.findViewById(R.id.tv_distance);
		TextView tv_addr = (TextView) rowView.findViewById(R.id.tv_addr);
		
		GasStation gasStation = getItem(position);
		tv_id.setText((position + 1) + ".");
		tv_name.setText(gasStation.getName());
		tv_distance.setText(gasStation.getDistance() + mContext.getResources().getString(R.string.metre));
		tv_addr.setText(gasStation.getAddress());
		
		GridView gv = (GridView) rowView.findViewById(R.id.gv_price);
		ListGridViewAdapter adapter = new ListGridViewAdapter(mContext, gasStation.getGastPriceList());
		gv.setAdapter(adapter);
		
		return rowView;
	}
	
}
