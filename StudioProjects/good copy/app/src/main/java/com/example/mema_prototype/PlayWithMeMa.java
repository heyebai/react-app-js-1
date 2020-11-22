package com.example.mema_prototype;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PlayWithMeMa extends AppCompatActivity {

    private ArrayList<String> vocabularies = new ArrayList<>();
    private int questionIndex;
    private String answer;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_with_me_ma);

        backButton = findViewById(R.id.back_button_phrase);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PlayWithMeMa.this, HomePage.class));
            }
        });

        final TextView question = findViewById(R.id.question);

        Intent intent = getIntent();
        questionIndex = intent.getIntExtra("questionIndex", 0);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Language Learning").child("Greeting");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String temp = ds.getKey() + " " + ds.child("en").getValue();
                    vocabularies.add(temp);
                }

                // set question
                String[] questionAnswer = vocabularies.get(questionIndex).split(" ", 2);
                question.setText(questionAnswer[1]);
                answer = questionAnswer[0];

                List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
                List<Integer> indexes = new LinkedList<>();
                for (int i = 0; i < 6; i++) {
                    int randomIndex = questionIndex;
                    while (indexes.contains(randomIndex)) {
                        randomIndex = new Random().nextInt(vocabularies.size());
                    }
                    indexes.add(randomIndex);

                    String[] temp = vocabularies.get(randomIndex).split(" ");
                    Map<String, Object> showItem = new HashMap<String, Object>();
                    showItem.put("option", temp[0]);

                    listItem.add(showItem);
                }
                Collections.shuffle(listItem);


                //create a simpleAdapter
                SimpleAdapter myAdapter = new myAdapter(getApplicationContext(), listItem, R.layout.layout_option, new String[]{"option"}, new int[]{R.id.option});
                ListView listView = findViewById(R.id.options);
//                listView.setOnItemClickListener(itemClickListener);
                listView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

    }

    private class myAdapter extends SimpleAdapter {
        private int count = 0;

        public myAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            final TextView text = view.findViewById(R.id.vocab);
            // save the word to myCollection
            final Button btn = view.findViewById(R.id.option);
            btn.setTag(position);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (btn.getText().equals(answer)) {
                        btn.setBackgroundResource(R.drawable.correct_btn);
                        Toast.makeText(getApplicationContext(), "Bingo!",
                                Toast.LENGTH_SHORT).show();

                        if (questionIndex + 1 == vocabularies.size()) {
                            Intent intent = new Intent(PlayWithMeMa.this, HomePage.class);
                            startActivity(intent);
                        } else {
                            questionIndex++;
                            Intent intent = new Intent(PlayWithMeMa.this, PlayWithMeMa.class);
                            intent.putExtra("questionIndex", questionIndex);
                            startActivity(intent);
                        }


                    } else {
                        btn.setBackgroundResource(R.drawable.wrong_btn);
                        Toast.makeText(getApplicationContext(), "Please try again.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return view;
        }
    }
}