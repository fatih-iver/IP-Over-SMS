package tk.e.ip_over_sms.actions;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

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
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_rates);

        button = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.textView);
        listView =findViewById(R.id.listView);

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
        IPMessage ipMessage = SmsReader.readSms(message);
        if(ipMessage.getType() == SmsConstants.ERROR_OCCURED){
            Toast.makeText(ExchangeRatesActivity.this, "An error occured!", Toast.LENGTH_SHORT).show();
        } else {
            //textView.setText(ipMessage.getParams().toString());

            String ratesAsString = ipMessage.getParams().get("rates");
            try {
                JSONObject ratesAsJson = new JSONObject(ratesAsString);

                Iterator<String> iterator = ratesAsJson.keys();

                ArrayList<String> items = new ArrayList<>();

                while(iterator.hasNext()) {
                    String key = iterator.next();
                    items.add("1 " + key + " = " + String.format("%.6f", (1.0 / ratesAsJson.getDouble(key))) + " TRY");
                }

                ArrayAdapter<String> itemsAdapter =
                        new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
                listView.setAdapter(itemsAdapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
