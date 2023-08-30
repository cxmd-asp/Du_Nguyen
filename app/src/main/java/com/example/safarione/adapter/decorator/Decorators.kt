package com.example.safarione.adapter.decorator

import android.content.Context
import android.graphics.Color
import androidx.annotation.AttrRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safarione.R
import com.google.android.material.color.MaterialColors
import com.google.android.material.divider.MaterialDividerItemDecoration

fun divideByLine(
    context: Context,
    orientation: Int = LinearLayoutManager.VERTICAL,
    thickness: Int = context.resources.getDimensionPixelSize(R.dimen.space_line_thin),
    insetStart: Int = 0,
    insetEnd: Int = 0,
    @AttrRes color: Int = R.attr.colorDivider,
    lastItemDecorated: Boolean = false,
): RecyclerView.ItemDecoration = MaterialDividerItemDecoration(context, orientation)
    .apply {
        dividerInsetEnd = insetEnd
        dividerInsetStart = insetStart
        dividerThickness = thickness
        isLastItemDecorated = lastItemDecorated
        dividerColor = MaterialColors.getColor(context, color, Color.DKGRAY)
    }