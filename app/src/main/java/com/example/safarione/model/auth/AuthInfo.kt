package com.example.safarione.model.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthInfo(
    val uuid: Long,
    val username: String,
    val password: String
) : Parcelable