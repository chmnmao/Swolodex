package com.example.ufl.srproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends ActionBarActivity {
    CallbackManager cb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: There's a bug that occurs on facebook login, find a way to resolve it
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        Resources res=getResources();
        String prefString=res.getString(R.string.prefsFile);
        final SharedPreferences pref=this.getSharedPreferences(prefString, Context.MODE_PRIVATE);
        cb=CallbackManager.Factory.create();
        LoginButton fbLog = (LoginButton) findViewById(R.id.fb_login);

        TextView registerScreen = (TextView) findViewById(R.id.link_to_register);
        registerScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        fbLog.registerCallback(cb, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(),"Successful Login", Toast.LENGTH_LONG).show();
                EditText user = (EditText)findViewById(R.id.userField);
                pref.edit().putString("authToken",loginResult.getAccessToken().getToken());
                user.setText(loginResult.getAccessToken().getUserId());
                //getting email and other facebook info
                /*String accessToken = loginResult.getAccessToken().getToken();
                Log.i("accessToken",accessToken);
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback(){
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response){
                        Log.i("LoginActivity", response.toString());
                        //Get facebook data from login
                        Bundle bFacebookData = getFacebookData(object);
                        pref.edit().putString("email",bFacebookData.get("email").toString());
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();*/
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"Cancel Login", Toast.LENGTH_LONG).show();
                Log.v("LoginActivity","cancel");
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getApplicationContext(),"Login Failure", Toast.LENGTH_LONG).show();
                Log.v("LoginActivity",e.getCause().toString());
            }
        });

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        cb.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    private Bundle getFacebookData(JSONObject object){
        try{
            Bundle bundle= new Bundle();
            String id=object.getString("id");
            try{
                URL profile_pic=new URL("https://graph.facebook.com/"+id+"/picture?width=200&height=150");
                Log.i("profile_pic",profile_pic+"");
                bundle.putString("profile_pic",profile_pic.toString());
            }catch(MalformedURLException e){
                e.printStackTrace();
                return null;
            }
            bundle.putString("idFacebook",id);
            if(object.has("first_name"))
                bundle.putString("first_name",object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));

            return bundle;
        }catch(JSONException e){
            e.printStackTrace();
            return null;
        }
    }
}
