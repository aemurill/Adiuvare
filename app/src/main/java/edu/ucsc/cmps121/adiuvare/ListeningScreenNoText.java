package edu.ucsc.cmps121.adiuvare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.IOException;


public class ListeningScreenNoText extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Adiuvare Listens 2";
    private String filename = "Adiuvare";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening_screen_only);
    }

    // we need audio buffer (?) so we can write to the buffer and read from same buffer, almost immediately
    // this is where the delay would be unavoidable, but potentially unnoticable if read quickly enough?
    public void onClick(View v) {
        requestRecordAudioPermission();

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

}
