package com.se17.notetrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Context ctx = MainActivity.this;
    private ListView listView;
    private androidx.appcompat.view.ActionMode mActionmode;

    MyAdapter myAdapter;
    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    List<Note> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_view);
        myAdapter = new MyAdapter(ctx, R.layout.note_item);
        listView.setAdapter(myAdapter);

        list = new ArrayList<>();

        databaseHelper = new DatabaseHelper(ctx);
        sqLiteDatabase = databaseHelper.getReadableDatabase();
        cursor =  databaseHelper.getAllData(sqLiteDatabase);

        if(cursor.moveToFirst()){
            do{
                String subject, description;

                subject = cursor.getString(0);
                description = cursor.getString(1);

                Note note = new Note(subject,description);
                myAdapter.add(note);
                list.add(note);
            } while (cursor.moveToNext());
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Note note = list.get(position);

                Intent intent = new Intent(ctx, ViewNoteActivity.class);

                intent.putExtra("SUBJECT", note.getSubject());
                intent.putExtra("DESCRIPTION", note.getDescription());

                startActivity(intent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (mActionmode != null){
                    return false;
                }

                mActionmode = startSupportActionMode(mActionModelCallback);

                return true;
            }
        });

    }

    private androidx.appcompat.view.ActionMode.Callback mActionModelCallback = new androidx.appcompat.view.ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(androidx.appcompat.view.ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.action_menu,menu);
            mode.setTitle("Select");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(androidx.appcompat.view.ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(androidx.appcompat.view.ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.update_note:
                    startActivity(new Intent(ctx,UpdateNoteActivity.class));
                    mode.finish();
                    return true;

                case R.id.delete_note:
                    //sqLiteDatabase = databaseHelper.getReadableDatabase();
                    //databaseHelper.deleteNote(DatabaseHelper.SUBJECT,sqLiteDatabase);
                    Toast.makeText(ctx, "Note Deleted", Toast.LENGTH_LONG).show();
                    mode.finish();
                    return true;

                case R.id.share_note:
                    Toast.makeText(ctx, "Shared", Toast.LENGTH_LONG).show();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(androidx.appcompat.view.ActionMode mode) {
            mActionmode = null;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.create_note:
                startActivity(new Intent(ctx,CreateNoteActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
