package com.mpos.fragment;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mpos.activity.MposBaseActivity;
import com.mpos.activity.R;
import com.mpos.mpossdk.api.MPOSService;
import com.mpos.mpossdk.api.MPOSServiceCallback;
import com.mpos.mpossdk.api.ReconciliationResult;
import com.mpos.mpossdk.api.TerminalStatus;
import com.mpos.mpossdk.api.TransactionRequest;
import com.mpos.mpossdk.api.TransactionType;
import com.mpos.utils.DataBaseHelper;
import com.mpos.utils.PendingStatus;
import com.mpos.utils.SharedClass;

import java.util.HashMap;


public class ProgressFragment extends Fragment {

	private TextView textView;
	ProgressBar progress;
	Boolean isTrasaction;
	Boolean isLastTransactionResult=false;
	Boolean isLastReconciliationResult=false;
	Boolean isSendingDigitalReceipt;
	String phoneNumber = null;
	String maskedPan = null;
	String email = null;
	String completeResponse = null;
	String rrn = null;
	String tid = "";
	String Amount = "00.00";
	TransactionType transactionType = TransactionType.SALE;
	SharedClass obj_sharedClass = new SharedClass();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.connection, container, false);

		Typeface tfBold = Typeface.createFromAsset(getActivity().getAssets(),
				"Interstate.ttf");

		textView = (TextView) view.findViewById(R.id.search_device_txt);
		textView.setTypeface(tfBold, Typeface.BOLD);
		final ImageView connectionDone = (ImageView) view
				.findViewById(R.id.connectionsuccess);
		progress = (ProgressBar) view
				.findViewById(R.id.self_service_confirm_progress);

		Bundle bundle = this.getArguments();

		tid = bundle.getString("tid");
		Amount = bundle.getString("Amount");

		isTrasaction = bundle.getBoolean("TSF");
		transactionType = TransactionType.getTransactionType(bundle.getInt("TransactionType", 1));
		isLastTransactionResult = bundle.getBoolean("LastTransactionResult", false);
		isLastReconciliationResult = bundle.getBoolean("LastReconciliationResult", false);
		rrn = bundle.getString("RRN", null);
		String cashbackAmount = bundle.getString("CashBack", "");
		String amount = bundle.getString("Amount", "");
		String maskedPan = bundle.getString("MaskedPAN", "");
		String authCode = bundle.getString("AuthCode", "");

		isSendingDigitalReceipt = bundle.getBoolean("DIGITAL_RECEIPT");


		if(isTrasaction)
			textView.setText("PLEASE WAIT WHILE PROCESSING...");
		else if(isSendingDigitalReceipt) {
			phoneNumber = bundle.getString("PhoneNumber");
			email= bundle.getString("Email");
			completeResponse = bundle.getString("CompleteResponse");
			textView.setText("PLEASE WAIT WHILE SENDING DIGITAL RECEIPT...");
		}
		else
			textView.setText("PLEASE WAIT...");

		if(isTrasaction) {

			if(transactionType == TransactionType.RECONCILIATION) {
				amount = obj_sharedClass.getEnglishNumbers(amount);
				String xmlRequest = "<TransactionRequest>" +
						"<Command></Command>" +
						"<Amount></Amount>" +
						"<MaskedPAN></MaskedPAN>" +
						"<PrintFlag>01</PrintFlag>" +
						"<Phone></Phone>" +
						"<Email></Email>" +
						"<UserId></UserId>" +
						"<DeviceId></DeviceId>" +
						"<RRN></RRN>" +
						"<AuthCode></AuthCode>" +
						"<AdditionalData>01</AdditionalData>" +
						"</TransactionRequest>";

				xmlRequest = xmlRequest.replaceAll("<Amount></Amount>", "<Amount>"+amount+"</Amount>");
				xmlRequest = xmlRequest.replaceAll("<Command></Command>", "<Command>"+transactionType.getName()+"</Command>");

				MPOSService.getInstance(getActivity()).startTransaction(xmlRequest, reconciliationCallback);
			} else if (isLastTransactionResult) {
				MPOSService.getInstance(getActivity()).getLastTransactionResult(transactionCallback);
			} else if (isLastReconciliationResult) {
				MPOSService.getInstance(getActivity()).getLastReconciliationResult(reconciliationCallback);
			} else {

				TransactionRequest txnRequest = new TransactionRequest();
				txnRequest.setTransactionType(transactionType);
				txnRequest.setTransactionAmount(amount);
				txnRequest.setRrn(rrn);

				String xmlRequest = "<TransactionRequest>" +
                        "<Command></Command>" +
                        "<Amount></Amount>" +
						"<Cashback></Cashback>" +
						"<MaskedPAN></MaskedPAN>" +
                        "<PrintFlag>01</PrintFlag>" +
                        "<Phone></Phone>" +
                        "<Email></Email>" +
                        "<UserId></UserId>" +
                        "<DeviceId></DeviceId>" +
						"<RRN></RRN>" +
						"<AuthCode></AuthCode>" +
						"<AdditionalData>01</AdditionalData>" +
                        "</TransactionRequest>";

				//xmlRequest = xmlRequest.replaceAll("<Amount></Amount>", "<Amount>"+amount+"</Amount>");
				xmlRequest = xmlRequest.replaceAll("<Amount></Amount>", "<Amount>"+amount+"</Amount>");
				xmlRequest = xmlRequest.replaceAll("<RRN></RRN>", "<RRN>"+rrn+"</RRN>");
				xmlRequest = xmlRequest.replaceAll("<MaskedPAN></MaskedPAN>", "<MaskedPAN>"+maskedPan+"</MaskedPAN>");
				xmlRequest = xmlRequest.replaceAll("<Cashback></Cashback>", "<Cashback>"+cashbackAmount+"</Cashback>");
				xmlRequest = xmlRequest.replaceAll("<Command></Command>", "<Command>"+transactionType.getName()+"</Command>");
				xmlRequest = xmlRequest.replaceAll("<AuthCode></AuthCode>", "<AuthCode>"+authCode+"</AuthCode>");

				MPOSService.getInstance(getActivity()).startTransaction(xmlRequest, transactionCallback);

			}
		} else if(isSendingDigitalReceipt){

			MPOSService.getInstance(getActivity()).sendDigitalReceipt(phoneNumber, email, completeResponse, new MPOSServiceCallback() {


				@Override
				public void onComplete(int status, final String message, Object result) {

					if(!((MposBaseActivity)getActivity()).isRunning()) {
						((MposBaseActivity)getActivity()).addToPendingStatus(new PendingStatus(this, true, status, message, result));
						return;
					}

					MPOSService.getInstance(getActivity()).cancelTransaction();

					new Handler(Looper.getMainLooper()).post(new Runnable() {
						@Override
						public void run() {
							isSendingDigitalReceipt = false;
//							Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
//							getFragmentManager()
//									.beginTransaction()
//									.replace(R.id.main_fragment, new GridViewFragment(),
//											"HOMESCREEN").commit();
						}
					});
				}

				@Override
				public void onFailed(int status, final String message) {

					if(!((MposBaseActivity)getActivity()).isRunning()) {
						((MposBaseActivity)getActivity()).addToPendingStatus(new PendingStatus(this, true, status, message, null));
						return;
					}

					MPOSService.getInstance(getActivity()).cancelTransaction();
					new Handler(Looper.getMainLooper()).post(new Runnable() {
						@Override
						public void run() {
							isSendingDigitalReceipt = false;
//							Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
//							getFragmentManager()
//									.beginTransaction()
//									.replace(R.id.main_fragment, new GridViewFragment(),
//											"HOMESCREEN").commit();
						}
					});
				}


			});
		}

		view.setFocusableInTouchMode(true);
		view.requestFocus();
		view.setOnKeyListener( new View.OnKeyListener()
		{
			@Override
			public boolean onKey( View v, int keyCode, KeyEvent event )
			{
				if( keyCode == KeyEvent.KEYCODE_BACK )
				{
					isTrasaction = false;
					isLastTransactionResult=false;
					isLastReconciliationResult=false;
					isSendingDigitalReceipt=false;

					getFragmentManager().beginTransaction()
							.replace(R.id.main_fragment, new GridViewFragment(), "HOMESCREEN").commit();


					MPOSService.getInstance(getActivity()).cancelTransaction();;

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
		getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	MPOSServiceCallback transactionCallback = new MPOSServiceCallback() {
		@Override
		public void onComplete(int status, final String message, final Object result) {

			if(!((MposBaseActivity)getActivity()).isRunning()) {
				((MposBaseActivity)getActivity()).addToPendingStatus(new PendingStatus(this, true, status, message, result));
				return;
			}
			//Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
			MPOSService.getInstance(getActivity()).cancelTransaction();
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					if (isTrasaction) {
						showTransactionResult((String) result);
						isTrasaction = false;
						isLastTransactionResult=false;
						isLastReconciliationResult=false;
						isSendingDigitalReceipt = false;

					}
				}
			});

		}

		@Override
		public void onFailed(int status, final String message) {

			if(!((MposBaseActivity)getActivity()).isRunning()) {
				((MposBaseActivity)getActivity()).addToPendingStatus(new PendingStatus(this, false, status, message, null));
				return;
			}

			isTrasaction = false;
			isLastTransactionResult=false;
			isLastReconciliationResult=false;
			isSendingDigitalReceipt = false;

			new Handler(Looper.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					MPOSService.getInstance(getActivity()).cancelTransaction();
					Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
//					progress.setVisibility(View.GONE);
//					textView.setText("TRANSACTION FAILED, PLEASE TRY AGAIN..");
//					getFragmentManager()
//							.beginTransaction()
//							.replace(R.id.main_fragment, new GridViewFragment(),
//									"HOMESCREEN").commit();
				}
			});

		}


	};

	MPOSServiceCallback reconciliationCallback = new MPOSServiceCallback() {
		@Override
		public void onComplete(int status, String message, final Object result) {

			if(!((MposBaseActivity)getActivity()).isRunning()) {
				((MposBaseActivity)getActivity()).addToPendingStatus(new PendingStatus(this, true, status, message, result));
				return;
			}

			MPOSService.getInstance(getActivity()).cancelTransaction();
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {

					DataBaseHelper obj_DataBaseHelper = new DataBaseHelper(getActivity());
					Bundle bundle = new Bundle();



					String xmlResponse = (String)result;
					boolean res =	obj_DataBaseHelper.InsertData(obj_sharedClass.getCurrentDateTime(getActivity()),xmlResponse,"","","Reconciliation");
					bundle.putString("page",null);
					bundle.putString("Date",obj_sharedClass.getCurrentDateTime(getActivity()));
					ReconciliationResult reconciliationResult = MPOSService.getInstance(getActivity()).parseReconcilationResult(xmlResponse);

					isTrasaction = false;
					isLastTransactionResult=false;
					isLastReconciliationResult=false;
					isSendingDigitalReceipt=false;

					if(reconciliationResult !=null) {
						ReconciliationReportFragment fragment = new ReconciliationReportFragment();
						fragment.setReconciliationResult(reconciliationResult);

						getFragmentManager().beginTransaction()
								.replace(R.id.main_fragment, fragment, "RECONRESULT")
								.commit();
					} else {
						Toast.makeText(getActivity(), "Error parsing reconciliation response", Toast.LENGTH_LONG).show();
					}
				}
			});

		}

		@Override
		public void onFailed(int status, final String message) {
			if(!((MposBaseActivity)getActivity()).isRunning()) {
				((MposBaseActivity)getActivity()).addToPendingStatus(new PendingStatus(this, true, status, message, null));
				return;
			}

			MPOSService.getInstance(getActivity()).cancelTransaction();
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
//					getFragmentManager()
//							.beginTransaction()
//							.replace(R.id.main_fragment, new GridViewFragment(),
//									"HOMESCREEN").commit();
//					isTrasaction = false;
//					isLastTransactionResult=false;
//					isLastReconciliationResult=false;
//					isSendingDigitalReceipt=false;
				}
			});

		}
	};

	@Override
	public void onSaveInstanceState(Bundle outState) {
		//super.onSaveInstanceState(outState);

//		isTrasaction = false;
//		isLastTransactionResult=false;
//		isLastReconciliationResult=false;
//		isSendingDigitalReceipt=false;
	}

	protected void showTransactionResult(String transactionResponse) {
		HashMap<String, String> result = MPOSService.getInstance(getActivity()).parseTransactionResponse(transactionResponse);
		Bundle bundleSale = new Bundle();

		String terminalStatusCode = result.get("TerminalStatusCode");
		if(terminalStatusCode != null) {
			TerminalStatus status = TerminalStatus.getStatus(terminalStatusCode);
			bundleSale.putString("TERMINAL_STATUS_CODE", terminalStatusCode);

			String resultEnglish = result.get("ResultEnglish");
			String transactionTypeEnglish = result.get("TransactionTypeEnglish");
			String amount = result.get("Amount");
			String performanceStartDateTime = result.get("PerformanceStartDateTime");
			String approvalCode = result.get("ApprovalCode");
			String rrn = result.get("RRN");
			String responseCode = result.get("ResponseCode");

			if(resultEnglish!=null)
				bundleSale.putString("TRANSACTION_RESULT", result.get("ResultEnglish"));
			else
				bundleSale.putString("TRANSACTION_RESULT", "");

			if(transactionTypeEnglish!=null)
				bundleSale.putString("TRANSACTION_TYPE", result.get("TransactionTypeEnglish"));
			else
				bundleSale.putString("TRANSACTION_TYPE", "");

			if(amount!=null)
				bundleSale.putString("TRANSACTION_AMOUNT", amount);
			else
				bundleSale.putString("TRANSACTION_AMOUNT", "");

			if(approvalCode!=null)
				bundleSale.putString("TRANSACTION_AUTH_ID", approvalCode);
			else
				bundleSale.putString("TRANSACTION_AUTH_ID", "");

			if(rrn!=null)
				bundleSale.putString("TRANSACTION_INVOICE_NO", rrn);
			else
				bundleSale.putString("TRANSACTION_INVOICE_NO", "");

			if(responseCode!=null)
				bundleSale.putString("TRANSACTION_RESPONSE_CODE", responseCode);
			else
				bundleSale.putString("TRANSACTION_RESPONSE_CODE", "");

			bundleSale.putString("TRANSACTION_RESPONSE_CODE", result.get("ResponseCode"));

			if( performanceStartDateTime != null && performanceStartDateTime.length()>=8) {
				String date = result.get("PerformanceStartDateTime");
				String day = date.substring(0, 2);
				String month = date.substring(2, 4);
				String year = date.substring(4, 8);
				bundleSale.putString("TRANSACTION_DATE", day+"/"+month+"/"+year);
			} else {
				bundleSale.putString("TRANSACTION_DATE", "");
			}


			bundleSale.putString("TRANSACTION_COMPLETE_RESPONSE", transactionResponse);
		}

		bundleSale.putString("tid", tid);
		bundleSale.putString("Amount", Amount);

		TransactionDetailsDisplayFragment fragment = new TransactionDetailsDisplayFragment(	);
		fragment.setArguments(bundleSale);

		getFragmentManager().beginTransaction()
				.replace(R.id.main_fragment, fragment, "TDDAFRAGMENT").commit();
	}



}
