package com.example.safarione.module.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.RoomDatabase.Callback
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.safarione.model.status.UserStatus
import com.example.safarione.module.database.converter.RoomMessageStatusConverter
import com.example.safarione.module.database.converter.RoomUserStatusConverter
import com.example.safarione.module.database.dao.ChatRoomDao
import com.example.safarione.module.database.dao.ChatRoomMemberDao
import com.example.safarione.module.database.dao.MessageDao
import com.example.safarione.module.database.dao.UserDao
import com.example.safarione.module.database.entity.ChatRoomEntity
import com.example.safarione.module.database.entity.MessageEntity
import com.example.safarione.module.database.entity.RefChatMemberEntity
import com.example.safarione.module.database.entity.UserEntity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@Database(
    entities = [ChatRoomEntity::class, MessageEntity::class, RefChatMemberEntity::class, UserEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    value = [
        RoomUserStatusConverter::class,
        RoomMessageStatusConverter::class
    ]
)
abstract class ChatDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun messageDao(): MessageDao

    abstract fun chatRoomDao(): ChatRoomDao

    abstract fun chatRoomMemberDao(): ChatRoomMemberDao
}

private fun onCreatePrePopulateUser() = object : Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }
        MainScope().launch(exceptionHandler) {
            db.beginTransaction()
            listOf(
                "909196671061",
                "909824830353",
                "909810565026",
                "909726822209",
                "909673129072",
                "909352982193",
                "909394105461",
                "909785876597",
                "909769646054",
                "909230524267",
                "909725319593",
                "909293654285",
                "909609412760",
                "909314268942",
                "909369272283",
                "909369862943",
                "909171221894",
                "908517758688",
                "909128920474",
                "908915696685",
                "908596317889",
                "908731889233",
                "909007204071",
                "909031808099",
                "906403076396",
                "905738049886",
                "906033669429",
                "905938787264",
                "905886991498",
                "906312887996",
                "906281728872",
                "905842804256",
                "905869839941",
                "909892757447",
                "909947868685",
                "907523483700",
                "907509873405",
                "907562599010",
                "907447103056",
                "907626551982",
                "907809995138",
                "907649420655",
                "907363704473",
                "907232609234",
                "907250692258",
                "908387443937",
                "907877678397",
                "908380020814",
                "908366828925",
                "908356302264",
                "907923461208",
                "908175316903",
                "906802776716",
                "906900981361",
                "906761879311",
                "907094014511",
                "906952330362",
                "906988081670",
                "907141157571",
                "906591856200",
                "907043284897",
                "906491180932",
                "903780109354",
                "903767746652",
                "903640742591",
                "904647437213",
                "904196911237",
                "904813481725",
                "904792375256",
                "904314828329",
                "904547704594",
                "904752272635",
                "904978677931",
                "905606438305",
                "902758153655",
                "902733514864",
                "902564703770",
                "902324815571",
                "901326399077",
                "901312626700",
                "901310215937",
                "900720666839",
                "900721344125",
                "901219920188",
                "901070959167",
                "903452548591",
                "903245522354",
                "902790276039",
                "903218581032",
                "902887931952",
                "903375239174",
                "903364838108",
                "903349386025",
                "902053916023",
                "901788743325",
                "901438371646",
                "901811552798",
                "901694758207",
                "901531140461",
                "901678020138"
            ).forEach {
                db.insert("tbl_user", SQLiteDatabase.CONFLICT_REPLACE, ContentValues().apply {
                    put("id", it.toLong())
                    putNull("name")
                    putNull("avatar")
                    put("status", RoomUserStatusConverter.fromEnum(UserStatus.OFFLINE))
                    put("phone", it)
                })
            }
            db.setTransactionSuccessful()
        }.invokeOnCompletion {
            db.endTransaction()
        }
    }
}

fun createChatDatabase(context: Context): ChatDatabase =
    Room.databaseBuilder(context, ChatDatabase::class.java, "chat_database")
        .fallbackToDestructiveMigration()
        .addCallback(onCreatePrePopulateUser())
        .build()