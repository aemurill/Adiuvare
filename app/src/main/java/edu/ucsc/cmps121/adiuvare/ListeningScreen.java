package edu.ucsc.cmps121.adiuvare;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Locale;

import static android.speech.SpeechRecognizer.isRecognitionAvailable;

public class ListeningScreen extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening_screen);



        // what do we want to happen when someone clicks on the headphones (currently an ear)? here is a first attempt
        findViewById(R.id.listenButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do we want a new speech recognizer object each time?
                // not sure if this works, but should check to see if this is available on their android system
                boolean available = isRecognitionAvailable(ListeningScreen.this);
                if (available) {
                    // then try to recognize speech
                    Toast.makeText(ListeningScreen.this, "Trying to listen!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ListeningScreen.this, "Sorry, your phone does not have speech recognition capabilities", Toast.LENGTH_LONG).show();
                }
                listen(); //<- this was a suggested method from an online tutorial
            }
        });

    }

    private void listen(){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Listening");

        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(ListeningScreen.this, "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(ListeningScreen.this, "Results Received", Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String inSpeech = res.get(0);
                recognition(inSpeech);
            }
        }
    }

    private void recognition(String text){
        Log.e("Speech",""+text);
    }


}
