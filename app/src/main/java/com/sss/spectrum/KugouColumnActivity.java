package com.sss.spectrum;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.sss.VisualizerHelper;
import com.sss.view.ColumnarView;
import com.sss.view.KugouColumn;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class KugouColumnActivity extends AppCompatActivity {
    private KugouColumn columnar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kugou_column);
        columnar = findViewById(R.id.columnar);
        VisualizerHelper.getInstance().addCallBack(columnar);
        ((SeekBar) findViewById(R.id.seek_bar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

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
        VisualizerHelper.getInstance().removeCallBack(columnar);
    }
}
