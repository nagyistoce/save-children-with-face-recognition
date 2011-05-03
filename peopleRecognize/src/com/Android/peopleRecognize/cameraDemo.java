package com.Android.peopleRecognize;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.Android.peopleRecognize.Preview;

public class cameraDemo extends Activity {
	private static final String TAG = "CameraDemo";
	Camera camera;
	Preview preview;
	Button buttonClick;
	
	private static final String IMAGE_PATH = android.os.Environment
	.getExternalStorageDirectory().getAbsolutePath() + "/faceRecognizeIMAGE";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Log.d(TAG, "begin");
		preview = new Preview(this);//get the picture
		((FrameLayout) findViewById(R.id.preview)).addView(preview);
		
		//如果用于保存数据库文件的目录不存在则创建目录
		File dir = new File(IMAGE_PATH);
		if (!dir.exists()) {
			if(dir.mkdirs()) 
				Log.d("dir create", "success    " + IMAGE_PATH);
			else
				Log.d("dir create", "fail     " + IMAGE_PATH);
		}
		
		buttonClick = (Button) findViewById(R.id.buttonClick);
		buttonClick.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				preview.camera.takePicture(shutterCallback, rawCallback,
						jpegCallback);
				if(jpegCallback != null)
				{
					new AlertDialog.Builder(cameraDemo.this)
					.setMessage("是否上传?")
					.setPositiveButton(R.string.ok,new android.content.DialogInterface.OnClickListener(){
						public void onClick(
								DialogInterface dialoginterface, int i){
							//todo
						}
					}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
						public void onClick(
								DialogInterface dialoginterface, int i){
							
						}
					}).show();
					}
				}
		});

		Log.d(TAG, "onCreate'd");
	}

	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			Log.d(TAG, "onShutter'd");
		}
	};

	/** Handles data for raw picture */
	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d(TAG, "onPictureTaken - raw");
		}
	};

	/** Handles data for jpeg picture */
	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			FileOutputStream outStream = null;
			try {
			//	String pathName="/sdcard/faceRecognizeImageFile/";
			    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");     
			    String fileName = sDateFormat.format(new java.util.Date());  		    
			    Log.d("testing", fileName);
			    //Toast.makeText(cameraDemo.this, fileName, Toast.LENGTH_LONG);
				// write to local sandbox file system
				// outStream = CameraDemo.this.openFileOutput(String.format("%d.jpg",System.currentTimeMillis()), 0);
				
				// Or write to sdcard
				outStream = new FileOutputStream(String.format(
						IMAGE_PATH + "/" + fileName + ".jpg", System.currentTimeMillis()));
				//		"/sdcard/camera1.jpg", System.currentTimeMillis()));
				outStream.write(data);
				outStream.close();
				Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
			Log.d(TAG, "onPictureTaken - jpeg");
		}
	};

}