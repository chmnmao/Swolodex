package com.example.kevin.srproject;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class presetRandomizers extends ActionBarActivity {

    String[] exercisePicks;
    String[] sets;
    String[] repetitions;
    String[] workout;
    ArrayAdapter<String> arrayAddNewExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rand_preset_workout_menu);
    }

    public void randomizeUpperBodyWorkout(View view) {

        Random r = new Random();
        // Used to determine how many exercises you could possible do in a workout
        int countTotalExercise = r.nextInt(4) + 5;

        // Load up a 2D array with all upper body listings
        String[][] tempExerciseList = {
                getResources().getStringArray(R.array.chest),
                getResources().getStringArray(R.array.traps),
                getResources().getStringArray(R.array.shoulders),
                getResources().getStringArray(R.array.biceps),
                getResources().getStringArray(R.array.triceps),
                getResources().getStringArray(R.array.wrists),
                getResources().getStringArray(R.array.upperBack),
                getResources().getStringArray(R.array.lowerBack)
        };

        // Turn this 2D array into a 1D array, there were complications with a 2D array
        List<String> exerciseList = new ArrayList<String>();
        for (int i = 0; i < tempExerciseList.length; i++){
            for (int j = 0; j < tempExerciseList[i].length; j++){
                exerciseList.add(tempExerciseList[i][j]);
            }
        }

        // Initialization of these arrays
        exercisePicks = new String[countTotalExercise];
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

            // Cast the int to a string and set the sets to that number... nice wording
            sets[countTotalExercise] = Integer.toString(getResources().getIntArray(R.array.sets)[randSetSelect]);
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
        arrayAddNewExercise = new ArrayAdapter<String>(this, R.layout.simple_list_item_custom, workout);
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

    public void randomizeLowerBodyWorkout(View view) {

        Random r = new Random();
        // Used to determine how many exercises you could possible do in a workout
        int countTotalExercise = r.nextInt(4) + 5;

        // Load up a 2D array with all lower body listings
        String[][] tempExerciseList = {
                getResources().getStringArray(R.array.totalLowerBody),
                getResources().getStringArray(R.array.calves),
                getResources().getStringArray(R.array.quads),
                getResources().getStringArray(R.array.gluts),
                getResources().getStringArray(R.array.hamstrings),
        };

        // turn this 2D array into a 1D array, there were complications with a 2D array
        List<String> exerciseList = new ArrayList<String>();
        for (int i = 0; i < tempExerciseList.length; i++){
            for (int j = 0; j < tempExerciseList[i].length; j++){
                exerciseList.add(tempExerciseList[i][j]);
            }
        }

        // Initialization of these arrays
        exercisePicks = new String[countTotalExercise];
        sets = new String[countTotalExercise];
        repetitions = new String[countTotalExercise];
        countTotalExercise--;

        while (countTotalExercise >= 0) {
            // Choose a random exercise, number of sets
            int randExerciseSelect = r.nextInt(exerciseList.size());
            int randSetSelect = r.nextInt(4);
            // Choose which array to pull the repetitions from
            int randStringRepetitionSelect = r.nextInt(4);
            // Needed a string array from values such as "10, 8, 6"
            int randIntRepetitionSelect = r.nextInt(2);
            // Needed an int array for values such as "10" and "6", kill me
            int randRepetitionArraySelect = r.nextInt(2);

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

            // Cast the int to a string and set the sets to that number... nice wording
            sets[countTotalExercise] = Integer.toString(getResources().getIntArray(R.array.sets)[randSetSelect]);
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
        arrayAddNewExercise = new ArrayAdapter<String>(this, R.layout.simple_list_item_custom, workout);
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
