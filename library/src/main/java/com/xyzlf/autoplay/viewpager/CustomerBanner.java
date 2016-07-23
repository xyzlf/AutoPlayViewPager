package com.xyzlf.autoplay.viewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

/**
 * Created by zhanglifeng on 2016/7/21.
 * banner view
 */

public class CustomerBanner extends RelativeLayout implements ViewPager.OnPageChangeListener {

    private AutoPlayViewPager viewPager;
    private AutoPagerIndicator indicator;

    private int mDotSpan = 36;
    private float mDotRadius = 6f;

    private int mBottomMargin;

    private int mSelectedColor = 0xFFFFFFFF;
    private int mUnSelectedColor = 0x9FB4B2B2;

    public CustomerBanner(Context context) {
        this(context, null);
    }

    public CustomerBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

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

            //indicator margin bottom
            mBottomMargin = (int) arr.getDimension(R.styleable.AutoPagerIndicator_indicator_bottommargin, 0);

            arr.recycle();
        }

        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.autoplay_banner_layout, this);

        viewPager = (AutoPlayViewPager) findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(this);

        indicator = (AutoPagerIndicator) findViewById(R.id.indicator);
        indicator.setAttrs(mDotRadius, mDotSpan, mSelectedColor, mUnSelectedColor);

        if (mBottomMargin != 0) {
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_BOTTOM, viewPager.getId());
            params.bottomMargin = mBottomMargin;
            indicator.setLayoutParams(params);
        }
    }

    public void setmDotClickHandler(AutoPagerIndicator.OnDotClickHandler mOnDotClickHandler) {
        if (null != indicator) {
            indicator.setmDotClickHandler(mOnDotClickHandler);
        }
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        if (viewPager != null) {
            viewPager.addOnPageChangeListener(onPageChangeListener);
        }
    }

    public void setAdapter(AutoPagerAdapter adapter) {
        this.setAdapter(adapter, true);
    }

    public void setAdapter(AutoPagerAdapter adapter, boolean isAutoPlay) {
        if (null == adapter) {
            return;
        }
        if (null == viewPager || null == indicator) {
            return;
        }
        setVisibility(VISIBLE);
        viewPager.setAdapter(adapter);
        int dataSize = adapter.getDataCount();
        indicator.setAdapter(adapter);
        if (dataSize > 1 && isAutoPlay) {
            viewPager.startPlay(dataSize);
        } else {
            stopPlay();
        }
    }

    public void stopPlay() {
        if (null != viewPager) {
            viewPager.stopPlay();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        indicator.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
