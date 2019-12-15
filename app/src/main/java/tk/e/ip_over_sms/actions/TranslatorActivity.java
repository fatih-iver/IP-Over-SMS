package tk.e.ip_over_sms.actions;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tk.e.ip_over_sms.ListenerActivity;
import tk.e.ip_over_sms.R;
import tk.e.ip_over_sms.sms.IPMessage;
import tk.e.ip_over_sms.sms.SmsConstants;
import tk.e.ip_over_sms.sms.SmsReader;
import tk.e.ip_over_sms.sms.SmsSender;
import tk.e.ip_over_sms.sms.SmsWriter;

public class TranslatorActivity extends ListenerActivity {

    private Spinner spinner_from;
    private ImageView imageView_swap;
    private Spinner spinner_to;
    private EditText editText_translation;
    private Button button_translate;
    private TextView textView_translation;

    private List<String> languages;
    private List<String> languageCodes;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translator);

        spinner_from = findViewById(R.id.spinner_from);
        imageView_swap = findViewById(R.id.imageView_swap);
        spinner_to = findViewById(R.id.spinner_to);
        editText_translation = findViewById(R.id.editText_translation);
        button_translate = findViewById(R.id.button_translate);
        textView_translation = findViewById(R.id.textview_translation);

        progressBar = findViewById(R.id.progressBar);

        languages = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.google_translate_supported_languages)));

        languageCodes = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.google_translate_supported_language_codes)));


        ArrayAdapter<CharSequence> adapter_from = ArrayAdapter.createFromResource(this,
                R.array.google_translate_supported_languages, android.R.layout.simple_spinner_item);
        adapter_from.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_from.setAdapter(adapter_from);

        spinner_from.setSelection(19);

        ArrayAdapter<CharSequence> adapter_to = ArrayAdapter.createFromResource(this,
                R.array.google_translate_supported_languages, android.R.layout.simple_spinner_item);
        adapter_to.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_to.setAdapter(adapter_to);

        spinner_to.setSelection(93);

        imageView_swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedItemPosition_spinnerFrom = spinner_from.getSelectedItemPosition();
                int selectedItemPosition_spinnerTo = spinner_to.getSelectedItemPosition();

                spinner_from.setSelection(selectedItemPosition_spinnerTo);
                spinner_to.setSelection(selectedItemPosition_spinnerFrom);
            }
        });

        button_translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sourceText = editText_translation.getText().toString();

                if(TextUtils.isEmpty(sourceText)){
                    editText_translation.setError("cannot be empty!");
                    return;
                }


                button_translate.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);

                int selectedItemPosition_spinnerFrom = spinner_from.getSelectedItemPosition();
                int selectedItemPosition_spinnerTo = spinner_to.getSelectedItemPosition();

                Map<String, String> params = new HashMap<>();
                
                params.put("sl", languageCodes.get(selectedItemPosition_spinnerFrom));
                params.put("tl", languageCodes.get(selectedItemPosition_spinnerTo));
                params.put("q", sourceText);

                String message = SmsWriter.writeSms(SmsConstants.TEXT_TRANSLATE_REQUEST, params);
                SmsSender.sendMultipartSms(message);

                Toast.makeText(TranslatorActivity.this, "Requested!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void handleMessage(String phoneNumber, String message) {
        button_translate.setEnabled(true);
        progressBar.setVisibility(View.GONE);
        Toast.makeText(TranslatorActivity.this, message, Toast.LENGTH_SHORT).show();
        IPMessage ipMessage = SmsReader.readSms(message);

        if(ipMessage.getType() == SmsConstants.ERROR_OCCURED){
            Toast.makeText(TranslatorActivity.this, "An error occured!", Toast.LENGTH_SHORT).show();
        } else {
            textView_translation.setText(ipMessage.getParams().get("t"));
        }

    }
}
