package com.ngoctuan.speech.db


import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class AccountViewModel(app: Application) : ViewModel() {

    private val accountRepository: AccountRepository = AccountRepository(app)
    fun updateAccount(account: Account) = viewModelScope.launch {
        accountRepository.updateAccountByID(account)
    }

    fun insertAccount(account: Account) = viewModelScope.launch {
        accountRepository.insertAccount(account)
    }

    fun getAccountByID(ID: Int): Account = accountRepository.getAccountByID(ID)
    class AccountViewModelFactory(private val application: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AccountViewModel(application) as T
            }
            throw IllegalArgumentException("Unable construct viewModel")
        }
    }
}