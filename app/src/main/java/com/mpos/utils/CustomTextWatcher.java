package com.mpos.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class CustomTextWatcher {
SharedClass obj = new SharedClass();
	public void decimalFormat(final TextView textView) {

		textView.addTextChangedListener(new TextWatcher() {
			String before = "";

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
				before = s.toString();
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!before.equalsIgnoreCase(s.toString())
						&& !TextUtils.isEmpty(s.toString().trim())) {
					before = s.toString();
					String tvStr = s.toString();
					if (tvStr.contains("-") || tvStr.equals(".")) {
						textView.setText("");
					}

					if (!tvStr.equals("") && !tvStr.equals(".")) {
						String cleanString = getFormatedNumber(tvStr, 2);
						String str = obj.getEnglishNumbers(cleanString);
						str = str.replace("٫", ".");
						textView.setText(str);
					}
				}
			}
		});
	}

	public static String getFormatedNumber(String text, int desimalPoint) {

		String cleanString = text.replaceAll("[,.|\\s]", "");
		if (TextUtils.isEmpty(cleanString)) {
			return "0.00";
		}
		NumberFormat formatter = new DecimalFormat("#,##0.00");
		double number = Double.parseDouble(cleanString)
				/ (long) Math.pow(10.0, desimalPoint);
		return formatter.format(number).toString();
	}
}
