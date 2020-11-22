package com.example.mema_prototype;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCollection extends AppCompatActivity {
    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            TextView vocab = view.findViewById(R.id.vocab);
            TextView otherInfo = view.findViewById(R.id.invisible);
            String text = vocab.getText().toString();
            String[] temp = otherInfo.getText().toString().split("_");
            Intent intent = new Intent(MyCollection.this, Vocabulary.class);
            intent.putExtra("text", text);
            intent.putExtra("collection", true);
            intent.putExtra("sentence", temp[0]);
            startActivity(intent);
        }
    };
    private ArrayList<String> vocabularies = new ArrayList<>();
    private String sentence, audioUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCollection.this, HomePage.class);
                startActivity(intent);
            }
        });
        dataRetrieve();
    }
    public void dataRetrieve() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("myCollection");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again whenever data at this location is updated.
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    sentence = "_" + ds.child("eg").getValue();
                    audioUrl = "_" + ds.child("audio").getValue();
                    String temp = ds.getKey() + " " + ds.child("en").getValue();
                    vocabularies.add(temp);
                }
                List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
                for (int i = 0; i < vocabularies.size(); i++) {
                    Map<String, Object> showItem = new HashMap<String, Object>();
                    showItem.put("vocab", vocabularies.get(i));

                    listItem.add(showItem);
                }
                // Create a simpleAdapter.
                SimpleAdapter myAdapter = new myAdapter(getApplicationContext(), listItem, R.layout.layout_linear_item, new String[]{"vocab"}, new int[]{R.id.vocab});
                ListView listView = findViewById(R.id.listView);
                listView.setOnItemClickListener(itemClickListener);
                listView.setAdapter(myAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
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
            // Save the word to myCollection.
            final ImageButton imgBtn = view.findViewById(R.id.collection);
            imgBtn.setBackgroundResource(R.drawable.star);
            imgBtn.setTag(position);
            imgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    count++;
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("myCollection");
                    String[] temp = text.getText().toString().split(" ", 2);
                    if (count % 2 == 1) {
                        imgBtn.setBackgroundResource(R.drawable.star_grey);
                        // Remove from firebase.
                        startActivity(new Intent(MyCollection.this, MyCollection.class));
                        myRef.child(temp[0]).removeValue();
                    } else {
                        imgBtn.setBackgroundResource(R.drawable.star);
                    }
                }
            });
            return view;
        }
    }
}