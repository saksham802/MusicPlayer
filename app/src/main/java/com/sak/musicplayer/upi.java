package com.sak.musicplayer;

import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.sak.musicplayer.R;

import dev.shreyaspatil.easyupipayment.EasyUpiPayment;
import dev.shreyaspatil.easyupipayment.exception.AppNotFoundException;
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener;
import dev.shreyaspatil.easyupipayment.model.TransactionDetails;

public class upi extends AppCompatActivity {

    private static final String TAG = "UPIActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upi);

        Button btn = findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if UPI apps are installed on the device
                if (areUpiAppsInstalled()) {
                    // Proceed with payment
                    try {
                        EasyUpiPayment.Builder builder = new EasyUpiPayment.Builder(upi.this)
                                .setPayeeVpa("example@vpa")
                                .setPayeeName("Narendra Modi")
                                .setPayeeMerchantCode("12345")
                                .setTransactionId("T2020090212345")
                                .setTransactionRefId("T2020090212345")
                                .setDescription("Description")
                                .setAmount("101.00");

                        EasyUpiPayment easyUpiPayment = builder.build();

                        easyUpiPayment.setPaymentStatusListener(new PaymentStatusListener() {
                            @Override
                            public void onTransactionCompleted(@NonNull TransactionDetails transactionDetails) {
                                // Handle transaction completion
                            }

                            @Override
                            public void onTransactionCancelled() {
                                // Handle transaction cancellation
                            }
                        });

                        // Start UPI payment
                        easyUpiPayment.startPayment();

                    } catch (AppNotFoundException e) {
                        // Handle AppNotFoundException if UPI apps are still not found
                        Log.e(TAG, "UPI App not found: " + e.getMessage());
                        Toast.makeText(upi.this, "UPI App not found. Please install a UPI app.", Toast.LENGTH_LONG).show();
                    } catch (NullPointerException e) {
                        // Handle any other exception
                        Log.e(TAG, "NullPointerException: " + e.getMessage());
                        Toast.makeText(upi.this, "An error occurred. Please try again.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // UPI apps not detected on the device
                    Toast.makeText(upi.this, "No UPI apps found. Please install a UPI app.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Helper method to check if any UPI apps are installed
    private boolean areUpiAppsInstalled() {
        PackageManager pm = getPackageManager();
        try {
            // Check if Google Pay is installed
            pm.getPackageInfo("com.google.android.apps.nbu.paisa.user", PackageManager.GET_ACTIVITIES);
            // Check if PhonePe is installed
            pm.getPackageInfo("com.phonepe.app", PackageManager.GET_ACTIVITIES);
            // Check if Paytm is installed
            pm.getPackageInfo("net.one97.paytm", PackageManager.GET_ACTIVITIES);
            // Check if BHIM is installed
            pm.getPackageInfo("com.phonepe.app", PackageManager.GET_ACTIVITIES);

            // All UPI apps found
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            // One or more UPI apps not installed
            return false;
        }
    }
}
