package com.mpos.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class CustomProgressDialog extends ProgressDialog {

	Typeface tfBold;

	public CustomProgressDialog(Context context) {
		super(context);
		tfBold = Typeface
				.createFromAsset(context.getAssets(), "Interstate.ttf");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView progressDialogMsgTxt = (TextView) findViewById(android.R.id.message);
		progressDialogMsgTxt.setTextSize(20);
		if (progressDialogMsgTxt != null) {
			progressDialogMsgTxt.setTypeface(tfBold, Typeface.BOLD);
		}
	}
}
