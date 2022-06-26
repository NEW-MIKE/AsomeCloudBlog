package com.example.astroclient.view

import android.content.Context
import android.graphics.*
import android.opengl.GLES10
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.util.Log
import com.example.astroclient.R
import com.example.astroclient.util.createProgram
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TpRender(val context: Context) : GLSurfaceView.Renderer {
    private val TAG = "TpRender"

    private var mCurrentBitmapWidth = 0
    private var mCurrentBitmapHeight = 0
    private var mCurrentWidth = 0
    private var mCurrentHeight = 0
    var matrix = Matrix()
    var minScale = 1.0f
    var maxScale = 4.0f

    var imageBoundsRect = RectF(-1f, -1f, 1f, 1f)
    var renderBlock: RenderInterface? = null
    var oldPoints = floatArrayOf(
        0f, 0f,
        0f, 0f,
        0f, 0f,
        0f, 0f
    )
    var newPoints = floatArrayOf(
        0f, 0f,
        0f, 0f,
        0f, 0f,
        0f, 0f
    )
    @Volatile
    private var mZoom = 1.0f

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        program.setup(context)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        mCurrentWidth = width
        mCurrentHeight = height
        GLES20.glViewport(0, 0, width, height)
        setupImageRect()
    }

    override fun onDrawFrame(gl: GL10?) {
        renderBlock?.run {
            program.render(matrix, imageBoundsRect, gl, this)
        }
    }

    fun setCurrentVideoSize(width: Int, height: Int) {
        mCurrentBitmapWidth = width
        mCurrentBitmapHeight = height
        setZoom(width, height, width)
    }

    fun getCurrentVideoSize(): Point {
        return Point(mCurrentBitmapWidth, mCurrentBitmapHeight)
    }

    fun setZoom(videoWidth: Int, videoHeight: Int, imgWidth: Int) {
        val k1: Float =
            Math.max(videoWidth.toFloat() / mCurrentWidth, videoHeight.toFloat() / mCurrentHeight)
        val k2 = if (imgWidth == 0) 1f else imgWidth.toFloat() / videoWidth
        mZoom = k1 * k2

        setupImageRect()
    }

    var mBoundLeft = 0f
    var mBoundRight = 0f
    var mBoundTop = 0f
    var mBoundBottom = 0f
    private fun setupImageRect() {
        if (mCurrentBitmapHeight == 0 || mCurrentBitmapWidth == 0 || mCurrentHeight == 0 || mCurrentWidth == 0) {
            return
        }
        var glHalfWidth: Float
        var glHalfHeight: Float
        val imageAspect = mCurrentBitmapWidth / mCurrentBitmapHeight.toFloat()
        val renderBufferAspect = mCurrentWidth / mCurrentHeight.toFloat()
        if (imageAspect > renderBufferAspect) {
            glHalfWidth = 1f
            glHalfHeight = glHalfWidth * renderBufferAspect / imageAspect
        } else {
            glHalfHeight = 1f
            glHalfWidth = glHalfHeight * imageAspect / renderBufferAspect
        }
        imageBoundsRect[-glHalfWidth, -glHalfHeight, glHalfWidth] = glHalfHeight
        mBoundLeft = (-glHalfWidth + 1) / 2 * mCurrentWidth
        mBoundRight = (glHalfWidth + 1) / 2 * mCurrentWidth
        mBoundTop = (-glHalfHeight + 1) / 2 * mCurrentHeight
        mBoundBottom = (glHalfHeight + 1) / 2 * mCurrentHeight

    }


    fun resetMatrix() {
        val v = FloatArray(9)
        matrix.getValues(v)
        v[Matrix.MSCALE_X] = 1f
        v[Matrix.MSCALE_Y] = 1f
        v[Matrix.MTRANS_X] = 0f
        v[Matrix.MTRANS_Y] = 0f
        matrix.setValues(v)
    }

    fun getScale(): Float {
        val values = FloatArray(9)
        matrix.getValues(values)
        return values[Matrix.MSCALE_X]
    }

    /**
     * @param scale 缩放比例
     * @param x    translate x坐标
     * @param y    translate y坐标
     */
    var mscaleFactor = 1f
    fun scale(scale: Float, x: Float, y: Float) {
        var x = x
        var y = y
        var scaleFactor = scale
        x = x * 2 / mCurrentWidth
        y = y * 2 / mCurrentHeight

/*        if ((scale < maxScale && scaleFactor > 1.0f) || (scale > minScale && scaleFactor < 1.0f)) {
            if (scale * scaleFactor > maxScale) {
                scaleFactor = maxScale / scale
            }
            if (scale * scaleFactor < minScale) {
                scaleFactor = minScale / scale
            }
        } else return*/

/*        if (scale * mscaleFactor > maxScale) {
            scaleFactor = maxScale / mscaleFactor
        }*/
        if (scale * mscaleFactor < minScale) {
            scaleFactor = minScale / mscaleFactor
            mscaleFactor = 1F
            resetMatrix()
            return
        }
        mscaleFactor = mscaleFactor * scale
        matrix.postScale(scaleFactor, scaleFactor,0f,0f)
        //constrainTranslation(scaleFactor, x, y)
    }

    fun getTranslation(): PointF {
        val values = FloatArray(9)
        matrix.getValues(values)
        return PointF(
            values[Matrix.MTRANS_X],
            values[Matrix.MTRANS_Y]
        )
    }

    /**
     * @param x    translate x坐标
     * @param y    translate y坐标
     */
    fun trans(x: Float, y: Float) {
        var x = x
        var y = y
        x = x * 2 / mCurrentWidth
        y = y * 2 / mCurrentHeight
        constrainTranslation(getScale(), x, y)
    }

    /**
     * @param scale 放缩
     * @param tx    单位化后 translate x
     * @param ty    单位化后 translate y
     */
    private fun constrainTranslation(scale: Float, tx: Float, ty: Float) {
        val values = FloatArray(9)
        val translation = PointF()
        matrix.getValues(values)
        val transX = values[Matrix.MTRANS_X] + tx
        val transY = values[Matrix.MTRANS_Y] + ty
        val imgeRect = imageBoundsRect
        val scaleX = Math.max(scale * imgeRect.right, 1f)
        val scaleY = Math.max(scale * imgeRect.bottom, 1f)
        if (transX < 0) {
            translation.x = Math.max(transX, 1 - scaleX)
        } else {
            translation.x = Math.min(transX, scaleX - 1)
        }
        if (transY < 0) {
            translation.y = Math.max(transY, 1 - scaleY)
        } else {
            translation.y = Math.min(transY, scaleY - 1)
        }
        values[Matrix.MTRANS_X] = translation.x
        values[Matrix.MTRANS_Y] = translation.y
        matrix.setValues(values)
    }

    fun mapPoints(points: FloatArray) {
        val matrix = Matrix()
        matrix.postConcat(matrix)
        matrix.postScale(mZoom, mZoom)
        matrix.mapPoints(points)
    }

    private var program = Program()
    fun setStaticBitmap(bitmap: Bitmap) {
        program.setStaticBitmap(bitmap)
        setCurrentVideoSize(bitmap.width, bitmap.height)
    }

    val pressPt = PointF(0f, 0f)
    var mBShowMagnifier = false
    var magnifierTop = 0f
    var magnifierRight = 0f
    var magnifierLeft = 0f
    var magnifierBtm = 0f

    interface RenderInterface {
        fun haveData(): Boolean
        fun render(texture: Int)
    }

    private inner class Program {
        private var mProgram = 0
        private lateinit var mVertexBuffer: FloatBuffer
        private var mPositionAttributeLocation = 0
        private var mTexCoordAttributeLocation = 0
        private val FLOAT_BYTE_LENGTH = 4
        private lateinit var textures: IntArray

        private var texture1 = 0
        private var texture2 = 0
        private val mVertices = floatArrayOf(
            -1f, -1f, 0.0f, 0f, 1f,
            1f, -1f, 0.0f, 1f, 1f,
            -1f, 1f, 0.0f, 0f, 0f,
            1f, 1f, 0.0f, 1f, 0f
        )

        fun setup(context: Context) {
            mProgram = context.createProgram(R.raw.vertex_shader, R.raw.fragment_shader)
            mPositionAttributeLocation = GLES20.glGetAttribLocation(mProgram, "position")
            mTexCoordAttributeLocation = GLES20.glGetAttribLocation(mProgram, "TexCoordIn")
            GLES20.glDisable(GL10.GL_CULL_FACE)
            mVertexBuffer =
                ByteBuffer.allocateDirect(mVertices.size * FLOAT_BYTE_LENGTH)
                    .order(
                        ByteOrder.nativeOrder()
                    ).asFloatBuffer()
            mVertexBuffer.put(mVertices)
            mVertexBuffer.position(0)
            texture1 = GLES20.glGetUniformLocation(mProgram, "texture1")
            texture2 = GLES20.glGetUniformLocation(mProgram, "texture2")
            GenTexture()
        }

        private fun GenTexture() {
            textures = IntArray(2)
            GLES20.glGenTextures(2, textures, 0)
            for (texture in textures) {
                setTexure(texture)
            }
        }

        private fun setTexure(texture: Int) {
            GLES20.glBindTexture(GLES10.GL_TEXTURE_2D, texture)
            GLES20.glTexParameterf(
                GL10.GL_TEXTURE_2D,
                GLES10.GL_TEXTURE_WRAP_S,
                GLES10.GL_CLAMP_TO_EDGE.toFloat()
            )
            GLES20.glTexParameterf(
                GL10.GL_TEXTURE_2D,
                GLES10.GL_TEXTURE_WRAP_T,
                GLES10.GL_CLAMP_TO_EDGE.toFloat()
            )
            GLES20.glTexParameterf(
                GL10.GL_TEXTURE_2D,
                GLES10.GL_TEXTURE_MIN_FILTER,
                GLES10.GL_NEAREST.toFloat()
            )
            GLES20.glTexParameterf(
                GL10.GL_TEXTURE_2D,
                GLES10.GL_TEXTURE_MAG_FILTER,
                GLES10.GL_LINEAR.toFloat()
            )
        }

        fun setStaticBitmap(bitmap: Bitmap) {
            GLES20.glActiveTexture(GLES10.GL_TEXTURE0)
            GLES20.glBindTexture(GLES10.GL_TEXTURE_2D, textures.get(0))
            GLUtils.texImage2D(GLES10.GL_TEXTURE_2D, 0, bitmap, 0)
            GLES20.glUniform1i(texture1, 0)
        }

        fun render(
            transformMatrix: Matrix,
            imageBoundsRect: RectF,
            gl: GL10?,
            block: RenderInterface
        ) {
            if (textures[0] == 0) {
                return
            }
            GLES20.glClearColor(0.1490196f, 0.1490196f, 0.1490196f, 0f)
            GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT)
            GLES20.glUseProgram(mProgram)
            GLES20.glActiveTexture(GLES10.GL_TEXTURE0)
            GLES20.glBindTexture(GLES10.GL_TEXTURE_2D, textures[0])
            if (block.haveData()) {
                block.render(0)
                GLES20.glUniform1i(texture1, 0)
                GLES20.glUniform1i(texture2, 1)
                GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "isNV12"), 0)
            }
            GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "bDrawWindow"), 0)
            val points = floatArrayOf(
                imageBoundsRect.left, imageBoundsRect.top,
                imageBoundsRect.right, imageBoundsRect.top,
                imageBoundsRect.left, imageBoundsRect.bottom,
                imageBoundsRect.right, imageBoundsRect.bottom
            )
            oldPoints = points.clone()

            transformMatrix.mapPoints(points)
            newPoints = points
            mVertices[0] = points[0]
            mVertices[1] = points[1]
            mVertices[2] = 0.0f
            mVertices[3] = 0.0f
            mVertices[4] = 1.0f
            mVertices[5] = points[2]
            mVertices[6] = points[3]
            mVertices[7] = 0.0f
            mVertices[8] = 1.0f
            mVertices[9] = 1.0f
            mVertices[10] = points[4]
            mVertices[11] = points[5]
            mVertices[12] = 0.0f
            mVertices[13] = 0.0f
            mVertices[14] = 0.0f
            mVertices[15] = points[6]
            mVertices[16] = points[7]
            mVertices[17] = 0.0f
            mVertices[18] = 1.0f
            mVertices[19] = 0.0f
            mVertexBuffer.position(0)
            mVertexBuffer.put(mVertices)
            mVertexBuffer.position(0)
            GLES20.glVertexAttribPointer(
                mPositionAttributeLocation,
                3,
                GLES10.GL_FLOAT,
                false,
                5 * FLOAT_BYTE_LENGTH,
                mVertexBuffer
            )
            mVertexBuffer.position(3)
            GLES20.glVertexAttribPointer(
                mTexCoordAttributeLocation,
                2,
                GLES10.GL_FLOAT,
                false,
                5 * FLOAT_BYTE_LENGTH,
                mVertexBuffer
            )
            GLES20.glEnableVertexAttribArray(mPositionAttributeLocation)
            GLES20.glEnableVertexAttribArray(mTexCoordAttributeLocation)
            GLES20.glDrawArrays(GLES10.GL_TRIANGLE_STRIP, 0, 4)
            if (mBShowMagnifier) renderMagnifier(
                pressPt.x,
                pressPt.y,
                gl
            )
        }

        private fun renderMagnifier(x: Float, y: Float, gl: GL10?) {
            //复用之前的纹理，减少重复渲染纹理
            val width = (magnifierRight - magnifierLeft) / 8
            val height = (magnifierTop - magnifierBtm) / 8
            var l: Float = if (x - width / 2 < 0) 0f else x - width / 2
            var r: Float = if (x + width / 2 > 1) 1f else x + width / 2
            var t: Float = if (y - height / 2 < 0) 0f else y - height / 2
            var b: Float = if (y + height / 2 > 1) 1f else y + height / 2
            l = if (r == 1f) r - width else l
            r = if (l == 0f) l + width else r
            t = if (b == 1f) b - height else t
            b = if (t == 0f) t + height else b
            mVertices[0] = magnifierLeft
            mVertices[1] = magnifierBtm
            mVertices[2] = 0.0f
            mVertices[3] = l
            mVertices[4] = b
            mVertices[5] = magnifierRight
            mVertices[6] = magnifierBtm
            mVertices[7] = 0.0f
            mVertices[8] = r
            mVertices[9] = b
            mVertices[10] = magnifierLeft
            mVertices[11] = magnifierTop
            mVertices[12] = 0.0f
            mVertices[13] = l
            mVertices[14] = t
            mVertices[15] = magnifierRight
            mVertices[16] = magnifierTop
            mVertices[17] = 0.0f
            mVertices[18] = r
            mVertices[19] = t
            mVertexBuffer.position(0)
            mVertexBuffer.put(mVertices)
            mVertexBuffer.position(0)
            GLES20.glVertexAttribPointer(
                mPositionAttributeLocation,
                3,
                GLES10.GL_FLOAT,
                false,
                5 * FLOAT_BYTE_LENGTH,
                mVertexBuffer
            )
            mVertexBuffer.position(3)
            GLES20.glVertexAttribPointer(
                mTexCoordAttributeLocation,
                2,
                GLES10.GL_FLOAT,
                false,
                5 * FLOAT_BYTE_LENGTH,
                mVertexBuffer
            )
            GLES20.glEnableVertexAttribArray(mPositionAttributeLocation)
            GLES20.glEnableVertexAttribArray(mTexCoordAttributeLocation)
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
            GLES20.glDisableVertexAttribArray(mPositionAttributeLocation)
            GLES20.glDisableVertexAttribArray(mTexCoordAttributeLocation)
            renderWindow(gl)
        }

        private fun renderWindow(gl: GL10?) {
            mVertexBuffer.position(0)
            GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "bDrawWindow"), 1)
            GLES20.glVertexAttribPointer(
                mPositionAttributeLocation,
                3,
                GLES10.GL_FLOAT,
                false,
                5 * FLOAT_BYTE_LENGTH,
                mVertexBuffer
            )
            GLES20.glEnableVertexAttribArray(mPositionAttributeLocation)
            if (magnifierTop > 0.9) {
                GLES20.glLineWidth(10.0f)
                GLES20.glDrawElements(
                    GLES20.GL_LINE_STRIP,
                    3,
                    GLES20.GL_UNSIGNED_BYTE,
                    ByteBuffer.wrap(byteArrayOf(1, 3, 2))
                )
                GLES20.glLineWidth(5.0f)
                GLES20.glDrawElements(
                    GLES20.GL_LINE_STRIP,
                    3,
                    GLES20.GL_UNSIGNED_BYTE,
                    ByteBuffer.wrap(byteArrayOf(2, 0, 1))
                )
                GLES20.glDrawElements(
                    GLES20.GL_POINTS,
                    1,
                    GLES20.GL_UNSIGNED_BYTE,
                    ByteBuffer.wrap(byteArrayOf(0))
                )
            } else {
                GLES20.glLineWidth(10.0f)
                GLES20.glDrawElements(
                    GLES20.GL_LINE_STRIP,
                    3,
                    GLES20.GL_UNSIGNED_BYTE,
                    ByteBuffer.wrap(byteArrayOf(0, 1, 3))
                )
                GLES20.glLineWidth(5.0f)
                GLES20.glDrawElements(
                    GLES20.GL_LINE_STRIP,
                    3,
                    GLES20.GL_UNSIGNED_BYTE,
                    ByteBuffer.wrap(byteArrayOf(0, 2, 3))
                )
                GLES20.glDrawElements(
                    GLES20.GL_POINTS,
                    1,
                    GLES20.GL_UNSIGNED_BYTE,
                    ByteBuffer.wrap(byteArrayOf(2))
                )
            }
            GLES20.glDisableVertexAttribArray(mPositionAttributeLocation)
            val w = magnifierRight - magnifierLeft
            val h = magnifierTop - magnifierBtm
            mVertices[0] = magnifierLeft + w / 10
            mVertices[1] = magnifierBtm + h / 2
            mVertices[2] = magnifierLeft + w * 4 / 10
            mVertices[3] = magnifierBtm + h / 2
            mVertices[4] = magnifierLeft + w * 6 / 10
            mVertices[5] = magnifierBtm + h / 2
            mVertices[6] = magnifierLeft + w * 9 / 10
            mVertices[7] = magnifierBtm + h / 2
            mVertices[8] = magnifierLeft + w / 2
            mVertices[9] = magnifierBtm + h / 10
            mVertices[10] = magnifierLeft + w / 2
            mVertices[11] = magnifierBtm + h * 4 / 10
            mVertices[12] = magnifierLeft + w / 2
            mVertices[13] = magnifierBtm + h * 6 / 10
            mVertices[14] = magnifierLeft + w / 2
            mVertices[15] = magnifierBtm + h * 9 / 10
            mVertexBuffer.put(mVertices)
            mVertexBuffer.position(0)
            GLES20.glVertexAttribPointer(
                mPositionAttributeLocation,
                2,
                GLES10.GL_FLOAT,
                false,
                2 * FLOAT_BYTE_LENGTH,
                mVertexBuffer
            )
            GLES20.glEnableVertexAttribArray(mPositionAttributeLocation)
            GLES20.glLineWidth(5.0f)
            var i = 0
            while (i < 8) {
                GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, i, 2)
                i += 2
            }
            GLES20.glDisableVertexAttribArray(mPositionAttributeLocation)
        }
    }
}