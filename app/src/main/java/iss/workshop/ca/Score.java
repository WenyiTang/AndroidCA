package iss.workshop.ca;

public class Score {

    private int attempts;
    private String timeTaken;

    public Score(int attempts, String timeTaken) {
        this.attempts = attempts;
        this.timeTaken = timeTaken;
    }

    public int getAttempts() {
        return attempts;
    }

    public String getTimeTaken() {
        return timeTaken;
    }
}