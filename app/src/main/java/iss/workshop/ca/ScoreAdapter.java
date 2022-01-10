package iss.workshop.ca;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ScoreAdapter extends ArrayAdapter<Object> {

    private final Context context;


    public ScoreAdapter(Context context, ArrayList<Rank> rankings) {
        super(context, R.layout.scorerow);
        this.context = context;
        this.rankings = rankings;
    }
}
