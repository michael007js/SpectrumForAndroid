package com.sss.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.sss.Utils;
import com.sss.bean.CircleRoundViewBean;
import com.sss.spectrum.AppConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 扩散の环
 */
public class CircleRoundView extends View {
    float energy = 0;
    //当频谱能量大于该值时绘制
    private int energyEnable = 800;
    //半径
    private int minRadius = Utils.dp2px(30);
    //透明衰减触发位置
    private int transparencyFallOffTrigger = 1;
    //透明度衰减量
    private int transparencyFallOffValue = 5;
    //扩散速度
    private int speed = 20;
    //中心点X轴坐标
    private int centerX;
    //中心点Y轴坐标
    private int centerY;
    //圆环绘制区域
    private RectF rectF = new RectF();
    //每个圆环信息储存集合
    private List<CircleRoundViewBean> list = new ArrayList<>();
    private Random random = new Random();
    private Paint paint = new Paint();

    public CircleRoundView(Context context) {
        super(context);
        init();
    }

    public CircleRoundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleRoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void setWaveData(byte[] data) {
        spread();
        energy = 0f;
        for (int i = 0; i < data.length; i++) {
            energy += Math.abs(data[i]);
        }
//        Log.e("SSSSS", energy + "");
        if (energy > energyEnable) {
            CircleRoundViewBean circleRoundViewBean = new CircleRoundViewBean();
            circleRoundViewBean.radius = (int) (minRadius + energy / 1000);
            circleRoundViewBean.red = Utils.randomInt(random, (int) (AppConstant.RED * (energy / 10000f)), AppConstant.RED);
            circleRoundViewBean.green = Utils.randomInt(random, (int) (AppConstant.GREEN * (energy / 10000f)), AppConstant.GREEN);
            circleRoundViewBean.blue = Utils.randomInt(random, (int) (AppConstant.BLUE * (energy / 10000f)), AppConstant.BLUE );
            list.add(circleRoundViewBean);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
    }

    private void init() {
        paint.setColor(AppConstant.COLOR);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);
    }

    public int red;
    public int green;
    public int blue;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        red = Utils.randomInt(random, (int) (AppConstant.RED * (energy / 10000f)), AppConstant.RED);
        green = Utils.randomInt(random, (int) (AppConstant.GREEN * (energy / 10000f)), AppConstant.GREEN);
        blue = Utils.randomInt(random, (int) (AppConstant.BLUE  * (energy / 10000f)), AppConstant.BLUE );
        for (int i = 0; i < list.size(); i++) {
            rectF.left = centerX - list.get(i).radius;
            rectF.right = centerX + list.get(i).radius;
            rectF.top = centerY - list.get(i).radius;
            rectF.bottom = centerY + list.get(i).radius;
            paint.setColor(Color.argb(getAlpha(i, list.get(i)), list.get(i).red, list.get(i).green, list.get(i).blue));
            canvas.drawArc(rectF, 0, 360, false, paint);
        }
        removeOutOfBorder();
        invalidate();
    }

    private int getAlpha(int i, CircleRoundViewBean circleRoundViewBean) {
        return circleRoundViewBean.radius > 50 ? AppConstant.ALPHA - i * transparencyFallOffValue : AppConstant.ALPHA;
//        return i < list.size() - transparencyFallOffTrigger ? AppConstant.ALPHA  - i * transparencyFallOffValue : AppConstant.ALPHA;
    }

    private void spread() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).radius += speed;
        }
    }

    private void removeOutOfBorder() {
        if (list.size() > 0) {
            if (list.get(0).radius > Math.max(getWidth(), getHeight())) {
                list.remove(0);
            }
        }
//        Log.e("SSSSS", list.size() + "");

    }

}
