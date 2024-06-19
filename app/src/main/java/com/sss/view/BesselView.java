package com.sss.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.sss.Utils;
import com.sss.VisualizerHelper;
import com.sss.spectrum.AppConstant;

import java.util.ArrayList;
import java.util.List;

public class BesselView extends View implements VisualizerHelper.OnVisualizerEnergyCallBack {

    //圆心点
    private int centerX, centerY;
    //每一个顶点之间的间距
    private int spacing = 5;

    private float sharpenRatio = 0.5f;


    private Paint paint = new Paint();
    private Path path = new Path();
    private List<PointF> list = new ArrayList<>();

    public void setSharpenRatio(float sharpenRatio) {
        this.sharpenRatio = sharpenRatio;
    }

    public BesselView(Context context) {
        super(context);
    }

    public BesselView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BesselView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setAntiAlias(true);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h;
        spacing = w / AppConstant.SAMPLE_SIZE;
        paint.setShader(new LinearGradient(0f, 0f, getWidth(), getHeight(), new int[]{0xffff0000, 0xffaaaa00, 0xffffff00}, new float[]{0, 0.5f, 1f}, Shader.TileMode.CLAMP));
    }

    @Override
    public void setWaveData(float[] data, float totalEnergy) {
        paint.setColor(Color.WHITE);
        list.clear();
        for (int i = 0; i < data.length; i++) {
            PointF point = new PointF();
            if (list.size() == 0) {
                point.x = 0;
                point.y = getHeight();
            } else {
                point.x = list.get(list.size() - 1).x + spacing;
                point.y = getHeight() - data[i] * AppConstant.LAGER_OFFSET * 1.5f;
            }
            list.add(point);
        }

        path.reset();
        Utils.calculateBezier3(list, sharpenRatio, path);
        path.lineTo(getWidth(), getHeight());
        path.lineTo(0, getHeight());
        path.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
        invalidate();
    }

}
