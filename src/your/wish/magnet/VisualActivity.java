/**
 * 
 */
package your.wish.magnet;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.audacity.wishmagnet.util.httpClient;

/**
 * @author Shaon
 *
 */
public class VisualActivity extends SherlockFragment {
	
	String responseVisual;
	httpClient httpClientVisual = new httpClient();
	ArrayList<NameValuePair> nvpVisual;
	String visualActivityURL = "http://wishmagnet.com/api/get_page/?";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.visualization, container, false);
		
		VisualFromServer();
		
		return view;
	}
	
//	public void onCreate(Bundle savedInstanceState) {
//        
//		super.onCreate(savedInstanceState);
//        setContentView(R.layout.visualization);
//        
//        initializieView();
//    
//   }// end of onCreate()
//	
//	
//	public void initializieView(){
//		
//		
//		VisualFromServer();
//		
//	}//end of initializeView()
	
	public void VisualFromServer(){

				try {
		
					//Creating A thread for processing  faster
					Thread intentionThread = new Thread(){
						
						public void run(){
						
							nvpVisual = new ArrayList<NameValuePair>();
							nvpVisual.add(new BasicNameValuePair("dev", "1"));  // user read format like JSON ARRAY FORMAT or 0 as unread format
							nvpVisual.add(new BasicNameValuePair("id", "2")); //
							
							responseVisual = httpClientVisual.httpConnectionWithURL(visualActivityURL, nvpVisual);
							handler.sendEmptyMessage(0);
							
						}//end of run()
						
					};//end of intentionThread
					
					//Yet the thread have not start. So thread have to start to execute further task
					intentionThread.start();
						
			} catch (Exception e) {
					
				Log.e("<<<- Exception on VisualActivity ->>>"," Exception is : " + e);
				
			}//end of exception handling
		
	}//end of VisualFromServer
	
	

	//Create arrayList to keep the profile of the user. So to use the profile info any time.
		Handler handler = new Handler(){

			/**
			 * handleMessage method task is to handle the response for previous thread
			 * msg is the response from server 
			 */
			public void handleMessage(Message msg) {

				//progresDialog.dismiss(); //when get the data from server dismiss the progressDialog
	    		
	    		if(responseVisual !=null){
					
	    			Log.i("Visual-Response:", responseVisual);
//	    			Toast.makeText(getActivity(), responseVisual, Toast.LENGTH_LONG).show();
	    			
					
				}//end of if condition
	    		
	    		super.handleMessage(msg);
	    		
	    		
	    	}//end of handleMessage()
	    	
	    };//end of handler
    
}// end of mainClass