package com.example.mema_prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VocabularyList extends AppCompatActivity {

    private ArrayList<String> vocabularies = new ArrayList<>();
    private String ref, sentence, audioUrl;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_list);

        Intent intent = getIntent();
        ref = intent.getStringExtra("ref");
        dataRetrieve(ref);

        backButton = findViewById(R.id.back_button_phrase);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VocabularyList.this, LearnWithMeMa.class);
                startActivity(intent);
            }
        });


    }

    public void dataRetrieve(String ref) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Language Learning").child(ref);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    sentence = "_" + ds.child("eg").getValue();
                    audioUrl = "_" + ds.child("audio").getValue();
                    String temp = ds.getKey() + " " + ds.child("en").getValue() + sentence + audioUrl;
                    vocabularies.add(temp);
                }

                List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
                for (int i = 0; i < vocabularies.size(); i++) {
                    Map<String, Object> showItem = new HashMap<String, Object>();
                    String[] temp = vocabularies.get(i).split("_", 2);
//                    System.out.println(vocabularies.get(i));
                    showItem.put("vocab", temp[0]);
                    showItem.put("otherInfo", temp[1]);
                    listItem.add(showItem);
                }

                //create a simpleAdapter
                SimpleAdapter myAdapter = new myAdapter(getApplicationContext(), listItem, R.layout.layout_linear_item, new String[]{"vocab", "otherInfo"}, new int[]{R.id.vocab, R.id.invisible});
                ListView listView = (ListView) findViewById(R.id.listView);
                listView.setOnItemClickListener(itemClickListener);
                listView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }

    private class myAdapter extends SimpleAdapter{
        private int count = 0;

        public myAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            final TextView text = view.findViewById(R.id.vocab);
            final TextView invisible = view.findViewById(R.id.invisible);
            // save the word to myCollection
            final ImageButton imgBtn = view.findViewById(R.id.collection);
            imgBtn.setTag(position);
            imgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    count++;
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("myCollection");
                    String[] temp = text.getText().toString().split(" ", 2);
                    String[] otherInfo = invisible.getText().toString().split("_");
                    if (count % 2 == 1) {
                        imgBtn.setBackgroundResource(R.drawable.star);
                        // insert to firebase
                        myRef.child(temp[0]).child("en").setValue(temp[1]);
                        myRef.child(temp[0]).child("eg").setValue(otherInfo[0]);
                        myRef.child(temp[0]).child("audio").setValue(otherInfo[1]);
                    } else {
                        imgBtn.setBackgroundResource(R.drawable.star_grey);
                        // remove from firebase
                        myRef.child(temp[0]).removeValue();
                    }
                }
            });

            return view;
        }
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            TextView vocab = view.findViewById(R.id.vocab);
            TextView otherInfo = view.findViewById(R.id.invisible);
            String text = vocab.getText().toString();
            String[] temp = otherInfo.getText().toString().split("_");
//            String[] temp = text.split(" ", 2);

//            System.out.println(text);
            Intent intent = new Intent(VocabularyList.this, Vocabulary.class);
            intent.putExtra("text",text);
            intent.putExtra("ref", ref);
            intent.putExtra("sentence", temp[0]);

            startActivity(intent);
        }
    };


}