package com.Android.peopleRecognize;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.Android.R;
import com.Android.peopleRecognize.PostPhoto.FormFile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SelectPhoto extends ListActivity {
	private static final String TAG = "SelectPhoto";
	private static final int DIALOG1 = 1;
	private String[] currentDetail;
	private List<String> pictureList = new ArrayList<String>();	
	private ArrayList<HashMap<String, Object>> listItems;   //存放文字、图片信息   
    private SimpleAdapter listItemAdapter;                  //适配器      
	
	private EditText location2;
	private EditText note2;
	private EditText file2;
	private Button selectPathButton;
	
	private Bitmap image;
	private byte[] bytes = new byte[4096];
	String response; 
	
	private static final String IMAGE_PATH = android.os.Environment
	.getExternalStorageDirectory().getAbsolutePath() + "/faceRecognizeIMAGE";
	
	private static String actionUrl = "http://192.168.0.67:8080/FaceRec/upload/imgUpload";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		currentDetail = bundle.getStringArray("currentDetail");
		
		pictureList = GetImage.getPicturesList(IMAGE_PATH);
		if (pictureList != null) {
			Log.d(TAG, "list.size = " + pictureList.size());
			initListView();
			this.setListAdapter(listItemAdapter); 
		} else {  
		    Log.d(TAG, "list is null!!!");
		    Toast.makeText(this, "列表为空", Toast.LENGTH_LONG).show();
		}		
	}
	
	 /**  
     * 设置适配器内容  
     */  
    private void initListView()   {   
		  listItems = new ArrayList<HashMap<String, Object>>();   
	      for(int i = 0; i < pictureList.size(); i++)      
	      {      
	          HashMap<String, Object> map = new HashMap<String, Object>();      
	          map.put("ItemTitle", pictureList.get(i));           
              BitmapFactory.Options options = new BitmapFactory.Options();
              options.inSampleSize = 4;
              Bitmap bm = BitmapFactory.decodeFile(pictureList.get(i), options);
              map.put("ItemImage", bm);
	          
	          listItems.add(map);      
	      } 
	     //生成适配器的Item和动态数组对应的元素      
	     listItemAdapter = new SimpleAdapter(this,listItems,     
	    		 R.layout.pic_list,              
	           new String[] {"ItemTitle", "ItemImage"},            
	           new int[] {R.id.pic_path, R.id.img}  
	     );      
    }  
    
    @Override  
    protected void onListItemClick(ListView l, View v, int position, long id) {   
        // TODO Auto-generated method stub        
        setTitle("你点击第" + position + "行");  
        currentDetail[2] = pictureList.get(position);
        Log.d("path:", currentDetail[2]); 
        Log.d("location:", currentDetail[0]);
        Log.d("note:", currentDetail[1]);

		LayoutInflater inflater = LayoutInflater.from(this);
		final View textEntryView2 = inflater.inflate(
				R.layout.upload_dialog, null);
		
		location2 = (EditText)textEntryView2.findViewById(R.id.location);
		note2 = (EditText) textEntryView2.findViewById(R.id.note);
		file2 = (EditText) textEntryView2.findViewById(R.id.file);
		selectPathButton = (Button) textEntryView2.findViewById(R.id.select);
		location2.setText(currentDetail[0]);
		note2.setText(currentDetail[1]);
		file2.setText(currentDetail[2]);
		selectPathButton.setVisibility(View.GONE);
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(SelectPhoto.this);
		builder.setIcon(R.drawable.icon);
		builder.setView(textEntryView2);
		builder.setPositiveButton("upload",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {											
						currentDetail[0] = String.valueOf(location2.getText());
						currentDetail[1] = String.valueOf(note2.getText());
						currentDetail[2] = String.valueOf(file2.getText());
						
						/*upload*/	
						FileInputStream fStream;
						try {
							fStream = new FileInputStream(currentDetail[2]);
							bytes = GetImage.getBytesFromInputStream(fStream, 3500000);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							Toast.makeText(SelectPhoto.this, "文件不存在！", Toast.LENGTH_LONG);
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
						//get the pic
						try {
							image = GetImage.loadBitmap(currentDetail[2]);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
						if (image == null) {
							Toast.makeText(SelectPhoto.this, "文件不存在！", Toast.LENGTH_LONG);
						} else {
							ImageView jpgView = new ImageView(SelectPhoto.this);
							jpgView.setImageBitmap(image);						
							new AlertDialog.Builder(SelectPhoto.this).setView(jpgView).setPositiveButton("确定",
									null).show();
						}
						
			            FormFile file = new FormFile(currentDetail[2], bytes, "image", "image/pjpeg");			            
						response = PostPhoto.post(actionUrl, file);
						
						if ("".equals(response)) {
							new AlertDialog.Builder(SelectPhoto.this)
							.setTitle("提示：")
							.setMessage("上传失败！")
							.setPositiveButton(R.string.ok,null)
							.show();
						}	
					}
				});
		builder.setNegativeButton("cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						setTitle("点击了对话框上的取消按钮");
						dialog.dismiss();
					}
				});
		builder.create().show();	
	};
}
