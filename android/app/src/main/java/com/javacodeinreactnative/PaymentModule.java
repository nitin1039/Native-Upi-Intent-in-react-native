package com.javacodeinreactnative;

import static com.javacodeinreactnative.MainActivity.UPI_PAYMENT_REQUEST_CODE;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.content.Context; // Import Context
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class PaymentModule extends ReactContextBaseJavaModule {
    private static final String TAG = "PaymentModule";
    private static final int UPI_PAYMENT_REQUEST_CODE = 1;
    String transactionReference = generateTransactionReference();

    public PaymentModule(@Nullable ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @NonNull
    @Override
    public String getName() {
        return "GetPayment";
    }

    @ReactMethod
    public void sayHello(String name, Callback callback) {
        try {
            String message = "Hello " + name;
            callback.invoke(null, message);
        } catch (Exception e) {
            callback.invoke(e.getMessage(), null);
        }
    }

    private String generateTransactionReference() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Date());

        // Generate a random 6-digit number
        Random random = new Random();
        int randomNum = random.nextInt(900000) + 100000;

        // Combine timestamp and random number to create a unique reference
        String transactionReference = timestamp + randomNum;

        return transactionReference;
    }
    @ReactMethod
    public void initiateUpiPayment(String upiId, String amount, Callback callback) {
        try {
            Uri uri = new Uri.Builder()
                    .scheme("upi")
                    .authority("pay")
                    .appendQueryParameter("pa", upiId)
                    .appendQueryParameter("pn", "Golden Duck") // Replace with recipient's name
                    .appendQueryParameter("tn", "") // Replace with transaction note
                    .appendQueryParameter("tr", generateTransactionReference()) // Replace with a unique transaction reference
                    .appendQueryParameter("am", amount)
                    .appendQueryParameter("cu", "INR")
                    .build();

            Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
            upiPayIntent.setData(uri);
            Activity currentActivity = getCurrentActivity();
            if (currentActivity != null) {
                currentActivity.startActivityForResult(upiPayIntent, UPI_PAYMENT_REQUEST_CODE);
                callback.invoke(null, "Payment initiated successfully");
            } else {
                callback.invoke("No current activity found", null);
            }
        } catch (Exception e) {
            callback.invoke(e.getMessage(), null);
        }
    }
}
