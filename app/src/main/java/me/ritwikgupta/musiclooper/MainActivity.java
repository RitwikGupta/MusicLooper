package me.ritwikgupta.musiclooper;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nononsenseapps.filepicker.FilePickerActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Uri musicFile;
    MediaPlayer mediaPlayer;
    int REQUEST_DIRECTORY = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Music Looper");

        Button choose = (Button) findViewById(R.id.choose_button);
        Button loop = (Button) findViewById(R.id.loop_button);
        Button stop = (Button) findViewById(R.id.stop_button);
        final EditText startTime = (EditText) findViewById(R.id.start_time);
        final EditText endTime = (EditText) findViewById(R.id.end_time);

        // What happens when the choose button is clicked
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add file select dialog here
                Intent intent = new Intent(getBaseContext(), FilePickerActivity.class);
                startActivityForResult(intent, REQUEST_DIRECTORY);
            }
        });

        // What happens when the loop button is clicked
        loop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Make sure a file is selected, and both times are set
                if(musicFile != null && startTime.getText().toString().length() > 0 && endTime.getText().toString().length() > 0) {
                    final int start = Integer.parseInt(startTime.getText().toString());
                    final int end = Integer.parseInt(endTime.getText().toString());

                    // Do music stuff here
                    if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                        Toast.makeText(getBaseContext(), "Media is already playing", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        mediaPlayer.setDataSource(getApplicationContext(), musicFile);
                    } catch(IOException e) {
                        Toast.makeText(getBaseContext(), "File exists, but isn't working?", Toast.LENGTH_SHORT).show();
                    }

                    if(mediaPlayer != null) {
                        try {
                            mediaPlayer.prepare();
                            mediaPlayer.setLooping(true);
                            mediaPlayer.seekTo(start * 1000);
                            mediaPlayer.start();

                            // Non UI-blocking while loop in AsyncTask
                            new Loop().execute(new Integer[]{start, end});

                        } catch (IOException e){
                            Toast.makeText(getBaseContext(), "Not prepared", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getBaseContext(), musicFile.toString(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "You are missing a step", Toast.LENGTH_SHORT).show();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                } else {
                    Toast.makeText(getBaseContext(), "No music is playing", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getBaseContext(), "Not implemented yet", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_DIRECTORY && resultCode == Activity.RESULT_OK) {
            if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
                ClipData clip = data.getClipData();

                if (clip != null) {
                    for (int i = 0; i < clip.getItemCount(); i++) {
                        musicFile = clip.getItemAt(i).getUri();
                    }
                    Toast.makeText(getBaseContext(), musicFile.toString(), Toast.LENGTH_SHORT).show();
                }
            } else {
                musicFile = data.getData();
                Toast.makeText(getBaseContext(), "Selected file!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class Loop extends AsyncTask<Integer, Void, Void> {

        public Void doInBackground(Integer... params) {

            // params[0] is start
            // params[1] is end

            while(mediaPlayer.isLooping()) {
                if(mediaPlayer.getCurrentPosition() >= params[1] * 1000) {
                    mediaPlayer.seekTo(params[0] * 1000);
                }
            }

            return null;
        }
    }

}
