package com.example.safarione.module.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.safarione.model.status.UserStatus

@Entity(tableName = "tbl_user", indices = [Index(value = ["phone"], unique = true)])
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "phone") val phone: String,
    @ColumnInfo(name = "avatar") val avatar: String? = null,
    @ColumnInfo(name = "status") val status: UserStatus = UserStatus.OFFLINE,
)