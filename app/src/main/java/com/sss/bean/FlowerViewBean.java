package com.sss.bean;

import android.animation.ValueAnimator;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class FlowerViewBean {
    private float x, y;
    private float speed;
    private float radius;
    private int color;
    private List<Point> points = new ArrayList<>();
    private boolean okey;
    private float duration;

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public boolean isOkey() {
        return okey;
    }

    public List<Point> getPoints() {
        return points;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    private ValueAnimator valueAnimator;

    public void start() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        if (points.size()==0){
            return;
        }
        valueAnimator = ValueAnimator.ofInt(0, points.size() - 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                x = points.get((Integer) animation.getAnimatedValue()).x;
                y = points.get((Integer) animation.getAnimatedValue()).y;
                if ((Integer) animation.getAnimatedValue() == points.size() - 1) {
                    valueAnimator.removeAllUpdateListeners();
                    okey = true;
                }
            }
        });
        valueAnimator.setDuration((long) (1000*duration));
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.start();
    }
}
