package your.wish.magnet.user;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import your.wish.magnet.R;
import your.wish.magnet.R.layout;

import com.audacity.wishmagnet.util.httpClient;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Shaon
 *
 */
public class UserWishesActivity extends Activity {

	String response;
	httpClient httpClientUserWishes= new httpClient();
	ArrayList<NameValuePair> nvpUserWishes;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userwishes);
     
        initializeView();
        
   }// end of onCreate()
	
	private void initializeView(){
		
		gettingUserWishesFromServer("1");
		
	}//end of initializeView()
	
	/**
	 * Requesting to server for getting latest intention from server
	 */
	public void gettingUserWishesFromServer(final String userid){
		
			try {

				//Creating A thread for processing  faster
				Thread intentionThread = new Thread(){
					
					public void run(){
					
						nvpUserWishes = new ArrayList<NameValuePair>();
						nvpUserWishes.add(new BasicNameValuePair("tag", "userwishes"));
					//  nvpUserInfo.add(new BasicNameValuePair("userid", "15"));
						nvpUserWishes.add(new BasicNameValuePair("userid", userid));
						
						response = httpClientUserWishes.httpConnection(nvpUserWishes);
						handler.sendEmptyMessage(0);
						
					}//end of run()
					
				};//end of intentionThread
				
				//Yet the thread have not start. So thread have to start to execute further task
				intentionThread.start();
					
		} catch (Exception e) {
				
			Log.e("<<<- UserInfoActivity on gettingUserWishesFromServer ->>>","Exception is : " + e);
			
		}//end of exception handling
		
	}//end of gettingIntentionFromServer()
	
	
	//Create arrayList to keep the profile of the user. So to use the profile info any time.
	Handler handler = new Handler(){

		/**
		 * handleMessage method task is to handle the response for previous thread
		 * msg is the response from server 
		 */
		public void handleMessage(Message msg) {

			//progresDialog.dismiss(); //when get the data from server dismiss the progressDialog
    		
    		if(response !=null){
				
    			
    			Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
    			
				
			}//end of if condition
    		
    		super.handleMessage(msg);
    		
    		
    	}//end of handleMessage()
    	
    };//end of handler
	
}//end of main class