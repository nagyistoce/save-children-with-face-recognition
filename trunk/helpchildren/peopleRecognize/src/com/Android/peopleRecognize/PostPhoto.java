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
	 * ֱ��ͨ��HTTPЭ���ύ���ݵ�������,ʵ�ֱ��ύ���� 
	 * @param actionUrl �ϴ�·�� 
	 * @param params ������� keyΪ������,valueΪ����ֵ 
	 * @param file �ϴ��ļ� 
	 */  
	public static String post(String actionUrl, FormFile file) {  
	    try {             
	        String BOUNDARY = "---------7dbb912107e0"; //���ݷָ���  
	        String MULTIPART_FORM_DATA = "multipart/form-data";  
	          
	        URL url = new URL(actionUrl);  
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
	        conn.setDoInput(true);//��������  
	        conn.setDoOutput(true);//�������  
	        conn.setUseCaches(false);//��ʹ��Cache  
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
            
	        //���ݽ�����־     
	        byte[] end_data = ("--" + BOUNDARY + "--\r\n").getBytes();          
	        outStream.write(end_data);  
	        
	        outStream.flush();  
	        int cah = conn.getResponseCode();  
	        if (cah != 200) throw new RuntimeException("����urlʧ��");  
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
	 * javaBean��FormFile��װ�ļ�����Ϣ
	 */
	public static class FormFile {  
	    /* �ϴ��ļ������� */  
	    private byte[] data;  
	    /* �ļ����� */  
	    private String filname;  
	    /* ���ֶ�����*/  
	    private String formname;  
	    /* �������� */  
	    private String contentType = "application/octet-stream"; //��Ҫ������ص�����  
	      
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
