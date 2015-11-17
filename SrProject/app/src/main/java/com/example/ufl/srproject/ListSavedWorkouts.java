package com.example.ufl.srproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListSavedWorkouts extends BaseActivity {

    String[] listArray;
    ArrayAdapter<String> arrayAddNewExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_workout_menu);

        final Context temp = this;
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final List<String> workoutDisplayList = new ArrayList<String>();
        final List<String> workoutRetrieveList = new ArrayList<String>();

        // Gets all the keys and determines which ones are saved workouts from the user
        Map<String, ?> allEntries = prefs.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().contains("USER")) {
                // add these saved workouts to a list and snip the USER header for display purposes
                workoutDisplayList.add(" •    " + entry.getKey().substring(4));
                workoutRetrieveList.add(entry.getKey());
            }
        }

        // Print the saved workouts to screen
        final ArrayAdapter<String> savedWorkouts = new ArrayAdapter<String>(this, R.layout.item_list_view_swolodex_1, workoutDisplayList);
        final ListView displayExercises = (ListView)findViewById(R.id.exerciseList);
        displayExercises.setAdapter(savedWorkouts);
        displayExercises.setLongClickable(true);

        displayExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View view, int position, long id) {
                String workoutName = workoutRetrieveList.get(position);

                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.putString("inspectWorkout", workoutName);
                prefsEditor.commit();

                Intent intent = new Intent(getApplicationContext(), SavedWorkout.class);
                startActivity(intent);
            }
        });

        displayExercises.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int pos, long id) {
                // Pop up dialog
                AlertDialog.Builder alert = new AlertDialog.Builder(temp);
                alert.setTitle("Are you sure you wish to delete this workout?");

                // When user hits "Yes"...
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // removes the selected workout from sharedprefs
                        SharedPreferences.Editor prefsEditor = prefs.edit();
                        prefsEditor.remove(workoutRetrieveList.get(pos));
                        prefsEditor.apply();

                        // removes the selected workout from view
                        savedWorkouts.remove(workoutDisplayList.get(pos));
                        displayExercises.setAdapter(savedWorkouts);

                        // remove from/update the retrieve list
                        workoutRetrieveList.remove(pos);

                        // Gets all the keys and determines which ones are saved workouts from the user
                        Map<String, ?> allEntries = prefs.getAll();

                        Toast.makeText(temp, "Workout removed", Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                        Toast.makeText(temp, "Workout not removed", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = alert.create();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                dialog.show();

                return true;
            }
        });
    }

    //region Menu creation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save, menu);
        //getMenuInflater().inflate(R.menu.menu_saved, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id==R.id.action_remove_workout){

        }
        if (id == R.id.action_settings) {
            return true;
        }
        if(id==R.id.action_login){
            //Begin login intent
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
