package com.example.ufl.srproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

//TODO: Add a class as a backend data handler
public class presetRandomizers extends BaseActivity {

    List<String> exercisePicks;
    String[] sets;
    String[] repetitions;
    String[] workout;
    ArrayAdapter<String> arrayAddNewExercise;
    final Context temp = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //receive intent
        Intent intent = getIntent();
        String body_area = intent.getStringExtra("body_area");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.rand_workout_menu);
        Resources res = getResources();
        randomizeWorkout(res, body_area);

        // initialize the save button and its listener
        //Refactored: extracted the method to here
        Button saveButton = (Button)findViewById(R.id.saveRandWorkout);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveWorkout(workout);
            }
        });

        ListView displayExercises = (ListView)findViewById(R.id.exerciseList);
        displayExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int pos, long id) {
                // Pop up dialog
                AlertDialog.Builder alert = new AlertDialog.Builder(temp);
                alert.setTitle("What would like like to do?");

                // When user hits "Reroll"...
                alert.setPositiveButton("Reroll", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        //reroll starts here
                        int numberOfExercises = 1;
                        Random r = new Random();

                        // Load up a 2D array with all exercise listings
                        String[][] tempExerciseList = {
                                getResources().getStringArray(R.array.CHEST),
                                getResources().getStringArray(R.array.TRAPS),
                                getResources().getStringArray(R.array.SHOULDERS),
                                getResources().getStringArray(R.array.BICEPS),
                                getResources().getStringArray(R.array.TRICEPS),
                                getResources().getStringArray(R.array.WRISTS),
                                getResources().getStringArray(R.array.UPPER_BACK),
                                getResources().getStringArray(R.array.LOWER_BACK),
                                getResources().getStringArray(R.array.LOWER_BODY),
                                getResources().getStringArray(R.array.CALVES),
                                getResources().getStringArray(R.array.QUADS),
                                getResources().getStringArray(R.array.GLUTES),
                                getResources().getStringArray(R.array.HAMSTRINGS),
                        };

                        // Turn this 2D array into a 1D array, there were complications with a 2D array
                        java.util.List<String> exerciseList = new ArrayList<String>();
                        for (int i = 0; i < tempExerciseList.length; i++) {
                            for (int j = 0; j < tempExerciseList[i].length; j++) {
                                exerciseList.add(tempExerciseList[i][j]);
                            }
                        }

                        // Choose  a random exercise
                        int randExerciseSelect = r.nextInt(exerciseList.size());

                        // Check if the exercise has already been selected and placed into the workout
                        for (int i = 0; i < exercisePicks.size(); i++) {
                            if (exercisePicks.contains(exerciseList.get(randExerciseSelect))) {
                                // If so, re-roll and restart the check
                                randExerciseSelect = r.nextInt(exerciseList.size());
                                i = -1;
                            }
                        }
                        // If it passes the check, put it into the workout
                        exercisePicks.add(pos, exerciseList.get(randExerciseSelect));

                        // Choose a random number of sets
                        int randSetSelect = r.nextInt(4);
                        // Choose which array to pull the repetitions from
                        int randRepetitionArraySelect = r.nextInt(2);
                        // Needed a string array from values such as "10, 8, 6"
                        int randStringRepetitionSelect = r.nextInt(4);
                        // Needed an int array for values such as "10" and "6", kill me
                        int randIntRepetitionSelect = r.nextInt(2);

                        String sets = Integer.toString(getResources().getIntArray(R.array.SETS)[randSetSelect]);

                        String repetitions = "";
                        if (randRepetitionArraySelect == 0 && sets.equals("3")) {
                            repetitions = getResources().getStringArray(R.array.stringRepetitions)[randStringRepetitionSelect];
                        } else {
                            repetitions = Integer.toString(getResources().getIntArray(R.array.intRepetitions)[randIntRepetitionSelect]);
                        }

                        // Go to the correct view for displaying this information
                        setContentView(R.layout.rand_workout_menu);

                        // Assemble the workout array
                        workout[pos] = exercisePicks.get(pos) + "\n" + "\t\t\tSets:\t" + sets + "\n" + "\t\t\tRepetitions:\t" + repetitions + "\n";

                        // Print the sucker to screen
                        arrayAddNewExercise = new ArrayAdapter<String>(temp, R.layout.item_list_view_swolodex_2, workout);
                        ListView displayExercises = (ListView) findViewById(R.id.exerciseList);
                        displayExercises.setAdapter(arrayAddNewExercise);
                    }
                });

                alert.setNegativeButton("Help", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                AlertDialog dialog = alert.create();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                dialog.show();
            }

        });
    }

    //Refactor: Begin consolidating into one single function
    public void randomizeWorkout(Resources res, String bodyArea){
        Random r = new Random();
        // Used to determine how many exercises you could possible do in a workout
        int countTotalExercise = r.nextInt(4) + 5;

        List<String> exerciseList = new ArrayList<String>();
        //Create list dependent on input
        if(bodyArea.equals("UPPER BODY")){
            exerciseList.addAll(Arrays.asList(res.getStringArray(R.array.CHEST)));
            exerciseList.addAll(Arrays.asList(res.getStringArray(R.array.TRAPS)));
            exerciseList.addAll(Arrays.asList(res.getStringArray(R.array.SHOULDERS)));
            exerciseList.addAll(Arrays.asList(res.getStringArray(R.array.BICEPS)));
            exerciseList.addAll(Arrays.asList(res.getStringArray(R.array.TRICEPS)));
            exerciseList.addAll(Arrays.asList(res.getStringArray(R.array.WRISTS)));
            exerciseList.addAll(Arrays.asList(res.getStringArray(R.array.UPPER_BACK)));
            exerciseList.addAll(Arrays.asList(res.getStringArray(R.array.LOWER_BACK)));
        }
        else if(bodyArea.equals("LOWER BODY")){
            exerciseList.addAll(Arrays.asList(res.getStringArray(R.array.LOWER_BODY)));
            exerciseList.addAll(Arrays.asList(res.getStringArray(R.array.CALVES)));
            exerciseList.addAll(Arrays.asList(res.getStringArray(R.array.QUADS)));
            exerciseList.addAll(Arrays.asList(res.getStringArray(R.array.GLUTES)));
            exerciseList.addAll(Arrays.asList(res.getStringArray(R.array.HAMSTRINGS)));
        }

        //Refactored: changed exercisePicks to list for easier implementation
        exercisePicks = new ArrayList<String>();
        sets = new String[countTotalExercise];
        repetitions = new String[countTotalExercise];
        countTotalExercise--;

        while (countTotalExercise >= 0) {
            // Choose a random exercise, number of sets
            int randExerciseSelect = r.nextInt(exerciseList.size());
            int randSetSelect = r.nextInt(4);
            // Choose which array to pull the repetitions from
            int randRepetitionArraySelect = r.nextInt(2);
            // Needed a string array from values such as "10, 8, 6"
            int randStringRepetitionSelect = r.nextInt(4);
            // Needed an int array for values such as "10" and "6", kill me
            int randIntRepetitionSelect = r.nextInt(2);

            // Check if the exercise has already been selected and placed into the workout
            //Refactored: using list implementation we only need a simple if statement
            if(exercisePicks.contains(exerciseList.get(randExerciseSelect)))
                randExerciseSelect=r.nextInt(exerciseList.size());

            // If it passes the check, put it into the workout
            //Refactored: using list implementation
            exercisePicks.add(exerciseList.get(randExerciseSelect));

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


        // Assemble the workout array
        workout = new String[exercisePicks.size()];
        for(int i = 0; i < workout.length; i++)
        {
            workout[i] = exercisePicks.get(i) + "\n" + "\t\t\tSets:\t" + sets[i] + "\n" + "\t\t\tRepetitions:\t" + repetitions[i] + "\n";
        }

        // Print the sucker to screen
        arrayAddNewExercise = new ArrayAdapter<String>(this, R.layout.item_list_view_swolodex_2, workout);
        ListView displayExercises = (ListView)findViewById(R.id.exerciseList);
        displayExercises.setAdapter(arrayAddNewExercise);
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




