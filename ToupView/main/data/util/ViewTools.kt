package com.example.astroclient.util

import android.app.Activity
import android.app.Dialog
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**反射**/
inline fun <reified VB : ViewBinding> Activity.inflate() = lazy {
    inflateBinding<VB>(layoutInflater).apply { setContentView(root) }
}

inline fun <reified VB : ViewBinding> Dialog.inflate() = lazy {
    inflateBinding<VB>(layoutInflater).apply { setContentView(root) }
}


/***
 * 依赖于bind方法, 并且
 */
inline fun <reified VB : ViewBinding> Fragment.bindView() =
    FragmentBindingDelegate {
        val clazz = VB::class.java
        clazz.getMethod("bind", View::class.java)
            .invoke(null, it) as VB
    }

/**非反射**/
inline fun <reified VB : ViewBinding> Activity.inflate(noinline block: (LayoutInflater) -> VB) =
    lazy {
        block(layoutInflater).apply { setContentView(root) }
    }

inline fun <reified VB : ViewBinding> Dialog.inflate(noinline block: (LayoutInflater) -> VB) =
    lazy {
        block(layoutInflater).apply { setContentView(root) }
    }

inline fun <reified VB : ViewBinding> Fragment.bindView(noinline block: (View) -> VB) =
    FragmentBindingDelegate(block)

@Suppress("UNCHECKED_CAST")
inline fun <reified VB : ViewBinding> inflateBinding(layoutInflater: LayoutInflater) =
    VB::class.java.getMethod("inflate", LayoutInflater::class.java)
        .invoke(null, layoutInflater) as VB


class FragmentBindingDelegate<VB : ViewBinding>(
    val block: (View) -> VB
) : ReadOnlyProperty<Fragment, VB> {

    private var isInitialized = false
    private var _binding: VB? = null
    private val binding: VB get() = _binding!!

    override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
        if (!isInitialized) {
            thisRef.viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun onDestroyView() {
                    _binding = null
                }
            })
            _binding = block(thisRef.requireView())
            isInitialized = true
        }
        return binding
    }
}

fun <T> T.createBitmap(w: Int, h: Int): Bitmap {
    return Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
}

