package com.sss.processor;

import android.media.AudioTrack;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import com.paramsen.noise.Noise;
import com.sss.spectrum.AppConstant;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import be.tarsos.dsp.util.fft.FFT;


public class FFTAudioProcessor implements AudioProcessor {

    private static final int INITIAL_CAPACITY = 8192;
    private static final int MAX_CAPACITY = 65536;
    private static final int BUFFER_EXTRA_SIZE = AppConstant.SAMPLE_SIZE * 8;

    private Noise noise;
    private boolean isActive = false;

    private ByteBuffer processBuffer;
    private ByteBuffer fftBuffer;
    private ByteBuffer outputBuffer;

    private FFTListener listener;

    private boolean inputEnded = false;

    private ByteBuffer srcBuffer;
    private int srcBufferPosition = 0;
    private final byte[] tempByteArray = new byte[AppConstant.SAMPLE_SIZE * 2];

    private int audioTrackBufferSize = 0;

    private float[] src = new float[AppConstant.SAMPLE_SIZE];
    private float[] dst = new float[AppConstant.SAMPLE_SIZE + 2];

    private final ExecutorService fftExecutor = Executors.newSingleThreadExecutor();


    public void setFftListener(FFTListener listener) {
        this.listener = listener;
    }

    public FFTAudioProcessor() {
        processBuffer = ByteBuffer.allocateDirect(INITIAL_CAPACITY).order(ByteOrder.nativeOrder());
        fftBuffer = ByteBuffer.allocateDirect(INITIAL_CAPACITY).order(ByteOrder.nativeOrder());
        outputBuffer = AudioProcessor.EMPTY_BUFFER;
    }

    private ByteBuffer ensureCapacity(ByteBuffer buffer, int requiredCapacity) {
        if (buffer.capacity() < requiredCapacity) {
            int newCapacity = Math.min(requiredCapacity * 2, MAX_CAPACITY);
            ByteBuffer newBuffer = ByteBuffer.allocateDirect(newCapacity).order(ByteOrder.nativeOrder());
            buffer.flip();
            newBuffer.put(buffer);
            return newBuffer;
        } else {
            buffer.clear();
            return buffer;
        }
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    private AudioFormat inputAudioFormat;

    @Override
    public AudioFormat configure(AudioFormat inputAudioFormat) throws UnhandledAudioFormatException {
        if (inputAudioFormat.encoding != C.ENCODING_PCM_16BIT) {
            throw new UnhandledAudioFormatException(inputAudioFormat);
        }
        this.inputAudioFormat = inputAudioFormat;
        isActive = true;
        noise = Noise.real(AppConstant.SAMPLE_SIZE);
        audioTrackBufferSize = getDefaultBufferSizeInBytes(inputAudioFormat);
        srcBuffer = ByteBuffer.allocate(audioTrackBufferSize + BUFFER_EXTRA_SIZE);
        return inputAudioFormat;
    }

    /**
     * 1.0立体声，小于1左声道  大于1右声道
     * 可动态切换
     */
    private float vocalTract = 0.5f;

    public void setVocalTract(float vocalTract) {
        this.vocalTract = vocalTract;
    }


    @Override
    public void queueInput(ByteBuffer inputBuffer) {
        int position = inputBuffer.position();
        int limit = inputBuffer.limit();
        int frameCount = (limit - position) / (2 * inputAudioFormat.channelCount);
        int singleChannelOutputSize = frameCount * 2;
        int outputSize = frameCount * inputAudioFormat.channelCount * 2;

        processBuffer = ensureCapacity(processBuffer, outputSize);
        fftBuffer = ensureCapacity(fftBuffer, singleChannelOutputSize);

        while (position < limit) {
            int summedUp = 0;
            for (int channelIndex = 0; channelIndex < inputAudioFormat.channelCount; channelIndex++) {
                short current = inputBuffer.getShort(position + 2 * channelIndex);

                // 对每个样本应用vocalTract值进行音量调整
                float adjustedSample = current * vocalTract;
                // 需要确保调整后的样本仍然在short类型的有效范围内
                short finalSample = (short) Math.max(Math.min(adjustedSample, Short.MAX_VALUE), Short.MIN_VALUE);
                // 写入调整后的样本值到processBuffer
                processBuffer.putShort(finalSample);

//                processBuffer.putShort(current);
                summedUp += current;
            }
            fftBuffer.putShort((short) (summedUp / inputAudioFormat.channelCount));
            position += inputAudioFormat.channelCount * 2;
        }

        inputBuffer.position(limit);

        processFFTByDsp(fftBuffer);
        processBuffer.flip();
        outputBuffer = processBuffer;
    }


    FFT fft = new FFT(AppConstant.SAMPLE_SIZE);

    private void processFFTByDsp(ByteBuffer buffer) {
        // 确保buffer是正确设置为读模式
        buffer.flip();

        // 准备一个float数组以供FFT变换
        float[] audioFloats = new float[AppConstant.SAMPLE_SIZE];
        ShortBuffer shortBuffer = buffer.asShortBuffer();

        // 将ByteBuffer中的短整型数据转换为浮点数
        for (int i = 0; i < AppConstant.SAMPLE_SIZE && shortBuffer.hasRemaining(); i++) {
            audioFloats[i] = shortBuffer.get() / 32768.0f; // Short.MAX_VALUE + 1 的浮点表示
        }

        // 使用TarsosDSP库执行FFT变换
        fft.forwardTransform(audioFloats);
        // 在这里处理FFT结果，例如通过listener回调
        if (listener != null) {
            // 注意，具体实现可能需要你根据实际频谱数据调整参数
            listener.onFFTReady(inputAudioFormat.sampleRate, inputAudioFormat.channelCount, audioFloats);
        }
    }

    private void processFFTByNoise(ByteBuffer buffer) {
        if (listener == null) {
            return;
        }
        srcBuffer.put(buffer.array());
        srcBufferPosition += buffer.array().length;
        int bytesToProcess = AppConstant.SAMPLE_SIZE * 2;
        Byte currentByte = null;
        while (srcBufferPosition > audioTrackBufferSize) {
            srcBuffer.position(0);
            srcBuffer.get(tempByteArray, 0, bytesToProcess);

            for (int index = 0; index < tempByteArray.length; index++) {
                byte b = tempByteArray[index];
                if (currentByte == null) {
                    currentByte = b;
                } else {
                    src[index / 2] = (currentByte.floatValue() * Byte.MAX_VALUE + b) / (Byte.MAX_VALUE * Byte.MAX_VALUE);
                    dst[index / 2] = 0f;
                    currentByte = null;
                }
            }
            srcBuffer.position(bytesToProcess);
            srcBuffer.compact();
            srcBufferPosition -= bytesToProcess;
            srcBuffer.position(srcBufferPosition);
            float[] fft = noise.fft(src, dst);
            listener.onFFTReady(inputAudioFormat.sampleRate, inputAudioFormat.channelCount, fft);
        }
    }

    @Override
    public void queueEndOfStream() {
        inputEnded = true;
        processBuffer = AudioProcessor.EMPTY_BUFFER;
    }

    @Override
    public ByteBuffer getOutput() {
        ByteBuffer outputBuffer = this.outputBuffer;
        this.outputBuffer = AudioProcessor.EMPTY_BUFFER;
        return outputBuffer;
    }

    @Override
    public boolean isEnded() {
        return inputEnded && processBuffer == AudioProcessor.EMPTY_BUFFER;
    }

    @Override
    public void flush() {
        outputBuffer = AudioProcessor.EMPTY_BUFFER;
        inputEnded = false;
    }

    @Override
    public void reset() {
        flush();
        processBuffer = ByteBuffer.allocateDirect(INITIAL_CAPACITY).order(ByteOrder.nativeOrder());
        fftBuffer = ByteBuffer.allocateDirect(INITIAL_CAPACITY).order(ByteOrder.nativeOrder());
        inputAudioFormat = new AudioFormat(Format.NO_VALUE, Format.NO_VALUE, Format.NO_VALUE);
    }

    private int getDefaultBufferSizeInBytes(AudioFormat audioFormat) {
        int outputPcmFrameSize = Util.getPcmFrameSize(audioFormat.encoding, audioFormat.channelCount);
        int minBufferSize = AudioTrack.getMinBufferSize(
                audioFormat.sampleRate,
                Util.getAudioTrackChannelConfig(audioFormat.channelCount),
                audioFormat.encoding
        );
        Assertions.checkState(minBufferSize != AudioTrack.ERROR_BAD_VALUE);
        int multipliedBufferSize = minBufferSize * 4;
        int minAppBufferSize = (int) durationUsToFrames(250000) * outputPcmFrameSize;
        int maxAppBufferSize = Math.max(
                minBufferSize,
                (int) (durationUsToFrames(750000) * outputPcmFrameSize)
        );
        int bufferSizeInFrames = Util.constrainValue(
                multipliedBufferSize,
                minAppBufferSize,
                maxAppBufferSize
        ) / outputPcmFrameSize;
        return bufferSizeInFrames * outputPcmFrameSize;
    }

    private long durationUsToFrames(long durationUs) {
        return durationUs * inputAudioFormat.sampleRate / C.MICROS_PER_SECOND;
    }

    public void release() {
        if (noise != null) {
            noise.close();
        }
        if (fftExecutor != null) {
            if (!fftExecutor.isTerminated()) {
                fftExecutor.shutdownNow();
            }
        }

    }
}
