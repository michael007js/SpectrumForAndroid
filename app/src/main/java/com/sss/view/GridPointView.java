package com.sss.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.sss.VisualizerHelper;
import com.sss.spectrum.AppConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 悦动网格
 */
public class GridPointView extends View implements VisualizerHelper.OnVisualizerEnergyCallBack {
    //每一格尺寸比例
    private float pointSizePercent = 0.05f;
    //横向格子数量
    private int landscapeCount;
    //纵向格子数量
    private int verticaCount;
    //背景网格
    private List<Rect> rectList = new ArrayList<>();
    //实时网格
    private List<Rect> showList = new ArrayList<>();
    private Paint paint = new Paint();
    private Random random = new Random();

    private boolean enable = true;

    public GridPointView(Context context) {
        this(context, null);
    }

    public GridPointView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setPointSizePercent(float pointSizePercent) {
        this.pointSizePercent = pointSizePercent;
        enable = false;
        requestSize(getWidth(), getHeight());
    }

    public GridPointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setAntiAlias(true);
    }


    @Override
    public void setWaveData(byte[] data, float totalEnergy) {
//        Log.e("SSSSS",energy+"");
        showList.clear();
        if (rectList.size()>1) {
            for (int i = 0; i < totalEnergy / (AppConstant.isFFT ? 100 : 1000); i++) {
                showList.add(rectList.get(random.nextInt(rectList.size() - 1)));
            }

            enable = true;
            invalidate();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        requestSize(w, h);

    }

    private void requestSize(int w, int h) {
        rectList.clear();
        int size = (int) (w * pointSizePercent);
        landscapeCount = w / size;
        verticaCount = h / size;
        for (int i = 0; i < landscapeCount; i++) {
            for (int j = 0; j < verticaCount; j++) {
                Rect rect = new Rect();
                if (rectList.size() == 0) {
                    rect.left = 0;
                    rect.top = 0;
                    rect.right = rect.left + size;
                    rect.bottom = rect.top + size;
                } else {
                    rect.left = i == 0 ? 0 : i * size;
                    rect.top = j == 0 ? 0 : j * size;
                    rect.right = rect.left + size;
                    rect.bottom = rect.top + size;
                }
                rectList.add(rect);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (enable) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(AppConstant.COLOR);
            for (int i = 0; i < rectList.size(); i++) {
                canvas.drawRect(rectList.get(i), paint);
            }
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(AppConstant.COLOR);
            for (int i = 0; i < showList.size(); i++) {
                canvas.drawRect(showList.get(i), paint);
            }
        }
    }
}
