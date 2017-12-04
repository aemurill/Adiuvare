package edu.ucsc.cmps121.adiuvare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout speak = (LinearLayout)findViewById(R.id.speak_it);
        speak.setOnClickListener(sListener);

        LinearLayout hear = (LinearLayout)findViewById(R.id.hear_it);
        hear.setOnClickListener(hListener);

        LinearLayout read = (LinearLayout)findViewById(R.id.read_it);
        read.setOnClickListener(rListener);
    }

    protected void openSpeaking(View view) {
        Intent intent = new Intent(this, SpeakingScreen.class);
        startActivity(intent);
    }

    protected void openListening(View view) {
        Intent intent = new Intent(this, ListeningScreen.class);
        startActivity(intent);
    }

    protected void openStrictlyListening(View view){
        Intent intent = new Intent(this, ListeningScreenNoText.class);
        startActivity(intent);
    }

    private OnClickListener sListener = new OnClickListener() {
            public void onClick(View v) {
                openSpeaking(v);
            }
    };

    private OnClickListener hListener = new OnClickListener() {
        public void onClick(View v) {
            openStrictlyListening(v);
        }
    };

    private OnClickListener rListener = new OnClickListener() {
        public void onClick(View v) {
            openListening(v);
        }
    };

}