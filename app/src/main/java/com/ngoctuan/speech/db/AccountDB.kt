package com.ngoctuan.speech.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Account::class], version = 1)
abstract class AccountDB : RoomDatabase() {
    abstract fun getAccountDao(): AccountDAO

    companion object {
        @Volatile
        private var instance: AccountDB? = null
        fun getInstance(context: Context): AccountDB {
            if (instance == null) {
                instance = Room.databaseBuilder(context, AccountDB::class.java, "texttospeechDB2")
                    .allowMainThreadQueries().build()
            }
            return instance!!
        }
    }
}