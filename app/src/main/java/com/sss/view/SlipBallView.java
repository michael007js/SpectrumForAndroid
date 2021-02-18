package com.sss.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.sss.VisualizerHelper;
import com.sss.bean.SlipBallBean;
import com.sss.spectrum.AppConstant;


/**
 * 伸缩球
 */
public class SlipBallView extends View implements VisualizerHelper.OnVisualizerEnergyCallBack {
    //半径
    private int radius;
    //圆心点
    private int centerX, centerY;

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
        radius = Math.min(w, h) / 3;
        centerX = w / 2;
        centerY = h / 2;
        slipBallBean.paintInner.setShader(new RadialGradient(centerX, centerY, radius, Color.RED, Color.YELLOW, Shader.TileMode.CLAMP));
        slipBallBean.paintOutter.setShader(new RadialGradient(centerX, centerY, radius, Color.YELLOW, Color.RED, Shader.TileMode.CLAMP));


    }


    @Override
    public void setWaveData(byte[] data, float totalEnergy) {
        slipBallBean.offset = (int) (totalEnergy / (AppConstant.isFFT ? 100 : 1000));
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
