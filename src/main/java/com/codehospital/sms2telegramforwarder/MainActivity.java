package com.codehospital.sms2telegramforwarder;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.RECEIVE_SMS;

public class MainActivity extends Activity
{

//    static final String TAG = "SMS Gateway";
    private static final int MY_PERMISSIONS_REQUEST_READ_SMS = 314;


    @BindView(R.id.senderIdInputView)
    EditText senderId;

    @BindView(R.id.forwardingUrlInputView)
    EditText forwardingUrl;

    @BindView(R.id.forwardingEnabled)
    CheckBox forwardingEnabled;

    @BindView(R.id.senderIdPanel)
    View senderIdPanel;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
//  public static final String RECEIVE_SMS = "android.permission.RECEIVE_SMS";
//  public static final String READ_CONTACTS = "android.permission.RECEIVE_SMS";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ButterKnife.bind(this);
//        int permissionCheck = ContextCompat.checkSelfPermission(this, RECEIVE_SMS);
        ActivityCompat.requestPermissions(this,
                new String[]{RECEIVE_SMS, READ_CONTACTS},
                MY_PERMISSIONS_REQUEST_READ_SMS);
    }
}
