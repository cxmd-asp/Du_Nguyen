package com.example.safarione.module.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.safarione.module.database.entity.ChatRoomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatRoomDao {

    @Query("SELECT * FROM tbl_chat_room")
    fun getAll(): Flow<List<ChatRoomEntity>>

    @Query("SELECT * FROM tbl_chat_room WHERE owner_id = :ownerId")
    fun loadByOwnerId(ownerId: Long): Flow<List<ChatRoomEntity>>

    @Query("SELECT * FROM tbl_chat_room WHERE id = :id")
    fun findOneById(id: Long): ChatRoomEntity?

    @Insert
    fun insertAll(vararg rooms: ChatRoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createChatRoom(room: ChatRoomEntity) : Long

    @Delete
    fun delete(room: ChatRoomEntity)
}