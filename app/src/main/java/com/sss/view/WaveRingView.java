package com.sss.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.sss.Utils;
import com.sss.VisualizerHelper;
import com.sss.spectrum.AppConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("ALL")
public class WaveRingView extends View implements VisualizerHelper.OnVisualizerEnergyCallBack {
    private int between = 1;
    private int radius = Utils.dp2px(100);
    private Point centerPoint = new Point();
    private Paint paint = new Paint();
    private int degress;
    private boolean isRotate = false;
    private boolean isRandom = true;
    private boolean isBase = true;
    private boolean isWave = true;
    private boolean isPowerOffset = false;
    private int scope = 10;
    private int value = 100;


    {
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
    }

    public void setRandom(boolean random) {
        isRandom = random;
        isRotate = !random;
    }

    public void setRotate(boolean rotate) {
        isRotate = rotate;
        isRandom = !rotate;
    }

    public void setBase(boolean base) {
        isBase = base;
    }

    public void setWave(boolean wave) {
        isWave = wave;
    }

    public void setPowerOffset(boolean powerOffset) {
        isPowerOffset = powerOffset;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }


    public void setValue(int value) {
        this.value = value;
    }


    private List<WaveRingViewBean> list = new ArrayList<>();

    public WaveRingView(Context context) {
        super(context);
    }

    public WaveRingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WaveRingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerPoint.set(w / 2, h / 2);
    }

    @Override
    public void setWaveData(byte[] data, float totalEnergy) {
        list.clear();
        int total = data.length - value;
        //圆周长
        float totalLength = 3.14f * 2 * radius;
        //每个角度所占用的长度
        float eachWidthByAngle = totalLength / 360;
        //每个能量所占用的长度
        float eachWidthByDataLength = totalLength / total;
        //间隔所占的长度
        float betweenWidth = between * eachWidthByAngle;

        for (int i = 0; i < total; i++) {
            //每个能量在圆环上的角度位置
            float positionAngle = i * 1.0f / total * 360;
            //每个能量的强度百分比
            float powerPercent = isPowerOffset ? 0f : data[i] * 1.0f / 128;
            WaveRingViewBean waveRingViewBean = new WaveRingViewBean();
            waveRingViewBean.width = eachWidthByDataLength - betweenWidth;
            if (data[i] > scope) {
                Utils.calcPoint(centerPoint.x, centerPoint.y, (int) (radius + data[i]) + value, positionAngle + (isRotate ? degress : getRandomAngle()), waveRingViewBean.inner);
                Utils.calcPoint(centerPoint.x, centerPoint.y, (int) (radius - data[i] - powerPercent * data[i]) + value, positionAngle + (isRotate ? degress : getRandomAngle()), waveRingViewBean.outter);
                Utils.calcPoint(centerPoint.x, centerPoint.y, radius + value, positionAngle + (isRotate ? degress : getRandomAngle()), waveRingViewBean.center);
            } else {
                Utils.calcPoint(centerPoint.x, centerPoint.y, radius + value, positionAngle + (isRotate ? degress : getRandomAngle()), waveRingViewBean.inner);
                Utils.calcPoint(centerPoint.x, centerPoint.y, radius + value, positionAngle + (isRotate ? degress : getRandomAngle()), waveRingViewBean.outter);
                Utils.calcPoint(centerPoint.x, centerPoint.y, radius + value, positionAngle + (isRotate ? degress : getRandomAngle()), waveRingViewBean.center);
            }
            list.add(waveRingViewBean);
        }
        invalidate();
    }

    private int randomAngle;
    private int x;
    private Random random = new Random();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.argb((int) (AppConstant.ALPHA * 0.6f), AppConstant.RED, AppConstant.GREEN, AppConstant.BLUE));
        paint.setStrokeWidth(Utils.dp2px(1.8f));
//        for (int i = 0; i < list.size(); i++) {
//            if (i == 0) {
//                canvas.drawLine(list.get(list.size() - 1).center.x, list.get(list.size() - 1).center.y, list.get(0).center.x, list.get(0).center.y, paint);
//            } else {
//                canvas.drawLine(list.get(i - 1).center.x, list.get(i - 1).center.y, list.get(i).center.x, list.get(i).center.y, paint);
//            }
//        }
        if (isBase) {
            paint.setStrokeWidth(Utils.dp2px(1f));
            for (int i = 0; i < list.size(); i++) {
                canvas.drawLine(list.get(i).inner.x, list.get(i).inner.y, list.get(i).outter.x, list.get(i).outter.y, paint);
            }
        }


        if (isWave) {
            paint.setStrokeWidth(Utils.dp2px(1.8f));
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    canvas.drawLine(list.get(list.size() - 1).center.x, list.get(list.size() - 1).center.y, list.get(0).outter.x, list.get(0).outter.y, paint);
                    canvas.drawLine(list.get(0).outter.x, list.get(0).outter.y, list.get(1).center.x, list.get(1).center.y, paint);
                    canvas.drawLine(list.get(1).center.x, list.get(1).center.y, list.get(0).inner.x, list.get(0).inner.y, paint);
                    canvas.drawLine(list.get(0).inner.x, list.get(0).inner.y, list.get(list.size() - 1).center.x, list.get(list.size() - 1).center.y, paint);
                } else if (i == list.size() - 1) {
                    canvas.drawLine(list.get(list.size() - 2).center.x, list.get(list.size() - 2).center.y, list.get(list.size() - 1).outter.x, list.get(list.size() - 1).outter.y, paint);
                    canvas.drawLine(list.get(list.size() - 1).outter.x, list.get(list.size() - 1).outter.y, list.get(0).center.x, list.get(0).center.y, paint);
                    canvas.drawLine(list.get(0).center.x, list.get(0).center.y, list.get(list.size() - 1).inner.x, list.get(list.size() - 1).inner.y, paint);
                    canvas.drawLine(list.get(list.size() - 1).inner.x, list.get(list.size() - 1).inner.y, list.get(list.size() - 2).center.x, list.get(list.size() - 2).center.y, paint);
                } else {
                    canvas.drawLine(list.get(i - 1).center.x, list.get(i - 1).center.y, list.get(i).outter.x, list.get(i).outter.y, paint);
                    canvas.drawLine(list.get(i).outter.x, list.get(i).outter.y, list.get(i + 1).center.x, list.get(i + 1).center.y, paint);
                    canvas.drawLine(list.get(i + 1).center.x, list.get(i + 1).center.y, list.get(i).inner.x, list.get(i).inner.y, paint);
                    canvas.drawLine(list.get(i).inner.x, list.get(i).inner.y, list.get(i - 1).center.x, list.get(i - 1).center.y, paint);
                }
            }
        }

        if (isRotate) {
            if (degress >= 360) {
                degress = 0;
            } else {
                degress++;
            }
        }

        x++;
        if (x > 300) {
            randomAngle = Utils.randomInt(random, 1, 4);
            x = 0;
        }
    }

    private int getRandomAngle() {
        if (isRandom) {
            switch (randomAngle) {
                case 1:
                    return 90;
                case 2:
                    return 180;
                case 3:
                    return 270;
                case 4:
                    return 360;
                default:
                    return 0;
            }
        }
        return 0;
    }

    private static class WaveRingViewBean {
        private Point inner = new Point();
        private Point center = new Point();
        private Point outter = new Point();
        private float width;
    }
}
