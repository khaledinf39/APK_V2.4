package com.mpos.Model;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class terminal {
    private String Name,Type,Amount,Date,Appr_code,RRN,tid;

    public terminal(String amount, String tid) {
        Amount = amount;
        this.tid = tid;
    }

    public String getAmount() {
        return Amount;
    }

    public String getTid() {
        return tid;
    }

    public terminal() {
    }

    public terminal(String name, String type, String amount, String date, String appr_code, String RRN, String tid) {
        Name = name;
        Type = type;
        Amount = amount;
        Date = date;
        Appr_code = appr_code;
        this.RRN = RRN;
        this.tid=tid;
    }
    public interface OnCoupon_lisennter{
        void onSuccess(int status);
        void onSuccess(terminal term);

        void onStart();
        void onFailure(String msg);
    }
    public void Post_add(final Context mcontext, final OnCoupon_lisennter listener){
        listener.onStart();
        RequestQueue queue = Volley.newRequestQueue(mcontext);  // this = context
        String url = "http://zabeb.com/terminal/add.php";




        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        String jsonData = response;
                        JSONObject Jobject = null;
                        try {
                            Jobject = new JSONObject(jsonData);
                            listener.onSuccess(Jobject.getInt("status"));
                        } catch (JSONException e1) {
                            listener.onFailure(e1.getMessage());
                            e1.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", String.valueOf(error.getMessage()));
                        listener.onFailure("Error.Response");
                    }
                }
        ) {


            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  parameters = new HashMap<String, String>();
//
                parameters.put("Name",Name);
                parameters.put("Type",Type);
                parameters.put("Amount",Amount);
                parameters.put("Date",Date);
                parameters.put("Appr_code",Appr_code);
                parameters.put("RRN",RRN);
                parameters.put("tid",tid);


                return parameters;
            }
        };
        queue.getCache().initialize();
        queue.add(postRequest);
        queue.getCache().clear();

        // prepare the Request

    }
    public void GET_data(final Context mcontext,String url, final OnCoupon_lisennter listener){
        listener.onStart();
        RequestQueue queue = Volley.newRequestQueue(mcontext);  // this = context
//        String url = "http://zabeb.com/terminal/mpos.php?tid="+tid+"&price="+Amount;




        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        String jsonData = response;
                        JSONObject Jobject = null;
                        try {
                            Jobject = new JSONObject(jsonData);
                            listener.onSuccess(Jobject.getInt("status"));

                            tid=Jobject.getString("tid");
                            Amount=Jobject.getString("price");
                            terminal term=new terminal(Amount,tid);
                            listener.onSuccess(term);
                        } catch (JSONException e1) {
                            listener.onFailure(e1.getMessage());
                            e1.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", String.valueOf(error.getMessage()));
                        listener.onFailure("Error.Response");
                    }
                }
        ) {



        };
        queue.getCache().initialize();
        queue.add(postRequest);
        queue.getCache().clear();

        // prepare the Request

    }

}
