package com.rdxcabs.UIActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.rdxcabs.Beans.UserBean;
import com.rdxcabs.Constants.Constants;
import com.rdxcabs.R;

import static com.firebase.client.Firebase.setAndroidContext;

public class LoginActivity extends AppCompatActivity {

    boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidContext(this);
        setContentView(R.layout.activity_login_activity);


        final TextView username=(TextView) findViewById(R.id.username);
        final TextView password=(TextView) findViewById(R.id.password);

        final Button login=(Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "Loading", "Logging in", false, false);

                Firebase firebaseRef = new Firebase(Constants.FIREBASE_URL + Constants.URL_SEP +Constants.USERS).child(username.getText().toString());
                firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
                        alertDialog.setTitle("Login");
                        if (dataSnapshot.exists()) {
                            final UserBean u = dataSnapshot.getValue(UserBean.class);
                            if (u.getUsername().equals(username.getText().toString()) && u.getPassword().equals(password.getText().toString())) {
                                SharedPreferences sharedPreferences = getSharedPreferences("username", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username", u.getUsername());
                                editor.commit();
                                alertDialog.setMessage("Login Successful");
                                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(LoginActivity.this, MenuScreenActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                alertDialog.setMessage("Login Unscuccessful");
                                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        } else {
                            alertDialog.setMessage("Login Unscuccessful");
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                        progressDialog.dismiss();
                        alertDialog.show();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
