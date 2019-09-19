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
import com.mpos.mpossdk.api.data.comm.CommSettings;

import java.util.List;

public class CommSettingsFragment extends ListFragment {

	private CusomListAdapter mAdapter;
	private List<CommSettings> commSettingses;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.recon_report, container, false);


		Typeface tfBold = Typeface.createFromAsset(getActivity().getAssets(),
				"Interstate.ttf");

		TextView textView = (TextView) view.findViewById(R.id.input_entry_label_label);
		TextView result = (TextView) view.findViewById(R.id.text_result);
		textView.setTypeface(tfBold, Typeface.BOLD);
		result.setTypeface(tfBold, Typeface.BOLD);
		result.setText("");
		textView.setText("Comm Settings");

		mAdapter = new CusomListAdapter(getActivity());

		for(int i = 0 ; i < commSettingses.size(); i++ ) {
			CommSettings commSettings = commSettingses.get(i);
			mAdapter.addSectionHeaderItem( commSettings.getConnectionType());
			mAdapter.addItem("Priority", commSettings.getPriority());
			mAdapter.addItem("IP Address", commSettings.getIPAddress());
			mAdapter.addItem("TCP Port", commSettings.getTCPPort());
			mAdapter.addItem("Retries", commSettings.getRetries());
			mAdapter.addItem("Timeout", commSettings.getTimeout());
			mAdapter.addItem("Telephone", commSettings.getTelephone());
			mAdapter.addItem("APN", commSettings.getApn());
			mAdapter.addItem("IP Address 2", commSettings.getIPAddress2());
			mAdapter.addItem("TCP Port 2", commSettings.getTCPPort2());
			mAdapter.addItem("Retries 2", commSettings.getRetries2());
			mAdapter.addItem("Timeout 2", commSettings.getTimeout2());
			mAdapter.addItem("Telephone 2", commSettings.getTelephone2());
			mAdapter.addItem("APN 2", commSettings.getApn2());

		}
		setListAdapter(mAdapter);

		return view;
	}
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(false);
		((MposBaseActivity)getActivity()).hideMenu();
	}

	public List<CommSettings> getCommSettingses() {
		return commSettingses;
	}

	public void setCommSettingses(List<CommSettings> commSettingses) {
		this.commSettingses = commSettingses;
	}
}
