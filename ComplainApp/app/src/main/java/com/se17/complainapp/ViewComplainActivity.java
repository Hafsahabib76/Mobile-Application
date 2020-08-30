package com.se17.complainapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class ViewComplainActivity extends AppCompatActivity{


    ListView mListView;
    ArrayList<Complain> mList;
    ViewComplainAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_complain);

        mListView = findViewById(R.id.listView);
        mList = new ArrayList<>();
        mAdapter = new ViewComplainAdapter(this, R.layout.row, mList);
        mListView.setAdapter(mAdapter);

        //get all data from sqlite
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this, "COMPLAIN.sqlite", null, 5);

        Cursor cursor = sqLiteHelper.getData("SELECT * FROM COMPLAIN_RECORD");
        mList.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String category = cursor.getString(1);
            String severity = cursor.getString(2);
            String description = cursor.getString(3);
            byte[] complainImage  = cursor.getBlob(4);

            //add to list
            mList.add(new Complain(id, category, severity, description, complainImage));
        }
        mAdapter.notifyDataSetChanged();
        if (mList.size()==0){

            //if there is no record in table of database which means listview is empty
            Toast.makeText(this, "No Complain Found!", Toast.LENGTH_SHORT).show();
        }


    }

    
}
