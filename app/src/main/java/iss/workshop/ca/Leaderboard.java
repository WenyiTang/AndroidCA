package iss.workshop.ca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Leaderboard extends AppCompatActivity {

    private int topAttempts1, topAttempts2, topAttempts3, topAttempts4, topAttempts5;
    private String topTime1, topTim2, topTim3, topTime4, topTime5
    private int mostRecentAttempts;
    private String mostRecentTimeTaken;
    List<SharedPreferences> topFive = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        SharedPreferences pref = getSharedPreferences("newScore", MODE_PRIVATE);
        mostRecentAttempts = pref.getInt("attempts", 0);
        mostRecentTimeTaken = pref.getString("timeTaken", "");



        ScoreAdapter scoreAdapter = new ScoreAdapter(this,);
        ListView scoreListView = findViewById(R.id.scoreListView);
        if (scoreListView != null) {
            scoreListView.setAdapter(scoreAdapter);
        }
    }
}