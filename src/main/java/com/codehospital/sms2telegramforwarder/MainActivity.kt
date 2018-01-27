package com.codehospital.sms2telegramforwarder

import android.Manifest.permission.READ_CONTACTS
import android.Manifest.permission.RECEIVE_SMS
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.WindowManager
import butterknife.ButterKnife

class MainActivity : Activity() {


//    @BindView(R.id.senderIdInputView)
//    internal var senderId: EditText? = null
//
//    @BindView(R.id.forwardingUrlInputView)
//    internal var forwardingUrl: EditText? = null
//
//    @BindView(R.id.forwardingEnabled)
//    internal var forwardingEnabled: CheckBox? = null
//
//    @BindView(R.id.senderIdPanel)
//    internal var senderIdPanel: View? = null

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_SMS -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
        }// other 'case' lines to check for other
        // permissions this app might request.
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        ButterKnife.bind(this)
        //        int permissionCheck = ContextCompat.checkSelfPermission(this, RECEIVE_SMS);
        ActivityCompat.requestPermissions(this,
                arrayOf(RECEIVE_SMS, READ_CONTACTS),
                MY_PERMISSIONS_REQUEST_READ_SMS)
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_READ_SMS = 314
    }
}
