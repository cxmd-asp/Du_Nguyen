package com.example.safarione.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface OnDataChangedListener {

    fun onDataSetEmpty()

    fun onDataSetFilled()
}

fun interface OnItemClick<T> {

    fun onItemClick(position: Int, view: View?, t: T?)

}

fun RecyclerView.removeAllItemDecorations() {
    ((itemDecorationCount - 1) downTo 0).forEach { removeItemDecorationAt(it) }
}