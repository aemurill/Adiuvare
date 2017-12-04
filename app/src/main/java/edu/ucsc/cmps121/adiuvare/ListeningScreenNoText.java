package edu.ucsc.cmps121.adiuvare;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.io.IOException;


public class   ListeningScreenNoText extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Adiuvare Listens 2";
    private String filename = "Adiuvare";
    private boolean state = true;
    boolean m_isRun;
    private ImageView ledIndicator;
    Dialog helpDialog;


    private Thread m_thread;

    // we need audio buffer (?) so we can write to the buffer and read from same buffer, almost immediately
    // this is where the delay would be unavoidable, but potentially unnoticable if read quickly enough?

    @Override
    public void onClick(View v) {
        Log.i(TAG, "A CLICK");
        if (v.getId() == R.id.amplifyAudio) {
            Log.i(TAG, "Clicked");
            if(state) {
                requestRecordAudioPermission();
                Toast.makeText(this, "CLICKED: "+state , Toast.LENGTH_LONG).show();
                ledIndicator.setImageResource(R.mipmap.lighton);
                m_thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        Log.i(TAG, "Doing loopBack");
                        loopBack();
                    }
                });
                m_thread.start();
                state = false;
            }else{
                state = true;
                m_isRun = false;
                ledIndicator.setImageResource(R.mipmap.lightoff);
            }
        }
        else if (v.getId() == R.id.textClose) {
            helpDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Entered Speaking Amplification Screen");

        setContentView(R.layout.activity_listening_screen_only);
        ImageButton speakButton = (ImageButton) findViewById(R.id.amplifyAudio);
        ledIndicator = (ImageView) findViewById(R.id.ledIndicator);
        speakButton.setOnClickListener(this);

        helpDialog = new Dialog(this);


        Log.i(TAG, "Making Thread");
    }

    public void showPopup(View v) {
        helpDialog.setContentView(R.layout.how_to_use_popup);
        TextView textClose = (TextView) helpDialog.findViewById(R.id.textClose);
        textClose.setOnClickListener(this);
        /*textClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpDialog.dismiss();
            }
        });*/
        helpDialog.show();
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

    public void loopBack(){
        int bufferSize = 0;
        int SAMPLE_RATE = 32000;
        AudioRecord m_record;
        AudioTrack m_track;
        byte[] buffer;
        m_isRun = true;
        Log.i(TAG, "loopBack start");
        try {
            bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);

//          if (bufferSize <= BUF_SIZE) {
//              bufferSize = BUF_SIZE;
//          }

            Log.i("LOG","Initializing Audio Record and Audio Playing objects");

            m_record = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, SAMPLE_RATE, AudioFormat.CHANNEL_CONFIGURATION_MONO
                    , AudioFormat.ENCODING_PCM_16BIT, bufferSize * 1);

            m_track = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_CONFIGURATION_MONO
                    , AudioFormat.ENCODING_PCM_16BIT, bufferSize * 1
                    , AudioTrack.MODE_STREAM);

            m_track.setPlaybackRate(SAMPLE_RATE);

            m_record.startRecording();
            m_track.play();
            Log.i("LOG","Audio Recording started");


            Log.i("LOG","Audio Playing started");

            buffer = new byte[bufferSize];

            while (m_isRun){
                Log.i("LOG","Audio playing?");
                m_record.read(buffer, 0, bufferSize);
                m_track.write(buffer, 0, buffer.length);
            }
            m_record.stop();
            m_track.stop();
            m_record.release();
            m_track.release();
            Log.i("LOG", "loopback exit");

        } catch (Exception e) {
            // TODO: handle exception
        }
        Log.i(TAG, "loopBack end");
    }

    @Override
    protected void onDestroy(){
        m_isRun = false;

        super.onDestroy();
    }
}
