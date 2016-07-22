package com.xyzlf.viewpager.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.xyzlf.autoplay.viewpager.AutoPagerAdapter;
import com.xyzlf.autoplay.viewpager.AutoPagerIndicator;
import com.xyzlf.autoplay.viewpager.AutoPlayViewPager;
import com.xyzlf.autoplay.viewpager.CustomerBanner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.pic_1);
        list.add(R.drawable.pic_2);
        list.add(R.drawable.pic_3);
        list.add(R.drawable.pic_4);

        /** 简单粗暴使用方式 **/
        CustomerBanner banner = (CustomerBanner) findViewById(R.id.banner);
        banner.setAdapter(new AutoPlayPagerAdapter(list));

        /** 自定义indicator大小，颜色 **/
        final AutoPlayViewPager viewPager = (AutoPlayViewPager) findViewById(R.id.main_viewpager);
        final AutoPagerIndicator indicator = (AutoPagerIndicator) findViewById(R.id.main_indicator);
        AutoPagerAdapter adapter = new AutoPlayPagerAdapter(list);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        });
        viewPager.setAdapter(adapter);
        indicator.setAdapter(adapter);
        viewPager.startPlay(adapter.getDataCount());

    }

    private class AutoPlayPagerAdapter extends AutoPagerAdapter {

        private List<Integer> list;

        public AutoPlayPagerAdapter(List<Integer> list) {
            this.list = list;
        }

        @Override
        public View getView(LayoutInflater layoutInflater, int position) {
            ImageView imageView = new ImageView(getApplicationContext());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(params);
            int resId = list.get(getPositionForIndicator(position));
            imageView.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, resId));
            return imageView;
        }

        @Override
        public int getDataCount() {
            return list == null ? 0 : list.size();
        }
    }

}
