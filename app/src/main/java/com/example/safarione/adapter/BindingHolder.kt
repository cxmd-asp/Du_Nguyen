package com.example.safarione.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

@Suppress("unused")
abstract class BindingHolder<BINDER : ViewDataBinding, MODEL> : Holder<MODEL> {
    protected val binder: BINDER

    constructor(binder: BINDER) : super(binder.root) {
        this.binder = binder
    }

    constructor(parent: ViewGroup, @LayoutRes layoutResId: Int) : super(
        inflateLayout(
            parent,
            layoutResId
        )
    ) {
        binder = requireNotNull(DataBindingUtil.bind(itemView))
    }

    companion object {
        fun <BINDER : ViewDataBinding> inflateBinding(
            parent: ViewGroup,
            @LayoutRes layoutRes: Int
        ): BINDER {
            return DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                layoutRes,
                parent,
                false
            )
        }

        fun inflateLayout(parent: ViewGroup, @LayoutRes layoutRes: Int): View {
            return LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        }
    }
}

val BindingHolder<*, *>.viewContext: Context get() = itemView.context
val ViewDataBinding.viewContext: Context get() = root.context