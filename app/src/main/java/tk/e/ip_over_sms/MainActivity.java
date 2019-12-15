package tk.e.ip_over_sms;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tk.e.ip_over_sms.actions.ExchangeRatesActivity;
import tk.e.ip_over_sms.actions.QuoteActivity;
import tk.e.ip_over_sms.actions.SendEmailActivity;
import tk.e.ip_over_sms.actions.TranslatorActivity;
import tk.e.ip_over_sms.sms.IPMessage;
import tk.e.ip_over_sms.sms.SmsConstants;
import tk.e.ip_over_sms.sms.SmsReader;
import tk.e.ip_over_sms.sms.SmsSender;
import tk.e.ip_over_sms.sms.SmsWriter;

import static tk.e.ip_over_sms.sms.SmsConstants.PARAMETERS;
import static tk.e.ip_over_sms.sms.SmsConstants.TYPE;

public class MainActivity extends ListenerActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;

    private static String EXCHANGE_RATES = "Exchange Rates";
    private static String RANDOM_QUOTE = "Random Quote";
    private static String SEND_EMAIL = "Send Email";

    private RequestQueue requestQueue;


    private Button button_healthCheck;
    private Button button_exchangeRates;
    private Button button_randomQuote;
    private Button button_sendEmail;
    private Button button_translateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);

        button_healthCheck = findViewById(R.id.button_healthCheck);

        button_healthCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_healthCheck.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(!button_healthCheck.isEnabled()){
                            button_healthCheck.setEnabled(true);
                            Toast.makeText(MainActivity.this, "Server is not responding!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 15000);
                SmsSender.sendMultipartSms(SmsWriter.writeSms(SmsConstants.HEALTH_CHECK_REQUEST));
                Toast.makeText(MainActivity.this, "Requested!", Toast.LENGTH_SHORT).show();
            }
        });

        button_exchangeRates = findViewById(R.id.button_exchangeRates);

        button_exchangeRates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ExchangeRatesActivity.class));
            }
        });

        button_randomQuote = findViewById(R.id.button_randomQuote);

        button_randomQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, QuoteActivity.class));
            }
        });

        button_sendEmail = findViewById(R.id.button_sendEmail);

        button_sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SendEmailActivity.class));

            }
        });

        button_translateText = findViewById(R.id.button_translateText);

        button_translateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TranslatorActivity.class));

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_DENIED) {

                String[] permissions = {Manifest.permission.SEND_SMS};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);

            }

            if (checkSelfPermission(Manifest.permission.RECEIVE_SMS)
                    == PackageManager.PERMISSION_DENIED) {

                String[] permissions = {Manifest.permission.RECEIVE_SMS};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);

            }

            if (checkSelfPermission(Manifest.permission.READ_SMS)
                    == PackageManager.PERMISSION_DENIED) {

                String[] permissions = {Manifest.permission.READ_SMS};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);

            }
        }
    }

    @Override
    protected void handleMessage(final String phoneNumber, String message) {
        IPMessage ipMessage = SmsReader.readSms(message);

        int type = ipMessage.getType();

        switch (type) {
            case SmsConstants.HEALTH_CHECK_REQUEST:
                SmsSender.sendMultipartSms(phoneNumber, SmsWriter.writeSms(SmsConstants.HEALTH_CHECK_RESPONSE));
                break;
            case SmsConstants.HEALTH_CHECK_RESPONSE:
                button_healthCheck.setEnabled(true);
                Toast.makeText(MainActivity.this, "Server is healthy!", Toast.LENGTH_SHORT).show();
                break;
            case SmsConstants.EXCHANGE_RATES_REQUEST:

                Toast.makeText(this, "Exchange Rates Request", Toast.LENGTH_SHORT).show();

                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.exchangeratesapi.io/latest?base=TRY",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONObject messageBodyAsJsonObject = new JSONObject();

                                try {
                                    messageBodyAsJsonObject.put(TYPE, SmsConstants.EXCHANGE_RATES_RESPONSE);
                                    messageBodyAsJsonObject.put(PARAMETERS, new JSONObject(response));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                SmsSender.sendMultipartSms(phoneNumber, messageBodyAsJsonObject.toString());

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = SmsWriter.writeSms(SmsConstants.ERROR_OCCURED);
                        SmsSender.sendMultipartSms(phoneNumber, message);
                    }
                });

                requestQueue.add(stringRequest);

                break;
            case SmsConstants.EXCHANGE_RATES_RESPONSE:
                break;
            case SmsConstants.QUOTE_OF_THE_DAY_REQUEST:

                Toast.makeText(this, "Quote of the Day Request", Toast.LENGTH_SHORT).show();

                StringRequest stringRequest1 = new StringRequest(Request.Method.GET, "https://quotes.rest/qod.json",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONObject messageBodyAsJsonObject = new JSONObject();

                                try {
                                    messageBodyAsJsonObject.put(TYPE, SmsConstants.EXCHANGE_RATES_RESPONSE);

                                    JSONObject paramsAsJson = new JSONObject();

                                    JSONObject responseAsJson = new JSONObject(response);

                                    JSONObject contentsAsJson = responseAsJson.getJSONObject("contents");

                                    JSONArray quotesAsJson = contentsAsJson.getJSONArray("quotes");

                                    JSONObject quoteAsJson = (JSONObject) quotesAsJson.get(0);

                                    paramsAsJson.put("author", quoteAsJson.getString("author"));

                                    paramsAsJson.put("quote", quoteAsJson.getString("quote"));

                                    paramsAsJson.put("tags", quoteAsJson.getString("tags"));

                                    messageBodyAsJsonObject.put(PARAMETERS, paramsAsJson);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                SmsSender.sendMultipartSms(phoneNumber, messageBodyAsJsonObject.toString());

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = SmsWriter.writeSms(SmsConstants.ERROR_OCCURED);
                        SmsSender.sendMultipartSms(phoneNumber, message);
                    }
                });

                requestQueue.add(stringRequest1);
                break;
            case SmsConstants.QUOTE_OF_THE_DAY_RESPONSE:
                break;
            case SmsConstants.SEND_EMAIL_REQUEST:
                Toast.makeText(this, "Send Email Request", Toast.LENGTH_SHORT).show();

                JSONObject jsonObject = new JSONObject();

                try {

                    jsonObject.put("personalizations",new JSONArray().put(new JSONObject().put("to", new JSONArray().put(new JSONObject().put("email", ipMessage.getParams().get("to"))))));
                    jsonObject.put("from", new JSONObject().put("email", ipMessage.getParams().get("from")));
                    jsonObject.put("subject", ipMessage.getParams().get("subject"));
                    jsonObject.put("content", new JSONArray().put(new JSONObject().put("type", "text/plain").put("value", ipMessage.getParams().get("content"))));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://api.sendgrid.com/v3/mail/send", jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Map<String, String> params = new HashMap<>();
                                params.put("result", "Delivered Successfully");
                                String message = SmsWriter.writeSms(SmsConstants.SEND_EMAIL_RESPONSE, params);
                                SmsSender.sendMultipartSms(phoneNumber, message);

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = SmsWriter.writeSms(SmsConstants.ERROR_OCCURED);
                        SmsSender.sendMultipartSms(phoneNumber, message);
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Authorization", "Bearer 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");
                        params.put("Content-Type", "application/json");
                        return params;
                    }

                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        try {
                            String jsonString = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

                            JSONObject result = null;

                            if (jsonString != null && jsonString.length() > 0)
                                result = new JSONObject(jsonString);

                            return Response.success(result,
                                    HttpHeaderParser.parseCacheHeaders(response));
                        } catch (UnsupportedEncodingException e) {
                            return Response.error(new ParseError(e));
                        } catch (JSONException je) {
                            return Response.error(new ParseError(je));
                        }
                    }
                };

                requestQueue.add(jsonObjectRequest);
                break;
            case SmsConstants.SEND_EMAIL_RESPONSE:
                break;
            case SmsConstants.TEXT_TRANSLATE_REQUEST:

                Toast.makeText(this, "Text Translate Request", Toast.LENGTH_SHORT).show();

                String URL = new Uri.Builder().scheme("https")
                        .authority("translate.yandex.net")
                        .appendPath("api")
                        .appendPath("v1.5")
                        .appendPath("tr.json")
                        .appendPath("translate")
                        .appendQueryParameter("key", "trnsl.1.1.20191216T183152Z.67859a1bef2e73e9.29dff788998d87c6ab4c25a4fa4355fe0b638bab")
                        .appendQueryParameter("text", ipMessage.getParams().get("q"))
                        .appendQueryParameter("lang", ipMessage.getParams().get("sl") + "-" + ipMessage.getParams().get("tl"))
                        .build()
                        .toString();

                StringRequest stringRequest2 = new StringRequest(Request.Method.GET, URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                JSONObject messageBodyAsJsonObject = new JSONObject();

                                try {

                                    messageBodyAsJsonObject.put(TYPE, SmsConstants.TEXt_TRANSLATE_RESPONSE);
                                    messageBodyAsJsonObject.put(PARAMETERS, new JSONObject().put("t", new JSONObject(response).getJSONArray("text").getString(0)));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                SmsSender.sendMultipartSms(phoneNumber, messageBodyAsJsonObject.toString());

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = SmsWriter.writeSms(SmsConstants.ERROR_OCCURED);
                        SmsSender.sendMultipartSms(phoneNumber, message);
                    }
                });

                requestQueue.add(stringRequest2);

                break;
            case SmsConstants.TEXt_TRANSLATE_RESPONSE:
                break;
            default:
                break;

        }

        Toast.makeText(this, phoneNumber + ":" + message, Toast.LENGTH_SHORT).show();
    }

}
