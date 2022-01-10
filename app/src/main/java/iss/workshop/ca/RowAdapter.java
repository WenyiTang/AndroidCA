package iss.workshop.ca;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RowAdapter extends RecyclerView.Adapter<RowAdapter.MyViewHolder> {

    int[] picturesArr;
    protected File dir;
    protected File[] existingFiles;
    protected ArrayList<Bitmap> imgBitmaps = new ArrayList<>();



    public interface ItemSelectedChangeListener{
        void onItemSelectedChange(int number);
    }
    private RowAdapter.ItemSelectedChangeListener listener;

    public List<Picture> pictures;
    private Context context;
    public List<Picture> picturesSelected;
    public int count = 0;


    public RowAdapter(int[] picturesArr) {
        this.picturesArr = picturesArr;
    }


    public RowAdapter(Context context, List<Picture> pictures, RowAdapter.ItemSelectedChangeListener listener) {
        //setImgBitmapsFromExtStorage();
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
        holder.bind(pictures.get(position), position,context);

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

        public void bind(final Picture picture, final int position,Context context) {

            if (picture.getBitmap() == null){

                imageView.setBackgroundResource(R.drawable.no_img);

            }else {
                imageView.setImageBitmap(picture.getBitmap());
            }

            if (pictures.size() >= 20 && pictures.get(19).getBitmap() != null){
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

    private void setImgBitmapsFromExtStorage() {
        dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        existingFiles = dir.listFiles();
        for (File imgFile : existingFiles) {
            Bitmap bitmap = null;

            try {
                if (imgFile.exists()) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    System.out.println("Trying to decode imgFile: " + imgFile.getAbsolutePath());
                    bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    if (bitmap != null) {
                        System.out.println("Decoding successful");
                        imgBitmaps.add(bitmap);
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
