package com.sss.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.sss.bean.AiVoiceViewBean;
import com.sss.spectrum.AppConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * AI语音
 */
public class AiVoiceView extends View {
    //初始Y轴
    private int defultY;


    private Paint paint = new Paint();

    private List<AiVoiceViewBean> attribute = new ArrayList<>();
    private List<Path> paths = new ArrayList<>();


    public AiVoiceView(Context context) {
        super(context);
        init();
    }

    public AiVoiceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AiVoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
        AiVoiceViewBean red = new AiVoiceViewBean();
        red.color = Color.argb(AppConstant.ALPHA, AppConstant.RED, 0, 0);
        red.start = 0f;
        red.end = 0.8f;
        attribute.add(red);
        paths.add(new Path());
        AiVoiceViewBean green = new AiVoiceViewBean();
        green.color = Color.argb(AppConstant.ALPHA, 0, AppConstant.GREEN, 0);
        green.start = 0.1f;
        green.end = 0.9f;
        attribute.add(green);
        paths.add(new Path());
        AiVoiceViewBean blue = new AiVoiceViewBean();
        blue.color = Color.argb(AppConstant.ALPHA, 0, 0, AppConstant.BLUE);
        blue.start = 0.2f;
        blue.end = 1.0f;
        attribute.add(blue);
        paths.add(new Path());

    }


    public void setWaveData(byte[] data) {
        if (attribute.size() != paths.size()) {
            return;
        }
        defultY = getHeight() / 2;
        for (int i = 0; i < attribute.size(); i++) {
            paths.get(i).reset();
            paths.get(i).moveTo(getWidth() * attribute.get(i).start, defultY);
            paths.get(i).cubicTo(
                    getWidth() * attribute.get(i).start, defultY,
                    getWidth() * (attribute.get(i).end + attribute.get(i).start) / 2, defultY - getEnergyByPoint(attribute.get(i), data),
                    getWidth() * attribute.get(i).end, defultY
            );
            paths.get(i).cubicTo(
                    getWidth() * attribute.get(i).end, defultY,
                    getWidth() * (attribute.get(i).start + attribute.get(i).end) / 2, defultY + getEnergyByPoint(attribute.get(i), data),
                    getWidth() * attribute.get(i).start, defultY
            );

            paths.get(i).close();
        }
    }

    private int getEnergyByPoint(AiVoiceViewBean bean, byte[] data) {
        int index = (int) (data.length * (bean.end + bean.start) / 2);
//        Log.e("SSSSS", index + "");
        if (index < data.length) {
            return (Math.abs(Math.abs(data[index])) - 2) * AppConstant.LAGER_OFFSET;
        } else {
            return (Math.abs(Math.abs(data[data.length - 1])) + 2) * AppConstant.LAGER_OFFSET;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < paths.size(); i++) {
            paint.setColor(attribute.get(i).color);
            canvas.drawPath(paths.get(i), paint);
        }
        invalidate();
    }
}
