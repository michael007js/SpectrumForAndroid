package com.sss.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.sss.bean.SlipBallBean;
import com.sss.spectrum.AppConstant;


/**
 * 伸缩球
 */
public class SlipBallView extends View {
    //总能量
    private float energy;
    //外星
    private int radius;
    //线宽
    private int stroke = 2;
    //实时旋转角度
    private float angle = 0;
    //圆心点
    private int centerX, centerY;

    private boolean enable;

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    private SlipBallBean slipBallBean = new SlipBallBean();

    public SlipBallView(Context context) {
        super(context);
        init();
    }

    public SlipBallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlipBallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        slipBallBean = new SlipBallBean();
        slipBallBean.paintInner.setAntiAlias(true);
        slipBallBean.paintInner.setStyle(Paint.Style.FILL);
        slipBallBean.paintOutter.setAntiAlias(true);
        slipBallBean.paintOutter.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = Math.min(w, h) / 3 - stroke * 2;
        centerX = w / 2 - stroke;
        centerY = h / 2 - stroke;
        slipBallBean.paintInner.setShader(new RadialGradient(centerX, centerY, radius, Color.RED, Color.YELLOW, Shader.TileMode.CLAMP));
        slipBallBean.paintOutter.setShader(new RadialGradient(centerX, centerY, radius, Color.YELLOW, Color.RED, Shader.TileMode.CLAMP));


    }

    public void setWaveData(byte[] data) {
        if (!enable) {
            return;
        }
        energy = 0f;
        for (int i = 0; i < data.length; i++) {
            energy += data[i];
        }
        slipBallBean.offset = (int) (energy / (AppConstant.isFFT ? 100 : 1000));
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        slipBallBean.radiusOutter = (int) (radius + Math.sqrt(slipBallBean.offset * slipBallBean.offset * 3));
        slipBallBean.radiusInner = radius - radius / 2;
        canvas.drawCircle(centerX, centerY, slipBallBean.radiusOutter, slipBallBean.paintOutter);
        canvas.drawCircle(centerX, centerY, slipBallBean.radiusInner, slipBallBean.paintInner);

    }


}
