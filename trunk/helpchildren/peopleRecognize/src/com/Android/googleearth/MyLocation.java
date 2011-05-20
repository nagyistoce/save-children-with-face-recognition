package com.Android.googleearth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MyLocation extends Activity implements LocationListener{
	private static String tag = "MyLocation";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(tag, "onCreate");
//        openGPSSettings();
        //update movement
        updateStat();
    }
    
    private LocationManager mgr;
    private String best;
    public String address;
    
    private void updateStat() {	
    	mgr = (LocationManager) getSystemService(LOCATION_SERVICE);
    	if (mgr.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
    		Toast.makeText(this, "GPS模块正常", Toast.LENGTH_SHORT).show();
    		Log.d(tag, "GPS successful");      
    	} else {       
	    	Toast.makeText(this, "请开启GPS！", Toast.LENGTH_SHORT).show();
	    	Log.d(tag, "GPS not successful");
	    	Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
	    	//此为设置完成后返回到获取界面
	    	startActivityForResult(intent,0); 
    	}
    	Criteria criteria = new Criteria();
    	criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度        
    	criteria.setAltitudeRequired(false);        
    	criteria.setBearingRequired(false);        
    	criteria.setCostAllowed(true);        
    	criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗
    	best = mgr.getBestProvider(criteria, true); // 获取GPS信息       
    	Location location = mgr.getLastKnownLocation(best); // 通过GPS获取位置   	
//    	Location location = mgr.getLastKnownLocation("gps");
	
    	if (location != null) {  		
    		//pop up
    		StringBuffer msg = new StringBuffer();
    		//获取经纬度
    		msg.append("Latitude: ");
    		msg.append(Double.toString(location.getLatitude()));
    		
    		msg.append(", Longitude: ");
    		msg.append(Double.toString(location.getLongitude()));
    		
    		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    		address = msg.toString();
    	} else {
    		Toast.makeText(this, "NO LOCATION FOUND", Toast.LENGTH_LONG).show();
    		address = "";
    	}
//    	// 设置监听器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
//    	mgr.requestLocationUpdates(best, 100 * 1000, 500,
//                locationListener);
    }
    
    //toast show when location change
    public void onLocationChanged(Location location) {
    	Toast.makeText(this, location.toString(), Toast.LENGTH_LONG).show();
    }
    
    public void onProviderDisabled(String provider) {
    	
    }
    
    public void onStatusChanged(String provider, int status, Bundle extras) {
    	
    }

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
	protected void onRusume() {
		super.onResume();
		mgr.requestLocationUpdates(best, 60000, 1, this);
	}
	
	protected void onPause() {
		super.onPause();
		mgr.removeUpdates(this);
	}
}