package tk.e.ip_over_sms.sms;

public class SmsHelper {

    public static void handleSmsMessage(String from, String smsMessage) {

        IPMessage ipMessage = SmsReader.readSms(smsMessage);




    }
}
