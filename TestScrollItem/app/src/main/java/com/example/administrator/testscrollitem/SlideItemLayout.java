package com.example.administrator.testscrollitem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by Administrator on 2017/5/8.
 */

public class SlideItemLayout extends FrameLayout {
    private View contentView, menuView;
    private int contentWidth, menuWidth, viewHeight;
    private Scroller scroller;

    public SlideItemLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        menuView = getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        contentWidth = contentView.getMeasuredWidth();
        menuWidth = menuView.getMeasuredWidth();
        viewHeight = contentView.getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        menuView.layout(contentWidth, 0, contentWidth + menuWidth, viewHeight);
    }


    private float startX, startY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = event.getX();
                float endY = event.getY();
                float distanceX = endX - startX;

//                Log.e("getScrollX():", getScrollX() + "");
//                Log.e("distanceX:", distanceX + "");
//                Log.e("toScrollX", (int) (getScrollX() - distanceX) + "");
                int toScrollX = (int) (getScrollX() - distanceX);
                //getScrollX一开始是0,后面-distanceX慢慢累加，distanceX会随着stratX初始化而改变。
                if (toScrollX < 0){
                    toScrollX = 0;
                }else if (toScrollX > menuWidth){
                    toScrollX = menuWidth;
                }
                scrollTo(toScrollX, getScrollY());

                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                int totalScrollX = getScrollX();//偏移量
                if (totalScrollX < menuWidth/2){
                    closeMenu();
                }else {
                    openMenu();
                }
                break;
        }

        return true;
    }

    private void openMenu() {
        int distanceX = menuWidth - getScrollX();
        //目标偏移量 - 起始偏移量
        scroller.startScroll(getScrollX(), getScrollY(), distanceX, getScrollY());
        //从X,Y滑到另一个X,Y
        invalidate();
    }

    private void closeMenu() {
        int distanceX = 0 - getScrollX();
        //目标点-起始点
        scroller.startScroll(getScrollX(), getScrollY(), distanceX, getScrollY());
        //从X,Y滑到另一个X,Y
//        startX 表示起点在水平方向到原点的距离（可以理解为X轴坐标，但与X轴相反），正值表示在原点左边，负值表示在原点右边。
//        dx 表示滑动的距离，正值向左滑，负值向右滑。
//        这与我们感官逻辑相反，需要注意。
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }
}
