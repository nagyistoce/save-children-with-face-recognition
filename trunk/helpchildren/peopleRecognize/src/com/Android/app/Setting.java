package com.Android.app;

import com.Android.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;

//setting
public class Setting extends Activity{
	private final static String TAG = "Settting";
		
	Button BtnOk;
	Button BtnCancel;
	Spinner SpinnerTheme;
	Spinner SpinnerAutoLogin;
	Spinner SpinnerPwd;
	public void onCreate( Bundle savedInstanceState ){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		//to do
		Log.d(TAG, "activity start!");
	}
}
