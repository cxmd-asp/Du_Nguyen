package com.example.safarione.module.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.safarione.module.database.entity.MessageEntity

@Dao
interface MessageDao {

    @Query("SELECT * FROM tbl_message WHERE tbl_message.chat_room_id = :roomId ORDER BY date DESC")

    fun loadPagedMessageInRoom(roomId: Long): PagingSource<Int, MessageEntity>

    @Insert
    fun insertAll(vararg messages: MessageEntity)

    @Delete
    fun delete(message: MessageEntity)
}