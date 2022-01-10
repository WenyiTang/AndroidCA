package iss.workshop.ca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class Guide extends AppCompatActivity implements View.OnClickListener{

    private Button returnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        returnBtn = findViewById(R.id.returnBtn);
        returnBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == returnBtn) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}