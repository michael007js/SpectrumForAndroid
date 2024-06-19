package com.sss.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.sss.Utils;
import com.sss.VisualizerHelper;
import com.sss.spectrum.AppConstant;
import com.sss.spectrum.R;

/**
 * 动感の圆
 */
public class RotatingCircleView extends View implements VisualizerHelper.OnVisualizerEnergyCallBack {
    //中心点
    private int centerX, centerY;
    //音频能量百分比
    private float energyPercent = 0;
    //旋转角度
    private float degress = 0f;
    //半径
    private int radius = Utils.dp2px(200);

    private final float maxDistance = Utils.dp2px(100);

    private Bitmap bitmap;
    private Matrix matrix = new Matrix();
    private Paint paint = new Paint();
    private RectF bitmapRect = new RectF();
    private ValueAnimator valueAnimator;

    public RotatingCircleView(Context context) {
        super(context);
        init();
    }

    public RotatingCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RotatingCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setAntiAlias(true);
        bitmap = Utils.getCirleBitmap(radius, Utils.getScaleBitmap(radius, R.mipmap.michael007js, getResources()), paint);
        valueAnimator = ValueAnimator.ofFloat(0f, 359.9f);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setDuration(10000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                degress = (float) animation.getAnimatedValue();
            }
        });
        valueAnimator.start();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = Utils.dp2px(100);
        }
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = Utils.dp2px(100);
        }
        centerX = widthSize / 2;
        centerY = heightSize / 2;
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    public void setWaveData(float[] data, float totalEnergy) {

        if (bitmap != null) {
            energyPercent = totalEnergy / (AppConstant.isFFT ? 100f : 1000f);
            bitmapRect.left = centerX - 100;
            bitmapRect.top = centerY - 100;
            bitmapRect.right = centerX + 100;
            bitmapRect.bottom = centerY + 100;
        }
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Log.e("SSSSS", energyPercent + "");
        bitmapRect.left = bitmapRect.left - maxDistance;
        bitmapRect.top = bitmapRect.top - maxDistance;
        bitmapRect.right = bitmapRect.right + maxDistance;
        bitmapRect.bottom = bitmapRect.bottom + maxDistance;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            paint.setColor(Color.argb(AppConstant.ALPHA * energyPercent, 100 + AppConstant.RED * energyPercent, 120 + AppConstant.GREEN * energyPercent, 150 + AppConstant.BLUE * energyPercent));
        } else {
            paint.setColor(Color.argb((int) (AppConstant.ALPHA * energyPercent), (int) (AppConstant.ALPHA * energyPercent), (int) (100 + AppConstant.RED * energyPercent), (int) (150 + AppConstant.BLUE * energyPercent)));
        }
        canvas.drawArc(bitmapRect, 0, 360, false, paint);
        paint.setAlpha(AppConstant.ALPHA);
        canvas.rotate(degress, centerX, centerY);
        canvas.translate(centerX - radius / 2, centerY - radius / 2);
        canvas.drawBitmap(bitmap, matrix, paint);
    }

}
