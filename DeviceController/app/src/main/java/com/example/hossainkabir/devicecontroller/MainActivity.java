package com.example.hossainkabir.devicecontroller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void StartWorking(View view)
    {
        Intent intent=new Intent(this,ServiceClass.class);
        startService(intent);

    }
    public void StopWorking(View view)
    {
        Intent intent=new Intent(this,ServiceClass.class);
        stopService(intent);

    }
}
