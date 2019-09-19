package com.mpos.utils;


import com.mpos.mpossdk.api.MPOSServiceCallback;

public class PendingStatus {

    MPOSServiceCallback callback;
    boolean status;
    int statusCode;
    String statusMessage;
    Object result;

    public PendingStatus(MPOSServiceCallback callback, boolean status, int statusCode, String statusMessage, Object result) {
        this.callback = callback;
        this.status = status;
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.result = result;
    }

    public void postStatus() {

        if(status) {
            callback.onComplete(statusCode, statusMessage, result);
        } else {
            callback.onFailed(statusCode, statusMessage);
        }
    }
}
