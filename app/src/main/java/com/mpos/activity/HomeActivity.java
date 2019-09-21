package com.mpos.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mpos.Model.terminal;
import com.mpos.fragment.GridViewFragment;
import com.mpos.fragment.ProgressFragment;
import com.mpos.mpossdk.api.MPOSService;
import com.mpos.mpossdk.api.MPOSServiceCallback;
import com.mpos.mpossdk.api.TransactionType;
import com.mpos.utils.CustomAlertDialog;


public class HomeActivity extends MposBaseActivity {

    MPOSService mposService = null;


    @Override
    public void onBackPressed() {

        Fragment fragment = getFragmentManager().findFragmentById(R.id.main_fragment);

        int count = getFragmentManager().getBackStackEntryCount();
        if (fragment.getTag().equals("HOMESCREEN")) {
            CustomAlertDialog.showConfirmationAlert(HomeActivity.this, getResources().getText(R.string.warning_dialog).toString(), getResources().getText(R.string.exit_app).toString());
        } else {
            // getFragmentManager().popBackStack();
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getFragmentManager().beginTransaction().replace(R.id.main_fragment, new GridViewFragment(), "HOMESCREEN").commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences userDetails = this.getSharedPreferences("userdetails", MODE_PRIVATE);
        setContentView(R.layout.main_fragment);


        getFragmentManager().beginTransaction().add(R.id.main_fragment, new GridViewFragment(), "HOMESCREEN").commit();

        mposService = MPOSService.getInstance(this);
        gotoSale();
    }

    public void showToast(final String toast) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(HomeActivity.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume() {

        super.onResume();
        try {
            if (!mposService.isDeviceConnected())
                mposService.connectToDevice(new MPOSServiceCallback() {
                    @Override
                    public void onComplete(int status, String message, Object result) {

                        Toast.makeText(HomeActivity.this, message, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailed(int status, String message) {
                        Toast.makeText(HomeActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                });
        } catch (Exception ex) {

    }

    }

    @Override
    protected void onStop() {
        //mposService.stop();
         super.onStop();
    }

    @Override
    public void onDestroy() {
        mposService.stop();
        super.onDestroy();
    }

    private void gotoSale(){

        Bundle bund=getIntent().getExtras();
        if (bund!=null) {
            Log.d("price  :", bund.getString("price") + "  tid :" + bund.getString("tid"));

            ProgressFragment connectionFragment = new ProgressFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("TSF", true);
            bundle.putString("CashBack", bund.getString("price"));
            bundle.putString("Amount", bund.getString("price"));
            bundle.putString("tid", bund.getString("tid"));
            bundle.putInt("TransactionType", TransactionType.SALE.getValue());

            connectionFragment.setArguments(bundle);

            getFragmentManager().beginTransaction().replace(R.id.
                    main_fragment, connectionFragment, "CONFRAGMENT").commit();
        }

        // ATTENTION: This was auto-generated to handle app links.
//        Intent appLinkIntent = getIntent();
//        String appLinkAction = appLinkIntent.getAction();
//        Uri appLinkData = appLinkIntent.getData();
//        Log.d("appLinkData",appLinkData +" ");
//        if (appLinkData!=null){
//            terminal terminal_=new terminal();
//            terminal_.GET_data(getApplicationContext(), appLinkData.toString(), new terminal.OnCoupon_lisennter() {
//                @Override
//                public void onSuccess(int status) {
//
//                }
//
//                @Override
//                public void onSuccess(terminal term) {
//
//                    Log.d("terminal_info  tid",term.getTid()+" amount  :"+term.getAmount());
//
//
//                    ProgressFragment connectionFragment = new ProgressFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putBoolean("TSF", true);
//                    bundle.putString("CashBack", term.getAmount());
//                    bundle.putString("Amount", term.getAmount());
//                    bundle.putString("tid", term.getTid());
//                    bundle.putInt("TransactionType", TransactionType.SALE.getValue());
//
//                    connectionFragment.setArguments(bundle);
//
//                    getFragmentManager().beginTransaction().replace(R.id.
//                            main_fragment, connectionFragment, "CONFRAGMENT").commit();
//                }
//
//                @Override
//                public void onStart() {
//
//                }
//
//                @Override
//                public void onFailure(String msg) {
//
//                }
//            });
//
//        }


    }
}