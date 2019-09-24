package com.mpos.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mpos.Model.DeviceInfo;
import com.mpos.Model.Users;
import com.mpos.activity.HomeActivity;
import com.mpos.activity.MposBaseActivity;
import com.mpos.activity.R;
import com.mpos.mpossdk.api.MPOSService;
import com.mpos.utils.CallWebService;
import com.mpos.utils.SharedClass;

public class MerchantAdminLoginFragment extends Fragment {

    TextView enterPinText, confirmPinText, txt_Register;
    EditText enter_UserName_edt, confirmPinEdt, enter_Password_edt, enter_CR_ID, enter_Mobile, enter_TID, editPassword, editConfirmPasword, editConfirmCode;
    Button saveBtn, btn_Register, btn_ConfirmCode;
    ImageButton btn_ConnectBluetooth;
    Fragment fragment;
    RadioGroup group;
    Menu optionMenu;
    public String TerminalId="";
    int idx = 0;
    boolean is_ID = false;
    // static CallWebService obj_CallWebService = new CallWebService();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.merchant_admin_login, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        Typeface tfBold = Typeface.createFromAsset(getActivity().getAssets(), "Interstate.ttf");

        txt_Register = (TextView) view.findViewById(R.id.txt_Register);
        enter_UserName_edt = (EditText) view.findViewById(R.id.enter_UserName_edt);
        enter_Password_edt = (EditText) view.findViewById(R.id.enter_Password_edt);
        saveBtn = (Button) view.findViewById(R.id.save_btn);


        saveBtn.setTypeface(tfBold, Typeface.BOLD);
        // On Click Register Text
        txt_Register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final View registerView = getActivity().getLayoutInflater().inflate(R.layout.register_dialog, null);
                btn_Register = (Button) registerView.findViewById(R.id.btn_Register);
                enter_CR_ID = (EditText) registerView.findViewById(R.id.enter_CR_ID);
                enter_Mobile = (EditText) registerView.findViewById(R.id.enter_Mobile);
                enter_TID = (EditText) registerView.findViewById(R.id.enter_TID);
                btn_ConnectBluetooth = (ImageButton) registerView.findViewById(R.id.btn_ConnectBluetooth);


                final Dialog dialog = new Dialog(getActivity());
                dialog.setTitle("Register Dialog");
                dialog.setContentView(registerView);
                dialog.show();

                btn_ConnectBluetooth.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MPOSService.getInstance(getActivity()).showDeviceListDialog();
                        // UpdateTerminalId(getActivity());
                    }
                });

                //on click Register button in Register dialog
                btn_Register.setOnClickListener(new OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View view) {
                        final View confirm_user_view = getActivity().getLayoutInflater().inflate(R.layout.confirm_user_dialog, null);
                        group = (RadioGroup) registerView.findViewById(R.id.radio_type);
                        //get index of selected radio button
                        int radioButtonID = group.getCheckedRadioButtonId();
                        View radioButton = group.findViewById(radioButtonID);
                        idx = group.indexOfChild(radioButton); // this is the index => 0 is CR and 1 is ID
                        boolean checkData = true;

                        // trying to update terminal id if bluetooth is connected
                        SharedClass obj_shar = new SharedClass();
                        DeviceInfo deviceInfo = new DeviceInfo();
                        MPOSService service = MPOSService.getInstance(getActivity());
                        String deviceName = service.getConnectedDeviceName();

                        if (deviceName == null || deviceName.length() == 0) {
                            enter_CR_ID.setError(null);
                            enter_Mobile.setError(null);
                            enter_TID.setError(null);
                            Toast.makeText(getActivity(), "Please connect to POS device .", Toast.LENGTH_LONG).show();
                            checkData = false;
                        } else {


                            if (enter_CR_ID.getText().toString().length() != 10) {
                                enter_CR_ID.setError("CR \\ ID should be 10 numbers!");
                                checkData = false;
                            }

                            if (enter_Mobile.getText().toString().length() != 9) {
                                enter_Mobile.setError("Mobile number should be 9 numbers!");
                                checkData = false;
                            }

                            if (enter_TID.getText().toString().length() != 8) {
                                enter_TID.setError("TID should be 8 numbers!");
                                checkData = false;
                            }
                        }
                        // if check data is true that mean we must validate retailer data if it correct
                        if (checkData) {
                            Users obj_User = new Users();

                            if (idx == 0) is_ID = false;
                            else is_ID = true;
                            try {
                                final SharedClass obj_shared = new SharedClass();
                                Thread secthread = new Thread(new Runnable() {
                                    public void run() {
                                        try {
                                            Looper.prepare();
                                            CallWebService.ValidateRetailerData(enter_CR_ID.getText().toString(), enter_TID.getText().toString(), enter_Mobile.getText().toString(), is_ID, false, "", "", "", getActivity());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                secthread.start();
                                try {
                                    secthread.join();
                                    if (obj_User.NoConnectionError.equals("true")) {
                                        if (!obj_User.ValidateRetailerData.equals("false")) {
                                            dialog.hide();
                                            final Dialog dialog = new Dialog(getActivity());
                                            dialog.setTitle("create user Dialog");
                                            dialog.setContentView(confirm_user_view);
                                            dialog.show();

                                            //declare data for confirm code view
                                            editConfirmCode = (EditText) confirm_user_view.findViewById(R.id.enter_ConfirmCode);
                                            btn_ConfirmCode = (Button) confirm_user_view.findViewById(R.id.btn_Confirm);

                                            btn_ConfirmCode.setOnClickListener(new OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    final View create_user_view = getActivity().getLayoutInflater().inflate(R.layout.create_user, null);
                                                    boolean checkCode = true;
                                                    if (editConfirmCode.getText().toString().length() != 4) {
                                                        editConfirmCode.setError("Enter valid code");
                                                        checkCode = false;
                                                    }
                                                    if (checkCode) {
                                                        Users obj_User = new Users();
                                                        Thread thirdThread = new Thread(new Runnable() {
                                                            public void run() {
                                                                try {
                                                                    Looper.prepare();
                                                                    CallWebService.ValidateRetailerData(enter_CR_ID.getText().toString(), enter_TID.getText().toString(), enter_Mobile.getText().toString(), is_ID, false, "", "", editConfirmCode.getText().toString(), getActivity());
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });

                                                        thirdThread.start();
                                                        try {
                                                            thirdThread.join();
                                                            if (obj_User.NoConnectionError.equals("true")) {
                                                                if (!obj_User.ValidateRetailerData.equals("false")) {
                                                                    dialog.hide();
                                                                    final Dialog dialog = new Dialog(getActivity());
                                                                    dialog.setTitle("create user Dialog");
                                                                    dialog.setContentView(create_user_view);
                                                                    dialog.show();
                                                                    //declare data for create user view
                                                                    final EditText editUserName = (EditText) create_user_view.findViewById(R.id.enter_User_Name);
                                                                    editPassword = (EditText) create_user_view.findViewById(R.id.enter_User_Password);
                                                                    editConfirmPasword = (EditText) create_user_view.findViewById(R.id.confirm_Password);
                                                                    Button btn_CreateUser = (Button) create_user_view.findViewById(R.id.btn_Create_User);

                                                                    //on click create user button
                                                                    btn_CreateUser.setOnClickListener(new OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View view) {
                                                                            boolean createUser = true;
                                                                            if (editUserName.getText().toString().length() == 0) {
                                                                                editUserName.setError("Enter User Name !");
                                                                                createUser = false;
                                                                            }
                                                                            if (editPassword.getText().toString().length() == 0 || editPassword.getText().toString().length() < 6) {
                                                                                editPassword.setError("Enter Password !");
                                                                                createUser = false;
                                                                            }
                                                                            if (editConfirmPasword.getText().toString().length() == 0 || editConfirmPasword.getText().toString().length() < 6) {
                                                                                editConfirmPasword.setError("Confirm Password !");
                                                                                createUser = false;
                                                                            }

                                                                            if (!editPassword.getText().toString().equals(editConfirmPasword.getText().toString()) && editConfirmPasword.getText().toString().length() != 0) {
                                                                                editConfirmPasword.setError("Password not match !");
                                                                                createUser = false;
                                                                            }
                                                                            if (createUser) {
                                                                                Users obj_User = new Users();
                                                                                try {
                                                                                    Thread secthread = new Thread(new Runnable() {
                                                                                        public void run() {
                                                                                            try {
                                                                                                Looper.prepare();
                                                                                                CallWebService.ValidateRetailerData(enter_CR_ID.getText().toString(), enter_TID.getText().toString(), enter_Mobile.getText().toString(), is_ID, true, editPassword.getText().toString(), editUserName.getText().toString(), "", getActivity());
                                                                                            } catch (Exception e) {
                                                                                                e.printStackTrace();
                                                                                            }
                                                                                        }
                                                                                    });

                                                                                    secthread.start();
                                                                                    if (obj_User.NoConnectionError.equals("true")) {
                                                                                        if (!obj_User.ValidateRetailerData.equals("false")) {
                                                                                            dialog.hide();
                                                                                            Toast.makeText(getActivity(), "User created successfully ", Toast.LENGTH_LONG).show();
                                                                                        } else
                                                                                            Toast.makeText(getActivity(), "Con't create user now please try again", Toast.LENGTH_LONG).show();

                                                                                    } else
                                                                                        Toast.makeText(getActivity(), "Please check your internet connection .", Toast.LENGTH_LONG).show();

                                                                                } catch (Exception ex) {

                                                                                }
                                                                            }

                                                                        }
                                                                    });
                                                                } else
                                                                    Toast.makeText(getActivity(), "Enter valid code ! ", Toast.LENGTH_LONG).show();
                                                            }

                                                        } catch (Exception ex) {

                                                        }

                                                    }
                                                }
                                            });
                                        } else
                                            Toast.makeText(getActivity(), "Please enter valid data", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Please check your internet connection .", Toast.LENGTH_LONG).show();
                                    }

                                } catch (Exception ex) {
                                    Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception ee) {
                                Toast.makeText(getActivity(), ee.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });

        //Login btn
        saveBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


               



                boolean validData = true;
                if(true)
                {
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                }

                if (enter_Password_edt.getText().toString().length() < 6) {
                    enter_Password_edt.setError("Password should be greater than 6 characters!");
                    validData = false;
                }
                if (enter_UserName_edt.getText().toString().length() == 0) {
                    enter_UserName_edt.setError("Please enter user name !");
                    validData = false;
                }
                if (validData) {
                    Users obj_User = new Users();
                    Thread thread = new Thread(new Runnable() {
                        public void run() {
                            try {
                                CallWebService.Login(enter_UserName_edt.getText().toString(), enter_Password_edt.getText().toString(), getActivity());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    thread.start();
                    try {
                        thread.join();
                        if (obj_User.NoConnectionError.equals("true")) {
                            if (!obj_User.webServiceResult.equals("false")) {
                                Intent intent = new Intent(getActivity(), HomeActivity.class);

                                startActivity(intent);
                                getActivity().finish();
                            } else
                                Toast.makeText(getActivity(), "Incorrect username or password !", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "Please check your internet connection .", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception ex) {
                        Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
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


}
