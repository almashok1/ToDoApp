package kz.adamant.todoapp.data.viewmodels

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kz.adamant.todoapp.R
import kz.adamant.todoapp.data.models.Priority
import kz.adamant.todoapp.data.models.ToDoData

class SharedViewModel(application: Application) : AndroidViewModel(application) {
    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(false)

    fun checkIfDatabaseEmpty(toDoDataList: List<ToDoData>) {
        emptyDatabase.value = toDoDataList.isEmpty()
    }

    val listener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            (parent?.getChildAt(0) as TextView)
                .setTextColor(
                    ContextCompat.getColor(
                        application,
                        when (position) {
                            0 -> R.color.red
                            1 -> R.color.yellow
                            2 -> R.color.green
                            else -> R.color.green
                        }
                    )
                )
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}

    }

    fun verifyDataFromUser(title: String, description: String): Boolean {
        return if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            false
        } else !(title.isEmpty() || description.isEmpty())
    }

    fun parseStringPriority(priority: String): Priority {
        return when (priority.trim()) {
            "High Priority" -> Priority.HIGH
            "Medium Priority" -> Priority.MEDIUM
            "Low Priority" -> Priority.LOW
            else -> Priority.LOW
        }
    }
}