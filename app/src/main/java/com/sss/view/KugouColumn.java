package com.sss.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.sss.Utils;
import com.sss.VisualizerHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class KugouColumn extends View implements VisualizerHelper.OnVisualizerEnergyCallBack {
    /**
     * 每一个能量柱的宽度
     */
    private int width;

    /**
     * 每一个能量柱之间的间距
     */
    private int spacing = Utils.dp2px(1);
    private int blockSpeed = Utils.dp2px(3f);
    //能量块与能量柱之间的距离
    private final int distance = Utils.dp2px(1);
    /**
     * 放大倍率
     */
    private float lagerOffsetRate = 35f;
    /**
     * 横屏时摄像头部分无效区域补偿
     */
    private int compensate;
    int drawListSize = 2;
    private Paint paint = new Paint();
    private final List<Rect> newData = new ArrayList<>();
    private final List<Rect> blockData = new ArrayList<>();
    private final List<List<Rect>> reallyData = new ArrayList<>();

    public KugouColumn(Context context) {
        super(context);
        init();
    }

    public KugouColumn(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KugouColumn(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setAntiAlias(true);
    }


    @Override
    public void setWaveData(float[] data, float totalEnergy) {
        int h = getHeight();
        if (newData.isEmpty()) {
            for (float f : data) {
                newData.add(new Rect());
            }
        }
        //创建两段队列
        if (reallyData.isEmpty()) {
            for (int i = 0; i < drawListSize; i++) {
                int size = data.length / 2;

                List<Rect> list = new ArrayList<>();
                for (int ii = 0; ii < size; ii++) {
                    list.add(new Rect());
                }
                reallyData.add(list);
            }
        }

        if (!newData.isEmpty() && h > 0) {
            // 确保width的计算考虑spacing并适应视图宽度
            int totalSpacing = (data.length - 1) * spacing;
            width = (getWidth() - totalSpacing) / data.length + Utils.dp2px(1.5f);
            //能量块高度
            int blockHeight = width / 2;
            if (blockData.isEmpty() || newData.size() != blockData.size()) {
                blockData.clear();
                for (int i = 0; i < data.length; i++) {
                    Rect rect = new Rect();
                    rect.top = h - blockHeight;
                    rect.bottom = h;
                    blockData.add(rect);
                }
            }
            for (int i = 0; i < blockData.size(); i++) {
                blockData.get(i).left = newData.get(i).left;
                blockData.get(i).right = newData.get(i).right;
                if (newData.get(i).top > 0 && newData.get(i).top < blockData.get(i).top) {
                    blockData.get(i).top = newData.get(i).top - blockHeight - distance;
                } else {
                    blockData.get(i).top = blockData.get(i).top + blockSpeed;
                }
                blockData.get(i).bottom = blockData.get(i).top + blockHeight;


            }
        }
        for (int i = 0; i < data.length; i++) {
            if (i == 0) {
                newData.get(i).left = 0;
            } else {
                newData.get(i).left = newData.get(i - 1).right + spacing;
            }

            // 放大倍率
            int height = (int) (h - data[i] * (1.0f + lagerOffsetRate));
            newData.get(i).top = Math.min(height, h - Utils.dp2px(1));
            newData.get(i).right = newData.get(i).left + width;
            newData.get(i).bottom = h;
        }
        //横屏时摄像头部分无效区域补偿
        compensate = (getWidth() - newData.get(newData.size() - 1).right) / 2;
        for (int i = 0; i < newData.size(); i++) {
            newData.get(i).left += compensate;
            newData.get(i).right += compensate;
        }
        for (int i = 0; i < blockData.size(); i++) {
            if (newData.get(i).height() > Utils.dp2px(1)) {
                blockData.get(i).left += compensate;
                blockData.get(i).right += compensate;
            }
        }
        int aaa = getWidth() / 2 / drawListSize;
        if (!blockData.isEmpty() && !newData.isEmpty()) {
            for (int i = 0; i < reallyData.get(0).size(); i++) {
                reallyData.get(0).get(i).left = newData.get(i).left + aaa;
                reallyData.get(0).get(i).right = newData.get(i).right + aaa;
                reallyData.get(0).get(i).bottom = newData.get(i).bottom;
                reallyData.get(0).get(i).top = blockData.get(i).top;
            }
            int offset = reallyData.get(0).size() - 1;
            for (int i = 0; i < reallyData.get(1).size(); i++) {
                reallyData.get(1).get(i).left = newData.get(i + offset).left - aaa;
                reallyData.get(1).get(i).right = newData.get(i + offset).right - aaa;
                reallyData.get(1).get(i).bottom = newData.get(i + offset).bottom;
                reallyData.get(1).get(i).top = blockData.get(i + offset).top;
            }

        }
    }
    private List<LinearGradient> gradients = new ArrayList<>();
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (getWidth() > 0 && getHeight() > 0) {
            gradients.clear();
            for (int i = 0; i < drawListSize; i++) {
                LinearGradient linearGradient;
                if (i % 2 == 0) {
                    // 偶数索引：透明度从1.0到0.3
                    linearGradient = new LinearGradient(w, 0f, 0f, h,
                            new int[]{0xccfdf4be, 0xccfedbb6, 0xccfec9c7, 0xccfec2df, 0xccfdb2e6},
                            new float[]{0, 0.25f, 0.5f, 0.75f, 1f},
                            Shader.TileMode.CLAMP);
                } else {
                    // 奇数索引：透明度从0.3到1.0
                    linearGradient = new LinearGradient(0f, 0f, w, h,
                            new int[]{0x4dfdf4be, 0x7dfedbb6, 0xbafec9c7, 0xe5fec2df, 0xfffdb2e6},
                            new float[]{0, 0.25f, 0.5f, 0.75f, 1f},
                            Shader.TileMode.CLAMP);
                }
                gradients.add(linearGradient);
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < reallyData.size(); i++) {
            paint.setShader(gradients.get(i));
            for (int j = 0; j < reallyData.get(i).size(); j++) {
                canvas.drawRect(reallyData.get(i).get(j), paint);
            }
        }
        invalidate();
    }
}
