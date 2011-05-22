package com.Android.peopleRecognize;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.util.Log;

public class GetImage {
	private static final String TAG = "GetImage";
	private static String actionUrl = " http://192.168.0.36:8080/FaceRec/upload/Upload.jsp";
	private static String newName = "image.jpg";
	
	public static Bitmap loadBitmap(String url) throws FileNotFoundException{
		Log.d(TAG,"begin");
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
    public static List<String> getPicturesList(String strPath) {  
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
	
    public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {
		if (bytes != null)
		{
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		}
		return null;
    }


    public static byte[] getBytesFromInputStream(InputStream is, int bufsiz) throws IOException {
		int total = 0;
		byte[] bytes = new byte[4096];
		ByteBuffer bb = ByteBuffer.allocate(bufsiz);
		
		while (true) {
			int read = is.read(bytes);
			if (read == -1)
				break;
			bb.put(bytes, 0, read);
			total += read;
		}
		byte[] content = new byte[total];
		bb.flip();
		bb.get(content, 0, total);
		return content;
	}
    
//    private void showHowToGetPhoto(String imagePath) {
//    	ContentResolver resolver = getContentResolver();
//    	
//    	//mContent���ϴ���ͼƬbyte[]���飬�õ�����������ô������Ȼ��Ҳ����ֱ����fileInput��
//    	FileInputStream fileInput = (FileInputStream) resolver.openInputStream(Uri.parse(imagePath));
//	
//    	// ��ͼƬ���ݽ������ֽ�����	
//    	byte[] mContent = getBytesFromInputStream(fileInput, 3500000);	
//    	fileInput.close();	
//	
//    	// ���ֽ�����ת��ΪImageView�ɵ��õ�Bitmap����	
//    	Bitmap bm = getPicFromBytes(mContent, null);
//    }  
    
    public static boolean uploadFile(String[] str) {
    	
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "---------7dbb912107e0";
        try
        {
	          URL url =new URL(actionUrl);
	          HttpURLConnection con=(HttpURLConnection)url.openConnection();
	          /* ����Input��Output����ʹ��Cache */
	          con.setDoInput(true);
	          con.setDoOutput(true);
	          con.setUseCaches(false);
	          /* ���ô��͵�method=POST */
	          con.setRequestMethod("POST");
	          /* setRequestProperty */
	          con.setRequestProperty("Connection", "Keep-Alive");
	          con.setRequestProperty("Charset", "UTF-8");
	          con.setRequestProperty("Content-Type",
	                             "multipart/form-data;boundary="+boundary);
	          /* ����DataOutputStream */
	          DataOutputStream ds = 
	        	  			new DataOutputStream(con.getOutputStream());
	          ds.writeBytes(twoHyphens + boundary + end);
	          ds.writeBytes("Content-Disposition: form-data; " +
	                        "name=\"file1\";filename=\"" +
	                        newName +"\"" + end);
	          ds.writeBytes(end);   
	
	          /* ȡ���ļ���FileInputStream */
	          FileInputStream fStream = new FileInputStream(str[2]);
	          /* ����ÿ��д��1024bytes */
	          int bufferSize = 1024;
	          byte[] buffer = new byte[bufferSize];
	
	          int length = -1;
	          /* ���ļ���ȡ������������ */
	          while((length = fStream.read(buffer)) != -1)
	          {
	        	  /* ������д��DataOutputStream�� */
	        	  ds.write(buffer, 0, length);
	          }
	          ds.writeBytes(end);
	          ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
	
	          /* close streams */
	          fStream.close();
	          ds.flush();
	
	          /* ȡ��Response���� */
	          InputStream is = con.getInputStream();
	          int ch;
	          StringBuffer b =new StringBuffer();
	          while( ( ch = is.read() ) != -1 )
	          {
	        	  b.append( (char)ch );
	          }
	          ds.close();
	          return true;
	    } catch (Exception e)
        {
        	e.printStackTrace();
        	return false;
        }	  		
	}
    
	public static class PictureNode implements Serializable{
		byte[] imagebytes;
		String path;
		String location;
		String description;
	}
}
