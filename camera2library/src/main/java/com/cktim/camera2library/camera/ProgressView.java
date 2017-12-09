package com.cktim.camera2library.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.cktim.camera2library.Camera2Config;
import com.cktim.camera2library.R;

/**
 * Created by cxk on 2017/12/8.
 * <p>
 * 长按录制时候的倒数进度条
 */

public class ProgressView extends View {
    //constant
    private int millisecond = 1000;//每一秒
    private float maxProgressSize = Camera2Config.RECORD_MAX_TIME * millisecond;//总进度是10s
    private float eachProgressWidth = 0;//每一格的宽度

    private Context mContext;
    private WindowManager mWindowManager;
    private Paint progressPaint;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        //设置每一刻度的宽度
        DisplayMetrics dm = new DisplayMetrics();
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        eachProgressWidth = dm.widthPixels / (maxProgressSize * 1.0f);
        //进度条的背景颜色
        setBackgroundColor(getResources().getColor(R.color.transparent));
        //进度条的前景颜色,画笔
        progressPaint = new Paint();
        progressPaint.setStyle(Paint.Style.FILL);
        progressPaint.setColor(getResources().getColor(Camera2Config.RECORD_PROGRESS_VIEW_COLOR));
    }

    private long initTime = -1;//上一次刷新完成后的时间
    private boolean isStart = false;
    private float countWidth = 0;//进度条进度的进程，每次调用invalidate（）都刷新一次

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isStart) {
            canvas.drawRect(0, 0, countWidth, getMeasuredHeight(), progressPaint);
            return;
        }
        if (initTime == -1) {
            initTime = System.currentTimeMillis();
            canvas.drawRect(0, 0, countWidth, getMeasuredHeight(), progressPaint);
            invalidate();
            return;
        }
        //这次刷新的时间，用于与上一次刷新完成的时间作差得出进度条需要增加的进度
        long thisTime = System.currentTimeMillis();
        countWidth += eachProgressWidth * (thisTime - initTime) * 1.0f;
        if (countWidth > getMeasuredWidth()) {
            countWidth = getMeasuredWidth();
        }
        canvas.drawRect(0, 0, countWidth, getMeasuredHeight(), progressPaint);

        //如果都了最大长度，就不再调用invalidate();了
        if (countWidth < getMeasuredWidth() && isStart) {
            initTime = System.currentTimeMillis();
            invalidate();
        } else {
            countWidth = 0;
            initTime = -1;
            isStart = false;
        }

    }

    //开始或暂停进度条进度刷新
    public void setIsStart(boolean isStart) {
        if (isStart == this.isStart)
            return;
        this.isStart = isStart;
        if (isStart) {
            initTime = -1;
            invalidate();
        }
    }

    //重置进度条
    public void reset() {
        countWidth = 0;
        initTime = -1;
        isStart = false;
        invalidate();
    }

    //设置每一个像素的宽度
    public void setEachProgressWidth(int width) {
        eachProgressWidth = width / (maxProgressSize * 1.0f);
    }
}
