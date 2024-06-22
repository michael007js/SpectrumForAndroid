package com.sss.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.sss.Utils;
import com.sss.VisualizerHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 柱状
 */
public class ColumnarView extends View implements VisualizerHelper.OnVisualizerEnergyCallBack {
    //每一个能量柱的宽度
    private int width;
    //每一个能量柱之间的间距
    private final int spacing = Utils.dp2px(1);
    private int blockSpeed = Utils.dp2px(1f);
    //能量块与能量柱之间的距离
    private final int distance = Utils.dp2px(1);

    private final Paint paint = new Paint();
    private final List<Rect> newData = new ArrayList<>();
    private final List<Rect> blockData = new ArrayList<>();
    private int compensate;

    public void setBlockSpeed(int blockSpeed) {
        this.blockSpeed = blockSpeed;
    }

    public ColumnarView(Context context) {
        super(context);
    }

    public ColumnarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColumnarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setWaveData(float[] data, float totalEnergy) {
//        paint.setColor(AppConstant.COLOR);
        int h = getHeight();
        if (newData.isEmpty()) {
            for (float f : data) {
                newData.add(new Rect());
            }
        }
        if (!newData.isEmpty() && h > 0) {
            // 确保width的计算考虑spacing并适应视图宽度
            int totalSpacing = (data.length - 1) * spacing;
            width = (getWidth() - totalSpacing) / data.length;
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
            float lagerOffsetRate = 3.0f;
            int height = (int) (h - data[i] * (1.0f + lagerOffsetRate));
            newData.get(i).top = Math.min(height, h - Utils.dp2px(1));
            newData.get(i).right = newData.get(i).left + width;
            newData.get(i).bottom = h;
        }
        //横屏时摄像头部分无效区域补偿
        compensate = (getWidth() - newData.get(newData.size() - 1).right) / 2;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        paint.setShader(new LinearGradient(0f, 0f, getWidth(), getHeight(), new int[]{0xffff0000, 0xff00ff00, 0xff0000ff}, new float[]{0, 0.5f, 1f}, Shader.TileMode.CLAMP));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < newData.size(); i++) {
            canvas.drawRect(newData.get(i), paint);
        }
        for (int i = 0; i < blockData.size(); i++) {
            canvas.drawRect(blockData.get(i), paint);
        }
        invalidate();
    }
}
