package com.example.ufl.srproject.db;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.ufl.srproject.R;

import java.util.ArrayList;
import java.util.Random;


public class AdditionalExercisePresetOptions extends ActionBarActivity {

    String[] exercisePicks;
    String[] sets;
    String[] repetitions;
    String[] workout;
    ArrayAdapter<String> arrayAddNewExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.additional_exercise_preset_options_menu);
    }

    public void randNumUpperBodyWorkout (View v) {

        // Pop up dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("How many additional exercises?");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        alert.setView(input);

        alert.setPositiveButton("Go!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                // Pull the exercise picks from SharedPreferences
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AdditionalExercisePresetOptions.this);
                String List = prefs.getString("Custom", "");
                // TODO Need to fix this, temporarily rigged to work with a saved list of 50 workouts
                String[] savedExercises = new String[50];
                if(!List.equals("")) {
                    savedExercises = List.split(";");
                }

                int numberOfExercises = Integer.parseInt(input.getText().toString());
                Random r = new Random();

                // Load up a 2D array with all exercise listings
                String[][] tempExerciseList = {
                        getResources().getStringArray(R.array.CHEST),
                        getResources().getStringArray(R.array.TRAPS),
                        getResources().getStringArray(R.array.SHOULDERS),
                        getResources().getStringArray(R.array.BICEPS),
                        getResources().getStringArray(R.array.TRAPS),
                        getResources().getStringArray(R.array.WRISTS),
                        getResources().getStringArray(R.array.UPPER_BACK),
                        getResources().getStringArray(R.array.LOWER_BACK),
                };

                // Turn this 2D array into a 1D array, there were complications with a 2D array
                java.util.List<String> exerciseList = new ArrayList<String>();
                for (int i = 0; i < tempExerciseList.length; i++){
                    for (int j = 0; j < tempExerciseList[i].length; j++){
                        exerciseList.add(tempExerciseList[i][j]);
                    }
                }

                // Initialization of these arrays
                int countTotalExercise = numberOfExercises;
                exercisePicks = new String[countTotalExercise];
                countTotalExercise--;

                while (countTotalExercise >= 0) {
                    // Choose  a random exercise
                    int randExerciseSelect = r.nextInt(exerciseList.size());

                    // Check if the exercise has already been selected and placed into the workout
                    for(int i = 0; i < exercisePicks.length; i++)
                    {
                        if(exercisePicks[i] == exerciseList.get(randExerciseSelect))
                        {
                            // If so, re-roll and restart the check
                            randExerciseSelect = r.nextInt(exerciseList.size());
                            i = -1;
                        }
                    }
                    // If it passes the check, put it into the workout
                    exercisePicks[countTotalExercise] = exerciseList.get(randExerciseSelect);

                    countTotalExercise--;
                }

                // Concatenate the saved exercises with the newly generated ones
                ArrayList<String>  finalExerciseList = new ArrayList<String>();
                for (int i = 0; i < savedExercises.length; i++){
                    finalExerciseList.add(savedExercises[i]);
                }
                for (int i = 0; i < exercisePicks.length; i++){
                    finalExerciseList.add(exercisePicks[i]);
                }

                // Generate the sets and reps for the entire list
                countTotalExercise = finalExerciseList.size();
                // Initialization of these arrays
                sets = new String[countTotalExercise];
                repetitions = new String[countTotalExercise];
                countTotalExercise--;

                while(countTotalExercise >= 0) {
                    // Choose a random number of sets
                    int randSetSelect = r.nextInt(4);
                    // Choose which array to pull the repetitions from
                    int randRepetitionArraySelect = r.nextInt(2);
                    // Needed a string array from values such as "10, 8, 6"
                    int randStringRepetitionSelect = r.nextInt(4);
                    // Needed an int array for values such as "10" and "6", kill me
                    int randIntRepetitionSelect = r.nextInt(2);

                    // Cast the int to a string and set the sets to that number... nice wording
                    sets[countTotalExercise] = Integer.toString(getResources().getIntArray(R.array.SETS)[randSetSelect]);
                    // If the roll has chosen the string array of repetition values and there are 3 sets, do this
                    if (randRepetitionArraySelect == 0 && sets[countTotalExercise].equals("3")) {
                        repetitions[countTotalExercise] = getResources().getStringArray(R.array.stringRepetitions)[randStringRepetitionSelect];
                    } else {
                        repetitions[countTotalExercise] = Integer.toString(getResources().getIntArray(R.array.intRepetitions)[randIntRepetitionSelect]);
                    }

                    countTotalExercise--;
                }

                // Go to the correct view for displaying this information
                setContentView(R.layout.rand_workout_menu);

                // Assemble the workout array
                workout = new String[finalExerciseList.size()];
                for (int i = 0; i < workout.length; i++) {
                    workout[i] = finalExerciseList.get(i) + "\n" + "\t\t\tSets:\t" + sets[i] + "\n" + "\t\t\tRepetitions:\t" + repetitions[i] + "\n";
                }

                // Print the sucker to screen
                arrayAddNewExercise = new ArrayAdapter<String>(AdditionalExercisePresetOptions.this, R.layout.simple_list_item_custom, workout);
                ListView displayExercises = (ListView) findViewById(R.id.exerciseList);
                displayExercises.setAdapter(arrayAddNewExercise);

                // initialize the save button and its listener
                Button saveButton = (Button) findViewById(R.id.saveRandWorkout);
                saveButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        saveWorkout(workout);
                    }
                });
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        AlertDialog dialog = alert.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.show();
    }

    public void randNumLowerBodyWorkout (View v) {

        // Pop up dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("How many additional exercises?");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        alert.setView(input);

        alert.setPositiveButton("Go!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                // Pull the exercise picks from SharedPreferences
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AdditionalExercisePresetOptions.this);
                String List = prefs.getString("Custom", "");
                // TODO Need to fix this, temporarily rigged to work with a saved list of 50 workouts
                String[] savedExercises = new String[50];
                if(!List.equals("")) {
                    savedExercises = List.split(";");
                }

                int numberOfExercises = Integer.parseInt(input.getText().toString());
                Random r = new Random();

                // Load up a 2D array with all exercise listings
                String[][] tempExerciseList = {
                        getResources().getStringArray(R.array.LOWER_BODY),
                        getResources().getStringArray(R.array.CALVES),
                        getResources().getStringArray(R.array.QUADS),
                        getResources().getStringArray(R.array.GLUTES),
                        getResources().getStringArray(R.array.HAMSTRINGS),
                };

                // Turn this 2D array into a 1D array, there were complications with a 2D array
                java.util.List<String> exerciseList = new ArrayList<String>();
                for (int i = 0; i < tempExerciseList.length; i++){
                    for (int j = 0; j < tempExerciseList[i].length; j++){
                        exerciseList.add(tempExerciseList[i][j]);
                    }
                }

                // Initialization of these arrays
                int countTotalExercise = numberOfExercises;
                exercisePicks = new String[countTotalExercise];
                countTotalExercise--;

                while (countTotalExercise >= 0) {
                    // Choose  a random exercise
                    int randExerciseSelect = r.nextInt(exerciseList.size());

                    // Check if the exercise has already been selected and placed into the workout
                    for(int i = 0; i < exercisePicks.length; i++)
                    {
                        if(exercisePicks[i] == exerciseList.get(randExerciseSelect))
                        {
                            // If so, re-roll and restart the check
                            randExerciseSelect = r.nextInt(exerciseList.size());
                            i = -1;
                        }
                    }
                    // If it passes the check, put it into the workout
                    exercisePicks[countTotalExercise] = exerciseList.get(randExerciseSelect);

                    countTotalExercise--;
                }

                // Concatenate the saved exercises with the newly generated ones
                ArrayList<String>  finalExerciseList = new ArrayList<String>();
                for (int i = 0; i < savedExercises.length; i++){
                    finalExerciseList.add(savedExercises[i]);
                }
                for (int i = 0; i < exercisePicks.length; i++){
                    finalExerciseList.add(exercisePicks[i]);
                }

                // Generate the sets and reps for the entire list
                countTotalExercise = finalExerciseList.size();
                // Initialization of these arrays
                sets = new String[countTotalExercise];
                repetitions = new String[countTotalExercise];
                countTotalExercise--;

                while(countTotalExercise >= 0) {
                    // Choose a random number of sets
                    int randSetSelect = r.nextInt(4);
                    // Choose which array to pull the repetitions from
                    int randRepetitionArraySelect = r.nextInt(2);
                    // Needed a string array from values such as "10, 8, 6"
                    int randStringRepetitionSelect = r.nextInt(4);
                    // Needed an int array for values such as "10" and "6", kill me
                    int randIntRepetitionSelect = r.nextInt(2);

                    // Cast the int to a string and set the sets to that number... nice wording
                    sets[countTotalExercise] = Integer.toString(getResources().getIntArray(R.array.SETS)[randSetSelect]);
                    // If the roll has chosen the string array of repetition values and there are 3 sets, do this
                    if (randRepetitionArraySelect == 0 && sets[countTotalExercise].equals("3")) {
                        repetitions[countTotalExercise] = getResources().getStringArray(R.array.stringRepetitions)[randStringRepetitionSelect];
                    } else {
                        repetitions[countTotalExercise] = Integer.toString(getResources().getIntArray(R.array.intRepetitions)[randIntRepetitionSelect]);
                    }

                    countTotalExercise--;
                }

                // Go to the correct view for displaying this information
                setContentView(R.layout.rand_workout_menu);

                // Assemble the workout array
                workout = new String[finalExerciseList.size()];
                for (int i = 0; i < workout.length; i++) {
                    workout[i] = finalExerciseList.get(i) + "\n" + "\t\t\tSets:\t" + sets[i] + "\n" + "\t\t\tRepetitions:\t" + repetitions[i] + "\n";
                }

                // Print the sucker to screen
                arrayAddNewExercise = new ArrayAdapter<String>(AdditionalExercisePresetOptions.this, R.layout.simple_list_item_custom, workout);
                ListView displayExercises = (ListView) findViewById(R.id.exerciseList);
                displayExercises.setAdapter(arrayAddNewExercise);

                // initialize the save button and its listener
                Button saveButton = (Button) findViewById(R.id.saveRandWorkout);
                saveButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        saveWorkout(workout);
                    }
                });
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        AlertDialog dialog = alert.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.show();
    }

    public void saveWorkout(String[] saveWorkout) {
        String List = "";

        for(int i = 0; i < saveWorkout.length; i++)
        {
            List += saveWorkout[i];
            List += ";";
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("YOURKEY", List);
        prefsEditor.commit();
    }
}
