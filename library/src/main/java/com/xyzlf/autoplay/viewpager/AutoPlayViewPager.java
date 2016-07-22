package com.xyzlf.autoplay.viewpager;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by zhanglifeng on 2016/7/20
 * Auto Play ViewPager
 */

public class AutoPlayViewPager extends ViewPager {

    private static final int DEFAULT_INTERVAL = 4000;

    public enum PlayDirection {
        TO_LEFT, TO_RIGHT
    }

    public enum PlayRecycleMode {
        REPEAT_FROM_START, PLAY_BACK
    }

    private PlayDirection mDirection = PlayDirection.TO_RIGHT;
    private PlayRecycleMode mPlayRecycleMode = PlayRecycleMode.REPEAT_FROM_START;

    private int timeInterval = DEFAULT_INTERVAL;

    private boolean isPause = false;
    private boolean isPlaying = false;
    private boolean skipNext = false;

    private final Handler mHandler = new Handler();
    private Runnable timerTask;

    public AutoPlayViewPager(Context context) {
        this(context, null);
    }

    public AutoPlayViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    public void startPlay(int startItem) {
        if (isPlaying) {
            return;
        }
        isPause = false;
        isPlaying = true;
        setCurrentItem(startItem  * AutoPlayConstant.START_INDICATOR_RATIO);
        if (null != timerTask) {
            mHandler.removeCallbacks(timerTask);
        }
        timerTask = new TimerRunnable();
        mHandler.postDelayed(timerTask, timeInterval);
    }

    public void stopPlay() {
        isPause = true;
        isPlaying = false;

        if (null != timerTask) {
            mHandler.removeCallbacks(timerTask);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //pause play
                isPause = true;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                //start play
                isPause = false;
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        stopPlay();
    }

    private class TimerRunnable implements Runnable {

        @Override
        public void run() {
            if (!isPause) {
                // 轮播next
                playNextFrame();
            }

            if (isPlaying) {
                mHandler.postDelayed(this, timeInterval);
            }
        }
    }

    private void playNextFrame() {
        if (skipNext) {
            skipNext = false;
            return;
        }
        int total = getAdapter() == null ? 0 : getAdapter().getCount();
        int current = getCurrentItem();
        if (mDirection == PlayDirection.TO_RIGHT) {
            if (current == total - 1) {
                if (mPlayRecycleMode == PlayRecycleMode.PLAY_BACK) {
                    mDirection = PlayDirection.TO_LEFT;
                    playNextFrame();
                } else {
                    setCurrentItem(0, false);
                }
            } else {
                setCurrentItem(getCurrentItem() + 1, true);
            }
        } else {
            if (current == 0) {
                if (mPlayRecycleMode == PlayRecycleMode.PLAY_BACK) {
                    mDirection = PlayDirection.TO_RIGHT;
                    playNextFrame();
                } else {
                    setCurrentItem(total - 1, false);
                }
            } else {
                setCurrentItem(getCurrentItem() - 1, true);
            }
        }
    }

}
