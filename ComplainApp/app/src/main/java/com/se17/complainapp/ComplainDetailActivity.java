package com.se17.complainapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ComplainDetailActivity extends AppCompatActivity {

    TextView categoryTextView,descriptionTextView,severityTextView;
    ImageView complainImageView;

    private void initializeWidgets(){
        categoryTextView= findViewById(R.id.categoryTV);
        severityTextView= findViewById(R.id.severityTV);
        descriptionTextView= findViewById(R.id.descriptionTV);
        complainImageView= findViewById(R.id.complainImage);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_detail);

        initializeWidgets();

        //get all data from sqlite
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this, "COMPLAIN.sqlite", null, 5);

        Cursor cursor = sqLiteHelper.getData("SELECT * FROM COMPLAIN_RECORD WHERE ID = ?");
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String category = cursor.getString(1);
            String severity = cursor.getString(2);
            String description = cursor.getString(3);
            byte[] complainImage  = cursor.getBlob(4);

            categoryTextView.setText(category);
            severityTextView.setText(severity);
            descriptionTextView.setText(description);
        }

    }

}
