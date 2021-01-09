package kz.adamant.todoapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kz.adamant.todoapp.data.models.Priority

@Entity(tableName = "todo_table")
@Parcelize
data class ToDoData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    var priority: Priority,
    var description: String
) : Parcelable