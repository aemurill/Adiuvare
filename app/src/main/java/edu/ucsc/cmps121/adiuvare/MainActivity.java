package edu.ucsc.cmps121.adiuvare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Adiuvare Begins";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // request permissions as soon as we get to this screen
        requestRecordAudioPermission();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void openSpeaking(View view) {
        Intent intent = new Intent(this, SpeakingScreen.class);
        startActivity(intent);
    }

    protected void openListening(View view) {
        Intent intent = new Intent(this, ListeningScreen.class);
        startActivity(intent);
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
            requiredPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            // If the permission is not permitted already, request permissions again
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                Log.d(TAG, "Write Permissions Denied");
                requestPermissions(new String[]{requiredPermission}, 102);
            }
        }
    }
}