package com.mpos.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

import com.mpos.fragment.MerchantAdminLoginFragment;
import com.mpos.utils.CustomAlertDialog;

public class StartingActivity extends MposBaseActivity {
	Menu optionMenu;

	@Override
	public void onBackPressed() {

		CustomAlertDialog.showConfirmationAlert(StartingActivity.this, getResources().getText(R.string.warning_dialog).toString(), getResources().getText(R.string.exit_app).toString());

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences userDetails = this.getSharedPreferences("userdetails", MODE_PRIVATE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.intro_screen);
		//	addActionBar();
		//hideActionBar();
		//hideMenu();
		//showActionBar();


		getFragmentManager().beginTransaction().replace(R.id.main_fragment, new MerchantAdminLoginFragment(), "MAAFRAGMENT").commit();


	}
}
