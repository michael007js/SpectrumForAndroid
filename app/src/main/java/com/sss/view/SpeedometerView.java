package com.sss.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.sss.Utils;
import com.sss.VisualizerHelper;
import com.sss.spectrum.AppConstant;

/**
 * 速度表
 */
public class SpeedometerView extends View implements VisualizerHelper.OnVisualizerEnergyCallBack {
    //半径
    private int radius;
    //圆心点
    private int centerX, centerY;
    //表盘区域
    private RectF rectF = new RectF();
    //正在绘制的圆弧上的点坐标
    private Point point = new Point();
    //角度
    private float angle;
    //圆弧点与圆边距离
    private int distance = 50;

    private float maxSpeed=100f;

    private Paint paint = new Paint();

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

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
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(Utils.sp2px(50f));
    }

    private float totalEnergy;

    @Override
    public void setWaveData(byte[] data, float totalEnergy) {
        this.totalEnergy = totalEnergy;

        angle = totalEnergy / (AppConstant.isFFT ? 10 : 100);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = h;
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
        paint.setColor(Color.WHITE);
        Utils.calcPoint(centerX, centerY, radius - distance, angle + 180, point);
        canvas.drawArc(rectF, 0, 360, false, paint);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(centerX, centerY, point.x, point.y, paint);
        paint.setStyle(Paint.Style.FILL);
        if (totalEnergy / 5> maxSpeed){
            canvas.drawText( "已超速" , getWidth() / 2, getHeight() / 2+paint.descent() - paint.ascent()*2, paint);
            paint.setColor(Color.RED);
        }
        canvas.drawText(totalEnergy / 5 + "km/h", getWidth() / 2, getHeight() / 2, paint);
        paint.setStyle(Paint.Style.STROKE);
        invalidate();
    }

//    private void calcPoint() {
//        point.x = (int) (centerX + (radiu - distance) * Math.cos((angle + 180) * Math.PI / 180));
//        point.y = (int) (centerY + (radiu - distance) * Math.sin((angle + 180) * Math.PI / 180));
//    }

}
