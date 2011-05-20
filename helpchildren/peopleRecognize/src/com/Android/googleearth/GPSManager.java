package com.Android.googleearth;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;

public class GPSManager {
	
	private static final String TAG = "GPSManager";
	
	Context mContext;
	
	Location mCurrentLocation;
	LocationManager mLocationManager;
	LocationProvider mGPSProvider;
	GpsStatus mLastGpsStatus;
	GpsStatusListener mGpsStatusListener;
	GpsLocationListener mGpsLocationListener;
	
	boolean mGpsStarted;
	int mGpsFixAt;
	int mNbSat;
	int mNbSatFix;
	long mLastLocTS=0;
	long mPreviousLocTime=0;
	boolean locationChange=true;
	
	Runnable mPeriodicUpdate;
	Handler mHandler;
	
	public GPSManager(Context context){
		mContext = context;
		mHandler = new Handler();
		mLocationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
		
		startGps();
	}
	
	public int[] getSatNumbers(){
		return new int[]{mNbSat,mNbSatFix};
	}
	
	public Location getCurrentLocation(){
		return mCurrentLocation;
	}
	
	private class FindLocationUpdate implements Runnable{
		@Override
		public void run(){
			if (mPeriodicUpdate == null ){
				return ;
			} else {
				updateLocationInfo();
				mHandler.postDelayed(mPeriodicUpdate, 2000);
			}
		}
	}
	
	public boolean isGPSEnable(){
		if (mLocationManager == null )
			return false;
		return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	
	private void updateLocationInfo(){
		
	}
	
	public void startGps(){
		try {
			if (mLocationManager == null){
				mLocationManager = (LocationManager)mContext.getSystemService( Context.LOCATION_SERVICE );
			}
			
			if (mGPSProvider == null && mLocationManager != null){
				mGPSProvider = mLocationManager.getProvider(LocationManager.GPS_PROVIDER);
			}
			
			if (mLocationManager != null){
				mGpsStatusListener = new GpsStatusListener();
				mLocationManager.addGpsStatusListener(mGpsStatusListener);
				
				mGpsLocationListener = new GpsLocationListener();
				mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mGpsLocationListener);
			}
			
			mPeriodicUpdate = new FindLocationUpdate();
			mPeriodicUpdate.run();
		} finally{
			updateGpsStatus();
		}
	}
	
	public void stopGps(){
		mPeriodicUpdate = null;
		
		if (mLocationManager != null){
			if (mGpsLocationListener != null){
				mLocationManager.removeUpdates(mGpsLocationListener);
				mGpsStatusListener = null;
			}
			if (mGpsStatusListener != null){
				mLocationManager.removeGpsStatusListener(mGpsStatusListener );
				mGpsStatusListener = null;
			}
			mGPSProvider = null;
			mLocationManager = null;
		}
		
		updateGpsStatus();
	}
	
	private class GpsStatusListener implements GpsStatus.Listener{

		@Override
		public void onGpsStatusChanged(int event) {
			if (mLocationManager == null)return;
			
			mLastGpsStatus = mLocationManager.getGpsStatus(mLastGpsStatus);
			
			switch (event) {
			case GpsStatus.GPS_EVENT_STARTED:
				mGpsStarted = true;
				break;
			case GpsStatus.GPS_EVENT_STOPPED:
				mGpsStarted = false;
				break;
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				mGpsFixAt = mLastGpsStatus.getTimeToFirstFix();
				break;
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				mNbSat = 0;
				mNbSatFix = 0;
				for( GpsSatellite sat : mLastGpsStatus.getSatellites() ){
					mNbSat++;
					if (sat.usedInFix())mNbSatFix++;
				}
				break;
			}
			
			updateGpsStatus();
		}		
	}
	
	private void updateGpsStatus(){		
		
	}
	
	private class GpsLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			locationChange = true;
			mCurrentLocation = new Location(location);
			updateLocationInfo();
			long ts = System.currentTimeMillis();
			if (mLastLocTS > 0)
				mPreviousLocTime = ts - mLastLocTS;
			mLastLocTS = ts;
		}

		@Override
		public void onProviderDisabled(String provider) {
			stopGps();
		}

		@Override
		public void onProviderEnabled(String provider) {
			startGps();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			updateGpsStatus();
		}		
	}
}
