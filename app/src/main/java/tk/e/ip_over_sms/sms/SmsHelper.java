package tk.e.ip_over_sms.sms;

import android.telephony.SmsManager;

import java.util.ArrayList;

public class SmsHelper {

    public static void sendSms(String phoneNumber, String message){
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> parts = smsManager.divideMessage(message);
        smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
    };

}
