package kz.adamant.todoapp.data

import androidx.room.TypeConverter
import kz.adamant.todoapp.data.models.Priority

class PriorityConverter {
    @TypeConverter
    fun fromPriority(priority: Priority) : String {
        return priority.name
    }

    @TypeConverter
    fun toPriority(priority: String) : Priority {
        return Priority.valueOf(priority)
    }
}