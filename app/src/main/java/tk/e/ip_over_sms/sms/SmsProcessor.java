package tk.e.ip_over_sms.sms;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;

public class SmsProcessor {

    private static RequestQueue requestQueue = null;
    private static final HashMap<Integer, Integer> methodMapper = new HashMap<>();
    private static final HashMap<Integer, String> urlMapper = new HashMap<>();

    static {
        methodMapper.put(SmsConstants.EXCHANGE_RATES_REQUEST, Request.Method.GET);
        urlMapper.put(SmsConstants.EXCHANGE_RATES_REQUEST, "https://api.exchangeratesapi.io/latest?base=TRY");

    }

    public static void processRequest(Context context, IPMessage ipMessage) {

        int type = ipMessage.getType();

        switch (type) {
            case SmsConstants.HEALTH_CHECK_REQUEST:
                break;
            case SmsConstants.HEALTH_CHECK_RESPONSE:
                break;
            case SmsConstants.EXCHANGE_RATES_REQUEST:
                break;
            case SmsConstants.EXCHANGE_RATES_RESPONSE:
                break;
            case SmsConstants.RANDOM_QUOTE_REQUEST:
                break;
            case SmsConstants.RANDOM_OUOTE_RESPONSE:
                break;
            case SmsConstants.SEND_EMAIL_REQUEST:
                break;
            case SmsConstants.SEND_EMAIL_RESPONSE:
                break;
            default:
                break;

        }
    }


    public static void handleNetworkRequest(Context context, String type){


        StringRequest stringRequest = new StringRequest(methodMapper.get(type), urlMapper.get(type),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        getRequestQueue(context).add(stringRequest);


    }

    private static RequestQueue getRequestQueue(Context context) {
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

}
