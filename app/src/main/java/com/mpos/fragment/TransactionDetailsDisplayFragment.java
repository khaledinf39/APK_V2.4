package com.mpos.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mpos.Model.terminal;
import com.mpos.activity.MposBaseActivity;
import com.mpos.activity.R;
import com.mpos.mpossdk.api.TerminalStatus;
import com.mpos.utils.DataBaseHelper;

public class TransactionDetailsDisplayFragment extends Fragment implements
        OnClickListener {

	String printAuthId;
	String authId;

	TextView transType, transAmount, transDate, transTypeTxt, transAmountTxt,
			transDateTxt, approved, cashierName, cashierNameText, authCode,
			authCodeText, referenceNo, referenceNoText, terminalStatusTxt;
	Button transactionOKBtn;
	String completeResponse = "";
	String screenType="SALE";
	String terminalStatusCode = "00";


	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		final View view = inflater.inflate (R.layout.transaction_details, container,
				false);
		Typeface tfBold = Typeface.createFromAsset(getActivity().getAssets(),
				"Interstate.ttf");
		DataBaseHelper obj_DataBaseHelper = new DataBaseHelper(getActivity());
		transTypeTxt = (TextView) view.findViewById(R.id.transactiontypetxt);
		transTypeTxt.setTypeface(tfBold, Typeface.BOLD);

		//cashierName = (TextView) view.findViewById(R.id.cashiernametext);
		//cashierName.setTypeface(tfBold, Typeface.BOLD);

		//cashierNameText = (TextView) view.findViewById(R.id.cashiernameedittxt);
		//cashierNameText.setTypeface(tfBold, Typeface.BOLD);

		authCode = (TextView) view.findViewById(R.id.authcodetxt);
		authCode.setTypeface(tfBold, Typeface.BOLD);

		authCodeText = (TextView) view.findViewById(R.id.authcodeedittxt);
		authCodeText.setTypeface(tfBold, Typeface.BOLD);

		referenceNo = (TextView) view.findViewById(R.id.refnotxt);
		referenceNo.setTypeface(tfBold, Typeface.BOLD);

		referenceNoText = (TextView) view.findViewById(R.id.refnoedittxt);
		referenceNoText.setTypeface(tfBold, Typeface.BOLD);

		terminalStatusTxt = (TextView) view.findViewById(R.id.terminal_status_message);
		terminalStatusTxt.setTypeface(tfBold, Typeface.BOLD);

		approved = (TextView) view.findViewById(R.id.approved);
		approved.setTypeface(tfBold, Typeface.BOLD);

		transAmountTxt = (TextView) view
				.findViewById(R.id.transactionamounttxt);
		transAmountTxt.setTypeface(tfBold);
		transDateTxt = (TextView) view
				.findViewById(R.id.transactiondatetimetxt);
		transDateTxt.setTypeface(tfBold, Typeface.BOLD);

		transType = (TextView) view.findViewById(R.id.transactiontypeedittxt);
		transType.setTypeface(tfBold, Typeface.BOLD);
		transAmount = (TextView) view
				.findViewById(R.id.transactionamountedittext);
		transAmount.setTypeface(tfBold, Typeface.BOLD);
		transDate = (TextView) view.findViewById(R.id.transactiondateedittext);
		transDate.setTypeface(tfBold, Typeface.BOLD);
		transactionOKBtn = (Button) view.findViewById(R.id.transactionokbtn);
		transactionOKBtn.setTypeface(tfBold, Typeface.BOLD);

		if (screenType.equalsIgnoreCase("SALECOMP")) {
			approved.setText("AUTH");
			transactionOKBtn.setText("CAPTURE");
		}

		final Bundle bundle = this.getArguments();
		String type = bundle.getString("TRANSACTION_TYPE");
//		String amount = bundle.getString("TRANSACTION_AMOUNT");
		String amount = bundle.getString("Amount");
		String tid = bundle.getString("tid");
		final String lang=bundle.getString("language");

		String date = bundle.getString("TRANSACTION_DATE");
		authId = bundle.getString("TRANSACTION_AUTH_ID");
		String invoiceNo = bundle.getString("TRANSACTION_INVOICE_NO");
		String result = bundle.getString("TRANSACTION_RESULT");
		String responseCode = bundle.getString("TRANSACTION_RESPONSE_CODE");
		terminalStatusCode = bundle.getString("TERMINAL_STATUS_CODE");
		completeResponse = bundle.getString("TRANSACTION_COMPLETE_RESPONSE");


		transactionOKBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getFragmentManager().beginTransaction()
						.replace(R.id.main_fragment, new GridViewFragment(), "HOMESCREEN")
						.commit();

			/*	if(terminalStatusCode!=null && terminalStatusCode.equals("00")) {
					Bundle bundleSale = new Bundle();
					bundleSale.putString("CompleteResponse", completeResponse);
					DigitalReceiptFragment fragment = new DigitalReceiptFragment();
					fragment.setArguments(bundleSale);
					getFragmentManager()
							.beginTransaction()
							.replace(R.id.main_fragment, fragment,
									"ACAFRAGMENT")
							.commit();
				} else {
					getFragmentManager().beginTransaction()
							.replace(R.id.main_fragment, new GridViewFragment(), "HOMESCREEN")
							.commit();
				}*/


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

		TerminalStatus status = TerminalStatus.getStatus(terminalStatusCode);
		if(status!=null) {
			terminalStatusTxt.setText(terminalStatusCode+": "+status.getDescription());
		}
		if(terminalStatusCode!=null && terminalStatusCode.equals("00")) {
			approved.setText("APPROVED");

		} else {
			approved.setText("DECLINED");
			approved.setTextColor(getResources().getColor(R.color.red));
			terminalStatusTxt.setTextColor(getResources().getColor(R.color.red));
		}


		boolean resul;
		transType.setText(type);
		transAmount.setText(amount);
		transDate.setText(date);
		authCodeText.setText(authId);
		referenceNoText.setText(invoiceNo);



		//this code added by khaled zaid
//		https://khamsat.com/user/khaled-zaid

/// added by khaled zaid **************************************************************************************************************/
		Hide_sate_bar();
		Button click_btn=view.findViewById(R.id.click_btn);
		click_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("de.ozerov.fully");
				if (launchIntent != null) {
					startActivity(launchIntent);//null pointer check in case package name was not found
				}
			}
		});

		ImageView img=view.findViewById(R.id.img);
		if (lang.equals("ar")){
			img.setImageDrawable(getActivity().getDrawable(R.drawable.end_ar));
			click_btn.setBackgroundDrawable(getActivity().getDrawable(R.drawable.click_ar_btn));
		}else {
			img.setImageDrawable(getActivity().getDrawable(R.drawable.end_en));
			click_btn.setBackgroundDrawable(getActivity().getDrawable(R.drawable.click_btn));

		}
		terminal terminal_=new terminal(approved.getText().toString(),
				type,amount,date,authId,invoiceNo,tid);
		Toast.makeText(getActivity(),"Start saving...",Toast.LENGTH_SHORT).show();

		terminal_.Post_add(getActivity(), new terminal.OnCoupon_lisennter() {
	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Override
	public void onSuccess(int status) {
		if (status==200){


			Toast.makeText(getActivity(),"The data added to database with successfully",Toast.LENGTH_LONG).show();
			Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("de.ozerov.fully");
			if (launchIntent != null) {
				startActivity(launchIntent);//null pointer check in case package name was not found
			}
		}
	}
           @Override
           public void onSuccess(terminal ter){

			}
	@Override
	public void onStart() {

	}

	@Override
	public void onFailure(String msg) {
		Toast.makeText(getActivity(),"There is error :- "+ msg,Toast.LENGTH_LONG).show();
	}
});

		if(invoiceNo != null &&  ! invoiceNo.equals("") && ! invoiceNo.isEmpty())
			resul = obj_DataBaseHelper.InsertData(amount,invoiceNo.toString(),date,completeResponse,"Transaction_Table");




		return view;


	}
	private void Hide_sate_bar() {
		View decorView = getActivity().getWindow().getDecorView();

		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| View.SYSTEM_UI_FLAG_FULLSCREEN
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
				| View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY // hide status bar and nav bar after a short delay, or if the user interacts with the middle of the screen
		);
	}
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(false);
		((MposBaseActivity)getActivity()).hideMenu();
	}

	@Override
	public void onClick(View v) {

	}



}
