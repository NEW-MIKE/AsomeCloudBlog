package com.example.astroclient.util

import android.util.Log
import com.example.astroclient.BuildConfig

fun <T> T.loge(tag: String, vararg objs: Any) {
    if (BuildConfig.DEBUG) Log.e(tag, parseString(objs))
}

fun <T> T.logi(tag: String, vararg objs: Any) {
    if (BuildConfig.DEBUG) Log.i(tag, parseString(objs))
}