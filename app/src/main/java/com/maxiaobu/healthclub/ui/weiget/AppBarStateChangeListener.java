package com.maxiaobu.healthclub.ui.weiget;

import android.support.design.widget.AppBarLayout;

/**
 * Created by Administrator on 2016/10/8.
 */

public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener{
    public enum State {
        EXPANDED,//展开状态
        COLLAPSED,//折叠状态
        IDLE//中间状态
    }

    private State mCurrentState = State.IDLE;
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

        if (i == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout,i, State.EXPANDED);
            }
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout,i, State.COLLAPSED);
            }
            mCurrentState = State.COLLAPSED;
        } else {
            if (mCurrentState != State.IDLE) {
                onStateChanged(appBarLayout, i,State.IDLE);
            }
            mCurrentState = State.IDLE;
        }
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, int verticalOffset, State state);
}
