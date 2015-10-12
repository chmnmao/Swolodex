package com.example.ufl.srproject;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
//region VARIABLE DECLARATION
    //TODO: Store this information in res xml files
    //First we declare icons and titles for navigation
    //Store them in the array below
    String TITLES[] = {"Home","Events","Mail","Shop","Travel","Customize"};
    //TODO: Uniform icon styles
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resources res = getResources();
//TODO:Create method to call and initialize toolbar instead of doing it every single time
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
        //region Setting onclick listeners for the RecyclerView
        final GestureDetector gd = new GestureDetector(MainActivity.this,new GestureDetector.SimpleOnGestureListener(){
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
                    if(recyclerView.getChildPosition(child)==6){
                        //We have clicked on the customize workout
                        Intent toCustom = new Intent(MainActivity.this,RandCustomWorkout.class);
                        startActivity(toCustom);
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

//region PAGE CONTENT
        /*The main page content is dedicated to randomizing
         workouts by muscle groups e.g. Select a muscle group
         and generate a workout
        * */
        ListView container = (ListView)findViewById(R.id.container);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.item_list_view_swolodex_1,
                res.getStringArray(R.array.BODY_AREAS));
        //TODO:
        //Eventually set alternate colors or some sort of styling for adapters
        //Also eventually we should use our own list item layout
        container.setAdapter(adapter);
        container.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //On item click we send the user to the next activity
                //TODO: Currently send intent to demo page
                Intent to_body_area = new Intent(getBaseContext(), presetRandomizers.class);
                to_body_area.putExtra("body_area",((TextView)view).getText());
                startActivity(to_body_area);
            }
        });
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
        if(id==R.id.action_login){
            //Begin login intent
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

//region unused methods
   /** private void prepareListData(Resources res){
        This method populates our preinitialized List and HashMap objects with
        data from the arrays.xml file for body region and muscle group strings

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
    }**/
    //endregion
}
