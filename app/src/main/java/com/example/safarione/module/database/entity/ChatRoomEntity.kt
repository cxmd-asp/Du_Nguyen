package com.example.safarione.module.database.entity

import androidx.room.*

@Entity(
    tableName = "tbl_chat_room",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["owner_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["owner_id"])]
)
data class ChatRoomEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "owner_id") val ownerId: Long,
)