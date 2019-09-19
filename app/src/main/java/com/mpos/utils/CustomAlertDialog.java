package com.mpos.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;

public class CustomAlertDialog {

	public static AlertDialog showConfirmationAlert(final Activity activity,
                                                    CharSequence stringTitle, CharSequence stringMessage) {
		Typeface tfBold = Typeface.createFromAsset(activity.getAssets(),
				"Interstate.ttf");
		AlertDialog.Builder downloadDialog = new AlertDialog.Builder(activity,
				AlertDialog.THEME_HOLO_DARK);
		downloadDialog.setMessage(stringMessage);

		downloadDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		downloadDialog.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						activity.finishAffinity();;
					}
				});

		final TextView myView = new TextView(activity);
		myView.setText(stringTitle);
		myView.setTypeface(tfBold, Typeface.BOLD);
		myView.setGravity(Gravity.CENTER_HORIZONTAL);
		myView.setPadding(0, 10, 0, 10);
		myView.setTextColor(Color.parseColor("#FFFFFF"));
		myView.setTextSize(30);
		downloadDialog.setCustomTitle(myView);

		AlertDialog alert = downloadDialog.create();
		alert.show();
		// alert.getWindow().getAttributes();
		TextView msgTxt = (TextView) alert.findViewById(android.R.id.message);
		msgTxt.setGravity(Gravity.CENTER_HORIZONTAL);
		msgTxt.setPadding(0, 30, 0, 10);
		msgTxt.setTypeface(tfBold, Typeface.BOLD);

		Button cancelBtn = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
		cancelBtn.setTextSize(20);
		cancelBtn.setTypeface(tfBold, Typeface.BOLD);
		Button yesBtn = alert.getButton(DialogInterface.BUTTON_POSITIVE);
		yesBtn.setTextSize(20);
		yesBtn.setTypeface(tfBold, Typeface.BOLD);
		return alert;

	}

}
