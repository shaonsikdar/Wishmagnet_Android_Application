package your.wish.magnet.tabactivity;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import your.wish.magnet.R;
import your.wish.magnet.R.id;
import your.wish.magnet.R.layout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import com.audacity.wishmagnet.model.LatestIntensionsModel;
import com.audacity.wishmagnet.util.httpClient;

public class LatestIntensionsNew extends Activity {

	ListView lvLatestIntensions;
	ProgressDialog progresDialog;
	String response;
	
	httpClient http = new httpClient();
	
	ArrayList<NameValuePair> nvpLatestIntention;
	
	
	//Keep all intentions information into ArrayList alData
	
	ArrayList<LatestIntensionsModel> alData = new ArrayList<LatestIntensionsModel>();	
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.latest_intensions);
		
		gettingIntentionFromServer(); // Calling Server for getting the data from server Latest 
		
		
		/**
		 * Fetching all data about intention from ArrayList
		 */
//		for(int i=1; i<=10; i++) {
//			
//			LatestIntensionsModel latestIntension = new LatestIntensionsModel();
//			
//			latestIntension.people = i + " people";
//			latestIntension.seconds = i + " seconds";
//			latestIntension.title = "Title-" + i;
//			latestIntension.user = "User_" + i;
//			
//			alData.add(latestIntension);
//			
//		}// end of for loop 
		
		lvLatestIntensions = (ListView) findViewById(R.id.listView_latestIntensions);
		
		lvLatestIntensions.setAdapter(new LatestIntensionsListAdapter(this, R.layout.latest_intensions, alData));
		
		lvLatestIntensions.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Toast.makeText(getApplicationContext(), "Intension - " + arg2 + " clicked!", 1000).show();
			}
			
		});// end of event of LatestIntention List View component
		
	}// end of onCreate()
	
	
	/**
	 * Requesting to server for getting latest intention from server
	 */
	public void gettingIntentionFromServer(){
		
		try {
				//progresDialog = ProgressDialog.show(getApplicationContext(), "Loading", "Wait! Latest Intention Loading....", true, false);

				//Creating A thread for processing login faster
				Thread intentionThread = new Thread(){
					
					public void run(){
					
						nvpLatestIntention = new ArrayList<NameValuePair>();
						nvpLatestIntention.add(new BasicNameValuePair("tag", "wishes"));
						
						response = http.httpConnection(nvpLatestIntention);
//						response = http.httpConnection();
						parseResponse(response);
												
					}//end of run()
					
				};//end of intentionThread
				
				//Yet the thread have not start. So thread have to start to execute further task
				intentionThread.start();
					
		} catch (Exception e) {
				
			Log.e("<<< Exception on Getting Intention From Server >>>","Exception name : " + e);
			
		}//end of exception handling
		
	}//end of gettingIntentionFromServer()
	
	    
    // Added bu Faisal 10-10-12
    private void parseResponse(String jsonString) {
    	
    	JSONObject jsonObject;
    	
		try {
			jsonObject = new JSONObject(jsonString);
			
			String success = jsonObject.getString("success");
			if(success.equalsIgnoreCase("1")) {
				
				JSONArray posts =	jsonObject.getJSONArray("posts");
				
				for(int i=0; i<posts.length(); i++) {
					
//					LatestIntensionsModel latestIntension = new LatestIntensionsModel();
//					
//					latestIntension.people = posts.getJSONObject(i).getString("meditators");
//					latestIntension.seconds = posts.getJSONObject(i).getString("totalmedittime");
//					latestIntension.title = posts.getJSONObject(i).getString("post_title");
//					latestIntension.user = posts.getJSONObject(i).getString("post_author");
//					
//					alData.add(latestIntension);
					
					Log.i("meditators-", posts.getJSONObject(i).getString("meditators"));
					Log.i("totalmedittime-", posts.getJSONObject(i).getString("totalmedittime"));
					Log.i("post_title-", posts.getJSONObject(i).getString("post_title"));
					Log.i("post_author-", posts.getJSONObject(i).getString("post_author"));
				}
			}

		}
		catch (JSONException e) {
			
			Log.e("JSONException:", e.getLocalizedMessage());
			
		}
    	
    }
	
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
			this.inflater = getLayoutInflater().from(context);
			
		}//end of LatestIntenstionListAdapter()
		
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
			tvPeople.setText(alData.get(position).people);
//			tvPeople.setTag(position);
			
			TextView tvSeconds = (TextView) row.findViewById(R.id.textView_seconds);
			tvSeconds.setText(alData.get(position).seconds);
//			tvSeconds.setTag(position);
			
			TextView tvTitle = (TextView) row.findViewById(R.id.textView_title);
			tvTitle.setText(alData.get(position).title);
			tvTitle.setTag(position);
//			tvTitle.setOnClickListener(titleListener);
			
			TextView tvUser = (TextView) row.findViewById(R.id.textView_user);
			tvUser.setText(alData.get(position).user);
//			tvDateTime.setTag(position);

			return row;
			
		}//end of View getView()
		
//		private OnClickListener titleListener = new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Toast.makeText(context, "Title-" + arg0.getTag(), 1000).show();
//			}
//		};
//		
//		private OnClickListener categoryListener = new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Toast.makeText(context, "Category-" + arg0.getTag(), 1000).show();
//			}
//		};
//		
//		private OnClickListener postedByListener = new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Toast.makeText(context, "Posted By-" + arg0.getTag(), 1000).show();
//			}
//		};
//		
//		private OnClickListener commentListener = new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Toast.makeText(context, "Comment-" + arg0.getTag(), 1000).show();
//			}
//		};
	
	}// end of LatestIntensionsListAdapter Class

}//end of main class