package com.ngoctuan.speech.db

import android.app.Application
import androidx.lifecycle.LiveData

class AccountRepository(app: Application) {
    private val accDAO: AccountDAO

    init {
        val accDB: AccountDB = AccountDB.getInstance(app)
        accDAO = accDB.getAccountDao()
    }

    fun updateAccountByID(acc: Account) = accDAO.updateAccountByID(acc)
    fun insertAccount(acc: Account) = accDAO.insertAccount(acc)
    fun getAccountByID(ID: Int): Account = accDAO.getAccountById(ID)
}