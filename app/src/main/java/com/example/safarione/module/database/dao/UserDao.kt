package com.example.safarione.module.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.safarione.module.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM tbl_user")
    fun getAll(): Flow<List<UserEntity>>

    @Query("SELECT * FROM tbl_user")
    fun getPaging(): PagingSource<Int, UserEntity>

    @Query("SELECT * FROM tbl_user WHERE INSTR(name, :keyword) OR INSTR(phone, :keyword) ORDER BY id ASC")
    fun getPaging(keyword: String): PagingSource<Int, UserEntity>

    @Query("SELECT * FROM tbl_user WHERE id = :id")
    fun getById(id: Long): UserEntity?

    @Query("SELECT * FROM tbl_user WHERE phone = :phone")
    fun getByPhone(phone: String): UserEntity?

    @Insert
    fun insertAll(vararg users: UserEntity)

    @Delete
    fun delete(user: UserEntity)
}