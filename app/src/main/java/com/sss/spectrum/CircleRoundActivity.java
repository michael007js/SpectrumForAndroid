package com.sss.spectrum;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import com.sss.VisualizerHelper;
import com.sss.view.CircleRoundView;

public class CircleRoundActivity extends AppCompatActivity {
    private CircleRoundView circle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_round);
        circle = findViewById(R.id.circle);
        VisualizerHelper.getInstance().addCallBack(circle);
        ((SeekBar) findViewById(R.id.seek_bar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (progress == 0) {
                        return;
                    }
                    circle.setTransparencyFallOffTrigger(progress * 1.0f / 100);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        VisualizerHelper.getInstance().removeCallBack(circle);
    }
}
