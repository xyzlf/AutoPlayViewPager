package com.xyzlf.autoplay.viewpager;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AutoPagerAdapter extends PagerAdapter {

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = getView(LayoutInflater.from(container.getContext()), position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public int getPositionForIndicator(int position) {
        if (getDataCount() <= 0) {
            return 0;
        }
        return position % getDataCount();
    }

    public abstract View getView(LayoutInflater layoutInflater, int position);

    public abstract int getDataCount();

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public int getCount() {
        return getDataCount() > 1 ? getDataCount() * AutoPlayConstant.DATA_COUNT_RATIO :  getDataCount();
    }

}
