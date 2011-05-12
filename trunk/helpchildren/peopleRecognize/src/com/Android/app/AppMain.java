package com.Android.app;

import com.Android.R;
import com.Android.peopleRecognize.CameraMain;
import com.Android.weibo.WeiboMain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


public class AppMain extends Activity{
	private final static String TAG = "AppMain";
	GridView mGridView;
	@Override
	public void onCreate( Bundle savedInstanceState ){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appmain);
		
		mGridView = (GridView)findViewById(R.id.gridview);
		mGridView.setNumColumns(2);
		mGridView.setAdapter( new ImageAdapter(this));
		mGridView.setOnItemClickListener(new MyOnItemClickListener());
	}
	
	private class ImageAdapter extends BaseAdapter{
		
		Activity mActivity;
		public ImageAdapter( Activity activity) {
			mActivity = activity;
			LayoutInflater inflater = mActivity.getLayoutInflater();
			
			views = new View[6];
			for( int i=0; i<6; i++ ){
				views[i] = inflater.inflate(R.layout.view_appmain, null);
				((ImageView)(views[i].findViewById(R.id.imageView1))).setImageResource(pics[i]);
				((TextView)(views[i].findViewById(R.id.textView1))).setText(texts[i]);
				((TextView)(views[i].findViewById(R.id.textView1))).setTextColor(0xff000000);
			}
		}

		@Override
		public int getCount() {
			return 6;
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			return views[arg0];
		}
		
		private View[] views;
		
		private int[] pics = new int[]{
				R.drawable.app1,
				R.drawable.app2,
				R.drawable.app3,
				R.drawable.app4,
				R.drawable.app5,
				R.drawable.app6,
		};
		
		private String[] texts = new String[]{
				"Î¢²©",
				"µØÍ¼",
				"ÅÄÕÕ",
				"ÉèÖÃ",
				"°ïÖú",
				"¹ØÓÚ",
		};
	}
	
	private class MyOnItemClickListener implements AdapterView.OnItemClickListener{		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			
			Intent intent;
			Bundle bundle;
			
			switch (arg2) {
			//Î¢²©
			case 0:
				intent = new Intent(AppMain.this, WeiboMain.class);
				startActivity(intent);
				break;
			//µØÍ¼
			case 1:
//				double[] center = new double[]{ 23.0666, 113.3920 };
//				double[] latitudes = new double[]{23.06521, 23.06567, 23.07160, 23.06841, 23.06902 };
//				double[] longitudes = new double[]{ 113.3919, 113.3889, 113.3934, 113.3931, 113.3911 };
//				String[] names = new String[]{
//						"1", "2 ", "3", "4","5"
//				};
//				int[] res = new int[]{
//						R.drawable.app0,
//						R.drawable.app1,
//						R.drawable.app2,
//						R.drawable.app3,
//						R.drawable.app4,
//				};
//				bundle = Utils.getBundleToStartMap(res, names, latitudes, longitudes, center);
//				intent = new Intent(AppMain.this, ShowPositionsOnMap.class);
//				intent.putExtras(bundle);
//				startActivity(intent);
				break;
			case 2:
				intent = new Intent(AppMain.this, CameraMain.class);
				startActivity(intent);
				break;
			case 3:
				intent = new Intent(AppMain.this, Setting.class);
				startActivity(intent);
				break;
			case 4:
				//to do later
				break;
			case 5:
				//to do later
				break;
			default:
				break;
			}
			
		}
	}
}
