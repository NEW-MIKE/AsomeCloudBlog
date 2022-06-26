package com.example.astroclient;

import static com.example.astroclient.TpConst.KEY_ID;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Size;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.microedition.khronos.opengles.GL10;

import androidx.annotation.Nullable;

import static javax.microedition.khronos.opengles.GL10.GL_TEXTURE_2D;

/**
 * Created by hyayh on 2017/11/28.
 */

public class TpLib {
	private String TAG = "TpLib";
	private static TpLib sInstance;
	private static Handler previewHandler = null;
	private boolean mBLocked = false;

	public void Init() {
		init();
	}

	public void Fini() {
		fini();
	}

	public synchronized boolean OpenCamera(String url) {
		return 0 == openDevice(url);
	}

	public void PauseCamera() {
		pauseCamera();
	}

	public boolean RestartCamera() {
		return restartCamera();
	}

	public void ReleaseCamera() {
		releaseCamera();
	}

	public synchronized boolean IsAlive() {
		return isAlive();
	}

	public boolean HaveData()
	{
		return haveData();
	}

	public void Step(int texture)
	{
		step(texture);
	}

	public byte[] GetPixelsData(){return getPixelsData();}

	public boolean StartStream() {return startCameraStream();}

	public static TpLib getInstance() {
		synchronized (TpLib.class) {
			if (sInstance == null) {
				sInstance = new TpLib();
			}
		}
		return sInstance;
	}

	public void DoUpdateFrame(int width, int height) {
		if (previewHandler != null) {
			Message Msg = previewHandler.obtainMessage();
			Msg.what = TpConst.MSG_UPDATE;
			Msg.arg1 = width;
			Msg.arg2 = height;
			previewHandler.sendMessage(Msg);
		}
	}

	public void ErrorCallBack(int type){
		pauseCamera();
		if (previewHandler != null) {
			Message Msg = previewHandler.obtainMessage();
			Msg.what = TpConst.MSG_ERROR;
			previewHandler.sendMessage(Msg);
		}
	}

	public double GetFps(){
		return getFps();
	}

	public Size GetLiveSize() {
		int[] array = getLiveSize();
		return new Size(array[0], array[1]);
	}

	public void setPreviewHandler(Handler handler) {
		previewHandler = handler;
	}

	static {
		System.loadLibrary("jnilib");
	}

	//region Toupnam
	private native int[] getLiveSize();

	private native void init();

	private native void fini();

	private native void pauseCamera();

	private native boolean restartCamera();

	public native boolean startCameraStream();

	public native byte[] getPixelsData();

	private native void step(int step);

	private native void releaseCamera();

	private native boolean isAlive();

	//endregion
	private native int openDevice(String url);

	private native double getFps();

	private native boolean haveData();
}
