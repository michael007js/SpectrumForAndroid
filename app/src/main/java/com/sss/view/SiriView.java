package com.sss.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.sss.Utils;
import com.sss.bean.SiriViewBean;
import com.sss.spectrum.AppConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * 仿siri那个智障倾听时的效果
 */
public class SiriView extends View {

    private int defultY;
    private Paint paint = new Paint();
    private List<SiriViewBean> attribute = new ArrayList<>();
    private boolean enable;
    private List<Point> pointFList = new ArrayList<>();
    private PathMeasure pathMeasure = new PathMeasure();

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public SiriView(Context context) {
        this(context, null);
    }

    public SiriView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SiriView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
        pointFList.clear();
        for (int i = 0; i < 10; i++) {
            pointFList.add(new Point());
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        defultY = h / 2;
        attribute.clear();
        float center = 0.5f;
        attribute.add(createScope(200, 109, 106, 160, 0, center - 0.4f, center + 0.1f));
        attribute.add(createScope(200, 144, 109, 131, 0.1f, center - 0.3f, center + 0.2f));
        attribute.add(createScope(200, 140, 147, 94, 0.35f, center - 0.15f, center + 0.15f));
        attribute.add(createScope(200, 117, 157, 145, 0.1f, center - 0.2f, center + 0.4f));
        attribute.add(createScope(200, 54, 102, 126, 0, center - 0.1f, center + 0.2f));
    }

    private SiriViewBean createScope(int a, int r, int g, int b, float heightPercent, float leftDistance, float rightDistance) {
        SiriViewBean siriViewBean = new SiriViewBean();
        siriViewBean.color = Color.argb(a, r, g, b);
        siriViewBean.path = new Path();
        siriViewBean.heightPercent = heightPercent;
        siriViewBean.leftDistance = leftDistance;
        siriViewBean.rightDistance = rightDistance;
        return siriViewBean;
    }


    public void setWaveData(byte[] data) {
        if (!enable) {
            return;
        }
        for (int i = 0; i < attribute.size(); i++) {
            pointFList.get(0).x = 0;
            pointFList.get(0).y = defultY;
            pointFList.get(1).x = (int) (attribute.get(i).leftDistance * getWidth());
            pointFList.get(1).y = defultY;
            pointFList.get(2).x = (int) ((attribute.get(i).rightDistance + attribute.get(i).leftDistance) / 2 * getWidth());
            pointFList.get(2).y = (int) (defultY - defultY * attribute.get(i).heightPercent - Math.abs(getData(data, i)));
            pointFList.get(3).x = (int) (attribute.get(i).rightDistance * getWidth());
            pointFList.get(3).y = defultY;
            pointFList.get(4).x = getWidth();
            pointFList.get(4).y = defultY;
            pointFList.get(5).x = (int) (attribute.get(i).rightDistance * getWidth());
            pointFList.get(5).y = defultY;
            pointFList.get(6).x = (int) ((attribute.get(i).rightDistance + attribute.get(i).leftDistance) / 2 * getWidth());
            pointFList.get(6).y = (int) (defultY + defultY * attribute.get(i).heightPercent + Math.abs(getData(data, i)));
            pointFList.get(7).x = (int) (attribute.get(i).leftDistance * getWidth());
            pointFList.get(7).y = defultY;
            pointFList.get(8).x = 0;
            pointFList.get(8).y = defultY;
            Utils.calculateBesselSmoothPath(0.1f, 10, true, pathMeasure, attribute.get(i).path, pointFList);
        }
    }

    private byte getData(byte[] data, int index) {
        if (index == 0) {
            return data[0];
        } else if (index == 1) {
            return data[2];
        } else if (index == 2) {
            return data[4];
        } else if (index == 3) {
            return data[6];
        } else if (index == 4) {
            return data[8];
        } else {
            return data[0];
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < attribute.size(); i++) {
            if (
                    i == 0
                            ||
                            i == 1
                            ||
                            i == 2
                            ||
                            i == 3
                            ||
                            i == 4
            ) {
                paint.setColor(attribute.get(i).color);
                canvas.drawPath(attribute.get(i).path, paint);
            }
        }
        invalidate();
    }
}
