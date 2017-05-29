package com.ourbuaa.buaahelper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.baoyz.widget.PullRefreshLayout;

/**
 * Created by Croxx on 2017/5/26.
 */

public class BUAAPullRefreshLayout extends PullRefreshLayout {

    public BUAAPullRefreshLayout(Context context) {
        super(context, null);
    }

    public BUAAPullRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    float y1, y2;
    boolean use = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!use) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                y1 = ev.getY();
            }
            if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                y2 = ev.getY();
                //Log.d("Y", "" + y1 + " " + y2);
                if (y2 - y1 > 10)
                    use=true;
            }

            return false;

        }else {
            if (ev.getAction() == MotionEvent.ACTION_CANCEL || ev.getAction() == MotionEvent.ACTION_UP) {
                use=false;
            }
            return super.onInterceptTouchEvent(ev);

        }

    }
}
