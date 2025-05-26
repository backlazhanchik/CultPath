package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private TextView textView;
    private final String text = "CultPath";
    private final long delayMillis = 100L;
    private final long splashDelayMillis = 1000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textView = findViewById(R.id.splashTextView);

        animateText(text);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, WelcomProgramms.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }, splashDelayMillis + text.length() * delayMillis);
    }

    private void animateText(final String text) {
        final int[] index = {0};
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (index[0] < text.length()) {
                    textView.setText(textView.getText().toString() + text.charAt(index[0]));
                    index[0]++;
                    handler.postDelayed(this, delayMillis);
                }
            }
        });
    }
}