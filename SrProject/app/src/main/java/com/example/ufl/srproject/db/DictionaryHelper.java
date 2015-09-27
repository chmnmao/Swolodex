package com.example.ufl.srproject.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kevin on 9/22/2015.
 * For NOW, unsure if we are going to use this database version of implementation
 */
//TODO: Decide if we need this or if current implementation is fine
public class DictionaryHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME="workoutDB";
    //Column names
    private static final String KEY_ID = "id";//Column 0
    private static final String KEY_NAME="exerciseName";//Column 1
    private static final String KEY_BODY_AREA="bodyArea";//Column 2
    private static final String KEY_REPS="reps";//Column 3
    private static final String KEY_SETS="sets";//Column 4
    //Creating DB
    private static final String TABLE_CREATE = "CREATE TABLE "+DATABASE_NAME+" ("+
            KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
            + KEY_BODY_AREA + " TEXT," + KEY_REPS + " TEXT," + KEY_SETS+" TEXT"+")";

    public DictionaryHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(TABLE_CREATE);
        //Populate table
    }
    //Upgrading DB
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);

        // Create tables again
        onCreate(db);
    }

    //add an exercise to DB
    public void addExercise(DictionaryEntry exercise){
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, exercise.getExerciseName());
        values.put(KEY_BODY_AREA, exercise.getBodyArea());
        values.put(KEY_REPS, exercise.getReps());
        values.put(KEY_SETS, exercise.getSets());

        SQLiteDatabase db=this.getWritableDatabase();

        db.insert(DATABASE_NAME, null, values);
        db.close();
    }
    //Add exercise by body area

    public DictionaryEntry findExercise(String name){
        String query = "Select * FROM "+DATABASE_NAME+" WHERE"+KEY_NAME+" = \""+name+"\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        DictionaryEntry exercise = new DictionaryEntry();
        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            exercise.setId(Integer.parseInt(cursor.getString(0)));
            exercise.setExerciseName(cursor.getString(1));
            exercise.setBodyArea(cursor.getString(2));
            exercise.setReps(cursor.getString(3));
            exercise.setSets(cursor.getString(4));
            cursor.close();
        }
        else{ exercise = null; }
        db.close();
        return exercise;
    }

    public boolean deleteExercise(String name){
        boolean result = false;
        String query = "Select * FROM "+DATABASE_NAME+" WHERE"+KEY_NAME+"= \""+name+"\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        DictionaryEntry exercise = new DictionaryEntry();
        if(cursor.moveToFirst()){
            exercise.setId(Integer.parseInt(cursor.getString(0)));
            db.delete(DATABASE_NAME, KEY_ID + " = ?",
                    new String[]{String.valueOf(exercise.getId())});
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
}
