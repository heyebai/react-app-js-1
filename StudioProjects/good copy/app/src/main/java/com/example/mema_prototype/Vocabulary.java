package com.example.mema_prototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.firebase.storage.FirebaseStorage;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.EmailAuthProvider;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vocabulary extends AppCompatActivity {
    private ImageButton mema, backButton;
    private Button gotIt;
    private TextView zh, en, sentence;
    private String audioUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);

        mema = findViewById(R.id.mema);
        gotIt = findViewById(R.id.gotIt);
        zh = findViewById(R.id.zh);
        en = findViewById(R.id.en);
        sentence = findViewById(R.id.sentence);

        Intent intent = getIntent();
        final String text = intent.getStringExtra("text");
        final String ref = intent.getStringExtra("ref");
        String eg = intent.getStringExtra("sentence");
        boolean collection = intent.getBooleanExtra("collection", false);

        final String[] temp = text.split(" ", 2);
        zh.setText(temp[0]);
        en.setText(temp[1]);
        sentence.setText(eg);

        backButton = findViewById(R.id.back_button_phrase);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Vocabulary.this, VocabularyList.class);
                intent.putExtra("ref",ref);
                startActivity(intent);
            }
        });

        gotIt = findViewById(R.id.gotIt);
        gotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Vocabulary.this, VocabularyList.class);
                intent.putExtra("ref",ref);
                startActivity(intent);
            }
        });

// ...Initialize Firebase Auth
//        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        mAuth.signInAnonymously()
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            FirebaseUser user = mAuth.getCurrentUser();
//                        } else {
//                            // If sign in fails, display a message to the user.
//                        }
//                        // ...
//                    }
//                });
//        AuthCredential credential = EmailAuthProvider.getCredential("1", "1");
//        // [END auth_email_cred]
//        mAuth.getCurrentUser().linkWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            FirebaseUser user = task.getResult().getUser();
//                        } else {
//                        }
//                    }
//                });


//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference myRef = storage.getReferenceFromUrl("gs://mema-ac1a6.appspot.com");
//        final StorageReference path = myRef.child("早上好.m4a");
//        System.out.println(path.getDownloadUrl().toString());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef;
        if (collection) {
            myRef = database.getReference("myCollection");

        } else {
            myRef = database.getReference("Language Learning").child(ref);
        }

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                audioUrl = "" + dataSnapshot.child(temp[0]).child("audio").getValue();
//                System.out.println(audioUrl);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

        mema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayer mp = new MediaPlayer();
                try {
                    mp.setDataSource(audioUrl);
                    mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mediaPlayer.start();
                        }
                    });

                    mp.prepare();
                } catch (IOException e) {
                    System.out.println("nothing here");
                }
            }
        });

    }


}