package com.se17.notetrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateNoteActivity extends AppCompatActivity  {

    Context ctx = CreateNoteActivity.this;
    private EditText inputSubject, inputDescp;
    Button create;


    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        inputSubject = findViewById(R.id.subject);
        inputDescp = findViewById(R.id.description);
        create = findViewById(R.id.createnoteBtn);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SUBJECT = inputSubject.getText().toString();
                String DESCP = inputDescp.getText().toString();


                databaseHelper = new DatabaseHelper(ctx);
                sqLiteDatabase = databaseHelper.getWritableDatabase();
                databaseHelper.insertDataMethod(SUBJECT, DESCP, sqLiteDatabase);
                Toast.makeText(getApplicationContext(), "Note Created Successfully", Toast.LENGTH_LONG).show();
                databaseHelper.close();

                inputSubject.setText("");
                inputDescp.setText("");
                startActivity(new Intent(ctx, MainActivity.class));

            }
        });

    }
}