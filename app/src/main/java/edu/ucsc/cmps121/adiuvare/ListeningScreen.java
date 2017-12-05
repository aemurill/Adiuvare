package edu.ucsc.cmps121.adiuvare;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class ListeningScreen extends AppCompatActivity implements View.OnClickListener {

    private TextView mText;
    private SpeechRecognizer sr;
    private static final String TAG = "Adiuvare Listens";

    Dialog helpDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening_screen);

        // request permissions as soon as we get to this screen
        requestRecordAudioPermission();

        // declare our two views: the image button users click and the location where the text will be displayed
        ImageButton speakButton = (ImageButton) findViewById(R.id.listenButton);
        mText = (TextView) findViewById(R.id.displayVocal);

        // set the listener for the speak button and create new speech recognizer
        speakButton.setOnClickListener(this);
        sr = SpeechRecognizer.createSpeechRecognizer(this);
        sr.setRecognitionListener(new listener());
    }


    class listener implements RecognitionListener {
        // need a method for each to implement the recognition listener
        public void onReadyForSpeech(Bundle params) {
            Log.d(TAG, "onReadyForSpeech");
        }
        public void onBeginningOfSpeech() {
            Log.d(TAG, "onBeginningOfSpeech");
        }
        public void onRmsChanged(float rmsdB) {
            Log.d(TAG, "onRmsChanged");
        }
        // buffer might also be a good use of putting text as it is interpreted in phase 2
        public void onBufferReceived(byte[] buffer) {
            Log.d(TAG, "onBufferReceived");
        }
        public void onEndOfSpeech() {
            Log.d(TAG, "onEndofSpeech");
        } // TO-DO ADD THE RECORDING LIGHT HERE
        public void onError(int error) {
            Log.d(TAG,  "error " +  error);
        }

        // right now just logging the first result, but in the next phase we can check the confidence
        // of each result and then just log (display) the result with the highest confidence level!
        public void onResults(Bundle results) {
            Log.d(TAG, "onResults " + results);
            ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for (int i = 0; i < data.size(); i++)
            {
                Log.d(TAG, "result " + data.get(i));
            }
            mText.setText(mText.getText() + "\n" + data.get(0)); // append new results underneath for now
        }
        // later (phase 2) we might want to use this to display results in real time, & append current string,
        // then we would want the textview to be scrollable and appendable!
        public void onPartialResults(Bundle partialResults) {
            Log.d(TAG, "onPartialResults");
        }
        public void onEvent(int eventType, Bundle params) {
            Log.d(TAG, "onEvent " + eventType);
        }
    }

    // on click, check for permissions (again) and then begin listening as a new intent
    public void onClick(View v) {
        if (v.getId() == R.id.listenButton) {
            requestRecordAudioPermission();
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);
            sr.startListening(intent);
        }
    }

    // Need permission from the user to access their audio
    private void requestRecordAudioPermission() {
        Log.d(TAG, "Trying to request permissions");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String requiredPermission = Manifest.permission.RECORD_AUDIO;
            // If the permission is not permitted already, request permissions again
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                Log.d(TAG, "Audio Permissions Denied");
                requestPermissions(new String[]{requiredPermission}, 101);
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        sr.destroy();
    }
}