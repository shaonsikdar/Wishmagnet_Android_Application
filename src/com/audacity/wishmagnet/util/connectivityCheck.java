package com.audacity.wishmagnet.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class connectivityCheck {

	/**
	 * hasInternetConnection method is checking Internet connection
	 * if the connection can not establish then show a message to 
	 * the user for confirming him about his Internet connection
	 * @return true if Internet is available in device
	 * otherwise return false if there have no Internet connection in device
	 */
	public static boolean hasInternetConnection() {
    	
        	try {
        	
		            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
		            urlc.setRequestProperty("User-Agent", "Test");
		            urlc.setRequestProperty("Connection", "close");
		            urlc.setConnectTimeout(1500); 
		            urlc.connect();
		            return (urlc.getResponseCode() == 200);
	            
		        	}catch (MalformedURLException e) {
				
							Log.e("<<<- Exception on google ->>>","Exception on " +e);
							
					}catch (IOException e) {
		
		        	Log.e("<<<- Exception on hasInternetConnection ->>>"," Exception is : "+e);
            
				}// end of exception handling
        
        return false;
        
    }//end of hasInternetConnection()
	
	
}//end of connectivity check clas
