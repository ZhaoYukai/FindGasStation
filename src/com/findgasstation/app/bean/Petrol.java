package com.findgasstation.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 表示油价的类
 */
public class Petrol implements Parcelable {
	
	private String type = null; //类型
	private String price = null; //价格
	
	
	/**
	 * ********************************************************************
	 * ********************************************************************
	 * ********************************************************************
	 */
	
	//以下是getter和setter方法
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
	/**
	 * ********************************************************************
	 * ********************************************************************
	 * ********************************************************************
	 */
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * **************************************************************************
	 */
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		/**
		 * 将对象序列化为一个Parcelable对象
		 */
		dest.writeString(type);
		dest.writeString(price);
	}
	
	/****************************************************************************
	 * 匿名内部类
	 */
	
	public static final Parcelable.Creator<Petrol> CREATOR = new Parcelable.Creator<Petrol>() {

		@Override
		public Petrol createFromParcel(Parcel source) {
			Petrol petrol = new Petrol();
			petrol.type = source.readString();
			petrol.price = source.readString();
			return petrol;
		}

		@Override
		public Petrol[] newArray(int size) {
			// TODO Auto-generated method stub
			return null;
		}
	};
	
}
