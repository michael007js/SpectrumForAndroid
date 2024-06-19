package com.sss.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import com.sss.Utils;
import com.sss.VisualizerHelper;
import com.sss.spectrum.AppConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.Nullable;

@SuppressWarnings("ALL")
public class AttachmentRingView extends View implements VisualizerHelper.OnVisualizerEnergyCallBack {
    private int between = 1;
    private int radius = Utils.dp2px(100);
    private Point centerPoint = new Point();
    private Paint paint = new Paint();
    private int degress;
    private boolean isRotate = false;
    private boolean isRandom = true;
    private int scope = 50;
    private int start = 10;
    private boolean isColumnar = true;
    private boolean isBomb = false;
    private boolean isWave = false;


    {
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

    public void setScope(int scope) {
        this.scope = scope;
    }

    public void setBomb(boolean bomb) {
        isBomb = bomb;
        isColumnar = !bomb;
        isWave = !isWave;
    }

    public void setColumnar(boolean columnar) {
        isColumnar = columnar;
        isBomb = !columnar;
        isWave = !isWave;
    }

    public void setWave(boolean wave) {
        isWave = wave;
        isBomb = !wave;
        isColumnar = !wave;
    }


    public void setStart(int start) {
        this.start = start;
    }


    private List<AttachmentRingViewBean> list = new ArrayList<>();

    public AttachmentRingView(Context context) {
        super(context);
    }

    public AttachmentRingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AttachmentRingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerPoint.set(w / 2, h / 2);
    }

    @Override
    public void setWaveData(float[] data, float totalEnergy) {
        list.clear();
        int total = data.length - scope;
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
            AttachmentRingViewBean attachmentRingViewBean = new AttachmentRingViewBean();
            attachmentRingViewBean.width = eachWidthByDataLength - betweenWidth;
            Utils.calcPoint(centerPoint.x, centerPoint.y, radius, positionAngle + (isRotate ? degress : getRandomAngle()), attachmentRingViewBean.start);
            Utils.calcPoint(centerPoint.x, centerPoint.y, (int) (radius + data[i] * 1.5) + start, positionAngle + (isRotate ? degress : getRandomAngle()), attachmentRingViewBean.end);
            list.add(attachmentRingViewBean);
        }
        invalidate();
    }

    private int randomAngle;
    private int x;
    private Random random = new Random();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);
//        canvas.drawCircle(centerPoint.x, centerPoint.y, radius, paint);

        if (isBomb) {
            for (int i = 0; i < list.size(); i++) {
                paint.setStrokeWidth(list.get(i).width);
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.argb((int) (AppConstant.ALPHA * 0.3f), AppConstant.RED, AppConstant.GREEN, AppConstant.BLUE));
                canvas.drawLine(list.get(i).start.x, list.get(i).start.y, list.get(i).end.x, list.get(i).end.y, paint);
                paint.setColor(Color.WHITE);
                paint.setStrokeWidth(list.get(i).width);
                canvas.drawPoint(list.get(i).end.x, list.get(i).end.y, paint);
            }
        } else if (isColumnar) {
            for (int i = 0; i < list.size(); i++) {
                paint.setStrokeWidth(list.get(i).width);
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.WHITE);
                canvas.drawLine(list.get(i).start.x, list.get(i).start.y, list.get(i).end.x, list.get(i).end.y, paint);
            }
        } else if (isWave) {
            for (int i = 0; i < list.size(); i++) {
                paint.setStyle(Paint.Style.FILL);
                paint.setStrokeWidth(Utils.dp2px(1));
                paint.setColor(Color.argb((int) (AppConstant.ALPHA * 0.3f), AppConstant.RED, AppConstant.GREEN, AppConstant.BLUE));
                if (i == 0) {
                    canvas.drawLine(list.get(list.size() - 1).end.x, list.get(list.size() - 1).end.y, list.get(0).end.x, list.get(0).end.y, paint);
                } else {
                    canvas.drawLine(list.get(i - 1).end.x, list.get(i - 1).end.y, list.get(i).end.x, list.get(i).end.y, paint);
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

    private static class AttachmentRingViewBean {
        private Point start = new Point();
        private Point end = new Point();
        private float width;
    }
}
