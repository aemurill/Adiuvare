package edu.ucsc.cmps121.adiuvare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
}