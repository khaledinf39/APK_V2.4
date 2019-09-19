package com.mpos.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.mpos.activity.StartingActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by m.alibraheem on 01/02/2018.
 */

public class userSessionManager extends Application {
    public void startUserSession(final Activity activity)
    {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run()
            {
                activity.finish();
                Intent myIntent = new Intent(activity, StartingActivity.class);
                activity.startActivity(myIntent);

            }
        },5000);
    }
}
