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
import com.sss.VisualizerHelper;
import com.sss.bean.FlowerViewBean;
import com.sss.spectrum.AppConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.Nullable;

public class ColorBubbleView extends View implements VisualizerHelper.OnVisualizerEnergyCallBack {
    private Point centerPoint = new Point();
    private Paint paint = new Paint();
    private float distance;
    //绘制辅助信息
    private boolean drawAssist = false;
    private boolean extended = true;
    private float percent;
    private int balance = 800;
    private int amplitude = 2;
    private Random random = new Random();

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public void setDrawAssist(boolean drawAssist) {
        this.drawAssist = drawAssist;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setAmplitude(int amplitude) {
        this.amplitude = amplitude;
    }

    public ColorBubbleView(Context context) {
        super(context);
        init();
    }

    public ColorBubbleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorBubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(Utils.sp2px(50f));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerPoint.set(w / 2, h / 2);
    }

    @Override
    public void setWaveData(float[] data, float totalEnergy) {
        distance = Math.min(totalEnergy / amplitude, getWidth() / 2);
//        distance = totalEnergy / amplitude;
        percent = distance * 1.0f / centerPoint.x;
//        Log.e("SSSSS", balance + "---" + distance + "---" + percent + "---" + list.size());

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isOkey()) {
                if (list.get(i).getY() >= getHeight()) {
                    list.remove(list.get(i));
                    i--;
                }
            }
        }
        createPoints(totalEnergy > balance ? 0 : 1);

        invalidate();
    }

    private List<FlowerViewBean> list = new ArrayList<>();

    private void createPoints(int direction) {

        FlowerViewBean flowerViewBean = new FlowerViewBean();
        if (direction == 0) {
            flowerViewBean.setX(centerPoint.x - distance * percent);
        } else {
            flowerViewBean.setX(centerPoint.x + distance * percent);
        }

        flowerViewBean.setY(getHeight() - getHeight() * percent);
        flowerViewBean.setSpeed(percent * 10 * 5);
        flowerViewBean.setDuration(percent);
        float radius = centerPoint.x - distance * percent / 2;
        radius = Math.min(radius, getHeight() - 30);

        for (int i = 0; i < 90; i++) {
            Point point = new Point();
            if (direction == 0) {
                flowerViewBean.getPoints().add(Utils.calcPoint((int) (centerPoint.x - radius * percent), getHeight(), (int) (radius * percent), 360 - i, point));
            } else {
                flowerViewBean.getPoints().add(Utils.calcPoint((int) (centerPoint.x + radius * percent), getHeight(), (int) (radius * percent), 180 + i, point));
            }
        }
        if (extended) {
            for (int i = 0; i < 90 * percent; i++) {
                Point point = new Point();
                if (direction == 0) {
                    flowerViewBean.getPoints().add(Utils.calcPoint((int) (centerPoint.x - radius * percent), getHeight(), (int) (radius * percent), 270 - i, point));
                } else {
                    flowerViewBean.getPoints().add(Utils.calcPoint((int) (centerPoint.x + radius * percent), getHeight(), (int) (radius * percent), 270 + i, point));
                }

            }
        }
        flowerViewBean.setRadius(30);
        flowerViewBean.setColor(Color.argb(
                Utils.randomInt(random, (int) (AppConstant.ALPHA * percent), AppConstant.ALPHA),
                Utils.randomInt(random, (int) (AppConstant.RED * percent), AppConstant.RED),
                Utils.randomInt(random, (int) (AppConstant.GREEN * percent), AppConstant.GREEN),
                Utils.randomInt(random, (int) (AppConstant.BLUE * percent), AppConstant.BLUE)
        ));
        list.add(flowerViewBean);
        flowerViewBean.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (drawAssist) {
            paint.setColor(Color.RED);
            paint.setStrokeWidth(1);
            canvas.drawLine(centerPoint.x, 0, centerPoint.x, centerPoint.y, paint);
            canvas.drawLine(centerPoint.x, centerPoint.y, centerPoint.x - distance, centerPoint.y, paint);
            canvas.drawLine(centerPoint.x, centerPoint.y, centerPoint.x + distance, centerPoint.y, paint);
            paint.setStrokeWidth(10);
            canvas.drawPoint(centerPoint.x - distance * percent, getHeight() - getHeight() * percent, paint);
            canvas.drawPoint(centerPoint.x + distance * percent, getHeight() - getHeight() * percent, paint);
            paint.setStrokeWidth(1);
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.get(i).getPoints().size(); j++) {
                    canvas.drawPoint(list.get(i).getPoints().get(j).x, list.get(i).getPoints().get(j).y, paint);
                }
//
            }
        }

        paint.setStrokeWidth(1);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isOkey()) {
                list.get(i).setY(list.get(i).getY() + list.get(i).getSpeed());
            }
            paint.setColor(list.get(i).getColor());
            canvas.drawCircle(list.get(i).getX(), list.get(i).getY(), list.get(i).getRadius(), paint);
        }


    }

}
