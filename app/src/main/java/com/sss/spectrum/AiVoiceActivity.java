package com.sss.spectrum;

import android.os.Bundle;

import com.sss.VisualizerHelper;
import com.sss.view.AiVoiceView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cn.bar.DoubleHeadedDragonBar;

public class AiVoiceActivity extends AppCompatActivity {
    private AiVoiceView aiVoiceView;
    private DoubleHeadedDragonBar bar1, bar2, bar3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_voice);
        aiVoiceView = findViewById(R.id.aiVoice);
        bar1 = findViewById(R.id.bar1);
        bar2 = findViewById(R.id.bar2);
        bar3 = findViewById(R.id.bar3);
        VisualizerHelper.getInstance().addCallBack(aiVoiceView);
        bar1.setMinValue(0);
        bar1.setMaxValue(80);
        bar2.setMinValue(10);
        bar2.setMaxValue(90);
        bar3.setMinValue(20);
        bar3.setMaxValue(100);
        bar1.setCallBack(new DoubleHeadedDragonBar.DhdBarCallBack() {
            @Override
            public void onEndTouch(float minPercentage, float maxPercentage) {
                super.onEndTouch(minPercentage, maxPercentage);
                aiVoiceView.setParameter(0, minPercentage / 100, maxPercentage / 100);
            }
        });
        bar2.setCallBack(new DoubleHeadedDragonBar.DhdBarCallBack() {
            @Override
            public void onEndTouch(float minPercentage, float maxPercentage) {
                super.onEndTouch(minPercentage, maxPercentage);
                aiVoiceView.setParameter(1, minPercentage / 100, maxPercentage / 100);
            }
        });
        bar3.setCallBack(new DoubleHeadedDragonBar.DhdBarCallBack() {
            @Override
            public void onEndTouch(float minPercentage, float maxPercentage) {
                super.onEndTouch(minPercentage, maxPercentage);
                aiVoiceView.setParameter(2, minPercentage / 100, maxPercentage / 100);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        VisualizerHelper.getInstance().removeCallBack(aiVoiceView);
    }
}
