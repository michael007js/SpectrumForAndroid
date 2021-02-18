package com.sss.spectrum;

import android.graphics.Color;

public class AppConstant {
    public static boolean isFFT = true;
    public static final int ALPHA = 255;
    public static final int RED = 255;
    public static final int GREEN = 255;
    public static final int BLUE = 255;
    public static final int COLOR = Color.argb(ALPHA, RED - 55, GREEN - 55, BLUE - 55);
    //音频把柱 min 1,max 128
    public static final int LUMP_COUNT = 127;
    //伪FPS，看运行设备的处理能力，这里只是找到了一种计算方法来拦截onFftDataCapture回调达到模拟一下FPS的效果，最大流畅度无法超过onFftDataCapture的回调次数，当为30帧以下时效果最明显
    public static final long FPS = 300;
    //放大量
    public static final int LAGER_OFFSET = isFFT ? 3 : 0;

}
