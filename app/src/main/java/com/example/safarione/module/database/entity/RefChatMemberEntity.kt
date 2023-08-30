package com.example.safarione.module.database.entity

import androidx.room.*

@Entity(
    tableName = "ref_chat_member",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = ChatRoomEntity::class,
        parentColumns = ["id"],
        childColumns = ["chat_room_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["user_id", "chat_room_id"],)]
)
data class RefChatMemberEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "user_id", index = true) val userId: Long,
    @ColumnInfo(name = "chat_room_id", index = true) val chatRoomId: Long,
)