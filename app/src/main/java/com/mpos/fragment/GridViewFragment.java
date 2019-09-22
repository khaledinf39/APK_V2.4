package com.mpos.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.mpos.activity.MposBaseActivity;
import com.mpos.activity.R;
import com.mpos.adapter.CustomGridAdapter;
import com.mpos.mpossdk.api.MPOSService;
import com.mpos.mpossdk.api.TransactionType;
import com.mpos.utils.BluetoothConnectAsyntask;
import com.mpos.utils.SharedClass;


public class GridViewFragment extends Fragment {

	ListView listView;
	GridView gridView;
	Fragment fragment;
	TextView bluetoothStatus;
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
						Log.d("price  :", bund.getString("price") + "  tid :" + bund.getString("tid"));

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

		Button gotoFK=view.findViewById(R.id.gotoFK);
		gotoFK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("de.ozerov.fully");
				if (launchIntent != null) {
					startActivity(launchIntent);//null pointer check in case package name was not found
				}
			}
		});
		registerBroadcast();
		updateBluetoothName();

		return view;
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

		if(deviceName==null || deviceName.length() == 0)
			deviceName = "NOT CONNECTED";
		else obj_shar.setTerminalId(getActivity());
		bluetoothStatus.setText("MPOS Device: "+deviceName);


///////لخ شعفخةشفهضعق ف
//		Bundle bundle = new Bundle();
//
//
//		Bundle bund=GridViewFragment.this.getArguments();
//		if (bund!=null) {
//			Log.d("price  :", bund.getString("price") + "  tid :" + bund.getString("tid"));
//
//			ProgressFragment connectionFragment = new ProgressFragment();
//			bundle.putBoolean("TSF", true);
//			bundle.putString("CashBack", bund.getString("Amount"));
//			bundle.putString("Amount", bund.getString("Amount"));
//			bundle.putString("tid", bund.getString("tid"));
//			bundle.putInt("TransactionType", TransactionType.SALE.getValue());
//
//			connectionFragment.setArguments(bundle);
//
//			getFragmentManager().beginTransaction().replace(R.id.
//					main_fragment, connectionFragment, "CONFRAGMENT").commit();
//
//
//
//		}
	}

}
