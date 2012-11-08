/**
 * 
 */
package com.audacity.wishmagnet.util;

import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Shaon
 *
 */

public class serverActivity extends Activity {
	
	ArrayList<NameValuePair> nameValuePairs;
	String responseFromServer;

	httpClient http = new httpClient();//object of httpClient
	

	/**
	 * From join page username, email, password is sent here for processing/sending the data to the server
	 * 
	 * for checking is there have exist user by username or email address
	 * 
	 * @param un username of user written in join page 
	 * @param em email address of user written in join page
	 * @param pass password of user written in join pagev
	 * @param stat status of user written in join page
	 */
	public String registrationIntoServer(String un,String em, String pass){
		
		try {
				nameValuePairs = new ArrayList<NameValuePair>();
				
				nameValuePairs.add(new BasicNameValuePair("tag", "wishes"));
//				nameValuePairs.add(new BasicNameValuePair("username",un));
//				nameValuePairs.add(new BasicNameValuePair("email",em));
//				nameValuePairs.add(new BasicNameValuePair("password",pass));
				
				Thread regThread = new Thread(){
					
					public void run(){
							
						responseFromServer = http.httpConnection(nameValuePairs); // passing nameValuePairs into server with encrypted username,email,password into nameValuePairs ArrayList
						handler.sendEmptyMessage(0);
						
					}//end of void run()
					
				};//end of userThread
				
				regThread.start();
				
				Log.d("<--RegistrationServer-->","Success in RegistrationServer");
			
		} catch (Exception e) {
			
			Log.e("<--RegistrationServer-->","Error in RegistrationServer" + e.toString());
			
		}//end of exception handling 
		
		return responseFromServer;
		
	}//end of registrationIntoSever method
	
	
	//Create arrayList to keep the profile of the user. So to use the profile info any time.
	Handler handler = new Handler(){

		/**
		 * handleMessage method task is to handle the response for previous thread
		 * msg is the response from server 
		 */
		public void handleMessage(Message msg) {

			//progresDialog.dismiss(); //when get the data from server dismiss the progressDialog
    		
    		if(responseFromServer !=null){
				
    			
    			Toast.makeText(getApplicationContext(), responseFromServer, Toast.LENGTH_LONG).show();
    			
				
			}//end of if condition
    		
    		super.handleMessage(msg);
    		
    		
    	}//end of handleMessage()
    	
    };//end of handler
	
}//end of main class