package com.Android.googleearth;

import com.Android.main.Main;

import android.widget.Toast;

public class GetMyLocation {
	
	private static GPSManager mGpsManager;
	private static String location;
	
	public static String getCurrenLocation() {
		mGpsManager = Main.mGpsManager;
		// TODO Auto-generated method stub
		if( mGpsManager.isGPSEnable() && mGpsManager.getCurrentLocation() == null ){
			return null;									
		}else if( mGpsManager.isGPSEnable() && mGpsManager.getCurrentLocation() != null ){			
			StringBuffer msg = new StringBuffer();
    		//获取经纬度
    		msg.append("Latitude: ");
    		msg.append(Double.toString(mGpsManager.getCurrentLocation().getLatitude()));
    		
    		msg.append(", Longitude: ");
    		msg.append(Double.toString(mGpsManager.getCurrentLocation().getLongitude()));  		
    		location = msg.toString();
    		return location;
		}
		return null;
	}
}
