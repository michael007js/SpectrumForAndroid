package com.sss.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.sss.spectrum.AppConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * 柱状
 */
public class ColumnarView extends View {
    //每一个立柱的宽度
    private int width;
    //每一个立柱之间的间距
    private int spacing = 5;
    //放大量
    private int lagerOffset = 2;

    private Paint paint = new Paint();
    private List<Rect> newData = new ArrayList<>();


    public ColumnarView(Context context) {
        super(context);
    }

    public ColumnarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColumnarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setWaveData(byte[] data) {
        width = getWidth() / AppConstant.LUMP_COUNT;
        newData.clear();
        for (int i = 0; i < data.length; i++) {
            Rect rect = new Rect();
            if (newData.size() == 0) {
                rect.left = 0;
            } else {
                rect.left = newData.get(newData.size() - 1).right + spacing;
            }
            rect.top = getHeight() - (Math.abs(127 - data[i])) * lagerOffset;
            rect.right = rect.left + width;
            rect.bottom = getHeight();
            newData.add(rect);
        }
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < newData.size(); i++) {
            canvas.drawRect(newData.get(i), paint);
        }
    }
}
