package com.sss.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import com.sss.VisualizerHelper;
import com.sss.spectrum.AppConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * 波形
 */
public class WaveformView extends View implements VisualizerHelper.OnVisualizerEnergyCallBack {
    //初始波形Y轴
    private int defultY;
    //每一个波形点之间的间距
    private int spacing = 5;
    private int offset = 0;

    public void setOffset(int offset) {
        this.offset = offset;
    }

    private Paint paint = new Paint();

    private List<Point> newData = new ArrayList<>();


    public WaveformView(Context context) {
        super(context);
    }

    public WaveformView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WaveformView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setWaveData(float[] data, float totalEnergy) {

        paint.setStrokeWidth(3);
        paint.setColor(AppConstant.COLOR);
        spacing = getWidth() / AppConstant.SAMPLE_SIZE + offset;
        defultY = getHeight() / 2;
        newData.clear();
        for (int i = 0; i < data.length; i++) {
            if (i == 0 || i == i - 1) {
                newData.add(new Point(0, defultY));
            } else {
                newData.add(new Point(spacing * i, defultY + getOffsetY(i, data[i])));
            }
        }
    }

    private int getOffsetY(int i, float data) {
        if (i % 2 == 0) {
            return (int) (data * AppConstant.LAGER_OFFSET);
        } else {
            return (int) (-data * AppConstant.LAGER_OFFSET);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < newData.size(); i++) {
            if (i > 1) {
                canvas.drawLine(newData.get(i - 1).x, newData.get(i - 1).y, newData.get(i).x, newData.get(i).y, paint);
            }
        }
        invalidate();
    }

}
