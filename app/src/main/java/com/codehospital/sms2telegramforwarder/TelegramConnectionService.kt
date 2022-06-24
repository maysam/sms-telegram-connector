package com.codehospital.sms2telegramforwarder

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.*

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 *
 *
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class TelegramConnectionService : IntentService("TelegramConnectionService") {

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val action = intent.action
            if (ACTION_FOO == action) {

                    val urlString = "https://api.telegram.org/bot488699109:AAEKYpTzU6VNumJIAY_L9yEQZpZND7Da688/getUpdates"
                    try {
                        val url = URL(urlString)
                        val conn = url.openConnection()
                        val sb = StringBuilder()
                        val bis =  java.io.BufferedInputStream(conn.getInputStream())
                        val br = java.io.BufferedReader(InputStreamReader(bis))
                        var inputLine = br.readLine()
                        while (inputLine != null) {
                          sb.append(inputLine)
                            inputLine=br.readLine()
                        }
                        val response = sb.toString()
                        // Do what you want with response
                    } catch (e: MalformedURLException) {
                        Log.e("SmsBroadcastReceiver", e.message!!)
                        Log.e("SmsBroadcastReceiver", Arrays.toString(e.stackTrace))
                        e.printStackTrace()
                    } catch (e: IOException) {
                        Log.e("SmsBroadcastReceiver", e.message!!)
                        Log.e("SmsBroadcastReceiver", Arrays.toString(e.stackTrace))
                        e.printStackTrace()
                    }
//                {
//                    "ok": true,
//                    "result": [
//                    {
//                        "update_id": 823472413,
//                        "message": {
//                        "message_id": 188,
//                        "from": {
//                        "id": 59755972,
//                        "is_bot": false,
//                        "first_name": "Maysam",
//                        "last_name": "Torabi",
//                        "username": "maysam",
//                        "language_code": "en-US"
//                    },
//                        "chat": {
//                        "id": 59755972,
//                        "first_name": "Maysam",
//                        "last_name": "Torabi",
//                        "username": "maysam",
//                        "type": "private"
//                    },
//                        "date": 1517905223,
//                        "text": "call baby"
//                    }
//                    }
//                    ]
//                }
//                https@ //api.telegram.org/bot488699109:AAEKYpTzU6VNumJIAY_L9yEQZpZND7Da688/getUpdates
                val param1 = intent.getStringExtra(EXTRA_PARAM1)!!
                val param2 = intent.getStringExtra(EXTRA_PARAM2)!!
                handleActionFoo(param1, param2)
            } else if (ACTION_BAZ == action) {
                val param1 = intent.getStringExtra(EXTRA_PARAM1)!!
                val param2 = intent.getStringExtra(EXTRA_PARAM2)!!
                handleActionBaz(param1, param2)
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionFoo(param1: String, param2: String) {
        // TODO: Handle action Foo
        throw UnsupportedOperationException("Not yet implemented")
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionBaz(param1: String, param2: String) {
        // TODO: Handle action Baz
        throw UnsupportedOperationException("Not yet implemented")
    }

    companion object {
        // TODO: Rename actions, choose action names that describe tasks that this
        // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
        private val ACTION_FOO = "com.codehospital.sms2telegramforwarder.action.FOO"
        private val ACTION_BAZ = "com.codehospital.sms2telegramforwarder.action.BAZ"

        // TODO: Rename parameters
        private val EXTRA_PARAM1 = "com.codehospital.sms2telegramforwarder.extra.PARAM1"
        private val EXTRA_PARAM2 = "com.codehospital.sms2telegramforwarder.extra.PARAM2"

        /**
         * Starts this service to perform action Foo with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        // TODO: Customize helper method
        fun startActionFoo(context: Context, param1: String, param2: String) {
            val intent = Intent(context, TelegramConnectionService::class.java)
            intent.action = ACTION_FOO
            intent.putExtra(EXTRA_PARAM1, param1)
            intent.putExtra(EXTRA_PARAM2, param2)
            context.startService(intent)
        }

        /**
         * Starts this service to perform action Baz with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        // TODO: Customize helper method
        fun startActionBaz(context: Context, param1: String, param2: String) {
            val intent = Intent(context, TelegramConnectionService::class.java)
            intent.action = ACTION_BAZ
            intent.putExtra(EXTRA_PARAM1, param1)
            intent.putExtra(EXTRA_PARAM2, param2)
            context.startService(intent)
        }
    }
}
