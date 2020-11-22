package com.example.mema_prototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Set up a list buttons.
        ImageButton word_of_day_btn = findViewById(R.id.phrase_of_the_day_btn);
        word_of_day_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, PhraseOfTheDay.class));
            }
        });

        ImageButton learn_with_mema_btn = findViewById(R.id.learn_with_mema_btn);
        learn_with_mema_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, LearnWithMeMa.class));
            }
        });

        ImageButton my_collection_btn = findViewById(R.id.my_collection_btn);
        my_collection_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, MyCollection.class));
            }
        });

        ImageButton talk_to_mema_btn = findViewById(R.id.talk_to_mema_btn);
        talk_to_mema_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, TalkToMeMa.class));
            }
        });

        ImageButton play_with_mema_btn = findViewById(R.id.play_with_mema_btn);
        play_with_mema_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, PlayWithMeMa.class));
            }
        });
    }
}