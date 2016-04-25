package rdxcabs.com.rdxcabs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class bookCabActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private GoogleMap mMap;
    private LatLng latLngSource;
    private LatLng latLngDest;
    private String parsedDistance;
    private Calendar cal = Calendar.getInstance();
    private int calYear = cal.get(Calendar.YEAR);
    private int calMonth = cal.get(Calendar.MONTH);
    private int calDay = cal.get(Calendar.DAY_OF_MONTH);

    private int calHour = cal.get(Calendar.HOUR_OF_DAY);
    private int calMinute = cal.get(Calendar.MINUTE);
    private String distance;
    private String cab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_cab_activity);
        setUpMapIfNeeded();

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.cabTypes, R.layout.dropdownlayout);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cab = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button enterDate = (Button) findViewById(R.id.enterDate);
        Button enterTime = (Button) findViewById(R.id.enterTime);


        enterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(bookCabActivity.this,bookCabActivity.this,calYear,calMonth,calDay);
                datePickerDialog.show();
            }
        });

        enterTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(bookCabActivity.this,bookCabActivity.this,calHour,calMinute,true);
                timePickerDialog.show();
            }
        });


        Button sourceBut = (Button) findViewById(R.id.source);
        sourceBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = ProgressDialog.show(bookCabActivity.this, "Loading", "Booking Cab", false, false);

                try{
                    if(latLngDest != null && latLngSource != null) {
                        distance=getDistance(latLngSource.latitude,latLngSource.longitude,latLngDest.latitude,latLngDest.longitude);
                        String[] distParse = distance.split(" ");
                        Double dist = Double.parseDouble(distParse[0]);
                        final double fare;
                        switch (cab){
                            case "Mini":fare=dist*8;
                                break;
                            case "Sedan":fare=dist*9;
                                break;
                            case "Sumo":fare=dist*10;
                                break;
                            case "XL":fare=dist*12;
                                break;
                            case "XT":fare=dist*14;
                                break;
                            case "Mini Bus":fare=dist*18;
                                break;
                            default:fare=0;
                        }
                        progressDialog.dismiss();

                        AlertDialog.Builder alert = new AlertDialog.Builder(bookCabActivity.this);
                        alert.setTitle("Book Cab");
                        alert.setMessage("Distance: " + distance + "\n" +
                                "Cab " + cab + "\n" +
                                "Fare: " + fare + "\n" +
                                "Date:" + calHour + ":" + calMinute + " " + calDay + "-" + calMonth + "-" + calYear);
                        alert.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final ProgressDialog progressDialog2 = ProgressDialog.show(bookCabActivity.this, "Loading", "Saving Data", false, false);
                                Firebase.setAndroidContext(bookCabActivity.this);
                                Firebase myFirebaseRef = new Firebase("https://resplendent-fire-1005.firebaseio.com/Trips");

                                Geocoder gcd = new Geocoder(bookCabActivity.this, Locale.getDefault());
                                List<Address> addresses1 = null;
                                List<Address> addresses2=null;
                                try {
                                    addresses1 = gcd.getFromLocation(latLngSource.latitude, latLngSource.longitude, 1);
                                    addresses2=gcd.getFromLocation(latLngDest.latitude,latLngDest.longitude,1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                SharedPreferences sp=getSharedPreferences("username", Context.MODE_PRIVATE);
                                final String username = sp.getString("username","");

                                TripsBean tripDetails = new TripsBean();
                                tripDetails.setUsername(username);
                                if (addresses1.size() > 0){
                                    tripDetails.setSourceLoc(addresses1.get(0).getAddressLine(0));
                                }
                                if(addresses2.size()>0){
                                    tripDetails.setDestLoc(addresses2.get(0).getAddressLine(0));
                                }
                                tripDetails.setCabType(cab);
                                tripDetails.setDate(calHour + ":" + calMinute + " " + calDay + "-" + calMonth + "-" + calYear);
                                tripDetails.setFare("" + fare);

                                myFirebaseRef.child(username + System.currentTimeMillis()).setValue(tripDetails, new Firebase.CompletionListener() {
                                    @Override
                                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                        progressDialog2.dismiss();
                                        if (firebaseError == null) {
                                            AlertDialog.Builder alert = new AlertDialog.Builder(bookCabActivity.this);
                                            alert.setTitle("Book Cab");
                                            alert.setMessage("Cab Booked Successfully");
                                            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(bookCabActivity.this, menuscreen.class);
                                                    startActivity(intent);
                                                }
                                            });
                                            alert.show();
                                        } else {
                                            AlertDialog.Builder alert = new AlertDialog.Builder(bookCabActivity.this);
                                            alert.setTitle("Book Cab");
                                            alert.setMessage("Could not process. Please try again");
                                            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(bookCabActivity.this, bookCabActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                            alert.show();
                                        }
                                    }
                                });
                            }
                        });
                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alert.show();
                    }
                } catch (Exception e){
                    AlertDialog.Builder alert = new AlertDialog.Builder(bookCabActivity.this);
                    alert.setTitle("Book Cab");
                    alert.setMessage("Could not process. Please try again");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(bookCabActivity.this, bookCabActivity.class);
                            startActivity(intent);
                        }
                    });
                    alert.show();
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_cab_activity, menu);
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
            Intent intent = new Intent(bookCabActivity.this,MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure TimePickerDialogthat {@link #mMap} is not null.
     */
    private void setUpMap() {

        ;

        GPSTracker gps = new GPSTracker(bookCabActivity.this);
        if(gps.canGetLocation){

            LatLng startPos = new LatLng(gps.getLatitude(),gps.getLongitude());
            LatLng destPos = new LatLng(gps.getLatitude()+.0025D,gps.getLongitude()+.0025D);

            latLngSource=startPos;
            latLngDest=destPos;

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gps.getLatitude(), gps.getLongitude()), 15.0f));

            final Marker destPosMarker = mMap.addMarker(new MarkerOptions().position(destPos).title("Destination").draggable(true));
            final Marker startPosMarker = mMap.addMarker(new MarkerOptions().position(startPos).title("Source").draggable(true));


            destPosMarker.showInfoWindow();
            startPosMarker.showInfoWindow();

            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {

                    latLngSource = startPosMarker.getPosition();
                    latLngDest=destPosMarker.getPosition();

                }

            });

        } else {
            gps.showSettingsAlert();
        }
    }

    private String getDistance(final double lat1, final double lon1, final double lat2, final double lon2){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                String response;
                try {
                    URL url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&sensor=false&units=metric&mode=driving");
                    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    response= org.apache.commons.io.IOUtils.toString(in, "UTF-8");

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("routes");
                    JSONObject routes = array.getJSONObject(0);
                    JSONArray legs = routes.getJSONArray("legs");
                    JSONObject steps = legs.getJSONObject(0);
                    JSONObject distance = steps.getJSONObject("distance");
                    parsedDistance =distance.getString("text");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return parsedDistance;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        calDay=dayOfMonth;
        calMonth=monthOfYear;
        calYear=year;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calHour = hourOfDay;
        calMinute = minute;
    }
}
