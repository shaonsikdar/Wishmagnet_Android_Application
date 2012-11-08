package com.audacity.wishmagnet.util;

import your.wish.magnet.LoginActivity;
import your.wish.magnet.R;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.view.ActionProvider;

public class UserActionProvider extends ActionProvider {

	Context mContext;
	LayoutInflater mLayoutInflater;
	CustomDialog mCustomDialog;
	EditText etInput;
	
	public UserActionProvider(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = context;  
		mLayoutInflater = LayoutInflater.from(mContext);
		mCustomDialog = new CustomDialog(mContext);
	}

	@Override
	public View onCreateActionView() {
		// TODO Auto-generated method stub
		View view = mLayoutInflater.inflate(R.layout.user_action_provider, null);
		
		Button option1 = (Button) view.findViewById(R.id.button1_actionProvider);
		option1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
				if(sharedPreferences.getBoolean("loginStatus", false) == false) {
					
					mCustomDialog.twoButtonDialog("Message", "You are not logged in, Please login to post an intension.", "Ok", btnLoginOk, "Cancel", btnLoginCancel);
				}
				else {
					
					etInput = mCustomDialog.inputTextDialog("Set Intension", "Enter your intension: ", "Ok", "Cancel", btnOk, btnCancel);
				}
				
			}
		});
		
		Button option2 = (Button) view.findViewById(R.id.button2_actionProvider);
		option2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				etInput = mCustomDialog.inputTextDialog("Search", "Enter your search keyword: ", "Ok", "Cancel", btnSearchOk, btnSearchCancel);
			}
		});
		
		Button option3 = (Button) view.findViewById(R.id.button3_actionProvider);
		option3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Toast.makeText(mContext, "Option 3 selected!", 1000).show();
				Intent i = new Intent(mContext, LoginActivity.class);
				mContext.startActivity(i);
			}
		});
		
		return view;
	}

	private DialogInterface.OnClickListener btnOk = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			Toast.makeText(mContext, "Input Text: " + etInput.getText().toString(), 1000).show();
		}
	};
	
	private DialogInterface.OnClickListener btnCancel = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.dismiss();
		}
	};
	
	private DialogInterface.OnClickListener btnSearchOk = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
//			Toast.makeText(mContext, "Input Text: " + etInput.getText().toString(), 1000).show();
			Intent i = new Intent(Intent.ACTION_SEARCH);
			i.putExtra(SearchManager.QUERY, etInput.getText().toString());
			mContext.startActivity(i);
		}
	};
	
	private DialogInterface.OnClickListener btnSearchCancel = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.dismiss();
		}
	};
	
	private DialogInterface.OnClickListener btnLoginOk = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			Intent i = new Intent(mContext, LoginActivity.class);
			mContext.startActivity(i);
		}
	};
	
	private DialogInterface.OnClickListener btnLoginCancel = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.dismiss();
		}
	};
}
