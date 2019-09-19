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


public class PANEntryFragment extends Fragment  {

    Button processButton, cancel;
    EditText maskedPan, email, cashierPin;
    String transactionAmount;
    TransactionType transactionType = TransactionType.REFUND;

    Boolean isTrasaction;
    String rrn = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pan_entry, container, false);

        Typeface tfBold = Typeface.createFromAsset(getActivity().getAssets(),
                "Interstate.ttf");
        maskedPan = (EditText) view.findViewById(R.id.pan_edittext);
        maskedPan.setTypeface(tfBold, Typeface.BOLD);

        int maxLength = 20;
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        maskedPan.setFilters(fArray);

        Bundle bundle = this.getArguments();

        isTrasaction = bundle.getBoolean("TSF");
        transactionAmount = bundle.getString("Amount");
        transactionType = TransactionType.getTransactionType(bundle.getInt("TransactionType", 1));
        rrn = bundle.getString("RRN", null);

        processButton = (Button) view.findViewById(R.id.process_btn);
        processButton.setTypeface(tfBold, Typeface.BOLD);
        cancel = (Button) view.findViewById(R.id.cancle_btn);
        cancel.setTypeface(tfBold, Typeface.BOLD);

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
                String pan  = maskedPan.getText().toString();
                if(pan.length()!= 0 && pan.length()<12) {
                    Toast.makeText(getActivity(), "Masked PAN should be minimum 12 digits", Toast.LENGTH_SHORT).show();
                    return;
                }

                ProgressFragment connectionFragment = new
                        ProgressFragment(); Bundle bundle = new Bundle();
                bundle.putBoolean("TSF", true);
                bundle.putString("RRN", rrn);
                bundle.putString("Amount", transactionAmount);
                bundle.putString("MaskedPAN", pan);
                bundle.putInt("TransactionType", TransactionType.REFUND.getValue());
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
