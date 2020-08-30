package com.se17.notetrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ViewNoteActivity extends AppCompatActivity {

    TextView subject, descp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        subject = findViewById(R.id.subject_shown);
        descp = findViewById(R.id.description_shown);

        Intent intent = getIntent();

        String SUBJECT =  intent.getStringExtra("SUBJECT");
        subject.setText(SUBJECT);

        String DESCRIPTION =  intent.getStringExtra("DESCRIPTION");
        descp.setText(DESCRIPTION);


    }
}
