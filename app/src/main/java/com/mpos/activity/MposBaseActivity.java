package com.mpos.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mpos.Model.terminal;
import com.mpos.fragment.AppSettingsFragment;
import com.mpos.fragment.ProgressFragment;
import com.mpos.fragment.ReconciliationHistoryFragment;
import com.mpos.fragment.TransactionHistoryFragment;
import com.mpos.mpossdk.api.MPOSService;
import com.mpos.mpossdk.api.MPOSServiceCallback;
import com.mpos.mpossdk.api.TerminalData;
import com.mpos.mpossdk.api.TransactionType;
import com.mpos.mpossdk.api.data.appsettings.AppSettings;
import com.mpos.utils.PendingStatus;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Queue;

public class MposBaseActivity extends AppCompatActivity {

    Menu optionMenu;
    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private boolean isRunning = true;
    Queue<PendingStatus> pendingStatuses = new ArrayDeque<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ActionBar actionBar;
    private Toolbar toolbar;

    public void addActionBar() {

        try {

            toolbar = findViewById(R.id.toolbar);
            toolbar.setVisibility(View.GONE);

            actionBar = getSupportActionBar();

            if (actionBar != null) {
                toolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F2F2F2")));
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setDisplayShowCustomEnabled(true);
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setIcon(R.color.actionbar_background);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        getOverflowMenu();

    }

    public void hideActionBar() {
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    public void showActionBar() {
        if (actionBar != null) {
            actionBar.show();

        }
    }

    public void hideMenu() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (optionMenu != null) optionMenu.setGroupVisible(R.id.group1, false);
            }
        }, 100);

    }

    public void showMenu() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (optionMenu != null) optionMenu.setGroupVisible(R.id.group1, true);
            }
        }, 100);
    }

    private void getOverflowMenu() {

        try {
            ViewConfiguration config = ViewConfiguration.get(this);

            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        optionMenu = menu;
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mnu, menu);
        showMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case R.id.menu_connect_id:
                Log.d("menu", "menu_connect_id");
                MPOSService.getInstance(this).showDeviceListDialog();
                break;
            case R.id.menu_get_terminal_data:
                Log.d("menu", "menu_connect_id");

                if (!MPOSService.getInstance(this).isDeviceConnected()) {
                    Toast.makeText(this, "Device not connected", Toast.LENGTH_LONG).show();
                    return true;
                }

                MPOSService.getInstance(this).getTerminalData(new MPOSServiceCallback() {
                    @Override
                    public void onComplete(int status, String message, Object result) {
                        TerminalData retailerData = (TerminalData) result;

                        showRetailerData(MposBaseActivity.this, getResources().getText(R.string.warning_dialog).toString(), retailerData.getTID(), retailerData.getMID());

                    }

                    @Override
                    public void onFailed(int status, String message) {
                        showToastMessage(getApplicationContext(), message);
                    }
                });
                break;


            case R.id.menu_get_last_txn_data:
                Log.d("menu", "menu_get_last_txn_data");
                ProgressFragment connectionFragment = new ProgressFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("TSF", true);
                bundle.putBoolean("LastTransactionResult", true);
                connectionFragment.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.
                        main_fragment, connectionFragment, "CONFRAGMENT").commit();
                break;
            case R.id.menu_all_txn_data:
                Log.d("menu", "menu_all_txn_data");
                try {
                    getFragmentManager().beginTransaction().replace(R.id.main_fragment, new TransactionHistoryFragment(), "TransactionHistoryFragment").commit();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_all_recon_data:
                Log.d("menu", "menu_all_txn_data");
                try {
                    bundle = new Bundle();
                    bundle.putBoolean("menu", true);
                    ReconciliationHistoryFragment fragment = new ReconciliationHistoryFragment();
                    fragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment, "ReconciliationHistoryFragment").addToBackStack("HOMESCREEN").commit();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.menu_get_last_recon_data:
                connectionFragment = new ProgressFragment();
                bundle = new Bundle();
                bundle.putBoolean("TSF", true);
                connectionFragment.setArguments(bundle);
                bundle.putBoolean("TransactionType", true);
                bundle.putBoolean("LastReconciliationResult", true);
                connectionFragment.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.
                        main_fragment, connectionFragment, "CONFRAGMENT").commit();
                break;
            case R.id.menu_check_existance:
                Log.d("menu", "menu_check_existance");
                MPOSService.getInstance(this).checkExistence(new MPOSServiceCallback() {
                    @Override
                    public void onComplete(int status, final String message, Object result) {
                        showToastMessage(getApplicationContext(), message);
                    }

                    @Override
                    public void onFailed(int status, final String message) {
                        showToastMessage(getApplicationContext(), message);
                    }
                });
                break;

        /*    case R.id.menu_on:
                Log.d("menu", "menu_on");
                MPOSService.getInstance(this).setMenuStatus(true, new MPOSServiceCallback() {
                    @Override
                    public void onComplete(int status, final String message, Object result) {
                        showToastMessage(getApplicationContext(), message);
                    }

                    @Override
                    public void onFailed(int status, final String message) {
                        showToastMessage(getApplicationContext(), message);
                    }
                });
                break;

            case R.id.menu_off:
                Log.d("menu", "menu_off");
                MPOSService.getInstance(this).setMenuStatus(false, new MPOSServiceCallback() {
                    @Override
                    public void onComplete(int status, String message, Object result) {
                        showToastMessage(getApplicationContext(), message);
                    }

                    @Override
                    public void onFailed(int status, String message) {
                        showToastMessage(getApplicationContext(), message);
                    }
                });
                break;
*/
            case R.id.menu_set_lanuguage_english:
                Log.d("menu", "menu_set_lanuguage");
                MPOSService.getInstance(this).setLanguage("English", new MPOSServiceCallback() {
                    @Override
                    public void onComplete(int status, String message, Object result) {
                        showToastMessage(getApplicationContext(), message);
                    }

                    @Override
                    public void onFailed(int status, String message) {
                        showToastMessage(getApplicationContext(), message);
                    }
                });
                break;

            case R.id.menu_set_lanuguage_arabic:
                Log.d("menu", "menu_set_lanuguage");
                MPOSService.getInstance(this).setLanguage("Arabic", new MPOSServiceCallback() {
                    @Override
                    public void onComplete(int status, String message, Object result) {
                        showToastMessage(getApplicationContext(), message);
                    }

                    @Override
                    public void onFailed(int status, String message) {
                        showToastMessage(getApplicationContext(), message);
                    }
                });
                break;

            case R.id.menu_get_app_settings:
                Log.d("menu", "menu_get_app_settings");
                MPOSService.getInstance(this).getAppSettings(new MPOSServiceCallback() {
                    @Override
                    public void onComplete(int status, String message, Object result) {
                        //showToastMessage(getApplicationContext(), message+":"+(String)result);

                        String xmlResponse = (String) result;

                        AppSettings appSettings = MPOSService.getInstance(MposBaseActivity.this).parseAppSettingsResponse(xmlResponse);

                        AppSettingsFragment fragment = new AppSettingsFragment();
                        fragment.setAppSettings(appSettings);

                        getFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment, "APPSETTINGS").commit();
                    }

                    @Override
                    public void onFailed(int status, String message) {
                        showToastMessage(getApplicationContext(), message);
                    }
                });
                break;

           /* case R.id.menu_get_card_scheme:
                Log.d("menu", "menu_get_card_scheme");
                MPOSService.getInstance(this).getCardSchemes(new MPOSServiceCallback() {
                    @Override
                    public void onComplete(int status, String message, Object result) {
                        //showToastMessage(getApplicationContext(), message+":"+(String)result);

                        String xmlResponse = (String) result;

                        List<CardScheme> cardSchemes = MPOSService.getInstance(MposBaseActivity.this).parseCardSchemeResponse(xmlResponse);

                        CardSchemeFragment fragment = new CardSchemeFragment();
                        fragment.setCardSchemes(cardSchemes);

                        getFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment, "APPSETTINGS").commit();
                    }

                    @Override
                    public void onFailed(int status, String message) {
                        showToastMessage(getApplicationContext(), message);
                    }
                });
                break;

            case R.id.menu_get_public_keys:
                Log.d("menu", "menu_get_public_keys");
                MPOSService.getInstance(this).getPublicKeys(new MPOSServiceCallback() {
                    @Override
                    public void onComplete(int status, String message, Object result) {
                        //showToastMessage(getApplicationContext(), message+":"+(String)result);

                        String xmlResponse = (String) result;

                        List<PublicKey> publicKeys = MPOSService.getInstance(MposBaseActivity.this).parsePublicKeyResponse(xmlResponse);

                        PublicKeysFragment fragment = new PublicKeysFragment();
                        fragment.setPublicKeys(publicKeys);

                        getFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment, "APPSETTINGS").commit();
                    }

                    @Override
                    public void onFailed(int status, String message) {
                        showToastMessage(getApplicationContext(), message);
                    }
                });
                break;
            case R.id.menu_get_aid_list:
                Log.d("menu", "menu_get_aid_list");
                MPOSService.getInstance(this).getAidList(new MPOSServiceCallback() {
                    @Override
                    public void onComplete(int status, String message, Object result) {
                        //showToastMessage(getApplicationContext(), message+":"+(String)result);

                        String xmlResponse = (String) result;

                        List<Application> applications = MPOSService.getInstance(MposBaseActivity.this).parseAIDListResponse(xmlResponse);

                        AIDListFragment fragment = new AIDListFragment();
                        fragment.setApplications(applications);

                        getFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment, "APPSETTINGS").commit();
                    }

                    @Override
                    public void onFailed(int status, String message) {
                        showToastMessage(getApplicationContext(), message);
                    }
                });
                break;

            case R.id.menu_get_connection_settings:
                Log.d("menu", "menu_get_connection_settings");
                MPOSService.getInstance(this).getConnectionSettings(new MPOSServiceCallback() {
                    @Override
                    public void onComplete(int status, String message, Object result) {
                        //showToastMessage(getApplicationContext(), message+":"+(String)result);
                        String xmlResponse = (String) result;

                        List<CommSettings> commSettingses = MPOSService.getInstance(MposBaseActivity.this).parseCommSettingsResponse(xmlResponse);

                        CommSettingsFragment fragment = new CommSettingsFragment();
                        fragment.setCommSettingses(commSettingses);

                        getFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment, "APPSETTINGS").commit();

                    }

                    @Override
                    public void onFailed(int status, String message) {
                        showToastMessage(getApplicationContext(), message);
                    }
                });
                break;

            case R.id.menu_get_device_flags:
                Log.d("menu", "menu_get_device_flags");
                MPOSService.getInstance(this).getDeviceFlags(new MPOSServiceCallback() {
                    @Override
                    public void onComplete(int status, String message, Object result) {
                        //showToastMessage(getApplicationContext(), message+":"+(String)result);

                        String xmlResponse = (String) result;

                        DeviceSpecificFlags deviceSpecificFlags = MPOSService.getInstance(MposBaseActivity.this).parseDeviceSpecificFlagResponse(xmlResponse);

                        DeviceSpecificFragment fragment = new DeviceSpecificFragment();
                        fragment.setDeviceSpecificFlags(deviceSpecificFlags);

                        getFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment, "APPSETTINGS").commit();
                    }

                    @Override
                    public void onFailed(int status, String message) {
                        showToastMessage(getApplicationContext(), message);
                    }
                });
                break;
            case R.id.menu_get_retailer_data:
                Log.d("menu", "menu_get_retailer_data");
                MPOSService.getInstance(this).getRetailerData(new MPOSServiceCallback() {
                    @Override
                    public void onComplete(int status, String message, Object result) {
                        //showToastMessage(getApplicationContext(), message+":"+(String)result);

                        String xmlResponse = (String) result;

                        RetailerData retailerData = MPOSService.getInstance(MposBaseActivity.this).parseRetailerDataResponse(xmlResponse);

                        RetailerDataFragment fragment = new RetailerDataFragment();
                        fragment.setRetailerData(retailerData);

                        getFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment, "APPSETTINGS").commit();
                    }

                    @Override
                    public void onFailed(int status, String message) {
                        showToastMessage(getApplicationContext(), message);
                    }
                });
                break;*/

		/*
        case R.id.menu_set_connection_mode:
			Log.d("menu","menu_set_connection_mode");
			ConnectionSettingsFragment fragment = new
					ConnectionSettingsFragment();

			getFragmentManager().beginTransaction().replace(R.id.
					main_fragment, fragment, "CONNSETTINGS").commit();
			break;
*/
            default:
                return super.onOptionsItemSelected(item);
        }


        return true;
    }

    public static void showToastMessage(final Context context, final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    /* End of the implementation of listeners */
    private final Runnable scrollTerminalToBottom = new Runnable() {
        @Override
        public void run() {
            // Scroll the terminal screen to the bottom
            //svTerminal.fullScroll(ScrollView.FOCUS_DOWN);
        }
    };

    public static AlertDialog showRetailerData(final Activity activity, CharSequence stringTitle, String TID, String MID) {
        Typeface tfBold = Typeface.createFromAsset(activity.getAssets(), "Interstate.ttf");
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_DARK);
        downloadDialog.setMessage("TID:" + TID + "\n" + "Retailer ID:" + MID);


        downloadDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        final TextView myView = new TextView(activity);
        myView.setText(stringTitle);
        myView.setTypeface(tfBold, Typeface.BOLD);
        myView.setGravity(Gravity.CENTER_HORIZONTAL);
        myView.setPadding(0, 10, 0, 10);
        myView.setTextColor(Color.parseColor("#FFFFFF"));
        myView.setTextSize(30);
        downloadDialog.setCustomTitle(myView);

        AlertDialog alert = downloadDialog.create();
        alert.show();
        // alert.getWindow().getAttributes();
        TextView msgTxt = (TextView) alert.findViewById(android.R.id.message);
        msgTxt.setGravity(Gravity.CENTER_HORIZONTAL);
        msgTxt.setPadding(0, 30, 0, 10);
        msgTxt.setTypeface(tfBold, Typeface.BOLD);

        Button cancelBtn = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        cancelBtn.setTextSize(20);
        cancelBtn.setTypeface(tfBold, Typeface.BOLD);
        Button yesBtn = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        yesBtn.setTextSize(20);
        yesBtn.setTypeface(tfBold, Typeface.BOLD);
        return alert;

    }

    @Override
    protected void onPause() {

        super.onPause();
        isRunning = false;


    }

    @Override
    protected void onResume() {

        super.onResume();
        isRunning = true;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void addToPendingStatus(PendingStatus pendingStatus) {
        pendingStatuses.add(pendingStatus);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        while (!pendingStatuses.isEmpty()) {
            pendingStatuses.remove().postStatus();
        }
    }
}
