package tk.e.ip_over_sms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

public abstract class ListenerActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {

                    SmsMessage[] smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

                    String phoneNumber = smsMessages[0].getOriginatingAddress();

                    StringBuilder messageBodyBuilder = new StringBuilder();

                    for (SmsMessage smsMessage : smsMessages) {
                        messageBodyBuilder.append(smsMessage.getMessageBody());
                    }

                    String message = messageBodyBuilder.toString();

                    handleMessage(phoneNumber, message);

                    //Toast.makeText(context, phoneNumber + ":" + message, Toast.LENGTH_SHORT).show();

                }
            }
        };

        intentFilter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(broadcastReceiver, intentFilter);

    }



    @Override
    protected void onStop() {
        unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    protected abstract void handleMessage(String phoneNumber, String message);
}
