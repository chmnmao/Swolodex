package com.example.ufl.srproject;

/**
 * Created by danie_000 on 10/6/2015.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class RandCustomWorkout extends BaseActivity {
//TODO: You should not be able to add the same exercise over and over and over
    //TODO: Also, we need to fix this layout, it's ugly.
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    List<String> exercisePicks = new ArrayList<String>();
    List<String> exerciseDisplay = new ArrayList<String>();
    ArrayAdapter<String> arrayAddNewExercise;
    String[] workout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rand_custom_workout_menu);
        Resources res = getResources();

        final Context useMe = this;

        // get the list view
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {
                Toast.makeText(useMe, "Added: " + listDataHeader.get(groupPosition) + " > " + listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();

                exercisePicks.add(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));
                exerciseDisplay.add(listDataHeader.get(groupPosition)+ " > " + listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));
                placeIntoListView();

                return false;
            }
        });

        expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int pos, long id) {
                groupAdd(pos);
                return true;
            }
        });

        final ListView displayExercises = (ListView)findViewById(R.id.selectionList);
        displayExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(useMe,"Removed: " + arrayAddNewExercise.getItem(position), Toast.LENGTH_SHORT).show();

                removeFromListView(position);
            }
        });
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Chest");
        listDataHeader.add("Traps");
        listDataHeader.add("Shoulders");
        listDataHeader.add("Biceps");
        listDataHeader.add("Triceps");
        listDataHeader.add("Wrists");
        listDataHeader.add("Upper Back");
        listDataHeader.add("Lower Back");
        listDataHeader.add("Full Lower Body");
        listDataHeader.add("Calves");
        listDataHeader.add("Quads");
        listDataHeader.add("Gluts");
        listDataHeader.add("Hamstrings");

        // Adding child data
        List<String> chest = new ArrayList<String>();
        for(int i =0; i < getResources().getStringArray(R.array.CHEST).length; i++){
            chest.add(getResources().getStringArray(R.array.CHEST)[i]);
        }

        List<String> traps = new ArrayList<String>();
        for(int i =0; i < getResources().getStringArray(R.array.TRAPS).length; i++){
            traps.add(getResources().getStringArray(R.array.TRAPS)[i]);
        }


        List<String> shoulders = new ArrayList<String>();
        for(int i =0; i < getResources().getStringArray(R.array.SHOULDERS).length; i++){
            shoulders.add(getResources().getStringArray(R.array.SHOULDERS)[i]);
        }

        List<String> biceps = new ArrayList<String>();
        for(int i =0; i < getResources().getStringArray(R.array.BICEPS).length; i++){
            biceps.add(getResources().getStringArray(R.array.BICEPS)[i]);
        }

        List<String> triceps = new ArrayList<String>();
        for(int i =0; i < getResources().getStringArray(R.array.TRICEPS).length; i++){
            triceps.add(getResources().getStringArray(R.array.TRICEPS)[i]);
        }

        List<String> wrists = new ArrayList<String>();
        for(int i =0; i < getResources().getStringArray(R.array.WRISTS).length; i++){
            wrists.add(getResources().getStringArray(R.array.WRISTS)[i]);
        }

        List<String> upperBack = new ArrayList<String>();
        for(int i =0; i < getResources().getStringArray(R.array.UPPER_BACK).length; i++){
            upperBack.add(getResources().getStringArray(R.array.UPPER_BACK)[i]);
        }

        List<String> lowerBack = new ArrayList<String>();
        for(int i =0; i < getResources().getStringArray(R.array.LOWER_BACK).length; i++){
            lowerBack.add(getResources().getStringArray(R.array.LOWER_BACK)[i]);
        }

        List<String> totalLowerBody = new ArrayList<String>();
        for(int i =0; i < getResources().getStringArray(R.array.LOWER_BODY).length; i++){
            totalLowerBody.add(getResources().getStringArray(R.array.LOWER_BODY)[i]);
        }

        List<String> calves = new ArrayList<String>();
        for(int i =0; i < getResources().getStringArray(R.array.CALVES).length; i++){
            calves.add(getResources().getStringArray(R.array.CALVES)[i]);
        }

        List<String> quads = new ArrayList<String>();
        for(int i =0; i < getResources().getStringArray(R.array.QUADS).length; i++){
            quads.add(getResources().getStringArray(R.array.QUADS)[i]);
        }

        List<String> gluts = new ArrayList<String>();
        for(int i =0; i < getResources().getStringArray(R.array.GLUTES).length; i++){
            gluts.add(getResources().getStringArray(R.array.GLUTES)[i]);
        }

        List<String> hamstrings = new ArrayList<String>();
        for(int i =0; i < getResources().getStringArray(R.array.HAMSTRINGS).length; i++){
            hamstrings.add(getResources().getStringArray(R.array.HAMSTRINGS)[i]);
        }

        listDataChild.put(listDataHeader.get(0), chest); // Header, Child data
        listDataChild.put(listDataHeader.get(1), traps);
        listDataChild.put(listDataHeader.get(2), shoulders);
        listDataChild.put(listDataHeader.get(3), biceps);
        listDataChild.put(listDataHeader.get(4), triceps);
        listDataChild.put(listDataHeader.get(5), wrists);
        listDataChild.put(listDataHeader.get(6), upperBack);
        listDataChild.put(listDataHeader.get(7), lowerBack);
        listDataChild.put(listDataHeader.get(8), totalLowerBody);
        listDataChild.put(listDataHeader.get(9), calves);
        listDataChild.put(listDataHeader.get(10), quads);
        listDataChild.put(listDataHeader.get(11), gluts);
        listDataChild.put(listDataHeader.get(12), hamstrings);
    }

    public void randWorkout(View view) {
        // no exercises selected
        if (exercisePicks.size() < 1) {
            Toast.makeText(this,"Please select at least one workout or group to randomize", Toast.LENGTH_SHORT).show();
        }

        else{
            int countTotalExercise = exercisePicks.size();
            String[] sets;
            String[] repetitions;

            Random r = new Random();

            sets = new String[countTotalExercise];
            repetitions = new String[countTotalExercise];
            countTotalExercise--;

            while (countTotalExercise >= 0) {
                int randSetSelect = r.nextInt(4);
                int randStringRepetitionSelect = r.nextInt(4);
                int randIntRepetitionSelect = r.nextInt(2);
                int randRepetitionArraySelect = r.nextInt(2);

                sets[countTotalExercise] = Integer.toString(getResources().getIntArray(R.array.SETS)[randSetSelect]);
                if (randRepetitionArraySelect == 0 && sets[countTotalExercise].equals("3")) {
                    repetitions[countTotalExercise] = getResources().getStringArray(R.array.stringRepetitions)[randStringRepetitionSelect];
                }
                else {
                    repetitions[countTotalExercise] = Integer.toString(getResources().getIntArray(R.array.intRepetitions)[randIntRepetitionSelect]);
                }

                countTotalExercise--;
            }

            setContentView(R.layout.rand_workout_menu);

            workout = new String[exercisePicks.size()];
            for(int i = 0; i < workout.length; i++)
            {
                workout[i] = exercisePicks.get(i) + "\n" + "\t\t\tSets:\t" + sets[i] + "\n" + "\t\t\tRepetitions:\t" + repetitions[i] + "\n";
            }

            arrayAddNewExercise = new ArrayAdapter<String>(this, R.layout.simple_list_item_custom, workout);
            ListView displayExercises = (ListView)findViewById(R.id.exerciseList);
            displayExercises.setAdapter(arrayAddNewExercise);

            Button saveButton = (Button)findViewById(R.id.saveRandWorkout);
            saveButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    saveWorkout(workout);
                }
            });
        }
    }

    // Called when the user clicks the More Options button
    public void options () {
        if (exercisePicks.size() < 1) {
            Toast.makeText(this,"Please select at least one workout or group to randomize", Toast.LENGTH_SHORT).show();
        }
        else {
            String Storing = "";

            for (int i = 0; i < exercisePicks.size(); i++) {
                Storing += exercisePicks.get(i);
                Storing += ";";
            }

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor prefsEditor = prefs.edit();

            String List = prefs.getString("Custom", "");
            if(List.equals("")) {
                prefsEditor.putString("Custom", Storing);
                prefsEditor.commit();
            }
            else {
                prefsEditor.putString("Custom", List + Storing);
                prefsEditor.commit();
            }

            Intent intent = new Intent(this, CustomWorkoutOptions.class);
            startActivity(intent);
        }
    }

    // allows user to see exercises picked
    public void placeIntoListView(){
        arrayAddNewExercise = new ArrayAdapter<String>(this, R.layout.item_list_view_swolodex_2, exerciseDisplay);
        ListView displayExercises = (ListView)findViewById(R.id.selectionList);
        displayExercises.setAdapter(arrayAddNewExercise);
    }

    // allows user to remove selected exercises
    public void removeFromListView(int position){
        String removeMe = arrayAddNewExercise.getItem(position);

        // removes a specific exercise
        if (!removeMe.contains(">")) {
            // Find what group we are looking to remove
            int truePosition = 0;
            for (int i = 0; i < listDataHeader.size(); i++) {
                if (arrayAddNewExercise.getItem(position) == listDataHeader.get(i)){
                    truePosition = i;
                    break;
                }
            }
            // Remove all children of a group from the exercisePicks array
            for(int i = 0; i < listDataChild.get(listDataHeader.get(truePosition)).size(); i++) {
                for(int j = 0; j < exercisePicks.size(); j++) {
                    if (i < listDataChild.get(listDataHeader.get(truePosition)).size() && (listDataChild.get(listDataHeader.get(truePosition)).get(i) == exercisePicks.get(j))) {
                        exercisePicks.remove(j);
                        i++;
                        j = -1;
                    }
                }
            }
        }
        else {
            exercisePicks.remove(position);
        }

        arrayAddNewExercise.remove(removeMe);

        ListView displayExercises = (ListView)findViewById(R.id.selectionList);
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

    public void groupAdd(int pos) {
        Toast.makeText(this,"Added: " + listDataHeader.get(pos) + "  At index: " + pos + "  Of Length : " + listDataChild.get(listDataHeader.get(pos)).size(), Toast.LENGTH_SHORT).show();

        // Add all children of a group to the exercisePicks array
        for(int i = 0; i < listDataChild.get(listDataHeader.get(pos)).size(); i++) {
            exercisePicks.add(listDataChild.get(listDataHeader.get(pos)).get(i));
        }

        // Display the group we added
        exerciseDisplay.add(listDataHeader.get(pos));
        arrayAddNewExercise = new ArrayAdapter<String>(this, R.layout.item_list_view_swolodex_2, exerciseDisplay);
        ListView displayExercises = (ListView)findViewById(R.id.selectionList);
        displayExercises.setAdapter(arrayAddNewExercise);
    }
//region Menu creation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_custom, menu);
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
        if(id==R.id.action_custom_options){
            this.options();
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
//endregion
}
