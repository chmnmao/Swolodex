package com.example.ufl.srproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Random;

public class CustomWorkoutOptions extends BaseActivity {

    String[] exercisePicks;
    String[] sets;
    String[] repetitions;
    String[] workout;
    ArrayAdapter<String> arrayAddNewExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_workout_options);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("Custom", "");
        prefsEditor.commit();
    }
    public void randWorkout (View v) {

        Random r = new Random();

        // Pull the exercise picks from SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String List = prefs.getString("Custom", "");
        if(!List.equals("")) {
            exercisePicks = List.split(";");
        }

        // Initialization of these arrays
        int countTotalExercise = exercisePicks.length;
        sets = new String[countTotalExercise];
        repetitions = new String[countTotalExercise];
        countTotalExercise--;

        while (countTotalExercise >= 0) {
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
            }
            else {
                repetitions[countTotalExercise] = Integer.toString(getResources().getIntArray(R.array.intRepetitions)[randIntRepetitionSelect]);
            }

            countTotalExercise--;
        }

        // Go to the correct view for displaying this information
        setContentView(R.layout.rand_workout_menu);

        // Assemble the workout array
        workout = new String[exercisePicks.length];
        for(int i = 0; i < workout.length; i++)
        {
            workout[i] = exercisePicks[i] + "\n" + "\t\t\tSets:\t" + sets[i] + "\n" + "\t\t\tRepetitions:\t" + repetitions[i] + "\n";
        }

        // Print the sucker to screen
        arrayAddNewExercise = new ArrayAdapter<String>(this, R.layout.item_list_view_swolodex_2, workout);
        ListView displayExercises = (ListView)findViewById(R.id.exerciseList);
        displayExercises.setAdapter(arrayAddNewExercise);

        // initialize the save button and its listener
        Button saveButton = (Button)findViewById(R.id.saveRandWorkout);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveWorkout(workout);
            }
        });
    }

    public void additionalExercises(View v) {
        Intent intent = new Intent(this, AdditionalExerciseOptions.class);
        startActivity(intent);
    }

    public void randNumWorkout (View v) {

        // Pop up dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("How many exercises?");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        alert.setView(input);

        alert.setPositiveButton("Go!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                // Pull the exercise picks from SharedPreferences
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(CustomWorkoutOptions.this);
                String List = prefs.getString("Custom", "");
                if(!List.equals("")) {
                    exercisePicks = List.split(";");
                }

                int numberOfExercises = Integer.parseInt(input.getText().toString());

                if(numberOfExercises > exercisePicks.length) {
                    Toast.makeText(CustomWorkoutOptions.this,"Can't randomize this many exercises: List is to small", Toast.LENGTH_SHORT).show();
                }

                else {
                    Random r = new Random();

                    // Initialization of these arrays
                    int countTotalExercise = numberOfExercises;
                    String [] tempExercises = new String[countTotalExercise];
                    sets = new String[countTotalExercise];
                    repetitions = new String[countTotalExercise];
                    countTotalExercise--;

                    while (countTotalExercise >= 0) {
                        // Choose  a random exercise
                        int randExerciseSelect = r.nextInt(exercisePicks.length);
                        // Choose a random number of sets
                        int randSetSelect = r.nextInt(4);
                        // Choose which array to pull the repetitions from
                        int randRepetitionArraySelect = r.nextInt(2);
                        // Needed a string array from values such as "10, 8, 6"
                        int randStringRepetitionSelect = r.nextInt(4);
                        // Needed an int array for values such as "10" and "6", kill me
                        int randIntRepetitionSelect = r.nextInt(2);

                        // Check if the exercise has already been selected and placed into the workout
                        for (int i = 0; i < tempExercises.length; i++) {
                            if (tempExercises[i] == exercisePicks[randExerciseSelect]) {
                                // If so, re-roll and restart the check
                                randExerciseSelect = r.nextInt(exercisePicks.length);
                                i = -1;
                            }
                        }
                        // If it passes the check, put it into the workout
                        tempExercises[countTotalExercise] = exercisePicks[randExerciseSelect];

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
                    workout = new String[tempExercises.length];
                    for (int i = 0; i < workout.length; i++) {
                        workout[i] = tempExercises[i] + "\n" + "\t\t\tSets:\t" + sets[i] + "\n" + "\t\t\tRepetitions:\t" + repetitions[i] + "\n";
                    }

                    // Print the sucker to screen
                    arrayAddNewExercise = new ArrayAdapter<String>(CustomWorkoutOptions.this, R.layout.item_list_view_swolodex_2, workout);
                    ListView displayExercises = (ListView) findViewById(R.id.exerciseList);
                    displayExercises.setAdapter(arrayAddNewExercise);

                    // initialize the save button and its listener
                    Button saveButton = (Button) findViewById(R.id.saveRandWorkout);
                    saveButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            saveWorkout(workout);
                        }
                    });

                    SharedPreferences.Editor prefsEditor = prefs.edit();
                    prefsEditor.putString("Custom", "");
                    prefsEditor.commit();
                }
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

    public void saveWorkout(final String[] saveWorkout) {

        // need to use original context
        final Context temp = this;

        // Pop up dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Save workout as:");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        // Set view to the pop up
        alert.setView(input);

        // When user hits "Save"...
        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String List = "";

                for (int i = 0; i < saveWorkout.length; i++) {
                    List += saveWorkout[i];
                    List += ";";
                }

                // Save the workout under a name (AKA key)
                // Add the header USER so that we can see which sharedpref keys are user generated
                String userKey = "USER" + input.getText().toString();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(temp);
                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.putString(userKey, List);
                prefsEditor.commit();

                Toast.makeText(temp, "Saved workout '" + input.getText().toString() + "'", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                Toast.makeText(temp, "Canceled Save", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = alert.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.show();
    }
}
