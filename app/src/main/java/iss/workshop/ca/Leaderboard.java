package iss.workshop.ca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Leaderboard extends AppCompatActivity {

    private int[] topAttempts = new int[5];
    private String[] topTime = new String[5];
    private int mostRecentAttempts;
    private String mostRecentTimeTaken;
    List<Score> topScores = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        SharedPreferences pref = getSharedPreferences("Scores", MODE_PRIVATE);

        retrieveTopFiveScores(pref);

        mostRecentAttempts = pref.getInt("attemptsNew", 0);
        mostRecentTimeTaken = pref.getString("timeTakenNew", "0 seconds");
        Score newScore = new Score(mostRecentAttempts, mostRecentTimeTaken);

        topScores = compareAndUpdateScores(topScores, newScore, pref);

        clearData(pref);

        ScoreAdapter scoreAdapter = new ScoreAdapter(this, topScores);
        ListView scoreListView = findViewById(R.id.scoreListView);
        if (scoreListView != null) {
            scoreListView.setAdapter(scoreAdapter);
        }
    }

    private void retrieveTopFiveScores(SharedPreferences pref) {
        topAttempts[0] = pref.getInt("topAttempts1", 0);
        topAttempts[1] = pref.getInt("topAttempts2", 0);
        topAttempts[2] = pref.getInt("topAttempts3", 0);
        topAttempts[3] = pref.getInt("topAttempts4", 0);
        topAttempts[4] = pref.getInt("topAttempts5", 0);

        topTime[0] = pref.getString("topTime1", "0 seconds");
        topTime[1] = pref.getString("topTime2", "0 seconds");
        topTime[2] = pref.getString("topTime3", "0 second");
        topTime[3] = pref.getString("topTime4", "0 seconds");
        topTime[4] = pref.getString("topTime5", "0 seconds");

        Score score1 = new Score(topAttempts[0], topTime[0]);
        Score score2 = new Score(topAttempts[1], topTime[1]);
        Score score3 = new Score(topAttempts[2], topTime[2]);
        Score score4 = new Score(topAttempts[3], topTime[3]);
        Score score5 = new Score(topAttempts[4], topTime[4]);

        topScores.add(score1);
        topScores.add(score2);
        topScores.add(score3);
        topScores.add(score4);
        topScores.add(score5);
    }

    private List<Score> compareAndUpdateScores(List<Score> topScores, Score newScore,
                                               SharedPreferences pref) {
        if (newScore.getAttempts() != 0 || !(newScore.getTimeTaken().equals("0 seconds"))) {
            topScores.add(newScore);
            Collections.sort(topScores,
                    new Comparator<Score>() {
                        @Override
                        public int compare(Score scoreA, Score scoreB) {
                            if (scoreA.getAttempts() == 0) {
                                return 1;
                            }
                            if (scoreB.getAttempts() == 0) {
                                return -1;

                            } else if (scoreA.getAttempts() < scoreB.getAttempts()) {
                                return -1;

                            } else if (scoreA.getAttempts() == scoreB.getAttempts()) {

                                String scoreATime = scoreA.getTimeTaken();
                                String[] scoreATimeUnit = scoreATime.split(" ");
                                Integer scoreATotalTime = Integer.parseInt(scoreATimeUnit[0]);

                                String scoreBTime = scoreB.getTimeTaken();
                                String[] scoreBTimeUnit = scoreBTime.split(" ");
                                Integer scoreBTotalTime = Integer.parseInt(scoreBTimeUnit[0]);

                                if (scoreATotalTime.compareTo(scoreBTotalTime) < 0) {
                                    return -1;
                                }
                                else if (scoreATotalTime.compareTo(scoreBTotalTime) > 0) {
                                    return 1;
                                }
                                else {
                                    return 0;
                                }
                            } else {
                                return 1;
                            }
                        }
                    });
            topScores.remove(topScores.size() - 1);

            SharedPreferences.Editor editor = pref.edit();
            for (int i = 1; i <= topScores.size(); i++) {
                editor.putInt("topAttempts" + i, topScores.get(i-1).getAttempts());
                editor.putString("topTime" + i, topScores.get(i-1).getTimeTaken());
                editor.commit();
            }
        }

        return topScores;
    }

    private void clearData(SharedPreferences pref) {
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("attemptsNew");
        editor.remove("timeTakenNew");
        editor.commit();
    }

    /*
    public static Duration parseDuration(String durStr) {
        String isoString = durStr.replaceFirst("^(\\d{1,2}):(\\d{2})$", "PT$1M$2S");
        return Duration.parse(isoString);
    }
    */
}