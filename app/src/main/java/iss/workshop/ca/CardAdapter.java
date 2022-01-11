package iss.workshop.ca;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CardAdapter extends /*ArrayAdapter<Object>*/ BaseAdapter {

    private final Context context;
   //protected String[] imgs;
    protected ArrayList<Picture> pics;

//    public CardAdapter(Context context, String[] imgs){
//        super(context, R.layout.card_item);
//        this.context = context;
//        this.imgs = imgs;
//
//        addAll(new Object[imgs.length]);
//    }

//        public CardAdapter(Context context, String[] imgs){
//        super(context, R.layout.card_item);
//        this.context = context;
//        this.imgs = imgs;
//
//        addAll(new Object[imgs.length]);
//    }

//    public CardAdapter(Context context, ArrayList<Picture> pics){
//        super(context, R.layout.card_item);
//        this.context = context;
//        this.pics = pics;
//
//        addAll(new Object[pics.size()]);
//    }

    public CardAdapter(Context context, ArrayList<Picture> pics){
        this.context = context;
        this.pics = pics;
    }

    @Override
    public int getCount() {
        return pics.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public View getView(int pos, View view, @NonNull ViewGroup parent){
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.card_item, null);
        }

        ImageView imageView = view.findViewById(R.id.cardImg);
        imageView.setTag(pics.get(pos));
        imageView.setImageBitmap(pics.get(pos).getBitmap());
        imageView.setVisibility(View.INVISIBLE);

        return view;
    }
}
