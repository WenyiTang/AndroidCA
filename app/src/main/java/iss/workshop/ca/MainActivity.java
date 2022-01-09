package iss.workshop.ca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button startBtn;
    private Button guideBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startBtn = findViewById(R.id.selectImage);
        startBtn.setOnClickListener(this);

        guideBtn = findViewById(R.id.guideBtn);
        guideBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == startBtn){
            Intent intent = new Intent(this, LoadingImageActivity.class);
            startActivity(intent);
            finish();
        }else if(view == guideBtn){
            Intent intent = new Intent(this, Guide.class);
            startActivity(intent);
        }
    }
}