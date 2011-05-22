package com.Android.peopleRecognize;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.Android.R;
import com.Android.googleearth.GPSManager;
import com.Android.main.Main;
import com.Android.peopleRecognize.GetImage.PictureNode;
import com.Android.peopleRecognize.Preview;
import com.Android.peopleRecognize.PostPhoto.FormFile;;;


public class CameraMain extends Activity {
	private static final String TAG = "CameraDemo";
	private static final int DIALOG1 = 1;
	private static final int DIALOG2 = 2;
	private String currentImageName;
	private String address;
	
	private CameraMain camera;
	private Preview preview;
	private GPSManager mGpsManager;
	
	private Button buttonClick;
	private Button buttonUpload;
	private Button selectButton;

	private EditText location;
	private EditText note;
	private EditText file;

	private Bitmap image;
	private PictureNode picnode;
	
	private String[] detail = new String[3];
	private byte[] bytes = new byte[4096];
	String response;
	
	private static String actionUrl = "http://192.168.0.67:8080/FaceRec/upload/imgUpload";

	private static final String IMAGE_PATH = android.os.Environment
	.getExternalStorageDirectory().getAbsolutePath() + "/faceRecognizeIMAGE";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);
				
		Log.d(TAG, "begin");
		preview = new Preview(this);//get the picture
		((FrameLayout) findViewById(R.id.preview)).addView(preview);
		
		mGpsManager = Main.mGpsManager;
		
		//如果用于保存数据库文件的目录不存在则创建目录
		File dir = new File(IMAGE_PATH);
		if (!dir.exists()) {
			if(dir.mkdirs()) 
				Log.d("dir create", "success    " + IMAGE_PATH);
			else
				Log.d("dir create", "fail     " + IMAGE_PATH);
		}	
		find_and_modify_view();
		Log.d(TAG, "onCreate'd");		
	};
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id){
		case 1:
			return buildDialog1(CameraMain.this);	
		}
		return null;
	}
	
	private void find_and_modify_view() {
		//take photo
		buttonClick = (Button) findViewById(R.id.buttonClick);
		buttonUpload = (Button) findViewById(R.id.buttonUpload);
		
		buttonClick.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				preview.camera.takePicture(shutterCallback, rawCallback,jpegCallback);
				if(jpegCallback != null)
				{
					new AlertDialog.Builder(CameraMain.this)
					.setMessage("是否上传?")
					.setPositiveButton(R.string.ok,new android.content.DialogInterface.OnClickListener(){
						public void onClick(
								DialogInterface dialoginterface, int i){
							Log.d(TAG, "select photo");													
							showDialog(DIALOG1);
						}						
					}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
						public void onClick(
								DialogInterface dialoginterface, int i){
							dialoginterface.dismiss();
						}
					}).show();					
				}
			}
		});	
		buttonUpload.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG1);								
			}
		});	
	}
	
	
	private Dialog buildDialog1(Context context) {
		//get current location	
//		getCurrenLocation();
		LayoutInflater inflater = LayoutInflater.from(this);
		final View textEntryView = inflater.inflate(
				R.layout.upload_dialog, null);
		
		location = (EditText)textEntryView.findViewById(R.id.location);
		note = (EditText) textEntryView.findViewById(R.id.note);
		file = (EditText) textEntryView.findViewById(R.id.file);
		file.setText(IMAGE_PATH + currentImageName + ".jpg");
		
		selectButton = (Button) textEntryView.findViewById(R.id.select);
		selectButton.setOnClickListener( new OnClickListener() {
			public void onClick(View v) {
				//select image from sdcard
				detail[0] = String.valueOf(location.getText());
				detail[1] = String.valueOf(note.getText());
				detail[2] = String.valueOf(file.getText());
				Intent intent = new Intent(CameraMain.this, SelectPhoto.class);
				intent.putExtra("currentDetail",detail );
				startActivity(intent);
				finish();
			}	
		});
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.icon);
		builder.setView(textEntryView);
		builder.setPositiveButton("upload",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						detail[0] = String.valueOf(location.getText());
						detail[1] = String.valueOf(note.getText());
						detail[2] = String.valueOf(file.getText());
						//get the pic
/*						try {
							image = GetImage.loadBitmap(IMAGE_PATH + currentImageName + ".jpg");
							FileInputStream fStream = new FileInputStream(IMAGE_PATH + currentImageName + ".jpg");
							bytes = GetImage.getBytesFromInputStream(fStream, 3500000);
							Log.d(TAG, bytes.toString());
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
						catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
*/
						
//						picnode.image = image;
//						picnode.description = String.valueOf(note.getText());
//						picnode.location = String.valueOf(location.getText());
						
						//get byte
						
						/*upload*/	
						FileInputStream fStream;
						try {
							fStream = new FileInputStream(detail[2]);
							bytes = GetImage.getBytesFromInputStream(fStream, 3500000);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							Toast.makeText(CameraMain.this, "文件不存在！", Toast.LENGTH_LONG);
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							image = GetImage.loadBitmap(detail[2]);															
							
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}	
						
						if (image == null) {
							Toast.makeText(CameraMain.this, "文件不存在！", Toast.LENGTH_LONG);
						} else {
							ImageView jpgView = new ImageView(CameraMain.this);
							jpgView.setImageBitmap(image);						
							new AlertDialog.Builder(CameraMain.this).setView(jpgView).setPositiveButton("确定",
									null).show();
						}
						
						if ("".equals(response)) {
							new AlertDialog.Builder(CameraMain.this)
							.setMessage("上传失败！")
							.setPositiveButton(R.string.ok,null)
							.show();
						}	
						
						FormFile file = new FormFile(detail[2], bytes, "image", "image/pjpeg");			            
						response = PostPhoto.post(actionUrl, file);
						Log.i("response", response);
					}					
				});
		builder.setNegativeButton("cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						setTitle("点击了对话框上的取消按钮");
						dialog.dismiss();
					}
				});
		return builder.create();	
	};
	
	private void getCurrenLocation() {
		// TODO Auto-generated method stub
		if( mGpsManager.isGPSEnable() && mGpsManager.getCurrentLocation() == null ){
			Toast.makeText(this, "正在获取您的位置，请稍候……", Toast.LENGTH_SHORT).show();						
		}else if( mGpsManager.isGPSEnable() && mGpsManager.getCurrentLocation() != null ){			
			StringBuffer msg = new StringBuffer();
    		//获取经纬度
    		msg.append("Latitude: ");
    		msg.append(Double.toString(mGpsManager.getCurrentLocation().getLatitude()));
    		
    		msg.append(", Longitude: ");
    		msg.append(Double.toString(mGpsManager.getCurrentLocation().getLongitude()));  		
    		address = msg.toString();
    		detail[0] = address;
		}
	}

	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			Log.d(TAG, "onShutter'd");
		}
	};

	/** Handles data for raw picture */
	PictureCallback rawCallback = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			Log.d(TAG, "onPictureTaken - raw");
		}
	};
	

	/** Handles data for jpeg picture */
	PictureCallback jpegCallback = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			FileOutputStream outStream = null;
			try {
			//	String pathName="/sdcard/faceRecognizeImageFile/";
			    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");     
			    String fileName = sDateFormat.format(new java.util.Date()); 
			    currentImageName = fileName;
			    Log.d(TAG + "testing", fileName);
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