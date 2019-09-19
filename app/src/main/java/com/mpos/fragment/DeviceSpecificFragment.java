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
import com.mpos.mpossdk.api.data.devicespecific.ContactlessParam;
import com.mpos.mpossdk.api.data.devicespecific.DeviceSpecificFlags;

import java.util.List;

public class DeviceSpecificFragment extends ListFragment {

	private CusomListAdapter mAdapter;
	private DeviceSpecificFlags deviceSpecificFlags;

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
		textView.setText("Device Specific Flags");

		mAdapter = new CusomListAdapter(getActivity());

		List<ContactlessParam> contactlessParams = deviceSpecificFlags.getContactlessParams();

		for(int i = 0 ; i < contactlessParams.size(); i++ ) {
			ContactlessParam contactlessParam = contactlessParams.get(i);

			mAdapter.addSectionHeaderItem( contactlessParam.getID());
			mAdapter.addItem("TranCtlsLimit", contactlessParam.getTranCtlsLimit());
			mAdapter.addItem("CVMCtlsReqLimit", contactlessParam.getCVMCtlsReqLimit());
			mAdapter.addItem("CtlsFloorLimit", contactlessParam.getCtlsFloorLimit());

		}

		mAdapter.addItem("MAXSAFDepth", deviceSpecificFlags.getMAXSAFDepth());
		mAdapter.addItem("MAXSAFAmount", deviceSpecificFlags.getMAXSAFAmount());
		mAdapter.addItem("IdleTime", deviceSpecificFlags.getIdleTime());
		mAdapter.addItem("MAXReconAmount", deviceSpecificFlags.getMAXReconAmount());
		mAdapter.addItem("MAXReconCount", deviceSpecificFlags.getMAXReconCount());
		mAdapter.addItem("QRPrintIndicator", deviceSpecificFlags.getQRPrintIndicator());
		mAdapter.addItem("GPSLocation", deviceSpecificFlags.getGPSLocation());


		setListAdapter(mAdapter);

		return view;
	}
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(false);
		((MposBaseActivity)getActivity()).hideMenu();
	}

	public DeviceSpecificFlags getDeviceSpecificFlags() {
		return deviceSpecificFlags;
	}

	public void setDeviceSpecificFlags(DeviceSpecificFlags deviceSpecificFlags) {
		this.deviceSpecificFlags = deviceSpecificFlags;
	}
}
