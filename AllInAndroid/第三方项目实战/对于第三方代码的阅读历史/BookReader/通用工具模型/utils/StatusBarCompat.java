/**从事Android开发的同学了解，对于StatusBar的控制，并没有ios那样好，需要对Android不同的系统版本进行适配
 * ，同时也需要对小米，魅族等国产手机单独适配，想要如ios那般好的体验，并没有那么简单。 
 * https://zhuanlan.zhihu.com/p/111481900
 * 
 * 那么这个模块是否能够解决这个问题，以及如何改进
 * 
 * */

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;

import com.justwayward.reader.R;

/**
 * @author yuyh.
 * @date 16/8/9.
 */
public class StatusBarCompat {
    private static final int INVALID_VAL = -1;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static View compat(Activity activity, int statusColor) {
        int color = ContextCompat.getColor(activity, R.color.colorPrimaryDark);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (statusColor != INVALID_VAL) {
                color = statusColor;
            }
            activity.getWindow().setStatusBarColor(color);
            return null;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (statusColor != INVALID_VAL) {
                color = statusColor;
            }
            View statusBarView = contentView.getChildAt(0);
            if (statusBarView != null && statusBarView.getMeasuredHeight() == getStatusBarHeight(activity)) {
                statusBarView.setBackgroundColor(color);
                return statusBarView;
            }
            statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity));
            statusBarView.setBackgroundColor(color);
            contentView.addView(statusBarView, lp);
            return statusBarView;
        }
        return null;

    }

    public static void compat(Activity activity) {
        compat(activity, INVALID_VAL);
    }


    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}