package com.example.safarione.ui.member.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.safarione.R
import com.example.safarione.adapter.BindingHolder
import com.example.safarione.adapter.BindingPagingDataAdapter
import com.example.safarione.adapter.DiffBindingAdapter
import com.example.safarione.adapter.OnItemClick
import com.example.safarione.adapter.decorator.divideByLine
import com.example.safarione.adapter.removeAllItemDecorations
import com.example.safarione.databinding.ItemMemberBinding
import com.example.safarione.model.user.User

class ChatMemberAdapter(
    private val itemClick: OnItemClick<User>
) : BindingPagingDataAdapter<User, BindingHolder<*, User>>(
    MemberDiffCallback
) {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.apply {
            removeAllItemDecorations()
            addItemDecoration(divideByLine(context))
        }
    }

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BindingHolder<*, User> = UserHolder(parent)

    private inner class UserHolder(parent: ViewGroup) :
        BindingHolder<ItemMemberBinding, User>(parent, R.layout.item_member) {

        override fun onCreate() {
            super.onCreate()
            registerRootViewItemClickEvent(itemClick)
        }

        override fun onBind(position: Int, model: User?) {
            super.onBind(position, model)
            model?.let {
                binder.lblTitle.text = model.phone
                binder.executePendingBindings()
            }
        }
    }

}

object MemberDiffCallback : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem.phone == newItem.phone

}