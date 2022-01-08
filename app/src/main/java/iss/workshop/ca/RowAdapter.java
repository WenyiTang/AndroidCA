package iss.workshop.ca;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RowAdapter extends RecyclerView.Adapter<RowAdapter.MyViewHolder> {

    int[] picturesArr;


    public interface ItemSelectedChangeListener{
        void onItemSelectedChange(int number);
    }
    private RowAdapter.ItemSelectedChangeListener listener;

    public List<Picture> pictures;
    private Context context;
    private List<Picture> picturesSelected;
    int count = 0;


    public RowAdapter(int[] picturesArr) {
        this.picturesArr = picturesArr;
    }


    public RowAdapter(Context context, List<Picture> pictures, RowAdapter.ItemSelectedChangeListener listener) {
        this.context = context;
        this.pictures = pictures;
        this.listener=listener;
        this.picturesSelected = new ArrayList<>();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picturerow,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        // set value to imageView  and TextView
        holder.bind(pictures.get(position), position);

//        holder.imageView.setImageResource(picturesArr[position]);
//        holder.textView.setText("1");

    }


    @Override
    public int getItemCount() {
        return pictures.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        ConstraintLayout constraintLayoutItem_image;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewPictureItem);
            textView = itemView.findViewById(R.id.item_image_slect_count);
            constraintLayoutItem_image = itemView.findViewById(R.id.item_image);
        }

        public void bind(final Picture picture, final int position) {

//            imageView.setImageResource(Integer.parseInt(picture.getPath()));

            imageView.setBackgroundResource(Integer.parseInt(picture.getPath()));


            if (pictures.size() >= 20){
                textView.setVisibility(View.VISIBLE);
            }else {
                textView.setVisibility(View.INVISIBLE);
            }

            if (picture.getSelectCount() > 0) {
                textView.setText(picture.getSelectCount() + "");
               // textView.setBackground(context.getResources().getDrawable(R.drawable.background_count_selected));
                textView.setBackgroundResource(R.drawable.background_count_selected);
            } else {
                textView.setText("");
                //textView.setBackground(context.getResources().getDrawable(R.drawable.background_count_not_selected));
                textView.setBackgroundResource(R.drawable.background_count_not_selected);
            }

            constraintLayoutItem_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (pictures.size() >= 20){

                        int removeSelectCount;
                        picture.setPosition(position);
                        if (picture.getSelectCount() > 0) {
                            count--;
                            textView.setText("");
                            textView.setBackground(context.getResources().getDrawable(R.drawable.background_count_not_selected));
                            //remove object from hashmap
                            //picturesSelectedMap.remove(picture);

                            picturesSelected.remove(picture);

                            for (Picture pictureUpdate : pictures) {
                                if (pictureUpdate.getSelectCount() > picture.getSelectCount()) {
                                    pictureUpdate.setSelectCount(pictureUpdate.getSelectCount() - 1);
                                    notifyItemChanged(pictureUpdate.getPosition());
                                }
                            }
                            removeSelectCount=picture.getSelectCount();
                            picture.setSelectCount(0);

                        } else {

                            if (picturesSelected.size() >= 6){



                            }else {

                                count++;
                                picture.setSelectCount(count);
                                //add object to hashmap
                                picturesSelected.add(picture);
                                //picturesSelected.add(picture);
                                textView.setText(picture.getSelectCount() + "");
                                textView.setBackground(context.getResources().getDrawable(R.drawable.background_count_selected));

                            }

                        }
                        listener.onItemSelectedChange(picturesSelected.size());


                    }else {

                    }

                }
            });
        }
    }


    //get all picture selected
    public ArrayList<Picture> getAllPictureSelected(){

        Collections.sort(picturesSelected, new Comparator<Picture>() {
            @Override
            public int compare(Picture o1, Picture o2) {
                return o1.getSelectCount()>=o2.getSelectCount()?1:-1;
            }
        });

        return (ArrayList<Picture>) picturesSelected;
    }
}
