package com.example.safarione.model.user

import android.os.Parcelable
import com.example.safarione.model.status.UserStatus
import com.example.safarione.module.database.entity.UserEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    val id: Long,
    val name: String? = null,
    val phone: String = "",
    val avatar: String? = null,
    var status: UserStatus = UserStatus.OFFLINE,
) : Parcelable

fun UserEntity.toModel() : User = User(
    id = id,
    avatar = avatar,
    phone = phone,
    name = name,
    status = UserStatus.OFFLINE
)
