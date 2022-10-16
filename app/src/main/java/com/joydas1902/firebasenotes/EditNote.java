package com.joydas1902.firebasenotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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

public class EditNote extends AppCompatActivity {
    EditText meditNoteTitle, meditNoteContent;
    FloatingActionButton msaveEditedNote;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Toolbar toolbar = findViewById(R.id.toolbar_edit_note);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        meditNoteTitle = findViewById(R.id.editnote_title);
        meditNoteContent = findViewById(R.id.editnote_content);
        msaveEditedNote = findViewById(R.id.save_editednotebtn);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        meditNoteTitle.setText(intent.getStringExtra("title"));
        meditNoteContent.setText(intent.getStringExtra("content"));

        msaveEditedNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newTitle = meditNoteTitle.getText().toString();
                String newContent = meditNoteContent.getText().toString();
                if(newTitle.isEmpty() || newContent.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else {
                    DocumentReference documentReference = firebaseFirestore.collection("All Notes").document(firebaseUser.getUid())
                            .collection("Notes").document(intent.getStringExtra("noteId"));
                    Map<String, Object> note = new HashMap<>();
                    note.put("title", newTitle);
                    note.put("content", newContent);
                    documentReference.set(note).addOnSuccessListener(unused -> {
                        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditNote.this, NotesActivity.class));
                    }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to update", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){onBackPressed();}
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EditNote.this, NotesActivity.class));
        super.onBackPressed();
    }
}