package com.example.ufl.srproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

//TODO: Add a class as a backend data handler
public class presetRandomizers extends ActionBarActivity {
    //region VARIABLE DECLARATION
    //TODO: Store this information in res xml files
    //First we declare icons and titles for navigation
    //Store them in the array below
    String TITLES[] = {"Home","Events","Mail","Shop","Travel","Customize"};
    int ICONS[] = {R.drawable.ic_home,R.drawable.ic_events,R.drawable.ic_mail,R.drawable.ic_shop,R.drawable.ic_travel,R.drawable.ic_cust_weight};

    //Add string resources for profile data that we also store in the header
    //And we also create a int resource for profile picture
    String NAME = "Test Tester";
    String EMAIL = "test@test.com";
    int PROFILE = R.drawable.gator;

    private Toolbar toolbar;                              // Declaring the Toolbar Object

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle
//endregion

    List<String> exercisePicks;
    String[] sets;
    String[] repetitions;
    String[] workout;
    ArrayAdapter<String> arrayAddNewExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //receive intent
        Intent intent = getIntent();
        String body_area = intent.getStringExtra("body_area");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.rand_workout_menu);
        Resources res = getResources();
//region INITIALIZE TOOLBAR AND MENU NAVIGATION
        /* Assinging the toolbar object ot the view
        and setting the the Action bar to our toolbar
     */
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager


        Drawer = (DrawerLayout) findViewById(R.id.exerciseDisplay);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.openDrawer,R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }
        };
        // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State
//endregion
        randomizeWorkout(res, body_area);

        // initialize the save button and its listener
        //Refactored: extracted the method to here
        Button saveButton = (Button)findViewById(R.id.saveRandWorkout);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveWorkout(workout);
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
            workout[i] = exercisePicks.get(i) + "\n" + "\t\t\tSets:\t" + sets[i] + "\n" + "\t\t\tRepetitions:\t" + repetitions[i];
        }

        // Print the sucker to screen
        arrayAddNewExercise = new ArrayAdapter<String>(this, R.layout.item_list_view_swolodex_2, workout);
        ListView displayExercises = (ListView)findViewById(R.id.exerciseList);
        displayExercises.setAdapter(arrayAddNewExercise);
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
