package iss.workshop.ca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GamePlay extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener{
//    private String[] img = {
//            "afraid", "full", "hug", "laugh", "peep", "snore"
//    };

    private ArrayList<Picture> pictures = new ArrayList<>();

//    private String[] imgs = new String[img.length * 2];
//    private String[] pics = new String[pictures.size() * 2];
//    private ArrayList<String> pics;
    private String[] cardflipped = new String[2];
    private ImageView[] imgflipped = new ImageView[2];
    private boolean ready = false;
    private Integer matches = 0;
    private Integer triesCount = 0;
    private TextView matchesCount;
    private TextView tries;
    private Button menuBtn, resumeBtn, playAgainBtn, endMainMenuBtn, mainMenuBtn, restartBtn;
    private long pauseTime;
    private Chronometer simpleChronometer;
    private ConstraintLayout menu;
    private ConstraintLayout endPopup;
    private ArrayList<Picture> duplicatePics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        // Get data from MainActivity
        Intent intent = getIntent();
        pictures = (ArrayList<Picture>) intent.getSerializableExtra("pictures");

        generateBitmap(pictures);

        duplicatePics = duplicateAndShuffle(pictures);

//        for (Picture pic : pictures) {
//            picId.add(pic.getId());
//        }

        //initialize primary elements on main thread
        menuBtn = findViewById(R.id.menuBtn);
        simpleChronometer = findViewById(R.id.timerCount);
        matchesCount = findViewById(R.id.matchesCount);
        tries = findViewById(R.id.tries);

        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resumeBtn = findViewById(R.id.resumeBtn);
                        playAgainBtn = findViewById(R.id.playAgain);
                        mainMenuBtn = findViewById(R.id.toMainMenu);
                        endMainMenuBtn = findViewById(R.id.endMainMenu);
                        restartBtn = findViewById(R.id.restartBtn);
                        menu = findViewById(R.id.menuPopup);
                        endPopup = findViewById(R.id.endGame);
                        initElements();
                    }
                });
            }
        }).start();

        loadGameGrid();
    }

    private void loadGameGrid(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //pics = populateImgs(picId);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CardAdapter adapter = new CardAdapter(GamePlay.this, duplicatePics);

                        GridView gridView = findViewById(R.id.gridView);
                        if (gridView != null) {
                            gridView.setAdapter(adapter);
                            gridView.setOnItemClickListener(GamePlay.this);
                            ready = true;
                            start();
                        }
                    }
                });
            }
        }).start();
    }

    private void initElements() {
        if (menuBtn != null){
            menuBtn.setOnClickListener(this);
        }
        if (resumeBtn != null){
            resumeBtn.setOnClickListener(this);
        }
        if (playAgainBtn != null){
            playAgainBtn.setOnClickListener(this);
        }
        if (mainMenuBtn != null){
            mainMenuBtn.setOnClickListener(this);
        }
        if (endMainMenuBtn != null){
            endMainMenuBtn.setOnClickListener(this);
        }
        if (restartBtn != null){
            restartBtn.setOnClickListener(this);
        }
        
        String matchStr = getString(R.string.matches_count, matches, pictures.size());
        matchesCount.setText(matchStr);

        String triesStr = getString(R.string.tries_count, triesCount);
        tries.setText(triesStr);
    }

//    private void populateImgs(String[] img, String[] imgs){
//        List<String> shuffledList = new ArrayList<>();
//        for (String s : img){
//            shuffledList.add(s);
//            shuffledList.add(s);
//        }
//        Collections.shuffle(shuffledList);
//        shuffledList.toArray(imgs);
//    }

//    private void populateImgs(ArrayList<String> picId, String[] pics){
//        List<String> shuffledList = new ArrayList<>();
//        for (String s : picId){
//            shuffledList.add(s);
//            shuffledList.add(s);
//        }
//        Collections.shuffle(shuffledList);
//        shuffledList.toArray(pics);
//    }

    private ArrayList<String> populateImgs(ArrayList<String> picId){
        ArrayList<String> shuffledList = new ArrayList<>();
        for (String s : picId){
            shuffledList.add(s);
            shuffledList.add(s);
        }
        Collections.shuffle(shuffledList);
        return shuffledList;
    }


    @Override
    public void onClick(View view) {
        if (view == menuBtn){
            pause();
            menu.setVisibility(View.VISIBLE);
            menuBtn.setVisibility(View.INVISIBLE);
            ready = false;
        }

        if (view == resumeBtn){
            menu.setVisibility(View.INVISIBLE);
            menuBtn.setVisibility(View.VISIBLE);
            resume();
            ready = true;
        }

        if (view == playAgainBtn){
//            Intent intent = new Intent(this, LoadingImageActivity.class);
//            startActivity(intent);
//            finish();
//            overridePendingTransition(0,0);
            restartGame();
            endPopup.setVisibility(View.INVISIBLE);
        }

        if (view == mainMenuBtn || view == endMainMenuBtn){
            deleteExistingImgFiles();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        if (view == restartBtn){
            restartGame();
        }
    }

    public void restartGame(){
        //couldnt find a way to restart the entire activity
        //recreate() cause black screen to appear for 1s
        //starting new intent requires passing of the pictures from LoadingImageActivity again,
        ////but getIntent() in beginning of onCreate will be from this GamePlay activity

        //hence one solution is to set the matches count to 0, and load the game grid again

        duplicatePics = duplicateAndShuffle(pictures);

        loadGameGrid();

        matches = 0;
        triesCount = 0;

        cardflipped[0] = null;
        cardflipped[1] = null;

        String matchStr = getString(R.string.matches_count, matches, pictures.size());
        matchesCount.setText(matchStr);

        String triesStr = getString(R.string.tries_count, triesCount);
        tries.setText(triesStr);

        menu.setVisibility(View.INVISIBLE);
        menuBtn.setVisibility(View.VISIBLE);
    }

    public void start(){
        simpleChronometer.setBase(SystemClock.elapsedRealtime());
        simpleChronometer.start();
    }

    public void pause(){
        pauseTime = simpleChronometer.getBase() - SystemClock.elapsedRealtime();
        simpleChronometer.stop();
    }

    public void resume(){
        simpleChronometer.setBase(SystemClock.elapsedRealtime() + pauseTime);
        simpleChronometer.start();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ImageView imageView = view.findViewById(R.id.cardImg);

        if (!ready || (imageView.getVisibility() == View.VISIBLE)){
            return;
        }

        if (cardflipped[0] == null){
            move1(imageView, i);
        }
        else if (cardflipped[1] == null){
            move2(imageView, i);

            checkMatch(imageView);
        }

        if (cardflipped[1] != null){
            cardflipped[0] = null;
            cardflipped[1] = null;
        }
    }

    protected void endGame(){
        simpleChronometer.stop();
        String timeTaken = simpleChronometer.getContentDescription().toString();

        menuBtn.setVisibility(View.INVISIBLE);

        TextView congrats = findViewById(R.id.congrats);
        String congratsStr = getString(R.string.congrats, pictures.size(), timeTaken, triesCount);
        congrats.setText(congratsStr);
        endPopup.setVisibility(View.VISIBLE);
    }

    protected void move1(ImageView imageView, int i){
        imgflipped[0] = imageView;
        imgflipped[0].setVisibility(View.VISIBLE);
        Picture pic = (Picture) imgflipped[0].getTag();
        cardflipped[0] = pic.getId();
    }

    protected void move2(ImageView imageView, int i){
        imgflipped[1] = imageView;
        imgflipped[1].setVisibility(View.VISIBLE);
        Picture pic = (Picture) imgflipped[1].getTag();
        cardflipped[1] = pic.getId();
        triesCount++;
        String triesStr = getString(R.string.tries_count, triesCount);
        tries.setText(triesStr);
    }

    protected void checkMatch(ImageView imageView){
        if (cardflipped[0].equals(cardflipped[1])){
            matches++;

            String matchStr = getString(R.string.matches_count, matches, pictures.size());
            matchesCount.setText(matchStr);

            if (matches == pictures.size()) {
                endGame();
            }
        }
        else{
            turnCardBack(imageView);
        }
    }

    protected void turnCardBack(ImageView imageView){
        ready = false;
        imageView.postDelayed(() -> {
            imgflipped[0].setVisibility(View.INVISIBLE);
            imgflipped[1].setVisibility(View.INVISIBLE);
            ready = true;
        }, 500);
    }

    private void generateBitmap(ArrayList<Picture> pictures) {
        for (Picture pic : pictures) {
            Bitmap bitmap = BitmapFactory.decodeFile(pic.getFile().getAbsolutePath());
            Bitmap bitmap_scaled = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            pic.setBitmap(bitmap_scaled);
        }
    }

    private ArrayList<Picture> duplicateAndShuffle(ArrayList<Picture> pics) {
        ArrayList<Picture> newPictures = new ArrayList<>();

        for (Integer i = 0; i < pics.size(); i++) {
            Bitmap bitmap = pics.get(i).getBitmap();
            String id = i.toString();
            newPictures.add(new Picture(bitmap, id));
            newPictures.add(new Picture(bitmap, id));
        }

        Collections.shuffle(newPictures);
        return newPictures;
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
}