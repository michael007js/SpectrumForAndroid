package com.sss.processor;

public interface FFTListener {
    void onFFTReady(int sampleRateHz, int channelCount, float[] fft);
}
