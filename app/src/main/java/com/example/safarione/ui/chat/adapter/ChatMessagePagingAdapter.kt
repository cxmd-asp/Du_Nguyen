package com.example.safarione.ui.chat.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.safarione.R
import com.example.safarione.adapter.BindingHolder
import com.example.safarione.adapter.BindingPagingDataAdapter
import com.example.safarione.databinding.ItemMessageBinding
import com.example.safarione.databinding.ItemMessageSelfBinding
import com.example.safarione.model.chat.message.Message

class ChatMessagePagingAdapter :
    BindingPagingDataAdapter<Message, BindingHolder<*, Message>>(MessageDiffCallback) {

    companion object {
        private const val ITEM_TYPE_SEND = 0
        private const val ITEM_TYPE_RECEIVE = 1
    }

    override fun getItemViewType(position: Int): Int =
        if (true == getItem(position)?.isMyMessage) ITEM_TYPE_SEND else ITEM_TYPE_RECEIVE

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BindingHolder<*, Message> =
        when (viewType) {
            ITEM_TYPE_SEND -> SendMessageHolder(parent)
            ITEM_TYPE_RECEIVE -> ReceiveMessageHolder(parent)
            else -> throw IllegalStateException("Doesn't support viewType=$viewType")
        }

    private inner class SendMessageHolder(parent: ViewGroup) :
        BindingHolder<ItemMessageSelfBinding, Message>(parent, R.layout.item_message_self) {

        override fun onBind(position: Int, model: Message?) {
            super.onBind(position, model)
            binder.apply {
                if (model != null) {
                    lblContent.text = model.message
                    lblName.text = model.senderId.toString()
                } else {
                    lblContent.text = null
                    lblName.text = null
                }
            }
        }
    }

    private inner class ReceiveMessageHolder(parent: ViewGroup) :
        BindingHolder<ItemMessageBinding, Message>(parent, R.layout.item_message) {

        override fun onBind(position: Int, model: Message?) {
            super.onBind(position, model)
            binder.apply {
                if (model != null) {
                    lblContent.text = model.message
                    lblName.text = model.senderId.toString()
                } else {
                    lblContent.text = null
                    lblName.text = null
                }
            }
        }
    }

}

object MessageDiffCallback : DiffUtil.ItemCallback<Message>() {

    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean =
        oldItem.uuid == newItem.uuid

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean =
        oldItem == newItem

}