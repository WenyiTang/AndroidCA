package iss.workshop.ca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    int noOfPairs = 6;
    private Button startBtn, guideBtn;

    private final String[] links = {"https://cdn.stocksnap.io/img-thumbs/960w/mountains-lake_LWLQMM5WID.jpg",
            "https://cdn.stocksnap.io/img-thumbs/960w/sunflower-field_6K5XD147YC.jpg",
            "https://cdn.stocksnap.io/img-thumbs/960w/wheelchair-hospital_YIA3IQHMCR.jpg",
            "https://cdn.stocksnap.io/img-thumbs/960w/top-workspace_ILQA1VXXOJ.jpg",
            "https://cdn.stocksnap.io/img-thumbs/960w/antique-watch_9NSZOOQGTD.jpg",
            "https://cdn.stocksnap.io/img-thumbs/960w/goose-animal_YPWZ567BJP.jpg",
            "https://cdn.stocksnap.io/img-thumbs/960w/candy-canes_UPC02BPCIO.jpg",
            "https://cdn.stocksnap.io/img-thumbs/960w/nature-animal_RSGQ4K2PX5.jpg",
            "https://cdn.stocksnap.io/img-thumbs/960w/mountain-desert_OSDDRVNIFI.jpg"};

    private ArrayList<Picture> pictures  = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < noOfPairs; i++) {
            startDownloadPicture(links[i]);
        };

//        setPicturesId(pictures);
//        generateBitmap(pictures);

        startBtn = findViewById(R.id.playGame);
        startBtn.setOnClickListener(this);


        guideBtn = findViewById(R.id.guideBtn);
        guideBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == startBtn){
            Intent intent = new Intent(this, GamePlay.class);
            intent.putExtra("pictures", (Serializable) pictures);
            startActivity(intent);
        }

        if (view == guideBtn){
            Intent intent = new Intent(this, Guide.class);
            startActivity(intent);
        }
    }

    protected void startDownloadPicture(String imgURL) {
        String destFilename = UUID.randomUUID().toString() + imgURL.lastIndexOf(".") + 1;
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File destFile = new File(dir, destFilename);
        pictures.add(new Picture(destFile));

        new Thread(new Runnable() {
            @Override
            public void run() {
                PictureDownloader imgDL = new PictureDownloader();
                imgDL.downloadPicture(imgURL, destFile);
            }
        }).start();
    }

//    protected void setPicturesId(ArrayList<Picture> pictures) {
//        for (Integer i = 0; i < pictures.size(); i++) {
//            pictures.get(i).setId(i.toString());
//        }
//    }
//
//    private void generateBitmap(ArrayList<Picture> pictures) {
//        for (Picture pic : pictures) {
//            Bitmap bitmap = BitmapFactory.decodeFile(pic.getFile().getAbsolutePath());
//            pic.setBitmap(bitmap);
//        }
//    }
}