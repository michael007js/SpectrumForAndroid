package com.sss;

import android.os.Handler;
import android.os.Looper;

import com.sss.processor.FFTListener;
import com.sss.spectrum.AppConstant;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class VisualizerHelper {
    private static VisualizerHelper helper;


    public static synchronized VisualizerHelper getInstance() {
        synchronized (VisualizerHelper.class) {
            if (helper == null) {
                helper = new VisualizerHelper();
            }
            return helper;
        }
    }

    private List<OnVisualizerEnergyCallBack> onEnergyCallBacks = new ArrayList<>();

    private final FFTListener fftListener = new FFTListener() {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        float[] fft;
        float energy;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < onEnergyCallBacks.size(); i++) {
                    if (!onEnergyCallBacks.isEmpty() && i <= onEnergyCallBacks.size() - 1) {
                        onEnergyCallBacks.get(i).setWaveData(fft, energy);
                    }
                }
            }
        };

        @Override
        public void onFFTReady(int sampleRateHz, int channelCount, float[] fft) {


            float energy = 0f;
            int size = Math.min(fft.length, AppConstant.SAMPLE_SIZE);
            float[] newData = new float[size];
            for (int i = 0; i < size; i++) {
                //TODO 注意：这里取正数，如果绘制波形图，则不需要取正，后期如果加入波形类频谱则需要对此处改造
                float value = Math.max(0, fft[i]) * AppConstant.LAGER_OFFSET;
                energy += value;
                newData[i] = value;
            }
            this.fft = newData;
            this.energy = energy;
            mainHandler.post(runnable);
        }
    };

    public FFTListener getFftListener() {
        return fftListener;
    }

    public void addCallBack(OnVisualizerEnergyCallBack onEnergyCallBack) {
        onEnergyCallBacks.add(onEnergyCallBack);
    }

    public void removeCallBack(OnVisualizerEnergyCallBack onEnergyCallBack) {
        onEnergyCallBacks.remove(onEnergyCallBack);
    }

    public interface OnVisualizerEnergyCallBack {

        void setWaveData(float[] data, float totalEnergy);

    }
}
