package com.mpos.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.mpos.activity.MposBaseActivity;
import com.mpos.activity.R;
import com.mpos.adapter.CustomGridAdapter;
import com.mpos.mpossdk.api.MPOSService;
import com.mpos.mpossdk.api.MPOSServiceCallback;
import com.mpos.mpossdk.api.TerminalData;
import com.mpos.mpossdk.api.TransactionType;
import com.mpos.mpossdk.api.data.appsettings.AppSettings;
import com.mpos.utils.BluetoothConnectAsyntask;
import com.mpos.utils.SharedClass;
import com.wang.avi.AVLoadingIndicatorView;

import static com.mpos.activity.MposBaseActivity.showRetailerData;


public class GridViewFragment extends Fragment implements MenuItem.OnMenuItemClickListener {





Button ar_btn,en_btn;
	AVLoadingIndicatorView avi;
	ListView listView;
	GridView gridView;
	Fragment fragment;
	TextView bluetoothStatus;
	boolean connected=false;
	/*String[] web = { "SALE", "REFUND", "PRE AUTH", "CASHBACK", "CASH ADV",
			"VOID", "ADVICE", "RECON"};*/

	int[] imageId = { R.drawable.saleiconnew, R.drawable.refundnew,
			R.drawable.preauthnew, R.drawable.sale_comp,
			R.drawable.cash_dispersment, R.drawable.voidnew, R.drawable.adjustmentnew, R.drawable.adjustmentnew   };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.gridlayout, container, false);
		Resources res = getResources();

		String[] web =res.getStringArray(R.array.homeGread);
		TextView Languge = (TextView)view.findViewById(R.id.txtLanguge);

		Typeface tfBold = Typeface.createFromAsset(getActivity().getAssets(),
				"Interstate.ttf");
		CustomGridAdapter adapter = new CustomGridAdapter(getActivity(), web,
				imageId);
		gridView = (GridView) view.findViewById(R.id.grid);
		bluetoothStatus = (TextView) view.findViewById(R.id.bluetooth_status);

		BluetoothConnectAsyntask bluetoothAST = new BluetoothConnectAsyntask();
		bluetoothAST.execute();
		Languge.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				SharedClass obj_SharedClass = new SharedClass();
				obj_SharedClass.ChangeLanguage(getActivity());

			}
		});

////added by khaled zaid******************************************************************************************************************/
	Hide_sate_bar();
		avi=view.findViewById(R.id.avi_load);
		avi.show();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// run() method will be executed when 3 seconds have passed
if (!connected){
	Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("de.ozerov.fully");
	if (launchIntent != null) {
		startActivity(launchIntent);//null pointer check in case package name was not found
	}
}

			}
		}, 10000);



		final ImageView meun_btn=view.findViewById(R.id.img_menu);
		meun_btn.setOnClickListener(new View.OnClickListener() {

			@RequiresApi(api = Build.VERSION_CODES.M)
			@Override
			public void onClick(View v) {
showMenu(v);
			}
		});
		Bundle bund=GridViewFragment.this.getArguments();
		if (bund!=null) {
			Log.d("price  :", bund.getString("Amount") + "  tid :" + bund.getString("tid"));
			meun_btn.setVisibility(View.GONE);
		}
		  ar_btn=view.findViewById(R.id.ar_btn);
		ar_btn.setOnClickListener(new View.OnClickListener() {
			@RequiresApi(api = Build.VERSION_CODES.M)
			@Override
			public void onClick(View v) {
				contun_ORder("ar");

			}
		});
		 en_btn=view.findViewById(R.id.en_btn);
		en_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				contun_ORder("en");
			}
		});

		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

				switch (position) {
				case 0:
					Bundle bundle = new Bundle();


					Bundle bund=GridViewFragment.this.getArguments();
					if (bund!=null) {
						Log.d("price  :", bund.getString("Amount") + "  tid :" + bund.getString("tid"));

						ProgressFragment connectionFragment = new ProgressFragment();
						bundle.putBoolean("TSF", true);
						bundle.putString("CashBack", bund.getString("Amount"));
						bundle.putString("Amount", bund.getString("Amount"));
						bundle.putString("tid", bund.getString("tid"));
						bundle.putInt("TransactionType", TransactionType.SALE.getValue());

						connectionFragment.setArguments(bundle);

						getFragmentManager().beginTransaction().replace(R.id.
								main_fragment, connectionFragment, "CONFRAGMENT").commit();



					}else {
						Fragment fragment = new AmountEntryFragment();

						bundle.putInt("TransactionType", TransactionType.SALE.getValue());
						fragment.setArguments(bundle);
						getFragmentManager()
								.beginTransaction()
								.replace(R.id.main_fragment,
										fragment,
										"TSAFRAGMENT").commit();
					}
					break;

				case 1:
					fragment = new AmountEntryFragment();
					bundle = new Bundle();
					bundle.putInt("TransactionType", TransactionType.REFUND.getValue());
					fragment.setArguments(bundle);
					getFragmentManager()
							.beginTransaction()
							.replace(R.id.main_fragment,
									fragment,
									"TSAFRAGMENT").commit();

					break;

				case 2:
					fragment = new AmountEntryFragment();
					bundle = new Bundle();
					bundle.putInt("TransactionType", TransactionType.PREAUTH.getValue());
					fragment.setArguments(bundle);
					getFragmentManager()
							.beginTransaction()
							.replace(R.id.main_fragment,
									fragment,
									"TSAFRAGMENT").commit();
					break;

				case 3:
					fragment = new AmountEntryFragment();
					bundle = new Bundle();
					bundle.putInt("TransactionType", TransactionType.CASHBACK.getValue());
					fragment.setArguments(bundle);
					getFragmentManager()
							.beginTransaction()
							.replace(R.id.main_fragment,
									fragment,
									"TSAFRAGMENT").commit();
					break;

				case 4:
					fragment = new AmountEntryFragment();
					bundle = new Bundle();
					bundle.putInt("TransactionType", TransactionType.CASH_ADVANCE.getValue());
					fragment.setArguments(bundle);
					getFragmentManager()
							.beginTransaction()
							.replace(R.id.main_fragment,
									fragment,
									"TSAFRAGMENT").commit();
					break;

				case 5:
					ProgressFragment connectionFragment = new
							ProgressFragment();
					bundle = new Bundle();
					bundle.putBoolean("TSF", true);
					connectionFragment.setArguments(bundle);
					bundle.putBoolean("TransactionType", true);
					bundle.putInt("TransactionType", TransactionType.VOID.getValue());
					connectionFragment.setArguments(bundle);

					getFragmentManager().beginTransaction().replace(R.id.
							main_fragment, connectionFragment,"CONFRAGMENT").commit();
					break;

				case 6:
					fragment = new AmountEntryFragment();
					bundle = new Bundle();
					bundle.putInt("TransactionType", TransactionType.ADVICE.getValue());
					fragment.setArguments(bundle);
					getFragmentManager()
							.beginTransaction()
							.replace(R.id.main_fragment,
									fragment,
									"TSAFRAGMENT").commit();
					break;

				case 7:
					connectionFragment = new
							ProgressFragment();
					bundle = new Bundle();
					bundle.putBoolean("TSF", true);
					connectionFragment.setArguments(bundle);
					bundle.putBoolean("TransactionType", true);
					bundle.putInt("TransactionType", TransactionType.RECONCILIATION.getValue());
					connectionFragment.setArguments(bundle);

					getFragmentManager().beginTransaction().replace(R.id.
							main_fragment, connectionFragment,"CONFRAGMENT").commit();
					break;

					/*connectionFragment = new
							ProgressFragment();
					bundle = new Bundle();
					bundle.putBoolean("TSF", true);
					connectionFragment.setArguments(bundle);
					bundle.putBoolean("TransactionType", true);
					bundle.putInt("TransactionType", TransactionType.RECONCILIATION.getValue());
					connectionFragment.setArguments(bundle);

					getFragmentManager().beginTransaction().replace(R.id.
							main_fragment, connectionFragment,"CONFRAGMENT").commit();
					break;
*/

				}

			}
		});

		gridView.setOnKeyListener( new View.OnKeyListener()
		{
			@Override
			public boolean onKey( View v, int keyCode, KeyEvent event )
			{
				if( keyCode == KeyEvent.KEYCODE_BACK )
				{
					unRegisterBroadcast();

					return false;
				}
				return false;
			}
		} );


		registerBroadcast();
		updateBluetoothName();

		return view;

	}

	private void Hide_sate_bar() {
		View decorView = getActivity().getWindow().getDecorView();

		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| View.SYSTEM_UI_FLAG_FULLSCREEN
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
				| View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY // hide status bar and nav bar after a short delay, or if the user interacts with the middle of the screen
		);
	}

	@RequiresApi(api = Build.VERSION_CODES.M)
	public void showMenu(View viw)
	{

		View v=getView().findViewById(R.id.img_menu);
		//Creating the instance of PopupMenu
		PopupMenu popup = new PopupMenu(getActivity(), v);
		//Inflating the Popup using xml file
		popup.getMenuInflater().inflate(R.menu.settings_screen, popup.getMenu());



		popup.show();//showing popup menu

		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				return GridViewFragment.this.onMenuItemClick(item);
			}
		});// to implement on click event on items of menu
//
	}
	private void contun_ORder(String lang) {
		Bundle bundle = new Bundle();


		Bundle bund=GridViewFragment.this.getArguments();
		if (bund!=null) {
			Log.d("price  :", bund.getString("price") + "  tid :" + bund.getString("tid"));

			ProgressFragment connectionFragment = new ProgressFragment();
			bundle.putBoolean("TSF", true);
			bundle.putString("CashBack", bund.getString("Amount"));
			bundle.putString("Amount", bund.getString("Amount"));
			bundle.putString("tid", bund.getString("tid"));
			bundle.putInt("TransactionType", TransactionType.SALE.getValue());
			bundle.putString("language", lang);
			connectionFragment.setArguments(bundle);

			getFragmentManager().beginTransaction().replace(R.id.
					main_fragment, connectionFragment, "CONFRAGMENT").commit();



		}else {
			Fragment fragment = new AmountEntryFragment();

			bundle.putInt("TransactionType", TransactionType.SALE.getValue());
			fragment.setArguments(bundle);
			getFragmentManager()
					.beginTransaction()
					.replace(R.id.main_fragment,
							fragment,
							"TSAFRAGMENT").commit();
		}
	}


	@Override
	public void onResume() {
		super.onResume();
		((MposBaseActivity)getActivity()).showMenu();
	}

	@Override
	public void onStart() {
		super.onStart();
		updateBluetoothName();
	}




	BroadCstListener notifyListener = new BroadCstListener();

	private void registerBroadcast() {

		IntentFilter intentFilter = new IntentFilter("com.mpos.bluetooth.status");

		getActivity().getApplication().registerReceiver(notifyListener,
				intentFilter);

	}

	private void unRegisterBroadcast() {

		try {
			getActivity().getApplication().unregisterReceiver(notifyListener);
		} catch (Exception e) {

		}


	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {


		switch (item.getItemId()) {


			case R.id.menu_connect_id:
				Log.d("menu", "menu_connect_id");
				MPOSService.getInstance(getActivity()).showDeviceListDialog();
				break;
			case R.id.menu_get_terminal_data:
				Log.d("menu", "menu_connect_id");

				if (!MPOSService.getInstance(getActivity()).isDeviceConnected()) {
					Toast.makeText(getActivity(), "Device not connected", Toast.LENGTH_LONG).show();
					return true;
				}

				MPOSService.getInstance(getActivity()).getTerminalData(new MPOSServiceCallback() {
					@Override
					public void onComplete(int status, String message, Object result) {
						TerminalData retailerData = (TerminalData) result;

						showRetailerData(getActivity(), getResources().getText(R.string.warning_dialog).toString(), retailerData.getTID(), retailerData.getMID());

					}

					@Override
					public void onFailed(int status, String message) {
						showToastMessage(getActivity(), message);
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
					Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
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
					Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
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
				MPOSService.getInstance(getActivity()).checkExistence(new MPOSServiceCallback() {
					@Override
					public void onComplete(int status, final String message, Object result) {
						showToastMessage(getActivity(), message);
					}

					@Override
					public void onFailed(int status, final String message) {
						showToastMessage(getActivity(), message);
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
				MPOSService.getInstance(getActivity()).setLanguage("English", new MPOSServiceCallback() {
					@Override
					public void onComplete(int status, String message, Object result) {
						showToastMessage(getActivity(), message);
					}

					@Override
					public void onFailed(int status, String message) {
						showToastMessage(getActivity(), message);
					}
				});
				break;

			case R.id.menu_set_lanuguage_arabic:
				Log.d("menu", "menu_set_lanuguage");
				MPOSService.getInstance(getActivity()).setLanguage("Arabic", new MPOSServiceCallback() {
					@Override
					public void onComplete(int status, String message, Object result) {
						showToastMessage(getActivity(), message);
					}

					@Override
					public void onFailed(int status, String message) {
						showToastMessage(getActivity(), message);
					}
				});
				break;

			case R.id.menu_get_app_settings:
				Log.d("menu", "menu_get_app_settings");
				MPOSService.getInstance(getActivity()).getAppSettings(new MPOSServiceCallback() {
					@Override
					public void onComplete(int status, String message, Object result) {
						//showToastMessage(getApplicationContext(), message+":"+(String)result);

						String xmlResponse = (String) result;

						AppSettings appSettings = MPOSService.getInstance(getActivity()).parseAppSettingsResponse(xmlResponse);

						AppSettingsFragment fragment = new AppSettingsFragment();
						fragment.setAppSettings(appSettings);

						getFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment, "APPSETTINGS").commit();
					}

					@Override
					public void onFailed(int status, String message) {
						showToastMessage(getActivity(), message);
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


	public class BroadCstListener extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Bundle bundle = intent.getExtras();
			if (intent.getAction().equalsIgnoreCase("com.mpos.bluetooth.status")) {
				updateBluetoothName();
			}

		}

	}


	public void updateBluetoothName(){
	SharedClass obj_shar = new SharedClass();
		MPOSService service = MPOSService.getInstance(getActivity());
		String deviceName = service.getConnectedDeviceName();

		if(deviceName==null || deviceName.length() == 0) {
			deviceName = "NOT CONNECTED";

		}
		else {
			obj_shar.setTerminalId(getActivity());
			connected=true;
			avi.hide();
			ar_btn.setVisibility(View.VISIBLE);
			en_btn.setVisibility(View.VISIBLE);
		}
		bluetoothStatus.setText("MPOS Device: "+deviceName);

	}

}
