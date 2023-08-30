package com.example.safarione.model.chat.room

import android.os.Message
import android.os.Parcelable
import com.example.safarione.model.user.User
import com.example.safarione.module.database.entity.ChatRoomEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatRoom(
    val id: Long,
    val title: String = "",
    val ownerId: Long,
    val image: String = "",
    val members: List<User> = emptyList(),
    val message: List<Message> = emptyList(),
) : Parcelable

fun ChatRoomEntity.toModel(members: List<User> = emptyList()) : ChatRoom = ChatRoom(
    id = id,
    ownerId = ownerId,
    members = members,
    title = title
)

