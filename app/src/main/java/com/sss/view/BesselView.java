package com.sss.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.sss.Utils;
import com.sss.spectrum.AppConstant;

import java.util.ArrayList;
import java.util.List;

public class BesselView extends View {

    //圆心点
    private int centerX, centerY;
    //每一个顶点之间的间距
    private int spacing = 5;

    private Paint paint = new Paint();
    private Path path = new Path();
    private List<Point> list = new ArrayList<>();

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
        spacing = w / AppConstant.LUMP_COUNT;
        paint.setShader(new LinearGradient(0f, 0f, getWidth(), getHeight(), new int[]{0xffff0000, 0xffaaaa00, 0xffffff00}, new float[]{0, 0.5f, 1f}, Shader.TileMode.CLAMP));
    }

    public void setWaveData(byte[] data) {
        paint.setColor(Color.WHITE);
        list.clear();
        for (int i = 0; i < data.length; i++) {
            Point point = new Point();
            if (list.size() == 0) {
                point.x = 0;
                point.y = (int) (getHeight() + getHeight() / 1.3);
            } else {
                point.x = list.get(list.size() - 1).x + spacing;
                point.y = (int) (getHeight() + getHeight() / 1.3 - data[i] * AppConstant.LAGER_PFFSET * 1.5);
            }
            list.add(point);
        }

        path.reset();
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                path.moveTo(list.get(i).x, list.get(i).y);
            } else {
                if (i < list.size() - 1) {
                    path.cubicTo(list.get(i - 1).x, list.get(i - 1).y, list.get(i).x, list.get(i).y - list.get(i - 1).y, list.get(i + 1).x, list.get(i + 1).y);
                }
            }
        }
        path.moveTo(list.get(0).x, list.get(0).y);
        path.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
        invalidate();
    }
}
