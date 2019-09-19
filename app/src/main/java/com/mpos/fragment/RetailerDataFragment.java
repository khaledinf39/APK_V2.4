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
import com.mpos.mpossdk.api.data.retailer.AcquirerData;
import com.mpos.mpossdk.api.data.retailer.Retailer;
import com.mpos.mpossdk.api.data.retailer.RetailerData;

public class RetailerDataFragment extends ListFragment {

	private CusomListAdapter mAdapter;
	private RetailerData retailerData;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.recon_report, container, false);



		Typeface tfBold = Typeface.createFromAsset(getActivity().getAssets(),
				"Interstate.ttf");

		TextView textView = (TextView) view.findViewById(R.id.input_entry_label_label);
		TextView result = (TextView) view.findViewById(R.id.text_result);
		textView.setTypeface(tfBold, Typeface.BOLD);

		textView.setText("Retailer Data");

		result.setTypeface(tfBold, Typeface.BOLD);
		result.setText("");

		mAdapter = new CusomListAdapter(getActivity());

		mAdapter.addSectionHeaderItem( "Retailer");

		Retailer retailer = retailerData.getRetailer();

		mAdapter.addItem("Retailer Name Eng", retailer.getRetailerNameEng());
		mAdapter.addItem("Retailer Number", retailer.getRetailerNumber());
		mAdapter.addItem("Download Phone", retailer.getDownloadPhone());
		mAdapter.addItem("Terminal SerialNo", retailer.getTerminalSerialNo());
		mAdapter.addItem("Currency Code", retailer.getCurrencyCode());
		mAdapter.addItem("Currency Exponent", retailer.getCurrencyExponent());
		mAdapter.addItem("EMV Term Type", retailer.getEmvTermType());
		mAdapter.addItem("Currency Symbol Eng", retailer.getCurrencySymbolEng());

		mAdapter.addItem("Currency Symbol Arb", retailer.getCurrencySymbolArb());
		mAdapter.addItem("Retailer Language", retailer.getRetailerLanguage());
		mAdapter.addItem("Auto Load Flag", retailer.getAutoLoadFlag());
		mAdapter.addItem("SAF Retry Limit", retailer.getSAFRetryLimit());
		mAdapter.addItem("SAF Limit", retailer.getSAFLimit());

		AcquirerData acquirerData = retailerData.getAcquirerData();


		mAdapter.addSectionHeaderItem( "Acquirer Data");
		mAdapter.addItem("Next Download Time", acquirerData.getNextDownloadTime());
		mAdapter.addItem("Next Recon Time", acquirerData.getNextReconTIme());
		mAdapter.addItem("Terminal Capability", acquirerData.getTerminalCapability());

		mAdapter.addItem("Addtional Capability", acquirerData.getAddtionalCapability());
		mAdapter.addItem("Terminal Id", acquirerData.getTerminalId());
		mAdapter.addItem("Acceptor Id", acquirerData.getAcceptorId());
		mAdapter.addItem("Acquirer Inst Id", acquirerData.getAcquirerInstId());

		setListAdapter(mAdapter);

		return view;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(false);
		((MposBaseActivity)getActivity()).hideMenu();
	}


	public RetailerData getRetailerData() {
		return retailerData;
	}

	public void setRetailerData(RetailerData retailerData) {
		this.retailerData = retailerData;
	}
}
