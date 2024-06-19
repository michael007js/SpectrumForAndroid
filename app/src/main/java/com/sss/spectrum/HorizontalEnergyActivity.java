package com.sss.spectrum;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.SeekBar;

import com.sss.VisualizerHelper;
import com.sss.view.HorizontalEnergyView;

public class HorizontalEnergyActivity extends AppCompatActivity {
    private HorizontalEnergyView horizontalEnergyView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_energy);
        horizontalEnergyView = findViewById(R.id.horizontalEnergyView);
        VisualizerHelper.getInstance().addCallBack(horizontalEnergyView);
        ((SeekBar) findViewById(R.id.seek_bar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (progress == 0) {
                        return;
                    }
                    horizontalEnergyView.setCount(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.circular).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalEnergyView.setCircle(true);
            }
        });

        findViewById(R.id.square).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalEnergyView.setCircle(false);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        VisualizerHelper.getInstance().removeCallBack(horizontalEnergyView);
    }
}
