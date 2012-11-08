package your.wish.magnet;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import your.wish.magnet.tabactivity.LatestIntensions;
import your.wish.magnet.user.UserInfoActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.audacity.wishmagnet.model.HomeModel;
import com.audacity.wishmagnet.util.connectivityCheck;
import com.audacity.wishmagnet.util.httpClient;

public class HomeActivity extends SherlockFragment {

	EditText etSet;
	Button btnSet;
	ListView lvHome;

	ProgressDialog progressDialog;
	httpClient http = new httpClient();
	String response;

	ArrayList<NameValuePair> nvpLatestIntention;
	ArrayList<HomeModel> alData = new ArrayList<HomeModel>();
	
	UserInfoActivity userName = new UserInfoActivity();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.home_layout, container, false);
		
		initView(view);

		new FetchData().execute();
		
		return view;
		
	}//end of onCreateView()

	/**
	 * initView is doing the task of initialize the layout component first then
	 * show data into list view which will be get from the server
	 */

	private void initView(View view) {

		
		lvHome = (ListView) view.findViewById(R.id.listView_home);

		lvHome.setAdapter(new HomeListAdapter(getActivity().getBaseContext(), R.layout.home_layout,alData)); // add alData arrayList into List View named lvHome

		/**
		 * Below code is responsible for the task What will happen if lvHome
		 * list View press , those code has been written in the below
		 */
		lvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {

				Toast.makeText(getActivity().getBaseContext(), "Item-" + arg2 + " clicked!",1000).show();

			}//end of onItemClick
			
		});// end of lvHome event handler

	}// end of initView()

	/**
	 * HomeListAdapter class is responsible for showing data into List View in
	 * the Activity
	 * 
	 * @author Shaon
	 * 
	 */
	private class HomeListAdapter extends ArrayAdapter<HomeModel> {

		Context context;
		LayoutInflater inflater;
		ArrayList<HomeModel> alData;

		/**
		 * HomeListAdapter method is for initialize the context , into proper
		 * textView from ArrayList
		 * 
		 * @param context
		 *            which context will work where
		 * @param textViewResourceId
		 *            which textView will used ID of that textView
		 * @param data
		 *            is ArrayList: here HomeModel class data have been used for
		 *            this ArrayList
		 */
		public HomeListAdapter(Context context, int textViewResourceId,ArrayList<HomeModel> data) {
			super(context, textViewResourceId, data);

			this.context = context;
			this.alData = data;
			this.inflater = getActivity().getLayoutInflater().from(context);

		}// end of HomeListAdapter method

		@Override
		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		public View getView(int position, View convertView, ViewGroup parent) {

			View row;

			if (convertView == null) {

				row = inflater.inflate(R.layout.list_item_home, parent, false);

			}else {

				row = convertView;

			}// end of if else condition

			TextView tvPeople = (TextView) row.findViewById(R.id.textView_people);
			tvPeople.setText(alData.get(position).people);
			// tvPeople.setTag(position);

			TextView tvSeconds = (TextView) row.findViewById(R.id.textView_seconds);
			tvSeconds.setText(alData.get(position).seconds);
			// tvSeconds.setTag(position);

			TextView tvTitle = (TextView) row.findViewById(R.id.textView_title);
			tvTitle.setText(alData.get(position).title);
			tvTitle.setTag(position);
			tvTitle.setOnClickListener(titleListener);

			TextView tvDateTime = (TextView) row.findViewById(R.id.textView_dateTime);
			tvDateTime.setText(alData.get(position).dateTime);
			// tvDateTime.setTag(position);

//			Post by
//			TextView tvCategory = (TextView) row.findViewById(R.id.textView_category);
//			tvCategory.setText(alData.get(position).category);
//			tvCategory.setTag(position);
//			tvCategory.setOnClickListener(categoryListener);
			
			
			//String postedBy = userName.gettingUserInfoFromServerPostBy(alData.get(position).postedBy);

			TextView tvPostedBy = (TextView) row.findViewById(R.id.textView_postedBy);
			tvPostedBy.setText("User # " + alData.get(position).postedBy);
			//tvPostedBy.setText("User # " + postedBy);
			tvPostedBy.setTag(position);
			tvPostedBy.setOnClickListener(postedByListener);

			TextView tvComment = (TextView) row.findViewById(R.id.textView_comment);
			tvComment.setText(alData.get(position).comment+" Comments");
			tvComment.setTag(position);
			tvComment.setOnClickListener(commentListener);

			TextView tvDetails = (TextView) row.findViewById(R.id.textView_details);
			tvDetails.setText(alData.get(position).details);
			// tvDetails.setTag(position);

			return row;
			
		}// end of View getView()

		private OnClickListener titleListener = new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Toast.makeText(context, "Title-" + arg0.getTag(), 1000).show();

			}// end of onClickListener()

		};// end of onClickListener of titleListener

		private OnClickListener categoryListener = new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Toast.makeText(context, "Category-" + arg0.getTag(), 1000)
						.show();

			}// end of onClick()

		};// end of onClickListener categoryListener

		private OnClickListener postedByListener = new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Toast.makeText(context, "Posted By-" + arg0.getTag(), 1000).show();

			}// end of onClick()

		};// end of OnClickListener postedByListener

		private OnClickListener commentListener = new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Toast.makeText(context, "Comment-" + arg0.getTag(), 1000)
						.show();

			}// end of onClick()

		};// end of OnClickListener of commentListener
	}

	public void getServerResponse() {

		nvpLatestIntention = new ArrayList<NameValuePair>();
		nvpLatestIntention.add(new BasicNameValuePair("tag", "wishes"));

		response = http.httpConnection(nvpLatestIntention);
		Log.i("Home-Response:", response);
		// response = http.httpConnection();
		parseResponse(response);
		
	}//end of getServerResponse()

	/**
	 *
	 * @param jsonString
	 */
	private void parseResponse(String jsonString) {

		JSONObject jsonObject;

		try {
			jsonObject = new JSONObject(jsonString);

			String success = jsonObject.getString("success");

			if (success.equalsIgnoreCase("1")) {

				JSONArray posts = jsonObject.getJSONArray("posts");

				for (int i = 0; i < posts.length(); i++) {

					HomeModel homeIntension = new HomeModel();

					homeIntension.people = posts.getJSONObject(i).getString(
							"meditators");
					homeIntension.seconds = posts.getJSONObject(i).getString(
							"totalmedittime");
					homeIntension.title = posts.getJSONObject(i).getString(
							"post_title");
					homeIntension.dateTime = posts.getJSONObject(i).getString(
							"post_date");
					homeIntension.category = posts.getJSONObject(i).getString(
							"post_type");
					homeIntension.postedBy = posts.getJSONObject(i).getString(
							"post_author");
					homeIntension.comment = posts.getJSONObject(i).getString(
							"comment_count");

					alData.add(homeIntension);
					
				}// end of for loop

			}// end of if condition
		
		} catch (JSONException e) {

			Log.e("parseResponse JSONException :", e.getLocalizedMessage());

		}// end of exception handling

	}// end of parseResponse()

	/**
	 * FetchData Class is used for fetching Data in Asynchronous approach
	 * 
	 * @author Shaon
	 * 
	 */
	private class FetchData extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {

			progressDialog = ProgressDialog.show(getActivity(), "Loading","Fetching data, Please wait...");

		}// end of onPreExecute()

		@Override
		protected String doInBackground(Void... params) {

			if (connectivityCheck.hasInternetConnection()) {

				getServerResponse();

			} else {

				return "No internet connection!";

			}// end of if else condition

			return "0";

		}// end of doInBackground

		@Override
		protected void onPostExecute(String msg) {

			progressDialog.dismiss();

			if (!alData.isEmpty()) {

				lvHome.setAdapter(new HomeListAdapter(getActivity().getBaseContext(),
						R.layout.latest_intensions, alData));

			} else {

				Toast.makeText(getActivity().getBaseContext(),
						"Error in downloading data, Please try again", 1000)
						.show();

			}// end of if else condition

		}// end of onPostExecute()

	}// end of class FetchData

}// end of main class