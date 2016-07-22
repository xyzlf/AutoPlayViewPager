package com.xyzlf.autoplay.viewpager;

import android.content.Context;
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

    public CustomerBanner(Context context) {
        this(context, null);
    }

    public CustomerBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.autoplay_banner_layout, this);

        viewPager = (AutoPlayViewPager) findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(this);
        indicator = (AutoPagerIndicator) findViewById(R.id.indicator);
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

}
