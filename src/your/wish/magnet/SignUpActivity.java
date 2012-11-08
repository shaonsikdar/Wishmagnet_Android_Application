/**
 * 
 */
package your.wish.magnet;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.audacity.wishmagnet.util.connectivityCheck;
import com.audacity.wishmagnet.util.httpClient;
import com.audacity.wishmagnet.util.serverActivity;

/**
 * @author Shaon
 *
 */
public class SignUpActivity extends Activity {
	
	EditText etUsername,etEmail,etPassword;
	Button btnJoin;
	ProgressDialog progressDialog;
	serverActivity serverActivityObject=new serverActivity();
	String joinResponse;
	httpClient httpClientSignUp = new httpClient();
	ArrayList<NameValuePair> nvpSignUp;
	
	String ExceptionToken,ExceptionMsg ;
		
	String username, email , password, internetConnectionError,internetConnectionToken;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);
     
        initializeView();
        
   }// end of onCreate()
	
	
	/**
	 * Do everything for initializing the layout of xml file
	 */
	public void initializeView(){
	
		try {
				etUsername = (EditText) findViewById(R.id.join_un_editText);
				etEmail = (EditText) findViewById(R.id.join_email_editText);
				etPassword = (EditText) findViewById(R.id.join_pass_editText);
				
				btnJoin = (Button) findViewById(R.id.join_submit_button);
						
				btnJoin.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {

						joinProcess();
						
					}//end of joinProcess();
					
				});//end of event of btnJoin
				
			}catch(Exception e){
				
				Log.e("<<<- Initialize View Method ->>>", "Exception is :" + e);
				
			}//end of exception handling  
		
	}//end of initializeView()
	
	
	/**
	 * 
	 */
	private void joinProcess(){
		
		try {
			
				username = etUsername.getText().toString();
				email = etEmail.getText().toString();
				password = etPassword.getText().toString();
				
	
				if(username.equalsIgnoreCase("")){
					
					etUsername.setError("Username Can't Blank");
					
				}else if (email.equalsIgnoreCase("")){
					
					etEmail.setError("Email Can't Blank");
					
				}else if (password.equalsIgnoreCase("")){
					
					etPassword.setError("Password Can't Blank");
				}else{
					
					new joinAsyncTask().execute();
				}
					
				Log.d("<<<- joinProcess Successfull ->>>","Success in Joining to WishMagnet");
				
				
			} catch (Exception e) {
				
				Log.e("<<<- SignUpActiviy exception on joinProcess ->>>","Excepion is : "+e);
				
			}// end of exception handling
		
	}// end of joinProcess()
	
	
	/**
	 * 
	 */
	public void joinToServer(){
		
		try {
				nvpSignUp = new ArrayList<NameValuePair>();
				
				nvpSignUp.add(new BasicNameValuePair("tag", "register"));
				nvpSignUp.add(new BasicNameValuePair("username",username));
				nvpSignUp.add(new BasicNameValuePair("email",email));
				nvpSignUp.add(new BasicNameValuePair("password",password));

				joinResponse = httpClientSignUp.httpConnection(nvpSignUp);
				
				Log.i("Join JSON ARRAY"," JSON ARRAY:"+joinResponse);
				
				parseResponse(joinResponse);
				
				
		} catch (Exception e) {

			Log.e("<<<- Exception on Join ->>>","Exception is :" + e);
			
		}//end of exception handling				
	
		
	}//end of joinToServer()
	
	
	/**
	 * 
	 * @param jsonString
	 */
	private void parseResponse(String jsonString){
			
			JSONObject jsonObject;
			
			try {
	
					jsonObject = new JSONObject(jsonString);
					
					
					String status = jsonObject.getString("success");
					String error = jsonObject.getString("error");
						
						if(status.equalsIgnoreCase("1")){
							
							
							Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
							startActivity(loginIntent);
	
							
						}else if(error.equalsIgnoreCase("2")){
							
								ExceptionToken = "2";
								ExceptionMsg = "This username is already registered!";
						
						}else if(error.equalsIgnoreCase("3")){
								
								ExceptionToken = "3";
								ExceptionMsg = "This email address is already registered!";
							
						}//end of if else condition
						
				
			} catch (JSONException e) {
			
				Log.e("Exception on parseResponse","Exception is :"+e);
				
			}//end of exception handling
			
		}//end of parseResponse()
	
	/**
	 * 
	 * @author Shaon
	 *
	 */
	private class joinAsyncTask extends AsyncTask<Void, Void, String>{

		protected void onPreExecute(){
			
			progressDialog = ProgressDialog.show(SignUpActivity.this, "Loading", "Join Processing, Please wait...");
			
		}//end of onPreExecute()
		
		@Override
		protected String doInBackground(Void... params) {

			if(connectivityCheck.hasInternetConnection()){
				
				joinToServer();
				
			}else{
				
				internetConnectionError = "Check your internet connection!";
				internetConnectionToken = "1";
				return "No Internet Connection!";
				
			}//end of if else condition
			
			return "0";
			
		}//end of doInBackground()
		
		protected void onPostExecute(String msg){
			
			progressDialog.dismiss();
			
			if(internetConnectionToken.equalsIgnoreCase("1")){
				
				Toast.makeText(SignUpActivity.this, internetConnectionError, 3000).show();
				
			}else if (ExceptionToken.equalsIgnoreCase("2")){
				
				//Toast.makeText(getBaseContext(), ExceptionMsg,  3000).show();
				
				etUsername.setError(ExceptionMsg);
				
			}else if (ExceptionToken.equalsIgnoreCase("3")){
				
				etEmail.setError(ExceptionMsg);
				
			}else{
				
				Toast.makeText(getBaseContext(), "Join Successfully to WishMagnet!",  3000).show();
				
			}//end of if else condition 
			
		}//end of onPostExecute()
		
	}//end of joinAsyncTask
	
}// end of mainClass