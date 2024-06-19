package com.sss.spectrum;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sss.VisualizerHelper;
import com.sss.view.SiriView;

public class SiriActivity extends AppCompatActivity {
    private SiriView siriView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siri);
        siriView = findViewById(R.id.siri_view);
        VisualizerHelper.getInstance().addCallBack(siriView);
    }

    @Override
    protected void onStop() {
        super.onStop();
        VisualizerHelper.getInstance().removeCallBack(siriView);
    }
}
