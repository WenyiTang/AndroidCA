package iss.workshop.ca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

    private Button startBtn, guideBtn, normalBtn, hardBtn, cancelBtn, leaderboardBtn;
    private ConstraintLayout diffMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        diffMenu = findViewById(R.id.diffPopup);
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startBtn = findViewById(R.id.selectImage);
                        guideBtn = findViewById(R.id.guideBtn);
                        normalBtn = findViewById(R.id.normalBtn);
                        hardBtn = findViewById(R.id.hardBtn);
                        cancelBtn = findViewById(R.id.cancel_diff);
                        leaderboardBtn = findViewById(R.id.leaderboardBtn);


                        initElements();
                    }
                });
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);
        diffMenu = findViewById(R.id.diffPopup);
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startBtn = findViewById(R.id.selectImage);
                        guideBtn = findViewById(R.id.guideBtn);
                        normalBtn = findViewById(R.id.normalBtn);
                        hardBtn = findViewById(R.id.hardBtn);
                        cancelBtn = findViewById(R.id.cancel_diff);
                        leaderboardBtn = findViewById(R.id.leaderboardBtn);

                        initElements();
                    }
                });
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        if (view == startBtn){
            diffMenu.setVisibility(View.VISIBLE);
            diffMenu.setEnabled(true);
            startBtn.setVisibility(View.INVISIBLE);
            guideBtn.setVisibility(View.INVISIBLE);
            leaderboardBtn.setVisibility(View.INVISIBLE);
            startBtn.setEnabled(false);
            guideBtn.setEnabled(false);
            leaderboardBtn.setEnabled(false);

        }
        if(view == guideBtn){
            Intent intent = new Intent(this, Guide.class);
            startActivity(intent);
        }
        if(view == normalBtn){
            Intent intent = new Intent(this, LoadingImageActivity.class);
            intent.putExtra("diff", "normal");
            startActivity(intent);
        }
        if(view == hardBtn){
            Intent intent = new Intent(this, LoadingImageActivity.class);
            intent.putExtra("diff", "hard");
            startActivity(intent);
        }
        if(view == leaderboardBtn) {
            Intent intent = new Intent(this, Leaderboard.class);
            intent.putExtra("diff", "normal");
            startActivity(intent);
        }

        if(view == cancelBtn){
            diffMenu.setVisibility(View.INVISIBLE);
            diffMenu.setEnabled(false);
            startBtn.setVisibility(View.VISIBLE);
            guideBtn.setVisibility(View.VISIBLE);
            leaderboardBtn.setVisibility(View.VISIBLE);
            startBtn.setEnabled(true);
            guideBtn.setEnabled(true);
            leaderboardBtn.setEnabled(true);
        }
    }

    private void initElements() {
        if (startBtn != null) {
            startBtn.setOnClickListener(this);
        }
        if (guideBtn != null) {
            guideBtn.setOnClickListener(this);
        }
        if (normalBtn != null) {
            normalBtn.setOnClickListener(this);
        }
        if (hardBtn != null) {
            hardBtn.setOnClickListener(this);
        }
        if (cancelBtn != null) {
            cancelBtn.setOnClickListener(this);
        }
        if (leaderboardBtn != null) {
            leaderboardBtn.setOnClickListener(this);
        }
    }

    private void initElements() {
        if (startBtn != null) {
            startBtn.setOnClickListener(this);
        }
        if (guideBtn != null) {
            guideBtn.setOnClickListener(this);
        }
        if (normalBtn != null) {
            normalBtn.setOnClickListener(this);
        }
        if (hardBtn != null) {
            hardBtn.setOnClickListener(this);
        }
        if (cancelBtn != null) {
            cancelBtn.setOnClickListener(this);
        }
        if (leaderboardBtn != null){
            leaderboardBtn.setOnClickListener(this);
        }
    }
}