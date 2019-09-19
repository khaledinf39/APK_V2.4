package com.mpos.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mpos.activity.MposBaseActivity;
import com.mpos.activity.R;

public class EnterPinFragment extends Fragment {

	public static final String TAG = "EnterPinFragment";
	View activationCodeDigit;
	ImageButton activationCodeDeleteBtn;
	Button clearBtn, enterBtn;
	LinearLayout codeCircleViewContainer;
	LinearLayout.LayoutParams params;
	Boolean reenterPassCode = false;
	Boolean deleteChar = false;
	Button activationCode0, activationCode1, activationCode2, activationCode3,
			activationCode4, activationCode5, activationCode6, activationCode7,

			activationCode8, activationCode9;

	// Update the UI for Passcode
	TextView textActivationSubtitle;

	// Flag to verify Passcode screen
	boolean isPasscodeScreen;

	// Activation retry
	boolean activationRetryNeeded;
	private int MAX_CODE_LENGTH = 4;

	private int INITIAL_VIEW_COUNT = 4;

	private StringBuilder activationCode;
	private String enteredActivationCode;

	// Pass code retry counter
	private int passcodeCounter = 0;

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.enter_pin, container, false);

		Typeface tfBold = Typeface.createFromAsset(getActivity().getAssets(),
				"Interstate.ttf");

		codeCircleViewContainer = (LinearLayout) view
				.findViewById(R.id.password_circle);

		// Layout property for code indicating circle
		params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.height = 30;
		params.width = 30;

		params.setMargins(
				(int) getActivity().getResources().getDimension(
						R.dimen.passcode_margin_left), 0, 0, 0);

		enterBtn = (Button) view.findViewById(R.id.enterBtn);

		for (int i = 0; i < INITIAL_VIEW_COUNT; i++) {
			activationCodeDigit = new View(getActivity());
			activationCodeDigit.setTag(i + 1);
			activationCodeDigit.setBackground(getActivity().getResources()
					.getDrawable(R.drawable.passcode_display_btn_circle_dark));
			codeCircleViewContainer.addView(activationCodeDigit, params);
		}

		activationCode = new StringBuilder();
		activationCodeDeleteBtn = (ImageButton) view
				.findViewById(R.id.passcodeDeleteBtn);
		activationCodeDeleteBtn.setVisibility(View.GONE);
		activationCodeDeleteBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteChar = true;
				if (activationCode.length() > 0) {

					// delete the char
					activationCode.deleteCharAt(activationCode.length() - 1);
					Log.d(TAG,
							"code length at delete char"
									+ activationCode.length());

					// if the length is 0 hide the delete button
					if (activationCode.length() <= 0) {
						activationCodeDeleteBtn.setVisibility(View.GONE);
					}
					setCodeDigitState(codeCircleViewContainer);
				}

			}
		});

		clearBtn = (Button) view.findViewById(R.id.clearBtn);
		clearBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (activationCode.length() > 0) {
					// delete the char
					resetViews();
				}

			}
		});

		enterBtn = (Button) view.findViewById(R.id.enterBtn);
		enterBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (activationCode.length() == MAX_CODE_LENGTH) {
					doPassCodeVerification();
				}
			}
		});

		// Initialize the digit buttons
		activationCode0 = (Button) view.findViewById(R.id.passcode0);
		activationCode0.setOnClickListener(digitPressed);

		activationCode1 = (Button) view.findViewById(R.id.passcode1);
		activationCode1.setOnClickListener(digitPressed);

		activationCode2 = (Button) view.findViewById(R.id.passcode2);
		activationCode2.setOnClickListener(digitPressed);

		activationCode3 = (Button) view.findViewById(R.id.passcode3);
		activationCode3.setOnClickListener(digitPressed);

		activationCode4 = (Button) view.findViewById(R.id.passcode4);
		activationCode4.setOnClickListener(digitPressed);

		activationCode5 = (Button) view.findViewById(R.id.passcode5);
		activationCode5.setOnClickListener(digitPressed);

		activationCode6 = (Button) view.findViewById(R.id.passcode6);
		activationCode6.setOnClickListener(digitPressed);

		activationCode7 = (Button) view.findViewById(R.id.passcode7);
		activationCode7.setOnClickListener(digitPressed);

		activationCode8 = (Button) view.findViewById(R.id.passcode8);
		activationCode8.setOnClickListener(digitPressed);

		activationCode9 = (Button) view.findViewById(R.id.passcode9);
		activationCode9.setOnClickListener(digitPressed);

		return view;
	}

	private void hideShowSoftKeyPad(EditText edit, boolean show) {
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (show) {
			imm.showSoftInputFromInputMethod(edit.getWindowToken(), 0);
		} else {
			imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
		}

	}

	View.OnClickListener digitPressed = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.d(TAG, "digitPressed");
			activationCodeDeleteBtn.setVisibility(View.VISIBLE);
			if (activationCode.length() < MAX_CODE_LENGTH) {
				String textDigit = ((Button) v).getText().toString();
				Log.d(TAG, "digitPressed:" + textDigit);
				activationCode.append(textDigit);
				setCodeDigitState(codeCircleViewContainer);
			}
			processPasscode();
		}
	};

	private void setCodeDigitState(ViewGroup root) {

		for (int i = 0; i < root.getChildCount(); i++) {
			View view = root.getChildAt(i);
			if (view instanceof View)
				System.out.println("true" + view.getTag());
			int tagDigit = (Integer) view.getTag();
			boolean pressed = false;
			if (activationCode.length() >= tagDigit)
				pressed = true;
			view.setPressed(pressed);
		}
	}

	private void processPasscode() {

		if (activationCode.length() > 0) {
			activationCodeDeleteBtn.setVisibility(View.VISIBLE);
		}
	}

	void resetViews() {
		activationCodeDeleteBtn.setVisibility(View.GONE);
		activationCode.setLength(0);
		setCodeDigitState(codeCircleViewContainer);
	}

	void doPassCodeVerification() {



		System.out.println("passcode" + enteredActivationCode);
	}

	private void animate() {
		Animation shake = new TranslateAnimation(0, 10, 0, 0);
		shake.setInterpolator(new CycleInterpolator(3));
		shake.setDuration(400);
		codeCircleViewContainer.setAnimation(shake);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(false);
		((MposBaseActivity)getActivity()).hideMenu();
	}
}
