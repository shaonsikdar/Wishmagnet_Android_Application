/**
 * 
 */
package your.wish.magnet;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import your.wish.magnet.tabactivity.LatestIntensions;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.audacity.wishmagnet.util.connectivityCheck;
import com.audacity.wishmagnet.util.httpClient;

/**
 * @author Shaon
 * 
 */
public class LoginActivity extends Activity {

	/**
	 * Below things are for initiatling the layout of activity
	 */
	EditText usernameET, passwordET;
	CheckBox rememberCB;
	Button loginBtn;
	TextView joinBtn;
	LinearLayout llFacebookLogin;

	httpClient loginHttp = new httpClient();
	ArrayList<NameValuePair> loginNVP;
	ProgressDialog progressDialog;

	String ExceptionToken,ExceptionMessage,internetConnectionToken,internetConnectionError;
	String username,password,loginResponse;
	String loginStatus = "0";


	/**
	 * Calling onCreate method for getting the layout of this activity
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_login);

		initializeView();

	}// end of onCreate()

	// this method develop for making the initialize of content of layout in a
	// better way
	// so that this will easy for user and application won't make runtime error
	// as I have handled
	// exception handling
	public void initializeView() {

		try {

			loginBtn = (Button) findViewById(R.id.button_login_login);
			joinBtn = (TextView) findViewById(R.id.button_login_join);
			llFacebookLogin = (LinearLayout) findViewById(R.id.linearLayout_login_facebook);
			usernameET = (EditText) findViewById(R.id.editText_login_username);
			passwordET = (EditText) findViewById(R.id.editText_login_password);

			/**
			 * Below code doing the work for happening what after pressing
			 * button login. After that which activity will come that is happen
			 * here
			 */
			loginBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {

					username = usernameET.getText().toString();
					password = passwordET.getText().toString();

					if (username.equalsIgnoreCase("")) {

						usernameET.setError("Username Can't Blank !");

					} else if (password.equalsIgnoreCase("")) {

						passwordET.setError("Password Cant Blank !");

					} else {

						new LoginAsyncTask().execute();

					}// checking username and password null or processing for

				}// end of onClick()

			});// end of event of loginBtn

			/**
			 * Below code doing the work for happening what after pressing
			 * button JOIN. After that which activity will come that is happen
			 * here
			 */
			joinBtn.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {

					Intent joinIntent = new Intent(LoginActivity.this, SignUpActivity.class);
					startActivity(joinIntent);

				}// end of onClick()

			}); // end of event of joinBtn

			/**
			 * Below code doing the work for happening what after pressing
			 * button LOGINWITHFB. After that which activity will come that is
			 * happen here
			 */

			llFacebookLogin.setOnClickListener(new View.OnClickListener() {

				public void onClick(View arg0) {

					Intent userInfoIntent = new Intent(getApplicationContext(),LatestIntensions.class);
					startActivity(userInfoIntent);

				}//end of onClick()
				
			});// end of loginWith FBBtn

		} catch (Exception e) {

			Log.e("<<<- LoginActivity ->>>", "Exception is:" + e.toString());

		}//end of exception handling

	}// end of InitializeView()

	/**
	 * 
	 */
	public void loginIntoWishMagnet() {

		try {

				loginNVP = new ArrayList<NameValuePair>();
				loginNVP.add(new BasicNameValuePair("tag", "login"));
	
	
				loginNVP.add(new BasicNameValuePair("username", username));
				loginNVP.add(new BasicNameValuePair("password", password));
				
//				loginNVP.add(new BasicNameValuePair("username", "siddiqab"));
//				loginNVP.add(new BasicNameValuePair("password", "wishmagnet"));
	
				loginResponse = loginHttp.httpConnection(loginNVP);
	
				Log.i("JSON ARRAY RESPONSE", "Data" + loginResponse);
	
				parseResponse(loginResponse);

		} catch (Exception e) {

			Log.e("<<<- loginIntoWishMagnet loginBtn.setOnClickListener Exception On->>>","Exception name :" + e);

		}// end of exception handling

	}// end of loginIntoWishMagnet


	/**
	 * 
	 * @param jsonString
	 */
	private void parseResponse(String jsonString) {

		JSONObject jsonObject;

		try {

			jsonObject = new JSONObject(jsonString);

			loginStatus = jsonObject.getString("success");

			if (loginStatus.equalsIgnoreCase("0")) {

				ExceptionToken = "1";
				ExceptionMessage = "Check you Username and Password!!!";

			}else{
				
				Intent loginSuccessIntent = new Intent(getApplicationContext(),HomeActivity.class);
				startActivity(loginSuccessIntent);
				
			}// end of if else condition

		} catch (JSONException e) {

			Log.e("<<<- LoginActivity parseResponse() ->>>", "Exception is :" + e);

		}// end of exception handling

	}// end of parseResponse()

	/**
	 * 
	 * @author Shaon
	 *
	 */
	private class LoginAsyncTask extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {

			progressDialog = ProgressDialog.show(LoginActivity.this, "Loading","Login Processing, Please wait...");

		}// end of onPreExecute()

		@Override
		protected String doInBackground(Void... params) {

			if (connectivityCheck.hasInternetConnection()) {

				loginIntoWishMagnet();

			} else {

				internetConnectionError = "Check your internet connection!";
				internetConnectionToken = "1";
				return "No internet connection!";

			}// end of if else condition

			return loginStatus;

		}// end of doInBackground

		@Override
		protected void onPostExecute(String msg) {

			progressDialog.dismiss();

			if(msg.equalsIgnoreCase("1")) {
				
				Toast.makeText(LoginActivity.this, "Login successful!", 1000).show();
				
				// Storing the login status
				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
				
				Editor editor = sharedPreferences.edit();
				editor.putBoolean("loginStatus", true);
				editor.commit();
				
				Intent i = new Intent(LoginActivity.this, NavigatorActivity.class);
				startActivity(i);
				finish();
				
			}else {
				
				Toast.makeText(LoginActivity.this, "Login failed! Check your username and Password.", 1000).show();
				
			}//end of if else condition

//			try {
//				if (internetConnectionToken.equalsIgnoreCase("1")) {
//
//					Toast.makeText(LoginActivity.this, internetConnectionError,
//							3000).show();
//
//				} else if (ExceptionToken.equalsIgnoreCase("1")) {
//
//					Toast.makeText(LoginActivity.this, ExceptionMessage, 3000)
//							.show();
//
//				} else {
//
//					Toast.makeText(LoginActivity.this, "Successfullt Login",
//							3000).show();
//
//				}// end of if else condition
//
//			} catch (Exception e) {
//
//				Log.e("Exception on onPostExecute :", "Exception Name:" + e);
//			}
			

		}// end of onPostExecute()

	}// end of LoginAsyncTask

}// end of mainClass