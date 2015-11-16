package com.example.ufl.srproject;

import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.ProfilePictureView;

/**
 * Created by Kevin on 10/21/2015.
 */
public class BaseActivity extends ActionBarActivity {
    //region VARIABLE DECLARATION
    //TODO: Store this information in res xml files
    //Declare callback manager
    CallbackManager callbackManager;
    //First we declare icons and titles for navigation
    //Store them in the array below
    String TITLES[] = {"Home","Events","Mail","Shop","Travel","Customize","Saved Workouts"};
    //TODO: Uniform icon styles
    int ICONS[] = {R.drawable.ic_home,R.drawable.ic_events,R.drawable.ic_mail,R.drawable.ic_shop,R.drawable.ic_travel,R.drawable.ic_cust_weight,R.drawable.ic_save};

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

    //region Do facebook login stuff here
    //Variable declaration
    String get_id, get_name, get_gender, get_email, get_birthday, get_locale, get_location;


    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager=CallbackManager.Factory.create();

    }

    @Override
    public void setContentView(int layoutResID){
        DrawerLayout fullView=(DrawerLayout) getLayoutInflater().
                inflate(R.layout.activity_base,new LinearLayout(this.getApplicationContext()),false);
        FrameLayout activityContainer=(FrameLayout) fullView.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID,activityContainer,true);
        super.setContentView(fullView);

    //region INITIALIZE TOOLBAR AND MENU NAVIGATION
        /* Assigning the toolbar object ot the view
        and setting the the Action bar to our toolbar
        */
        //region INITIALIZE FACEBOOK PROFILE INFORMATION
        ProfilePictureView myProfilePic;
        myProfilePic=(ProfilePictureView)findViewById(R.id.profilePicture);
        String userID;
        Profile profile=Profile.getCurrentProfile();
        if(profile!=null){
            //Set profile data
            //Currently we set name and email here and do profile picture in MyAdapter class
            NAME=profile.getName();
        }
        //endregion
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView
        //region Setting onclick listeners for the RecyclerView
        final GestureDetector gd = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e){
                return true;
            }
        });
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener(){
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent){
                View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());
                if(child!=null&&gd.onTouchEvent(motionEvent)){
                    Drawer.closeDrawers();
                    //Do something here depending on item
                    switch(recyclerView.getChildPosition(child)){
                        case 1://Clicked to go to home menu
                            Intent toHome = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(toHome);
                            break;
                        case 6://We have clicked on the customize workout
                            Intent toCustom = new Intent(getApplicationContext(),RandCustomWorkout.class);
                            startActivity(toCustom);
                            break;
                        case 7://We have clicked on the customize workout
                            Intent toSaved = new Intent(getApplicationContext(),ListSavedWorkouts.class);
                            startActivity(toSaved);
                            break;
                    }
                    return true;
                }
                return false;
            }
            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent){}
        });
        //endregion
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }
}
