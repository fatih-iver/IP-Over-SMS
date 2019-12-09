package tk.e.ip_over_sms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

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
                Toast.makeText(MainActivity.this, "Exchange Rates", Toast.LENGTH_SHORT).show();
            }
        });

        button_randomQuote = findViewById(R.id.button_randomQuote);

        button_randomQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Random Quote", Toast.LENGTH_SHORT).show();
            }
        });

        button_sendEmail = findViewById(R.id.button_sendEmail);

        button_sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Send Email", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
