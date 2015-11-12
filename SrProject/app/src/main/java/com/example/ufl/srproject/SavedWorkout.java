package com.example.ufl.srproject;

/**
 * Created by danie_000 on 11/11/2015.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SavedWorkout extends BaseActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_workout_menu);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String[] listArray = {"ok"};
        ArrayAdapter<String> arrayAddNewExercise;

        String savedWorkoutName = prefs.getString("inspectWorkout", "");
        String List = prefs.getString(savedWorkoutName, "");
        if(!List.equals("")) {
            listArray = List.split(";");
        }

        arrayAddNewExercise = new ArrayAdapter<String>(this, R.layout.item_list_view_swolodex_2, listArray);
        ListView displayExercises = (ListView)findViewById(R.id.exerciseList);
        displayExercises.setAdapter(arrayAddNewExercise);
        }
    }