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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static android.speech.SpeechRecognizer.isRecognitionAvailable;

public class ListeningScreen extends AppCompatActivity {

    // Some SpeechRecognizer methods we might need:
    // void cancel(); cancels the speech recognition
    // void destroy(); destroys the speechrecognizer object

    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening_screen);

        // create a new text to speech object
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }
                    // this is how we can have audio spoken, we just need to move it to an on-click listener
                    // and we need to give the user the ability to type in text and then press some button to
                    // say the text aloud. If you start the app now, this will be spoken first thing.
                    speak("Hello");

                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });

        // what do we want to happen when someone clicks on the headphones? here is a first attempt
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

    // we will use this to speak what someone types in
    private void speak(String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void listen(){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");

        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(ListeningScreen.this, "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(ListeningScreen.this, "Results Received, Processing for some reason", Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String inSpeech = res.get(0);
                recognition(inSpeech);
            }
        }
    }

    private void recognition(String text){
        Log.e("Speech",""+text);
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
