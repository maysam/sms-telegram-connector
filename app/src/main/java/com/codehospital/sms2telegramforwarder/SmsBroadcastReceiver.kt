package com.codehospital.sms2telegramforwarder

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.provider.Telephony.Sms.Intents.getMessagesFromIntent
import android.telephony.SmsMessage
import android.telephony.SmsMessage.createFromPdu
import android.util.Log
import android.widget.Toast
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.codehospital.sms2telegramforwarder.MainActivity.Companion.TAG
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URLEncoder


class SmsBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        /* Get Messages */
        // Engine.startTheAlarm(context);

        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            val sms = getSmsFromIntent(intent)
            val urlStringFormat = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text="

            val sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context /* Activity context */)
            val apiToken = sharedPreferences.getString("telegram_token", "")!!
            val chatId = sharedPreferences.getString("telegram_id", "")!!

            var sender: String? = null
            var msg: StringBuilder? = null
            var lastPhone: String? = null

            var urlString = ""
            for (smsMessage in sms) {
                /* Parse Each Message */
                val phone = smsMessage.originatingAddress
                val message = smsMessage.displayMessageBody
                if (lastPhone == null || lastPhone != phone) {
                    if (lastPhone != null && lastPhone != phone) {
                        val x = Payamak()
                        x.from_person = sender
                        x.from_phone = lastPhone
                        x.text = msg.toString()
                        sendMessageToTelegram(context, urlString, x)
                    }
                    lastPhone = phone
                    msg = StringBuilder(message)
                    val personUri =
                        Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, phone)
                    val cur = context.contentResolver.query(
                        personUri,
                        arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME),
                        null,
                        null,
                        null
                    )
                    if (cur != null && cur.moveToFirst()) {
                        val nameIndex =
                            cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)
                        sender = cur.getString(nameIndex)
                        cur.close()
                    } else {
                        sender = phone
                    }
                } else {
                    msg!!.append(message)
                }
                val text = URLEncoder.encode("$sender:\n$msg".trim(), "UTF-8")
                urlString = String.format(urlStringFormat, apiToken, chatId) + text
            } // for
            val x = Payamak()
            x.from_person = sender
            x.from_phone = lastPhone
            x.text = msg.toString()
            sendMessageToTelegram(context, urlString, x)
        }
    }

    private fun getSmsFromIntent(intent: Intent): Array<SmsMessage> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            getMessagesFromIntentApi19(intent)
        else
            getMessagesFromIntentApi15(intent)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun getMessagesFromIntentApi19(intent: Intent): Array<SmsMessage> {
        return getMessagesFromIntent(intent)
    }

    private fun getMessagesFromIntentApi15(intent: Intent): Array<SmsMessage> {
        val pdus = intent.getSerializableExtra("pdus") as Array<*>
        return pdus.map {
            @Suppress("DEPRECATION")
            createFromPdu(it as ByteArray)
        }.toTypedArray()
    }

    private fun sendMessageToTelegram(context: Context, urlString: String, x: Payamak) {
        val app = context.applicationContext
        val database =
            Room.databaseBuilder(app, AppDatabase::class.java, "appDatabase.db")
                .fallbackToDestructiveMigration()
                .build()
        val forwardingUrl = "https://sms-forwarding-proxy.herokuapp.com/forward"
        //AppDatabase.getInMemoryDatabase(context.getApplication());
        Log.d(TAG, urlString)
        doAsync {
            database.payamakDao().insert(x)
        }
        doAsync {

            val client = OkHttpClient()

            val formBody = FormBody.Builder()
                .add("url", urlString)
                .build()
            val request = Request.Builder()
                .url(forwardingUrl)
                .post(formBody)
                .build()

            val response = client.newCall(request).execute()
            Log.i(TAG, response.message)

            uiThread {
                Toast.makeText(app, x.text, Toast.LENGTH_SHORT).show()
            }
        }
    }
}