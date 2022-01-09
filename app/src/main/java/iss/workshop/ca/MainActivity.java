package iss.workshop.ca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button startBtn, guideBtn;
    private MediaPlayer mediaPlayer;
    public static boolean continueMusic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startBtn = findViewById(R.id.playGame);
        startBtn.setOnClickListener(this);

        guideBtn = findViewById(R.id.guideBtn);
        guideBtn.setOnClickListener(this);

        //playBackground();
    }

    /*protected void playBackground(){
        if (mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.background);
            if (!mediaPlayer.isPlaying()){
                //isPlayingAudio=true;
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        }

    }*/


    @Override
    protected void onPause() {
        super.onPause();
        if (!continueMusic){
            MusicManager.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        continueMusic=true;
        MusicManager.start(this,MusicManager.MUSIC_BACKGROUND);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicManager.release();
    }

    @Override
    public void onClick(View view) {
        if (view == startBtn){
            Intent intent = new Intent(this, GamePlay.class);
            startActivity(intent);
        }

        if (view == guideBtn){
            Intent intent = new Intent(this, Guide.class);
            startActivity(intent);
        }

    }
}