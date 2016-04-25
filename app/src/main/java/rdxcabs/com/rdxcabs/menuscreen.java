package rdxcabs.com.rdxcabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class menuscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuscreen);
        Button profile = (Button) findViewById(R.id.button1);
        Button bookCab = (Button) findViewById(R.id.button2);
        Button myCash = (Button) findViewById(R.id.button3);
        Button myTrips = (Button)findViewById(R.id.button4);
        Button rateCard = (Button) findViewById(R.id.button5);
        Button aboutUs = (Button) findViewById(R.id.button6);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bookCab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menuscreen.this,bookCabActivity.class);
                startActivity(intent);
            }
        });

        myCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        myTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(menuscreen.this,MyTripList.class);
                startActivity(intent);
            }
        });

        rateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

       aboutUs.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

           }
       });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menuscreen, menu);
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
            Intent intent = new Intent(menuscreen.this,MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
