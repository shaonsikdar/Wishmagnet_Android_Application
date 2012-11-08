package your.wish.magnet.tabactivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import your.wish.magnet.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.audacity.wishmagnet.model.LatestIntensionsModel;
import com.audacity.wishmagnet.util.httpClient;

public class LatestIntensions extends SherlockFragment {

	ListView lvLatestIntensions;
	ProgressDialog progressDialog;
	String response;
	
	httpClient http = new httpClient();
	
	
	ArrayList<NameValuePair> nvpLatestIntention;
	
	
	//Keep all intentions information into ArrayList alData
	
	ArrayList<LatestIntensionsModel> alData = new ArrayList<LatestIntensionsModel>();	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.latest_intensions, container, false);
		
		new DownloadData().execute();
		
		lvLatestIntensions = (ListView) view.findViewById(R.id.listView_latestIntensions);
		lvLatestIntensions.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Toast.makeText(getActivity(), "Intension - " + arg2 + " clicked!", 1000).show();
			}
			
		});// end of event of LatestIntention List View component
		
		return view;
	}
	
	
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
//		setContentView(R.layout.latest_intensions);
//
//		new DownloadData().execute();
//
//		lvLatestIntensions = (ListView) findViewById(R.id.listView_latestIntensions);
//		lvLatestIntensions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//
//				Toast.makeText(getApplicationContext(), "Intension - " + arg2 + " clicked!", 1000).show();
//			}
//			
//		});// end of event of LatestIntention List View component
//		
//	}// end of onCreate()
	
	
	/**
	 * Requesting to server for getting latest intention from server
	 */
	public void gettingIntentionFromServer(){
		
		try {

					
						nvpLatestIntention = new ArrayList<NameValuePair>();
						nvpLatestIntention.add(new BasicNameValuePair("tag", "wishes"));
						
						response = http.httpConnection(nvpLatestIntention);
						Log.i("RESPONSE:", response);
						parseResponse(response);
						
					
		} catch (Exception e) {
				
			Log.e("<<< Exception on Getting Intention From Server >>>","Exception name : " + e);
			
		}//end of exception handling
		
	}//end of gettingIntentionFromServer()
	
	    
    /**
     * This method task is to maintain the response from server to show in the android application
     * @param jsonString which response is getting from server
     */
    private void parseResponse(String jsonString) {
    	
    	JSONObject jsonObject;
    	
		try {
				jsonObject = new JSONObject(jsonString);
				
				String success = jsonObject.getString("success");

						if(success.equalsIgnoreCase("1")) {
							
							JSONArray posts =	jsonObject.getJSONArray("posts");
							
							for(int i=0; i<posts.length(); i++) {
								
									LatestIntensionsModel latestIntension = new LatestIntensionsModel();
									
									latestIntension.people = posts.getJSONObject(i).getString("meditators");
									latestIntension.seconds = posts.getJSONObject(i).getString("totalmedittime");
									latestIntension.title = posts.getJSONObject(i).getString("post_title");
									latestIntension.user = posts.getJSONObject(i).getString("post_author");
									
									
									alData.add(latestIntension);
																								
							}//end of for loop
							
						}//end of if condition
			
						
		}catch (JSONException e) {
			
			Log.e("<<<- LatestIntension Class parseResponse :", e.getLocalizedMessage());
			
		}//end of exception handling
    	
    }//end of parseResponse()
	
    
    /**
     * LatestIntentionListAdapter extends ArrayAdapter from where data of latest intention from server is showing in text view 
     * from here after here
     * @author Shaon
     *
     */
	private class LatestIntensionsListAdapter extends ArrayAdapter<LatestIntensionsModel> {
	
		LayoutInflater inflater;	
		ArrayList<LatestIntensionsModel> alData;

		/**
		 * LatestIntentensionListAdapter method task is to show data from arrayList into List View
		 * @param context which context have to use
		 * @param textViewResourceId textView's ID which have to use inside the method
		 * @param data arrayList of LatestIntentionModel 
		 */
		public LatestIntensionsListAdapter(Context context, int textViewResourceId, ArrayList<LatestIntensionsModel> data) {

			super(context, textViewResourceId, data);
			this.alData = data;
			this.inflater = getActivity().getLayoutInflater().from(context);
			
			
		}//end of LatestIntenstionListAdapter()
		
		/**
		 * getView method are for Viewing the data
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View row;
			
			if(convertView == null) {
			
				row = inflater.inflate(R.layout.list_item_latest, parent, false);
				
			}
			else {
				
				row = convertView;
				
			}//end of if else condition 
			
			TextView tvPeople = (TextView) row.findViewById(R.id.textView_people);
			tvPeople.setText(alData.get(position).people + " people");
//			tvPeople.setTag(position);
			
			TextView tvSeconds = (TextView) row.findViewById(R.id.textView_seconds);
			tvSeconds.setText(alData.get(position).seconds);
//			tvSeconds.setTag(position);
			
			TextView tvTitle = (TextView) row.findViewById(R.id.textView_title);
			tvTitle.setText(alData.get(position).title);
			tvTitle.setTag(position);
//			tvTitle.setOnClickListener(titleListener);
			
			TextView tvUser = (TextView) row.findViewById(R.id.textView_user);
			tvUser.setText("User Id# " + alData.get(position).user);
//			tvDateTime.setTag(position);

			return row;
			
		}//end of View getView()
		
	}// end of LatestIntensionsListAdapter Class
	
	private class DownloadData extends AsyncTask<Void, Void, String> {

    	@Override
    	protected void onPreExecute() {
    	
    		progressDialog = ProgressDialog.show(getActivity(), "Loading", "Fetching data, Please wait...");
    		
    	}//end of onPreExecute()
    	
    	@Override
		protected String doInBackground(Void... params) {

    		if(hasInternetConnection()) {
				
				gettingIntentionFromServer();
				
			}else {
				
				return "No internet connection!";
				
			}//end of if else condition
			
			return "0";
			
		}//end of doInBackground
    	
    	@Override
    	protected void onPostExecute(String msg) {
    		Log.i("Post Execute of Latest Intensions", "");
    		
    		progressDialog.dismiss();
    		
    		if(!alData.isEmpty()) {
    			
    			lvLatestIntensions.setAdapter(new LatestIntensionsListAdapter(getActivity(), R.layout.latest_intensions, alData));
    			
    		}else {
    			
    			Toast.makeText(getActivity().getBaseContext(), "Error in downloading data, Please try again",  1000).show();
    			
    		}//end of if else condition
    		
    	}// end of onPostExecute()
    	
    }//end of DownloadData AsyncTask


	/**
	 * hasInternetConnection method is checking Internet connection
	 * if the connection can not establish then show a message to 
	 * the user for confirming him about his Internet connection
	 * @return true if Internet is available in device
	 * otherwise return false if there have no internet connection in device
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
			
						Log.e("Exception on google","Exception on " +e);
						
				}catch (IOException e) {
	
	        	Log.e("<<<- Exception on hasInternetConnection->>>"," Exception is : "+e);
            
        }// end of exception handling
        
        return false;
        
    }//end of hasInternetConnection()

}//end of main class