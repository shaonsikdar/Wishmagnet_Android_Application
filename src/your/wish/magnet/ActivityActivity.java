/**
 * 
 */
package your.wish.magnet;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragment;
import com.audacity.wishmagnet.model.ActivityModel;
import com.audacity.wishmagnet.util.connectivityCheck;
import com.audacity.wishmagnet.util.httpClient;

import android.app.Activity;
import android.app.Fragment;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Shaon
 */
public class ActivityActivity extends SherlockFragment {

	//Spinner for getting the item of all member type like everone, update one, post, comment , friendship and newest user.
	Spinner allMemberSpinner; 
	String response;
	httpClient httpClientActivity = new httpClient();
	ArrayList<NameValuePair> nvpActivity;
	ArrayList<ActivityModel> alData = new ArrayList<ActivityModel>();
	String recentActivityURL= "http://wishmagnet.com/api/buddypressread/get_activities/?";
	ProgressDialog progressDialog;
	ListView activityListView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity, container, false);

		//	View view = inflater.inflate(R.layout.image_slideshow_test, container, false);
		initializeView(view);
		
		new DownloadMembersData().execute();
		
		return view;
		
	}//end of onCreateView
	
	// Below things comment for checking imageSlideShow
	
	/**
	 * 
	 * @param view
	 */
	private void initializeView(View view){
		
		allMemberSpinner = (Spinner) view.findViewById(R.id.act_show_spinner);
		//Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapterMember = ArrayAdapter.createFromResource(getActivity(), R.array.activity_show, android.R.layout.simple_spinner_item);
		//Specify the layout to use when the list of choices apprers
		adapterMember.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//apply the adapter to the spinner
		allMemberSpinner.setAdapter(adapterMember);
		
		activityListView = (ListView) view.findViewById(R.id.listViewActivity); 
		
		ActivityFromServer();
		
	}//end of initializeView()
	
	/**
	 * 
	 * @param parent
	 * @param v
	 * @param pos
	 * @param id
	 */
	public void onItemSelected(AdapterView<?> parent, View v, int pos, long id){
		
			//An Item was select. You can retrieves the selected item using parent.getItemAtPosition(pos)
		
	}//end of onItemSelected
	
	
	/**
	 * Getting Activity of WISHMAGNET from server
	 */
	public void ActivityFromServer(){
				
			try {
						
							nvpActivity = new ArrayList<NameValuePair>();
							nvpActivity.add(new BasicNameValuePair("dev", "1"));
							nvpActivity.add(new BasicNameValuePair("pages","4"));
							nvpActivity.add(new BasicNameValuePair("offset", "3"));
							nvpActivity.add(new BasicNameValuePair("limit", "10"));
							nvpActivity.add(new BasicNameValuePair("sort", "ASC"));
							//nvpActivity.add(new BasicNameValuePair("userid", "1"));
							nvpActivity.add(new BasicNameValuePair("component", "blogs"));
							nvpActivity.add(new BasicNameValuePair("type", "activity_update"));
							
							response = httpClientActivity.httpConnectionWithURL(recentActivityURL,nvpActivity);
							
							parseResponse(response);
							
						
			} catch (Exception e) {
					
				Log.e("<<<- Exception on UserInfoActivity Getting Intention From Server ->>>"," UserInfoActivity Exception name : " + e);
				
			}//end of exception handling
		
		}//end of RecentCommentFromServer()
	
	private void parseResponse(String jsonString) {

		JSONObject jsonObject;

		try {
			jsonObject = new JSONObject(jsonString);

			String success = jsonObject.getString("status");

			if (success.equalsIgnoreCase("ok")) {

				JSONArray activityArray = jsonObject.getJSONArray("users");
//		        JSONArray newArray = users.getJSONArray(0);

				for (int i = 0; i < activityArray.length(); i++) {

					ActivityModel activeModel = new ActivityModel();
					
					activeModel.username = activityArray.getJSONObject(i).getString("display_name");
					activeModel.post = activityArray.getJSONObject(i).getString("last_activity");

					alData.add(activeModel);

				}// end of for loop

			}// end of if condition

		} catch (JSONException e) {

			Log.e("parseResponse JSONException :", "Exception on :" + e);

		}// end of exception handling

	}// end of parseResponse()
	
	
	
	private class ActivityListAdapter extends ArrayAdapter<ActivityModel> {

		LayoutInflater inflater;
		ArrayList<ActivityModel> mAlData;

		/**
		 * LatestIntentensionListAdapter method task is to show data from
		 * arrayList into List View
		 * 
		 * @param context
		 *            which context have to use
		 * @param textViewResourceId
		 *            textView's ID which have to use inside the method
		 * @param data
		 *            arrayList of LatestIntentionModel
		 */
		public ActivityListAdapter(Context context,int textViewResourceId, ArrayList<ActivityModel> data) {

			super(context, textViewResourceId, data);
			this.mAlData = data;
			this.inflater = getActivity().getLayoutInflater().from(context);

		}// end of LatestIntenstionListAdapter()

		/**
		 * getView method are for Viewing the data
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View row;

			if (convertView == null) {

				row = inflater.inflate(R.layout.list_item_activity, parent,false);

			}

			else {

				row = convertView;

			}// end of if else condition

			TextView tvUsername = (TextView) row.findViewById(R.id.textViewMemberUsername);
			tvUsername.setText(mAlData.get(position).username);
			// tvPeople.setTag(position);

			TextView tvPost = (TextView) row.findViewById(R.id.textViewMemberActive);
			tvPost.setText(mAlData.get(position).post);
			
			TextView tvLike = (TextView) row.findViewById(R.id.textViewMemberActive);
			tvLike.setText(mAlData.get(position).post);
			// tvSeconds.setTag(position);
			
			return row;

		}// end of View getView()

	}// end of LatestIntensionsListAdapter Class
	
	private class DownloadMembersData extends AsyncTask<Void, Void, String> {

		protected void onPreExecute() {

			progressDialog = ProgressDialog.show(getActivity(), "Loading",
					"Fetching data, Please wait...");

		}// end of onPreExecute()

		@Override
		protected String doInBackground(Void... params) {

			if (connectivityCheck.hasInternetConnection()) {

				ActivityFromServer();

			} else {

				return "No internet connection!";

			}// end of if else condition

			return "0";

		}// end of doInBackground

		@Override
		protected void onPostExecute(String msg) {

			progressDialog.dismiss();

			if (!alData.isEmpty()) {

				activityListView.setAdapter(new ActivityListAdapter(getActivity(), R.layout.activity, alData));

			} else {

//				Toast.makeText(getActivity().getBaseContext(),
//						"Error in downloading data, Please try again", 1000).show();
				
				Toast.makeText(getActivity().getBaseContext(),
						"Server side is in processing! Will live soon", 1000).show();
				

			}// end of if else condition

		}// end of onPostExecute()

	}// end of DownloadMembersData AsyncTask
		
		
}// end of mainClass