package tk.e.ip_over_sms.sms;

import android.telephony.SmsManager;

import java.util.ArrayList;
import java.util.List;

public class SmsSender {

    private static final String DEFAULT_PHONE_NUMBER = "905076319260";

    private static SmsManager smsManager = SmsManager.getDefault();

    public static void sendMultipartSms(String message){
        sendMultipartSms(DEFAULT_PHONE_NUMBER, message);
      };

    public static void sendMultipartSms(String phoneNumber, String message){
        ArrayList<String> parts = smsManager.divideMessage(message);
        smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
    };


}
