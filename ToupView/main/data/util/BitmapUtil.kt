package com.example.astroclient.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.view.MotionEvent


object BitmapUtil {

    var widthScale = 0f
    var heightScale = 0f
    var flag = 0
    fun setClickPosition(e: MotionEvent,left:Float,right:Float,top:Float,bottom:Float){
        widthScale = (e.x - left)/(right - left)
        heightScale = (e.y - top)/(bottom - top)
        flag = 1
    }
    fun bitMapScale(bitmap: Bitmap, scale:Float): Bitmap? {
        if(flag == 1) {
            var offset = 15
            var xPosition = bitmap.width * widthScale - offset
            var yPosition = bitmap.height * heightScale - offset
            val matrix = Matrix()
            matrix.postScale(scale, scale) //长和宽放大缩小的比例
            val nbitMap = Bitmap.createBitmap(
                bitmap,
                xPosition.toInt(),
                yPosition.toInt(),
                offset * 2,
                offset * 2,
                matrix,
                true
            )
            bitmap.recycle()
            return nbitMap
        }
        else{
            val matrix = Matrix()
            matrix.postScale(scale, scale) //长和宽放大缩小的比例
            val nbitMap = Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
            bitmap.recycle()
            return nbitMap
        }
    }
    fun rgb2Bitmap(data: ByteArray, width: Int, height: Int): Bitmap? {
        val colors: IntArray = convertIntToColor(data) ?: return null //取RGB值转换为int数组
        return Bitmap.createBitmap(
            colors,width, height,
            Bitmap.Config.ARGB_8888
        )
    }
    fun convertByteToInt(data: Byte): Int {
        val heightBit = ((data.toInt() shr 4) and 0x0F) as Int
        return heightBit * 16 + (0x0F and data.toInt())
    }

    // 将纯RGB数据数组转化成int像素数组
    fun convertIntToColor(data: ByteArray): IntArray? {
        val size = data.size
        if (size == 0) {
            return null
        }

        val color = IntArray(size / 3)
        var red: Int
        var green: Int
        var blue: Int
        val colorLen = color.size
        for (i in 0 until colorLen) {
            red = convertByteToInt(data[i * 3]);
            green = convertByteToInt(data[i * 3 + 1]);
            blue = convertByteToInt(data[i * 3 + 2]);
            // 获取RGB分量值通过按位或生成int的像素值
            color[i] = (red shl 16) or (green shl 8) or blue or 0x7F000000
        }
        return color
    }
}