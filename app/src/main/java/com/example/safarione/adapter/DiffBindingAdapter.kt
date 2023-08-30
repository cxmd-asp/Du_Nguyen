package com.example.safarione.adapter

import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class DiffBindingAdapter<HOLDER : RecyclerView.ViewHolder, MODEL>(
    diffCallback: DiffUtil.ItemCallback<MODEL>,
) : RecyclerView.Adapter<HOLDER>() {

    private val currentListChangedListener: AsyncListDiffer.ListListener<MODEL> =
        AsyncListDiffer.ListListener<MODEL> { previousList, currentList ->
            this@DiffBindingAdapter.onCurrentListChanged(
                previousList, currentList
            )
        }

    @Suppress("LeakingThis")
    private val asyncListDiffer: AsyncListDiffer<MODEL> = AsyncListDiffer<MODEL>(
        AdapterListUpdateCallback(this), AsyncDifferConfig.Builder(diffCallback).build()
    ).also {
        it.addListListener(currentListChangedListener)
    }

    protected fun getItem(position: Int): MODEL = asyncListDiffer.currentList[position]

    override fun getItemCount() = asyncListDiffer.currentList.size

    fun submit(data: List<MODEL>) {
        asyncListDiffer.submitList(data)
    }

    open fun onCurrentListChanged(previousList: List<MODEL>, currentList: List<MODEL>) {

    }
}