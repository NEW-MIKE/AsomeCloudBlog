package com.mall.library.global

import android.os.Handler
import com.joanzapata.iconify.IconFontDescriptor
import com.joanzapata.iconify.Iconify
import com.mall.library.util.storage.MemoryStore

/**
 * 全局配置控制类
 */
class Configurator private constructor() {

    private val mIcons = ArrayList<IconFontDescriptor>()

    private object Holder {
        internal val INSTANCE = Configurator()
    }

    companion object {
        //这里获取到全局的存储容器
        private val mStore = MemoryStore.instance
        //Handler需要反复使用，不妨提前创建
        private val mHandler = Handler()

        internal val instance: Configurator
            get() = Holder.INSTANCE
    }

    init {
        //加一个标签，判断是否配置完成，现在还没开始配置
        mStore.addData(GlobalKeys.IS_CONFIGURE_READY, false)
        mStore.addData(GlobalKeys.HANDLER, mHandler)
    }

    private fun initIcons() {
        if (mIcons.size > 0) {
            val initializer = Iconify.with(mIcons[0])
            for (i in 1 until mIcons.size) {
                initializer.with(mIcons[i])
            }
        }
    }

    fun withIcon(descriptor: IconFontDescriptor): Configurator {
        mIcons.add(descriptor)
        return this
    }

    /**
     * 访问服务器端API设置
     */
    fun withApiHost(host: String): Configurator {
        mStore.addData(GlobalKeys.API_HOST, host)
        return this
    }

    fun withLoaderDelayed(delayed: Long): Configurator {
        mStore.addData(GlobalKeys.LOADER_DELAYED, delayed)
        return this
    }

    /**
     * 结束配置
     */
    fun configure() {
        mStore.addData(GlobalKeys.IS_CONFIGURE_READY, true)
        initIcons()
        //下面可以做一些回收动作
    }

    private fun checkConfiguration() {
        val isReady =
            mStore.getData<Boolean>(GlobalKeys.IS_CONFIGURE_READY)
        if (!isReady) {
            throw RuntimeException("config is not ready!")
        }
    }

    fun <T> getConfiguration(key: String): T {
        checkConfiguration()
        return mStore.getData(key)
    }

    fun <T> getConfiguration(key: Enum<*>): T {
        return getConfiguration(key.name)
    }
}