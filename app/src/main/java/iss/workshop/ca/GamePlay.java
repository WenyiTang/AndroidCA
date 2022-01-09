package iss.workshop.ca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GamePlay extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener{
    private String[] img = {
            "afraid", "full", "hug", "laugh", "peep", "snore"
    };

    private String[] imgs = new String[img.length * 2];
    private String[] cardflipped = new String[2];
    private ImageView[] imgflipped = new ImageView[2];
    private boolean ready = false;
    private Integer matches = 0;
    private Integer triesCount = 0;
    private TextView matchesCount;
    private TextView tries;
    private Button menuBtn, resumeBtn, playAgainBtn, endMainMenuBtn, mainMenuBtn, restartBtn;
    private long pauseTime;
    private Chronometer simpleChronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        menuBtn = findViewById(R.id.menuBtn);
                        resumeBtn = findViewById(R.id.resumeBtn);
                        playAgainBtn = findViewById(R.id.playAgain);
                        mainMenuBtn = findViewById(R.id.toMainMenu);
                        endMainMenuBtn = findViewById(R.id.endMainMenu);
                        restartBtn = findViewById(R.id.restartBtn);
                        simpleChronometer = findViewById(R.id.timerCount);
                        matchesCount = findViewById(R.id.matchesCount);
                        tries = findViewById(R.id.tries);
                    }
                });
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                populateImgs(img, imgs);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CardAdapter adapter = new CardAdapter(GamePlay.this, imgs);

                        GridView gridView = findViewById(R.id.gridView);
                        if (gridView != null) {
                            gridView.setAdapter(adapter);
                            gridView.setOnItemClickListener(GamePlay.this);
                            ready = true;
                            start();
                        }

                        initElements();
                    }
                });
            }
        }).start();
    }

    private void initElements() {
        if (menuBtn != null){
            menuBtn.setOnClickListener(this);
        }
        if (resumeBtn != null){
            resumeBtn.setOnClickListener(this);
        }
        if (playAgainBtn != null){
            playAgainBtn.setOnClickListener(this);
        }
        if (mainMenuBtn != null){
            mainMenuBtn.setOnClickListener(this);
        }
        if (endMainMenuBtn != null){
            endMainMenuBtn.setOnClickListener(this);
        }
        if (restartBtn != null){
            restartBtn.setOnClickListener(this);
        }
        simpleChronometer = findViewById(R.id.timerCount);

        String matchStr = getString(R.string.matches_count, matches, img.length);
        matchesCount.setText(matchStr);

        String triesStr = getString(R.string.tries_count, triesCount);
        tries.setText(triesStr);
    }

    private void populateImgs(String[] img, String[] imgs){
        List<String> shuffledList = new ArrayList<>();
        for (String s : img){
            shuffledList.add(s);
            shuffledList.add(s);
        }
        Collections.shuffle(shuffledList);
        shuffledList.toArray(imgs);
    }


    @Override
    public void onClick(View view) {
        if (view == menuBtn){
            pause();
            ConstraintLayout menu = findViewById(R.id.menuPopup);
            menu.setVisibility(View.VISIBLE);
            ready = false;
        }

        if (view == resumeBtn){
            ConstraintLayout menu = findViewById(R.id.menuPopup);
            menu.setVisibility(View.INVISIBLE);
            resume();
            ready = true;
        }

        if (view == playAgainBtn){
            restartGame();
        }

        if (view == mainMenuBtn || view == endMainMenuBtn){
            //Intent intent = new Intent(this, MainActivity.class);
            //startActivity(intent);

            //Since GamePlay Activity came from MainActivity(without finish()),
            // just need finish() to go back to MainActivity.
            finish();
        }

        if (view == restartBtn){
            restartGame();
        }
    }

    public void restartGame(){
        startActivity(getIntent());
        finish();
        overridePendingTransition(0, 0);
    }

    public void start(){
        simpleChronometer.setBase(SystemClock.elapsedRealtime());
        simpleChronometer.start();
    }

    public void pause(){
        pauseTime = simpleChronometer.getBase() - SystemClock.elapsedRealtime();
        simpleChronometer.stop();
    }

    public void resume(){
        simpleChronometer.setBase(SystemClock.elapsedRealtime() + pauseTime);
        simpleChronometer.start();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ImageView imageView = view.findViewById(R.id.cardImg);

        if (!ready || (imageView.getVisibility() == View.VISIBLE)){
            return;
        }

        if (cardflipped[0] == null){
            move1(imageView, i);
        }
        else if (cardflipped[1] == null){
            move2(imageView, i);

            checkMatch(imageView);
        }

        if (cardflipped[1] != null){
            cardflipped[0] = null;
            cardflipped[1] = null;
        }
    }

    protected void endGame(){
        Chronometer simpleChronometer = findViewById(R.id.timerCount);
        simpleChronometer.stop();
        String timeTaken = simpleChronometer.getContentDescription().toString();

        menuBtn = findViewById(R.id.menuBtn);
        menuBtn.setVisibility(View.INVISIBLE);

        ConstraintLayout endPopup = findViewById(R.id.endGame);
        TextView congrats = findViewById(R.id.congrats);
        String congratsStr = getString(R.string.congrats, img.length, timeTaken, triesCount);
        congrats.setText(congratsStr);
        endPopup.setVisibility(View.VISIBLE);
    }

    protected void move1(ImageView imageView, int i){
        imgflipped[0] = imageView;
        imgflipped[0].setVisibility(View.VISIBLE);
        cardflipped[0] = imgs[i];
    }

    protected void move2(ImageView imageView, int i){
        imgflipped[1] = imageView;
        imgflipped[1].setVisibility(View.VISIBLE);
        cardflipped[1] = imgs[i];
        TextView tries = findViewById(R.id.tries);
        triesCount++;
        String triesStr = getString(R.string.tries_count, triesCount);
        tries.setText(triesStr);
    }

    protected void checkMatch(ImageView imageView){
        if (cardflipped[0].equals(cardflipped[1])){
            matches++;

            TextView matchesCount = findViewById(R.id.matchesCount);
            String matchStr = getString(R.string.matches_count, matches, img.length);
            matchesCount.setText(matchStr);

            if (matches == img.length) {
                endGame();
            }
        }
        else{
            turnCardBack(imageView);
        }
    }

    protected void turnCardBack(ImageView imageView){
        ready = false;
        imageView.postDelayed(() -> {
            imgflipped[0].setVisibility(View.INVISIBLE);
            imgflipped[1].setVisibility(View.INVISIBLE);
            ready = true;
        }, 500);
    }
}