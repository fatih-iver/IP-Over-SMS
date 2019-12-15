package tk.e.ip_over_sms.actions;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import tk.e.ip_over_sms.ListenerActivity;
import tk.e.ip_over_sms.R;
import tk.e.ip_over_sms.sms.IPMessage;
import tk.e.ip_over_sms.sms.SmsConstants;
import tk.e.ip_over_sms.sms.SmsReader;
import tk.e.ip_over_sms.sms.SmsSender;
import tk.e.ip_over_sms.sms.SmsWriter;

public class QuoteActivity extends ListenerActivity {

    private TextView textView_author;
    private TextView textView_quote;
    private TextView textView_tags;

    private Button button;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        textView_author = findViewById(R.id.textView_author);
        textView_quote = findViewById(R.id.textView_quote);
        textView_tags = findViewById(R.id.textView_tags);

        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                String message = SmsWriter.writeSms(SmsConstants.QUOTE_OF_THE_DAY_REQUEST);
                SmsSender.sendMultipartSms(message);
                Toast.makeText(QuoteActivity.this, "Requested!", Toast.LENGTH_SHORT).show();
            }
        });

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

    }

    @Override
    protected void handleMessage(String phoneNumber, String message) {
        button.setEnabled(true);
        progressBar.setVisibility(View.INVISIBLE);
        IPMessage ipMessage = SmsReader.readSms(message);
        if(ipMessage.getType() == SmsConstants.ERROR_OCCURED){
            Toast.makeText(QuoteActivity.this, "An error occured!", Toast.LENGTH_SHORT).show();
        } else {
            textView_author.setText(ipMessage.getParams().get("author"));
            textView_quote.setText(ipMessage.getParams().get("quote"));
            textView_tags.setText(ipMessage.getParams().get("tags"));        }

    }
}
