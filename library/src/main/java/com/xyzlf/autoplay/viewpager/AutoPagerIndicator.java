package com.xyzlf.autoplay.viewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by zhanglifeng on 2016/7/20.
 * Pager indicator
 */

public class AutoPagerIndicator extends LinearLayout {

    public interface OnDotClickHandler {
        void onDotClick(int index);
    }

    private int mCurrent = 0;

    private int mLittleDotSize = -2;
    private int mDotSpan = 36;
    private float mDotRadius = 6f;

    private int mSelectedColor = 0xFFFFFFFF;
    private int mUnSelectedColor = 0x9FB4B2B2;

    private OnDotClickHandler mOnDotClickHandler;

    private AutoPagerAdapter adapter;

    public AutoPagerIndicator(Context context) {
        super(context);
    }

    public AutoPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setGravity(Gravity.CENTER);

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.AutoPagerIndicator, 0, 0);
        if (arr != null) {
            // indicator 大小
            if (arr.hasValue(R.styleable.AutoPagerIndicator_indicator_radius)) {
                mDotRadius = arr.getDimension(R.styleable.AutoPagerIndicator_indicator_radius, mDotRadius);
            }

            // indicator 间距
            if (arr.hasValue(R.styleable.AutoPagerIndicator_indicator_span)) {
                mDotSpan = (int) arr.getDimension(R.styleable.AutoPagerIndicator_indicator_span, mDotSpan);
            }

            // indicator 选中颜色
            mSelectedColor = arr.getColor(R.styleable.AutoPagerIndicator_indicator_selected_color, mSelectedColor);
            // indicator unselected color
            mUnSelectedColor = arr.getColor(R.styleable.AutoPagerIndicator_indicator_unselected_color, mUnSelectedColor);
            arr.recycle();
        }

        mLittleDotSize = (int) (mDotSpan / 2 + mDotRadius * 2);
    }

    public final void setAdapter(AutoPagerAdapter adapter) {
        if (null == adapter) {
            setVisibility(GONE);
            return;
        }
        this.adapter = adapter;
        int count = adapter.getDataCount();
        if (count <= 1) {
            setVisibility(GONE);
            return;
        }
        setVisibility(VISIBLE);
        setOrientation(HORIZONTAL);

        removeAllViews();

        for (int i = 0; i < count; i++) {
            LittleDot dot = new LittleDot(getContext(), i);
            if (i == 0) {
                dot.setColor(mSelectedColor);
            } else {
                dot.setColor(mUnSelectedColor);
            }
            dot.setLayoutParams(new LayoutParams(mLittleDotSize, (int) mDotRadius * 2));
            dot.setClickable(true);
            dot.setOnClickListener(mDotClickHandler);
            addView(dot);
        }
    }

    public final void onPageSelected(int position) {
        if (null == adapter) {
            return;
        }
        int index = adapter.getPositionForIndicator(position);
        if (index >= getChildCount() || index < 0 || mCurrent == index)
            return;
        if (getChildAt(mCurrent) != null) {
            ((LittleDot) getChildAt(mCurrent)).setColor(mUnSelectedColor);
        }
        if (getChildAt(index) != null) {
            ((LittleDot) getChildAt(index)).setColor(mSelectedColor);
        }
        mCurrent = index;
    }

    public void setmDotClickHandler(OnDotClickHandler mOnDotClickHandler) {
        this.mOnDotClickHandler = mOnDotClickHandler;
    }

    private OnClickListener mDotClickHandler = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof LittleDot && null != mOnDotClickHandler) {
                mOnDotClickHandler.onDotClick(((LittleDot) v).getIndex());
            }
        }
    };

    private class LittleDot extends View {

        private int mColor;
        private Paint mPaint;
        private int mIndex;

        public LittleDot(Context context, int index) {
            super(context);
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mIndex = index;
        }

        public int getIndex() {
            return mIndex;
        }

        public void setColor(int color) {
            if (color == mColor) {
                return;
            }
            mColor = color;
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            mPaint.setColor(mColor);
            canvas.drawCircle(mLittleDotSize / 2, mDotRadius, mDotRadius, mPaint);
        }
    }

}
