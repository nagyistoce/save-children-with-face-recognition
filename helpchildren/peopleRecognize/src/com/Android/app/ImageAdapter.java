package com.Android.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Gallery.LayoutParams;

public class ImageAdapter extends BaseAdapter{
	
	Context mContext;
	Bitmap[] mBitmaps;
	LayoutInflater mInflater;
	ImageView[] mImageViews;
	public ImageAdapter( Context context, Bitmap[] bitmaps){
		mContext = context;
		mBitmaps = bitmaps;
		mImageViews = new ImageView[ mBitmaps.length ];
		
		LayoutParams layoutParams = new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
		for( int i=0; i<mBitmaps.length; i++ ){
			mImageViews[i] = new ImageView(mContext);
			mImageViews[i].setLayoutParams(layoutParams);
			mImageViews[i].setAdjustViewBounds(true);
			mImageViews[i].setImageBitmap(mBitmaps[i]);
		}
	}

	@Override
	public int getCount() {
		return mImageViews.length;
	}

	@Override
	public Object getItem(int arg0) {
		return mImageViews[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		return mImageViews[arg0];
	}

	
}
