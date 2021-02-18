package com.sss.spectrum;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sss.VisualizerHelper;
import com.sss.view.SlipBallView;

public class SlipBallActivity extends AppCompatActivity {
    private SlipBallView slipBallView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slip_ball);
        slipBallView = findViewById(R.id.slip_ball);
        VisualizerHelper.getInstance().addCallBack(slipBallView);
    }

    @Override
    protected void onStop() {
        super.onStop();
        VisualizerHelper.getInstance().removeCallBack(slipBallView);
    }
}
