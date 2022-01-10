package iss.workshop.ca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Leaderboard extends AppCompatActivity implements View.OnClickListener{

    private Button returnBtn, clearBtn, normalHistory, hardHistory;
    private TextView difficulty;
    private int[] topAttempts = new int[5];
    private String[] topTime = new String[5];
    List<Score> topScores = new ArrayList<>();
    List<String> attemptStrings = new ArrayList<>();
    List<String> timeStrings = new ArrayList<>();
    private final int numOfRanks = 5;
    private String scoresMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        for (int i = 1; i <= numOfRanks; i++){
            attemptStrings.add("topAttempts" + i);
            timeStrings.add("topTime" + i);
        }

        Intent intent = getIntent();
        scoresMode = intent.getStringExtra("diff");

        SharedPreferences pref = getSharedPreferences(scoresMode, MODE_PRIVATE);

        if (!pref.contains("topAttempts1")){
            resetLeaderboard(scoresMode);
            pref = getSharedPreferences(scoresMode, MODE_PRIVATE);
        }

        retrieveTopFiveScores(pref);

        ScoreAdapter scoreAdapter = new ScoreAdapter(this, topScores);
        ListView scoreListView = findViewById(R.id.scoreListView);
        if (scoreListView != null) {
            scoreListView.setAdapter(scoreAdapter);
        }

        returnBtn = findViewById(R.id.returnBtn);
        returnBtn.setOnClickListener(this);
        clearBtn = findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(this);
        normalHistory = findViewById(R.id.normalHistory);
        normalHistory.setOnClickListener(this);
        hardHistory = findViewById(R.id.hardHistory);
        hardHistory.setOnClickListener(this);
        difficulty = findViewById(R.id.difficulty);
        difficulty.setText("Difficulty: Normal");
    }

    private void retrieveTopFiveScores(SharedPreferences pref) {

        for (int j = 0; j < numOfRanks; j++){
            topAttempts[j] = pref.getInt(attemptStrings.get(j), 0);
            topTime[j] = pref.getString(timeStrings.get(j), "0 seconds");
            Score score = new Score(topAttempts[j], topTime[j]);
            topScores.add(score);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == normalHistory){
            scoresMode = "normal";
            reload();
            difficulty.setText("Difficulty: Normal");
        }

        if (view == hardHistory){
            scoresMode = "hard";
            reload();
            difficulty.setText("Difficulty: Hard");
        }

        if (view == returnBtn) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        if (view == clearBtn){
            resetLeaderboard(scoresMode);
            reload();
        }
    }

    /*
    public static Duration parseDuration(String durStr) {
        String isoString = durStr.replaceFirst("^(\\d{1,2}):(\\d{2})$", "PT$1M$2S");
        return Duration.parse(isoString);
    }
    */

    public void resetLeaderboard(String scoresMode){

        SharedPreferences pref = getSharedPreferences(scoresMode, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        for (int j = 0; j < numOfRanks; j++){
            editor.putInt(attemptStrings.get(j), 0);
            editor.putString(timeStrings.get(j), "0 seconds");
        }

        editor.commit();
    }

    public void reload(){
        Intent intent = new Intent(this, Leaderboard.class);
        intent.putExtra("diff", scoresMode);
        startActivity(intent);
        finish();
        overridePendingTransition(0,0);
    }
}