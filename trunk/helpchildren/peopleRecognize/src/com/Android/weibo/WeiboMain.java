package com.Android.weibo;

import com.Android.R;

import android.app.Activity;
import android.os.Bundle;

public class WeiboMain extends Activity {
	private final static String TAG = "weiboMain";
	
	public void onCreate( Bundle savedInstanceState ){		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibomain);
	}
}
