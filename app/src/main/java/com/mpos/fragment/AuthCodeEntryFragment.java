package com.mpos.fragment;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mpos.activity.MposBaseActivity;
import com.mpos.activity.R;
import com.mpos.mpossdk.api.TransactionType;


public class AuthCodeEntryFragment extends Fragment  {

    Button processButton, cancel;
    EditText rrn, email, cashierPin;
    String transactionAmount;
    TransactionType transactionType = TransactionType.REFUND;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.authcode_entry, container, false);

        Typeface tfBold = Typeface.createFromAsset(getActivity().getAssets(),
                "Interstate.ttf");
        rrn = (EditText) view.findViewById(R.id.rrn_edittext);
        rrn.setTypeface(tfBold, Typeface.BOLD);

        int maxLength = 6;
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        rrn.setFilters(fArray);

        processButton = (Button) view.findViewById(R.id.process_btn);
        processButton.setTypeface(tfBold, Typeface.BOLD);
        cancel = (Button) view.findViewById(R.id.cancle_btn);
        cancel.setTypeface(tfBold, Typeface.BOLD);

        Bundle bundle = this.getArguments();
        transactionAmount = bundle.getString("Amount");
        transactionType = TransactionType.getTransactionType(bundle.getInt("TransactionType", TransactionType.ADVICE.getValue()));

        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment, new GridViewFragment(),
                                "HOMESCREEN").commit();
            }
        });

        processButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                String rrnNumber  = rrn.getText().toString();
                if(rrnNumber.length()!=6) {
                    Toast.makeText(getActivity(), "AuthCode should be 6 digits", Toast.LENGTH_SHORT).show();
                    return;
                }

                ProgressFragment connectionFragment = new
                        ProgressFragment(); Bundle bundle = new Bundle();
                bundle.putBoolean("TSF", true);
                bundle.putString("AuthCode", rrn.getText().toString());
                bundle.putString("Amount", transactionAmount);
                bundle.putInt("TransactionType", transactionType.getValue());
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
