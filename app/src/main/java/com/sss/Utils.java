package com.sss;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.TypedValue;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {


    public static boolean calculateBesselSmoothPath(float lineSmoothness, int offset, boolean forceClosed, PathMeasure pathMeasure, Path path, List<Point> entry) {
        if (entry.size() < 2) {
            return false;
        }
        path.reset();
        float prePreviousPointX = Float.NaN;
        float prePreviousPointY = Float.NaN;
        float previousPointX = Float.NaN;
        float previousPointY = Float.NaN;
        float currentPointX = Float.NaN;
        float currentPointY = Float.NaN;
        float nextPointX;
        float nextPointY;

        final int lineSize = entry.size();
        for (int valueIndex = 0; valueIndex < lineSize; ++valueIndex) {
            if (Float.isNaN(currentPointX)) {
                Point point = entry.get(valueIndex);
                currentPointX = point.x;
                currentPointY = point.y;
            }
            if (Float.isNaN(previousPointX)) {
                //是否是第一个点
                if (valueIndex > 0) {
                    Point point = entry.get(valueIndex - 1);
                    previousPointX = point.x;
                    previousPointY = point.y;
                } else {
                    //是的话就用当前点表示上一个点
                    previousPointX = currentPointX;
                    previousPointY = currentPointY;
                }
            }

            if (Float.isNaN(prePreviousPointX)) {
                //是否是前两个点
                if (valueIndex > 1) {
                    Point point = entry.get(valueIndex - 2);
                    prePreviousPointX = point.x;
                    prePreviousPointY = point.y;
                } else {
                    //是的话就用当前点表示上上个点
                    prePreviousPointX = previousPointX;
                    prePreviousPointY = previousPointY;
                }
            }

            // 判断是不是最后一个点了
            if (valueIndex < lineSize - 1) {
                Point point = entry.get(valueIndex + 1);
                nextPointX = point.x;
                nextPointY = point.y;
            } else {
                //是的话就用当前点表示下一个点
                nextPointX = currentPointX;
                nextPointY = currentPointY;
            }

            if (valueIndex == 0) {
                // 将Path移动到开始点
                path.moveTo(currentPointX, currentPointY);
            } else {
                // 求出控制点坐标
                final float firstDiffX = (currentPointX - prePreviousPointX);
                final float firstDiffY = (currentPointY - prePreviousPointY);
                final float secondDiffX = (nextPointX - previousPointX);
                final float secondDiffY = (nextPointY - previousPointY);
                final float firstControlPointX = previousPointX + (lineSmoothness * firstDiffX) + offset;
                final float firstControlPointY = previousPointY + (lineSmoothness * firstDiffY) + offset;
                final float secondControlPointX = currentPointX - (lineSmoothness * secondDiffX) - offset;
                final float secondControlPointY = currentPointY - (lineSmoothness * secondDiffY) - offset;
                //画出曲线
                path.cubicTo(firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY,
                        currentPointX, currentPointY);
            }

            // 更新值,
            prePreviousPointX = previousPointX;
            prePreviousPointY = previousPointY;
            previousPointX = currentPointX;
            previousPointY = currentPointY;
            currentPointX = nextPointX;
            currentPointY = nextPointY;
        }
        pathMeasure.setPath(path, forceClosed);
        float distance = pathMeasure.getLength() * 1f;
        return pathMeasure.getSegment(0, distance, path, true);
    }

    /**
     * 将以3点钟方向的角度系转换成以12点钟方向的角度系
     */
    public static float getAngle(float angle) {
        if (angle < 90) {
            return 270 + 30 + 90 - angle;
        } else {
            return angle - 90;
        }
    }

    /**
     * 计算圆弧上的某一点
     */
    public static Point calcPoint(int centerX, int centerY, int radius, float angle, Point point) {
        point.x = (int) (centerX + radius * Math.cos((angle) * Math.PI / 180));
        point.y = (int) (centerY + radius * Math.sin((angle) * Math.PI / 180));
        return point;
    }

    /**
     * dp转px
     */
    public static int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, Resources.getSystem().getDisplayMetrics());
    }

    /**
     * sp转px
     */
    public static int sp2px(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, Resources.getSystem().getDisplayMetrics());
    }

    public static int randomInt(Random random, int min, int max) {
        if ((max - min + 1) == 0) {
            return (min + max) / 2;
        }
        return random.nextInt(max) % (max - min + 1) + min;
    }

    public static Bitmap getScaleBitmap(int r, int res, Resources resources) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeResource(resources, res, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > r || height > r) {
            // 缩放
            scaleWidth = ((float) width) / r;
            scaleHeight = ((float) height) / r;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int) scale;
        opts.inDither = true;
        WeakReference<Bitmap> weak = new WeakReference<>(BitmapFactory.decodeResource(resources, res, opts));
        return Bitmap.createScaledBitmap(weak.get(), r, r, true);
    }

    /**
     * 获取圆形bitmap
     */
    public static Bitmap getCirleBitmap(int r, Bitmap bmp, Paint paint) {
        WeakReference<Bitmap> weak = new WeakReference<>(Bitmap.createBitmap(r, r, Bitmap.Config.ARGB_8888));
        Canvas canvas = new Canvas(weak.get());
        Path path = new Path();
        path.addCircle(r / 2, r / 2, r / 2, Path.Direction.CW);
        canvas.clipPath(path);
        canvas.drawBitmap(bmp, 0, 0, paint);
        return weak.get();
    }
}
