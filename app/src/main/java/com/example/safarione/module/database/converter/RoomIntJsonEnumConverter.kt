package com.example.safarione.module.database.converter

import android.util.SparseArray
import androidx.room.TypeConverter
import com.example.safarione.model.status.IntEnum
import com.example.safarione.model.status.MessageStatus
import com.example.safarione.model.status.UserStatus

abstract class RoomIntJsonEnumConverter<T>(
    private val defaultValue: T? = null, values: Array<T>
) where T : Enum<T>, T : IntEnum {

    private val sparseValues: SparseArray<T> = SparseArray<T>(values.size).apply {
        values.forEach {
            this.append(it.uid, it)
        }
    }

    @TypeConverter
    fun toEnum(value: Int?): T? = value?.let {
        sparseValues[value]
    } ?: defaultValue

    @TypeConverter
    fun fromEnum(enum: T?): Int? = enum?.uid
}

object RoomUserStatusConverter : RoomIntJsonEnumConverter<UserStatus>(UserStatus.OFFLINE, enumValues())

object RoomMessageStatusConverter: RoomIntJsonEnumConverter<MessageStatus>(MessageStatus.PENDING, enumValues())