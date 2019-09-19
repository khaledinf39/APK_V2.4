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
import com.mpos.mpossdk.api.data.application.Application;

import java.util.List;

public class AIDListFragment extends ListFragment {

	private CusomListAdapter mAdapter;
	private List<Application> applications;

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
		textView.setText("AID List");

		mAdapter = new CusomListAdapter(getActivity());

		for(int i = 0 ; i < applications.size(); i++ ) {
			Application application = applications.get(i);

			mAdapter.addSectionHeaderItem( application.getAIDLabel());
			mAdapter.addItem("AID", application.getAID());
			mAdapter.addItem("AID Version", application.getAIDVersion());
			mAdapter.addItem("AID Fullmatch", application.getAIDFullMatch());
			mAdapter.addItem("DenialActionCode", application.getDenialActionCode());
			mAdapter.addItem("OnlineActionCode", application.getOnlineActionCode());
			mAdapter.addItem("DefaultActionCode", application.getDefaultActionCode());
			mAdapter.addItem("TargetPercent", application.getTargetPercent());
			mAdapter.addItem("MaxPercent", application.getMaxPercent());
			mAdapter.addItem("ThresholdAmount", application.getThresholdAmount());
			mAdapter.addItem("TDOL", application.getTDOL());
			mAdapter.addItem("DDOL", application.getDDOL());
			mAdapter.addItem("EMVAdditionalTags", application.getEMVAdditionalTags());

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

	public List<Application> getApplications() {
		return applications;
	}

	public void setApplications(List<Application> applications) {
		this.applications = applications;
	}
}
