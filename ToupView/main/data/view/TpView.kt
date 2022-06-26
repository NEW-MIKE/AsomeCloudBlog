package com.example.astroclient.view

import android.content.Context
import android.graphics.*
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import com.example.astroclient.BuildConfig
import java.util.*
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLContext
import javax.microedition.khronos.egl.EGLDisplay


class TpView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    GLSurfaceView(context, attrs) {

    companion object {
        private const val TAG = "TpView"

        private fun checkEglError(prompt: String, egl: EGL10) {
            var error: Int
            while (egl.eglGetError().also { error = it } != EGL10.EGL_SUCCESS) {
                Log.e(TAG, String.format(Locale.getDefault(), "%s: EGL error: 0x%x", prompt, error))
            }
        }
    }



    var render: TpRender

    init {
        render = TpRender(context)
        init(false, 0, 0)
    }

    private fun init(translucent: Boolean, depth: Int, stencil: Int) {
        if (translucent) {
            this.holder.setFormat(PixelFormat.TRANSLUCENT)
        }
        setEGLContextFactory(ContextFactory())
        setEGLContextClientVersion(2)
        setEGLConfigChooser(
            if (translucent) ConfigChooser(
                8,
                8,
                8,
                8,
                depth,
                stencil
            ) else ConfigChooser(5, 6, 5, 0, depth, stencil)
        )

        setRenderer(render)
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    private class ContextFactory : EGLContextFactory {
        override fun createContext(
            egl: EGL10,
            display: EGLDisplay,
            eglConfig: EGLConfig
        ): EGLContext {
            checkEglError("Before eglCreateContext", egl)
            val attribs = intArrayOf(EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE)
            val context = egl.eglCreateContext(display, eglConfig, EGL10.EGL_NO_CONTEXT, attribs)
            checkEglError("After eglCreateContext", egl)
            return context
        }

        override fun destroyContext(egl: EGL10, display: EGLDisplay, context: EGLContext) {
            egl.eglDestroyContext(display, context)
        }

        private val EGL_CONTEXT_CLIENT_VERSION = 0x3098
    }

    private class ConfigChooser(// Subclasses can adjust these values:
        protected var mRedSize: Int,
        protected var mGreenSize: Int,
        protected var mBlueSize: Int,
        protected var mAlphaSize: Int,
        protected var mDepthSize: Int,
        protected var mStencilSize: Int
    ) :
        EGLConfigChooser {
        override fun chooseConfig(egl: EGL10, display: EGLDisplay): EGLConfig {
            val num_config = IntArray(1)
            egl.eglChooseConfig(display, CONFIG_ATTRIBS, null, 0, num_config)
            val numConfigs = num_config[0]
            require(numConfigs > 0) { "No configs match configSpec" }
            val configs = arrayOfNulls<EGLConfig>(numConfigs)
            egl.eglChooseConfig(
                display, CONFIG_ATTRIBS, configs, numConfigs,
                num_config
            )
            if (BuildConfig.DEBUG) {
                printConfigs(egl, display, configs)
            }
            return chooseConfig(egl, display, configs)!!
        }

        fun chooseConfig(
            egl: EGL10, display: EGLDisplay,
            configs: Array<EGLConfig?>
        ): EGLConfig? {
            for (config in configs) {
                val d = findConfigAttrib(egl, display, config, EGL10.EGL_DEPTH_SIZE, 0)
                val s = findConfigAttrib(egl, display, config, EGL10.EGL_STENCIL_SIZE, 0)
                if (d < mDepthSize || s < mStencilSize) continue
                val r = findConfigAttrib(egl, display, config, EGL10.EGL_RED_SIZE, 0)
                val g = findConfigAttrib(egl, display, config, EGL10.EGL_GREEN_SIZE, 0)
                val b = findConfigAttrib(egl, display, config, EGL10.EGL_BLUE_SIZE, 0)
                val a = findConfigAttrib(egl, display, config, EGL10.EGL_ALPHA_SIZE, 0)
                if (r == mRedSize && g == mGreenSize && b == mBlueSize && a == mAlphaSize) return config
            }
            return null
        }

        private fun findConfigAttrib(
            egl: EGL10,
            display: EGLDisplay,
            config: EGLConfig?,
            attribute: Int,
            defaultValue: Int
        ): Int {
            return if (egl.eglGetConfigAttrib(display, config, attribute, mValue)) {
                mValue[0]
            } else defaultValue
        }

        private fun printConfigs(egl: EGL10, display: EGLDisplay, configs: Array<EGLConfig?>) {
            for (config in configs) {
                printConfig(egl, display, config)
            }
        }

        private fun printConfig(egl: EGL10, display: EGLDisplay, config: EGLConfig?) {
            val attributes = intArrayOf(
                EGL10.EGL_BUFFER_SIZE, EGL10.EGL_ALPHA_SIZE,
                EGL10.EGL_BLUE_SIZE,
                EGL10.EGL_GREEN_SIZE,
                EGL10.EGL_RED_SIZE,
                EGL10.EGL_DEPTH_SIZE,
                EGL10.EGL_STENCIL_SIZE,
                EGL10.EGL_CONFIG_CAVEAT,
                EGL10.EGL_CONFIG_ID,
                EGL10.EGL_LEVEL,
                EGL10.EGL_MAX_PBUFFER_HEIGHT,
                EGL10.EGL_MAX_PBUFFER_PIXELS,
                EGL10.EGL_MAX_PBUFFER_WIDTH,
                EGL10.EGL_NATIVE_RENDERABLE,
                EGL10.EGL_NATIVE_VISUAL_ID,
                EGL10.EGL_NATIVE_VISUAL_TYPE,
                0x3030,  // EGL10.EGL_PRESERVED_RESOURCES,
                EGL10.EGL_SAMPLES,
                EGL10.EGL_SAMPLE_BUFFERS,
                EGL10.EGL_SURFACE_TYPE,
                EGL10.EGL_TRANSPARENT_TYPE,
                EGL10.EGL_TRANSPARENT_RED_VALUE,
                EGL10.EGL_TRANSPARENT_GREEN_VALUE,
                EGL10.EGL_TRANSPARENT_BLUE_VALUE,
                0x3039,  // EGL10.EGL_BIND_TO_TEXTURE_RGB,
                0x303A,  // EGL10.EGL_BIND_TO_TEXTURE_RGBA,
                0x303B,  // EGL10.EGL_MIN_SWAP_INTERVAL,
                0x303C,  // EGL10.EGL_MAX_SWAP_INTERVAL,
                EGL10.EGL_LUMINANCE_SIZE, EGL10.EGL_ALPHA_MASK_SIZE,
                EGL10.EGL_COLOR_BUFFER_TYPE, EGL10.EGL_RENDERABLE_TYPE,
                0x3042 // EGL10.EGL_CONFORMANT
            )
            val names = arrayOf(
                "EGL_BUFFER_SIZE", "EGL_ALPHA_SIZE",
                "EGL_BLUE_SIZE", "EGL_GREEN_SIZE", "EGL_RED_SIZE",
                "EGL_DEPTH_SIZE", "EGL_STENCIL_SIZE", "EGL_CONFIG_CAVEAT",
                "EGL_CONFIG_ID", "EGL_LEVEL", "EGL_MAX_PBUFFER_HEIGHT",
                "EGL_MAX_PBUFFER_PIXELS", "EGL_MAX_PBUFFER_WIDTH",
                "EGL_NATIVE_RENDERABLE", "EGL_NATIVE_VISUAL_ID",
                "EGL_NATIVE_VISUAL_TYPE", "EGL_PRESERVED_RESOURCES",
                "EGL_SAMPLES", "EGL_SAMPLE_BUFFERS", "EGL_SURFACE_TYPE",
                "EGL_TRANSPARENT_TYPE", "EGL_TRANSPARENT_RED_VALUE",
                "EGL_TRANSPARENT_GREEN_VALUE",
                "EGL_TRANSPARENT_BLUE_VALUE", "EGL_BIND_TO_TEXTURE_RGB",
                "EGL_BIND_TO_TEXTURE_RGBA", "EGL_MIN_SWAP_INTERVAL",
                "EGL_MAX_SWAP_INTERVAL", "EGL_LUMINANCE_SIZE",
                "EGL_ALPHA_MASK_SIZE", "EGL_COLOR_BUFFER_TYPE",
                "EGL_RENDERABLE_TYPE", "EGL_CONFORMANT"
            )
            val value = IntArray(1)
            for (i in attributes.indices) {
                val attribute = attributes[i]
                val name = names[i]
                if (egl.eglGetConfigAttrib(display, config, attribute, value)) {
                    Log.w(
                        TAG, String.format(
                            Locale.getDefault(), "  %s: %d\n", name,
                            value[0]
                        )
                    )
                } else {
                    Log.w(TAG, String.format(Locale.getDefault(), "  %s: failed\n", name))
                    while (egl.eglGetError() != EGL10.EGL_SUCCESS);
                }
            }
        }

        private val mValue = IntArray(1)
        private val EGL_OPENGL_ES2_BIT = 4
        private val CONFIG_ATTRIBS = intArrayOf(
            EGL10.EGL_RED_SIZE, 4, EGL10.EGL_GREEN_SIZE, 4, EGL10.EGL_BLUE_SIZE, 4,
            EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT, EGL10.EGL_NONE
        )
    }
}