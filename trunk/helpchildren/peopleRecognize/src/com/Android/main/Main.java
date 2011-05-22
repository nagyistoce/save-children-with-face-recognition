package com.Android.main;

import com.Android.R;
import com.Android.app.AppMain;
import com.Android.googleearth.GPSManager;
import com.Android.peopleRecognize.CameraMain;
import com.Android.weibo.WeiboMain;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;

public class Main extends TabActivity {
	private final static String TAG = "Main";	
	
	private final static String TAB_CAMERA = "Camera";
	private final static String TAB_WEIBO = "Weibo";
	private final static String TAB_APP = "App";
	
	public static GPSManager mGpsManager;
	
	TabHost mTabHost;
	TabHost.TabSpec mSpec;
	
	int mCurSelect=0;
	
	int[] mCircleX=null;
	int mCircleHeight;
	ImageView[] mCircles;
	
	int mScreenHeight;
	int mScreenWidth;
	
//	public static GPSManager mGpsManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mCircles = new ImageView[3];
        mCircles[0] = (ImageView)findViewById(R.id.circle1);
        mCircles[1] = (ImageView)findViewById(R.id.circle2);
        mCircles[2] = (ImageView)findViewById(R.id.circle3);  
                
        Display display = getWindowManager().getDefaultDisplay();
        mScreenHeight = display.getHeight();
        mScreenWidth = display.getWidth();
        
        Intent intent = new Intent(this, CameraMain.class);
        mTabHost = getTabHost(); 
        //mTabHost.setBackgroundColor(0xFFE4B500);
        mTabHost.setBackgroundColor(0xffffffff);
//        mTabHost.setBackgroundResource(R.drawable.backgroup);
        
        mSpec = mTabHost.newTabSpec(TAB_CAMERA)
        				.setIndicator("拍照")//,getResources().getDrawable(R.drawable.tab2))
        				.setContent(intent);
        mTabHost.addTab(mSpec);
        
        
        intent = new Intent(this, WeiboMain.class);
        mSpec = mTabHost.newTabSpec(TAB_WEIBO)
        				.setIndicator("我的微薄")
        				.setContent(intent);
        mTabHost.addTab(mSpec);
        
        intent = new Intent( this, AppMain.class);
        mSpec = mTabHost.newTabSpec(TAB_APP)
        				.setIndicator("应用")
        				.setContent(intent);
        mTabHost.addTab(mSpec);     
        
        mTabHost.setOnTabChangedListener( new MyOnTabChangeListener());
        
        mGpsManager = new GPSManager(this);
        if( !mGpsManager.isGPSEnable() ){
        	Toast.makeText(this, "GPS尚未打开，无法获得当前位置", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onDestroy(){
//    	if( mGpsManager!=null )
//    		mGpsManager.stopGps();
    	super.onDestroy();
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	
    }
    
    @Override
    public void onStart(){
    	super.onStart();
    	
    }
    
    class MyOnTabChangeListener implements TabHost.OnTabChangeListener{

		@Override
		public void onTabChanged(String tabId) {
			if( mCircleX == null ){
	    		mCircleX = new int[3];
	    		for( int i = 0; i < 3; i++ ){
	            	mCircleX[i] = mCircles[i].getLeft();
	                Log.e(TAG, i + " " + mCircleX[i]);
	            }
	    	}
			
			Animation animation;
			if( tabId.equals(TAB_APP)){
				animation = getNowAnimation(2);
			}else if (tabId.equals(TAB_CAMERA)) {
				animation = getNowAnimation(0);
			}else {
				animation = getNowAnimation(1);
			}
			mCircles[mCurSelect].startAnimation(animation);
		}    	
    }
    
    Animation getNowAnimation( final int target ){
    	Log.e(TAG, "target:" + target + " cur:" + mCurSelect);
    	if( target == mCurSelect )
    		return null;
    	
    	TranslateAnimation animation;
    	if( target - mCurSelect > 0 )
    		 animation = new TranslateAnimation(0, mCircleX[target - mCurSelect], 0, 0);
    	else {
			animation = new TranslateAnimation(0, -mCircleX[mCurSelect - target], 0, 0);
		}
    	animation.setInterpolator( AnimationUtils.loadInterpolator(Main.this,
                android.R.anim.accelerate_interpolator) );
    	animation.setDuration(500);
    	
    	animation.setAnimationListener( new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				mCircles[mCurSelect].setVisibility(View.INVISIBLE);
				mCircles[target].setVisibility(View.VISIBLE);
				mCurSelect = target;
			}
		});
    	
    	return animation;
    }
}
