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
import com.mpos.mpossdk.api.data.publickey.PublicKey;

import java.util.List;

public class PublicKeysFragment extends ListFragment {

	private CusomListAdapter mAdapter;
	private List<PublicKey> publicKeys;

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
		textView.setText("Public Keys");

		mAdapter = new CusomListAdapter(getActivity());

		mAdapter.addSectionHeaderItem( "Application Settings");

		for(int i = 0 ; i < publicKeys.size(); i++ ) {
			PublicKey publicKey = publicKeys.get(i);

			mAdapter.addSectionHeaderItem( publicKey.getRID());
			mAdapter.addItem("Key", publicKey.getKey());
			mAdapter.addItem("Length", publicKey.getPublicKeyLength());
			mAdapter.addItem("Index", publicKey.getKeyIndex());
			mAdapter.addItem("Exponent", publicKey.getPublicKeyExponent());
			mAdapter.addItem("Expiry Date", publicKey.getPublicKeyExpiryDate());
			mAdapter.addItem("Checksum", publicKey.getPublicKeyChecksum());

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

	public List<PublicKey> getPublicKeys() {
		return publicKeys;
	}

	public void setPublicKeys(List<PublicKey> publicKeys) {
		this.publicKeys = publicKeys;
	}
}
