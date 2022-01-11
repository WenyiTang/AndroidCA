package iss.workshop.ca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;


import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class LoadingImageActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RowAdapter rowAdapter;
    private ArrayList<Picture> pictures;
    private Button nextBtn;
    private Button downloadBtn;
    private ProgressBar progressBar;
    private TextView downloading;
    private TextView downloaded;


    private EditText urlInput;
    private String externalUrl;
    private String[] imageURLArray;
    private Thread imgUrlThread;
    private Thread downloadImagesThread;
    private volatile boolean stopDownload = false;
    private volatile boolean stopRender = false;




    private int difficulty = 0;
    private TextView selectInstruct;

    protected int RequireSelectedSize = 6;


    Thread thread;

    private int fetchClick = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_image);

        getDifficulty();
        getLayoutWidget();
        setFetchBtnListener();//from Daniel
        setNextBtn();
        setAdpter();
        initPictureData();
    }


    public void initPictureData(){

        for (int i = 0; i < 20;i++){
            Picture picture = new Picture();
            pictures.add(picture);
        }

    }


    private void setAdpter(){
        pictures = new ArrayList<>();
        //initPictureData();
        layoutManager = new GridLayoutManager(this,4);
        recyclerView.setLayoutManager(layoutManager);
        rowAdapter = new RowAdapter(this, pictures, difficulty, new RowAdapter.ItemSelectedChangeListener() {
            @Override
            public void onItemSelectedChange(int number) {
                if (number > 0) {
                    //check how many images are currently selected
                    System.out.println("current have pictures count: " + number);


                    if (number == difficulty){

                        nextBtn.setVisibility(View.VISIBLE);
                        selectInstruct.setVisibility(View.INVISIBLE);

                    }else {
                        nextBtn.setVisibility(View.INVISIBLE);
                        selectInstruct.setVisibility(View.VISIBLE);
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
                ArrayList<Picture> picturesProcessed = removeBitmap(picturesSelected);
                System.out.println("pictures Number:" + picturesSelected.size());
                //jump to next Activity
                Intent intentDetail = new Intent(getApplicationContext(), GamePlay.class);
                intentDetail.putExtra("pictures", (Serializable) picturesProcessed);
                startActivity(intentDetail);
            }
        });
    }


    private void getLayoutWidget(){
        nextBtn = findViewById(R.id.nextBtn);
        recyclerView = findViewById(R.id.recyclerview);
        urlInput = findViewById(R.id.urlEditText);
        downloadBtn = findViewById(R.id.downloadBtn);
        progressBar = findViewById(R.id.progressBar);
        downloading = findViewById(R.id.DownloadText);
        downloaded =findViewById(R.id.Downloaded);
        selectInstruct = findViewById(R.id.selectInstruct);

    }


    private void setProgressBarBycheckDownloadPictureNumber(int index){

        if (rowAdapter.pictures != null){

            if(rowAdapter.pictures.get(index).getBitmap() != null){
                TextView downloading = findViewById(R.id.DownloadText);
                int imageNum = index +1 ;
                String downloadStr = getString(R.string.DownloadText, imageNum,pictures.size());
                downloading.setText(downloadStr);
                ValueAnimator animator = ValueAnimator.ofInt(0, 100);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {

                        int progress = (index + 1) * 5;
                        progressBar.setProgress(progress);
                        selectInstruct.setText("Please select " + difficulty + " images");

                        if(progress == 100){
                            progressBar.setVisibility(View.INVISIBLE);
                            downloading.setVisibility(View.INVISIBLE);
                            TextView downloaded = findViewById(R.id.Downloaded);
                            String downloadedStr = getString(R.string.Downloaded,pictures.size());
                            downloaded.setText(downloadedStr);
                            downloaded.setVisibility(View.VISIBLE);
                            selectInstruct.setVisibility(View.VISIBLE);
                        }
                        else{
                            progressBar.setVisibility(View.VISIBLE);
                            downloading.setVisibility(View.VISIBLE);
                            downloaded.setVisibility(View.INVISIBLE);
                            selectInstruct.setVisibility(View.INVISIBLE);
                            //progressBar.setProgress(progress);
                        }
                    }
                });
                animator.setRepeatCount(0);
                animator.start();
            }else {

            }
        }

    }


    public void onePictureDownloadSuccess(int index,Picture picture){

        //System.out.println("currentIndex :" + index);
        rowAdapter.pictures.set(index,picture);
        setProgressBarBycheckDownloadPictureNumber(index);
        rowAdapter.notifyDataSetChanged();


    }

    private void setFetchBtnListener() {
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(LoadingImageActivity.this);

                // FOR DEMO
         /*       fetchClick++;
                if(fetchClick % 2 == 0) {
                    urlInput.setText("https://stocksnap.io/search/cats");
                }
                else {
                    urlInput.setText("https://stocksnap.io/search/dogs");
                }*/



                externalUrl = urlInput.getText().toString();
                System.out.println("Get images from " + externalUrl);
                if(Patterns.WEB_URL.matcher(externalUrl).matches()) {

                    Toast.makeText(LoadingImageActivity.this, "Beginning download...", Toast.LENGTH_SHORT).show();

                    progressBar.setVisibility(View.VISIBLE);
                    downloading.setVisibility(View.VISIBLE);
                    downloaded.setVisibility(View.INVISIBLE);
                    nextBtn.setVisibility(View.INVISIBLE);


                    // Stop imgUrlThread and downloadImagesThread
                    stopDownload = true;

                    // Wake up downloadImagesThread thread if it is waiting
                    synchronized (LoadingImageActivity.this) {
                        LoadingImageActivity.this.notify();
                    }


                    // Ensure that imgUrlThread has terminated
                    if (imgUrlThread != null) {
                        try {
                            imgUrlThread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    // Ensure that downloadImagesThread has terminated
                    if (downloadImagesThread != null) {
                        try {
                            downloadImagesThread.join();


                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }


                    // Clear images from view
                    rowAdapter.pictures.clear();//clear previous images
                    for (int i = 0; i < 20;i++){
                        Picture picture = new Picture();
                        rowAdapter.pictures.add(picture);
                    }
                    rowAdapter.notifyDataSetChanged();


                    if (rowAdapter.picturesSelected.size() != 0 && rowAdapter != null){
                        rowAdapter.count = 0;
                        rowAdapter.picturesSelected.clear();
                    }

                    // Allow imgUrlThread and downloadImagesThread to execute
                    stopDownload = false;

                    // Call on methods to start imgUrlThread and downloadImagesThread
                    fetchImgSRCs();
                    downloadImages();

                }
                else {
                    System.out.println(externalUrl + " is an invalid web URL. Aborting...");
                    progressBar.setVisibility(View.INVISIBLE);
                    downloading.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoadingImageActivity.this,"URL invalid",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }


    public void fetchImgSRCs(){
        imgUrlThread = new Thread(() -> {
            System.out.println("Starting imgUrlThread");

            try {
                int index = 0;
                imageURLArray = new String[20];
                // get http response and get parsed by Jsoup
                Document document = Jsoup.connect(externalUrl).timeout(10000).get();
                // get all <img> tags from http response
                Elements elements = document.select("img");
                for (Element element : elements) {

                    if(stopDownload) {
                        System.out.println("Ending imgUrlThread...");
                        return;
                    }
                    // determine whether there is a correct image
                    String imgSrc = element.attr("src");

                    // determine the file format
                    if ((imgSrc.contains(".jpg") || imgSrc.contains(".png")) && imgSrc.contains("https://")) {
                        // get first 20 images
                        if (index >= 20) {
                            break;
                        }
                        //System.out.println(imgSrc);
                        imageURLArray[index] = imgSrc;
                        index++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        imgUrlThread.start();


    }





    public void downloadImages() {
        ImageDownloader downloader = new ImageDownloader();
        try {
            imgUrlThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        downloadImagesThread =  new Thread (new Runnable() {
            @Override
            public void run() {

                System.out.println("Starting downloadImagesThread");


                // Delete existing images on SD card
                deleteExistingImgFiles();
                String destFilename;
                File destFile = null;
                File dir =getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                int counter = 0;
                DecimalFormat df = new DecimalFormat("00");
                for (String imgURL : imageURLArray) {

                    destFilename =  "image_" + df.format(counter);


                    destFile = new File(dir,destFilename);



                    if(downloader.downloadImage(imgURL,destFile))
                    {
                        System.out.println("Downloaded file: " + destFilename);
                        File finalDestFile = destFile;
                        String finalDestFilename = destFilename;
                        int finalCounter = counter;

                        Runnable myRunnable = new Runnable() {
                            @Override
                            public  void run() {



                                    if (stopRender) {


                                        synchronized (LoadingImageActivity.this) {
                                            System.out.println("Interrupting myRunnable");
                                            LoadingImageActivity.this.notify();
                                        }
                                        return;
                                    }


                                System.out.println("Rendering " + finalDestFilename);

                                Picture picture = new Picture(BitmapFactory.decodeFile(finalDestFile.getAbsolutePath()), finalDestFile);
                                onePictureDownloadSuccess(finalCounter,picture);

                                // Wake up download thread after rendering one image
                                synchronized (LoadingImageActivity.this){
                                    LoadingImageActivity.this.notify();
                                }


                            }
                        };

                        if(stopDownload) {
                            System.out.println("Aborting download...");
                            synchronized (LoadingImageActivity.this) {
                                // Interrupt runnables which are rendering images on UI thread
                                stopRender = true;

                                // Wake up UI thread
                                LoadingImageActivity.this.notify();
                            }

                            // Exits imageDownloadThread
                            return;
                        }
                        synchronized (LoadingImageActivity.this) {
                            stopRender = false;
                            runOnUiThread(myRunnable);
                            try {
                                // Wait for UI thread to finish executing
                                LoadingImageActivity.this.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    counter++;
                }
            }
        });
        downloadImagesThread.start();

    }

    private void deleteExistingImgFiles(){
        File dir =getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] existingFiles = dir.listFiles();
        // Delete existing images from directory
        for(File file : existingFiles) {
            try{
                if(file.exists()) {
                    boolean result = file.delete();
                    if(result) {
                    }
                }
            }
            catch (Exception e) {
            }
        }

    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    private ArrayList<Picture> removeBitmap(ArrayList<Picture> pictures) {
        ArrayList<Picture> processedPics = new ArrayList<>();
        for (Picture pic : pictures) {
            File file = pic.getFile();
            int position = pic.getPosition();
            processedPics.add(new Picture(file, position));
        }
        return processedPics;
    }

    private void getDifficulty() {
        Intent intent = getIntent();
        String diff = intent.getStringExtra("diff");
        if (diff.equals("normal")) {
            difficulty = 6;
        };
        if (diff.equals("hard")) {
            difficulty = 8;
        }
        return;
    }



}