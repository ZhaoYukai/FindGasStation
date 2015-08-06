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

public class ListGridViewAdapter extends BaseAdapter{
	
	private List<Petrol> petrolList = null;
	private LayoutInflater mLayoutInflater = null;
	
	/**
	 * **************************************************************************
	 */
	
	public ListGridViewAdapter(Context context , List<Petrol> petrolList) {
		this.petrolList = petrolList;
		mLayoutInflater = LayoutInflater.from(context);
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
			rowView = mLayoutInflater.inflate(R.layout.item_price_gridview , null);
			convertView = rowView;
		}
		else {
			rowView = convertView;
		}
		
		TextView tv = (TextView) rowView.findViewById(R.id.tv);
		Petrol petrol = getItem(position);
		tv.setText(petrol.getType() + " " + petrol.getPrice());
		
		return rowView;
	}
	
}
