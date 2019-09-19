package com.mpos.fragment;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.mpos.activity.MposBaseActivity;
import com.mpos.activity.R;
import com.mpos.mpossdk.api.ConnectionType;
import com.mpos.mpossdk.connection.ConnectionSettings;


public class ConnectionSettingsFragment extends Fragment  {

    Button saveButton, cancel;
    EditText edtIpAddress, edtPortNumber;
    String transactionAmount;
    Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.connection_settings, container, false);

        Typeface tfBold = Typeface.createFromAsset(getActivity().getAssets(),
                "Interstate.ttf");
        edtIpAddress = (EditText) view.findViewById(R.id.ip_address_edt);
        edtIpAddress.setTypeface(tfBold, Typeface.BOLD);

        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend);
                    if (!resultingTxt.matches ("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (int i=0; i<splits.length; i++) {
                            if (Integer.valueOf(splits[i]) > 255) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }
        };
        edtIpAddress.setFilters(filters);

        edtPortNumber = (EditText) view.findViewById(R.id.port_number_edt);
        edtPortNumber.setTypeface(tfBold, Typeface.BOLD);

        spinner = (Spinner)view.findViewById(R.id.connection_mode);

        int maxLength = 15;
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        edtIpAddress.setFilters(fArray);

        saveButton = (Button) view.findViewById(R.id.save_btn);
        saveButton.setTypeface(tfBold, Typeface.BOLD);
        cancel = (Button) view.findViewById(R.id.cancle_btn);
        cancel.setTypeface(tfBold, Typeface.BOLD);

        ConnectionSettings.loadConnectionSettings(getActivity());

        spinner.setSelection(ConnectionSettings.getConnectionType().getValue());
        edtIpAddress.setText(ConnectionSettings.getIpAddress());
        edtPortNumber.setText(""+ConnectionSettings.getPortNumber());

        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment, new GridViewFragment(),
                                "HOMESCREEN").commit();
            }
        });

        saveButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ConnectionType connectionType = ConnectionType.getConnectionType(spinner.getSelectedItemPosition());
                String ipAddress = edtIpAddress.getText().toString();
                String portNumber = edtPortNumber.getText().toString();

                ConnectionSettings.storeConnectionSettings(getActivity(), connectionType, ipAddress, portNumber);

                getFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment, new GridViewFragment(), "HOMESCREEN")
                        .commit();
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
