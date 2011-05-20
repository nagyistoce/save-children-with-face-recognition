package com.Android.peopleRecognize;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
	
	/** 
     * Get pictures under directory of strPath 
     * @param strPath 
     * @return list 
     */  
    public static List<String> getPictures(String strPath) {  
        List<String> list = new ArrayList<String>();  
          
        File file = new File(strPath);  
        File[] files = file.listFiles();  
          
        if (files == null) {  
            return null;  
        }  
          
        for(int i = 0; i < files.length; i++) {  
            final File f = files[i];  
            if(f.isFile()) {  
                try{  
                    int idx = f.getPath().lastIndexOf(".");  
                    if (idx <= 0) {  
                        continue;  
                    }  
                    String suffix = f.getPath().substring(idx);  
                    if (suffix.toLowerCase().equals(".jpg") ||  
                        suffix.toLowerCase().equals(".jpeg") ||  
                        suffix.toLowerCase().equals(".bmp") ||  
                        suffix.toLowerCase().equals(".png") ||  
                        suffix.toLowerCase().equals(".gif") )  
                    {  
                        list.add(f.getPath());  
                    }  
                } catch(Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
          
        return list;  
    }  
	
	public static class PictureNode implements Serializable{
		Bitmap image;
		String location;
		String description;
	}
}
