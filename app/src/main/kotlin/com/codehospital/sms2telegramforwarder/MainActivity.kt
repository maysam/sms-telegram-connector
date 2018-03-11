package com.codehospital.sms2telegramforwarder

import android.Manifest
import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.main.*/// HERE "YOUR_LAYOUT_NAME" IS YOUR LAYOUT NAME WHICH U HAVE INFLATED IN onCreate()/onCreateView()
import com.newrelic.agent.android.NewRelic
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration
import android.arch.persistence.room.Room






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


    public final fun callSomeone(number: String) {
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
    var database: AppDatabase? = null;
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
        //        int permissionCheck = ContextCompat.checkSelfPermission(this, RECEIVE_SMS);
        val numberBox : EditText = findViewById(R.id.senderIdInputView) as EditText
        val saveProject: Button = findViewById(R.id.saveProjectId) as Button
        saveProject.setOnClickListener { callSomeone(numberBox.getText().toString()) }
        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase::class.java!!, "sms-database-name").build()

//        Room.databaseBuilder(getApplicationContext(), MyDb::class.java!!, "database-name")
//                .addMigrations(MIGRATION_1_2, MIGRATION_2_3).build()
//
//        val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("CREATE TABLE `Fruit` (`id` INTEGER, " + "`name` TEXT, PRIMARY KEY(`id`))")
//            }
//        }


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

    companion object {
        private const val MY_PERMISSIONS_REQUEST_READ_SMS = 314

//        val database: AppDatabase = AppDatabase.getDatabase(getApplicationContext());
    }
}
