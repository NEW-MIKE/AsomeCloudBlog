package com.example.astroclient.view

import android.content.Context
import android.util.AttributeSet
import android.widget.Checkable
import android.widget.TextView
import com.example.astroclient.R

class MyTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    TextView(context, attrs, defStyleAttr, defStyleRes), Checkable {
    var checkedColor: Int = 0
    var unCheckedColor: Int = 0

    init {
        var attrbutes = context.obtainStyledAttributes(attrs, R.styleable.MyTextView)
        checkedColor = attrbutes.getColor(R.styleable.MyTextView_checkedColor,  resources.getColor(R.color.teal_700))
        unCheckedColor = attrbutes.getColor(R.styleable.MyTextView_uncheckedColor, resources.getColor(R.color.teal_700))
    }

    private var mChecked = false

    override fun setChecked(checked: Boolean) {
        if (mChecked != checked) {
            mChecked = checked
            refreshDrawableState()
            setBackgroundColor(if (checked) checkedColor else unCheckedColor)
        }
    }

    override fun isChecked(): Boolean {
        return mChecked
    }

    override fun toggle() {
        isChecked = !mChecked
    }
}