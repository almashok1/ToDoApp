package kz.adamant.todoapp.data.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.adamant.todoapp.data.ToDoDao
import kz.adamant.todoapp.data.ToDoDatabase
import kz.adamant.todoapp.data.models.ToDoData
import kz.adamant.todoapp.data.repository.ToDoRepository



class ToDoViewModel(application: Application) : AndroidViewModel(application) {
    private var toDoDao: ToDoDao = ToDoDatabase.getDatabase(application).toDoDao()
    private val repository: ToDoRepository

    private val _getAllData: LiveData<List<ToDoData>>
    private val _getAllDataHighPriority : LiveData<List<ToDoData>>
    private val _getAllDataLowPriority : LiveData<List<ToDoData>>

    val allData
        get() = _getAllData
    val allDataHighPriority
        get() = _getAllDataHighPriority
    val allDataLowPriority
        get() = _getAllDataLowPriority

    init {
        repository = ToDoRepository(toDoDao)
        _getAllData = repository.getAllData
        _getAllDataHighPriority = repository.sortByHighPriority()
        _getAllDataLowPriority = repository.sortByLowPriority()
    }

    fun insertData(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(toDoData)
        }
    }

    fun updateData(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(toDoData)
        }
    }

    fun deleteItem(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(toDoData)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun searchQuery(searchString : String) : LiveData<List<ToDoData>>{
        return repository.searchDatabase(searchString)
    }
}