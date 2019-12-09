package tk.e.ip_over_sms;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import tk.e.ip_over_sms.sms.SmsHelper;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;

    private static String PHONE_NUMBER = "905076319260";
    private static String EXCHANGE_RATES = "Exchange Rates";
    private static String RANDOM_QUOTE = "Random Quote";
    private static String SEND_EMAIL = "Send Email";


    private Button button_exchangeRates;
    private Button button_randomQuote;
    private Button button_sendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_exchangeRates = findViewById(R.id.button_exchangeRates);

        button_exchangeRates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, EXCHANGE_RATES, Toast.LENGTH_SHORT).show();
                SmsHelper.sendSms(PHONE_NUMBER, EXCHANGE_RATES);
            }
        });

        button_randomQuote = findViewById(R.id.button_randomQuote);

        button_randomQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, RANDOM_QUOTE, Toast.LENGTH_SHORT).show();
                SmsHelper.sendSms(PHONE_NUMBER, RANDOM_QUOTE);

            }
        });

        button_sendEmail = findViewById(R.id.button_sendEmail);

        button_sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, SEND_EMAIL, Toast.LENGTH_SHORT).show();
                SmsHelper.sendSms(PHONE_NUMBER, SEND_EMAIL);
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

}
