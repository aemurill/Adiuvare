package edu.ucsc.cmps121.adiuvare;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ListeningScreen extends AppCompatActivity implements View.OnClickListener {

    private TextView mText;
    private SpeechRecognizer sr;
    //used to control volume throughout class
    private float leftVolume = 0.4f;
    private float rightVolume = 0.4f;
    private static final String TAG = "Adiuvare Listens";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening_screen);

        // declare our two views: the image button users click and the location where the text will be displayed
        ImageButton speakButton = (ImageButton) findViewById(R.id.listenButton);
        mText = (TextView) findViewById(R.id.displayVocal);

        //volume control buttons
        Button volumeUpButton = (Button) findViewById(R.id.Plus);
        Button volumeDownButton = (Button) findViewById(R.id.Minus);

        volumeUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseVolume();
            }
        });

        volumeDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseVolume();
            }
        });

        // set the listener for the speak button and create new speech recognizer
        speakButton.setOnClickListener(this);
    }

    // on click, check for permissions (again) and then begin listening as a new intent
    public void onClick(View v) {
        if (v.getId() == R.id.listenButton) {
            startSpeechRecognition();
        }
    }

    //functions increase both left and right sides same amount for now
    //0.2 increment is arbitrary
    public void increaseVolume() {
        if (leftVolume < 1.0f && rightVolume < 1.0f) {
            leftVolume += 0.2f;
            rightVolume += 0.2f;
            Toast.makeText(ListeningScreen.this, ((String) ("Volume: " + leftVolume)), Toast.LENGTH_LONG).show();
        }
        else Toast.makeText(ListeningScreen.this, "Max Volume", Toast.LENGTH_LONG).show();
    }

    public void decreaseVolume() {
        if (leftVolume > 0.2f && rightVolume > 0.2f) {
            leftVolume -= 0.2f;
            rightVolume -= 0.2f;
            Toast.makeText(ListeningScreen.this, ((String) ("Volume: " + leftVolume)), Toast.LENGTH_LONG).show();
        }
        else Toast.makeText(ListeningScreen.this, "Min Volume", Toast.LENGTH_LONG).show();
    }

    public void startSpeechRecognition() {
        // Fire an intent to start the speech recognition activity.
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);
        // secret parameters that when added provide audio url in the result
        intent.putExtra("android.speech.extra.GET_AUDIO_FORMAT", "audio/AMR");
        intent.putExtra("android.speech.extra.GET_AUDIO", true);

        startActivityForResult(intent, 121);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 121 && resultCode == RESULT_OK) {
            // the resulting text is in the getExtras:
            Bundle bundle = data.getExtras();
            ArrayList<String> matches = bundle.getStringArrayList(RecognizerIntent.EXTRA_RESULTS);

            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            // the recording uri is in getData:
            String str = "";
            Log.d(TAG, "onResults " + results);
            ArrayList list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            for (int i = 0; i < list.size(); i++) {
                Log.d(TAG, "result " + list.get(i));
                str += list.get(i);
            }
            mText.setText(mText.getText() + "\n" + list.get(0)); // append new results underneath for now
            //stopButton instantiated here bc it will only appear when something is playing
            final Button stopButton = (Button) findViewById(R.id.Stop);

            Uri audioUri = data.getData();
            final MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                // mediaPlayer.setDataSource(String.valueOf(myUri));
                mediaPlayer.setDataSource(this, audioUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            mediaPlayer.setVolume(leftVolume,rightVolume);
            //makes stop button visible once sound plays
            stopButton.setVisibility(View.VISIBLE);
            //If stop button is clicked then sound stops and button is invisible again
            stopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopPlayback(mediaPlayer,stopButton);
                }
            });
            //if there is no sound then stop button is invisible again
            //DOESN'T WORK AS INTENDED YET
            if (mediaPlayer.isPlaying()) stopButton.setVisibility(View.VISIBLE);


        }
    }

    //method to stop sound created for easy reading
    public void stopPlayback(MediaPlayer mp, Button b) {
        if (mp.isPlaying()) {
            mp.stop();
            b.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}