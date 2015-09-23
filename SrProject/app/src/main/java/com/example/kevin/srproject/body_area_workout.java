package com.example.kevin.srproject;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class body_area_workout extends ActionBarActivity {
    //region VARIABLE DECLARATION
    //TODO: Store this data somewhere else so we don't call it every time we start activities
    //First we declare icons and titles for navigation
    //Store them in the array below
    String TITLES[] = {"Home","Events","Mail","Shop","Travel"};
    int ICONS[] = {R.drawable.ic_home,R.drawable.ic_events,R.drawable.ic_mail,R.drawable.ic_shop,R.drawable.ic_travel};

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //receive intent
        Intent intent = getIntent();
        String body_area = intent.getStringExtra("body_area");

        setContentView(R.layout.activity_body_area_workout);
        Resources res = getResources();
        //Determine which body area we are working
        //this hashmap gives us corresponding muscles to the area we chose
        HashMap<String, Integer> workoutMap=new HashMap<>();
        workoutMap.put(res.getString(R.string.UPPER_BODY), R.array.MUSCLE_GROUP_UPPER);
        workoutMap.put(res.getString(R.string.LOWER_BODY), R.array.MUSCLE_GROUP_LOWER);
        workoutMap.put(res.getString(R.string.CALISTHENICS), R.array.CALISTHENICS);
        workoutMap.put(res.getString(R.string.ARMS), R.array.MUSCLE_GROUP_ARMS);
        workoutMap.put(res.getString(R.string.OTHER), R.array.OTHER);

        //region INITIALIZE TOOLBAR AND MENU NAVIGATION
        /* Assinging the toolbar object ot the view
    and setting the the Action bar to our toolbar
     */
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(body_area);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager


        Drawer = (DrawerLayout) findViewById(R.id.mainLayout);        // Drawer object Assigned to the view
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
        //So we want to display a randomly generated workout dependent on what muscle area selected
        //Determine which muscle group to select from
        //Select from those arrays
        //Dependent on which muscle group paged from intent


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_body_area_workout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<String> randomizeUpper(Resources res){
        //Our output list
        List<String> resultWorkout = new ArrayList<>();
        //The string that we use to get muscle group names
        List<String> upperAreas = Arrays.asList(res.getStringArray(R.array.MUSCLE_GROUP_UPPER));
        //Add the arm workouts that are stored in a different arary. This needs to be changed later
        upperAreas.addAll(Arrays.asList(res.getStringArray(R.array.MUSCLE_GROUP_ARMS)));
        //Where we store muscle group and workout associations
        HashMap<String, String[]> upperMap=new HashMap<>();

        upperMap.put(upperAreas.get(upperAreas.indexOf("CHEST")), res.getStringArray(R.array.CHEST));
        upperMap.put(upperAreas.get(upperAreas.indexOf("TRAPS")), res.getStringArray(R.array.TRAPS));
        upperMap.put(upperAreas.get(upperAreas.indexOf("SHOULDERS")),
                res.getStringArray(R.array.SHOULDERS));
        upperMap.put(upperAreas.get(upperAreas.indexOf("BICEPS")), res.getStringArray(R.array.BICEPS));
        upperMap.put(upperAreas.get(upperAreas.indexOf("TRICEPS")), res.getStringArray((R.array.TRICEPS)));
        //More areas need to be added later but for now we will stay with these

        return resultWorkout;
    }

}
