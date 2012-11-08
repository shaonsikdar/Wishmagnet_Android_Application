package your.wish.magnet;

import your.wish.magnet.tabactivity.LatestIntensions;
import your.wish.magnet.tabactivity.MembersInfoActivity;
import your.wish.magnet.tabactivity.RecentCommentsActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.audacity.wishmagnet.util.CustomDialog;
import com.audacity.wishmagnet.util.UserActionProvider;

public class NavigatorActivity extends SherlockFragmentActivity {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	CustomDialog customDialog;
	com.actionbarsherlock.app.ActionBar actionBar;
	
	// Added by FAISAL 19-10-12
//	EditText etInput;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		customDialog = new CustomDialog(this);

		actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true); // enabling the "up" navigation
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		

		SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this,R.array.navigation_list,android.R.layout.simple_selectable_list_item);
		
		ActionBar.OnNavigationListener mOnNavigationListener = new OnNavigationListener() {
			
			String[] pages = getResources().getStringArray(R.array.navigation_list);
			
			@Override
			public boolean onNavigationItemSelected(int itemPosition, long itemId) {

				FragmentManager fm = getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction(); 
				
    			switch(itemPosition) {
    				
    			case 0:
    				SherlockFragment f1 = new HomeActivity();
    				ft.replace(android.R.id.content, f1, pages[itemPosition]);
    				break;
    			
    			case 1:
    				SherlockFragment f2 = new ActivityActivity();
    				ft.replace(android.R.id.content, f2, pages[itemPosition]);
    				break;
    				
    			case 2:
    				SherlockFragment f3 = new MembersInfoActivity();
    				ft.replace(android.R.id.content, f3, pages[itemPosition]);
    				break;
    				
    			case 3:
    				SherlockFragment f4 = new VisualActivity();
    				ft.replace(android.R.id.content, f4, pages[itemPosition]);
    				break;
    	
    			case 4:
    				SherlockFragment f5 = new LatestIntensions();
    				ft.replace(android.R.id.content, f5, pages[itemPosition]);
    				break;
    				
    			case 5:
    				RecentCommentsActivity f6 = new RecentCommentsActivity();
    				ft.replace(android.R.id.content, f6, pages[itemPosition]);
    				break;
    				
    			}//end of switch
    			
    			ft.commit();
				return true;
				
			}//onNavigationItemSelected()
			
		};//end of ActionBar.OnNavigationListener mOnNavigationListener
		
		actionBar.setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);
		
	}//end of onCreate()
	
	/**
	 * 
	 * @param menu
	 * @return
	 */
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.home_menu, menu);
		
		UserActionProvider mUserActionProvider = (UserActionProvider) menu.findItem(R.id.action).getActionProvider();
		mUserActionProvider.onPerformDefaultAction();

		return super.onCreateOptionsMenu(menu);
		
	}//end of onCreateOptionsMenu
	
	
	/**
	 * 
	 * @param item
	 * @return
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		
		return true;
		
	}//end of onOptionsItemSelected()
	
	private DialogInterface.OnClickListener btnCancel = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.dismiss();
		}
	};
	
}//end of main class