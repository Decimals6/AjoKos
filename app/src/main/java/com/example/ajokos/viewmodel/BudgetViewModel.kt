package com.example.ajokos.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.room.Database
import com.example.ajokos.model.dao.BudgetDao
import com.example.ajokos.model.data.Budget
import com.example.ajokos.model.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class BudgetViewModel(application: Application) : AndroidViewModel(application),
    CoroutineScope {
    private val budgetDao = AppDatabase.getDatabase(application).budgetDao()
    val budgetLD = MutableLiveData<List<Budget>>()
    val selectedBudget = MutableLiveData<Budget>()
    val budgetLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()
    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    fun getBudget(userId: Int) {
        loadingLD.value = true
        budgetLoadErrorLD.value = false
        launch {
            val db = AppDatabase.getDatabase(
                getApplication()
            )

            budgetLD.postValue(db.budgetDao().getAllBudgets(userId))
            loadingLD.postValue(false)
        }
    }
    fun getBudgetById(id:Int) {
        launch {
            val db = AppDatabase.getDatabase(getApplication())
            selectedBudget.postValue(db.budgetDao().selectBudget(id))
        }
    }

    fun addBudget(budget: Budget){
        viewModelScope.launch(Dispatchers.IO) {
            budgetDao.insert(budget)
        }
    }
}


