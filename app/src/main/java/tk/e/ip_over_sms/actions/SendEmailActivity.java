package tk.e.ip_over_sms.actions;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import tk.e.ip_over_sms.ListenerActivity;
import tk.e.ip_over_sms.R;
import tk.e.ip_over_sms.sms.IPMessage;
import tk.e.ip_over_sms.sms.SmsConstants;
import tk.e.ip_over_sms.sms.SmsReader;
import tk.e.ip_over_sms.sms.SmsSender;
import tk.e.ip_over_sms.sms.SmsWriter;

public class SendEmailActivity extends ListenerActivity {

    private EditText editText_from;
    private EditText editText_to;
    private EditText editText_subject;
    private EditText editText_content;

    private Button button;
    private ProgressBar progressBar;

    private Button button_fillTestData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        editText_from = findViewById(R.id.editText_from);
        editText_to = findViewById(R.id.editText_to);
        editText_subject = findViewById(R.id.editText_subject);
        editText_content = findViewById(R.id.editText_content);

        button = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String to = editText_to.getText().toString();

                if(TextUtils.isEmpty(to)){
                    editText_to.setError("cannot be empty");
                    return;
                }

                String from = editText_from.getText().toString();

                if(TextUtils.isEmpty(from)){
                    editText_from.setError("cannot be empty");
                    return;
                }

                String subject = editText_subject.getText().toString();

                if(TextUtils.isEmpty(subject)){
                    editText_subject.setError("cannot be empty");
                    return;
                }


                String content = editText_content.getText().toString();

                if(TextUtils.isEmpty(content)){
                    editText_content.setError("cannot be empty");
                    return;
                }

                button.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);

                Map<String, String> params = new HashMap<>();
                params.put("from", from);
                params.put("to", to);
                params.put("subject", subject);
                params.put("content", content);
                String Sms = SmsWriter.writeSms(SmsConstants.SEND_EMAIL_REQUEST, params);
                SmsSender.sendMultipartSms(Sms);
            }
        });


        button_fillTestData = findViewById(R.id.button_fillTestData);

        button_fillTestData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_from.setText("fatih.iver@gmail.com");
                editText_to.setText("fatih.iver@boun.edu.tr");
                editText_subject.setText("Greeting!");
                editText_content.setText("Hello World!");
            }
        });
    }

    @Override
    protected void handleMessage(String phoneNumber, String message) {
        button.setEnabled(true);
        progressBar.setVisibility(View.GONE);
        IPMessage ipMessage = SmsReader.readSms(message);
        Toast.makeText(SendEmailActivity.this, ipMessage.getParams().get("result"), Toast.LENGTH_LONG).show();
    }
}
