package iss.workshop.ca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class LoadingImageActivity extends AppCompatActivity {

    int[] arr = {R.drawable.afraid,R.drawable.full,R.drawable.hug,R.drawable.laugh,
            R.drawable.peep,R.drawable.snore,R.drawable.full,R.drawable.hug
    };

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RowAdapter rowAdapter;
    private ArrayList<Picture> pictures;
    private Button nextBtn;
    private EditText editText;
    private Button downloadBtn;
    private ProgressBar progressBar;
    private TextView downloading;
    private TextView downloaded;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_image);

        getLayoutWidget();
        setDownloadBtn();
        setNextBtn();
        setAdpter();

    }


    public void initPictureData(){

        for (int i = 0; i < arr.length;i++){
            int imageId = arr[i];
            Picture picture = new Picture(String.valueOf(imageId));
            pictures.add(picture);
        }

    }


    private void setAdpter(){
        pictures = new ArrayList<>();
        initPictureData();
        layoutManager = new GridLayoutManager(this,4);
        recyclerView.setLayoutManager(layoutManager);
        rowAdapter = new RowAdapter(this, pictures, new RowAdapter.ItemSelectedChangeListener() {
            @Override
            public void onItemSelectedChange(int number) {
                if (number > 0) {
                    //check how many images are currently selected
                    System.out.println("current have pictures count: " + number);

                    if (number == 6){

                        nextBtn.setVisibility(View.VISIBLE);

                    }else {
                        nextBtn.setVisibility(View.INVISIBLE);
                    }

                } else {

                }
            }
        });
        recyclerView.setAdapter(rowAdapter);

    }

    private void setNextBtn(){

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pass a list of pictures to the next Activity
                ArrayList<Picture> picturesSelected = rowAdapter.getAllPictureSelected();
                System.out.println("pictures Number:" + picturesSelected.size());
                //jump to next Activity
//                Intent intentDetail = new Intent(MainActivity.this, list.class);
//                intentDetail.putParcelableArrayListExtra("listpicture", picturesSelected);
//                startActivity(intentDetail);
            }
        });

    }


    private void getLayoutWidget(){
        nextBtn = findViewById(R.id.nextBtn);
        recyclerView = findViewById(R.id.recyclerview);
        editText = findViewById(R.id.urlEditText);
        downloadBtn = findViewById(R.id.downloadBtn);
        progressBar = findViewById(R.id.progressBar);
        downloading = findViewById(R.id.DownloadText);
        downloaded =findViewById(R.id.Downloaded);
    }


    private void setDownloadBtn(){
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] newdataArr = {R.drawable.afraid};
                for (int i = 0; i < newdataArr.length;i++){
                    int imageId = newdataArr[i];
                    Picture picture = new Picture(String.valueOf(imageId));
                    onePictureDownloadSuccess(picture);
                }
                String downloadUrl = editText.getText().toString();
                if (downloadUrl != null){

                }

            }
        });
    }


    private void setProgressBarBycheckDownloadPictureNumber(){

        if (rowAdapter.pictures != null){

            ValueAnimator animator = ValueAnimator.ofInt(0, 100);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    int progress = rowAdapter.pictures.size() * 5;
                    progressBar.setProgress(progress);
                    if(progress == 100){
                        progressBar.setVisibility(View.INVISIBLE);
                        downloading.setVisibility(View.INVISIBLE);
                        downloaded.setVisibility(View.VISIBLE);
                    }
                    else{
                        progressBar.setVisibility(View.VISIBLE);
                        downloading.setVisibility(View.VISIBLE);
                        downloaded.setVisibility(View.INVISIBLE);
                        //progressBar.setProgress(progress);
                    }
                }
            });
            animator.setRepeatCount(0);
            animator.setDuration(2000);
            animator.start();

        }

    }


    public void onePictureDownloadSuccess(Picture picture){
        rowAdapter.pictures.add(picture);
        setProgressBarBycheckDownloadPictureNumber();
        rowAdapter.notifyDataSetChanged();


    }




}