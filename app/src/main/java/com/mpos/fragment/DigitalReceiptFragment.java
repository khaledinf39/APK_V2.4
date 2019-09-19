package com.mpos.fragment;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mpos.activity.MposBaseActivity;
import com.mpos.activity.R;

public class DigitalReceiptFragment extends Fragment  {

	Button sendReceipt, cancel;
	EditText mobileNumber, email, cashierPin;
	String completeResponse;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.send_digital_receipt, container, false);

		Typeface tfBold = Typeface.createFromAsset(getActivity().getAssets(),
				"Interstate.ttf");
		mobileNumber = (EditText) view.findViewById(R.id.rrn_edittext);
		email = (EditText) view.findViewById(R.id.email_edit_text);
		mobileNumber.setTypeface(tfBold, Typeface.BOLD);
		email.setTypeface(tfBold, Typeface.BOLD);

		sendReceipt = (Button) view.findViewById(R.id.process_btn);
		sendReceipt.setTypeface(tfBold, Typeface.BOLD);
		cancel = (Button) view.findViewById(R.id.cancle_btn);
		cancel.setTypeface(tfBold, Typeface.BOLD);

		Bundle bundle = this.getArguments();
		completeResponse = bundle.getString("CompleteResponse");
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getFragmentManager()
						.beginTransaction()
						.replace(R.id.main_fragment, new GridViewFragment(),
								"HOMESCREEN").commit();
			}
		});

		sendReceipt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ProgressFragment connectionFragment = new
						ProgressFragment(); Bundle bundle = new Bundle();
				bundle.putBoolean("DIGITAL_RECEIPT", true);
				bundle.putString("PhoneNumber", mobileNumber.getText().toString());
				bundle.putString("Email", email.getText().toString());
				bundle.putString("CompleteResponse", completeResponse);
				connectionFragment.setArguments(bundle);

				getFragmentManager().beginTransaction().replace(R.id.
						main_fragment, connectionFragment,"CONFRAGMENT").commit();
			}
		});

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
							.replace(R.id.main_fragment, new GridViewFragment(), "HOMESCREEN")
							.commit();
					return true;
				}
				return false;
			}
		} );
		return view;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(false);
		((MposBaseActivity)getActivity()).hideMenu();
	}

}
