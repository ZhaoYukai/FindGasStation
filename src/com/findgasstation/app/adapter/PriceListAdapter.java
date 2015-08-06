package com.findgasstation.app.adapter;

import java.util.List;

import com.findgasstation.app.R;
import com.findgasstation.app.bean.Petrol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PriceListAdapter extends BaseAdapter{
	
	private Context mContext = null;
	private List<Petrol> petrolList = null;
	
	/**
	 * **************************************************************************
	 */
	
	public PriceListAdapter(Context context , List<Petrol> petrolList) {
		mContext = context;
		this.petrolList = petrolList;
	}
	
	/**
	 * **************************************************************************
	 */
	
	@Override
	public int getCount() {
		return petrolList.size();
	}
	
	/**
	 * **************************************************************************
	 */

	@Override
	public Petrol getItem(int position) {
		return petrolList.get(position);
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
			rowView = LayoutInflater.from(mContext).inflate(R.layout.item_info_list , null);
			convertView = rowView;
		}
		else {
			rowView = convertView;
		}
		
		TextView tv_name = (TextView) rowView.findViewById(R.id.tv_name);
		TextView tv_price = (TextView) rowView.findViewById(R.id.tv_price);
		
		Petrol petrol = getItem(position);
		tv_name.setText(petrol.getType());
		tv_price.setText(petrol.getPrice());

		return rowView;
	}

}
