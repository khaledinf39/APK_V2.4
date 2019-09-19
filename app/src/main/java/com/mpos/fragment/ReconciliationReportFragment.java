package com.mpos.fragment;

import android.app.ListFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mpos.activity.MposBaseActivity;
import com.mpos.activity.R;
import com.mpos.adapter.CustomAdapter;
import com.mpos.mpossdk.api.CardSchemeTotals;
import com.mpos.mpossdk.api.HostTotals;
import com.mpos.mpossdk.api.ReconciliationResult;
import com.mpos.mpossdk.api.TransactionTotal;
import com.mpos.utils.SharedClass;

import java.util.List;

public class ReconciliationReportFragment extends ListFragment {

	private CustomAdapter mAdapter;
	private ReconciliationResult reconciliationResult;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.recon_report, container, false);
		SharedClass obj_Shared = new SharedClass();
		try {
			String date = "";
			try {
				Bundle bundle = this.getArguments();
				date= bundle.getString("Date");

			}
			catch (Exception e)
			{
				date = obj_Shared.getCurrentDateTime(getActivity()).substring(0,10);

			}

			Typeface tfBold = Typeface.createFromAsset(getActivity().getAssets(),
					"Interstate.ttf");
			TextView txt_Date = (TextView) view.findViewById(R.id.text_Date);
			TextView textView = (TextView) view.findViewById(R.id.input_entry_label_label);
			TextView result = (TextView) view.findViewById(R.id.text_result);
			textView.setTypeface(tfBold, Typeface.BOLD);
			result.setTypeface(tfBold, Typeface.BOLD);
			result.setText(reconciliationResult.getResult());
			txt_Date.setText(date);
		}
		catch (Exception ex)
		{
			Toast.makeText(getActivity(),ex.toString(),Toast.LENGTH_LONG).show();
		}

		List<CardSchemeTotals> cardSchemeTotals = reconciliationResult.getCardSchemeTotals();

		mAdapter = new CustomAdapter(getActivity());
		for (int i = 0; i < cardSchemeTotals.size(); i++) {
			mAdapter.addSectionHeaderItem( cardSchemeTotals.get(i));

			List<HostTotals> hostTotals = cardSchemeTotals.get(i).getHostTotals();

			for (int j = 0; j < hostTotals.size(); j++) {
				mAdapter.addSectionHeaderItem(hostTotals.get(j));
				List<TransactionTotal> transactionTotals = hostTotals.get(j).getTotals();


				for (int k = 0; k < transactionTotals.size(); k++) {
					mAdapter.addItem(transactionTotals.get(k));
				}

			}
		}
		setListAdapter(mAdapter);

		view.setFocusableInTouchMode(true);
		view.requestFocus();
		view.setOnKeyListener( new View.OnKeyListener()
		{
			@Override
			public boolean onKey( View v, int keyCode, KeyEvent event )
			{
				if( keyCode == KeyEvent.KEYCODE_BACK )
				{
					getFragmentManager().beginTransaction()
							.replace(R.id.main_fragment, new GridViewFragment(), "HOMESCREEN").commit();
					return true;
				}
				return false;
			}
		} );

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		((MposBaseActivity)getActivity()).hideMenu();
	}

	public ReconciliationResult getReconciliationResult() {
		return reconciliationResult;
	}

	public void setReconciliationResult(ReconciliationResult reconciliationResult) {
		this.reconciliationResult = reconciliationResult;
	}
}
