package com.mpos.fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.res.Resources;
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
import android.widget.TextView;
import android.widget.Toast;

import com.mpos.activity.MposBaseActivity;
import com.mpos.activity.R;
import com.mpos.mpossdk.api.TransactionType;
import com.mpos.utils.CustomTextWatcher;
import com.mpos.utils.SharedClass;

import java.text.ParseException;


public class AmountEntryFragment extends Fragment {
    public String amount;
    public String cashbackAmount = "";
    TextView enterAmount, transactionTitle, enterAmountTxt;
    Button lessThanSale;
    Typeface tfBold, tfNew;
    String enteredNumber = "";
    TransactionType transactionType = TransactionType.SALE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // registerBroadcast();
        View view = inflater.inflate(R.layout.transactions_sale, container, false);

        tfBold = Typeface.createFromAsset(getActivity().getAssets(), "Interstate.ttf");
        tfNew = Typeface.createFromAsset(getActivity().getAssets(), "InterstateLightCondensed.ttf");

        enterAmountTxt = (TextView) view.findViewById(R.id.enteramounttxt);
        enterAmountTxt.setTypeface(tfBold);

        transactionTitle = (TextView) view.findViewById(R.id.transaction_title);
        transactionTitle.setTypeface(tfBold, Typeface.BOLD);


        Bundle bundle = this.getArguments();
        transactionType = TransactionType.getTransactionType(bundle.getInt("TransactionType", 1));

        transactionTitle.setText(transactionType.getName());
        Button submitSaleValue = (Button) view.findViewById(R.id.submitsale);
        submitSaleValue.setTypeface(tfBold, Typeface.BOLD);
        lessThanSale = (Button) view.findViewById(R.id.lessthan);
        lessThanSale.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String textInBox = enterAmount.getText().toString();
                String newText = "";
                try {
                    if (textInBox.length() > 0) {
                        // Remove last character//
                        newText = textInBox.substring(0, textInBox.length() - 1);

                        // Update edit text
                        enterAmount.setText(newText);
                        enteredNumber = newText;
                    }
                } catch (Exception e) {
                    enteredNumber = newText;
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                }

            }
        });

        final Button one = (Button) view.findViewById(R.id.numberone);
        one.setTypeface(tfBold, Typeface.BOLD);
        final Button two = (Button) view.findViewById(R.id.numbertwo);
        two.setTypeface(tfBold, Typeface.BOLD);
        final Button three = (Button) view.findViewById(R.id.numberthree);
        three.setTypeface(tfBold, Typeface.BOLD);
        final Button four = (Button) view.findViewById(R.id.numberfour);
        four.setTypeface(tfBold, Typeface.BOLD);
        final Button five = (Button) view.findViewById(R.id.numberfive);
        five.setTypeface(tfBold, Typeface.BOLD);
        final Button six = (Button) view.findViewById(R.id.numbersix);
        six.setTypeface(tfBold, Typeface.BOLD);
        final Button seven = (Button) view.findViewById(R.id.numberseven);
        seven.setTypeface(tfBold, Typeface.BOLD);
        final Button eight = (Button) view.findViewById(R.id.numbereight);
        eight.setTypeface(tfBold, Typeface.BOLD);
        final Button nine = (Button) view.findViewById(R.id.numbernine);
        nine.setTypeface(tfBold, Typeface.BOLD);
        final Button zero = (Button) view.findViewById(R.id.numberzero);
        zero.setTypeface(tfBold, Typeface.BOLD);
        final Button clear = (Button) view.findViewById(R.id.clr);
        clear.setTypeface(tfBold, Typeface.BOLD);
        enterAmount = (TextView) view.findViewById(R.id.enteramountedittxt);
        enterAmount.setTypeface(tfBold, Typeface.BOLD);
        final Resources res = getResources();
        one.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                try {

                    enterAmount.setText(addCurrency(res.getString(R.string.num1)));
                } catch (Exception e) {
                    //Toast.makeText(getActivity(), "Invalid Amount", Toast.LENGTH_SHORT).show();
                }
            }
        });


        two.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                try {
                    enterAmount.setText(addCurrency("2"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        three.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                try {
                    enterAmount.setText(addCurrency("3"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

        four.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                try {
                    enterAmount.setText(addCurrency("4"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        five.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                try {
                    enterAmount.setText(addCurrency("5"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        six.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                try {
                    enterAmount.setText(addCurrency("6"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        seven.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                try {
                    enterAmount.setText(addCurrency("7"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                try {
                    enterAmount.setText(addCurrency("8"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        nine.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                try {
                    enterAmount.setText(addCurrency("9"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        zero.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                try {
                    enterAmount.setText(addCurrency("0"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                enterAmount.setText("");
                enteredNumber = "";

            }
        });

        submitSaleValue.setOnClickListener(new OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                try {
                    SharedClass obj_shar = new SharedClass();

                    String enteredAmount = enterAmount.getText().toString();
                    enteredAmount = obj_shar.getEnglishNumbers(enteredAmount);

                    //enteredAmount = numberFormat.format( Float.parseFloat(enteredAmount));
                    enteredAmount = enteredAmount.replace(".", "");
                    enteredAmount = enteredAmount.replace(",", "");
                    enteredAmount = enteredAmount.replace("٫", "");
                    try {
                        if (enteredAmount.length() == 0 || Float.parseFloat(enteredAmount) <= 0) {
                            Toast.makeText(getActivity(), "Invalid Amount", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (Exception r) {

                    }
                    if (transactionType == TransactionType.CASHBACK && enterAmountTxt.getText().toString().equals("Enter Amount")) {
                        enterAmountTxt.setText("Enter Cashback");
                        enterAmount.setText("");
                        enteredNumber = "";
                        amount = enteredAmount;
                        return;
                    } else if (transactionType == TransactionType.CASHBACK && enterAmountTxt.getText().toString().equals("Enter Cashback")) {
                        cashbackAmount = enteredAmount;

                        if (Float.parseFloat(amount) < Float.parseFloat(cashbackAmount)) {
                            Toast.makeText(getActivity(), "Cash back amount should not be greater then Sale amount", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        amount = enteredAmount;
                    }

                    if (transactionType == TransactionType.SALE || transactionType == TransactionType.CASHBACK || transactionType == TransactionType.PREAUTH || transactionType == TransactionType.CASH_ADVANCE) {
                        ProgressFragment connectionFragment = new ProgressFragment();
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("TSF", true);
                        bundle.putString("CashBack", cashbackAmount);
                        bundle.putString("Amount", amount);
                        bundle.putInt("TransactionType", transactionType.getValue());

                        connectionFragment.setArguments(bundle);

                        getFragmentManager().beginTransaction().replace(R.id.
                                main_fragment, connectionFragment, "CONFRAGMENT").commit();
                    } else if (transactionType == TransactionType.REFUND) {
                        RRNEntryFragment fragment = new RRNEntryFragment();
                        amount = enterAmount.getText().toString();
                        Bundle bundle = new Bundle();
                        bundle.putString("Amount", amount);
                        fragment.setArguments(bundle);

                        getFragmentManager().beginTransaction().replace(R.id.
                                main_fragment, fragment, "RRNENTRYFRMT").commit();
                    } else if (transactionType == TransactionType.ADVICE) {
                        AuthCodeEntryFragment fragment = new AuthCodeEntryFragment();
                        amount = enterAmount.getText().toString();
                        Bundle bundle = new Bundle();
                        bundle.putString("Amount", amount);
                        fragment.setArguments(bundle);

                        getFragmentManager().beginTransaction().replace(R.id.
                                main_fragment, fragment, "AUTHENTRYFRMT").commit();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                }


            }
        });

        CustomTextWatcher textWatcher = new CustomTextWatcher();
        textWatcher.decimalFormat(enterAmount);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    getFragmentManager().beginTransaction().replace(R.id.main_fragment, new GridViewFragment(), "HOMESCREEN").commit();
                    return true;
                }
                return false;
            }
        });


        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        ((MposBaseActivity) getActivity()).hideMenu();
    }

    /**
     * This method enters the transaction amount values from the decimal point
     * onwards
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private String addCurrency(String digits) throws ParseException {
        String string = enteredNumber;
        enteredNumber = enteredNumber + digits;

        return enteredNumber;
    }


}
