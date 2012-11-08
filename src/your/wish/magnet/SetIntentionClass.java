package your.wish.magnet;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.audacity.wishmagnet.util.httpClient;

public class SetIntentionClass {

	httpClient httpSetIntention = new httpClient();
	ArrayList<NameValuePair> nvpIntention;
	String setIntentionURL= " ";
	
	public void SetIntentionWithID(String userID, String userIntention){
		
		nvpIntention = new ArrayList<NameValuePair>();
		nvpIntention.add(new BasicNameValuePair("userID", userID));
		nvpIntention.add(new BasicNameValuePair("intention",userIntention));

	}//end of SetIntentionClass()
	
}//end of SetIentionClass
