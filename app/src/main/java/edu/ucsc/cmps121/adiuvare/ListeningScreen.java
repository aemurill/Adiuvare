package edu.ucsc.cmps121.adiuvare;

import android.content.ActivityNotFoundException;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Locale;

import static android.speech.RecognizerIntent.getVoiceDetailsIntent;
import static android.speech.SpeechRecognizer.createSpeechRecognizer;
import static android.speech.SpeechRecognizer.isRecognitionAvailable;

public class ListeningScreen extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening_screen);

        // code to execute when someone selects the ear
        findViewById(R.id.listenButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean available = isRecognitionAvailable(ListeningScreen.this);
                if (available) {
                    // then notify & try to recognize speech
                    Toast.makeText(ListeningScreen.this, "Begin Listening", Toast.LENGTH_SHORT).show();
                    listen();
                } else {
                    Toast.makeText(ListeningScreen.this, "Sorry, your phone does not have speech recognition capabilities", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void listen(){
        //SpeechRecognizer listening = createSpeechRecognizer(this);
        //RecognitionListener test = listening.getVoiceDetailsIntent(this);
        //listening.startListening(test);
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US"); // for english only
        //i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        //i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Listening");

        try {
            startActivityForResult(i, 1);
            //displayResults.setText("");
        } catch (ActivityNotFoundException a) {
            Toast.makeText(ListeningScreen.this, "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(ListeningScreen.this, "Results Received", Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            Toast.makeText(ListeningScreen.this, "FOUND RQC 1", Toast.LENGTH_SHORT).show();
            if (null != data){ // not result_ok
                Toast.makeText(ListeningScreen.this, "Data is not null", Toast.LENGTH_SHORT).show();
            }else if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String inSpeech = res.get(0);
                Log.e("Speech",""+inSpeech);
                TextView displayResults = (TextView) findViewById(R.id.displayVocal);
                displayResults.setText(inSpeech);

            } else {
                TextView displayResults = (TextView) findViewById(R.id.displayVocal);
                displayResults.setText("Something didn't work");
            }
        }
    }

    private void recognition(String text){
        Log.e("Speech",""+text);
    }

    // if voice recognition begins, display cancel recognition symbol and then execute .cancel() method


}
