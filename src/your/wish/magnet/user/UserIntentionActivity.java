package your.wish.magnet.user;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import your.wish.magnet.R;
import your.wish.magnet.R.layout;

import com.audacity.wishmagnet.model.UserIntentionModel;
import com.audacity.wishmagnet.util.connectivityCheck;
import com.audacity.wishmagnet.util.httpClient;

import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Shaon
 *
 */
public class UserIntentionActivity extends Activity {

	String response;
	httpClient httpClientUserWishes= new httpClient();
	ArrayList<NameValuePair> nvpUserWishes;
	ProgressDialog progressDialog;
	String userWishesID="1";
	
	TextView tvUsername;
	ListView lvUserIntention;
	
	ArrayList<UserIntentionModel> alData = new ArrayList<UserIntentionModel>();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_intentions);
     
        new DownloadUserWishes().execute();
        
        initializeView();
        
   }// end of onCreate()
	
	private void initializeView(){
		
		lvUserIntention = (ListView) findViewById(R.id.listViewUserIntention);
		
		
	}//end of initializeView()
	
	/**
	 * Requesting to server for getting latest intention from server
	 */
	public void gettingUserWishesFromServer(final String userid){
		
			try {
					
						nvpUserWishes = new ArrayList<NameValuePair>();
						nvpUserWishes.add(new BasicNameValuePair("tag", "userwishes"));
						nvpUserWishes.add(new BasicNameValuePair("userid", userid));
						
						response = httpClientUserWishes.httpConnection(nvpUserWishes);
						
						Log.i("Data","User Wishes:" + response);
						parseResponse(response);
									
		} catch (Exception e) {
				
			Log.e("<<<- UserInfoActivity on gettingUserWishesFromServer ->>>","Exception is : " + e);
			
		}//end of exception handling
		
	}//end of gettingIntentionFromServer()
	
	private void parseResponse(String jsonString) {

		JSONObject jsonObject;

		try {
			jsonObject = new JSONObject(jsonString);

			String success = jsonObject.getString("success");

			if (success.equalsIgnoreCase("1")) {

				JSONArray users = jsonObject.getJSONArray("posts");
				//JSONArray newArray = users.getJSONArray(0);

				for (int i = 0; i < users.length(); i++) {
//
//					String  postTitle= users.getJSONObject(i)
//							.getString("post_title");
					
					UserIntentionModel userIntentionModel = new UserIntentionModel();
					
					userIntentionModel.intention = users.getJSONObject(i).getString("post_title");
					
					Log.i("Post Title User","Post"+users.getJSONObject(i).getString("post_title"));

					alData.add(userIntentionModel);

				}// end of for loop

			}// end of if condition

		} catch (JSONException e) {

			Log.e("User Wishes parseResponse JSONException :", "Exception on :" + e);

		}// end of exception handling

	}// end of parseResponse()
	
	
	private class UserIntentionListAdapter extends ArrayAdapter<UserIntentionModel> {

		LayoutInflater inflater;
		ArrayList<UserIntentionModel> mAlData;
		
		public UserIntentionListAdapter(Context context,
				int textViewResourceId, ArrayList<UserIntentionModel> data) {

			super(context, textViewResourceId, data);
			this.mAlData = data;
			this.inflater = UserIntentionActivity.this.getLayoutInflater().from(context);

		}// end of LatestIntenstionListAdapter()

		/**
		 * getView method are for Viewing the data
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View row;

			if (convertView == null) {

				row = inflater.inflate(R.layout.list_user_intention, parent,false);

			}

			else {

				row = convertView;

			}// end of if else condition
			
//			TextView tvUserIntention = (TextView) row.findViewById(R.id.textViewUserIntentionListItem) ;
			
//			tvUserIntention.setText(mAlData.get(position).intention);
			// tvPeople.setTag(position);
			
			return row;

		}// end of View getView()

	}// end of LatestIntensionsListAdapter Class
	
	
	private class DownloadUserWishes extends AsyncTask<Void,Void,String>{
		@Override
		protected void onPreExecute() {

			progressDialog = ProgressDialog.show(UserIntentionActivity.this, "Loading",
					"Fetching data, Please wait...");

		}// end of onPreExecute()

		@Override
		protected String doInBackground(Void... params) {

			if (connectivityCheck.hasInternetConnection()) {

				gettingUserWishesFromServer("9");

			} else {

				return "No internet connection!";

			}// end of if else condition

			return "0";

		}// end of doInBackground

		@Override
		protected void onPostExecute(String msg) {

			progressDialog.dismiss();

//			if (!alData.isEmpty()) {
//
//				lvUserIntention.setAdapter(new UserIntentionListAdapter(UserIntentionActivity.this, R.layout.user_intentions, alData));
//
//			} else {
//
//				Toast.makeText(UserIntentionActivity.this.getBaseContext(),
//						"Error in downloading data, Please try again", 1000).show();
//
//			}// end of if else condition

		}// end of onPostExecute()
		
	}//end of DownloadUserWishes
	
	
}//end of main class