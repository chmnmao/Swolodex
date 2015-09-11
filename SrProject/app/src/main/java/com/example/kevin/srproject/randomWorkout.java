package com.example.kevin.srproject;

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
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class randomWorkout extends ActionBarActivity {
//region VARIABLE DECLARATION
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

    //Declaring adapters to display information here
    ExpandableListAdapter groups_Adapter;
    ExpandableListView expandable_Container;
    List<String> headerData;
    HashMap<String, List<String>> childData;
//endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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


        Drawer = (DrawerLayout) findViewById(R.id.mainDrawerLayout);        // Drawer object Assigned to the view
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
//endregion A AN

//region PAGE CONTENT
        /*The main page content is dedicated to randomizing
         workouts by muscle groups e.g. Select a muscle group
         and generate a workout
        * */
        expandable_Container = (ExpandableListView)findViewById(R.id.expandable_container);
        //prepare the list data
        prepareListData(res);
        groups_Adapter=new ExpandableListAdapter(this.getApplicationContext(), headerData, childData);
        expandable_Container.setAdapter((ExpandableListAdapter)groups_Adapter);
//endregion
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void prepareListData(Resources res){
        /*This method populates our preinitialized List and HashMap objects with
        data from the arrays.xml file for body region and muscle group strings
        */
        headerData = new ArrayList<String>();
        childData = new HashMap<String, List<String>>();

        //First we populate the header data
        headerData.addAll(Arrays.asList(res.getStringArray(R.array.BODY_AREAS)));
        //Now we populate the child data
        //Populate by body area
        List<String> WORKOUT_UPPER = new ArrayList<String>(Arrays.asList(
                res.getStringArray(R.array.MUSCLE_GROUP_UPPER)));
        List<String> WORKOUT_ARMS = new ArrayList<String>(Arrays.asList(
                res.getStringArray(R.array.MUSCLE_GROUP_ARMS)));
        List<String> WORKOUT_LOWER = new ArrayList<String>(Arrays.asList(
                res.getStringArray(R.array.MUSCLE_GROUP_LOWER)));
        List<String> WORKOUT_CALIS = new ArrayList<String>(Arrays.asList(
                res.getStringArray(R.array.CALISTHENICS)));
        List<String> WORKOUT_OTHER = new ArrayList<String>(Arrays.asList(
                res.getStringArray(R.array.OTHER)));
        //Match header to child data
        //Perhaps we find a way to do this automatically in the future for now we just use put()
        childData.put(headerData.get(0),WORKOUT_UPPER);
        childData.put(headerData.get(1),WORKOUT_ARMS);
        childData.put(headerData.get(2),WORKOUT_LOWER);
        childData.put(headerData.get(3),WORKOUT_CALIS);
        childData.put(headerData.get(4),WORKOUT_OTHER);
    }
}
