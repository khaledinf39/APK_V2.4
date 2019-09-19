package com.mpos.utils;

import android.os.AsyncTask;

/**
 * Created by m.alibraheem on 26/02/2018.
 */

public class BluetoothConnectAsyntask extends AsyncTask<String, Void, String> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("inside preexecute");
    }

    @Override
    protected String doInBackground(String... params) {

        try {

            System.out.println("inside doBackground");
				/*HomeActivity.controller.startBTv2(new String[] {
						"iBT-02 Demo", "WisePad", "WP" });*/
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String file_url) {
        // dismiss the dialog after the file was downloaded
        System.out.println("inside postexecute");
    }
}