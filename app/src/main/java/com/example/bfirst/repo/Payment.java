package com.example.bfirst.repo;

import android.app.Application;
import android.content.Context;

import com.example.bfirst.MyApplication;
import com.example.bfirst.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;

import net.one97.paytm.nativesdk.PaytmSDK;
import net.one97.paytm.nativesdk.dataSource.PaytmPaymentsUtilRepository;

public class Payment {

    Context context = MyApplication.getContext();

    // Link Paytm user account and fetch secured tokens
    PaytmPaymentsUtilRepository paymentsUtilRepository = PaytmSDK.getPaymentsUtilRepository();

    // A. Check if Paytm app is installed on user's phone
    boolean isInstalled = paymentsUtilRepository.isPaytmAppInstalled(context);
    // B. Include the SDK’s consent checkbox on your UI.
    // C. Fetch authCode for the logged in user

    // Call the method fetchAuthCode on the background thread in order to get the authcode for the currently logged in user
    // inside Paytm App The authCode will be fetched only if the user has given the consent on the above checkbox.
    String authCode = paymentsUtilRepository.fetchAuthCode(context, Utils.CLIENT_ID, Utils.MERCHANT_ID);
//   context Context :	The current application Context
//   clientId String	: The clientId issued from Paytm identifying a merchant
//   mid String	: Merchant Id identifying a merchant




}
