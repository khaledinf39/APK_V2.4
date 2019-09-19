package com.mpos.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.mpos.Model.Transaction_Model;
import com.mpos.Model.Transaction_Result;
import com.mpos.Model.Users;
import com.mpos.activity.R;
import com.mpos.adapter.transactionAdapter;
import com.mpos.utils.CallWebService;
import com.mpos.utils.DataBaseHelper;
import com.mpos.utils.SharedClass;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TransactionHistoryFragment extends Fragment {

    //region declare global value
    GridView gridView;
    ArrayList<Transaction_Model> lst_tra_model = new ArrayList<>();
    String Amount, RRN, Date, XML = "";
    int Id;
    TextView hed_Amount;
    TextView hed_RRN;
    TextView hed_Date;
    TextView hed_XML;
    transactionAdapter adapter;
    Cursor res;
    DataBaseHelper obj_db;
    int myLastVisiblePos;
    int defaultPageSize = 14;
    int itemCount = 0;
    ProgressDialog progressDialog;
    Handler handler;
    private int preLast;
    boolean updateXML = false;
    View receipt_dialog;
    // static CallWebService obj_CallWebService = new CallWebService();

    //endregion  declare global value
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.transaction_listview, container, false);


        gridView = (GridView) view.findViewById(R.id.gridtransaction);
        myLastVisiblePos = gridView.getFirstVisiblePosition();
        obj_db = new DataBaseHelper(getActivity());


        res = obj_db.getAllData("Transaction_Table", Integer.toString(1), Integer.toString(defaultPageSize));
        if (res.getCount() == 0) {
            // sync server first time
            try {
                Sync();
                res = obj_db.getAllData("Transaction_Table", Integer.toString(1), Integer.toString(defaultPageSize));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        hed_Amount = (TextView) view.findViewById(R.id.hed_t_Amount);
        hed_Date = (TextView) view.findViewById(R.id.hed_t_Date);
        hed_RRN = (TextView) view.findViewById(R.id.hed_t_RRN);


        hed_Amount.setText("Amount");
        hed_Date.setText("Date");
        hed_RRN.setText("RRN");


        if (res != null) {
            lst_tra_model = new ArrayList<Transaction_Model>();
            while (res.moveToNext()) {
                Id = Integer.parseInt(res.getString(0));
                Amount = res.getString(1);
                Date = res.getString(2);
                RRN = res.getString(3);
                Transaction_Model t_model = new Transaction_Model(Id, Amount, Date, RRN, "");
                lst_tra_model.add(t_model);
            }
            adapter = new transactionAdapter(getActivity());
        }


        adapter.addItem(lst_tra_model);


        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Thread thread;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {

                    SharedClass sharedClass = new SharedClass();
                    receipt_dialog = getActivity().getLayoutInflater().inflate(R.layout.receipt_dialog, null);
                    Cursor pokedrow;


                    pokedrow = obj_db.pokedRow("Transaction_Table", i);
                    if (pokedrow != null) {
                        while (pokedrow.moveToNext()) {
                            RRN = pokedrow.getString(2);
                            XML = pokedrow.getString(4);
                        }
                    } else {
                    }
                    if (XML.equals("")) {
                        if (sharedClass.checkInternetConnection(getActivity())) {
                            if (true) {

                                final Users obj_User = new Users();
                                SharedClass obj_shar = new SharedClass();
                                handler = new Handler();
                                //  handler = new Handler();


                                if (obj_shar.checkInternetConnection(getActivity())) {
                                    progressDialog = new ProgressDialog(getActivity());
                                    progressDialog.setTitle("Get Data");
                                    progressDialog.setMessage("Please wait...");
                                    progressDialog.show();
                                    Thread thread = new Thread() {
                                        Transaction_Result t_result = new Transaction_Result();
                                        SharedClass sharedClass = new SharedClass();

                                        public void run() {
                                            try {
                                                Looper.prepare();
                                                XML = CallWebService.getTransactionXML(getActivity(), RRN);
                                            } catch (Exception e) {
                                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                                                e.printStackTrace();
                                            }
                                            updateXML = true;
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {

                                                    try {
                                                        if (!obj_User.ConnectedToTheDevice.equals("false")) {
                                                            if (obj_User.NoConnectionError.equals("true")) {
                                                                if (!obj_User.webServiceResult.equals("false")) {

                                                                    if (!XML.equals("")) {
                                                                        if (updateXML) {
                                                                            obj_db.updateTransactionXML(XML, RRN);
                                                                        }
                                                                        t_result = sharedClass.showTransactionResult(XML, getActivity());

                                                                        if (t_result != null) {
                                                                            ViewReceipt(t_result);
                                                                        } else {
                                                                            Toast.makeText(getActivity(), "Receipt is not available .", Toast.LENGTH_LONG).show();
                                                                        }
                                                                    } else
                                                                        Toast.makeText(getActivity(), "Receipt is not available .", Toast.LENGTH_LONG).show();
                                                                    progressDialog.dismiss();

                                                                } else
                                                                    Toast.makeText(getActivity(), "Incorrect username or password !", Toast.LENGTH_LONG).show();
                                                            } else {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(getActivity(), "Please check your internet connection .", Toast.LENGTH_LONG).show();
                                                            }
                                                        } else {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(getActivity(), "Please connect to the POS device .", Toast.LENGTH_LONG).show();
                                                        }
                                                    } catch (Exception ex) {
                                                        Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                    progressDialog.dismiss();
                                                }
                                            });

                                        }
                                    };


                                    thread.start();
                                } else {
                                    progressDialog.dismiss();
                                }

                            }

                        } else
                            Toast.makeText(getActivity(), "Please check your internet connection .", Toast.LENGTH_LONG).show();

                    } else {
                        Transaction_Result t_result = new Transaction_Result();
                        t_result = sharedClass.showTransactionResult(XML, getActivity());

                        if (t_result != null) {
                            ViewReceipt(t_result);
                        } else {
                            Toast.makeText(getActivity(), "null value", Toast.LENGTH_LONG).show();
                        }

                    }


                    receipt_dialog.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            java.util.Date today = java.util.Calendar.getInstance().getTime();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
                            String currentDateTime = formatter.format(today);
                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();

                            try {

                                String Path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                                File sub = new File(Path, "InterSoft");
                                if (!sub.exists()) sub.mkdirs();
                                String mPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/InterSoft/" + currentDateTime + ".jpg";

                                receipt_dialog.setDrawingCacheEnabled(true);
                                Bitmap bitmap = Bitmap.createBitmap(receipt_dialog.getDrawingCache());
                                receipt_dialog.setDrawingCacheEnabled(false);

                                File imageFile = new File(mPath);

                                FileOutputStream outputStream = new FileOutputStream(imageFile);
                                int quality = 100;
                                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                                outputStream.flush();
                                outputStream.close();

                                Toast.makeText(getActivity(), "Download Done .", Toast.LENGTH_LONG).show();
                            } catch (Throwable e) {
                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                            }


                            return true;
                        }
                    });

                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisableItem, int visibleItemCount, final int totalItemCount) {

                //  final int currentFirstVisPos = absListView.getFirstVisiblePosition();
                final int lastItem = firstVisableItem + visibleItemCount;
                if (lastItem == totalItemCount) {
                    if (preLast != lastItem) {


                        if (true) {

                            final Users obj_User = new Users();
                            SharedClass obj_shar = new SharedClass();
                            handler = new Handler();


                            if (obj_shar.checkInternetConnection(getActivity())) {
                                progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setTitle("Load more data");
                                progressDialog.setMessage("Please wait...");
                                progressDialog.show();

                                Thread thread = new Thread() {
                                    public void run() {
                                        try {
                                            Looper.prepare();
                                            CallWebService.Synchronization(getActivity());
                                        } catch (Exception e) {
                                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                                            e.printStackTrace();
                                        }

                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {

                                                try {
                                                    if (!obj_User.ConnectedToTheDevice.equals("false")) {
                                                        if (obj_User.NoConnectionError.equals("true")) {
                                                            if (!obj_User.webServiceResult.equals("false")) {
                                                                addMoreData(totalItemCount, totalItemCount + defaultPageSize);

                                                                progressDialog.dismiss();

                                                            } else
                                                                Toast.makeText(getActivity(), "Incorrect username or password !", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(getActivity(), "Please check your internet connection .", Toast.LENGTH_LONG).show();
                                                        }
                                                    } else {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(getActivity(), "Please connect to POS device .", Toast.LENGTH_LONG).show();
                                                    }
                                                } catch (Exception ex) {
                                                    Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                                progressDialog.dismiss();
                                            }
                                        });

                                    }
                                };

                                thread.start();
                            } else {
                                addMoreData(totalItemCount, totalItemCount + defaultPageSize);
                                try {
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                }

                            }

                        }
                        preLast = lastItem;
                    }

                }
            }
        });

        return view;
    }

    public boolean addMoreData(int curruntItems, int pageSize) {
        Cursor res;
        boolean result = false;
        try {
            res = obj_db.getAllData("Transaction_Table", Integer.toString(1), Integer.toString(pageSize));


            lst_tra_model = new ArrayList<Transaction_Model>();
            while (res.moveToNext()) {
                Id = Integer.parseInt(res.getString(0));
                Amount = res.getString(1);
                Date = res.getString(2);
                RRN = res.getString(3);
                Transaction_Model t_model = new Transaction_Model(Id, Amount, Date, RRN, "");
                lst_tra_model.add(t_model);
            }

            adapter.updateTransactionList(lst_tra_model);
            result = true;
        } catch (Exception ex) {

        }


        return result;
    }

    public void Sync() throws InterruptedException {
        Thread thread = new Thread() {
            public void run() {
                try {
                    Looper.prepare();
                    CallWebService.Synchronization(getActivity());
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void ViewReceipt(Transaction_Result t_result) {
        receipt_dialog = getActivity().getLayoutInflater().inflate(R.layout.receipt_dialog, null);
        String txt_R_Info = "";
        String txt_Bank_Info = "";
        String txt_DT_EN = "";
        String txt_DT_AR = "";
        String txt_App_Ar = "";
        String txt_App_EN = "";
        String txt_Amount_EN = "";
        String txt_Amount_AR = "";
        String txt_detalis = "";
        String txt_ALLdetails = "";
        String txt_sys_DT_EN = "";
        String txt_sys_DT_AR = "";

        TextView R_Info = (TextView) receipt_dialog.findViewById(R.id.R_Info);
        TextView Bank_Info = (TextView) receipt_dialog.findViewById(R.id.Bank_Info);
        TextView DT_EN = (TextView) receipt_dialog.findViewById(R.id.DT_EN);
        TextView DT_AR = (TextView) receipt_dialog.findViewById(R.id.DT_AR);
        TextView App_Ar = (TextView) receipt_dialog.findViewById(R.id.App_AR);
        TextView App_EN = (TextView) receipt_dialog.findViewById(R.id.App_EN);
        TextView Amount_EN = (TextView) receipt_dialog.findViewById(R.id.Amount_EN);
        TextView Amount_AR = (TextView) receipt_dialog.findViewById(R.id.Amount_AR);
        TextView details = (TextView) receipt_dialog.findViewById(R.id.details);
        TextView ALLdetails = (TextView) receipt_dialog.findViewById(R.id.ALLdetails);
        TextView sys_DT_EN = (TextView) receipt_dialog.findViewById(R.id.sys_DT_EN);
        TextView sys_DT_AR = (TextView) receipt_dialog.findViewById(R.id.sys_DT_AR);


        txt_R_Info = t_result.getRetailerNameArb() + "\n" + t_result.getRetailerNameEng() + "\n" + t_result.getRetaileraddress_arb_1() + "\n" + t_result.getRetaileraddress_eng_1();
        txt_DT_EN = t_result.getTRANSACTION_DATE() + "\n" + t_result.getSTime();
        txt_DT_AR = t_result.getTRANSACTION_DATE_AR() + "\n" + t_result.getSTimeAR();
        txt_Bank_Info = t_result.getBankId() + "\n" + t_result.getMerchantID() + "\n" + t_result.getTerminalID() + "\n" + t_result.getMCC() + "\n" + t_result.getSTAN() + "\n" + t_result.getVersion() + "\n" + t_result.getrrn();
        txt_App_EN = t_result.getApplicationLabelEnglish() + "\n" + t_result.getTransactionTypeEnglish() + "\n" + t_result.getPAN() + "\n" + t_result.getCardExpiryDate();
        txt_App_Ar = t_result.getApplicationLabelArabic() + "\n" + t_result.getTransactionTypeArabic() + "\n" + t_result.getPAAR() + "\n" + t_result.getCardExpiryDateAr();
        txt_Amount_EN = "TOTAL AMOUNT \n" + t_result.getamount() + "\n" + t_result.getResultEN() + "\n" + t_result.getCardholderVerificationEnglish();
        txt_Amount_AR = "المبلغ الإجمالي" + "\n" + t_result.getamountAR() + "\n" + t_result.getResultAR() + "\n" + t_result.getCardholderVerificationArabic();
        txt_detalis = "شكرا لإستخدامكم مدى" + "\n THANK YOU FOR USING mada \n" + "يرجى الإحتفاض بالإصال" + "\n PLEASE RETAIN RECEIPT \n" + "نسخة الكترونية" + "\n ONLINE COPY";
        txt_ALLdetails = t_result.getPosEntryMode() + " " + t_result.getresponseCode() + " " + t_result.getTVR() + " " + t_result.getTIS() + " " + t_result.getCVR() + " " + t_result.getACI() + " " + t_result.getAC();

        txt_sys_DT_EN = t_result.getDateEN().substring(0, 10) + "\n" + t_result.getDateEN().substring(10, 16);
        txt_sys_DT_AR = t_result.getDateAR().substring(0, 10) + "\n" + t_result.getDateAR().substring(10, 16);

        R_Info.setText(txt_R_Info);
        DT_EN.setText(txt_DT_EN);
        DT_AR.setText(txt_DT_AR);
        Bank_Info.setText(txt_Bank_Info);
        App_Ar.setText(txt_App_Ar);
        App_EN.setText(txt_App_EN);
        Amount_EN.setText(txt_Amount_EN);
        Amount_AR.setText(txt_Amount_AR);
        details.setText(txt_detalis);
        ALLdetails.setText(txt_ALLdetails);
        sys_DT_EN.setText(txt_sys_DT_EN);
        sys_DT_AR.setText(txt_sys_DT_AR);
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Transaction receipt");

        dialog.setContentView(receipt_dialog);
        dialog.show();

    }
}


