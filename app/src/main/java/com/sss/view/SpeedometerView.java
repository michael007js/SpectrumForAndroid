package com.sss.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.sss.spectrum.AppConstant;

/**
 * 速度表
 */
public class SpeedometerView extends View {
    //半径
    private int radiu;
    //圆心点
    private int centerX, centerY;
    //表盘区域
    private RectF rectF = new RectF();
    //正在绘制的圆弧上的点坐标
    private Point point = new Point();
    //角度
    private double angle;
    //圆弧点与圆边距离
    private int distance = 50;
    //色值 红
    private int red = 0;
    //色值 绿
    private int green = 0;
    //色值 蓝
    private int blue = 0;
    private Paint paint = new Paint();

    public SpeedometerView(Context context) {
        super(context);
        init();
    }

    public SpeedometerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpeedometerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
    }


    public void setWaveData(byte[] data) {
        float energy = 0f;
        for (int i = 0; i < data.length; i++) {
            energy += Math.abs(data[i]);
        }
        angle = energy / 10;
//                angle=data[0];
//        Log.e("SSSSS",energy+"");
        red = data[0] * 2;
        green = data[AppConstant.LUMP_COUNT / 3 * 2] * 2;
        blue = data[AppConstant.LUMP_COUNT - 1] * 2;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radiu = h;
        centerX = w / 2;
        centerY = h;
        rectF.left = centerX - h;
        rectF.top = 0;
        rectF.right = centerX + h;
        rectF.bottom = h * 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(10);
        paint.setColor(Color.rgb(0, 0, 0));
        calcPoint();
        canvas.drawArc(rectF, 0, 360, false, paint);
        paint.setColor(Color.rgb(red, green, blue));
        canvas.drawLine(centerX, centerY, point.x, point.y, paint);
        invalidate();
    }


    private void calcPoint() {
        point.x = (int) (centerX + (radiu - distance) * Math.cos((angle + 180) * Math.PI / 180));
        point.y = (int) (centerY + (radiu - distance) * Math.sin((angle + 180) * Math.PI / 180));
    }

}
