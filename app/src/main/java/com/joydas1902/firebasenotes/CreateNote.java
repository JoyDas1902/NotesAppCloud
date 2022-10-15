package com.joydas1902.firebasenotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateNote extends AppCompatActivity {
    private EditText mcreateNoteTitle, mcreateNoteContent;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        mcreateNoteTitle = findViewById(R.id.createnote_title);
        mcreateNoteContent = findViewById(R.id.createnote_content);
        FloatingActionButton msaveNoteBtn = findViewById(R.id.savenotebtn);

        Toolbar toolbar = findViewById(R.id.toolbar_create_note);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        msaveNoteBtn.setOnClickListener(view -> {
            String title = mcreateNoteTitle.getText().toString();
            String content = mcreateNoteContent.getText().toString();
            if(title.isEmpty() || content.isEmpty()){
                Toast.makeText(getApplicationContext(), "Both fields are required", Toast.LENGTH_SHORT).show();
            }
            else{
                // store the data on cloud firestore
                DocumentReference documentReference = firebaseFirestore.collection("notes")
                        .document(firebaseUser.getUid()).collection("myNotes").document();
                Map<String, Object> note = new HashMap<>();
                note.put("title", title);
                note.put("content", content);
                documentReference.set(note).addOnSuccessListener(unused -> {
                    Toast.makeText(getApplicationContext(), "Note created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateNote.this, NotesActivity.class));
                }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to create note, check internet", Toast.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CreateNote.this, NotesActivity.class));
        super.onBackPressed();
    }
}