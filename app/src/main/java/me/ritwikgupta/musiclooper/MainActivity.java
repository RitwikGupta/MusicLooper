package me.ritwikgupta.musiclooper;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Uri musicFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button choose = (Button) findViewById(R.id.choose_button);
        Button loop = (Button) findViewById(R.id.loop_button);
        final EditText startTime = (EditText) findViewById(R.id.start_time);
        final EditText endTime = (EditText) findViewById(R.id.end_time);

        // What happens when the choose button is clicked
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add file select dialog here
            }
        });

        // What happens when the loop button is clicked
        loop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Make sure a file is selected, and both times are set
                if(musicFile != null && Integer.parseInt(startTime.getText().toString()) > 0 && Integer.parseInt(endTime.getText().toString()) > 0) {
                    // Do music stuff here
                } else {
                    Toast.makeText(getBaseContext(), "You are missing a step", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
}
