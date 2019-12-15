package tk.e.ip_over_sms.actions;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import tk.e.ip_over_sms.ListenerActivity;
import tk.e.ip_over_sms.R;
import tk.e.ip_over_sms.sms.IPMessage;
import tk.e.ip_over_sms.sms.SmsConstants;
import tk.e.ip_over_sms.sms.SmsReader;
import tk.e.ip_over_sms.sms.SmsSender;
import tk.e.ip_over_sms.sms.SmsWriter;

public class ExchangeRatesActivity extends ListenerActivity {

    private Button button;
    private ProgressBar progressBar;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_rates);

        button = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                String message = SmsWriter.writeSms(SmsConstants.EXCHANGE_RATES_REQUEST);
                SmsSender.sendMultipartSms(message);
                Toast.makeText(ExchangeRatesActivity.this, "Requested!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void handleMessage(String phoneNumber, String message) {
        button.setEnabled(true);
        progressBar.setVisibility(View.INVISIBLE);
        try {
            JSONObject messageAsJson = new JSONObject(message);
            JSONObject paramsAsJson = messageAsJson.getJSONObject(SmsConstants.PARAMETERS);
            paramsAsJson.getJSONObject("rates");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IPMessage ipMessage = SmsReader.readSms(message);
        textView.setText(ipMessage.getParams().toString());
    }

}
