package iss.workshop.ca;

import android.content.SharedPreferences;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortScore {

    private int[] topAttempts = new int[5];
    private String[] topTime = new String[5];
    private int mostRecentAttempts;
    private String mostRecentTimeTaken;
    List<Score> topScores = new ArrayList<>();
    List<String> attemptStrings = new ArrayList<>();
    List<String> timeStrings = new ArrayList<>();
    private final int numOfRanks = 5;

    public void updateScoreboard(SharedPreferences pref){
        retrieveTopFiveScores(pref);

        mostRecentAttempts = pref.getInt("attemptsNew", 0);
        mostRecentTimeTaken = pref.getString("timeTakenNew", "0 seconds");
        Score newScore = new Score(mostRecentAttempts, mostRecentTimeTaken);

        topScores = compareAndUpdateScores(topScores, newScore, pref);

        clearData(pref);
    }

    private void retrieveTopFiveScores(SharedPreferences pref) {
        for (int i = 1; i <= numOfRanks; i++){
            attemptStrings.add("topAttempts" + i);
            timeStrings.add("topTime" + i);
        }

        for (int j = 0; j < numOfRanks; j++){
            topAttempts[j] = pref.getInt(attemptStrings.get(j), 0);
            topTime[j] = pref.getString(timeStrings.get(j), "0 seconds");
            Score score = new Score(topAttempts[j], topTime[j]);
            topScores.add(score);
        }

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
}
