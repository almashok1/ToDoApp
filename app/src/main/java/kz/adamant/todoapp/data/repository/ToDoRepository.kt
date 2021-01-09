package kz.adamant.todoapp.data.repository

import androidx.lifecycle.LiveData
import kz.adamant.todoapp.data.ToDoDao
import kz.adamant.todoapp.data.models.ToDoData

class ToDoRepository(private val toDoDao: ToDoDao) {
    val getAllData: LiveData<List<ToDoData>> = toDoDao.getAllData()

    suspend fun insertData(toDoData: ToDoData) {
        toDoDao.insertData(toDoData)
    }

    suspend fun updateData(toDoData: ToDoData) {
        toDoDao.updateData(toDoData)
    }

    suspend fun deleteItem(toDoData: ToDoData) {
        toDoDao.deleteItem(toDoData)
    }

    suspend fun deleteAll() {
        toDoDao.deleteAll()
    }

    fun searchDatabase(searchString: String) : LiveData<List<ToDoData>>{
        return toDoDao.searchDatabase(searchString)
    }


    fun sortByHighPriority() : LiveData<List<ToDoData>> {
        return toDoDao.sortByHighPriority()
    }


    fun sortByLowPriority() : LiveData<List<ToDoData>> {
        return toDoDao.sortByLowPriority()
    }
}