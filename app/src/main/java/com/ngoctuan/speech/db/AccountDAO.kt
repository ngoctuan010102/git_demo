package com.ngoctuan.speech.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
public interface AccountDAO {
    @Query("SELECT * FROM Account WHERE id=:ID")
    fun getAccountById(ID: Int): Account

    @Update
    fun updateAccountByID(acc: Account)

    @Insert
    fun insertAccount(acc: Account)
}