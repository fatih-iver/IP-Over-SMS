package tk.e.ip_over_sms.sms;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static tk.e.ip_over_sms.sms.SmsConstants.PARAMETERS;
import static tk.e.ip_over_sms.sms.SmsConstants.TYPE;

public class SmsWriter {

    public static String writeSms(int type) {
        JSONObject messageBodyAsJsonObject = new JSONObject();

        try {
            messageBodyAsJsonObject.put(TYPE, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return messageBodyAsJsonObject.toString();
    }


    public static String writeSms(int type, Map<String, String> params) {

        JSONObject messageBodyAsJsonObject = new JSONObject();

        try {
            messageBodyAsJsonObject.put(TYPE, type);
            messageBodyAsJsonObject.put(PARAMETERS, new JSONObject(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return messageBodyAsJsonObject.toString();
    }
}
