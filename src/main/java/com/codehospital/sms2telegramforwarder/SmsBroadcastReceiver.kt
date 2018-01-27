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
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.util.*


class SmsBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        /* Get Messages */
        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            val sms = getSmsFromIntent(intent)
            val urlStringFormat = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s"
            val apiToken = "488699109:AAEKYpTzU6VNumJIAY_L9yEQZpZND7Da688"
            val chatId = "59755972"
            var sender: String? = null
            var msg: StringBuilder? = null
            var lastPhone: String? = null

            var urlString: String? = null
            for (smsMessage in sms) {
                /* Parse Each Message */
                val phone = smsMessage.originatingAddress
                val message = smsMessage.displayMessageBody
                if (lastPhone == null || !lastPhone.contentEquals(phone)) {
                    if (lastPhone != null && !lastPhone.contentEquals(phone))
                        sendMessageToTelegram(urlString)
                    lastPhone = phone
                    msg = StringBuilder(message)
                    val personUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, phone)
                    val cur = context.contentResolver.query(personUri, arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME), null, null, null)
                    if (cur != null && cur.moveToFirst()) {
                        val nameIndex = cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)
                        sender = cur.getString(nameIndex)
                        cur.close()
                    } else {
                        sender = phone
                    }
                } else {
                    msg!!.append(message)
                }
                val text = sender + ":\n" + msg
                urlString = String.format(urlStringFormat, apiToken, chatId, text.replace("\n", "%0A"))
            } // for
            sendMessageToTelegram(urlString)
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

    private fun sendMessageToTelegram(urlString: String?) {

        val thread = Thread(Runnable {
            try {
                val url = URL(urlString)
                val conn = url.openConnection()
                conn.content
                //            StringBuilder sb = new StringBuilder();
                //            InputStream is = new BufferedInputStream(conn.getInputStream());
                //            BufferedReader br = new BufferedReader(new InputStreamReader(is));
                //            String inputLine = "";
                //            while ((inputLine = br.readLine()) != null) {
                //              sb.append(inputLine);
                //            }
                //            String response = sb.toString();
                // Do what you want with response
            } catch (e: MalformedURLException) {
                Log.e("SmsBroadcastReceiver", e.message)
                Log.e("SmsBroadcastReceiver", Arrays.toString(e.stackTrace))
                e.printStackTrace()
            } catch (e: IOException) {
                Log.e("SmsBroadcastReceiver", e.message)
                Log.e("SmsBroadcastReceiver", Arrays.toString(e.stackTrace))
                e.printStackTrace()
            }
        })

        thread.start()
    }
}
