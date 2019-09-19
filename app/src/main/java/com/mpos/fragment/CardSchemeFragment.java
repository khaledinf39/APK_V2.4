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
import com.mpos.mpossdk.api.data.cardscheme.CardScheme;

import java.util.List;

public class CardSchemeFragment extends ListFragment {

	private CusomListAdapter mAdapter;
	private List<CardScheme> cardSchemes;

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
		textView.setText("Card Schemes");

		mAdapter = new CusomListAdapter(getActivity());

		for(int i = 0 ; i < cardSchemes.size(); i++ ) {
			CardScheme cardScheme = cardSchemes.get(i);

			mAdapter.addSectionHeaderItem( cardScheme.getSchemeNameEng());
			mAdapter.addItem("SchemeID", cardScheme.getSchemeID());
			mAdapter.addItem("Term", cardScheme.getTerm());
			mAdapter.addItem("Merch", cardScheme.getMerch());
			mAdapter.addItem("MCC", cardScheme.getMCC());
			mAdapter.addItem("Acquirer", cardScheme.getAcquirer());
			mAdapter.addItem("Max Cashback", cardScheme.getMaxCashback());
			mAdapter.addItem("Max Amount Trans", cardScheme.getMaxAmountTrans());
			mAdapter.addItem("EMV Enabled", cardScheme.getEnableEmv());
			mAdapter.addItem("Service Code", cardScheme.getServiceCode());
			mAdapter.addItem("Offline Refund Flag", cardScheme.getOfflineRefundFlag());
			mAdapter.addItem("Floor Limit", cardScheme.getFloorLimit());
			mAdapter.addItem("Fallback Limit", cardScheme.getFallbackLimit());
			mAdapter.addItem("Luhn Check", cardScheme.getLuhnCheck());
			mAdapter.addItem("Trans Allowed", cardScheme.getTransAllowed());
			mAdapter.addItem("Card Holder Auth", cardScheme.getCardHolderAuth());
			mAdapter.addItem("Supervisor Auth", cardScheme.getSupervisorAuth());
			mAdapter.addItem("Manual Entry Allowed", cardScheme.getManualEntryAllowed());
			mAdapter.addItem("Max Trans Amount", cardScheme.getMaxtransAmountFlag());
			mAdapter.addItem("Prefix", cardScheme.getPrefix());
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

	public List<CardScheme> getCardSchemes() {
		return cardSchemes;
	}

	public void setCardSchemes(List<CardScheme> cardSchemes) {
		this.cardSchemes = cardSchemes;
	}
}
