package com.eme.mas.customeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.eme.mas.R;

import java.util.LinkedList;
import java.util.List;

public class StickyScrollView extends ScrollView {
    private static final String STICKY = "sticky";
    private View mCurrentStickyView;
    private Drawable mShadowDrawable;
    private List<View> mStickyViews;
    private GestureDetector mGestureDetector;
    private int mStickyViewTopOffset;
    private int defaultShadowHeight = 10;
    private float density;
    private boolean isTouch;
    private boolean isMove;
    private boolean redirectTouchToStickyView;
    private LoadMoerListener moerListener;
    float lastX, lastY;
    private Runnable mInvalidataRunnable = new Runnable() {

        @Override
        public void run() {
            if (mCurrentStickyView != null) {
                int left = mCurrentStickyView.getLeft();
                int top = mCurrentStickyView.getTop();
                int right = mCurrentStickyView.getRight();
                int bottom = getScrollY() + (mCurrentStickyView.getHeight() + mStickyViewTopOffset);

                invalidate(left, top, right, bottom);
            }

            postDelayed(this, 16);
        }
    };

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = false;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = ev.getX();
                lastY = ev.getY();
                isTouch = true;
                break;

            case MotionEvent.ACTION_MOVE:

                int distanceX = (int) Math.abs(ev.getX() - lastX);
                int distanceY = (int) Math.abs(ev.getY() - lastY);
                if (distanceX > distanceY) {
                    result = false;
                } else {
                    if (distanceY > 3) {
                        result = true;//临时改变，实际为true。
                    } else {
                        result = false;//执行跳转

                    }

                }
                isMove = true;
                break;
            case MotionEvent.ACTION_UP:

            default:
                break;
        }

        return result && mGestureDetector.onTouchEvent(ev);

    }

    public StickyScrollView(Context context) {
        super(context);

    }

    public StickyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public StickyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mGestureDetector = new GestureDetector(context, new GestureListener());
        mShadowDrawable = context.getResources().getDrawable(R.drawable.sticky_shadow_default);
        mStickyViews = new LinkedList<View>();
        density = context.getResources().getDisplayMetrics().density;
    }

    private void findViewByStickyTag(ViewGroup viewGroup) {
        int childCount = ((ViewGroup) viewGroup).getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = viewGroup.getChildAt(i);

            if (getStringTagForView(child).contains(STICKY)) {
                mStickyViews.add(child);
            }

            if (child instanceof ViewGroup) {
                findViewByStickyTag((ViewGroup) child);
            }
        }

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            findViewByStickyTag((ViewGroup) getChildAt(0));
        }
        showStickyView();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        showStickyView();
        View view = getChildAt(getChildCount()-1);
        int d = view.getBottom();
        d -= (getHeight()+getScrollY());
        if(d==0){
            if(null!=moerListener){
                moerListener.callBack(true);
            }
        }

    }



    /**
     *
     */
    private void showStickyView() {
        View curStickyView = null;
        View nextStickyView = null;

        for (View v : mStickyViews) {
            int topOffset = v.getTop() - getScrollY();

            if (topOffset <= 0) {
                if (curStickyView == null || topOffset > curStickyView.getTop() - getScrollY()) {
                    curStickyView = v;
                }
            } else {
                if (nextStickyView == null || topOffset < nextStickyView.getTop() - getScrollY()) {
                    nextStickyView = v;
                }
            }
        }

        if (curStickyView != null) {
            mStickyViewTopOffset = nextStickyView == null ? 0 : Math.min(0, nextStickyView.getTop() - getScrollY() - curStickyView.getHeight());
            mCurrentStickyView = curStickyView;
            post(mInvalidataRunnable);
        } else {
            mCurrentStickyView = null;
            removeCallbacks(mInvalidataRunnable);

        }

    }


    private String getStringTagForView(View v) {
        Object tag = v.getTag();
        return String.valueOf(tag);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mCurrentStickyView != null) {
            canvas.save();
            canvas.translate(0, getScrollY() + mStickyViewTopOffset);

            if (mShadowDrawable != null) {
                int left = 0;
                int top = mCurrentStickyView.getHeight() + mStickyViewTopOffset;
                int right = mCurrentStickyView.getWidth();
                int bottom = top + (int) (density * defaultShadowHeight + 0.5f);
                mShadowDrawable.setBounds(left, top, right, bottom);
                mShadowDrawable.draw(canvas);
            }


            canvas.clipRect(0, mStickyViewTopOffset, mCurrentStickyView.getWidth(), mCurrentStickyView.getHeight());

            mCurrentStickyView.draw(canvas);

            //�������ԭ�����
            canvas.restore();
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            redirectTouchToStickyView = true;
        }

        if (redirectTouchToStickyView) {
            redirectTouchToStickyView = mCurrentStickyView != null;

            if (redirectTouchToStickyView) {
                redirectTouchToStickyView = ev.getY() <= (mCurrentStickyView
                        .getHeight() + mStickyViewTopOffset)
                        && ev.getX() >= mCurrentStickyView.getLeft()
                        && ev.getX() <= mCurrentStickyView.getRight();
            }
        }

        if (redirectTouchToStickyView) {
            ev.offsetLocation(0, -1 * ((getScrollY() + mStickyViewTopOffset) - mCurrentStickyView.getTop()));
        }
        return super.dispatchTouchEvent(ev);
    }


    private boolean hasNotDoneActionDown = true;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (redirectTouchToStickyView) {
            ev.offsetLocation(0, ((getScrollY() + mStickyViewTopOffset) - mCurrentStickyView.getTop()));
        }

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            hasNotDoneActionDown = false;
        }

        if (hasNotDoneActionDown) {
            MotionEvent down = MotionEvent.obtain(ev);
            down.setAction(MotionEvent.ACTION_DOWN);
            super.onTouchEvent(down);
            hasNotDoneActionDown = false;
        }

        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            hasNotDoneActionDown = true;
        }
        return super.onTouchEvent(ev);
    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override//判断移动的方向
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceY) >= Math.abs(distanceX)) {
                return true;
            } else {
                return false;
            }

        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            performLongClick();
        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return performClick();
        }
    }


    public void addListener(LoadMoerListener listenerl){
        this.moerListener=listenerl;
    }

    public interface LoadMoerListener {

        void callBack(boolean v);
    }

}
