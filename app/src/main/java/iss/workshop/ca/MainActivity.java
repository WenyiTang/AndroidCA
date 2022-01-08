package iss.workshop.ca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button startBtn;
    private Button seletcBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startBtn = findViewById(R.id.playGame);
        startBtn.setOnClickListener(this);

        seletcBtn = findViewById(R.id.selectImage);
        seletcBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == startBtn){
            Intent intent = new Intent(this, GamePlay.class);
            startActivity(intent);
            finish();
        }else if(view == seletcBtn){
            Intent intent = new Intent(this, LoadingImageActivity.class);
            startActivity(intent);
        }
    }
}