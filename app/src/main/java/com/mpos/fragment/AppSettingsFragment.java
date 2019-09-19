package com.mpos.fragment;

import android.app.ListFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mpos.activity.MposBaseActivity;
import com.mpos.activity.R;
import com.mpos.adapter.CusomListAdapter;
import com.mpos.mpossdk.api.data.appsettings.AppSettings;

public class AppSettingsFragment extends ListFragment {

	private CusomListAdapter mAdapter;
	private AppSettings appSettings;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.recon_report, container, false);



		Typeface tfBold = Typeface.createFromAsset(getActivity().getAssets(),
				"Interstate.ttf");

		TextView textView = (TextView) view.findViewById(R.id.input_entry_label_label);
		TextView result = (TextView) view.findViewById(R.id.text_result);
		textView.setTypeface(tfBold, Typeface.BOLD);

		textView.setText("Application Settings");

		result.setTypeface(tfBold, Typeface.BOLD);
		result.setText("");

		mAdapter = new CusomListAdapter(getActivity());



		mAdapter.addSectionHeaderItem( "Application Settings");

		mAdapter.addItem("Version", appSettings.getVersion());
		mAdapter.addItem("Terminal ID", appSettings.getTerminalID());
		mAdapter.addItem("Serial No", appSettings.getSerialNumber());

		mAdapter.addSectionHeaderItem( "Key Version");
		mAdapter.addItem("Host", appSettings.getKeysVersion().getHost());
		mAdapter.addItem("SSL Enabled", appSettings.getKeysVersion().getSsl());

		mAdapter.addItem("Integration Type", appSettings.getIntegration().getType());

		mAdapter.addItem("GPS Location", appSettings.getGpsLocation());

		mAdapter.addSectionHeaderItem( "Local IP");
		mAdapter.addItem("IP", appSettings.getLocalIP().getIP());
		mAdapter.addItem("Port", appSettings.getLocalIP().getPort());
		mAdapter.addItem("DNS", appSettings.getLocalIP().getDNS());
		mAdapter.addItem("Gateway", appSettings.getLocalIP().getGateWay());
		mAdapter.addItem("Subnet Mask", appSettings.getLocalIP().getSubnetMask());
		mAdapter.addItem("Terminal Type", appSettings.getLocalIP().getTerminalType());

		mAdapter.addSectionHeaderItem( "Destination IP");
		mAdapter.addItem("IP", appSettings.getDestinationIP().getIP());
		mAdapter.addItem("Port", appSettings.getDestinationIP().getPort());
		mAdapter.addItem("DNS", appSettings.getDestinationIP().getDNS());
		mAdapter.addItem("Gateway", appSettings.getDestinationIP().getGateWay());
		mAdapter.addItem("Subnet Mask", appSettings.getDestinationIP().getSubnetMask());
		mAdapter.addItem("Terminal Type", appSettings.getDestinationIP().getTerminalType());

		mAdapter.addSectionHeaderItem( "Configuration");
		mAdapter.addItem("Auto Recon", appSettings.getAppConfiguration().getAutoRecon());
		mAdapter.addItem("Instant Recon", appSettings.getAppConfiguration().getInstantRecon());
		mAdapter.addItem("Instant Advice", appSettings.getAppConfiguration().getInstantAdvice());
		mAdapter.addItem("GUI", appSettings.getAppConfiguration().getGUI());

		setListAdapter(mAdapter);

		return view;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(false);
		((MposBaseActivity)getActivity()).hideMenu();
	}

	public AppSettings getAppSettings() {
		return appSettings;
	}

	public void setAppSettings(AppSettings appSettings) {
		this.appSettings = appSettings;
	}
}
