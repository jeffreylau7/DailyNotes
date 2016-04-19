package com.wingsofts.progresscircle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/2/16.
 */
public class ProgressCircle extends View {
    private Paint mPaint;
    public ProgressCircle(Context context) {
        this(context,null);
    }

    public ProgressCircle(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ProgressCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int centerX = getMeasuredWidth()/2;
        int centerY = getMeasuredHeight()/2;
        canvas.save();
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.parseColor("#D5D5D5"));
        for(int i = 0; i<120;i++){
            canvas.rotate(3,centerX,centerY);
            canvas.drawLine(centerX,200,centerX,220,mPaint);
        }

        canvas.restore();

        mPaint.setColor(Color.parseColor("#247ACB"));
        canvas.drawCircle(centerX,centerY,280,mPaint);
        String str = "预期年化利率";
        mPaint.setTextSize(40);

        float txtLength = mPaint.measureText(str);
        mPaint.setColor(Color.WHITE);
        canvas.drawText(str,centerX- txtLength/2,centerY-50,mPaint);


        String percent = "12.00%";

        mPaint.setTextSize(50);

        float perLength = mPaint.measureText(percent);

        canvas.drawText(percent,centerX- perLength/2,centerY+50,mPaint);
    }
}
