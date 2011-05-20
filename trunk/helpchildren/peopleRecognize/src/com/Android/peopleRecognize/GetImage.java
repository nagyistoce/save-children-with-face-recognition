package com.Android.peopleRecognize;

import java.io.FileNotFoundException;
import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

public class GetImage {
	private static final String TAG = "GetImage";
	public static Bitmap loadBitmap(String url) throws FileNotFoundException{
		Log.e(TAG,"begin");
		if (url == null || url.equals("")) {
			return null;
		}
		Bitmap bm = null;
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(url, opts);
		opts.inJustDecodeBounds = false;
	  
		int sampleSize = 1;
		while (true) {
			if (opts.outHeight*opts.outWidth / sampleSize < 1281*901) {
				break;
			}
			sampleSize *= 2;
		}
		 opts.inSampleSize = sampleSize;
		 try {
			 bm = BitmapFactory.decodeFile(url, opts);
		 } catch(Exception ex) {
			 ex.printStackTrace();
			 return null;
		 }
		 return bm;
	}
	
	public static class PictureNode implements Serializable{
		Bitmap image;
		String location;
		String description;
	}
}
