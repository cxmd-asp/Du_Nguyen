package com.example.safarione.adapter

import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

abstract class BindingPagingDataAdapter<Model : Any, Holder : BindingHolder<*, Model>>
    (diffCallback: DiffUtil.ItemCallback<Model>) : PagingDataAdapter<Model, Holder>(diffCallback) {

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        onCreateHolder(parent, viewType).also {
            it.onCreate()
        }

    abstract fun onCreateHolder(parent: ViewGroup, viewType: Int): Holder

    final override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position)
        holder.onBind(position, item)
    }

    protected var mItemClickListener: OnItemClick<Model>? = null

    protected fun setRootViewItemClick(target: BindingHolder<out ViewDataBinding, Model>) {
        target.registerRootViewItemClickEvent(mItemClickListener)
    }

    fun setItemClickListener(itemClick: OnItemClick<Model>?) {
        mItemClickListener = itemClick
    }

    fun setItemClickListener(listener: (position: Int, view: View?, model: Model?) -> Unit) {
        mItemClickListener = OnItemClick(listener)
    }
}