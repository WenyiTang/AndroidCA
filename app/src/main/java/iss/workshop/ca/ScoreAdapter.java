package iss.workshop.ca;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ScoreAdapter extends ArrayAdapter<Object> {

    private final Context context;
    private List<Score> topScores;


    public ScoreAdapter(Context context, List<Score> topScores) {
        super(context, R.layout.scorerow);
        this.context = context;
        this.topScores = topScores;

        addAll(new Object[topScores.size()]);
    }

    public View getView(int pos, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.scorerow, parent, false);
        }

        TextView rankView = view.findViewById(R.id.rank);
        int rank = pos + 1;
        rankView.setText("Rank: " + rank);

        TextView attemptView = view.findViewById(R.id.attempts);
        attemptView.setText("Attempts: " + topScores.get(pos).getAttempts());

        TextView timeTakenView = view.findViewById(R.id.timeTaken);
        timeTakenView.setText("Time Taken: " + topScores.get(pos).getTimeTaken());

        return view;
    }
}
