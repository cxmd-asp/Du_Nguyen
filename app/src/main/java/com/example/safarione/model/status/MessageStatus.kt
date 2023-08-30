package com.example.safarione.model.status

enum class MessageStatus : IntEnum {
    PENDING, RECEIVED, READ;

    override val uid: Int
        get() = ordinal
}