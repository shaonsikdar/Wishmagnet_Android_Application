package your.wish.magnet.tabactivity;

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
import com.audacity.wishmagnet.model.RecentCommentsModel;
import com.audacity.wishmagnet.util.connectivityCheck;
import com.audacity.wishmagnet.util.httpClient;

public class RecentCommentsActivity extends SherlockFragment {

	ListView listViewRecentComments;
	ProgressDialog progressDialogRecentComments;

	String response;
	httpClient httpClientRecentComment = new httpClient();
	ArrayList<NameValuePair> nvpRecentComment;
	ArrayList<RecentCommentsModel> recentCommentData = new ArrayList<RecentCommentsModel>();

	String recentCommentURL = "http://wishmagnet.com/api/buddypressread/get_activities/?";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.recentcomments, container, false);

		initializeView(view);

		return view;
	}

	// public void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	// setContentView(R.layout.recentcomments);
	//
	// initializeView();
	//
	// }// end of onCreate()

	private void initializeView(View view) {

		// RecentCommentFromServer();
		new recentCommentDownloadData().execute();

		listViewRecentComments = (ListView) view
				.findViewById(R.id.listViewRecentComments);
		listViewRecentComments
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						Toast.makeText(getActivity().getBaseContext(),
								"Post - " + arg2 + "Clicked", 1000).show();

					}// end of onItemClick()

				});// end of event listener of listViewRecentComments

	}// end of initializeView()

	/**
	 * 
	 */
	public void RecentCommentFromServer() {

		try {

			nvpRecentComment = new ArrayList<NameValuePair>();
			nvpRecentComment.add(new BasicNameValuePair("component", "blogs"));
			// nvpUserInfo.add(new BasicNameValuePair("userid", "1"));

			response = httpClientRecentComment.httpConnectionWithURL(
					recentCommentURL, nvpRecentComment);
			Log.i("Recent Comment JSON", response);
			parseResponse(response);

		} catch (Exception e) {

			Log.e("<<< RecentCommentActivity Exception on  >>>",
					" Exception is : " + e);

		}// end of exception handling

	}// end of RecentCommentFromServer()

	/**
	 * 
	 * @param jsonString
	 */
	private void parseResponse(String jsonString) {

		JSONObject jsonObject;

		try {

			jsonObject = new JSONObject(jsonString);

			String status = jsonObject.getString("status");

			if (status.equalsIgnoreCase("ok")) {

				JSONArray commenst = jsonObject.getJSONArray("activities");
				JSONArray newArray = commenst.getJSONArray(0);

				for (int i = 0; i < newArray.length(); i++) {

					RecentCommentsModel recentComments = new RecentCommentsModel();

					recentComments.username = newArray
							.getJSONObject(i).getString("display_name");
					recentComments.post = newArray.getJSONObject(i)
							.getString("primary_link");

					recentCommentData.add(recentComments);

				}// end of for loop

			}// end of is condition

		} catch (JSONException e) {

			Log.e("Exception on parseResponse", "Exception is :" + e);
		}// end of exception handling

	}// end of parseResponse()

	/**
	 * 
	 * @author Shaon
	 *
	 */
	private class RecentCommentsListAdapter extends
			ArrayAdapter<RecentCommentsModel> {

		LayoutInflater inflatorComment;
		ArrayList<RecentCommentsModel> recentCommentModelData;

		/**
		 * 
		 * @param context
		 * @param textViewResourceId
		 * @param commentData
		 */
		public RecentCommentsListAdapter(Context context,
				int textViewResourceId,
				ArrayList<RecentCommentsModel> commentData) {
			super(context, textViewResourceId, commentData);

			this.recentCommentModelData = commentData;
			this.inflatorComment = getActivity().getLayoutInflater().from(
					context);

		}// end of RecentCommentsListAdapter constructor

		/**
		 * 
		 */
		public View getView(int position, View convertView, ViewGroup parent) {

			View rowComment;

			if (convertView == null) {

				rowComment = inflatorComment.inflate(
						R.layout.list_item_comments, parent, false);

			} else {

				rowComment = convertView;

			}// end of if else condition

			TextView username = (TextView) rowComment
					.findViewById(R.id.textViewCommentUsername);
			username.setText(recentCommentModelData.get(position).username + " on ");

			TextView post = (TextView) rowComment
					.findViewById(R.id.textViewCommentOnPost);
			post.setText(recentCommentModelData.get(position).post);

			return rowComment;

		}// end of getView()

	}// end of RecentCommentsListAdapter

	/**
	 * 
	 * @author Shaon
	 * 
	 */
	private class recentCommentDownloadData extends
			AsyncTask<Void, Void, String> {

		protected void onPreExecute() {

			progressDialogRecentComments = ProgressDialog.show(getActivity(),
					"Loading", "Fetching data,Please wait...");

		}// end of onPreExecute()

		@Override
		protected String doInBackground(Void... arg0) {

			
			try {
				if (connectivityCheck.hasInternetConnection()) {

					RecentCommentFromServer();

				} else {

					return "No Internet Connection";
					// Toast.makeText(getBaseContext(),
					// "Check your Internet Connection!!!", 1000).show();

				}// end of if else condition
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return e.getLocalizedMessage();
			}

			return "0";

		}// end of doInBackground()

		protected void onPostExecute(String msg) {

			progressDialogRecentComments.dismiss();

			if (!recentCommentData.isEmpty()) {

				listViewRecentComments
						.setAdapter(new RecentCommentsListAdapter(
								getActivity(), R.layout.recentcomments,
								recentCommentData));

			} else {

				Toast.makeText(getActivity().getBaseContext(),
						"Error in downloading data, Please try again.", 1000)
						.show();

			}// end of if else condition

		}// end of onPostExecute

	}// end of recentCommentDownloadData

}// end of main class