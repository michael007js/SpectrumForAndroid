package com.sss.spectrum;

import android.graphics.Color;

public class AppConstant {
    public static boolean imageValue = true;
    public static boolean isFFT = true;
    public static final int ALPHA = 255;
    public static final int RED = 255;
    public static final int GREEN = 255;
    public static final int BLUE = 255;
    public static final int COLOR = Color.argb(ALPHA, RED - 55, GREEN - 55, BLUE - 55);
    //音频把柱(max1024)
    public static final int LUMP_COUNT = 256;
    //放大量
    public static final int LAGER_OFFSET = isFFT ? 3 : 0;

}
