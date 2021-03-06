package iss.workshop.ca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button startBtn, guideBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startBtn = findViewById(R.id.playGame);
        startBtn.setOnClickListener(this);

        guideBtn = findViewById(R.id.guideBtn);
        guideBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == startBtn){
            Intent intent = new Intent(this, GamePlay.class);
            startActivity(intent);
        }

        if (view == guideBtn){
            Intent intent = new Intent(this, Guide.class);
            startActivity(intent);
        }
    }
}