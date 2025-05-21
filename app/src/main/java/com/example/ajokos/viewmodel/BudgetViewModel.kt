package com.example.ajokos.viewmodel

import androidx.lifecycle.*
import com.example.ajokos.model.data.Budget
import kotlinx.coroutines.launch

class BudgetViewModel(private val repository: BudgetRepository) : ViewModel() {
    fun getAllBudgets(userId: Int): LiveData<List<Budget>> {
        return repository.getAllBudgets(userId)
    }

    fun insert(budget: Budget) = viewModelScope.launch {
        repository.insert(budget)
    }

    fun update(budget: Budget) = viewModelScope.launch {
        repository.update(budget)
    }

    fun delete(budget: Budget) = viewModelScope.launch {
        repository.delete(budget)
    }
}

class BudgetViewModelFactory(private val repository: BudgetRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BudgetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BudgetViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
