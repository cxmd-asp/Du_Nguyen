package com.example.safarione.module.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.safarione.module.database.entity.RefChatMemberEntity
import com.example.safarione.module.database.entity.UserEntity

@Dao
interface ChatRoomMemberDao {

    @Query("SELECT * FROM tbl_user WHERE id IN (SELECT ref_chat_member.user_id FROM ref_chat_member WHERE ref_chat_member.chat_room_id = :roomId)")
    fun getMembersInChatRoom(roomId: Long): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg refs: RefChatMemberEntity)

    @Delete
    fun delete(refs: RefChatMemberEntity)
}