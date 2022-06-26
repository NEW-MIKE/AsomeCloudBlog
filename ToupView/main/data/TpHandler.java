package com.example.astroclient;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by hyayh on 2017/11/28.
 */

public class TpHandler extends Handler {
    private final WeakReference<MainActivity> mOwner;

    public TpHandler(MainActivity owner) {
        mOwner = new WeakReference<MainActivity>(owner);
    }

    @Override
    public void handleMessage(Message msg) {
        MainActivity player = mOwner.get();
        switch (msg.what) {
            case TpConst.MSG_VIDEOSIZE_CHANGED:
                return;
            case TpConst.MSG_UPDATE:
                int width = msg.arg1;
                int height = msg.arg2;
                player.update(width, height);
                break;
            case TpConst.MSG_ERROR:
//                player.warnExit();
                break;
            case TpConst.MSG_PARAM:
//                player.nativeNotify(msg.arg1, msg.arg2);
                break;
            case TpConst.MSG_DEVICE_CHANGED:
                player.selectCamera();
                break;
            case TpConst.MSG_CHART_UPDATE:
                player.CombineChart();
                break;
            default:
                break;
        }
    }
}
