package com.sss.spectrum;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sss.VisualizerHelper;
import com.sss.view.RotatingCircleView;

public class RotatingCircleActivity extends AppCompatActivity {
    private RotatingCircleView rotating;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotating_circle);
        rotating = findViewById(R.id.rotating);
        VisualizerHelper.getInstance().addCallBack(rotating);
    }


    @Override
    protected void onStop() {
        super.onStop();
        VisualizerHelper.getInstance().removeCallBack(rotating);
    }
}
