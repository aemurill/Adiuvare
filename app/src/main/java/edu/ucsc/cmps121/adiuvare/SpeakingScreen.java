package edu.ucsc.cmps121.adiuvare;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;

/**
 * Created by MadelineConner on 11/12/17.
 */

public class SpeakingScreen extends AppCompatActivity {

    private TextToSpeech tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaking_screen);

        Button SpeakButton = (Button) findViewById(R.id.Speak);

        // create a new text to speech object
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }

                } else {
                    Log.e("TTS", "Initialization Failed!");
                }
            }
        });


        SpeakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getText();
            }
        });

    }

    // gets text from user input and is spoken as soon as the "SPEAK" button is selected
    public void getText() {
        EditText textInput = (EditText) findViewById(R.id.TextInput);
        String text = textInput.getText().toString();
        speak(text);
    }

    // we will use this to speak what someone types in
    private void speak(String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            // https://developer.android.com/reference/android/speech/tts/TextToSpeech.html
            // 'This method was deprecated in API level 21. As of API level 21, replaced by speak(CharSequence, int, Bundle, String).'
            // We likely shouldn't use the replacement method, since our minimum API level is 16.
            // Ignore the error Android Studio keeps highlighting, ignore this comment
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
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
