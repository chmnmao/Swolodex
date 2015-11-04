package com.example.ufl.srproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SavedWorkout extends ActionBarActivity {

    String[] listArray;
    ArrayAdapter<String> arrayAddNewExercise;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_workout_menu);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String List = prefs.getString("YOURKEY", "");
        if(!List.equals("")) {
            listArray = List.split(";");
        }

        arrayAddNewExercise = new ArrayAdapter<String>(this, R.layout.simple_list_item_custom, listArray);
        ListView displayExercises = (ListView)findViewById(R.id.exerciseList);
        displayExercises.setAdapter(arrayAddNewExercise);
    }
}
