package com.codehospital.sms2telegramforwarder

import android.Manifest
import android.Manifest.permission.*
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceFragmentCompat
import com.newrelic.agent.android.NewRelic

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var database: AppDatabase

    private fun callSomeone(number: String) {
        val callIntent = Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("MAINACTIVITY", "No Permission")
            ActivityCompat.requestPermissions(this,
                    arrayOf(RECEIVE_SMS, READ_CONTACTS, CALL_PHONE),
                    MY_PERMISSIONS_REQUEST_READ_SMS)

            return
        }
        Log.d("MAINACTIVITY", "Have Permission")
        if (callIntent.resolveActivity(getPackageManager()) != null)
            startActivity(callIntent)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NewRelic.withApplicationToken(
                "AA7a32db908d1b307f210c65b450beb060895dbd1a"
        ).start(this.getApplication());
        setContentView(R.layout.main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        ActivityCompat.requestPermissions(this,
                arrayOf(RECEIVE_SMS, READ_CONTACTS, CALL_PHONE),
                MY_PERMISSIONS_REQUEST_READ_SMS)
        val permissionCheck = ContextCompat.checkSelfPermission(this, RECEIVE_SMS)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        database = AppDatabase.getInstance(this)!!

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

    }
//
//    protected override fun onDestroy() {
//        database.destroyInstance()
//        super.onDestroy()
//    }
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

    class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
            Log.i(TAG, "$key changed")
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        override fun onResume() {
            super.onResume()
            preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_READ_SMS = 314
        val TAG = "SMS-Forwarder"
//        val database: AppDatabase = AppDatabase.getDatabase(getApplicationContext());
    }
}
