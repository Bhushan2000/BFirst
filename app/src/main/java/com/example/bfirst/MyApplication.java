package com.example.bfirst;

import android.app.Application;
import android.content.Context;

import net.one97.paytm.nativesdk.PaytmSDK;

public class MyApplication extends Application {

    private static Application context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

     /* If you has other classes that need context object to initialize when application is created,
         you can use the appContext here to process. */

        // paytm  Initialisation of SDK
        PaytmSDK.init(context);
    }

    public static Application getContext() {
        return context;
    }
}
