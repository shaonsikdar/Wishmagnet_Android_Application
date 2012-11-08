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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Shaon
 *
 */
public class UserActivity extends Activity {

	Button btnWall,btnProfile,btnIntention;
	ImageView ivUserImage;
	TextView tvUserLasrActive;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);
     
//        new userInfoAsync().execute();
        
        initializeView();
        
   }// end of onCreate()
	
	private void initializeView(){
		
		ivUserImage = (ImageView) findViewById(R.id.imageViewUserImage);
		tvUserLasrActive = (TextView) findViewById(R.id.textViewUserLastActive);
		
		btnWall=(Button) findViewById(R.id.buttonUserWall);
		btnProfile=(Button) findViewById(R.id.buttonUserProfile);
		btnIntention=(Button) findViewById(R.id.buttonUserIntention);
	
		
		//gettingUserInfoFromServer("1");
		
	}//end of initializeView()
		
}//end of main class