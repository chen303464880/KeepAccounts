package com.cjj.keepaccounts.view

import android.graphics.drawable.Drawable
import android.support.v7.widget.SimpleItemAnimator
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue

/**
 * @author chenjunjie
 * Created by CJJ on 2018/4/12 9:27.
 */
var android.widget.TextView.drawableStart: Drawable?
    get() {
        return compoundDrawables[0]
    }
    set(value) {
        setCompoundDrawables(value, drawableTop, drawableEnd, drawableBottom)
    }

var android.widget.TextView.drawableTop: Drawable?
    get() {
        return compoundDrawables[1]
    }
    set(value) {
        setCompoundDrawables(drawableStart, value, drawableEnd, drawableBottom)
    }

var android.widget.TextView.drawableEnd: Drawable?
    get() {
        return compoundDrawables[2]
    }
    set(value) {
        setCompoundDrawables(drawableStart, drawableTop, value, drawableBottom)
    }

var android.widget.TextView.drawableBottom: Drawable?
    get() {
        return compoundDrawables[3]
    }
    set(value) {
        setCompoundDrawables(drawableStart, drawableTop, drawableEnd, value)
    }

fun android.view.View.ripple() {
    val typedValue = TypedValue()
    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)
    val attribute = intArrayOf(android.R.attr.selectableItemBackground)
    val typedArray = context.theme.obtainStyledAttributes(typedValue.resourceId, attribute)
    background = typedArray.getDrawable(0)
}

fun android.view.View.rippleBorderless() {
    val typedValue = TypedValue()
    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)
    val attribute = intArrayOf(android.R.attr.selectableItemBackgroundBorderless)
    val typedArray = context.theme.obtainStyledAttributes(typedValue.resourceId, attribute)
    background = typedArray.getDrawable(0)
}

fun android.support.v7.widget.RecyclerView.clearItemAnimator() {
    (this.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
}

fun android.widget.EditText.addAfterTextChangedListener(afterTextChanged: (s: Editable) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            afterTextChanged.invoke(s)
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }
    })
}

fun android.widget.EditText.addBeforeTextChangedListener(beforeTextChanged: (s: CharSequence, start: Int, count: Int, after: Int) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable) {

        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            beforeTextChanged.invoke(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }
    })
}

fun android.widget.EditText.addTextChangedListener(onTextChanged: (s: CharSequence, start: Int, before: Int, count: Int) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            onTextChanged.invoke(s, start, before, count)
        }
    })
}