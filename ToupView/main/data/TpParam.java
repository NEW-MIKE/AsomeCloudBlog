package com.example.astroclient;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hyayh on 2017/11/28.
 */

public class TpParam implements Parcelable {
    public int imin;// 参数的最小值
    public int imax;// 参数的最大值
    public int idefault;// 参数的默认值
    public int icurPos;// bar参数位置值
    public int icurValue;// 当前值
    public float fcoef;
    public boolean bisAble;// 是否支持该参数 true 支持 false 不支持

    /**
     * 计算当前bar的位置
     * 必须保证imax imin icurValue
     *
     * @param param bar 参数
     * @return
     */
    public int calcuPos() {
        fcoef = (imax - imin) / 1000.0f;
        icurPos = (int) ((icurValue - imin) / fcoef);
        return icurPos;
    }

    @Override
    public String toString() {
        return "TpParam{" +
                "imin=" + imin +
                ", imax=" + imax +
                ", idefault=" + idefault +
                ", icurPos=" + icurPos +
                ", icurValue=" + icurValue +
                ", fcoef=" + fcoef +
                ", bisAble=" + bisAble +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(imin);// 参数的最小值
        out.writeInt(imax);// 参数的最大值
        out.writeInt(idefault);// 参数的默认值
        out.writeInt(icurPos);// bar参数位置值
        out.writeInt(icurValue);// 当前值
        out.writeFloat(fcoef);
        out.writeBoolean(bisAble);// 是否支持该参数 true 支持 false 不支持
    }

    public static final Parcelable.Creator<TpParam> CREATOR = new Parcelable.Creator<TpParam>() {
        /**
         * Return a new point from the data in the specified parcel.
         */
        @Override
        public TpParam createFromParcel(Parcel in) {
            TpParam r = new TpParam();
            r.readFromParcel(in);
            return r;
        }


        @Override
        public TpParam[] newArray(int size) {
            return new TpParam[size];
        }
    };


    public void readFromParcel(@androidx.annotation.NonNull Parcel in) {
        imin = in.readInt();
        imax = in.readInt();
        idefault = in.readInt();
        icurPos = in.readInt();
        icurValue = in.readInt();
        fcoef = in.readFloat();
        bisAble = in.readBoolean();
    }
}