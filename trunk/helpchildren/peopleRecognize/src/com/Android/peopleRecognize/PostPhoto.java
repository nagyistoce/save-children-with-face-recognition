package com.Android.peopleRecognize;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class PostPhoto {
	
	/** 
	 * 直接通过HTTP协议提交数据到服务器,实现表单提交功能 
	 * @param actionUrl 上传路径 
	 * @param params 请求参数 key为参数名,value为参数值 
	 * @param file 上传文件 
	 */  
	public static String post(String actionUrl, FormFile file) {  
	    try {             
	        String BOUNDARY = "---------7dbb912107e0"; //数据分隔线  
	        String MULTIPART_FORM_DATA = "multipart/form-data";  
	          
	        URL url = new URL(actionUrl);  
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
	        conn.setDoInput(true);//允许输入  
	        conn.setDoOutput(true);//允许输出  
	        conn.setUseCaches(false);//不使用Cache  
	        conn.setRequestMethod("POST");            
	        conn.setRequestProperty("Connection", "Keep-Alive");  
	        conn.setRequestProperty("Charset", "UTF-8");  
	        conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);  
	  
	        StringBuilder sb = new StringBuilder();  
	          
	        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());        
	        
	        StringBuilder split = new StringBuilder();  
            split.append("--");  
            split.append(BOUNDARY);  
            split.append("\r\n");  
            split.append("Content-Disposition: form-data;name=\""+ file.getFormname()+"\";filename=\""+ file.getFilname() + "\"\r\n");  
            split.append("Content-Type: "+ file.getContentType()+"\r\n\r\n");  
            outStream.write(split.toString().getBytes());  
            outStream.write(file.getData(), 0, file.getData().length);  
            outStream.write("\r\n".getBytes());
            
	        //数据结束标志     
	        byte[] end_data = ("--" + BOUNDARY + "--\r\n").getBytes();          
	        outStream.write(end_data);  
	        
	        outStream.flush();  
	        int cah = conn.getResponseCode();  
	        if (cah != 200) throw new RuntimeException("请求url失败");  
	        InputStream is = conn.getInputStream();  
	        int ch;  
	        StringBuilder b = new StringBuilder();  
	        while( (ch = is.read()) != -1 ){  
	            b.append((char)ch);  
	        }  
	        outStream.close();  
	        conn.disconnect();  
	        return b.toString();  
	    } catch (Exception e) {  
	        throw new RuntimeException(e);  
	    }  
	}  
	/*
	 * javaBean类FormFile封装文件的信息
	 */
	public static class FormFile {  
	    /* 上传文件的数据 */  
	    private byte[] data;  
	    /* 文件名称 */  
	    private String filname;  
	    /* 表单字段名称*/  
	    private String formname;  
	    /* 内容类型 */  
	    private String contentType = "application/octet-stream"; //需要查阅相关的资料  
	      
	    public FormFile(String filname, byte[] data, String formname, String contentType) {  
	        this.data = data;  
	        this.filname = filname;  
	        this.formname = formname;  
	        if(contentType!=null) this.contentType = contentType;  
	    }  
	  
	    public FormFile() {
			// TODO Auto-generated constructor stub
		}

		public byte[] getData() {  
	        return data;  
	    }  
	  
	    public void setData(byte[] data) {  
	        this.data = data;  
	    }  
	  
	    public String getFilname() {  
	        return filname;  
	    }  
	  
	    public void setFilname(String filname) {  
	        this.filname = filname;  
	    }  
	  
	    public String getFormname() {  
	        return formname;  
	    }  
	  
	    public void setFormname(String formname) {  
	        this.formname = formname;  
	    }  
	  
	    public String getContentType() {  
	        return contentType;  
	    }  
	  
	    public void setContentType(String contentType) {  
	        this.contentType = contentType;  
	    }  
	      
	}  
}
