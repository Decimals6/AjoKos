package com.example.ajokos.viewmodel

import androidx.lifecycle.*
import com.example.ajokos.model.data.Expenses
import kotlinx.coroutines.launch

class ExpensesViewModel(private val repository: ExpensesRepository) : ViewModel() {

    fun getByUser(userId: Int): LiveData<List<Expenses>> {
        return repository.getByUser(userId)
    }

    fun getByBudget(budgetId: Int): LiveData<List<Expenses>> {
        return repository.getByBudget(budgetId)
    }

    fun insert(expense: Expenses) = viewModelScope.launch {
        repository.insert(expense)
    }

    fun delete(expense: Expenses) = viewModelScope.launch {
        repository.delete(expense)
    }
}

class ExpensesViewModelFactory(private val repository: ExpensesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpensesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpensesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
