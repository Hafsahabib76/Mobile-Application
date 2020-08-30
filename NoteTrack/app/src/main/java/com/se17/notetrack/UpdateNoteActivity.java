package com.se17.notetrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateNoteActivity extends AppCompatActivity {

    Context ctx = UpdateNoteActivity.this;
    EditText uSubject, uDescp;
    Button updateButton;

    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    String SUBJECT;

    String getSubject, getDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);

        uSubject = findViewById(R.id.subject_update);
        uDescp = findViewById(R.id.description_update);

        updateButton = findViewById(R.id.updatenoteBtn);

        databaseHelper = new DatabaseHelper(ctx);
        sqLiteDatabase = databaseHelper.getReadableDatabase();
        cursor = databaseHelper.getAllData(sqLiteDatabase);

        if(cursor.moveToFirst()){
            do{

                String subject, descp;
                subject = cursor.getString(0);
                descp = cursor.getString(1);

                getSubject = subject;
                getDescription = descp;

                uSubject.setText(getSubject);
                uDescp.setText(getDescription);

            } while (cursor.moveToNext());
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject, description;

                subject = uSubject.getText().toString();
                description = uDescp.getText().toString();

                databaseHelper = new DatabaseHelper(ctx);
                sqLiteDatabase = databaseHelper.getWritableDatabase();
                databaseHelper.updateNoteDetail(subject,description,sqLiteDatabase);

                Toast.makeText(getApplicationContext(),"Note updated", Toast.LENGTH_LONG).show();
                finish();
                Intent intent = new Intent(ctx, MainActivity.class);
                startActivity(intent);

                uSubject.setText("");
                uDescp.setText("");
            }
        });
    }
}
