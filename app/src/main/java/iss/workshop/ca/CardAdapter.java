package iss.workshop.ca;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;

public class CardAdapter extends ArrayAdapter<Object> {

    private final Context context;
    protected String[] imgs;

    public CardAdapter(Context context, String[] imgs){
        super(context, R.layout.card_item);
        this.context = context;
        this.imgs = imgs;

        addAll(new Object[imgs.length]);
    }

    public View getView(int pos, View view, @NonNull ViewGroup parent){
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.card_item, parent, false);
        }

        ImageView imageView = view.findViewById(R.id.cardImg);
        int id = context.getResources().getIdentifier(imgs[pos], "drawable", context.getOpPackageName());
        imageView.setImageResource(id);
        imageView.setVisibility(View.INVISIBLE);



        return view;
    }
}
