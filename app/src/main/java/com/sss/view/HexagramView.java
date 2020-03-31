package com.sss.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.sss.Utils;
import com.sss.spectrum.AppConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 六芒星
 * Do Mo Ke Sa La Mo!
 * 出来吧、光能使者!
 */
public class HexagramView extends View {
    //总能量
    private float energy;
    //外星实时旋转角度
    private float angle = 0;
    //线宽
    private int stroke = 3;
    //两个六芒星之间的半径间距
    private int radiusDistance = Utils.dp2px(8);
    //外星
    private int radiusOutter;
    //内星
    private int radiusInner;
    //圆心点
    private int centerX, centerY;
    //内星六个点坐标
    private List<Point> innerPoints = new ArrayList<>();
    //外星六个点坐标
    private List<Point> outterPoints = new ArrayList<>();
    private Paint paint = new Paint();
    private Random random = new Random();

    private boolean enable;

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public HexagramView(Context context) {
        super(context);
        init();
    }

    public HexagramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HexagramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setStrokeWidth(stroke);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radiusOutter = Math.min(w, h) / 2 - stroke * 2;
        radiusInner = radiusOutter - radiusDistance;
        centerX = w / 2 - stroke;
        centerY = h / 2 - stroke;
        outterPoints.clear();
        for (int i = 0; i < 6; i++) {
            outterPoints.add(Utils.calcPoint(centerX, centerY, radiusOutter, Utils.getAngle((i + 1) * 60), new Point()));
        }

        innerPoints.clear();
        for (int i = 0; i < 6; i++) {
            innerPoints.add(new Point());
        }
        calcHexagramPoint(innerPoints, radiusInner, angle);
    }


    public void setWaveData(byte[] data) {
        if (!enable){
            return;
        }
        energy = 0f;
        for (int i = 0; i < data.length; i++) {
            energy += Math.abs(data[i]);
        }
        Log.e("SSSSS", energy + "");
        calcHexagramPoint(innerPoints, (int) (radiusInner * energy / 10000), energy);
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawHexagram(canvas, radiusInner, innerPoints);
        angle = angle > 359.9 ? 0 : angle + 1;
        canvas.rotate(angle,centerX,centerY);
        drawHexagram(canvas, radiusOutter, outterPoints);
    }

    private void calcHexagramPoint(List<Point> list, int radius, float offsetAngle) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).x = Utils.calcPoint(centerX, centerY, radius, Utils.getAngle((i + 1) * 60 + offsetAngle), list.get(i)).x;
            list.get(i).y = Utils.calcPoint(centerX, centerY, radius, Utils.getAngle((i + 1) * 60 + offsetAngle), list.get(i)).y;
        }
    }

    private void drawHexagram(Canvas canvas, int radius, List<Point> list) {
        if (list.size() != 6) {
            return;
        }
        if (list==outterPoints){
            paint.setColor(Color.argb(
                    Utils.randomInt(random, (int) (AppConstant.RED * (energy / 10000f)), AppConstant.ALPHA),
                    Utils.randomInt(random, (int) (AppConstant.RED * (energy / 10000f)), AppConstant.RED),
                    Utils.randomInt(random, (int) (AppConstant.RED * (energy / 10000f)), AppConstant.GREEN),
                    Utils.randomInt(random, (int) (AppConstant.RED * (energy / 10000f)), AppConstant.BLUE)
                    )
            );
        }else {
            paint.setColor(AppConstant.COLOR);
        }
        canvas.drawCircle(centerX, centerY, radius, paint);
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                canvas.drawLine(list.get(0).x, list.get(0).y, list.get(2).x, list.get(2).y, paint);
            } else if (i == 1) {
                canvas.drawLine(list.get(1).x, list.get(1).y, list.get(3).x, list.get(3).y, paint);
            } else if (i == 2) {
                canvas.drawLine(list.get(2).x, list.get(2).y, list.get(4).x, list.get(4).y, paint);
            } else if (i == 3) {
                canvas.drawLine(list.get(3).x, list.get(3).y, list.get(5).x, list.get(5).y, paint);
            } else if (i == 4) {
                canvas.drawLine(list.get(4).x, list.get(4).y, list.get(0).x, list.get(0).y, paint);
            } else if (i == 5) {
                canvas.drawLine(list.get(5).x, list.get(5).y, list.get(1).x, list.get(1).y, paint);
            }
        }
    }

}
