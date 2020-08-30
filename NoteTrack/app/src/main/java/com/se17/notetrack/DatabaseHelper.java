package com.se17.notetrack;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String SUBJECT = "subject";
    public static final String DESCRIPTION = "description";

    public static final String TABLE_NAME = "noteTable";

    public static final String DATABASE_NAME = "noteTrackDB";
    public static final int DATABASE_VERSION = 3;

    public static final String QUERY_FOR_CREATE_TABLE =
            "create table "+TABLE_NAME+"("+SUBJECT+" text,"+DESCRIPTION+" text);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("SQLite", "Database created");

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUERY_FOR_CREATE_TABLE);
        Log.d("SQLite", "Table created");
    }

    // for insert data
    public void insertDataMethod(String subject, String description, SQLiteDatabase sqLiteDatabase){
        ContentValues contentValues = new ContentValues();
        contentValues.put(SUBJECT,subject);
        contentValues.put(DESCRIPTION,description);
        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        Log.d("SQLite", "Data inserted");

    }

    // for view data
    public Cursor getAllData(SQLiteDatabase sqLiteDatabase){
        Cursor cursor;
        String columns[] = {SUBJECT,DESCRIPTION};
        cursor = sqLiteDatabase.query(TABLE_NAME,columns,null,null,null,null,null);
        return cursor;
    }

    // update data
    public void updateNoteDetail(String subject, String description, SQLiteDatabase sqLiteDatabase){
        ContentValues contentValues = new ContentValues();
        contentValues.put(SUBJECT,subject);
        contentValues.put(DESCRIPTION,description);

        String updateQry = SUBJECT + " LIKE ?";
        String string[] = {subject};
        sqLiteDatabase.update(TABLE_NAME,contentValues,updateQry,string);

    }

    // delete data
    public void deleteNote(String name, SQLiteDatabase sqLiteDatabase){
        String deleteQry = SUBJECT + " LIKE ?";
        String string[] = {name};
        sqLiteDatabase.delete(TABLE_NAME,deleteQry,string);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
