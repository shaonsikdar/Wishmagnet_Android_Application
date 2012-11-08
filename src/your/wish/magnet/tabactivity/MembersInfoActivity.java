/**
 * 
 */
package your.wish.magnet.tabactivity;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import your.wish.magnet.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.GpsStatus.Listener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.internal.widget.IcsAdapterView;
import com.actionbarsherlock.internal.widget.IcsAdapterView.OnItemSelectedListener;
import com.audacity.wishmagnet.model.MembersModel;
import com.audacity.wishmagnet.util.connectivityCheck;
import com.audacity.wishmagnet.util.httpClient;

/**
 * @author Shaon
 * 
 */
public class MembersInfoActivity extends SherlockFragment {

	/**
	 * Below string are creating for making the choose for viewing member by
	 * order of Activity, Newsest and Alphabetically
	 */
	String memberByActive = "active",memberByNewest = "newest",memberByAlphabetical = "alphabetical";

	ListView listViewMembers;
	ProgressDialog progressDialog;
	EditText searchMember;
	TextView allMember;
	Spinner spinnerOrderBy;

	connectivityCheck internetCheck = new connectivityCheck();


	String responseMemberInfo;
	httpClient httpClientMembersInfo = new httpClient();
	ArrayList<NameValuePair> nvpMemberInfo;
	ArrayList<MembersModel> alData = new ArrayList<MembersModel>();
	ArrayList<MembersModel> alSearchResult;
	LatestMembersListAdapter listAdapter;
	
	String membersInfoURL = "http://wishmagnet.com/api/buddypressread/get_members/?";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.members, container, false);

		initializeView(view);
		
		new DownloadMembersData().execute();

		return view;
		
	}//end of onCreateView()


	/**
	 * 
	 * @param view
	 */
	public void initializeView(View view) {

		searchMember = (EditText) view.findViewById(R.id.editTextSearchMemeber);
		listViewMembers = (ListView) view.findViewById(R.id.listViewMemberInfo);
		spinnerOrderBy = (Spinner) view.findViewById(R.id.spinnerOrderBy);
		
		ArrayAdapter<CharSequence> membersorderAdapter = ArrayAdapter.createFromResource(getSherlockActivity(), R.array.members_order_array, android.R.layout.simple_spinner_item);
		membersorderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerOrderBy.setAdapter(membersorderAdapter);
		
		listViewMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						Toast.makeText(getActivity().getBaseContext(),
								"Members - " + arg2 + " clicked!", 1000).show();

					}// end of onItemClick()

				});// end of event of User List View component

		
		
	}// end of initializeView()
	
	
	/**
	 * fetch user from server into application
	 * 
	 * @param memberOrderBy
	 *            how to fetch the user there have three options for ordering
	 *            the members one: active two: latest three: alphabetical
	 * 
	 */
	private void membersInfoFromServer(final String memberOrderBy) {

		try {

			nvpMemberInfo = new ArrayList<NameValuePair>();

			nvpMemberInfo.add(new BasicNameValuePair("dev", "1"));
			// nvpMemberInfo.add(new BasicNameValuePair("userid", "9"));
			nvpMemberInfo.add(new BasicNameValuePair("type", memberOrderBy));
			// nvpMemberInfo.add(new BasicNameValuePair("limit", "5"));

			responseMemberInfo = httpClientMembersInfo.httpConnectionWithURL(
					membersInfoURL, nvpMemberInfo);
			Log.i("Member-Response:", responseMemberInfo);
			parseResponse(responseMemberInfo);

		} catch (Exception e) {

			Log.e("<<<- MembersInfoActivit on memberThread ->>>",
					"Exception is: " + e);

		}// end of exception handling

	}// end of memberInfoFromServer()

	/**
	 * This method task is to maintain the response from server to show in the
	 * android application
	 * 
	 * @param jsonString
	 *            which response is getting from server
	 */
	private void parseResponse(String jsonString) {

		JSONObject jsonObject;

		try {
			jsonObject = new JSONObject(jsonString);

			String success = jsonObject.getString("status");

			if (success.equalsIgnoreCase("ok")) {

				JSONArray users = jsonObject.getJSONArray("users");
				JSONArray newArray = users.getJSONArray(0);

				for (int i = 0; i < newArray.length(); i++) {

					MembersModel membersModel = new MembersModel();
					membersModel.memberName = newArray.getJSONObject(i)
							.getString("display_name");
					membersModel.lastActiveTime = newArray.getJSONObject(i)
							.getString("last_activity");

					alData.add(membersModel);

				}// end of for loop

			}// end of if condition

		} catch (JSONException e) {

			Log.e("parseResponse JSONException :", "Exception on :" + e);

		}// end of exception handling

	}// end of parseResponse()

	/**
	 * LatestIntentionListAdapter extends ArrayAdapter from where data of latest
	 * intention from server is showing in text view from here after here
	 * 
	 * @author Shaon
	 * 
	 */
	private class LatestMembersListAdapter extends ArrayAdapter<MembersModel> implements Filterable {

		LayoutInflater inflater;
		ArrayList<MembersModel> mAlData;
		ArrayList<MembersModel> mSearchData;
		private SearchFilter filter;
		
		public LatestMembersListAdapter(Context context,
				int textViewResourceId, ArrayList<MembersModel> data) {

			super(context, textViewResourceId, data);
			this.mAlData = new ArrayList<MembersModel>();
			mAlData.addAll(data);
			this.mSearchData = new ArrayList<MembersModel>();
			mSearchData.addAll(mAlData);
			this.inflater = getActivity().getLayoutInflater().from(context);
			getFilter();
		}// end of LatestIntenstionListAdapter()

		/**
		 * getView method are for Viewing the data
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View row;		

			if (convertView == null) {

				row = inflater.inflate(R.layout.list_item_members, parent, false);
			}
			else {

				row = convertView;
			}// end of if else condition

			TextView tvUsername = (TextView) row.findViewById(R.id.textViewMemberUsername);
			tvUsername.setText(mAlData.get(position).memberName);
			// tvPeople.setTag(position);

			TextView tvLastActiveTime = (TextView) row.findViewById(R.id.textViewMemberActive);
			tvLastActiveTime.setText(mAlData.get(position).lastActiveTime);
			// tvSeconds.setTag(position);
			
			return row;

		}// end of View getView()
		
		@Override
		public Filter getFilter() {
			
			if(filter == null) {
				
				filter = new SearchFilter();
			}
			
			return filter;
		}

		private class SearchFilter extends Filter {
			
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				// TODO Auto-generated method stub
				constraint = constraint.toString().toLowerCase();
				FilterResults result = new FilterResults();
				
				if(constraint != null && constraint.toString().length() > 0) {
					
					ArrayList<MembersModel> alFilteredItems = new ArrayList<MembersModel>();
					
					for(int i=0; i<mAlData.size(); i++) {
						
						MembersModel memberModel = mAlData.get(i);
						
						if(memberModel.memberName.toLowerCase().contains(constraint)) {
							
							alFilteredItems.add(memberModel);
						}
					}
					
					result.count = alFilteredItems.size();
					result.values = alFilteredItems;
				}
				else {
					synchronized (this) {
					
						result.values = mAlData;
						result.count = mAlData.size();
					}
				}
				
				return result;
			}
	
		
			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				// TODO Auto-generated method stub
				mSearchData = (ArrayList<MembersModel>) results.values;
				notifyDataSetChanged();
				clear();
				
				for(int i=0; i<mSearchData.size(); i++) {
					
					add(mSearchData.get(i));
				}
				notifyDataSetInvalidated();
			}
		}
	}// end of LatestIntensionsListAdapter Class

	private class DownloadMembersData extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {

			progressDialog = ProgressDialog.show(getActivity(), "Loading",
					"Fetching data, Please wait...");

		}// end of onPreExecute()

		@Override
		protected String doInBackground(Void... params) {

			if (connectivityCheck.hasInternetConnection()) {

				membersInfoFromServer(memberByActive);

			} else {

				return "No internet connection!";

			}// end of if else condition

			return "0";

		}// end of doInBackground

		@Override
		protected void onPostExecute(String msg) {

			progressDialog.dismiss();

			if (!alData.isEmpty()) {

//				alSearchResult = alData;
				listAdapter = new LatestMembersListAdapter(getActivity(), R.layout.members, alData);
				listViewMembers.setAdapter(listAdapter);
				listViewMembers.setTextFilterEnabled(true);
//				final ArrayList<MembersModel> alSearchResults;
				
				searchMember.addTextChangedListener(new TextWatcher() {
					
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						// TODO Auto-generated method stub
						listAdapter.getFilter().filter(s.toString());
//						String searchString = s.toString();
//						
//						alSearchResult = new ArrayList<MembersModel>();
//						
//						for(int i=0; i<alData.size(); i++) {
//							
//							if(searchString.length() > 0 && searchString.length() < alData.get(i).toString().length()) {
//								
//								if(alData.get(i).toString().contains(searchString)) {
//									
//									alSearchResult.add(alData.get(i));
//								}
//							}
//							else {
//								// restore original data
//							}
//						}
//						
//						listAdapter.notifyDataSetChanged();
//						listViewMembers.invalidate();
					}
					
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count,
							int after) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub
						
					}
				});
				
			} else {

				Toast.makeText(getActivity().getBaseContext(),
						"Error in downloading data, Please try again", 1000).show();

			}// end of if else condition

		}// end of onPostExecute()

	}// end of DownloadMembersData AsyncTask

}// end of main class