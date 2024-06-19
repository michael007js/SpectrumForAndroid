package com.sss.spectrum;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sss.VisualizerHelper;
import com.sss.view.HexagramView;

public class HexagramViewActivity extends AppCompatActivity {
    private HexagramView hexagram;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hexagram);
        hexagram = findViewById(R.id.hexagram);
        VisualizerHelper.getInstance().addCallBack(hexagram);
    }


    @Override
    protected void onStop() {
        super.onStop();
        VisualizerHelper.getInstance().removeCallBack(hexagram);
    }
}
