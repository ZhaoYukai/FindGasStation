package com.findgasstation.app.bean;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 表示加油站的类
 */
public class GasStation implements Parcelable {
	
	private String name = null; //加油站的名字
	private String address = null; //加油站的所在地址
	private String area = null; //加油站的所在区域
	private String brand = null; //所属石化公司
	private double lat; //纬度
	private double lon; //经度
	private int distance; //距离
	
	private ArrayList<Petrol> priceList = null; //省基准的油价
	private ArrayList<Petrol> gastPriceList = null; //加油站的油价

	/**
	 * **************************************************************************
	 */
	
	//以下是getter和setter方法
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public ArrayList<Petrol> getGastPriceList() {
		return gastPriceList;
	}
	public void setGastPriceList(ArrayList<Petrol> gastPriceList) {
		this.gastPriceList = gastPriceList;
	}
	public ArrayList<Petrol> getPriceList() {
		return priceList;
	}
	public void setPriceList(ArrayList<Petrol> priceList) {
		this.priceList = priceList;
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
		dest.writeString(name);
		dest.writeString(address);
		dest.writeString(area);
		dest.writeString(brand);
		dest.writeDouble(lat);
		dest.writeDouble(lon);
		dest.writeInt(distance);
		dest.writeList(gastPriceList);
		dest.writeList(priceList);
	}
	
	
	/**************************************************************************
	 * 匿名内部类
	 */
	public static final Parcelable.Creator<GasStation> CREATOR = new Parcelable.Creator<GasStation>() {

		@SuppressWarnings("unchecked")
		@Override
		public GasStation createFromParcel(Parcel source) {
			
			GasStation gasStation = new GasStation();
			gasStation.name = source.readString();
			gasStation.address = source.readString();
			gasStation.area = source.readString();
			gasStation.brand = source.readString();
			gasStation.lat = source.readDouble();
			gasStation.lon = source.readDouble();
			gasStation.distance = source.readInt();
			gasStation.gastPriceList = source.readArrayList(Petrol.class.getClassLoader());
			gasStation.priceList = source.readArrayList(Petrol.class.getClassLoader());
			
			return gasStation;
		}
		
		/**
		 * **************************************************************************
		 */

		@Override
		public GasStation[] newArray(int size) {
			// TODO Auto-generated method stub
			return null;
		}
		
	};
	
}
