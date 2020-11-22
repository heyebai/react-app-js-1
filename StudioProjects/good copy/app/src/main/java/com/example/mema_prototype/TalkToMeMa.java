package com.example.mema_prototype;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

public class TalkToMeMa extends AppCompatActivity {

    static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    TextView resultText;
    String originalText;
    String translatedText;
    boolean connected;
    Translate translate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_to_me_ma);
        resultText = findViewById(R.id.resultText);
        ImageButton customButton = findViewById(R.id.MeMaButton);
        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInternetConnection()) {
                    speak();
                }
            }
        });
        ImageButton anotherButton = findViewById(R.id.talkButton);
        anotherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInternetConnection()) {
                    speak();
                }
            }
        });
        ImageButton backButton = findViewById(R.id.backbtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TalkToMeMa.this, HomePage.class));
            }
        });
    }

    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, I am MeMa");
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    checkInternetConnection();
                    getTranslateService();
                    assert result != null;
                    translate(result.get(0));
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getTranslateService() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try (InputStream is = getResources().openRawResource(R.raw.credentials)) {
            // Get credentials.
            final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);
            // Set credentials and get translate service.
            TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(myCredentials).build();
            translate = translateOptions.getService();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void translate(String msg) {
        // Get input text to be translated.
        originalText = msg;
        Translation translation = translate.translate(originalText,
                Translate.TranslateOption.targetLanguage("zh"),
                Translate.TranslateOption.model("base"));
        translatedText = translation.getTranslatedText();
        // Update text.
        resultText.setText(translatedText);
    }

    public boolean checkInternetConnection() {
        // Check internet connection.
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Means that we are connected to a network (mobile or wi-fi).
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
        return connected;
    }
}