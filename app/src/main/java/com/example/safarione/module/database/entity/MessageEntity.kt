package com.example.safarione.module.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.safarione.model.status.MessageStatus

@Entity(
    tableName = "tbl_message",
    foreignKeys = [
        ForeignKey(
            entity = ChatRoomEntity::class,
            parentColumns = ["id"],
            childColumns = ["chat_room_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["sender_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [Index(value = ["sender_id", "chat_room_id"])]
)
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "date") val date: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "sender_id", index = true) val senderId: Long,
    @ColumnInfo(name = "chat_room_id", index = true) val chatRoomId: Long,
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "status") val status: MessageStatus = MessageStatus.PENDING
)