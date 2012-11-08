/**
 * 
 */
package com.audacity.wishmagnet.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;

/**
 * @author Shaon
 *
 */
public class httpClient {
	
	static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    
    
    /**
     * Sent http request on server for data with namevaluepairs and InputStream
     * @param nvpLatestIntention data into nameValuePairs
     * @param inputStream 
     * @return return string
     */
    
	public String httpConnection(ArrayList<NameValuePair> nvp){
		
		String Res = null;
		
		try{
				
			HttpClient httpClient = new DefaultHttpClient();
			
			HttpPost httpPost = new HttpPost("http://wishmagnet.com/json.php");
			
			//HttpPost httpPost = new HttpPost("http://wishmagnet.com/androidwishmagnet/android_latest_intention.php");

			httpPost.setEntity(new UrlEncodedFormEntity(nvp));
			
			HttpResponse response;
		
			
				try {
						response = httpClient.execute(httpPost);
						
						int statusCode = response.getStatusLine().getStatusCode();
						
						if(statusCode != HttpStatus.SC_OK){
							
							return null;
							
						}else{
								HttpEntity entity = response.getEntity();
								
								if(entity!= null){
								
									Res = EntityUtils.toString(entity);
									
								}//end of if condition
								
						}//end of if else condition
						
						
						Log.d("<<<-http_log_tag->>>","Succes in Http Connection");
							
					} catch (UnsupportedEncodingException e) {
						
						Log.e("<<<- httpConnection API CALL ->>>","Error in UnsupportedEncodingException : " + e.toString());
						
					} catch (ClientProtocolException e) {
						
						Log.e ("<<<- httpConnection API CALL ->>>", "Error in ClientProtocolException : " + e.toString());
						
					} catch (IllegalStateException e) {
						
						Log.e("<<<- httpConnection API CALL ->>>", "Error in IllegalStateException : " + e.toString());
						
					} catch (IOException e) {
						
						Log.e("<<<- httpConnection API CALL ->>>", "Error in IOException : " + e.toString());
						
					}//end of exception handling
			
				
				Log.d("<<<- http_log_tag ->>>","Succes in Http Connection");
				
		}catch(Exception e){
			
			Log.e("<<<- http_log_tag ->>>","Error in Http Connection" + e.toString());
			
		}//end of exception handling
		
		return Res;
		
	}//end of httpConnection()	
	
	/**
	 * 
	 * @param URL
	 * @param nvp
	 * @return
	 */
	public String httpConnectionWithURL(String URL,ArrayList<NameValuePair> nvp){
		
		String Res = null;
		
		try{
				
			HttpClient httpClient = new DefaultHttpClient();
			
			HttpPost httpPost = new HttpPost(URL);
			
			//HttpPost httpPost = new HttpPost("http://wishmagnet.com/androidwishmagnet/android_latest_intention.php");

			httpPost.setEntity(new UrlEncodedFormEntity(nvp));
			
			HttpResponse response;
		
			
				try {
						response = httpClient.execute(httpPost);
						
						int statusCode = response.getStatusLine().getStatusCode();
						
						if(statusCode != HttpStatus.SC_OK){
							
							return null;
							
						}else{
								HttpEntity entity = response.getEntity();
								
								if(entity!= null){
								
									Res = EntityUtils.toString(entity);
									
								}//end of if condition
								
						}//end of if else condition
						
						
						Log.d("<<<-http_log_tag->>>","Succes in Http Connection");
							
					} catch (UnsupportedEncodingException e) {
						
						Log.e("<<<- httpConnection API CALL ->>>","Error in UnsupportedEncodingException : " + e.toString());
						
					} catch (ClientProtocolException e) {
						
						Log.e ("<<<- httpConnection API CALL ->>>", "Error in ClientProtocolException : " + e.toString());
						
					} catch (IllegalStateException e) {
						
						Log.e("<<<- httpConnection API CALL ->>>", "Error in IllegalStateException : " + e.toString());
						
					} catch (IOException e) {
						
						Log.e("<<<- httpConnection API CALL ->>>", "Error in IOException : " + e.toString());
						
					}//end of exception handling
			
				
				Log.d("<<<- http_log_tag ->>>","Succes in Http Connection");
				
		}catch(Exception e){
			
			Log.e("<<<- http_log_tag ->>>","Error in Http Connection" + e.toString());
			
		}//end of exception handling
		
		return Res;
		
	}//end of httpConnection()	
	
	
	//testing for wishmagnet
	public String httpConnectionMYAPI(){
		
		String Res = null;
		try{
				
				HttpClient httpClient = new DefaultHttpClient();
			
				//HttpPost httpPost = new HttpPost("http://10.0.2.2/androidwishmagnet/android_latest_intention.php");
				
				HttpPost httpPost = new HttpPost("http://wishmagnet.com/androidwishmagnet/android_latest_intention.php");
				
				//httpPost.setEntity(new UrlEncodedFormEntity());
				
				HttpResponse response;
				
				try {
						response = httpClient.execute(httpPost);
						
						int statusCode = response.getStatusLine().getStatusCode();
						
						if(statusCode != HttpStatus.SC_OK){
							
							return null;
							
						}else{
								HttpEntity entity = response.getEntity();
								
								if(entity!= null){
								
									Res = EntityUtils.toString(entity);
								}
						}
						
						
						Log.d("<<<-http_log_tag->>>","Succes in Http Connection");
							
					} catch (ClientProtocolException e) {
							
									Log.e("<<<-ClientProtocolException http_log_tag->>>","Error in Http Connection" + e.toString());
								
					  }catch (IOException e) {
						  
									Log.e("<<<-IOException http_log_tag->>>","Error in Http Connection" + e.toString());
						  		
					  }//end of exception handling
			
				
				Log.d("<<<-http_log_tag->>>","Succes in Http Connection");
				
		}catch(Exception e){
			
			Log.e("<<<-http_log_tag->>>","Error in Http Connection" + e.toString());
			
		}//end of exception handling
		
		return Res;
		
	}//end of httpConnection()

}//end of main class