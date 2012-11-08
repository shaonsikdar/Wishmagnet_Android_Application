/**
 * 
 */
package your.wish.magnet.user;

import java.nio.channels.AsynchronousCloseException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import your.wish.magnet.R;
import your.wish.magnet.R.layout;

import com.audacity.wishmagnet.util.connectivityCheck;
import com.audacity.wishmagnet.util.httpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Shaon
 *
 */
public class UserInfoActivity extends Activity {

	String response;
	httpClient httpClientUserInfo= new httpClient();
	ArrayList<NameValuePair> nvpUserInfo;
	ProgressDialog progressDialog;
	String userID="1";
	String nameParse;
	String userNameServer;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);
     
        initializeView();
        
   }// end of onCreate()
	
	private void initializeView(){
		
		new userInfoAsync().execute();
		//gettingUserInfoFromServer("1");
		
	}//end of initializeView()
	
	/**
	 * Requesting to server for getting latest intention from server
	 */
	public void gettingUserInfoFromServer(final String userid){
		
			try {
					
						nvpUserInfo = new ArrayList<NameValuePair>();
						nvpUserInfo.add(new BasicNameValuePair("tag", "userinfo"));
					//  nvpUserInfo.add(new BasicNameValuePair("userid", "1"));
						nvpUserInfo.add(new BasicNameValuePair("userid", userid));
						
						response = httpClientUserInfo.httpConnection(nvpUserInfo);
											
					
		} catch (Exception e) {
				
			Log.e("<<<- UserInfoActivity gettingUserInfoFromServer ->>>"," Exception name : " + e);
			
		}//end of exception handling
		
	}//end of gettingUserInfoFromServer
	
	
	public String gettingUserInfoFromServerPostBy(final String userid){
		
		try {
				
					nvpUserInfo = new ArrayList<NameValuePair>();
					nvpUserInfo.add(new BasicNameValuePair("tag", "userinfo"));
				//  nvpUserInfo.add(new BasicNameValuePair("userid", "1"));
					nvpUserInfo.add(new BasicNameValuePair("userid", userid));
					
					response = httpClientUserInfo.httpConnection(nvpUserInfo);
							
					Log.i("Respone",response);
					String userNameServer = parseResponse(response);
					
				
			} catch (Exception e) {
					
				Log.e("<<<- UserInfoActivity gettingUserInfoFromServer ->>>"," Exception name : " + e);
				
			}//end of exception handling
		
			return userNameServer;
			
	}//end of gettingUserInfoFromServer
	
	
	
	private String parseResponse(String jsonString) {

		JSONObject jsonObject;

		try {
			
			jsonObject = new JSONObject(jsonString);

			String success = jsonObject.getString("success");

			if (success.equalsIgnoreCase("1")) {

				JSONArray users = jsonObject.getJSONArray("user");

				for (int i = 0; i < users.length(); i++) {

//					"registered_at":"2012-10-05 21:29:47","id":7,"email":"FB_1460584625@unknown.com","name":"David"
					String reg_at = users.getJSONObject(i).getString("registered_at");				
					String ID = users.getJSONObject(i).getString("id");
					String email = users.getJSONObject(i).getString("email");
					nameParse = users.getJSONObject(i).getString("name");
					
				}// end of for loop
				
			}else{
				
				Log.w("user does not exist","!!!");
				
			}//end of if else

		} catch (JSONException e) {

			Log.e("parseResponse JSONException :", "Exception on :" + e);

		}// end of exception handling
		
		return nameParse;

	}// end of parseResponse()
	
	private class userInfoAsync extends AsyncTask<Void, Void, String>{
		@Override
		protected void onPreExecute() {

			progressDialog = ProgressDialog.show(getApplicationContext(), "Loading",
					"Fetching data, Please wait...");

		}// end of onPreExecute()

		@Override
		protected String doInBackground(Void... params) {

			if (connectivityCheck.hasInternetConnection()) {

				gettingUserInfoFromServer(userID);

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
//				listViewMembers.setAdapter(new LatestMembersListAdapter(getActivity(), R.layout.members, alData));
//
//			} else {
//
//				Toast.makeText(getActivity().getBaseContext(),
//						"Error in downloading data, Please try again", 1000).show();
//
//			}// end of if else condition

		}// end of onPostExecute()

	}// end of userInfo AsyncTask
	
}//end of main class