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

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;


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
    private Button downloadBtn;
    private ProgressBar progressBar;
    private TextView downloading;
    private TextView downloaded;

    //Daniel
    private EditText urlInput;
    private String externalUrl;
    private WebView mWebView;
    private String imageURLs;
    private String[] imageURLArray;

    Thread thread;
    private int fetchClick = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_image);

        getLayoutWidget();
        //setDownloadBtn();
        setFetchBtnListener();//from Daniel
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
        //initPictureData();
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
        urlInput = findViewById(R.id.urlEditText);
        downloadBtn = findViewById(R.id.downloadBtn);
        progressBar = findViewById(R.id.progressBar);
        downloading = findViewById(R.id.DownloadText);
        downloaded =findViewById(R.id.Downloaded);
        mWebView = findViewById(R.id.web_view);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
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
//            animator.setDuration(2000);
            animator.start();

        }

    }


    public void onePictureDownloadSuccess(Picture picture){

        rowAdapter.pictures.add(picture);
        setProgressBarBycheckDownloadPictureNumber();
        rowAdapter.notifyDataSetChanged();


    }

    //Daniel's methods start here

    private void setFetchBtnListener() {
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(LoadingImageActivity.this);
                externalUrl ="https://" + urlInput.getText().toString();
                System.out.println("External URL = " + externalUrl);
                if(Patterns.WEB_URL.matcher(externalUrl).matches()) {
                    mWebView.loadUrl("about:blank");
                    Toast.makeText(LoadingImageActivity.this, "Beginning download...", Toast.LENGTH_LONG).show();
                    //rowAdapter.pictures.clear();//clear previous images
                    //setAdpter();
                    //mWebView.loadUrl("about:blank");
                    loadPage();
                }
                else {
                    Toast.makeText(LoadingImageActivity.this,"URL invalid",Toast.LENGTH_LONG).show();
                }


            }
        });
    }
    private void loadPage(){
        mWebView.loadUrl("about:blank");

        mWebView.setWebViewClient(new WebViewClient() {


                                      @Override
                                      public void onPageFinished(WebView view, String url) {
                                          super.onPageFinished(view,url);
                                          System.out.println("External URL in loadPage() = " + mWebView.getUrl().toString());
                                          System.out.println("page finished loading");


                                          // Begin download process
                                          if(mWebView.getUrl().toString().contains("about:blank")) {
                                              System.out.println("Webview is in about:blank");
                                          }
                                          else {


                                              getImgURLs(mWebView);
                                          }



                                      }
                                  }
        );

        //mWebView.loadUrl(externalUrl);
    }
    public void getImgURLs(WebView webView) {
        String javascriptFn = "(function() {let imageNodes = document.querySelectorAll(\"img\"); " +
                "let imageSrcs = new Array(); " +
                "imageNodes.forEach(imageNode => imageSrcs.push(imageNode.src));" +
                "return imageSrcs; })()";
        webView.evaluateJavascript(javascriptFn, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                System.out.println("Callback for getImgURLs");
                System.out.println(s);
                externalUrl = null;
                imageURLs = s;
                //printImageURLs();
                processImageUrlString();
                downloadImages();



            }
        });


    }

    public void printImageURLs() {
        System.out.println("Executing printImageURLs");
        System.out.println(imageURLs);
        try {
            System.out.println("String Length = " + imageURLs.length());
        }
        catch (Exception e) {
            System.out.println("Error. Probably not done loading");
        }
    }
    public void processImageUrlString() {


        imageURLArray = imageURLs.replace("\"","")
                .replace("[","")
                .replace("]","")
                .split(",");


        System.out.println("Length of imageURLArray = " + imageURLArray.length);
        String[] tempStringArray = new String[20];
        int counter = 0;
        for (String url : imageURLArray) {
            if(counter > 19){
                break;
            }
            else if(url.contains(".jpg") || url.contains(".png"))
            {
                tempStringArray[counter] = url;
                counter++;
            }

        }
        imageURLArray = tempStringArray;
        System.out.println("After processing image URLs");
        for (String url : imageURLArray){
            System.out.println(url);
        }





    }
    public void downloadImages() {
        System.out.println("Executing downloadImages()...");
        ImageDownloader downloader = new ImageDownloader();



        new Thread (new Runnable() {
            @Override
            public void run() {
                System.out.println("entering new thread");

                // Delete existing images on SD card
                deleteExistingImgFiles();

                String destFilename;
                File destFile = null;
                File dir =getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                int counter = 0;
                DecimalFormat df = new DecimalFormat("00");
                for (String imgURL : imageURLArray) {
                    destFilename =  "image_" + df.format(counter);
                    counter++;
                    System.out.println("Downloading image from: " + imgURL);

                    destFile = new File(dir,destFilename);


                    if(downloader.downloadImage(imgURL,destFile))
                    {
                        System.out.println("Downloaded file: " + destFilename);
                        File finalDestFile = destFile;
                        String finalDestFilename = destFilename;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("Rendering " + finalDestFilename);
                                Picture picture = new Picture(BitmapFactory.decodeFile(finalDestFile.getAbsolutePath()));
                                onePictureDownloadSuccess(picture);

                            }
                        });



                    }

                }


            }

        }).start();

    }

    private void deleteExistingImgFiles(){
        File dir =getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] existingFiles = dir.listFiles();
        // Delete existing images from directory
        for(File file : existingFiles) {
            try{
                if(file.exists()) {
                    System.out.print("Deleting file : " + file.getName());
                    boolean result = file.delete();
                    if(result) {
                        System.out.println(", successful");
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Error while deleting file");
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





}