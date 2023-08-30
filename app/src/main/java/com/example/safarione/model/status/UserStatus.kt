package com.example.safarione.model.status

enum class UserStatus : IntEnum {
    ONLINE, OFFLINE;

    override val uid: Int
        get() = ordinal
}