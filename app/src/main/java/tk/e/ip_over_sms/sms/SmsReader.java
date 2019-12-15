package tk.e.ip_over_sms.sms;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.PortUnreachableException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SmsReader {

    public static IPMessage readSms(String smsMessage){

        IPMessage ipMessage = null;

        try {

            JSONObject smsMessageAsJson = new JSONObject(smsMessage);

            int type = smsMessageAsJson.getInt(SmsConstants.TYPE);

            ipMessage = new IPMessage(type);

            if(smsMessageAsJson.has(SmsConstants.PARAMETERS)){

                String params = smsMessageAsJson.getString(SmsConstants.PARAMETERS);
                JSONObject paramsAsJson = new JSONObject(params);

                ipMessage.initializeParams();

                for (Iterator<String> it = paramsAsJson.keys(); it.hasNext(); ) {
                    String key = it.next();
                    ipMessage.addParam(key, paramsAsJson.getString(key));
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ipMessage;
    }



}
