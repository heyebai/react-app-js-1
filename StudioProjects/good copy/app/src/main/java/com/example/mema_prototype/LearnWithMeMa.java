package com.example.mema_prototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class LearnWithMeMa extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_with_me_ma);
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LearnWithMeMa.this, HomePage.class);
                startActivity(intent);
            }
        });
    }

    public void showList(View view) {
        Button btn_greeting = findViewById(R.id.greeting);
        btn_greeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LearnWithMeMa.this, VocabularyList.class);
                intent.putExtra("ref", "Greeting");
                startActivity(intent);
            }
        });
        Button btn_food = findViewById(R.id.food);
        btn_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LearnWithMeMa.this, VocabularyList.class);
                intent.putExtra("ref", "Food");
                startActivity(intent);
            }
        });
        Button btn_living = findViewById(R.id.living);
        btn_living.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LearnWithMeMa.this, VocabularyList.class);
                intent.putExtra("ref", "Living");
                startActivity(intent);
            }
        });
        Button btn_travel = findViewById(R.id.travel);
        btn_travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LearnWithMeMa.this, VocabularyList.class);
                intent.putExtra("ref", "Travel");
                startActivity(intent);
            }
        });
        Button btn_emergency = findViewById(R.id.emergency);
        btn_emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LearnWithMeMa.this, VocabularyList.class);
                intent.putExtra("ref", "Emergency");
                startActivity(intent);
            }
        });
    }
}