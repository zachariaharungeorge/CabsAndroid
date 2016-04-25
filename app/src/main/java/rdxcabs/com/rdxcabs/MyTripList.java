package rdxcabs.com.rdxcabs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;

import static com.firebase.client.Firebase.setAndroidContext;

public class MyTripList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidContext(this);
        setContentView(R.layout.activity_my_trip_list);

        final ArrayList<TripsBean> lst= new ArrayList<TripsBean>();

        final ProgressDialog progressDialog=ProgressDialog.show(this, "Loading", "Fetching Data", false,false);
        progressDialog.show();
        Firebase myFirebaseRef = new Firebase("https://resplendent-fire-1005.firebaseio.com/Trips");

        SharedPreferences sp=getSharedPreferences("username", 0);
        final String username = sp.getString("username","");

        myFirebaseRef.addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                    TripsBean tripsBean = dataSnapshot.getValue(TripsBean.class);
                                                    if (tripsBean.getUsername().equalsIgnoreCase(username)) {
                                                        lst.add(tripsBean);
                                                    }

                                                    TripLayoutAdapter tripLayoutAdapter = new TripLayoutAdapter(MyTripList.this, R.layout.activity_trip_layout, lst);
                                                    ListView listView = (ListView) findViewById(R.id.listView);
                                                    listView.setAdapter(tripLayoutAdapter);
                                                    progressDialog.dismiss();

                                                }

                                                @Override
                                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                                }

                                                @Override
                                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                                }

                                                @Override
                                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                                }

                                                @Override
                                                public void onCancelled(FirebaseError firebaseError) {

                                                }
                                            }
        );


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_trip_list, menu);
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

        if(id == R.id.signOut){
            SharedPreferences sp = getSharedPreferences("username", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor= sp.edit();
            editor.putString("username",null);
            editor.commit();
            Intent intent = new Intent(MyTripList.this,MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
