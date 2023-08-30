package com.example.safarione.model.chat.message

import android.os.Parcelable
import com.example.safarione.model.status.MessageStatus
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Message(
    val uuid: Long = 0,
    val senderId: Long,
    val message: String = "",
    val date: Date = Date(),
    val status: MessageStatus = MessageStatus.PENDING,
    val isMyMessage: Boolean = false
) : Parcelable