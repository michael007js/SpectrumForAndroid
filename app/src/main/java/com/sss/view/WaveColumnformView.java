package com.sss.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.sss.VisualizerHelper;
import com.sss.spectrum.AppConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * 波形
 */
public class WaveColumnformView extends View implements VisualizerHelper.OnVisualizerEnergyCallBack {
    /**
     * 画笔
     */
    private Paint paint = new Paint();

    /**
     * 数据集
     */
    private List<RectF> newData = new ArrayList<>();

    /**
     * 初始波形Y轴
     */
    private int defultY;
    /**
     * 每一个波形点之间的间距
     */
    private int spacing = 5;
    /**
     * 间距偏移，动态调整
     */
    private int spacingOffset = 0;

    /**
     * 主色调
     */
    private int mainColor = Color.parseColor("#F53F3F");

    public void setMainColor(int mainColor) {
        this.mainColor = mainColor;
    }

    public void setSpacingOffset(int spacingOffset) {
        this.spacingOffset = spacingOffset;
    }

    public WaveColumnformView(Context context) {
        super(context);
    }

    public WaveColumnformView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WaveColumnformView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        paint.setShader(new LinearGradient(0f, getHeight() >> 1, right, getHeight() >> 1,
                new int[]{
                        getColorWithAlpha(0.3f, mainColor),
                        getColorWithAlpha(1f, mainColor),
                        getColorWithAlpha(1f, mainColor),
                        getColorWithAlpha(0.3f, mainColor)
                },
                new float[]{0, 0.1f, 0.9f, 1f},
                Shader.TileMode.CLAMP));

    }

    int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }

    @Override
    public void setWaveData(float[] data, float totalEnergy) {

        paint.setStrokeWidth(3);
        paint.setColor(AppConstant.COLOR);
        spacing = getWidth() / AppConstant.SAMPLE_SIZE + spacingOffset;
        defultY = getHeight() / 2;
        newData.clear();
        for (float f : data) {
            RectF rect = new RectF();
            rect.left = newData.isEmpty() ? 0 : newData.get(newData.size() - 1).right + spacing;
            rect.top = defultY + getOffsetY(true, f);
            rect.right = rect.left + 20;
            rect.bottom = defultY + getOffsetY(false, f);
            newData.add(rect);
        }

    }

    /**
     * 中心点保持距离，该值越大，没有音频输入时默认的长度越长
     */
    int centerHolder = 20;

    private int getOffsetY(boolean top, float data) {
        if (top) {
            return (int) (data * AppConstant.LAGER_OFFSET) + centerHolder;
        } else {
            return (int) (-data * AppConstant.LAGER_OFFSET) - centerHolder;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < newData.size(); i++) {
            canvas.drawRoundRect(newData.get(i), 10, 10, paint);
        }
        invalidate();
    }

}
