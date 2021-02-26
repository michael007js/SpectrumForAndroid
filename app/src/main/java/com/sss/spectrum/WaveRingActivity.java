package com.sss.spectrum;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.SeekBar;

import com.sss.VisualizerHelper;
import com.sss.view.WaveRingView;

public class WaveRingActivity extends AppCompatActivity {
    private WaveRingView wave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave_ring);
        wave = findViewById(R.id.wave);
        VisualizerHelper.getInstance().addCallBack(wave);
        ((RadioButton) findViewById(R.id.checkbox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                wave.setRotate(isChecked);
            }
        });
        ((RadioButton) findViewById(R.id.checkbox2)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                wave.setRandom(isChecked);
            }
        });
        ((CheckBox) findViewById(R.id.checkbox3)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                wave.setBase(isChecked);
            }
        });
        ((CheckBox) findViewById(R.id.checkbox4)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                wave.setWave(isChecked);
            }
        });
        ((CheckBox) findViewById(R.id.checkbox5)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                wave.setPowerOffset(isChecked);
            }
        });
        ((SeekBar) findViewById(R.id.seek_bar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (progress == 0) {
                        return;
                    }
                    wave.setScope(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ((SeekBar) findViewById(R.id.seek_bar2)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (progress == 0) {
                        return;
                    }
                    wave.setValue(progress);
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
        VisualizerHelper.getInstance().removeCallBack(wave);
    }
}
