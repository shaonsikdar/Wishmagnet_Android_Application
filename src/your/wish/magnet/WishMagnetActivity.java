package your.wish.magnet;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WishMagnetActivity extends Activity {
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				Intent loginIntent = new Intent(WishMagnetActivity.this,
						NavigatorActivity.class);
				
				startActivity(loginIntent);
				finish();

			}// end of run()

		}, 3000); // end of postDelayed after 3000 means 3 seconds.

	}// end of onCreate()

}// end of mainClass